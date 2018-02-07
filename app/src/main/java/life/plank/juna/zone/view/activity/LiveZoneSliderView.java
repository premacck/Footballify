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

import life.plank.juna.zone.R;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.ItemTouchHelperCallback;
import life.plank.juna.zone.util.helper.ScrubberEvent;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

/**
 * Created by plank-hasan on 1/31/2018.
 */

public class LiveZoneSliderView extends BaseSliderView implements ScrubberViewAdapter.ScrubberPointerUpdate {

    final ArrayList<String> data = new ArrayList<>();
    RecyclerView scrubberView;
    ScrubberViewAdapter adapter;
    ImageView pointer;
    View view;
    View viewBottom;
    int screenWidth;
    ScrubberEvent scrubberEvent;
    private String liveZoneSliderData;
    private Context context;


    public LiveZoneSliderView(Context context, String data, ScrubberEvent scrubberEvent) {
        super(context);
        this.liveZoneSliderData = data;
        this.context = context;
        this.scrubberEvent = scrubberEvent;
        screenWidth = UIDisplayUtil.getDisplayMetricsData(context,
                GlobalVariable.getInstance().getDisplayWidth());
    }

    @Override
    public View getView() {
        View v;
        if (liveZoneSliderData.contentEquals("text")) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row, null);
        } else {
            v = LayoutInflater.from(getContext()).inflate(R.layout.live_zone_slider_row_one, null);
            scrubberView = v.findViewById(R.id.scrubber_recycler_view);
            scrubberView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            pointer = v.findViewById(R.id.arrow);
            adapter = new ScrubberViewAdapter(context, data, this, scrubberEvent);
            viewBottom = v.findViewById(R.id.view_after);
            scrubberView.setAdapter(adapter);
            setUpScrubber();
        }
        bindEventAndShow(v, null);
        return v;
    }

    private void setUpScrubber() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(adapter, data);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scrubberView);
        adapter.setItemTouchHelper(touchHelper);

        new Thread(() -> {
            int progressStatus = 0;
            data.add("cursor");
            // TODO: 26-01-2018 decide based on device width
            while (progressStatus < GlobalVariable.getInstance().getTotalWindow()) {
                // Update the progress status
                progressStatus += 1;

                // TODO: 06-02-2018 Remove this, Hardcoded value based on previous match
                try {
                    data.remove(data.size() - 1);

                    if (progressStatus < GlobalVariable.getScrubberPreMatch() &&
                            ((progressStatus) % 10) == 0) {
                        data.add(GlobalVariable.getInstance().getScrubberPost());
                    }

                    if (progressStatus > (GlobalVariable.getScrubberPreMatch() - GlobalVariable.getScrubberPostMatch()) &&
                            (progressStatus + 1 % 10) == 0) {
                        data.add(GlobalVariable.getInstance().getScrubberPost());
                    }

                    if (progressStatus == 45 + GlobalVariable.getScrubberPreMatch()
                            || progressStatus == 46 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 47 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 48 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 49 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 50 + GlobalVariable.getScrubberPreMatch())
                        data.add(GlobalVariable.getInstance().getScrubberHalf());
                    else if (progressStatus == 7 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 12 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 60 + GlobalVariable.getHalfDuration())
                        data.add(GlobalVariable.getInstance().getScrubberGoal());
                    else if (progressStatus == 17 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == 31 + GlobalVariable.getScrubberPreMatch()
                            || progressStatus == (62 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch()) ||
                            progressStatus == (66 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch()))
                        data.add(GlobalVariable.getInstance().getScrubberCard());
                    else if (progressStatus == 30 + GlobalVariable.getScrubberPreMatch() ||
                            progressStatus == (65 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch()) ||
                            progressStatus == (72 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch()) ||
                            progressStatus == (84 + GlobalVariable.getHalfDuration()) + GlobalVariable.getScrubberPreMatch())
                        data.add(GlobalVariable.getInstance().getScrubberSubstitute());
                    else
                        data.add(GlobalVariable.getInstance().getScrubberNormal());

                    data.add(GlobalVariable.getInstance().getScrubberCursor());
                    Thread.sleep(500);
                    moveScrubberPointer(null, data.size() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(() -> adapter.notifyDataSetChanged());
            }
        }).start(); // Start the operation
    }


    @Override
    public void moveScrubberPointer(View view, int position) {
        int[] xyViewAfter = new int[]{0, 0};
        scrubberView.getLocationOnScreen(xyViewAfter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                50,
                50
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
            params.leftMargin = xyData[0];
            view.getLocationInWindow(xyData);
            params.leftMargin = xyData[0] - 10;
            new Handler(Looper.getMainLooper()).post(() -> pointer.setLayoutParams(params));
        }
    }

}
