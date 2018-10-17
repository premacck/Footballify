package life.plank.juna.zone.view.activity.camera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
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
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
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
import life.plank.juna.zone.view.fragment.camera.CameraFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.AUDIO;
import static life.plank.juna.zone.util.AppConstants.AUDIO_PICKER_RESULT;
import static life.plank.juna.zone.util.AppConstants.GALLERY;
import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;
import static life.plank.juna.zone.util.AppConstants.IMAGE;
import static life.plank.juna.zone.util.AppConstants.VIDEO;
import static life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.enableOrDisableView;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;
import static life.plank.juna.zone.util.camera.PermissionHandler.CAMERA_PERMISSIONS;
import static life.plank.juna.zone.util.camera.PermissionHandler.STORAGE_PERMISSIONS;
import static life.plank.juna.zone.util.camera.PermissionHandler.requestCameraAndStoragePermissions;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = UploadActivity.class.getCanonicalName();
    private static final int CAMERA_AND_STORAGE_PERMISSIONS = 15;

    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @BindView(R.id.captured_image_view)
    ImageView capturedImageView;
    @BindView(R.id.profile_image_view)
    ImageView profilePicture;
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

    private String openFrom;
    private String userId, boardId;
    private String filePath;
    private String absolutePath;
    private Handler mHandler;

    /**
     * Method to launch {@link UploadActivity} to upload image or audio which is already saved in gallery or media storage
     * Provide mediaFilePath param when uploading image or video provided from {@link CameraFragment}
     */
    public static void launch(Activity from, String openFrom, String boardId, String... mediaFilePath) {
        if (boardId != null) {
            Intent intent = new Intent(from, UploadActivity.class);
            intent.putExtra(from.getString(R.string.intent_open_from), openFrom);
            intent.putExtra(from.getString(R.string.intent_board_id), boardId);
            if (mediaFilePath != null) {
                intent.putExtra(from.getString(R.string.intent_file_path), mediaFilePath[0]);
            }
            from.startActivity(intent);
            from.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        userId = preference.getString(getString(R.string.pref_object_id), getString(R.string.na));

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        if (intent.hasExtra(getString(R.string.intent_file_path))) {
            filePath = intent.getStringExtra(getString(R.string.intent_file_path));
        }
        boardId = intent.getStringExtra(getString(R.string.intent_board_id));
        openFrom = intent.getStringExtra(getString(R.string.intent_open_from));
        handleMediaContent();
    }

    @AfterPermissionGranted(CAMERA_AND_STORAGE_PERMISSIONS)
    private void handleMediaContent() {
        if (EasyPermissions.hasPermissions(this, ArrayUtils.addAll(CAMERA_PERMISSIONS, STORAGE_PERMISSIONS))) {
            switch (openFrom) {
                case IMAGE:
                    prepareImageForUpload();
                    break;
                case VIDEO:
                    updateUI(VIDEO);
                    new VideoCompressionTask(UploadActivity.this, filePath, getCacheDir().getAbsolutePath()).execute();
                    break;
                case GALLERY:
                    getImageResourceFromGallery();
                    break;
                case AUDIO:
                    openGalleryForAudio();
                    break;
            }
        } else {
            requestCameraAndStoragePermissions(this);
        }
    }

    private void updateUI(String type) {
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        capturedVideoView.setVisibility(type.equals(VIDEO) ? View.VISIBLE : View.GONE);
        capturedImageView.setVisibility(type.equals(VIDEO) ? View.GONE : View.VISIBLE);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        if (!Objects.equals(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)), getString(R.string.na))) {
            picasso.load(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)))
                    .error(R.drawable.ic_default_profile)
                    .placeholder(R.drawable.ic_default_profile)
                    .into(profilePicture);
        }
    }

    public void openGalleryForAudio() {
        Intent audioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(audioIntent, getString(R.string.select_audio)), AUDIO_PICKER_RESULT);
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setDataAndType(Media.EXTERNAL_CONTENT_URI, getString(R.string.image_format));
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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
                            Toast.makeText(UploadActivity.this, R.string.unable_to_process, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
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

    private void setImagePreview(Bitmap bitmap) {
        int width = (int) (getScreenSize(getWindowManager().getDefaultDisplay())[0] - getDp(16));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) capturedImageView.getLayoutParams();
        params.height = width * bitmap.getHeight() / bitmap.getWidth();
        capturedImageView.setLayoutParams(params);
        capturedImageView.setImageBitmap(bitmap);
    }

    private void postMediaContent(String selectedFileUri, String mediaType, String contentType, String userId, String dateCreated) {
        if (titleEditText.getText() == null || titleEditText.getText().toString().trim().isEmpty()) {
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.just_a_moment));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        File file = new File(selectedFileUri);
        RequestBody requestBody;
        requestBody = RequestBody.create(MediaType.parse(mediaType), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("", file.getName(), requestBody);

        String description = descriptionEditText.getText() != null ? descriptionEditText.getText().toString().trim() : "";

        restApi.postMediaContentToServer(body, boardId, contentType, userId,
                dateCreated, AppConstants.BOARD, description, titleEditText.getText().toString(), getToken())
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
                                Toast.makeText(UploadActivity.this, R.string.upload_successful, Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                Toast.makeText(UploadActivity.this, R.string.upload_failed, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @OnClick(R.id.post_btn)
    public void onPostBtnClick() {
        if (titleEditText.getText() != null && titleEditText.getText().toString().trim().isEmpty()) {
            titleEditText.setError(getString(R.string.please_enter_title));
            return;
        }
        switch (openFrom) {
            case IMAGE:
            case GALLERY:
                postMediaContent(filePath, getString(R.string.media_type_image), IMAGE, userId, getRequestDateStringOfNow());
                break;
            case VIDEO:
                postMediaContent(filePath, getString(R.string.media_type_video), VIDEO, userId, getRequestDateStringOfNow());
                break;
            case AUDIO:
                postMediaContent(absolutePath, getString(R.string.media_type_audio), AUDIO, userId, getRequestDateStringOfNow());
                break;
            default:
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.float_down, R.anim.sink_down);
    }

    static class VideoCompressionTask extends AsyncTask<Void, Void, String> {

        private WeakReference<UploadActivity> ref;
        private String videoPath;
        private String destinationPath;

        VideoCompressionTask(UploadActivity uploadActivity, String videoPath, String destinationPath) {
            this.ref = new WeakReference<>(uploadActivity);
            this.videoPath = videoPath;
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
                return SiliCompressor.with(ref.get()).compressVideo(videoPath, destinationPath);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: Compressing video - ", e);
            }
            return null;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Override
        protected void onPostExecute(String compressedVideoPath) {
            enableOrDisableView(ref.get().postBtn, true);
            ref.get().postBtn.setText(R.string.post);
            if (compressedVideoPath != null) {
//                Deleting original video file and using the compressed one.
                ref.get().filePath = compressedVideoPath;
                ref.get().capturedVideoView.setVideoPath(compressedVideoPath);
                File originalVideo = new File(videoPath);
                if (originalVideo.exists()) {
                    originalVideo.delete();
                }
            } else {
//                Using the original video in case of compression failure.
                ref.get().capturedVideoView.setVideoPath(videoPath);
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
