package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ChatMediaViewData;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private int selectedImage = 0;
    private ImageView imageView;
    private ArrayList<ChatMediaViewData> imageList;
    private Context context;

    public GalleryAdapter(ArrayList<ChatMediaViewData> imageList, Context context, ImageView imageView) {
        this.imageList = imageList;
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryAdapter.GalleryViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.gallery_row,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(imageList.get(position).getMediaData()));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_place_holder);
        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(uri).into(holder.galleryImageViewRow);
        holder.itemView.setOnClickListener(view -> {
            selectedImage = position;
            holder.itemView.setAlpha(0.5f);
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(uri).into(imageView);
        });

        if (position == 0) {
            holder.itemView.setAlpha(0.5f);
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(uri).into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public String getGallerySelected() {
        return imageList.get(selectedImage).getMediaData();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gallery_image_view_row)
        ImageView galleryImageViewRow;

        GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
