package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Zones;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import life.plank.juna.zone.view.adapter.onboarding.SelectZoneAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.Auth.getToken;
import static life.plank.juna.zone.util.RestUtilKt.errorToast;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class SelectZoneActivity extends AppCompatActivity {
    private static final String TAG = SelectZoneActivity.class.getSimpleName();

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    @BindView(R.id.follow_button)
    Button followButton;

    @Inject
    RestApi restApi;

    SelectZoneAdapter selectZoneAdapter;
    private ArrayList<Zones> zonesList = new ArrayList<>();
    Zones zone = new Zones();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_zone);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        retrieveZones();
        initRecyclerView();
    }

    @OnClick(R.id.follow_button)
    public void onFollowButtonClick() {
        zone.setZoneIds(selectZoneAdapter.getSelectedZoneNames());
        followZones(zone);
    }

    public void retrieveZones() {
        restApi.retrieveZones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Zones>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "retrieveZones() : onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "retrieveZones() : onError()" + e);
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<List<Zones>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                setUpAdapterWithNewData(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                errorToast(R.string.failed_to_retrieve_zones, response);
                                break;
                            default:
                                errorToast(R.string.something_went_wrong, response);
                                break;
                        }

                    }
                });
    }

    private void followZones(Zones zones) {
        restApi.followZones(getToken(), zones)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<JsonObject>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<JsonObject> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                startActivity(new Intent(SelectZoneActivity.this, HomeActivity.class));
                                finish();
                                break;
                            default:
                                errorToast(R.string.something_went_wrong, response);
                                break;
                        }
                    }
                });
    }

    private void setUpAdapterWithNewData(List<Zones> zonesList) {
        this.zonesList.clear();
        this.zonesList.addAll(zonesList);
        selectZoneAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        selectZoneAdapter = new SelectZoneAdapter(zonesList, Glide.with(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(selectZoneAdapter);

    }
}