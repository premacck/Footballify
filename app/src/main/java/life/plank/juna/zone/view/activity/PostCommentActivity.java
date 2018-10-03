package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;

public class PostCommentActivity extends AppCompatActivity {
    String TAG = PostCommentActivity.class.getSimpleName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    @BindView(R.id.profile_image_view)
    ImageView profilePicture;
    @BindView(R.id.comment_text_view)
    TextView commentTextView;
    @BindView(R.id.post_comment)
    TextView postCommentTextView;
    @BindView(R.id.blue)
    ImageView blueBg;
    @BindView(R.id.purple)
    ImageView purpleBg;
    @BindView(R.id.green)
    ImageView greenBg;
    @BindView(R.id.orange)
    ImageView orangeBg;
    @BindView(R.id.card_relative_layout)
    RelativeLayout cardRelativeLayout;
    String commentBg = "blue_bg";
    private RestApi restApi;
    private String boardId;
    private String userId;
    private String date;
    Drawable highlight;

    public static void launch(Activity packageContext, String boardId) {
        Intent intent = new Intent(packageContext, PostCommentActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_board_id), boardId);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_comment);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        commentReflectOnPostSurface();
        date = new SimpleDateFormat(getString(R.string.string_format)).format(Calendar.getInstance().getTime());
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        userId = preference.getString(getString(R.string.pref_object_id), getString(R.string.na));
        boardId = getIntent().getStringExtra(getString(R.string.intent_board_id));
        highlight = getResources().getDrawable(R.drawable.highlight);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        if (!sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)).equals(getString(R.string.na))) {
            picasso.load(sharedPref.getString(getString(R.string.pref_profile_pic_url), getString(R.string.na)))
                    .error(R.drawable.ic_default_profile)
                    .placeholder(R.drawable.ic_default_profile)
                    .into(profilePicture);
        }
    }

    @OnClick({R.id.post_comment})
    public void onViewClicked(View view) {
        if (commentEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.please_enter_comment, Toast.LENGTH_LONG).show();
        } else {
            postCommentOnBoardFeed(commentBg + "$" + commentEditText.getText().toString(), boardId, AppConstants.ROOT_COMMENT, userId, date);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void commentReflectOnPostSurface() {
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                commentTextView.setText(commentEditText.getText().toString());
            }
        });

        blueBg.setOnClickListener(v -> {
            blueBg.setBackground(highlight);
            purpleBg.setBackground(null);
            greenBg.setBackground(null);
            orangeBg.setBackground(null);
            setColor(R.drawable.blue_gradient, getString(R.string.blue_color));
        });
        purpleBg.setOnClickListener(v -> {
            blueBg.setBackground(null);
            purpleBg.setBackground(highlight);
            greenBg.setBackground(null);
            orangeBg.setBackground(null);
            setColor(R.drawable.purple_gradient, getString(R.string.purple_color));
        });
        greenBg.setOnClickListener(v -> {
            blueBg.setBackground(null);
            purpleBg.setBackground(null);
            greenBg.setBackground(highlight);
            orangeBg.setBackground(null);
            setColor(R.drawable.green_gradient, getString(R.string.green_color));
        });
        orangeBg.setOnClickListener(v -> {
            blueBg.setBackground(null);
            purpleBg.setBackground(null);
            greenBg.setBackground(null);
            orangeBg.setBackground(highlight);
            setColor(R.drawable.orange_gradient, getString(R.string.orange_color));
        });

    }

    private void setColor(int drawable, String drawableText) {
        commentBg = drawableText;
        cardRelativeLayout.setBackground(getResources().getDrawable(drawable));
    }

    private void postCommentOnBoardFeed(String getEditTextValue, String boardId, String contentType, String userId, String dateCreated) {

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));

        restApi.postFeedItemOnBoard(getEditTextValue, boardId, contentType, userId, dateCreated, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {

                        switch (jsonObjectResponse.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                Toast.makeText(PostCommentActivity.this, R.string.comment_posted_successfully, Toast.LENGTH_SHORT).show();
                                finish();
                                break;
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                Toast.makeText(PostCommentActivity.this, R.string.enter_text_to_be_posted, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(PostCommentActivity.this, R.string.could_not_post_comment, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}
