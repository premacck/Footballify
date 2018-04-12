package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.ZoneApplication.getApplication;

public class StandingFragment extends Fragment {
    @Inject
    @Named("default")
    Retrofit retrofit;
    @BindView(R.id.table_recycler_view)
    RecyclerView scoreTableRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancleImageView;
    StandingTableAdapter standingTableAdapter;
    List<StandingModel> standingModels;
    private String TAG = StandingFragment.class.getSimpleName();
    private RestApi restApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standing, container, false);
        ButterKnife.bind(this, view);
        getApplication().getStandingScoreNetworkComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        setUpRecyclerViewInStandingScoreTable();
        getStandings(2);
        return view;
    }

    private void setUpRecyclerViewInStandingScoreTable() {
        standingModels = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter(getActivity(), standingModels);
        scoreTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        scoreTableRecyclerView.setAdapter(standingTableAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(scoreTableRecyclerView.getContext(),
                new LinearLayoutManager(getActivity()).getOrientation());
        scoreTableRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModelResponse) {
        standingModels.addAll(standingModelResponse);
        standingTableAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        UIDisplayUtil.getInstance().hideSoftKeyboard(getView(), getActivity());
        ((SwipePageActivity) getActivity()).retainHomeLayout();
    }

    public void getStandings(int id) {
        restApi.getStandings(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<StandingModel>>>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "response: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.print("error--" + e);
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        Log.e(TAG, "response: " + ", list data " + response.toString());
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populateStandingRecyclerView(response.body());
                        }
                    }
                });
    }
}