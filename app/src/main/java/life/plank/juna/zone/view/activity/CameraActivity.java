package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.AUDIO_PICKER_RESULT;
import static life.plank.juna.zone.util.AppConstants.CAMERA_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.REQUEST_CAMERA_PERMISSION;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int VIDEO_CAPTURE = 101;
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.captured_image_view)
    ImageView capturedImageView;
    @BindView(R.id.captured_video_view)
    VideoView capturedVideoView;
    @BindView(R.id.post_image)
    TextView postImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    String apiCallFromActivity;
    File absolutefile;
    String openFrom;
    String userId;
    private int GALLERY_IMAGE_RESULT = 7;
    private RestApi restApi;
    private String filePath;
    private String absolutePath;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFrom = getIntent().getStringExtra("OPEN_FROM");
        ((ZoneApplication) getApplication()).getImageUploaderNetworkComponent().inject(this);
        ((ZoneApplication) getApplication()).getUploadAudioNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        apiCallFromActivity = getIntent().getStringExtra("API");
        if (openFrom.equalsIgnoreCase("Camera")) {
            if (isStoragePermissionGranted())
                takePicture();
        } else if (openFrom.equalsIgnoreCase("Gallery")) {
            getImageResourceFromGallery();
        } else if (openFrom.equalsIgnoreCase("Video")) {
            openVideo();
        } else {
            openGalleryForAudio();
        }
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        userId = preference.getString("objectId", "NA");
    }

    private void setUpUi(String type) {
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        if (type.equalsIgnoreCase("video")) {
            capturedVideoView.setVisibility(View.VISIBLE);
            capturedImageView.setVisibility(View.GONE);
        } else {
            capturedVideoView.setVisibility(View.GONE);
            capturedImageView.setVisibility(View.VISIBLE);
        }
        postImageView.setOnClickListener(this);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileUri = getOutputMediaFileUri(CameraActivity.this);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAMERA_IMAGE_RESULT);
    }

    public Uri getOutputMediaFileUri(Context mContext) {

        try {
            return FileProvider.getUriForFile(mContext, AppConstants.FILE_PROVIDER_TO_CAPTURE_IMAGE, createImageFileName());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createImageFileName() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/juna/" + "Images" + "/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, /* prefix */".png", /* suffix */storageDir /* directory */);
        return image;
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
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return false;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE_RESULT) {
            try {
                setUpUi("image");
                filePath = fileUri.getPath();
                Log.e("filePath Camera ", "" + filePath);
                File imgFile = new File(filePath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    capturedImageView.setImageBitmap(myBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == AUDIO_PICKER_RESULT) {
            if (data != null) {
                Uri uri = data.getData();
                if (null != uri) {
                    try {
                        String uriString = uri.toString();
                        File file = new File(uriString);
                        String path = file.getAbsolutePath();
                        absolutePath = UIDisplayUtil.getAudioPath(uri);
                        absolutefile = new File(absolutePath);
                        long fileSizeInBytes = absolutefile.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if (fileSizeInMB > 8) {
                            Toast.makeText(this, "file size is big", Toast.LENGTH_SHORT).show();
                        } else {
                            String profilePicUrl = absolutePath;
                        }
                    } catch (Exception e) {
                        Log.e("TAG", "message" + e);
                        Toast.makeText(CameraActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT).show();
                    }
                    postAudioFile(absolutePath, "ManCityVsManU", "Board", "audio", "3cebd56b-ff80-4212-80aa-a84fc19cd955", "13-02-2018+04%3A50%3A23");
                    finish();
                }
            }

        } else if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                setUpUi("Video");
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                Uri videoUri = data.getData();
                String path = getRealPathFromURIForVideo(videoUri);
                capturedVideoView.setVideoURI(videoUri);
                capturedVideoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == GALLERY_IMAGE_RESULT) {
            if (data != null) {
                Uri selectedImageFromGallery = data.getData();
                if (null != selectedImageFromGallery) {
                    setUpUi("image");
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageFromGallery);
                        capturedImageView.setImageBitmap(bitmap);
                        filePath = getRealPathFromURIForGalleryImage(selectedImageFromGallery);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.unable_to_capture_image), Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: Pass the extension. Remove hardcoded value
    private void postImageFromGallery(String selectedImageUri, String targetId, String targetType, String contentType, String userId, String dateCreated) {
        progressBar.setVisibility(View.VISIBLE);
        File file = new File(selectedImageUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("", file.getName(), requestFile);
        restApi.postImageFromGallery(body, targetId, targetType, contentType, userId, dateCreated)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(CameraActivity.this, "error message", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("", "onNext: " + jsonObjectResponse);

                        if (jsonObjectResponse.code() == HttpsURLConnection.HTTP_CREATED) {
                            Toast.makeText(CameraActivity.this, "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CameraActivity.this, "Error" + jsonObjectResponse.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void postAudioFile(String selectedAudioUri, String targetId, String targetType, String contentType, String userId, String dateCreated) {
        File file = new File(selectedAudioUri);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("", file.getName(), requestBody);

        restApi.postAudioFile(body, targetId, targetType, contentType, userId, dateCreated)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                        Toast.makeText(CameraActivity.this, "error message", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        Log.e("", "onNext: " + jsonObjectResponse);
                        Toast.makeText(CameraActivity.this, "Update SuccessFully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        //todo:-Remove hardcoded topic
        if (apiCallFromActivity.equalsIgnoreCase("BoardActivity")) {
            if (openFrom.equalsIgnoreCase("Camera")) {
                postImageFromGallery(filePath, "8316683d-15df-45ad-8719-e3ba8f59b6ef", "Board", "image", userId, "04-02-2018 04:50:23");
            } else if (openFrom.equalsIgnoreCase("Gallery")) {
                postImageFromGallery(filePath, "8316683d-15df-45ad-8719-e3ba8f59b6ef", "Board", "image", userId, "04-02-2018 04:50:23");
            } else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (openFrom.equalsIgnoreCase("Camera")) {
                postImageFromGallery(filePath, "8316683d-15df-45ad-8719-e3ba8f59b6ef", "Board", "image", userId, "04-02-2018 04:50:23");
            } else if (openFrom.equalsIgnoreCase("Gallery")) {
                postImageFromGallery(filePath, "8316683d-15df-45ad-8719-e3ba8f59b6ef", "Board", "image", userId, "04-02-2018 04:50:23");
            } else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getRealPathFromURIForGalleryImage(Uri uri) {
        String filePath = "";
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public String getRealPathFromURIForVideo(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void openGalleryForAudio() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(audioIntent, "Select Audio"), AUDIO_PICKER_RESULT);
    }

    public void openVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);
        }
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }
}
