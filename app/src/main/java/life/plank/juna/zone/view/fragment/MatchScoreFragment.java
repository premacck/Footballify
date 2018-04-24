package life.plank.juna.zone.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.MatchScoreAdapter;

@SuppressLint("ValidFragment")
public class MatchScoreFragment extends Fragment {
    Context context;
    @BindView(R.id.show_score_recycler_view)
    RecyclerView showScoreRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancelImage;
    private Unbinder unbinder;
    List<ScoreFixtureModel> scoreFixtureModelList;
    @SuppressLint("ValidFragment")
    public MatchScoreFragment(List<ScoreFixtureModel> scoreFixtureModelList) {
        this.scoreFixtureModelList =scoreFixtureModelList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_scores, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        return view;
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        ((SwipePageActivity) getActivity()).retainFeedContainer();
    }

    private void initializeRecyclerView() {
        showScoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        MatchScoreAdapter matchScoreAdapter = new MatchScoreAdapter(getActivity(),scoreFixtureModelList);
        showScoreRecyclerView.setAdapter(matchScoreAdapter);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
