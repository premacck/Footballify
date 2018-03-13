package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.interfaces.ScrubberPointerUpdate;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.helper.ItemTouchHelperInterface;

/**
 * Created by plank-niraj on 26-01-2018.
 */
public class ScrubberViewAdapter extends RecyclerView.Adapter<ScrubberViewAdapter.ScrubberViewHolder> implements ItemTouchHelperInterface {
    public boolean trigger = false;
    ScrubberPointerUpdate scrubberPointerUpdate;
    //Temp data
    HashMap<Integer, String> detailedData;
    HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    ArrayList<Integer> scrubberProgressData;
    int matchNumber;
    private Context context;
    // TODO: 26-01-2018 use server data.
    private SimpleTooltip simpleTooltip;
    private ItemTouchHelper itemTouchHelper;

    public ScrubberViewAdapter(Context context,
                               ArrayList<Integer> data,
                               HashMap<Integer, ScrubberViewData> scrubberViewDataHolder,
                               ScrubberPointerUpdate scrubberPointerUpdate, int matchNumber) {
        this.context = context;
        this.scrubberViewDataHolder = scrubberViewDataHolder;
        this.scrubberPointerUpdate = scrubberPointerUpdate;
        detailedData = new HashMap<>();
        this.scrubberProgressData = data;
        this.matchNumber = matchNumber;
        addHardCodedData();
    }

    private void addHardCodedData() {
        ScrubberConstants.getHighLightsMatchOne(scrubberViewDataHolder);
    }

    @Override
    public ScrubberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case ScrubberConstants.SCRUBBER_VIEW_HALF_TIME:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_half_time, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_GOAL:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_goal, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_CURSOR:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_pointer, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_CARDS:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_cards, parent, false));

            case ScrubberConstants.SCRUBBER_VIEW_SUBSTITUTE:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_substitions, parent, false));

            case ScrubberConstants.SCRUBBER_POST_MATCH:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_post_match, parent, false));

            default:
                return new ScrubberViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.scrubber_view_events_progress, parent, false));

        }
    }

    @Override
    public int getItemViewType(int position) {
        return scrubberProgressData.get(position);
    }

    @Override
    public void onBindViewHolder(final ScrubberViewHolder holder, final int position) {

        //To start the drag on touch and swipe.
        holder.view.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder);
            }
            return false;
        });

        if (simpleTooltip != null) {
            simpleTooltip.dismiss();
        }

        if (position == scrubberProgressData.size() - 1 && scrubberViewDataHolder.containsKey(position) &&
                scrubberViewDataHolder.get(position).isTriggerEvents()) {
            displayTooltip(holder.view, scrubberViewDataHolder.get(position).getMessage());
        }

        holder.view.setOnClickListener(itemView -> {
            if (scrubberViewDataHolder.containsKey(position) && scrubberViewDataHolder.get(position).isTriggerEvents()) {
                displayTooltip(itemView, scrubberViewDataHolder.get(position).getMessage());
            }
        });
        if (scrubberViewDataHolder.containsKey(position) && scrubberViewDataHolder.get(position).isTriggerEvents()) {
            scrubberPointerUpdate.updateRecentEvents(position);
        }
    }

    /**
     * @param view:  Display on top of view
     * @param status : String to display
     */
    private void displayTooltip(View view, String status) {
        SimpleTooltip simpleTooltip = new SimpleTooltip.Builder(context)
                .anchorView(view)
                .text(status)
                .backgroundColor(ContextCompat.getColor(context, R.color.orange))
                .arrowColor(ContextCompat.getColor(context, R.color.orange))
                .highlightShape(R.drawable.shadow_tooltip)
                .transparentOverlay(true)
                .gravity(Gravity.TOP)
                .build();
        simpleTooltip.show();
        final Handler handler = new Handler();
        handler.postDelayed(simpleTooltip::dismiss, 2000);
    }

    @Override
    public int getItemCount() {
        return scrubberProgressData.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, RecyclerView.ViewHolder target) {
        //TODO: Remove hardcoded code
        trigger = true;
        if (simpleTooltip != null)
            simpleTooltip.dismiss();
        if (scrubberViewDataHolder.containsKey(target.getLayoutPosition()) &&
                scrubberViewDataHolder.get(target.getLayoutPosition()).isTriggerEvents()) {
            displayTooltip(target.itemView, scrubberViewDataHolder.get(target.getLayoutPosition()).getMessage());
        }
        return false;
    }

    @Override
    public void clearView(int adapterPosition) {
        trigger = false;
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    class ScrubberViewHolder extends RecyclerView.ViewHolder {
        View view;

        ScrubberViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.scrubber_view);
        }
    }
}