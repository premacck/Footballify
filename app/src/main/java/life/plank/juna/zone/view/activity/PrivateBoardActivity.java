package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.PrivateBoardAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getContext;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

/**
 * Created by plank-dhamini on 25/7/2018.
 */

public class PrivateBoardActivity extends AppCompatActivity {
    private static final String TAG = PrivateBoardActivity.class.getSimpleName();

    public static RenderScript renderScript;
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Inject
    Picasso picasso;
    private RestApi restApi;

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;
    PrivateBoardAdapter privateBoardAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout_board_engagement)
    RelativeLayout layoutBoardEngagement;
    @BindView(R.id.layout_info_tiles)
    RelativeLayout layoutInfoTiles;
    @BindView(R.id.board_type_title)
    TextView boardTypeTitle;
    @BindView(R.id.lock)
    ImageView lock;
    @BindView(R.id.board_parent_layout)
    CardView boardCardView;
    @BindView(R.id.private_board_title)
    TextView boardTitle;
    Board board;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataReceivedFromPushNotification(intent);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_private_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initRecyclerView();

        board = Board.getInstance();
        setUpBoomMenu();
        layoutBoardEngagement.setBackgroundColor(getColor(R.color.transparent_white_one));
        layoutInfoTiles.setBackgroundColor(getColor(R.color.transparent_white_two));
        boardTypeTitle.setText(board.getBoardType());
        lock.setVisibility(View.VISIBLE);

        boardTitle.setText(board.getDisplayname());
        boardCardView.setCardBackgroundColor(Color.parseColor(board.getColor()));

        retrieveBoardByBoardId(board.getId());
        String topic = getString(R.string.board_id_prefix) + board.getId();
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.intent_board)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    public void setDataReceivedFromPushNotification(Intent intent) {
        String title = intent.getStringExtra(getString(R.string.intent_comment_title));
        String contentType = intent.getStringExtra(getString(R.string.intent_content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.intent_thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FootballFeed footballFeed = new FootballFeed();

        footballFeed.setContentType(contentType);
        if (contentType.equals(AppConstants.ROOT_COMMENT)) {
            footballFeed.setTitle(title);
        } else {
            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setImageWidth(thumbnailWidth);
            thumbnail.setImageHeight(thumbnailHeight);
            thumbnail.setImageUrl(thumbnailUrl);
            footballFeed.setThumbnail(thumbnail);
            footballFeed.setUrl(imageUrl);
        }
        privateBoardAdapter.updateNewPost(footballFeed);
        boardRecyclerView.smoothScrollToPosition(0);
    }

    private void initRecyclerView() {
        privateBoardAdapter = new PrivateBoardAdapter(picasso);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(privateBoardAdapter);
        renderScript = RenderScript.create(this);

    }

    public void retrieveBoardByBoardId(String boardId) {
        progressBar.setVisibility(View.VISIBLE);
        restApi.retrieveByBoardId(boardId, getToken(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "On Error()" + e);
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null)
                                    privateBoardAdapter.update(response.body());
                                else
                                    Toast.makeText(getContext(), R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getContext(), R.string.board_not_populated, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void setUpBoomMenu() {
        UIDisplayUtil.setupBoomMenu(this, arcMenu, board.getId());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PrivateBoardActivity.this, SwipePageActivity.class));
    }
}
