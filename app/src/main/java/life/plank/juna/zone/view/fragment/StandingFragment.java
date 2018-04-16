package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.util.AppConstants;
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
    @BindView(R.id.standing_progress_bar)
    ProgressBar standingProgressBar;
    StandingTableAdapter standingTableAdapter;
    List<StandingModel> standingModel;
    @BindView(R.id.search_bar)
    EditText standingSearchBar;
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
        getStandings(AppConstants.COMPETITION_ID);
        setUpStandingSearchWithFootballTeamName();
        return view;
    }

    private void setUpRecyclerViewInStandingScoreTable() {
        standingModel = new ArrayList<>();
        standingTableAdapter = new StandingTableAdapter(getActivity(), standingModel);
        scoreTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        scoreTableRecyclerView.setAdapter(standingTableAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(scoreTableRecyclerView.getContext(),
                new LinearLayoutManager(getActivity()).getOrientation());
        scoreTableRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void populateStandingRecyclerView(List<StandingModel> standingModelResponse) {
        standingModel.add(null);
        standingModel.addAll(standingModelResponse);
        standingTableAdapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        UIDisplayUtil.getInstance().hideSoftKeyboard(getView(), getActivity());
        ((SwipePageActivity) getActivity()).retainHomeLayout();
    }

    public void getStandings(Integer id) {
        standingProgressBar.setVisibility(View.VISIBLE);
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
                        Log.d(TAG, "In onCompleted()");
                        standingProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Response<List<StandingModel>> response) {
                        Log.e(TAG, "response: " + ", list data " + response.toString());
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            populateStandingRecyclerView(response.body());
                        } else {
                            showToast(AppConstants.DEFAULT_ERROR_MESSAGE);
                        }
                        standingProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void setUpStandingSearchWithFootballTeamName() {
        standingSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        List<StandingModel> standingSearchList = new ArrayList();
        for (int teamList = 0; teamList < standingModel.size(); teamList++) {
            if (teamList == 0) {
                standingSearchList.add(null);
            } else {
                if (standingModel.get(teamList).getFootballTeam().toLowerCase().contains(text))
                    standingSearchList.add(standingModel.get(teamList));
            }
        }
        standingTableAdapter.updateList(standingSearchList);
    }
}