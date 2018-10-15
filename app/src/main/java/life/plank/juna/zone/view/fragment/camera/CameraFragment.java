package life.plank.juna.zone.view.fragment.camera;

import android.Manifest;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.camera.CameraHandler;
import life.plank.juna.zone.util.camera.CameraHandler.ImageSaver;
import life.plank.juna.zone.util.customview.AutoFitTextureView;
import life.plank.juna.zone.view.activity.camera.UploadActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.hardware.camera2.CameraCharacteristics.LENS_FACING;
import static android.hardware.camera2.CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP;
import static android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.UIDisplayUtil.displaySnackBar;
import static life.plank.juna.zone.util.camera.CameraHandler.chooseOptimalSize;
import static life.plank.juna.zone.util.camera.CameraHandler.chooseVideoSize;
import static life.plank.juna.zone.util.camera.CameraHandler.createMediaFile;
import static life.plank.juna.zone.util.camera.CameraHandler.getSensorToDeviceRotation;
import static life.plank.juna.zone.util.camera.PermissionHandler.CAMERA_PERMISSIONS;
import static life.plank.juna.zone.util.camera.PermissionHandler.CAMERA_PERMISSION_REQUEST_CODE;
import static life.plank.juna.zone.util.camera.PermissionHandler.STORAGE_PERMISSIONS;
import static life.plank.juna.zone.util.camera.PermissionHandler.STORAGE_PERMISSION_REQUEST_CODE_CAMERA;
import static life.plank.juna.zone.util.camera.PermissionHandler.requestCameraPermissions;
import static life.plank.juna.zone.util.camera.PermissionHandler.requestStoragePermissionsForCamera;

public class CameraFragment extends Fragment {

    private static final int STATE_PREVIEW = 2;
    private static final int STATE_WAIT_LOCK = 3;
    private static final String TAG = CameraFragment.class.getSimpleName();


    @BindView(R.id.camera_preview)
    AutoFitTextureView cameraPreview;
    @BindView(R.id.camera_flash_toggle)
    ImageButton cameraFlashToggleButton;
    @BindView(R.id.camera_capture)
    ImageButton cameraCaptureButton;
    @BindView(R.id.camera_flip)
    ImageButton cameraFlipButton;

    private boolean isForImage;
    private boolean isRecording = false;
    private int totalRotation;
    private int cameraFacing;
    private int captureState = STATE_PREVIEW;
    private String cameraId;
    private Size previewSize;
    private Size videoSize;
    private Size imageSize;
    private File mediaFolder;
    public String mediaFileName;
    private String boardId;

    private HandlerThread backgroundHandlerThread;
    private Handler backgroundHandler;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private MediaRecorder mediaRecorder;
    private ImageReader imageReader;
    private CameraCaptureSession previewCaptureSession;

    private ImageReader.OnImageAvailableListener onImageAvailableListener = reader -> backgroundHandler.post(new ImageSaver(this, reader.acquireLatestImage()));

