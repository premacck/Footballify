package life.plank.juna.zone.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

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

public class PostCommentActivity extends AppCompatActivity {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.comment_edit_text)
    EditText commentEditText;
    @BindView(R.id.comment_text_view)
    TextView commentTextView;
    @BindView(R.id.post_comment)
    TextView postCommentTextView;
    private RestApi restApi;
    private String getEditTextValue, boardId, userId, date;

    @BindView(R.id.red)
    ImageView redBg;
    @BindView(R.id.pink)
    ImageView pinkBg;
    @BindView(R.id.yellow)
    ImageView yellowBg;
    @BindView(R.id.green)
    ImageView greenBg;
    @BindView(R.id.blue)
    ImageView blueBg;

    @BindView(R.id.comment_card_view)
    CardView commentCardView;
    Integer commentBg = R.color.material_blue_600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_comment);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getPostCommentFeedNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        commentReflectOnPostSurface();
        date = new SimpleDateFormat(getString(R.string.string_format)).format(Calendar.getInstance().getTime());
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        userId = preference.getString("objectId", "NA");
        boardId = getIntent().getStringExtra(getString(R.string.board_id));
    }

    @OnClick({R.id.post_comment})
    public void onViewClicked(View view) {
        getEditTextValue = commentBg.toString() + "$" + commentEditText.getText().toString();
        postCommentOnBoardFeed(getEditTextValue, boardId, AppConstants.ROOT_COMMENT, userId, date);
        finish();
    }

    public void commentReflectOnPostSurface() {
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                commentTextView.setText(commentEditText.getText().toString());
            }
        });

        redBg.setOnClickListener(v -> setColor(R.color.red));
        pinkBg.setOnClickListener(v -> setColor(R.color.material_pink_800));
        yellowBg.setOnClickListener(v -> setColor(R.color.material_yellow_700));
        greenBg.setOnClickListener(v -> setColor(R.color.material_green_700));
        blueBg.setOnClickListener(v -> setColor(R.color.material_blue_600));

    }

    private void setColor(int color) {
        commentBg = color;
        commentCardView.setCardBackgroundColor(getResources().getColor(color));
    }

    private void postCommentOnBoardFeed(String getEditTextValue, String boardId, String contentType, String userId, String dateCreated) {
        restApi.postCommentOnBoardFeed(getEditTextValue, boardId, contentType, userId, dateCreated)
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
                        Toast.makeText(PostCommentActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        if (jsonObjectResponse.code() == HttpURLConnection.HTTP_CREATED) {
                            Log.e("", "onNext: " + jsonObjectResponse);
                            Toast.makeText(PostCommentActivity.this, "Comment Post Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
