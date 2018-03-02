package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;

public class StandingFragment extends Fragment {
    @BindView(R.id.table_recycler_view)
    RecyclerView scoreTableRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancleImageView;

    public StandingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_standing, container, false);
        ButterKnife.bind(this, view);
        populateStandingRecyclerView();
        return view;
    }

    public void populateStandingRecyclerView() {
        StandingTableAdapter standingTableAdapter = new StandingTableAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        scoreTableRecyclerView.setLayoutManager(layoutManager);
        scoreTableRecyclerView.setAdapter(standingTableAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(scoreTableRecyclerView.getContext(),
                layoutManager.getOrientation());
        scoreTableRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        ((SwipePageActivity) getActivity()).retainHomeLayout();
    }
}
