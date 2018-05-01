package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.ScoreFixtureAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;

public class ScoreFixtureFragment extends Fragment implements View.OnClickListener {
    @Inject
    @Named("default")
    Retrofit retrofit;
    Context context;
    @BindView(R.id.score_recycler_view)
    RecyclerView scoreRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancelImage;
    @BindView(R.id.show_scores_text_view)
    TextView showScores;
    private Unbinder unbinder;
    private RestApi restApi;
    private String TAG = ScoreFixtureFragment.class.getSimpleName();
    List<ScoreFixtureModel> scoreFixtureModelList;
    ScoreFixtureAdapter fixtureAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score_fixture, container, false);
        unbinder = ButterKnife.bind(this, view);
        getApplication().getScoreFixtureNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        initializeRecyclerView();
        getScoreFixture(AppConstants.SEASON_NAME);

        return view;
    }

    private void initializeRecyclerView() {
        scoreFixtureModelList = new ArrayList<>();
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        fixtureAdapter = new ScoreFixtureAdapter(getActivity(), scoreFixtureModelList);
        scoreRecyclerView.setAdapter(fixtureAdapter);
        showScores.setOnClickListener(this);

    }

    public void populateScoreFixtureRecyclerView(List<ScoreFixtureModel> scoreFixtureModelResponse) {
        scoreFixtureModelList.addAll(scoreFixtureModelResponse);
        fixtureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {

        Fragment matchScoreFragment = new MatchScoreFragment(scoreFixtureModelList);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_frame_layout, matchScoreFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

    }

    public void getScoreFixture(String seasonName) {
        restApi.getScoresAndFixtures(seasonName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ScoreFixtureModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "response: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onNext(Response<List<ScoreFixtureModel>> response) {
                        Log.e(TAG, "response: " + ", list data " + response.toString());
                        if(response.code() == HttpURLConnection.HTTP_OK)
                        {
                            populateScoreFixtureRecyclerView(response.body());
                        }

                    }
                });
    }
}

