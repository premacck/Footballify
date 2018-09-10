package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.util.DataUtil;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class ScrubberLayout extends FrameLayout implements CustomViewListener {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.scrubber)
    LineChart scrubber;
    @BindView(R.id.scrubber_expand_collapse)
    ImageButton scrubberExpandCollapse;

    private boolean isExpanded;
    private ScrubberLayoutListener listener;

    public ScrubberLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScrubberLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrubberLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_scrubber, this);
        ButterKnife.bind(this, rootView);
        isExpanded = false;
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
        scrubber.setVisibility(isLoading ? GONE : VISIBLE);
        scrubberExpandCollapse.setVisibility(isLoading ? GONE : VISIBLE);
    }

    /**
     * TODO : add {@code List<ScrubberData> scrubberDataList} as parameter once backend confirms API.
     */
    public void prepare() {
        DataUtil.ScrubberLoader.prepare(scrubber, false);
    }

    @OnClick(R.id.scrubber)
    public void onScrubberClick() {
        listener.onScrubberClick(scrubber);
    }

    @OnClick(R.id.scrubber_expand_collapse)
    public void onScrubberExpandCollapseClick() {
        isExpanded = !isExpanded;
        scrubberExpandCollapse.setImageResource(isExpanded ?
                R.drawable.ic_collapse :
                R.drawable.ic_expand
        );
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) scrubber.getLayoutParams();
        params.height = (int) getDp(getContext(), isExpanded ? 250 : 70);
        scrubber.setLayoutParams(params);
    }

    @Override
    public void initListeners(Fragment fragment) {
        if (fragment instanceof ScrubberLayoutListener) {
            listener = (ScrubberLayoutListener) fragment;
        } else {
            throw new IllegalStateException("Fragment must implement ScrubberLayoutListener");
        }
    }

    @Override
    public void initListeners(Activity activity) {
        if (activity instanceof ScrubberLayoutListener) {
            listener = (ScrubberLayoutListener) activity;
        } else {
            throw new IllegalStateException("Activity must implement ScrubberLayoutListener");
        }
    }

    @Override
    public void dispose() {
        listener = null;
    }

    public interface ScrubberLayoutListener {
        void onScrubberClick(View view);
    }
}