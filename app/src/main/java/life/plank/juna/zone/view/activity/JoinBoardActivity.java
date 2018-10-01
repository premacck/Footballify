package life.plank.juna.zone.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import life.plank.juna.zone.util.customview.GenericToolbar;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class JoinBoardActivity extends AppCompatActivity {
    private static final String TAG = JoinBoardActivity.class.getSimpleName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Gson gson;
    @BindView(R.id.board_parent_layout)
    CardView boardCardView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.preview_toolbar)
    GenericToolbar toolbar;
    @Inject
    Picasso picasso;

    private RestApi restApi;
    Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        getBoardDetails(getIntent().getStringExtra(getString(R.string.board_id_prefix)));

    }

    @OnClick({R.id.accept_invite_button, R.id.reject_invite_button})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.accept_invite_button:
                followBoard();
                break;
            case R.id.reject_invite_button:
                finish();
                break;
        }
    }


    public void followBoard() {

        restApi.followBoard(getToken(this), getIntent().getStringExtra(getString(R.string.board_id_prefix)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {

                        switch (response.code()) {
                            case HttpURLConnection.HTTP_CREATED:
                                PrivateBoardActivity.launch(getApplicationContext(), gson.toJson(board));
                                finish();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(JoinBoardActivity.this, R.string.failed_to_follow_board, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(JoinBoardActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    private void getBoardDetails(String boardId) {
        restApi.getBoardById(boardId, getToken(this))
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Board>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<Board> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                board = response.body();
                                populateBoardDetails(board);

                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    private void populateBoardDetails(Board board) {
        toolbar.setTitle(board.getName());
        toolbar.setBoardTitle(board.getBoardType().equals(getString(R.string.public_lowercase)) ? R.string.public_board : R.string.private_board);
        toolbar.setLeagueLogo(picasso, board.getBoardIconUrl());
        toolbar.setBackgroundColor(Color.parseColor(board.getColor()));
        boardCardView.setCardBackgroundColor(Color.parseColor(board.getColor()));
        description.setText(board.getDescription());
    }
}
