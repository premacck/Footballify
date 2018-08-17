package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
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
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefs;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity implements OnClickFeedItemListener {
    private static final String TAG = BoardActivity.class.getSimpleName();
    public static Bitmap boardParentViewBitmap = null;
    public static Bitmap blurredBitmap = null;
    public static RenderScript renderScript;
    @Inject
    @Named("default")
    Retrofit retrofit;
    int TRCNumber = 20;
    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;
    @BindView(R.id.following_text_view)
    TextView followingTextView;
    @BindView(R.id.board_parent_layout)
    CardView boardParentLayout;
    BoardMediaAdapter boardMediaAdapter;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.home_team_logo_image_view)
    ImageView homeTeamLogoImageView;
    @BindView(R.id.visiting_team_logo_image_view)
    ImageView visitingTeamLogoImageView;
    @BindView(R.id.home_team_score)
    TextView homeTeamScore;
    @BindView(R.id.visiting_team_score)
    TextView visitingTeamScore;
    @BindView(R.id.layout_board_engagement)
    RelativeLayout layoutBoardEngagement;
    @BindView(R.id.layout_info_tiles)
    RelativeLayout layoutInfoTiles;

    private ArrayList<FootballFeed> boardFeed;
    private RestApi restApi;
    private String enterBoardId;
    private String homeTeamLogo, awayTeamLogo;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataReceivedFromPushNotification(intent);
        }
    };

    public static void launch(Context packageContext, int homeGoals, int awayGoals, long matchId,
                              String homeTeamLogo, String awayTeamLogo) {
        Intent intent = new Intent(packageContext, BoardActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_home_team_score), homeGoals)
                .putExtra(packageContext.getString(R.string.intent_visiting_team_score), awayGoals)
                .putExtra(packageContext.getString(R.string.match_id_string), matchId)
                .putExtra(packageContext.getString(R.string.pref_home_team_logo), homeTeamLogo)
                .putExtra(packageContext.getString(R.string.pref_away_team_logo), awayTeamLogo);
        packageContext.startActivity(intent);
    }

    public void setDataReceivedFromPushNotification(Intent intent) {
        String title = intent.getStringExtra(getString(R.string.intent_comment_title));
        String contentType = intent.getStringExtra(getString(R.string.intent_content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.intent_thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.intent_thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.intent_thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.intent_image_url));
        FootballFeed footballFeed = new FootballFeed();
        Log.e(TAG, "content_type: " + contentType);
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
        boardFeed.add(0, footballFeed);
        boardMediaAdapter.notifyItemInserted(0);
        boardRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Device ID", FirebaseInstanceId.getInstance().getToken());
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        progressBar.setVisibility(View.VISIBLE);
        if (boardFeed == null) boardFeed = new ArrayList<>();

        Intent intent = getIntent();
        long currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
        homeTeamLogo = intent.getStringExtra(getString(R.string.pref_home_team_logo));
        awayTeamLogo = intent.getStringExtra(getString(R.string.pref_away_team_logo));

        initRecyclerView();
        setUpBoomMenu();

        retrieveBoard(currentMatchId, AppConstants.BOARD_TYPE);
        setToolbarTeamLogo();

        layoutBoardEngagement.setBackgroundColor(getColor(R.color.transparent_white_one));
        layoutInfoTiles.setBackgroundColor(getColor(R.color.transparent_white_two));
    }

    private void setToolbarTeamLogo() {
        Picasso.with(this).load(homeTeamLogo).fit().centerCrop().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder).into(homeTeamLogoImageView);
        Picasso.with(this).load(awayTeamLogo).fit().centerCrop().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder).into(visitingTeamLogoImageView);
        homeTeamScore.setText(getIntent().getStringExtra(getString(R.string.intent_home_team_score)));
        visitingTeamScore.setText(getIntent().getStringExtra(getString(R.string.intent_visiting_team_score)));
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

    //todo: Inject adapter
    private void initRecyclerView() {
        boardMediaAdapter = new BoardMediaAdapter(this, boardFeed);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardMediaAdapter.setOnClickFeedItemListener(this);
        boardRecyclerView.setAdapter(boardMediaAdapter);
        renderScript = RenderScript.create(this);

    }

    private void setUpAdapterWithNewData(List<FootballFeed> boardFeedList) {
        boardFeed.clear();
        boardFeed.addAll(boardFeedList);
        boardMediaAdapter.notifyDataSetChanged();
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.text_icon, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Comment", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);
            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
            //TODO: move common code to separate method
            arcMenu.addItem(child, titles[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            break;
                        }
                        case 2: {
                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.gallery));
                            intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.camera));
                            intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_audio));
                            intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {
                            Intent intent = new Intent(BoardActivity.this, PostCommentActivity.class);
                            intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
                            startActivity(intent);
                            break;
                        }
                        case 7: {
                            break;
                        }
                        case 8: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.video));
                            intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_board_activity));
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }

    @OnClick({R.id.following_text_view})
    //todo: can subscribe to Board,card and User field
    public void onViewClicked(View view) {
        String id = getString(R.string.board_id_prefix) + enterBoardId;
        switch (view.getId()) {
            case R.id.following_text_view:
                if (followingTextView.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    followingTextView.setText(R.string.unfollow);
                    FirebaseMessaging.getInstance().subscribeToTopic(id);
                } else {
                    followingTextView.setText(R.string.follow);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
                }
                break;
        }
    }

    public void retrieveBoard(Long foreignId, String boardType) {

        String token = getString(R.string.bearer) + " " + getSharedPrefs(getString(R.string.login_credentails), getString(R.string.azure_token));

        restApi.retrieveBoard(foreignId, boardType, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Board>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<Board> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            enterBoardId = response.body().getId();
                            saveBoardId();
                            retrieveBoardByBoardId(enterBoardId);
                            String topic = getString(R.string.board_id_prefix) + enterBoardId;
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);

                        }
                    }
                });
    }

    public void saveBoardId() {
        SharedPreferences.Editor boardIdEditor;
        boardIdEditor = getSharedPreferences(getString(R.string.pref_enter_board_id), Context.MODE_PRIVATE).edit();
        boardIdEditor.putString(getString(R.string.pref_enter_board_id), enterBoardId).apply();
    }

    public void retrieveBoardByBoardId(String boardId) {

        String token = getString(R.string.bearer) + " " + getSharedPrefs(getString(R.string.login_credentails), getString(R.string.azure_token));

        progressBar.setVisibility(View.VISIBLE);
        restApi.retrieveByBoardId(boardId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error()" + e);
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null)
                                    setUpAdapterWithNewData(response.body());
                                else
                                    Toast.makeText(BoardActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(BoardActivity.this, R.string.board_not_populated, Toast.LENGTH_SHORT).show();
                                ;
                                break;
                            default:
                                Toast.makeText(BoardActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int position) {

        boardParentViewBitmap = loadBitmap(boardParentLayout, boardParentLayout, this);
        Intent intent = new Intent(this, BoardFeedDetailActivity.class);
        intent.putExtra(getString(R.string.intent_position), String.valueOf(position));
        intent.putExtra(getString(R.string.intent_feed_items), new Gson().toJson(boardFeed));
        intent.putExtra(getString(R.string.intent_board_id), enterBoardId);
        startActivity(intent);
    }
}