package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
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
    String userId, targetId;
    private RestApi restApi;
    private String filePath;
    private String absolutePath;
    private Uri fileUri;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openFrom = getIntent().getStringExtra(getString(R.string.open_from));
        ((ZoneApplication) getApplication()).getImageUploaderNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        apiCallFromActivity = getIntent().getStringExtra(getString(R.string.board_api));
        targetId = getIntent().getStringExtra(getString(R.string.board_id));
        if (openFrom.equalsIgnoreCase(getString(R.string.camera))) {
            if (UIDisplayUtil.checkPermission(CameraActivity.this)) {
                takePicture();
            }
        } else if (openFrom.equalsIgnoreCase(getString(R.string.gallery))) {
            getImageResourceFromGallery();
        } else if (openFrom.equalsIgnoreCase(getString(R.string.video))) {
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
        if (type.equalsIgnoreCase(getString(R.string.video))) {
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

    public void openGalleryForAudio() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(audioIntent, getString(R.string.select_audio)), AUDIO_PICKER_RESULT);
    }

    public void openVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, AppConstants.VIDEO_CAPTURE);
        }
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.image_format));
        startActivityForResult(galleryIntent, AppConstants.GALLERY_IMAGE_RESULT);
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
        String timeStamp = new SimpleDateFormat(getString(R.string.simple_date_format)).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/juna/" + "Images" + "/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, /* prefix */".png", /* suffix */storageDir /* directory */);
        return image;
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
                    postMediaContent(absolutePath, targetId, getString(R.string.target_type_board), getString(R.string.content_type_audio), userId, getString(R.string.posted_contant_date));
                    finish();
                }
            }
        } else if (requestCode == AppConstants.VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                setUpUi("Video");
                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                Uri videoUri = data.getData();
                path = UIDisplayUtil.getPathForVideo(videoUri, this);
                capturedVideoView.setVideoURI(videoUri);
                capturedVideoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == AppConstants.GALLERY_IMAGE_RESULT) {
            if (data != null) {
                Uri selectedImageFromGallery = data.getData();
                if (null != selectedImageFromGallery) {
                    setUpUi("image");
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageFromGallery);
                        capturedImageView.setImageBitmap(bitmap);
                        filePath = UIDisplayUtil.getPathForGalleryImageView(selectedImageFromGallery, this);
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
    //TODO: Fix progressbar bug for audio upload
    private void postMediaContent(String selectedFileUri, String targetId, String targetType, String contentType, String userId, String dateCreated) {
        progressBar.setVisibility(View.VISIBLE);
        File file = new File(selectedFileUri);
        RequestBody requestBody;
        MultipartBody.Part body = null;
        if (contentType.equals(getString(R.string.content_type_image))) {
            requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            body = MultipartBody.Part.createFormData("", file.getName(), requestBody);
        } else if (contentType.equals(getString(R.string.content_type_video))) {
            requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
            body = MultipartBody.Part.createFormData("", file.getName(), requestBody);
        } else if (contentType.equals(getString(R.string.content_type_audio))) {
            requestBody = RequestBody.create(MediaType.parse("audio/mpeg"), file);
            body = MultipartBody.Part.createFormData("", file.getName(), requestBody);
        }

        restApi.postMediaContentToServer(body, targetId, targetType, contentType, userId, dateCreated)
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

    @Override
    public void onClick(View v) {
        //todo:-Remove hardcoded topic
        if (apiCallFromActivity.equalsIgnoreCase(getString(R.string.board_activity))) {
            if (openFrom.equalsIgnoreCase(getString(R.string.camera))) {
                postMediaContent(filePath, targetId, getString(R.string.target_type_board), getString(R.string.content_type_image), userId, getString(R.string.posted_contant_date));
            } else if (openFrom.equalsIgnoreCase(getString(R.string.gallery))) {
                postMediaContent(filePath, targetId, getString(R.string.target_type_board), getString(R.string.content_type_image), userId, getString(R.string.posted_contant_date));
            } else if (openFrom.equalsIgnoreCase(getString(R.string.video))) {
                postMediaContent(path, targetId, getString(R.string.target_type_board), getString(R.string.content_type_video), userId, getString(R.string.posted_contant_date));
            } else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (openFrom.equalsIgnoreCase(getString(R.string.camera))) {
                postMediaContent(filePath, targetId, getString(R.string.target_type_board), getString(R.string.content_type_image), userId, getString(R.string.posted_contant_date));
            } else if (openFrom.equalsIgnoreCase(getString(R.string.gallery))) {
                postMediaContent(filePath, targetId, getString(R.string.target_type_board), getString(R.string.content_type_image), userId, getString(R.string.posted_contant_date));
            } else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.camera_access, Toast.LENGTH_SHORT).show();
                    finish();
                } else if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), R.string.external_storage_excess, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    takePicture();
                }
                break;
        }
    }
}
