package life.plank.juna.zone.view.adapter;

import android.content.Context;
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
import life.plank.juna.zone.util.GlobalVariable;
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
    private Context context;
    // TODO: 26-01-2018 use server data.
    private ArrayList<String> data;
    private SimpleTooltip simpleTooltip;
    private ItemTouchHelper itemTouchHelper;

    public ScrubberViewAdapter(Context context, ArrayList<String> data, ScrubberPointerUpdate scrubberPointerUpdate, ScrubberEvent scrubberEvent) {
        this.context = context;
        this.data = data;
        this.scrubberPointerUpdate = scrubberPointerUpdate;
        detailedData = new HashMap<>();
        this.scrubberEvent = scrubberEvent;
        addHardCodedData();
    }

    private void addHardCodedData() {

        detailedData.put(7 + GlobalVariable.getScrubberPreMatch(), "Goal!! Eden Hazard - 7");
        detailedData.put(12 + GlobalVariable.getScrubberPreMatch(), "Goal! Rudiger - 12");
        detailedData.put(60 + GlobalVariable.getScrubberPreMatch() + GlobalVariable.getHalfDuration(), "Goal!! Granit - 60");

        detailedData.put(17 + GlobalVariable.getScrubberPreMatch(), "Yellow card Hazard - 17");
        detailedData.put(31 + GlobalVariable.getScrubberPreMatch(), "Yellow card Wilshere - 31  ");
        detailedData.put(62 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch(), "Yellow card Victor -62");
        detailedData.put(66 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch(), "Yellow card Nacho -66");

        detailedData.put(30 + GlobalVariable.getScrubberPreMatch(), "Willian - OUT Barkley - IN  -30");
        detailedData.put(65 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch(), "Pedro - OUT Batshuayi - IN - 65");
        detailedData.put(72 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch(), "Pedro - OUT Batshuayi - IN - 72");

        detailedData.put(84 + GlobalVariable.getHalfDuration() + GlobalVariable.getScrubberPreMatch(), "Moses - OUT Zappacosta - IN - 84");
    }

    @Override
    public ScrubberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scrubber_view_progress, parent, false);
        if (viewType == GlobalVariable.getScrubberViewHalfTime()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_half_time, parent, false);
        } else if (viewType == GlobalVariable.getScrubberViewGoal()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_goal, parent, false);
        } else if (viewType == 4) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_pointer, parent, false);
        } else if (viewType == GlobalVariable.getScrubberViewCards()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_cards, parent, false);
        } else if (viewType == GlobalVariable.getScrubberViewSubstitute()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_substitions, parent, false);
        } else if (viewType == GlobalVariable.getScrubberPostMatch()) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_post_match, parent, false);
        }
        return new ScrubberViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        // TODO: 26-01-2018 get from modal.(from server)
        // TODO: 06-02-2018 check performance of view updation
        if (data.get(position).equals(GlobalVariable.getInstance().getScrubberGoal()))
            return GlobalVariable.getScrubberViewGoal();
        else if (data.get(position).equals(GlobalVariable.getInstance().getScrubberCursor()))
            return GlobalVariable.getScrubberViewCursor();
        else if (data.get(position).equals(GlobalVariable.getInstance().getScrubberHalf()))
            return GlobalVariable.getScrubberViewHalfTime();
        else if (data.get(position).equals(GlobalVariable.getInstance().getScrubberSubstitute()))
            return GlobalVariable.getScrubberViewSubstitute();
        else if (data.get(position).equals(GlobalVariable.getInstance().getScrubberCard()))
            return GlobalVariable.getScrubberViewCards();
        else if (data.get(position).equals(GlobalVariable.getInstance().getScrubberPost()))
            return GlobalVariable.getScrubberPostMatch();
        return GlobalVariable.getScrubberViewProgress();
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
        if (position == data.size() - 1 && detailedData.containsKey(position)) {
            displayTooltip(holder.view, detailedData.get(position));
            scrubberEvent.onNewEvent(1, position);
        }

        // TODO: 06-02-2018 Fix the issue.
        /*holder.view.setOnClickListener(v -> {
            //TODO: Remove hardcoded code
            String status;
            if (!detailedData.containsKey(position))
                status = "inProgress";
            else
                status = detailedData.get(position);
            displayTooltip(v, status);
        });*/
    }

    /**
     * @param view:  Display on top of view
     * @param status : String to display
     */
    private void displayTooltip(View view, String status) {
        simpleTooltip = new SimpleTooltip.Builder(context)
                .anchorView(view)
                .text(status)
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
        if (detailedData.containsKey(target.getLayoutPosition())) {
            displayTooltip(target.itemView, detailedData.get(target.getLayoutPosition()));
        }
        scrubberPointerUpdate.moveScrubberPointer(target.itemView, -1);
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
