package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.UIDisplayUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;
import static life.plank.juna.zone.util.UIDisplayUtil.getPathForGalleryImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    @BindView(R.id.dob_edit_text)
    EditText dobEditText;
    @BindView(R.id.profile_picture_image_view)
    CircleImageView profilePicture;
    @BindView(R.id.change_picture_text_view)
    TextView changePicture;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Picasso picasso;
    private String filePath;

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, EditProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        blurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), UserProfileActivity.parentViewBitmap));
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        if (!sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)).equals(getString(R.string.na))) {
            picasso.load(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)))
                    .error(R.drawable.ic_default_profile)
                    .placeholder(R.drawable.ic_default_profile)
                    .into(profilePicture);
        }
        UIDisplayUtil.checkPermission(EditProfileActivity.this);

    }

    @OnClick({R.id.dob_edit_text, R.id.change_picture_text_view, R.id.blur_background_image_view, R.id.save_button})
    public void dobClicked(View view) {
        switch (view.getId()) {
            case R.id.dob_edit_text:
                showCalendar();
                break;
            case R.id.change_picture_text_view:
                if (UIDisplayUtil.checkPermission(EditProfileActivity.this)) {
                    getImageResourceFromGallery();
                } else {
                    Toast.makeText(this, R.string.add_permission, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.blur_background_image_view:
                finish();
                break;
            case R.id.save_button:
                finish();
                break;
        }
    }

    public void getImageResourceFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.image_format));
        startActivityForResult(galleryIntent, GALLERY_IMAGE_RESULT);
    }

    private void showCalendar() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) ->
                        dobEditText.setText(dayOfMonth + " " + new DateFormatSymbols().getMonths()[monthOfYear] + ", " + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_IMAGE_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        profilePicture.setImageBitmap(bitmap);
                        filePath = getPathForGalleryImageView(data.getData(), this);
                        uploadProfilePicture();
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }
    }

    public void uploadProfilePicture() {
        File fileToUpload = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse(getString(R.string.media_type_image)), fileToUpload);
        MultipartBody.Part image = MultipartBody.Part.createFormData("", fileToUpload.getName(), requestBody);

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));
        restApi.uploadProfilePicture(image, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.upload_failed, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_NO_CONTENT:
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.upload_failed, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }
}
