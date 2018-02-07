package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.mikepenz.itemanimators.ScaleXAnimator;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.util.helper.ScrubberEvent;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.activity.LiveZoneSliderView;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;
import life.plank.juna.zone.viewmodel.LiveZoneGridModel;

public class LiveZoneFragment extends Fragment implements ScrubberEvent {

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
    ScrubberEvent scrubberEvent;
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
        //setUpBounceAnimation();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpGridView() {
        SnapHelper snapHelper = new StartSnapHelper();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.supportsPredictiveItemAnimations();
        liveZoneGridViewRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new LiveZoneGridAdapter(getActivity());
        liveZoneGridViewRecyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        snapHelper.attachToRecyclerView(liveZoneGridViewRecyclerView);
        liveZoneGridViewRecyclerView.setItemAnimator(new ScaleXAnimator());
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
            liveZoneGridViewRecyclerView.setAdapter(adapter);
        });
    }

    private void setUpSlider() {
        scrubberEvent = this;
        liveZoneSlider.stopAutoCycle();
        ArrayList<String> sliderData = new ArrayList<>();
        sliderData.add("text");
        sliderData.add("video");
        sliderData.add("text");
        sliderData.add("video");

        if (sliderData.size() > 0) {
            for (String data : sliderData) {
                LiveZoneSliderView textSliderView = new LiveZoneSliderView(getActivity(), data, scrubberEvent);
                liveZoneSlider.addSlider(textSliderView);
            }
        }
    }

    /**
     * @param status   : Status
     * @param position : position
     */
    @Override
    public void onNewEvent(int status, int position) {
        // TODO: 06-02-2018 Animate
    }

    private void setUpAnimation() {
        Handler handler = new Handler();
        int delay = 10000;
        ArrayList<LiveZoneGridModel> liveZoneGridModels = new ArrayList<>();
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_one));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_six));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_five));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_two));
        liveZoneGridModels.add(new LiveZoneGridModel("image", R.drawable.ic_grid_three));
        handler.postDelayed(new Runnable() {
            public void run() {
                int position = ((GridLayoutManager) liveZoneGridViewRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                for (int i = 0; i <= liveZoneGridModels.size() - 1; i++) {
                    adapter.addData(position + i, liveZoneGridModels.get((new Random()).nextInt(liveZoneGridModels.size())));
                }
                liveZoneGridViewRecyclerView.scrollToPosition(0);
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void setUpBounceAnimation() {
        Handler handler = new Handler();
        int delay = 10000;
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                liveZoneGridViewRecyclerView.getChildAt(new Random().nextInt(20)).startAnimation(animation);
                handler.postDelayed(this, delay);
            }
        }, delay);

    }


}
