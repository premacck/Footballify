package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.helper.ItemTouchHelperInterface;

/**
 * Created by plank-niraj on 26-01-2018.
 */


public class ScrubberViewAdapter extends RecyclerView.Adapter<ScrubberViewAdapter.ScrubberViewHolder> implements ItemTouchHelperInterface {
    private Context context;

    // TODO: 26-01-2018 use server data.
    private ArrayList<String> data;

    public ScrubberViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ScrubberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scrubber_view_progress, parent, false);
        if (viewType == 2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_progress, parent, false);
        } else if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_half_time, parent, false);
        } else if (viewType == 3) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_goal, parent, false);
        } else if (viewType == 4) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrubber_view_pointer, parent, false);
        }
        return new ScrubberViewHolder(v);
    }


    @Override
    public int getItemViewType(int position) {
        // TODO: 26-01-2018 get from modal.(from server)
        if (data.get(position).equals("goal"))
            return 3;
        else if (data.get(position).equals("cursor"))
            return 4;
        else
            return data.get(position).equals("half") ? 1 : 2;
    }


    @Override
    public void onBindViewHolder(final ScrubberViewHolder holder, final int position) {
        holder.view.setOnClickListener(v -> {
            //TODO: Remove hardcoded code
            String text = "Play in progress.";
            if (data.get(position).equals("goal"))
                text = " Goal!!!! ";
            else if (data.get(position).equals("half"))
                text = "half time.";
            new SimpleTooltip.Builder(context)
                    .anchorView(v)
                    .text(text)
                    .gravity(Gravity.TOP)
                    .build()
                    .show();
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private SimpleTooltip simpleTooltip;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition, RecyclerView.ViewHolder target) {
        //TODO: Remove hardcoded code
        String text = "Play in progress.";
        if (data.get(toPosition).equals("goal"))
            text = " Goal!!!! ";
        else if (data.get(toPosition).equals("half"))
            text = "half time.";
        if (simpleTooltip == null) {
            simpleTooltip = new SimpleTooltip.Builder(context)
                    .anchorView(target.itemView)
                    .text(text)
                    .gravity(Gravity.TOP).build();
            simpleTooltip.show();
        } else {
            simpleTooltip.dismiss();
            simpleTooltip = new SimpleTooltip.Builder(context)
                    .anchorView(target.itemView)
                    .text(text)
                    .gravity(Gravity.TOP).build();
            simpleTooltip.show();
        }
        return false;
    }
    class ScrubberViewHolder extends RecyclerView.ViewHolder {
        View view;

        ScrubberViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.scrubber_view);
        }
    }

}
