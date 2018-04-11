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
import life.plank.juna.zone.view.adapter.ShowScoreAdapter;


public class ShowScoresFragment extends Fragment {
    Context context;
    @BindView(R.id.show_score_recycler_view)
    RecyclerView showScoreRecyclerView;
    @BindView(R.id.cancel_image_view)
    ImageView cancelImage;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_show_scores, container, false);
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
        ShowScoreAdapter showScoreAdapter = new ShowScoreAdapter(getActivity());
        showScoreRecyclerView.setAdapter(showScoreAdapter);
    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