    private CameraCaptureSession.CaptureCallback previewCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            process(result);
        }

        @SuppressWarnings("ConstantConditions")
        private void process(CaptureResult captureResult) {
            switch (captureState) {
                case STATE_PREVIEW:
//                    DO nothing
                    break;
                case STATE_WAIT_LOCK:
                    captureState = STATE_PREVIEW;
                    Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                            afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
//                        AF locked! Ready to capture image.
                        captureImage();
                    }
                    break;
            }
        }
    };
    private CameraDevice.StateCallback cameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            if (!isForImage && isRecording) {
                try {
                    mediaFileName = createMediaFile(isForImage, mediaFolder);
                    startVideoRecording();
                    mediaRecorder.start();
                } catch (Exception e) {
                    Log.e(TAG, "cameraDeviceStateCallback : onOpened(): ", e);
                }
                return;
            }
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
        }
    };
    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            setupCamera(width, height);
            connectCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    public CameraFragment() {
    }

    public static CameraFragment newInstance(boolean isForImage, String boardId) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putBoolean(ZoneApplication.getContext().getString(R.string.intent_is_camera_for_image), isForImage);
        args.putString(ZoneApplication.getContext().getString(R.string.intent_board_id), boardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        isForImage = args.getBoolean(getString(R.string.intent_is_camera_for_image));
        boardId = args.getString(getString(R.string.intent_board_id));
        cameraManager = (CameraManager) ZoneApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;
        mediaFolder = CameraHandler.createMediaFolderIfNotExists(isForImage);
        mediaRecorder = new MediaRecorder();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (cameraPreview.isAvailable()) {
            setupCamera(cameraPreview.getWidth(), cameraPreview.getHeight());
            connectCamera();
        } else {
            cameraPreview.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /**
     * Method for setting up the prerequisites for the camera (rotation, size, imageReader, etc.)
     */
    @SuppressWarnings({"ConstantConditions", "SuspiciousNameCombination"})
    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) ZoneApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(LENS_FACING) == LENS_FACING_FRONT) {
//                    skip the front camera
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(SCALER_STREAM_CONFIGURATION_MAP);
                int deviceOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                totalRotation = getSensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean isInPortrait = totalRotation == 90 || totalRotation == 270;
                int rotatedWidth = width;
                int rotatedHeight = height;
                if (isInPortrait) {
//                    Swap height and width if the device is in portrait mode
                    rotatedWidth = height;
                    rotatedHeight = width;
                }
//                TODO : try to use different classes in argument
                Size[] availableSizes = map.getOutputSizes(SurfaceTexture.class);
                videoSize = chooseVideoSize(availableSizes);
                previewSize = chooseOptimalSize(availableSizes, rotatedWidth, rotatedHeight, videoSize);
                imageSize = chooseOptimalSize(map.getOutputSizes(ImageFormat.JPEG), rotatedWidth, rotatedHeight, videoSize);
                imageReader = ImageReader.newInstance(imageSize.getWidth(), imageSize.getHeight(), ImageFormat.JPEG, 1);
                imageReader.setOnImageAvailableListener(onImageAvailableListener, backgroundHandler);
                this.cameraId = cameraId;
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "setupCamera(): ", e);
        }
    }

    /**
     * Method for connecting the camera through {@link CameraManager} after camera permission check
     */
    @AfterPermissionGranted(CAMERA_PERMISSION_REQUEST_CODE)
    private void connectCamera() {
        if (EasyPermissions.hasPermissions(ZoneApplication.getContext(), CAMERA_PERMISSIONS)) {
            CameraManager cameraManager = (CameraManager) ZoneApplication.getContext().getSystemService(Context.CAMERA_SERVICE);
            try {
                if (checkSelfPermission(ZoneApplication.getContext(), Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
                    return;
                }
                cameraManager.openCamera(cameraId, cameraDeviceStateCallback, backgroundHandler);
            } catch (Exception e) {
                Log.e(TAG, "connectCamera(): ", e);
            }
        } else {
            requestCameraPermissions(getActivity());
        }
    }

    /**
     * Method to start the camera preview on the {@link TextureView}
     */
    private void startPreview() {
        SurfaceTexture surfaceTexture = cameraPreview.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);
        try {
            closePreviewSession();
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    previewCaptureSession = session;
                    try {
                        previewCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
                    } catch (Exception e) {
                        Log.e(TAG, "startPreview : createCaptureSession(): ", e);
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    displaySnackBar(cameraPreview, R.string.failed_to_setup_camera_preview);
                }
            }, null);
        } catch (Exception e) {
            Log.e(TAG, "startPreview(): ", e);
        }
    }

    /**
     * NOTE : the calls to the {@link MediaRecorder} should be in THIS order for it to work properly:
     * 1 -> setVideoSource()
     * 2 -> setOutputFormat()
     * 3 -> setOutputFile()
     * 4 -> setVideoEncodingBitRate()
     * 5 -> setVideoFrameRate()
     * 6 -> setVideoSize()
     * 7 -> setVideoEncoder()
     * 8 -> setOrientationHint()
     * 9 -> prepare()
     */
    private void setupMediaRecorder() {
        try {
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(mediaFileName);
            mediaRecorder.setVideoEncodingBitRate(10000000);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOrientationHint(totalRotation);
            mediaRecorder.prepare();
        } catch (Exception e) {
            Log.e(TAG, "setupMediaRecorder(): ", e);
        }
    }

    /**
     * Method to begin the video recording
     */
    private void startVideoRecording() {
        if (cameraDevice == null || !cameraPreview.isAvailable() || previewSize == null) {
            return;
        }
        closePreviewSession();
        setupMediaRecorder();
        SurfaceTexture surfaceTexture = cameraPreview.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);
        Surface recordSurface = mediaRecorder.getSurface();
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            captureRequestBuilder.addTarget(previewSurface);
            captureRequestBuilder.addTarget(recordSurface);

            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, recordSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.setRepeatingRequest(captureRequestBuilder.build(), null, backgroundHandler);
                    } catch (Exception e) {
                        Log.e(TAG, "createCaptureSession(): ", e);
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, null);
        } catch (Exception e) {
            Log.e(TAG, "startVideoRecording(): ", e);
        } finally {
//            previewSurface.release();
        }
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST_CODE_CAMERA)
    private void prepareForImageCapture() {
        if (EasyPermissions.hasPermissions(ZoneApplication.getContext(), STORAGE_PERMISSIONS)) {
            lockFocus();
        } else {
            requestStoragePermissionsForCamera(getActivity());
        }
    }

    /**
     * Method to lock the camera focus (done just before the image capture)
     */
    private void lockFocus() {
        try {
            captureState = STATE_WAIT_LOCK;
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
            previewCaptureSession.capture(captureRequestBuilder.build(), previewCaptureCallback, backgroundHandler);
        } catch (Exception e) {
            Log.e(TAG, "lockFocus(): ", e);
        }
    }

    /**
     * Method to capture image from camera
     */
    private void captureImage() {
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());

