package life.plank.juna.zone.util.camera;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import org.jetbrains.annotations.NotNull;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class PermissionHandler {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    public static final int STORAGE_PERMISSION_REQUEST_CODE_CAMERA = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE_GALLERY = 10;

    public static void showPermissionRationale(@NotNull Fragment fragment, String permission, @StringRes int title, @StringRes int message, DialogInterface.OnClickListener listener) {
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(ZoneApplication.getContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.okay, listener)
                    .setNegativeButton(R.string.cancel, ((dialog, which) -> dialog.dismiss()))
                    .show();
        }
    }

    public static boolean checkCameraPermissions(@NotNull Fragment fragment) {
        if (checkSelfPermission(ZoneApplication.getContext(), Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            requestCameraPermissions(fragment);
            return false;
        }
        return true;
    }

    public static void requestCameraPermissions(@NotNull Fragment fragment) {
        fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    public static boolean checkStoragePermissions(@NotNull Fragment fragment, boolean isForGallery) {
        if (checkSelfPermission(ZoneApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED ||
                checkSelfPermission(ZoneApplication.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            fragment.requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    isForGallery ? STORAGE_PERMISSION_REQUEST_CODE_GALLERY : STORAGE_PERMISSION_REQUEST_CODE_CAMERA
            );
            return false;
        }
        return true;
    }
}