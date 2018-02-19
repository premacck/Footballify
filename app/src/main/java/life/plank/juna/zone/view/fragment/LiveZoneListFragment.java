package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneMatchListAdapter;

/**
 * Created by plank-niraj on 16-02-2018.
 */

public class LiveZoneListFragment extends Fragment implements LiveZoneMatchListAdapter.OnClickListeners {

    Context context;
    private Unbinder unbinder;
    @BindView(R.id.live_zone_list)
    RecyclerView liveZoneListView;
    LiveZoneMatchListAdapter liveZoneListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livezone_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpView();
        return view;
    }

    private void setUpView() {
        liveZoneListAdapter = new LiveZoneMatchListAdapter(context,this);
        liveZoneListView.setLayoutManager(new LinearLayoutManager(context));
        liveZoneListView.setAdapter(liveZoneListAdapter);
    }

    @OnClick(R.id.close_image)
    public void onCloseImageClicked() {
        ((SwipePageActivity) getActivity()).retainLayout();
    }

    @Override
    public void onClick(int matchNumber) {
        ((SwipePageActivity) getActivity()).goToLiveMatch(matchNumber);
    }

}
