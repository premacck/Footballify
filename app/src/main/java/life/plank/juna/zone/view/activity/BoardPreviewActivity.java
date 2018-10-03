package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;

public class BoardPreviewActivity extends AppCompatActivity {

    private static final String TAG = BoardPreviewActivity.class.getSimpleName();
    @BindView(R.id.board_parent_layout)
    CardView boardCardView;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.create_board_button)
    Button createBoard;
    @BindView(R.id.preview_toolbar)
    GenericToolbar toolbar;
    @BindView(R.id.title)
    TextView titleTextView;

    Board board;
    String filePath;
    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;

    public static void launch(Context packageContext, String board, String filePath) {
        Intent intent = new Intent(packageContext, BoardPreviewActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_board), board);
        intent.putExtra(packageContext.getString(R.string.intent_file_path), filePath);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_preview);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.intent_board))) {
            board = gson.fromJson(intent.getStringExtra(getString(R.string.intent_board)), Board.class);
        }

        if (intent != null && intent.hasExtra(getString(R.string.intent_file_path))) {
            filePath = intent.getStringExtra(getString(R.string.intent_file_path));
        }

        toolbar.setTitle(board.getDisplayname());
        boardCardView.setCardBackgroundColor(Color.parseColor(board.getColor()));
        description.setText(board.getDescription());
        toolbar.setLeagueLogo(picasso, Uri.fromFile(new File(filePath)).toString());

        toolbar.setupForPreview();
        titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), CreateBoardActivity.parentViewBitmap));
    }

    @OnClick({R.id.create_board_button})
    public void createBoard() {

        createBoard.setEnabled(false);

        File fileToUpload = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse(getString(R.string.media_type_image)), fileToUpload);
        MultipartBody.Part image = MultipartBody.Part.createFormData("", fileToUpload.getName(), requestBody);

        RequestBody displayName = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.getDisplayname());
        RequestBody zone = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.getZone());
        RequestBody description = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.getDescription());
        RequestBody color = RequestBody.create(MediaType.parse(getString(R.string.text_content_type)), board.getColor());

        String token = getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token));
        restApi.createPrivateBoard(board.getBoardType(), displayName, zone, description, color, image, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        createBoard.setEnabled(true);
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getApplicationContext(), R.string.could_not_create_board, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                navigateToBoard(response.body(), token);
                                break;
                            case HttpURLConnection.HTTP_CONFLICT:
                                Toast.makeText(getApplicationContext(), R.string.board_name_already_exists, Toast.LENGTH_LONG).show();
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
                                PrivateBoardActivity.launch(BoardPreviewActivity.this, gson.toJson(response.body()));
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
