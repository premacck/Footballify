package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.slider.library.SliderLayout;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.view.activity.LiveZoneSliderView;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;

public class LiveZoneFragment extends Fragment {

    @BindView(R.id.liveZoneTextView)
    TextView liveZoneTextView;
    @BindView(R.id.liveZoneRelativeLayout)
    RelativeLayout liveZoneRelativeLayout;
    @BindView(R.id.liveZoneGridViewRelativeLayout)
    RecyclerView liveZoneGridViewRelativeLayout;
    @BindView(R.id.closeImage)
    ImageView closeImage;
    @BindView(R.id.liveZoneSlider)
    Context context;
    SliderLayout liveZoneSlider;
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
        setUpGridView();
        setUpSlider();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setUpGridView() {
        SnapHelper snapHelper = new StartSnapHelper();
        liveZoneGridViewRelativeLayout.setLayoutManager(new GridLayoutManager(getActivity(), calculateNoOfColumns(), GridLayoutManager.HORIZONTAL, false));
        liveZoneGridViewRelativeLayout.setAdapter(new LiveZoneGridAdapter(getActivity()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewRelativeLayout.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        snapHelper.attachToRecyclerView(liveZoneGridViewRelativeLayout);
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


    private void setUpSlider() {
        liveZoneSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        liveZoneSlider.removeAllSliders();
        ArrayList<String> sliderData = new ArrayList<>();
        sliderData.add("");
        sliderData.add("");
        sliderData.add("");


        if (sliderData.size() > 0) {
            for (String data : sliderData) {
                LiveZoneSliderView textSliderView = new LiveZoneSliderView(getActivity(), data);

                liveZoneSlider.addSlider(textSliderView);
            }
        }
    }
}
