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

public class LivezoneGridAdapter extends RecyclerView.Adapter<LivezoneGridAdapter.SimpleViewHolder>{

    private Context context;
    private List<String> elements;

    public LivezoneGridAdapter(Context context){
        this.context = context;
        this.elements = new ArrayList<String>();
        for(int i = 0; i < 40 ; i++){
            this.elements.add("");
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {


        public SimpleViewHolder(View view) {
            super(view);

        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.item_livezone_grid, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {

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
