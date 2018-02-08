package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.ItemTouchHelperCallback;
import life.plank.juna.zone.util.helper.ScrubberEvent;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

/**
 * Created by plank-hasan on 1/31/2018.
 */

public class LiveZoneSliderView extends BaseSliderView implements ScrubberViewAdapter.ScrubberPointerUpdate {

    final ArrayList<Integer> data = new ArrayList<>();
    View view;
    int progressStatus = 0;
    @BindView(R.id.scrubber_recycler_view)
    RecyclerView scrubberView;
    @BindView(R.id.arrow)
    ImageView pointer;
    private ScrubberViewAdapter adapter;
    private int screenWidth;
    private ScrubberEvent scrubberEvent;
    private HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    private String liveZoneSliderData;
    private Context context;

    public LiveZoneSliderView(Context context, String data, ScrubberEvent scrubberEvent) {
        super(context);
        this.liveZoneSliderData = data;
        this.context = context;
        this.scrubberEvent = scrubberEvent;
        screenWidth = UIDisplayUtil.getDisplayMetricsData(context,
                GlobalVariable.getInstance().getDisplayWidth());
        scrubberViewDataHolder = new HashMap<>();
    }

    @Override
    public View getView() {


        View v = null;
        if (liveZoneSliderData.contentEquals("text")) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row, null);
            bindEventAndShow(v, null);
        } else {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row_one, null);
            ButterKnife.bind(this, v);
            scrubberView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            adapter = new ScrubberViewAdapter(context, data, scrubberViewDataHolder, this, scrubberEvent);
            scrubberView.setAdapter(adapter);
            setUpScrubber();
            bindEventAndShow(v, null);
        }
        return v;
    }

    private void setUpScrubber() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter, data);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scrubberView);
        adapter.setItemTouchHelper(touchHelper);
        new Thread(this::updateScrubber).start();
    }

    private void updateScrubber() {

        new Handler(Looper.getMainLooper()).post(() -> pointer.setVisibility(View.VISIBLE));
        while (progressStatus < ScrubberConstants.getScrubberViewTotalWindow()) {
            try {
                moveScrubberPointer(null, data.size() - 1);
                progressStatus++;
                if (data.size() > 0)
                    data.remove(data.size() - 1);
                if (scrubberViewDataHolder.containsKey(progressStatus)) {
                    data.add(scrubberViewDataHolder.get(progressStatus).getType());
                } else {
                    data.add(ScrubberConstants.getScrubberViewProgress());
                }
                data.add(ScrubberConstants.getScrubberViewCursor());
                Thread.sleep(2000);
                new Handler(Looper.getMainLooper()).post(() -> adapter.notifyItemChanged(data.size() - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void moveScrubberPointer(View view, int position) {
        int[] xyViewAfter = new int[]{0, 0};
        scrubberView.getLocationOnScreen(xyViewAfter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                25,
                25
        );
        params.addRule(RelativeLayout.BELOW, scrubberView.getId());
        if (position != -1 && scrubberView != null) {
            view = scrubberView.getLayoutManager().findViewByPosition(position);
        }
        if (view == null && position == -1) {
            view = scrubberView.getLayoutManager().findViewByPosition(data.size() - 1);
        }
        int[] xyData;
        if (view != null) {
            xyData = new int[]{0, 0};
            view.getLocationOnScreen(xyData);
            params.leftMargin = xyData[0] - 10;
            view.getLocationInWindow(xyData);
            new Handler(Looper.getMainLooper()).post(() -> pointer.setLayoutParams(params));
        }
    }
}
