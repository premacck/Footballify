package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

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

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefs;
import static life.plank.juna.zone.util.UIDisplayUtil.toggleZone;

public class CreateBoardActivity extends AppCompatActivity {

    private static final String TAG = CreateBoardActivity.class.getSimpleName();

    @BindView(R.id.football) ToggleButton football;
    @BindView(R.id.music) ToggleButton music;
    @BindView(R.id.drama) ToggleButton drama;
    @BindView(R.id.tune) ToggleButton tune;
    @BindView(R.id.skill) ToggleButton skill;
    @BindView(R.id.other) ToggleButton other;
    @BindView(R.id.board_name_edit_text) EditText boardName;
    @BindView(R.id.board_description_edit_text) EditText boardDescription;
    @BindView(R.id.private_board_color_list) RecyclerView privateBoardColorList;
    @BindView(R.id.private_board_icon_list) RecyclerView privateBoardIconList;
    @BindView(R.id.upload_board_icon) Button uploadBoardIcon;
    @BindView(R.id.toggle_public_board) ToggleButton togglePublicBoard;
    @BindView(R.id.toggle_private_board) ToggleButton togglePrivateBoard;
    @BindView(R.id.create_board_button) Button createPrivateBoard;

    private RestApi restApi;
    @Inject @Named("default") Retrofit retrofit;

    private String zone;
    private BoardColorThemeAdapter boardColorThemeAdapter;
    private BoardIconAdapter boardIconAdapter;
    private List<String> boardColorList = new ArrayList<>();
    private List<String> boardIconList = new ArrayList<>();

    public static void launch(Context packageContext) {
        packageContext.startActivity(new Intent(packageContext, CreateBoardActivity.class));
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getCreatePrivateBoardNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        Picasso picasso = ((ZoneApplication) getApplication()).getViewComponent().getPicasso();
        boardColorThemeAdapter = new BoardColorThemeAdapter(boardColorList, picasso);
        boardIconAdapter = new BoardIconAdapter(boardIconList, picasso);
        privateBoardColorList.setAdapter(boardColorThemeAdapter);
        privateBoardIconList.setAdapter(boardIconAdapter);
    }

    @OnClick({R.id.football, R.id.music, R.id.drama, R.id.tune, R.id.skill, R.id.other})
    public void toggleView(ToggleButton view) {
        zone = view.getText().toString();
        toggleZone(this, view);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.create_board_button) public void onViewClicked(View view) {
        Board board = new Board();
        board.setZone(zone.toLowerCase().trim());
        board.setDisplayname(boardName.getText().toString().trim());
        board.setDescription(boardDescription.getText().toString().trim());
        board.setColor(boardColorThemeAdapter.getSelectedColor());

        String token = getString(R.string.bearer) + " " + getSharedPrefs(getString(R.string.login_credentails), getString(R.string.azure_token));
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
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), R.string.could_not_create_board, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
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