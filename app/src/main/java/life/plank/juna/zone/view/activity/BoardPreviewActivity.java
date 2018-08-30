package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;

public class BoardPreviewActivity extends AppCompatActivity {

    private static final String TAG = BoardPreviewActivity.class.getSimpleName();
    @BindView(R.id.board_parent_layout)
    CardView boardCardView;
    @BindView(R.id.private_board_title)
    TextView boardTitle;
    @BindView(R.id.options_image)
    ImageView optionsImage;
    @BindView(R.id.share_image)
    ImageView shareImage;
    @BindView(R.id.following_text_view)
    TextView followTextView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.preview_board_layout)
    RelativeLayout boardLayout;
    @BindView(R.id.create_board_button)
    Button createBoard;
    Board board;
    @Inject
    @Named("default")
    Retrofit retrofit;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_preview);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        board = Board.getInstance();
        boardTitle.setText(board.getDisplayname());
        boardCardView.setCardBackgroundColor(Color.parseColor(board.getColor()));
        description.setText(board.getDescription());

        optionsImage.setVisibility(View.INVISIBLE);
        shareImage.setVisibility(View.INVISIBLE);
        followTextView.setVisibility(View.INVISIBLE);

        boardLayout.setBackground(new BitmapDrawable(getResources(), CreateBoardActivity.parentViewBitmap));
    }

    @OnClick({R.id.create_board_button})
    public void createBoard() {
        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));
        restApi.createPrivateBoard(board.getBoardType(), board, token)
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
                                navigateToBoard(response.body(), token);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.could_not_create_board, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    private void navigateToBoard(String boardId, String token) {
        restApi.getBoardById(boardId, token)
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
                                Board.setInstance(response.body());
                                startActivity(new Intent(BoardPreviewActivity.this, PrivateBoardActivity.class));
                                finish();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.could_not_navigate_to_board, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }
}
