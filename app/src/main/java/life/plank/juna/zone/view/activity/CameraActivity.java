package life.plank.juna.zone.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

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
    private static final int AUDIO_REQUEST = 3;
    @Inject
    @Named("azure")
    Retrofit retrofit;
    @BindView(R.id.captured_image_view)
    ImageView capturedImageView;
    @BindView(R.id.post_image)
    TextView postImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    String apiCallFromBoardActivity;
    private Uri imageUri;
    private Uri selectedImageFromGallery;
    private int GALLERY_IMAGE_RESULT = 7;
    private RestApi restApi;
    private Uri selectedImage;
    private String filePath;
    private Uri uri;
    private String profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        String openFrom = getIntent().getStringExtra( "OPEN_FROM" );
        ((ZoneApplication) getApplication()).getImageUploaderNetworkComponent().inject( this );
        restApi = retrofit.create( RestApi.class );
        apiCallFromBoardActivity = getIntent().getStringExtra( "API" );
        if (openFrom.equalsIgnoreCase( "Camera" )) {
            if (isStoragePermissionGranted())
                takePicture();
        } else if (openFrom.equalsIgnoreCase( "Gallery" )) {
            getImageResourceFromGallery();
        } else {
            openGalleryForAudio();
        }
    }

    private void setUpUi() {
        setContentView( R.layout.activity_camera );
        ButterKnife.bind( this );
        postImageView.setOnClickListener( this );
    }

    private void takePicture() {
        try {
            File file = createImageFile();
            imageUri = FileProvider.getUriForFile( this, AppConstants.FILE_PROVIDER_TO_CAPTURE_IMAGE, file );
            Intent cameraIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
            cameraIntent.setFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
            cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
            startActivityForResult( cameraIntent, CAMERA_IMAGE_RESULT );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
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
            if (checkSelfPermission( Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions( new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION );
                return false;
            }
        }
        return false;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat( AppConstants.DATE_FORMAT ).format( new Date() );
        String imageFileName = AppConstants.CAPTURED_IMAGE_NAME + timeStamp + AppConstants.CAPTURED_IMAGE_FORMAT;
        File mediaStorageDirectory = new File( Environment.getExternalStorageDirectory(),
                AppConstants.CAPTURED_IMAGES_FOLDER_NAME );
        File storageDirectory = new File( mediaStorageDirectory + File.separator + AppConstants.CAPTURED_IMAGES_SUB_FOLDER_NAME );
        if (!storageDirectory.exists()) {
            storageDirectory.mkdirs();
        }
        return new File( storageDirectory, imageFileName );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_IMAGE_RESULT) {
            if (imageUri != null) {
                selectedImage = imageUri;
                getContentResolver().notifyChange( selectedImage, null );
                ContentResolver cr = getContentResolver();
                Bitmap bitmap;
                try {
                    bitmap = android.provider.MediaStore.Images.Media.getBitmap( cr, selectedImage );
                    capturedImageView.setImageBitmap( bitmap );
                } catch (Exception e) {
                    Toast.makeText( this, "Failed to load", Toast.LENGTH_SHORT ).show();
                    Log.e( "Camera", e.toString() );
                }
            }
        } else if (requestCode == AUDIO_PICKER_RESULT) {
            if (data != null) {
                uri = data.getData();
                if(null != uri) {
                    try {
                        String uriString = uri.toString();
                        File file = new File( uriString );
                        String path = file.getAbsolutePath();
                        String absolutePath = UIDisplayUtil.getAudioPath( uri );
                        File absolutefile = new File( absolutePath );
                        long fileSizeInBytes = absolutefile.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if (fileSizeInMB > 8) {
                            Toast.makeText( this, "file size is big", Toast.LENGTH_SHORT ).show();
                        } else {
                            profilePicUrl = absolutePath;
                        }
                    } catch (Exception e) {
                        Toast.makeText( CameraActivity.this, "Unable to process,try again", Toast.LENGTH_SHORT ).show();
                    }
                }

            }
        }else if (requestCode == GALLERY_IMAGE_RESULT) {
            if (data != null) {
                selectedImageFromGallery = data.getData();
                if (null != selectedImageFromGallery) {
                    setUpUi();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(), selectedImageFromGallery );
                        capturedImageView.setImageBitmap( bitmap );
                        imageUri = selectedImageFromGallery;
                        filePath = getRealPathFromURI( imageUri );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText( this, getString( R.string.unable_to_capture_image ), Toast.LENGTH_SHORT ).show();
        }
    }
    private void postImageFromGallery(String selectedImageUri, String targetId, String targetType, String contentType, String userId, String dateCreated) {
        progressBar.setVisibility( View.VISIBLE );
        File file = new File( selectedImageUri );
        RequestBody requestBody = RequestBody.create( MediaType.parse( "image" ), file );
        MultipartBody.Part body = MultipartBody.Part.createFormData( "", file.getName(), requestBody ); // SERVER key name is image

        restApi.postImageFromGallery( body, targetId, targetType, contentType, userId, dateCreated )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e( "", "onCompleted: " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e( "", "onError: " + e );
                        progressBar.setVisibility( View.VISIBLE );
                        Toast.makeText( CameraActivity.this, "error message", Toast.LENGTH_SHORT ).show();

                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        progressBar.setVisibility( View.INVISIBLE );
                        Log.e( "", "onNext: " + jsonObjectResponse );
                        Toast.makeText( CameraActivity.this, "Update SuccessFully", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    @Override
    public void onClick(View v) {
        if (apiCallFromBoardActivity.equalsIgnoreCase( "BoardActivity" ))
            postImageFromGallery( filePath, "ManCityVsManU", "Board", "image", "54a1e691-003f-4cff-829e-a8da42c5fcd9", "13-02-2018+04%3A50%3A23" );
    }

    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query( uri, filePathColumn, null, null, null );
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
            filePath = cursor.getString( columnIndex );
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public void openGalleryForAudio() {
        Intent audioIntent = new Intent( Intent.ACTION_PICK,android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( Intent.createChooser( audioIntent, "Select Audio" ), AUDIO_PICKER_RESULT );
    }
    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        galleryIntent.setType( "image/*" );
        startActivityForResult( galleryIntent, GALLERY_IMAGE_RESULT );
    }
}
