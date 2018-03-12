package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.ScoreFixtureAdapter;

public class ScoreFixtureFragment extends Fragment {

    Context context;
    @BindView(R.id.score_recycler_view)
    RecyclerView scoreRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancelImage;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        ((SwipePageActivity) getActivity()).retainFeedContainer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score_fixture, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeRecyclerView();
        return view;
    }

    private void initializeRecyclerView() {
        scoreRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ScoreFixtureAdapter fixtueAdapter = new ScoreFixtureAdapter(getActivity());
        scoreRecyclerView.setAdapter(fixtueAdapter);
    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
