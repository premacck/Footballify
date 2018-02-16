package life.plank.juna.zone.view.adapter;

import android.content.Context;
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
import life.plank.juna.zone.data.network.model.LiveFeedTileData;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.helper.ItemTouchHelperInterface;
import life.plank.juna.zone.util.helper.ScrubberEvent;

/**
 * Created by plank-niraj on 26-01-2018.
 */
public class ScrubberViewAdapter extends RecyclerView.Adapter<ScrubberViewAdapter.ScrubberViewHolder> implements ItemTouchHelperInterface {
    ScrubberPointerUpdate scrubberPointerUpdate;
    boolean trigger = false;
    //Temp data
    HashMap<Integer, String> detailedData;
    ScrubberEvent scrubberEvent;
    HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;
    ArrayList<Integer> data;
    private Context context;
    // TODO: 26-01-2018 use server data.
    private SimpleTooltip simpleTooltip;
    private ItemTouchHelper itemTouchHelper;

    public ScrubberViewAdapter(Context context, ArrayList<Integer> data, HashMap<Integer, ScrubberViewData> scrubberViewDataHolder, ScrubberPointerUpdate scrubberPointerUpdate, ScrubberEvent scrubberEvent) {
        this.context = context;
        this.scrubberViewDataHolder = scrubberViewDataHolder;
        this.scrubberPointerUpdate = scrubberPointerUpdate;
        detailedData = new HashMap<>();
        this.scrubberEvent = scrubberEvent;
        this.data = data;
        addHardCodedData();
    }

    private void addHardCodedData() {
        ArrayList<Integer> tileImages = new ArrayList<>();

        tileImages.add(R.drawable.image4);
        tileImages.add(R.drawable.ic_grid_one);

        scrubberViewDataHolder.put(7, new ScrubberViewData("Goal!! Eden Hazard - 7",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages)));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image5);
        scrubberViewDataHolder.put(12, new ScrubberViewData("Goal! Rudiger - 12",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages)));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image6);
        tileImages.add(R.drawable.ic_grid_two);
        // tileImages.add(R.drawable.ic_grid_three);
        scrubberViewDataHolder.put(60 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Goal!! Granit - 60",
                ScrubberConstants.getScrubberViewGoal(), new LiveFeedTileData(tileImages)));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(17, new ScrubberViewData("Yellow card Hazard - 17",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages)));

        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(31, new ScrubberViewData("Yellow card Wilshere - 31",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages)));


        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image17);
        scrubberViewDataHolder.put(46, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();

        scrubberViewDataHolder.put(47, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.ic_grid_five);

        scrubberViewDataHolder.put(48, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();

        scrubberViewDataHolder.put(49, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.ic_grid_six);

        scrubberViewDataHolder.put(50, new ScrubberViewData("Half Time",
                ScrubberConstants.getScrubberViewHalfTime(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card);
        scrubberViewDataHolder.put(62 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Victor -62",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.yellow_card2);
        scrubberViewDataHolder.put(67 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Yellow card Nacho -66",
                ScrubberConstants.getScrubberViewCards(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(30, new ScrubberViewData("Willian - OUT Barkley - IN  -30",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(65 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Pedro - OUT Batshuayi - IN - 65",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        scrubberViewDataHolder.put(72 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Moses - OUT Zappacosta - IN - 72",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages)));
        tileImages = new ArrayList<>();
        tileImages.add(R.drawable.image18);
        tileImages.add(R.drawable.meme3);
        scrubberViewDataHolder.put(84 + ScrubberConstants.getScrubberViewHalfTimeWindow(), new ScrubberViewData("Lacazette - IN Kolasinac -IN - 84",
                ScrubberConstants.getScrubberViewSubstitute(), new LiveFeedTileData(tileImages)));

    }

    @Override
    public ScrubberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scrubber_view_progress, parent, false);

        if (viewType == ScrubberConstants.getScrubberViewHalfTime()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_half_time, parent, false);
        } else if (viewType == ScrubberConstants.getScrubberViewGoal()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_goal, parent, false);
        } else if (viewType == 4) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_pointer, parent, false);
        } else if (viewType == ScrubberConstants.getScrubberViewCards()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_cards, parent, false);
        } else if (viewType == ScrubberConstants.getScrubberViewSubstitute()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_substitions, parent, false);
        } else if (viewType == ScrubberConstants.getScrubberPostMatch()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_post_match, parent, false);
        }
        return new ScrubberViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {

        return data.get(position);

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
        if (position == data.size() - 1 && scrubberViewDataHolder.containsKey(position)) {

            displayTooltip(holder.view, scrubberViewDataHolder.get(position).getMessage());
            scrubberEvent.onNewEvent(scrubberViewDataHolder.get(position));
        }

    }

    /**
     * @param view:  Display on top of view
     * @param status : String to display
     */
    private void displayTooltip(View view, String status) {
        simpleTooltip = new SimpleTooltip.Builder(context)
                .anchorView(view)
                .text(status)
                .backgroundColor(ContextCompat.getColor(context, R.color.orange))
                .arrowColor(ContextCompat.getColor(context, R.color.orange))
                .highlightShape(R.drawable.shadow_tooltip)
                .transparentOverlay(true)
                .gravity(Gravity.TOP)
                .build();
        simpleTooltip.show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, RecyclerView.ViewHolder target) {
        //TODO: Remove hardcoded code
        if (simpleTooltip != null)
            simpleTooltip.dismiss();
        if (scrubberViewDataHolder.containsKey(target.getLayoutPosition())) {
            displayTooltip(target.itemView, scrubberViewDataHolder.get(target.getLayoutPosition()).getMessage());
            scrubberEvent.onNewEvent(scrubberViewDataHolder.get(target.getLayoutPosition()));
        }
        //Pointer movement disabled
        //scrubberPointerUpdate.moveScrubberPointer(target.itemView, -1);
        return false;
    }

    @Override
    public void clearView(int adapterPosition) {
        scrubberPointerUpdate.moveScrubberPointer(null, -1);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    public interface ScrubberPointerUpdate {
        void moveScrubberPointer(View view, int position);
    }

    class ScrubberViewHolder extends RecyclerView.ViewHolder {
        View view;

        ScrubberViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.scrubber_view);
        }
    }
}
