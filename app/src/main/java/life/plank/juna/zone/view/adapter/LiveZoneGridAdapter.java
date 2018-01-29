package life.plank.juna.zone.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 1/27/2018.
 */

public class LiveZoneGridAdapter extends RecyclerView.Adapter<LiveZoneGridAdapter.LiveZoneGridViewHolder> {

    private Context context;
    private List<String> elements;

    public LiveZoneGridAdapter(Context context) {
        this.context = context;
        this.elements = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            this.elements.add("");
        }
    }

    public static class LiveZoneGridViewHolder extends RecyclerView.ViewHolder {


        public LiveZoneGridViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public LiveZoneGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.item_livezone_grid, parent, false);
        return new LiveZoneGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveZoneGridViewHolder holder, final int position) {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.elements.size();
    }

}
