package life.plank.juna.zone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

public class LiveZoneFragment extends Fragment {

    @BindView(R.id.liveZoneTv)
    TextView liveZoneTv;
    @BindView(R.id.liveZoneRl)
    RelativeLayout liveZoneRl;
    @BindView(R.id.liveZoneGridView)
    RecyclerView liveZoneGridView;
    @BindView(R.id.close)
    ImageView close;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livezone, container, false);
        unbinder = ButterKnife.bind(this, view);
        setUpGridView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setUpGridView() {
        liveZoneGridView.setLayoutManager(new GridLayoutManager(getActivity(), calculateNoOfColumns(), GridLayoutManager.HORIZONTAL, false));
        liveZoneGridView.setAdapter(new LiveZoneGridAdapter(getActivity()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

    }


    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 90;
        int columnCount = (int) (dpWidth / scalingFactor);
        return (columnCount >= 2 ? columnCount : 2);
    }


    @OnClick(R.id.close)
    public void onViewClicked() {
        ((SwipePageActivity) getActivity()).retainLayout();
    }
}