//            Fixing orientation issue of image present on some devices
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, totalRotation);

            CameraCaptureSession.CaptureCallback stillCaptureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                    try {
                        mediaFileName = createMediaFile(isForImage, mediaFolder);
                        UploadActivity.launch(getActivity(), IMAGE, boardId, mediaFileName);
                        Objects.requireNonNull(getActivity()).finish();
                    } catch (Exception e) {
                        Log.e(TAG, "stillCaptureCallback : onCaptureStarted(): ", e);
                    }
                }
            };
            previewCaptureSession.capture(captureRequestBuilder.build(), stillCaptureCallback, null);
        } catch (Exception e) {
            Log.e(TAG, "captureImage(): ", e);
        }
    }

    private void closeCamera() {
        closePreviewSession();
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public void closePreviewSession() {
        if (previewCaptureSession != null) {
            previewCaptureSession.close();
            previewCaptureSession = null;
        }
    }

    private void startBackgroundThread() {
        backgroundHandlerThread = new HandlerThread(TAG);
        backgroundHandlerThread.start();
        backgroundHandler = new Handler(backgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        backgroundHandlerThread.quitSafely();
        try {
            backgroundHandlerThread.join();
            backgroundHandlerThread = null;
            backgroundHandler = null;
        } catch (Exception e) {
            Log.e(TAG, "stopBackgroundThread(): ", e);
        }
    }

    @OnClick(R.id.camera_flash_toggle)
    public void toggleFlash() {
    }

    @OnClick(R.id.camera_capture)
    public void toggleMediaCapture() {
        if (isForImage) {
            prepareForImageCapture();
        } else {
            if (isRecording) {
                isRecording = false;
                cameraCaptureButton.setImageResource(R.drawable.camera_inactive);
                stopVideoCapture();
            } else {
                isRecording = true;
                startVideoCapture();
                cameraCaptureButton.setImageResource(R.drawable.camera_active);
            }
        }
    }

    @OnClick(R.id.camera_flip)
    public void flipCamera() {
    }

    @AfterPermissionGranted(STORAGE_PERMISSION_REQUEST_CODE_CAMERA)
    private void startVideoCapture() {
        if (EasyPermissions.hasPermissions(ZoneApplication.getContext(), STORAGE_PERMISSIONS)) {
            try {
                mediaFileName = createMediaFile(isForImage, mediaFolder);
            } catch (Exception e) {
                Log.e(TAG, "startVideoCapture(): ", e);
            }
            startVideoRecording();
            mediaRecorder.start();
        } else {
            requestCameraPermissions(getActivity());
        }
    }

    private void stopVideoCapture() {
        if (!isForImage) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            UploadActivity.launch(getActivity(), VIDEO, boardId, mediaFileName);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}