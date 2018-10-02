package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.Image;
import life.plank.juna.zone.util.UIDisplayUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.AUDIO_PICKER_RESULT;
import static life.plank.juna.zone.util.AppConstants.CAMERA_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.GALLERY;
import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.AppConstants.VIDEO_CAPTURE;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.enableOrDisableView;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = CameraActivity.class.getCanonicalName();

    @Inject
    @Named("default")
    RestApi restApi;
    @BindView(R.id.captured_image_view)
    ImageView capturedImageView;
    @BindView(R.id.title_text)
    TextInputEditText titleEditText;
    @BindView(R.id.description_edit_text)
    TextInputEditText descriptionEditText;
    @BindView(R.id.captured_video_view)
    VideoView capturedVideoView;
    @BindView(R.id.play_btn)
    ImageButton playBtn;
    @BindView(R.id.post_btn)
    Button postBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.root_layout)
    ScrollView scrollView;

    private String apiCallFromActivity;
    private String openFrom;
    private String userId, boardId;
    private String date;
    private String filePath;
    private String absolutePath;
    private Uri fileUri;
    private String path;
    private Handler mHandler;

    public static void launch(Context packageContext, String openFrom, String boardId, String api) {
        if (boardId != null) {
            Intent intent = new Intent(packageContext, CameraActivity.class);
            intent.putExtra(packageContext.getString(R.string.intent_open_from), openFrom);
            intent.putExtra(packageContext.getString(R.string.intent_board_id), boardId);
            intent.putExtra(packageContext.getString(R.string.intent_api), api);
            packageContext.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFrom = getIntent().getStringExtra(getString(R.string.intent_open_from));
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        apiCallFromActivity = getIntent().getStringExtra(getString(R.string.intent_api));
        boardId = getIntent().getStringExtra(getString(R.string.intent_board_id));
        openMediaContent();
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        userId = preference.getString(getString(R.string.pref_object_id), "NA");
        date = new SimpleDateFormat(getString(R.string.string_format), Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    private void openMediaContent() {
        if (UIDisplayUtil.checkPermission(CameraActivity.this)) {
            switch (openFrom) {
                case IMAGE:
                    takePicture();
                    break;
                case VIDEO:
                    openVideo();
                    break;
                case GALLERY:
                    getImageResourceFromGallery();
                    break;
                case AUDIO:
                    openGalleryForAudio();
                    break;
                default:
//                    TODO: add proper permission request dialog here
                    Toast.makeText(this, R.string.add_permission, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void updateUI(String type) {
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        capturedVideoView.setVisibility(type.equals(VIDEO) ? View.VISIBLE : View.GONE);
        capturedImageView.setVisibility(type.equals(VIDEO) ? View.GONE : View.VISIBLE);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileUri = getOutputMediaFileUri(CameraActivity.this);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAMERA_IMAGE_RESULT);
    }

    public void openGalleryForAudio() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(audioIntent, getString(R.string.select_audio)), AUDIO_PICKER_RESULT);
    }

    public void openVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);
        }
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.image_format));
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }

    public Uri getOutputMediaFileUri(Context mContext) {
        try {
            return FileProvider.getUriForFile(mContext, AppConstants.FILE_PROVIDER_TO_CAPTURE_IMAGE, createImageFileName());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createImageFileName() throws IOException {
        String timeStamp = new SimpleDateFormat(getString(R.string.simple_date_format), Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/juna/" + "Images" + "/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        return File.createTempFile(imageFileName, /* prefix */".png", /* suffix */storageDir /* directory */);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        File imgFile;
        switch (requestCode) {
            case CAMERA_IMAGE_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        filePath = fileUri.getPath();
                        prepareImageForUpload();
                        try {
                            updateUI(IMAGE);
                            imgFile = new File(filePath);
                            if (imgFile.exists()) {
                                Bitmap bitmap = new Image().compress(imgFile, imgFile.toString());
                                filePath = imgFile.toString();
                                setImagePreview(bitmap);
                            }
                        } catch (Exception e) {
                            Log.e("TAG", "CAMERA_IMAGE_RESULT : " + e);
                            Toast.makeText(getApplicationContext(), R.string.could_not_process_image, Toast.LENGTH_LONG).show();
                            finish();
                        }
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        Toast.makeText(this, R.string.could_not_process_image, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
                break;
            case AUDIO_PICKER_RESULT:
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if (null != uri) {
                        try {
                            updateUI(VIDEO);
                            absolutePath = UIDisplayUtil.getAudioPath(uri);
                            File absoluteFile = new File(absolutePath);
                            long fileSizeInMB = absoluteFile.length() / (1024 * 1024);
                            if (fileSizeInMB <= 8) {
//                                TODO: implement audio player using "this.absolutePath"
                            } else {
                                Toast.makeText(this, R.string.file_too_large, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("TAG", "AUDIO_PICKER_RESULT : " + e);
                            Toast.makeText(CameraActivity.this, R.string.unable_to_process, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
                break;
            case VIDEO_CAPTURE:
                switch (resultCode) {
                    case RESULT_OK:
                        updateUI(VIDEO);
                        Uri videoUri = data.getData();
                        path = UIDisplayUtil.getPathForVideo(videoUri, this);
                        new VideoCompressionTask(CameraActivity.this, videoUri, getCacheDir().getAbsolutePath()).execute();
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        Toast.makeText(this, R.string.video_recording_failed, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
                break;
            case GALLERY_IMAGE_RESULT:
                switch (resultCode) {
                    case RESULT_OK:
                        if (data != null) {
                            filePath = getPathForGalleryImageView(data.getData(), this);
                            prepareImageForUpload();
                        }
                        break;
                    case RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        Toast.makeText(this, R.string.failed_to_process_image, Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void prepareImageForUpload() {
        try {
            updateUI(IMAGE);
            File imgFile = new File(filePath);
            if (imgFile.exists()) {
                Bitmap bitmap = new Image().compress(imgFile, filePath);
                setImagePreview(bitmap);
            }
        } catch (Exception e) {
            Log.e("TAG", "CAMERA_IMAGE_RESULT : " + e);
            Toast.makeText(getApplicationContext(), R.string.could_not_process_image, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void prepareVideoForUpload(Intent data) {
        try {
            updateUI(VIDEO);
            Uri videoUri = data.getData();
            path = UIDisplayUtil.getPathForVideo(videoUri, this);
            capturedVideoView.setVideoPath(SiliCompressor.with(getApplicationContext()).compressVideo(videoUri, getCacheDir().getAbsolutePath()));
            capturedVideoView.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(true);
                progressBar.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                scrollView.smoothScrollTo(0, scrollView.getBottom());
            });
        } catch (Exception e) {
            Log.e("TAG", "VIDEO_CAPTURE : " + e);
            Log.d(TAG, "Video compression failed");
            finish();
        }
    }

    private void setImagePreview(Bitmap bitmap) {
        int width = (int) (getScreenSize(getWindowManager().getDefaultDisplay())[0] - getDp(16));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) capturedImageView.getLayoutParams();
        params.height = width * bitmap.getHeight() / bitmap.getWidth();
        capturedImageView.setLayoutParams(params);
        capturedImageView.setImageBitmap(bitmap);
    }

    private void postMediaContent(String selectedFileUri, String mediaType, String contentType, String userId, String dateCreated) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.just_a_moment));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        File file = new File(selectedFileUri);
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse(mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("", file.getName(), requestBody);

        restApi.postMediaContentToServer(body, boardId, contentType, userId,
                dateCreated, AppConstants.BOARD, descriptionEditText.getText().toString(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.cancel();
                        Log.e(TAG, "onError: postMediaContentToServer" + e);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        progressDialog.cancel();
                        switch (jsonObjectResponse.code()) {
                            case HttpsURLConnection.HTTP_CREATED:
                                Toast.makeText(CameraActivity.this, R.string.upload_successful, Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                Toast.makeText(CameraActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @OnClick(R.id.post_btn)
    public void onPostBtnClick() {
        switch (apiCallFromActivity) {
            case "BoardActivity":
                switch (openFrom) {
                    case IMAGE:
                    case GALLERY:
                        postMediaContent(filePath, getString(R.string.media_type_image), IMAGE, userId, date);
                        break;
                    case VIDEO:
                        postMediaContent(path, getString(R.string.media_type_video), VIDEO, userId, date);
                        break;
                    case AUDIO:
                        postMediaContent(absolutePath, getString(R.string.media_type_audio), AUDIO, userId, date);
                        break;
                    default:
                        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                switch (openFrom) {
                    case IMAGE:
                    case GALLERY:
                        postMediaContent(filePath, getString(R.string.media_type_image), IMAGE, userId, date);
                        break;
                    default:
                        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
    }

    @OnClick(R.id.play_btn)
    public void playVideo() {
        playBtn.setVisibility(View.GONE);
        capturedVideoView.start();
    }

    @OnClick(R.id.captured_video_view)
    public void pauseVideo() {
        playBtn.setVisibility(View.VISIBLE);
        capturedVideoView.pause();
    }

    //TODO: Refine this method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.camera_access, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Objects.equals(openFrom, AUDIO)) {
                        openGalleryForAudio();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.external_storage_excess, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    if (Objects.equals(openFrom, IMAGE)) {
                        takePicture();
                    } else {
                        openVideo();
                    }
                }
                break;
            case AppConstants.REQUEST_ID_STORAGE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    if (Objects.equals(openFrom, AUDIO))
                        openGalleryForAudio();
                    else {
                        getImageResourceFromGallery();
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        if (capturedVideoView != null && capturedVideoView.isPlaying()) {
            capturedVideoView.stopPlayback();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }

    static class VideoCompressionTask extends AsyncTask<Void, Void, String> {

        private WeakReference<CameraActivity> ref;
        private Uri videoUri;
        private String destinationPath;

        VideoCompressionTask(CameraActivity cameraActivity, Uri videoUri, String destinationPath) {
            this.ref = new WeakReference<>(cameraActivity);
            this.videoUri = videoUri;
            this.destinationPath = destinationPath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            enableOrDisableView(ref.get().postBtn, false);
            ref.get().progressBar.setVisibility(View.VISIBLE);
            ref.get().postBtn.setText(R.string.optimizing_video);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return SiliCompressor.with(ref.get()).compressVideo(videoUri, destinationPath);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Compressing video - ", e);
            }
            return null;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        protected void onPostExecute(String compressedVideoPath) {
            super.onPostExecute(compressedVideoPath);
            enableOrDisableView(ref.get().postBtn, true);
            ref.get().postBtn.setText(R.string.post);
            if (compressedVideoPath != null) {
//                Deleting original video file and using the compressed one.
                ref.get().capturedVideoView.setVideoPath(compressedVideoPath);
                File originalVideo = new File(videoUri.getPath());
                if (originalVideo.exists()) {
                    originalVideo.delete();
                }
            } else {
//                Using the original video in case of compression failure.
                ref.get().capturedVideoView.setVideoURI(videoUri);
            }
            ref.get().capturedVideoView.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(true);
                ref.get().progressBar.setVisibility(View.GONE);
                ref.get().playBtn.setVisibility(View.VISIBLE);
                ref.get().mHandler = new Handler();
                ref.get().mHandler.postDelayed(() -> ref.get().scrollView.smoothScrollTo(0, ref.get().scrollView.getBottom()), 1500);
            });
        }
    }
}
