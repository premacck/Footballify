package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
import life.plank.juna.zone.data.network.model.Zones;
import life.plank.juna.zone.view.adapter.ZoneAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class ZoneActivity extends AppCompatActivity {
    private static final String TAG = ZoneActivity.class.getSimpleName();

    @BindView(R.id.board_recycler_view)
    RecyclerView boardRecyclerView;
    ZoneAdapter zoneAdapter;

    @Inject
    @Named("default")
    Retrofit retrofit;

    private RestApi restApi;
    private ArrayList<Zones> zones = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zone);
        ButterKnife.bind(this);

        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        retrieveZones();
        initRecyclerView();
    }

    public void retrieveZones() {
        restApi.retrieveZones()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<List<Zones>>>() {
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
                    public void onNext(Response<List<Zones>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                setUpAdapterWithNewData(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
//                                Toast.makeText(ZoneActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                            default:
//                                Toast.makeText(ZoneActivity.this, R.string.failed_to_retrieve_feed, Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
    }

    private void setUpAdapterWithNewData(List<Zones> zonesList) {
        zones.clear();
        zones.addAll(zonesList);
        zoneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initRecyclerView() {
        zoneAdapter = new ZoneAdapter(this, zones);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        boardRecyclerView.setLayoutManager(gridLayoutManager);
        boardRecyclerView.setAdapter(zoneAdapter);

    }
}