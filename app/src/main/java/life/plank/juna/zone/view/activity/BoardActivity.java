package life.plank.juna.zone.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.BoardCreationModel;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.Thumbnail;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-hasan on 5/3/2018.
 */

public class BoardActivity extends AppCompatActivity {
    private static final String TAG = BoardActivity.class.getSimpleName();
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
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    BoardMediaAdapter boardMediaAdapter;
    GridLayoutManager gridLayoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.home_team_logo_image_view)
    ImageView homeTeamLogoImageView;
    @BindView(R.id.visiting_team_logo_image_view)
    ImageView visitingTeamLogoImageView;
    private int apiHitCount = 0;
    private ArrayList<FootballFeed> boardFeed = new ArrayList<>();
    private RestApi restApi;
    private Subscription subscription;
    private String nextPageToken = "";
    private int PAGE_SIZE;
    private boolean isLoading = false;
    private RenderScript renderScript;
    private String enterBoardId;
    private String objectId;
    private long currentMatchId;
    private String homeTeamLogo, awayTeamLogo;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            setDataReceivedFromPushNotification(intent);

        }
    };

    public void setDataReceivedFromPushNotification(Intent intent) {

        String contentType = intent.getStringExtra(getString(R.string.content_type));
        String thumbnailUrl = intent.getStringExtra(getString(R.string.thumbnail_url));
        Integer thumbnailHeight = intent.getIntExtra(getString(R.string.thumbnail_height), 0);
        Integer thumbnailWidth = intent.getIntExtra(getString(R.string.thumbnail_width), 0);
        String imageUrl = intent.getStringExtra(getString(R.string.image_url));

        FootballFeed footballFeed = new FootballFeed();
        footballFeed.setContentType(contentType);
        Thumbnail thumbnail = new Thumbnail();
        thumbnail.setImageWidth(thumbnailWidth);
        thumbnail.setImageHeight(thumbnailHeight);
        thumbnail.setImageUrl(thumbnailUrl);
        footballFeed.setThumbnail(thumbnail);
        footballFeed.setUrl(imageUrl);

        boardFeed.add(footballFeed);
        Collections.reverse(boardFeed);
        boardMediaAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Device ID", FirebaseInstanceId.getInstance().getToken());
        setContentView(R.layout.activity_board);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getBoardFeedNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        progressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
        setUpBoomMenu();
        SharedPreferences preference = UIDisplayUtil.getSignupUserData(this);
        objectId = preference.getString(getString(R.string.object_id_string), "NA");
        SharedPreferences matchPref = getSharedPreferences(getString(R.string.match_pref), 0);
        currentMatchId = matchPref.getLong(getString(R.string.match_id_string), 0);
        enterBoardApiCall(enterBoardId, objectId);
        retrieveBoard(currentMatchId, AppConstants.BOARD_TYPE);
        Intent intent = getIntent();
        homeTeamLogo = intent.getStringExtra(getString(R.string.home_team_logo));
        awayTeamLogo = intent.getStringExtra(getString(R.string.away_team_logo));
        setToolbarTeamLogo();
    }

    private void setToolbarTeamLogo() {
        Picasso.with(this).load(homeTeamLogo).fit().centerCrop().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder).into(homeTeamLogoImageView);
        Picasso.with(this).load(awayTeamLogo).fit().centerCrop().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder).into(visitingTeamLogoImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.board_intent)));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    //todo: Inject adapter
    private void initRecyclerView() {
        boardMediaAdapter = new BoardMediaAdapter(this, boardFeed);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(boardMediaAdapter);
    }

    private void setUpAdapterWithNewData(List<FootballFeed> boardFeedList) {
        if (!boardFeedList.isEmpty() && boardFeedList.size() > 0) {
            Collections.reverse(boardFeedList);
            boardFeed.addAll(boardFeedList);
            boardMediaAdapter.notifyDataSetChanged();
        }
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);
            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
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
                            intent.putExtra(getString(R.string.open_from), getString(R.string.gallery));
                            intent.putExtra(getString(R.string.board_id), enterBoardId);
                            intent.putExtra(getString(R.string.board_api), getString(R.string.board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.open_from), getString(R.string.camera));
                            intent.putExtra(getString(R.string.board_id), enterBoardId);
                            intent.putExtra(getString(R.string.board_api), getString(R.string.board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.open_from), getString(R.string.audio));
                            intent.putExtra(getString(R.string.board_id), enterBoardId);
                            intent.putExtra(getString(R.string.board_api), getString(R.string.board_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {
                            break;
                        }
                        case 7: {
                            Intent intent = new Intent(BoardActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.open_from), getString(R.string.video));
                            intent.putExtra(getString(R.string.board_id), enterBoardId);
                            intent.putExtra(getString(R.string.board_api), getString(R.string.board_activity));
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
        String id = "Board-" + enterBoardId;
        switch (view.getId()) {
            case R.id.following_text_view:
                if (followingTextView.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
                    followingTextView.setText(R.string.unfollow);
                    Log.e("Board id", "onCompleted: " + id);
                    FirebaseMessaging.getInstance().subscribeToTopic(id);
                } else {
                    followingTextView.setText(R.string.follow);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
                }
                break;
        }
    }

    private void enterBoardApiCall(String enterBoard, String userId) {
        restApi.enterBoard(enterBoard, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<JsonObject> jsonObjectResponse) {
                        Log.e("", "onNext: " + jsonObjectResponse);
                        Toast.makeText(BoardActivity.this, "You entered in Board Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void retrieveBoard(Long foreignId, String boardType) {
        restApi.retrieveBoard(foreignId, boardType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BoardCreationModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted-->: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "In onError()" + e);
                    }

                    @Override
                    public void onNext(Response<BoardCreationModel> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            enterBoardId = response.body().getId();
                            retrieveBoardByBoardId(enterBoardId);

                        }
                    }
                });
    }

    public void retrieveBoardByBoardId(String boardId) {
        progressBar.setVisibility(View.VISIBLE);
        restApi.retrieveByBoardId(boardId)
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
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e(TAG, "On Error()" + e);
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.code() == HttpURLConnection.HTTP_OK && response.body() != null) {
                            Log.e(TAG, "Retrieved details: ");
                            setUpAdapterWithNewData(response.body());
                        }
                    }
                });
    }
}