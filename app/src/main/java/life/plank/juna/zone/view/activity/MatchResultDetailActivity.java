package life.plank.juna.zone.view.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.view.activity.MatchResultActivity.matchStatsParentViewBitmap;

public class MatchResultDetailActivity extends AppCompatActivity {

    private static final String TAG = MatchResultDetailActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    Retrofit retrofit;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    List<StandingModel> standingModel;
    private StandingTableAdapter standingTableAdapter;
    private RestApi restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result_detail);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        blurBackgroundImageView.setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));
        restApi = retrofit.create(RestApi.class);
        initStandingRecyclerView();
        getStandings(AppConstants.LEAGUE_NAME);
    }

    public void initStandingRecyclerView() {
        standingModel = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter(this, standingModel);
        standingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        standingRecyclerView.setAdapter(standingTableAdapter);
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModels) {
        standingModel.addAll(standingModels);
        standingTableAdapter.notifyDataSetChanged();
    }

    public void getStandings(String leagueName) {
        progressBar.setVisibility(View.VISIBLE);
        restApi.getStandings(leagueName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e(TAG, "onError: " + e);
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                Log.d(TAG, "Data from Backend: " + response.body());

                                populateStandingRecyclerView(response.body());
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(MatchResultDetailActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
                            default:
                                Toast.makeText(MatchResultDetailActivity.this, R.string.failed_to_get_standings, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }
}
