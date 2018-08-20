package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

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
import life.plank.juna.zone.view.adapter.BoardColorThemeAdapter;
import life.plank.juna.zone.view.adapter.BoardIconAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.facebook.internal.Utility.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;
import static life.plank.juna.zone.util.UIDisplayUtil.toggleZone;

public class CreateBoardActivity extends AppCompatActivity {

    private static final String TAG = CreateBoardActivity.class.getSimpleName();

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
    @BindView(R.id.board_name_edit_text)
    TextInputEditText boardName;
    @BindView(R.id.board_description_edit_text)
    TextInputEditText boardDescription;
    @BindView(R.id.private_board_color_list)
    RecyclerView privateBoardColorList;
    @BindView(R.id.private_board_icon_list)
    RecyclerView privateBoardIconList;
    @BindView(R.id.upload_board_icon)
    Button uploadBoardIcon;
    @BindView(R.id.board_type_radio_group)
    RadioGroup boardTypeRadioGroup;
    @BindView(R.id.toggle_public_board)
    RadioButton togglePublicBoard;
    @BindView(R.id.toggle_private_board)
    RadioButton togglePrivateBoard;
    @BindView(R.id.create_board_button)
    Button createPrivateBoard;
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    @Inject
    BoardColorThemeAdapter boardColorThemeAdapter;
    @Inject
    BoardIconAdapter boardIconAdapter;
    private RestApi restApi;
    private String zone = "";

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, CreateBoardActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        restApi = retrofit.create(RestApi.class);
        privateBoardColorList.setAdapter(boardColorThemeAdapter);
        privateBoardIconList.setAdapter(boardIconAdapter);
    }

    @OnClick({R.id.football, R.id.music, R.id.drama, R.id.tune, R.id.skill, R.id.other})
    public void toggleView(ToggleButton view) {
        zone = view.getText().toString();
        toggleZone(this, view);
    }

    @OnClick(R.id.create_board_button)
    public void onViewClicked(View view) {
        Board board = new Board();
        board.setZone(zone.toLowerCase().trim());
        board.setDisplayname(boardName.getText().toString().trim());
        board.setDescription(boardDescription.getText().toString().trim());
        board.setColor(boardColorThemeAdapter.getSelectedColor());

        createBoard(board);
    }

    private void createBoard(Board board) {
        if (isNullOrEmpty(board.getZone())) {
            Toast.makeText(this, R.string.select_zone_for_board, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isNullOrEmpty(board.getDisplayname())) {
            boardName.setError("Please enter a board name", getDrawable(R.drawable.ic_error));
            boardName.requestFocus();
            return;
        }
        if (isNullOrEmpty(board.getDescription())) {
            boardDescription.setError("Please enter the board's description", getDrawable(R.drawable.ic_error));
            boardDescription.requestFocus();
            return;
        }
        if (isNullOrEmpty(board.getColor())) {
            Toast.makeText(this, R.string.select_board_color, Toast.LENGTH_SHORT).show();
            return;
        }

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));
        restApi.createPrivateBoard(getString(boardTypeRadioGroup.getCheckedRadioButtonId() == R.id.toggle_public_board ? R.string.public_lowercase : R.string.private_lowercase), board, token)
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
                        Toast.makeText(getApplicationContext(), R.string.could_not_create_board, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                startActivity(new Intent(CreateBoardActivity.this, PrivateBoardActivity.class));
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.could_not_create_board, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }
}