package life.plank.juna.zone.util.camera;

import android.Manifest;
import android.app.Activity;

import life.plank.juna.zone.R;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionHandler {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 0;
    public static final int STORAGE_PERMISSION_REQUEST_CODE_CAMERA = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE_GALLERY = 10;
    public static final String[] CAMERA_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    public static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void requestCameraPermissions(Activity activity) {
        EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.camera_permission_body),
                CAMERA_PERMISSION_REQUEST_CODE,
                CAMERA_PERMISSIONS
        );
    }

    public static void requestStoragePermissionsForCamera(Activity activity) {
        EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.storage_permission_body),
                STORAGE_PERMISSION_REQUEST_CODE_CAMERA,
                STORAGE_PERMISSIONS
        );
    }

    public static void requestStoragePermissionsForGallery(Activity activity) {
        EasyPermissions.requestPermissions(
                activity,
                activity.getString(R.string.storage_permission_body),
                STORAGE_PERMISSION_REQUEST_CODE_GALLERY,
                STORAGE_PERMISSIONS
        );
    }
}