package life.plank.juna.zone.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Board;
import life.plank.juna.zone.data.network.model.UserFeed;
import life.plank.juna.zone.view.adapter.UserBoardsAdapter;
import life.plank.juna.zone.view.adapter.UserFeedAdapter;
import life.plank.juna.zone.view.adapter.UserZoneAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsBoolean;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefsString;

public class UserFeedActivity extends AppCompatActivity {
    private static final String TAG = UserFeedActivity.class.getSimpleName();

    @Inject
    @Named("default")
    Retrofit retrofit;
    UserFeedAdapter userFeedAdapter;
    UserZoneAdapter userZoneAdapter;
    private UserBoardsAdapter userBoardsAdapter;
    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_zone_recycler_view)
    RecyclerView userZoneRecyclerView;
    @Inject
    Picasso picasso;
    private RestApi restApi;
    private String token;
    private ArrayList<UserFeed> userFeed = new ArrayList<>();
    ArrayList<Board> userBoards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        SharedPreferences editor = getApplicationContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        String userObjectId = editor.getString(getApplicationContext().getString(R.string.pref_object_id), "NA");

        String topic = getString(R.string.juna_user_topic) + userObjectId;
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        token = getSharedPrefsBoolean(getString(R.string.pref_login_credentails), getString(R.string.pref_is_logged_in)) ?
                getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token)) : "";
        initRecyclerView();
        initZoneRecyclerView();
        initBoardsRecyclerView();
        getUserFeed();
        getUserBoards();
    }

    private void initRecyclerView() {
        userFeedAdapter = new UserFeedAdapter(this, userFeed);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        userFeedRecyclerView.setLayoutManager(gridLayoutManager);
        userFeedRecyclerView.setAdapter(userFeedAdapter);

    }

    private void initZoneRecyclerView() {
        userZoneAdapter = new UserZoneAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        userZoneRecyclerView.setLayoutManager(gridLayoutManager);
        userZoneRecyclerView.setAdapter(userZoneAdapter);

    }

    private void initBoardsRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.user_boards_recycler_view);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(UserFeedActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        userBoardsAdapter = new UserBoardsAdapter(userBoards, this, picasso);
        recyclerView.setAdapter(userBoardsAdapter);

    }

    public void getUserFeed() {
        restApi.getUserFeed(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<UserFeed>>>() {
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
                    public void onNext(Response<List<UserFeed>> response) {

                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null)
                                    setUpAdapterWithNewData(response.body());
                                else
                                    Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    public void getUserBoards() {
        restApi.getFollowingBoards(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Board>>>() {
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
                    public void onNext(Response<List<Board>> response) {

                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null) {
                                    userBoards.clear();
                                    userBoards.addAll(response.body());
                                    userBoardsAdapter.notifyDataSetChanged();
                                } else
                                    Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(UserFeedActivity.this, R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    private void setUpAdapterWithNewData(List<UserFeed> userFeedList) {
        userFeed.clear();
        userFeed.addAll(userFeedList);
        userFeedAdapter.notifyDataSetChanged();
    }
}
