package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;

import static life.plank.juna.zone.util.AppConstants.GALLERY_IMAGE_RESULT;

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
    }

    @OnClick({R.id.dob_edit_text, R.id.change_picture_text_view})
    public void dobClicked(View view) {
        switch (view.getId()) {
            case R.id.dob_edit_text:
                showCalendar();
                break;
            case R.id.change_picture_text_view:
                getImageResourceFromGallery();
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
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }
    }
}
