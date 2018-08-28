package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import life.plank.juna.zone.data.network.model.UserFeed;
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
    @BindView(R.id.user_feed_recycler_view)
    RecyclerView userFeedRecyclerView;
    @BindView(R.id.user_zone_recycler_view)
    RecyclerView userZoneRecyclerView;
    private RestApi restApi;
    private ArrayList<UserFeed> userFeed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);

        initRecyclerView();
        initZoneRecyclerView();
        getUserFeed();
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

    //TODO: replace this with on selection of football feed board
    @OnClick({R.id.user_profile})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.user_profile:
                startActivity(new Intent(UserFeedActivity.this, SwipePageActivity.class));
                break;
        }
    }

    public void getUserFeed() {

        String token = getSharedPrefsBoolean(getString(R.string.pref_login_credentails), getString(R.string.pref_is_logged_in)) ?
                getString(R.string.bearer) + " " + getSharedPrefsString(getString(R.string.pref_login_credentails), getString(R.string.pref_azure_token)) : "";

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

    private void setUpAdapterWithNewData(List<UserFeed> userFeedList) {
        userFeed.clear();
        userFeed.addAll(userFeedList);
        userFeedAdapter.notifyDataSetChanged();
    }
}
