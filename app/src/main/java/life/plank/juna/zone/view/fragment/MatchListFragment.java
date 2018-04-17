package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchListModel;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.MatchListAdapter;

public class MatchListFragment extends Fragment {
    @BindView(R.id.match_list_recycler_view)
    RecyclerView matchListRecyclerView;
    MatchListAdapter matchListAdapter;
    List<MatchListModel> matchListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        ButterKnife.bind(this, view);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        matchListModel = new ArrayList<MatchListModel>();
        matchListAdapter = new MatchListAdapter(getContext(), matchListModel);
        matchListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        matchListRecyclerView.setAdapter(matchListAdapter);
    }

    @OnClick(R.id.cancel_image_view)
    public void onCancel() {
        ((SwipePageActivity) getActivity()).retainHomeLayout();
    }
}