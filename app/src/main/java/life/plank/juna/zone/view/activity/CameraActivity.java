package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.RoundedTransformation;
import life.plank.juna.zone.util.UIDisplayUtil;

import static life.plank.juna.zone.util.AppConstants.CAMERA_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.REQUEST_CAMERA_PERMISSION;


public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.captured_image_view)
    ImageView capturedImageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isStoragePermissionGranted())
            takePicture();
    }

    private void setUpUi() {
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        loadImage();
    }

    private void loadImage() {
        Picasso.with(this)
                .load(Uri.fromFile(new File(imageUri.getPath().replace("//", "/"))))
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .transform(new RoundedTransformation(UIDisplayUtil.dpToPx(8, this), 0))
                .error(R.drawable.ic_place_holder)
                .into(capturedImageView);
    }


    private void takePicture() {
        try {
            File file = createImageFile();
            imageUri = FileProvider.getUriForFile(this, AppConstants.FILE_PROVIDER_TO_CAPTURE_IMAGE, file);
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_IMAGE_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }
                break;
            default:
                break;
        }
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return false;
            }
        }
        return false;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(AppConstants.DATE_FORMAT).format(new Date());
        String imageFileName = AppConstants.CAPTURED_IMAGE_NAME + timeStamp + AppConstants.CAPTURED_IMAGE_FORMAT;
        File mediaStorageDirectory = new File(Environment.getExternalStorageDirectory(),
                AppConstants.CAPTURED_IMAGES_FOLDER_NAME);
        File storageDirectory = new File(mediaStorageDirectory + File.separator + AppConstants.CAPTURED_IMAGES_SUB_FOLDER_NAME);
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }
        return new File(storageDirectory, imageFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE_RESULT) {
            if (resultCode == RESULT_OK) {
                if (imageUri != null) {
                    setUpUi();
                }
            } else {
                Toast.makeText(this, getString(R.string.unable_to_capture_image), Toast.LENGTH_SHORT).show();
                if (imageUri != null) {
                    new File(imageUri.getPath().replace("//", "/")).delete();
                    finish();
                }
            }
        }
    }
}
