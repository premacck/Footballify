package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

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
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.data.network.model.firebaseModel.BoardNotificationModel;
import life.plank.juna.zone.firebasepushnotification.database.DBHelper;
import life.plank.juna.zone.util.AppConstants;
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
    ArrayList<BoardNotificationModel> boardNotificationModelArrayList = new ArrayList<>();
    DBHelper dbHelper;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    BoardMediaAdapter boardMediaAdapter;
    GridLayoutManager gridLayoutManager;
    private int apiHitCount = 0;
    private List<FootballFeed> boardFeeds;
    private RestApi restApi;
    private Subscription subscription;
    private String nextPageToken = "";
    private int PAGE_SIZE;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private RenderScript renderScript;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                arcMenu.show();
            super.onScrollStateChanged( recyclerView, newState );
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled( recyclerView, dx, dy );
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    isLoading = true;
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            //getBoardApiCall();

                        }
                    }, AppConstants.PAGINATION_DELAY );
                }
            }
            if (dy > 0 || dy < 0 && arcMenu.isShown())
                arcMenu.hide();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Log.e( "Device ID", FirebaseInstanceId.getInstance().getToken() );
        setContentView( R.layout.activity_board );
        ButterKnife.bind( this );
        ((ZoneApplication) getApplication()).getBoardFeedNetworkComponent().inject( this );
        restApi = retrofit.create( RestApi.class );
        dbHelper = new DBHelper( this );
        populateUplaodedData();
        initRecyclerView();
        setUpBoomMenu();
    }


    public void populateUplaodedData() {
        ArrayList<String> dataList = dbHelper.getDataList();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Gson gson = new Gson();
                BoardNotificationModel boardNotificationModel = gson.fromJson( dataList.get( i ), BoardNotificationModel.class );
                boardNotificationModelArrayList.add( boardNotificationModel );
            }
        }
    }

    //todo: Inject adapter
    private void initRecyclerView() {
        boardMediaAdapter = new BoardMediaAdapter( this, boardNotificationModelArrayList );
        GridLayoutManager gridLayoutManager = new GridLayoutManager( this, 3, GridLayoutManager.VERTICAL, false );
        boardRecyclerView.setLayoutManager( gridLayoutManager );
        boardRecyclerView.setAdapter( boardMediaAdapter );
        boardRecyclerView.setHasFixedSize( true );
    }

    public String updateToken(String nextPageToken, String replaceStringRT, String replaceStringTRC) {
        if (!nextPageToken.isEmpty()) {
            String updatedNextPageToken = nextPageToken.replaceFirst( AppConstants.REGULAR_EXPRESSION_RT, replaceStringRT );
            updatedNextPageToken = updatedNextPageToken.replaceFirst( AppConstants.REGULAR_EXPRESSION_TRC, replaceStringTRC );
            return updatedNextPageToken;
        }
        return "";
    }
    /*  //todo: Inject adapter
        private void initRecyclerView() {
            int numberOfRows = 3;
            boardFeeds = new ArrayList<>();
            gridLayoutManager = new GridLayoutManager( this, numberOfRows, GridLayoutManager.VERTICAL, false );
            boardRecyclerView.setLayoutManager( gridLayoutManager );
            boardMediaAdapter = new BoardMediaAdapter( this, boardFeeds );
            boardRecyclerView.setAdapter( boardMediaAdapter );
            boardRecyclerView.setHasFixedSize( true );
            boardRecyclerView.addOnScrollListener( recyclerViewOnScrollListener );
            renderScript = RenderScript.create( this );
        }*/
    public void getBoardApiCall() {
        subscription = restApi.getBoardFeed( updateToken( nextPageToken,
                getString( R.string.replace_rt ) + String.valueOf( apiHitCount ), getString( R.string.replace_trc ) + String.valueOf( apiHitCount * TRCNumber ) ) )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d( "", "In onCompleted()" );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d( "", "In onCompleted()" );
                    }

                    public void onNext(Response<List<FootballFeed>> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            if (apiHitCount == 0) {
                                nextPageToken = response.headers().get( AppConstants.FOOTBALL_FEEDS_HEADER_KEY );
                            }
                            setUpAdapterWithNewData( response.body() );
                            apiHitCount = apiHitCount + 1;
                        } else {
                            Toast.makeText( BoardActivity.this, "Error message", Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }

    private void setUpAdapterWithNewData(List<FootballFeed> footballFeedsList) {
        if (!footballFeedsList.isEmpty() && footballFeedsList.size() > 0) {
            if ("".contentEquals( nextPageToken ) ? (isLastPage = true) : (isLoading = false)) ;
            boardFeeds.addAll( footballFeedsList );
            boardMediaAdapter.notifyDataSetChanged();
            PAGE_SIZE = footballFeedsList.size();
        }
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon( R.drawable.ic_un, R.drawable.ic_close_white );
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate( R.layout.layout_floating_action_button, null );
            //child.setId(i);
            RelativeLayout fabRelativeLayout = child.findViewById( R.id.fab_relative_layout );
            ImageView fabImageVIew = child.findViewById( R.id.fab_image_view );
            fabRelativeLayout.setBackground( ContextCompat.getDrawable( this, backgroundColors[i] ) );
            fabImageVIew.setImageResource( fabImages[i] );
            final int position = i;
            arcMenu.addItem( child, titles[i], new View.OnClickListener() {
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
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Gallery" );
                            intent.putExtra( "API", "BoardActivity" );
                            startActivity( intent );
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Camera" );
                            intent.putExtra( "API", "BoardActivity" );
                            startActivity( intent );
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Audio" );
                            intent.putExtra( "API", "BoardActivity" );
                            startActivity( intent );
                            break;
                        }
                        case 6: {
                            break;
                        }
                        case 7: {
                            Intent intent = new Intent( BoardActivity.this, CameraActivity.class );
                            intent.putExtra( "OPEN_FROM", "Video" );
                            intent.putExtra( "API", "BoardActivity" );
                            startActivity( intent );
                            break;
                        }
                    }
                }
            } );
        }
    }

    @OnClick({R.id.following_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.following_text_view:
                if (followingTextView.getText().toString().equalsIgnoreCase( "FOLLOWING" )) {
                    followingTextView.setText( R.string.unfollow );
                    FirebaseMessaging.getInstance().subscribeToTopic( "ManUvsManCity" );
                } else {
                    followingTextView.setText( R.string.following );
                    FirebaseMessaging.getInstance().unsubscribeFromTopic( "ManUvsManCity" );
                }
                break;
        }
    }
}