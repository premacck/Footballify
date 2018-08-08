package life.plank.juna.zone.view.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreatePrivateBoardActivity extends AppCompatActivity {

    private static final String TAG = CreatePrivateBoardActivity.class.getSimpleName();
    @BindView(R.id.football)
    ToggleButton football;
    @BindView(R.id.music)
    ToggleButton music;
    @BindView(R.id.drama)
    ToggleButton drama;
    @BindView(R.id.tune)
    ToggleButton tune;
    @BindView(R.id.skill)
    ToggleButton skill;
    @BindView(R.id.other)
    ToggleButton other;

    @BindView(R.id.red)
    TextView red;
    @BindView(R.id.yellow)
    TextView yellow;
    @BindView(R.id.purple)
    TextView purple;
    @BindView(R.id.pink)
    TextView pink;
    @BindView(R.id.green)
    TextView green;
    @BindView(R.id.blue)
    TextView blue;

    @BindView(R.id.board_name_edit_text)
    EditText boardName;
    @BindView(R.id.board_description_edit_text)
    EditText boardDescription;

    @BindView(R.id.create_private_board_button)
    Button createPrivateBoard;

    private RestApi restApi;
    @Inject
    @Named("default")
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_board);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getCreatePrivateBoardNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        football.setOnClickListener(v -> toggleView(football));
        music.setOnClickListener(v -> toggleView(music));
        drama.setOnClickListener(v -> toggleView(drama));
        tune.setOnClickListener(v -> toggleView(tune));
        skill.setOnClickListener(v -> toggleView(skill));
        other.setOnClickListener(v -> toggleView(other));

        setColor(red, getColor(R.color.red));
        setColor(yellow, getColor(R.color.material_yellow_700));
        setColor(purple, getColor(R.color.purple_toolbar_background));
        setColor(pink, getColor(R.color.fab_button_pink));
        setColor(green, getColor(R.color.green));
        setColor(blue, getColor(R.color.com_facebook_blue));
    }

    public void toggleView(ToggleButton view) {
        if (view.isChecked()) {
            view.setBackground(getDrawable(R.drawable.unselected_text_view_bg));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            view.setTextColor(getColor(R.color.grey));
        } else {
            view.setBackground(getDrawable(R.drawable.selected_textview_bg));
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_tag, 0);
            view.setTextColor(getColor(R.color.white));
        }
    }

    public void setColor(TextView view, int color) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(color);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.create_private_board_button})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.create_private_board_button:
                // Replace hard coded values with user entered values
                Board board = new Board();
                board.setZone("football");
                board.setDisplayname("Beer Brothers");
                board.setDescription("Meant for drunkards");
                board.setColor("brown");
                SharedPreferences azurePref = ZoneApplication.getContext().getSharedPreferences(getString(R.string.azure_token), 0);
                String token = getString(R.string.bearer) + " " + azurePref.getString(getString(R.string.azure_token), "NA");
                restApi.createPrivateBoard("private", board, token)
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
                                Toast.makeText(getApplicationContext(), R.string.share_feed_item_toast, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(Response<JsonObject> response) {


                                switch (response.code()) {
                                    case HttpURLConnection.HTTP_CREATED:
                                        // Navigate to activity
                                        break;
                                    default:
                                        // Handle error case
                                        break;
                                }

                            }
                        });
                break;
        }
    }
}
