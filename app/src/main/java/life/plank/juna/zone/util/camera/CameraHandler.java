package life.plank.juna.zone.util.camera;

import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.view.fragment.camera.CameraFragment;

import static android.hardware.camera2.CameraCharacteristics.SENSOR_ORIENTATION;
import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

/**
 * Class for handling media size and storage issues
 */
public class CameraHandler {

    private static final String TAG = CameraHandler.class.getSimpleName();
    private static SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    @SuppressWarnings("ConstantConditions")
    public static int getSensorToDeviceRotation(CameraCharacteristics characteristics, int deviceOrientation) {
        int sensorOrientation = characteristics.get(SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrientation + deviceOrientation + 360) % 360;
    }

    /**
     * By default, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1280p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param sizesAvailable The list of available sizes
     * @return The video size matching 4:3 ratio, if any, or the last aspect ratio
     */
    public static Size chooseVideoSize(Size[] sizesAvailable) {
        for (Size size : sizesAvailable) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1280) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return getMaxSupportedDefaultSize(sizesAvailable);
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param sizesAvailable The list of sizes that the camera supports for the intended output class
     * @param aspectRatio    the aspect ratio size of the video.
     * @return The optimal {@link Size}, or the first one if no matches are found
     */
    public static Size chooseOptimalSize(Size[] sizesAvailable, int width, int height, Size aspectRatio) {
        List<Size> bigEnoughSizes = new ArrayList<>();
        for (Size size : sizesAvailable) {
            if (size.getHeight() == size.getWidth() * aspectRatio.getHeight() / aspectRatio.getWidth()
                    && size.getWidth() >= width && size.getHeight() >= height) {
                bigEnoughSizes.add(size);
            }
        }
        if (!isNullOrEmpty(bigEnoughSizes)) {
//            return minimum matching screen size
            return Collections.min(bigEnoughSizes, new CompareResolution());
        } else {
//            return first available supported size
            return getMaxSupportedDefaultSize(sizesAvailable);
        }
    }

    private static Size getMaxSupportedDefaultSize(Size[] sizesAvailable) {
        for (Size size : sizesAvailable) {
            if (size.getWidth() <= 1080) {
                return size;
            }
        }
        return sizesAvailable[sizesAvailable.length - 1];
    }

    /**
     * Method to create the media folder for this app, if it does not exists
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createMediaFolderIfNotExists(boolean isForImage) {
        File mediaFolder = new File(
                Environment.getExternalStoragePublicDirectory(isForImage ? DIRECTORY_PICTURES : DIRECTORY_MOVIES),
                ZoneApplication.getContext().getString(R.string.app_name)
        );
        if (!mediaFolder.exists()) {
            mediaFolder.mkdirs();
        }
        return mediaFolder;
    }

    /**
     * Method to create a new media file (video or image) in the media folder
     */
    public static String createMediaFile(boolean isForImage, File mediaFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy_MMdd_ss", Locale.getDefault()).format(new Date());
        String prefix = (isForImage ? "IMAGE_" : "VIDEO") + timeStamp + "_";
        File mediaFile = File.createTempFile(prefix, isForImage ? ".jpg" : ".mp4", mediaFolder);
        updateMediaStoreDatabase(mediaFile.getAbsolutePath());
        return mediaFile.getAbsolutePath();
    }

    /**
     * Method for updating the media store database of the device, to make the other apps aware of the new media file
     */
    private static void updateMediaStoreDatabase(String videoPath) {
        ZoneApplication.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(new File(videoPath))));
    }

    public static class CompareResolution implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    public static class ImageSaver implements Runnable {

        private final WeakReference<CameraFragment> ref;
        private final Image image;

        public ImageSaver(CameraFragment cameraFragment, Image image) {
            this.ref = new WeakReference<>(cameraFragment);
            this.image = image;
        }

        @Override
        public void run() {
            FileOutputStream stream = null;
            try {
                ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                byte[] imageBytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(imageBytes);

                stream = new FileOutputStream(ref.get().mediaFileName);
                stream.write(imageBytes);
            } catch (Exception e) {
                Log.e(TAG, "ImageSaver : run(): ", e);
            } finally {
                image.close();
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "ImageSaver : run(): Closing FileOutputStream: ", e);
                    }
                }
            }
        }
    }
}