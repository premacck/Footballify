package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ChatMediaViewData;
import life.plank.juna.zone.util.AppConstants;

/**
 * Created by plank-niraj on 07-03-2018.
 */

public class MediaSelectionAdapter extends RecyclerView.Adapter<MediaSelectionAdapter.MediaSelectionViewHolder> {

    private ArrayList<ChatMediaViewData> chatMediaViewData;
    private Context context;

    public MediaSelectionAdapter(Context context, ArrayList<ChatMediaViewData> chatMediaViewData) {
        this.context = context;
        this.chatMediaViewData = chatMediaViewData;
    }

    @Override
    public MediaSelectionAdapter.MediaSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.media_selection_row, parent, false);
        return new MediaSelectionAdapter.MediaSelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaSelectionViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return chatMediaViewData.size();
    }

    class MediaSelectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photos_image_view)
        ImageView photosImageView;
        @BindView(R.id.select_item)
        ImageView selectImageView;

        MediaSelectionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}