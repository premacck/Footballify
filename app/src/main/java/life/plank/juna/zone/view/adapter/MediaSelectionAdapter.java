package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 2/26/2018.
 */

public class MediaSelectionAdapter extends RecyclerView.Adapter<MediaSelectionAdapter.MediaSelectionViewHolder> {

    private Context context;

    public MediaSelectionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MediaSelectionAdapter.MediaSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.item_livezone_grid, parent, false);
        return new MediaSelectionAdapter.MediaSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaSelectionViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MediaSelectionViewHolder extends RecyclerView.ViewHolder {

        public MediaSelectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
