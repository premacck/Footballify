package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.mikepenz.itemanimators.ScaleXAnimator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.activity.LiveZoneSliderView;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;
import life.plank.juna.zone.viewmodel.LiveZoneGridModel;

public class LiveZoneFragment extends Fragment {

    @BindView(R.id.liveZoneTextView)
    TextView liveZoneTextView;
    @BindView(R.id.liveZoneRelativeLayout)
    RelativeLayout liveZoneRelativeLayout;
    @BindView(R.id.liveZoneGridViewRelativeLayout)
    RecyclerView liveZoneGridViewRecyclerView;
    @BindView(R.id.closeImage)
    ImageView closeImage;
    @BindView(R.id.liveZoneSlider)
    SliderLayout liveZoneSlider;
    Context context;
    LiveZoneGridAdapter adapter;
    int liveZoneGridViewHeight;
    private Unbinder unbinder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livezone, container, false);
        unbinder = ButterKnife.bind(this, view);
        getHeightDetails();
        setUpGridView();
        setUpSlider();
        setUpAnimation();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpGridView() {
        SnapHelper snapHelper = new StartSnapHelper();
        liveZoneGridViewRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5, GridLayoutManager.HORIZONTAL, false));
        liveZoneGridViewRecyclerView.setItemAnimator(new ScaleXAnimator());
        adapter = new LiveZoneGridAdapter(getActivity());
        liveZoneGridViewRecyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        snapHelper.attachToRecyclerView(liveZoneGridViewRecyclerView);
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 90;
        int columnCount = (int) (dpWidth / scalingFactor);
        return (columnCount >= 2 ? columnCount : 2);
    }

    @OnClick(R.id.closeImage)
    public void onCloseImageClicked() {
        ((SwipePageActivity) getActivity()).retainLayout();
    }

    /**
     * Get linearLayout after it is drawn.
     */
    public void getHeightDetails() {
        // TODO: 01-02-2018 Check the performance and change accordingly once implement the server data
        liveZoneGridViewRecyclerView.post(() -> {
            liveZoneGridViewHeight = liveZoneGridViewRecyclerView.getHeight();
            adapter.addData(liveZoneGridViewHeight);
        });
    }

    private void setUpSlider() {
        liveZoneSlider.stopAutoCycle();
        ArrayList<String> sliderData = new ArrayList<>();
        sliderData.add("text");
        sliderData.add("video");
        sliderData.add("text");
        sliderData.add("video");

        if (sliderData.size() > 0) {
            for (String data : sliderData) {
                LiveZoneSliderView textSliderView = new LiveZoneSliderView(getActivity(), data);
                liveZoneSlider.addSlider(textSliderView);
            }
        }
    }

    private void setUpAnimation() {
        Handler handler = new Handler();
        int delay = 10000;
        ArrayList<LiveZoneGridModel> liveZoneGridModels = new ArrayList<>();
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_one));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_six));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_three));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_two));
        handler.postDelayed(new Runnable() {
            public void run() {
                int position = ((GridLayoutManager) liveZoneGridViewRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                for (int i = 0; i <= liveZoneGridModels.size() - 1; i++) {
                    adapter.addData(position + i, liveZoneGridModels.get(i));
                }
                Log.d("newdata", "added");
                liveZoneGridViewRecyclerView.scrollToPosition(0);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }
}
