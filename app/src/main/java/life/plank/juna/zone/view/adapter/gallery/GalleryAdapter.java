package life.plank.juna.zone.view.adapter.gallery;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity;
import life.plank.juna.zone.view.activity.camera.UploadActivity;

import static life.plank.juna.zone.util.common.AppConstants.IMAGE;
import static life.plank.juna.zone.util.common.AppConstants.VIDEO;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();

    private CustomCameraActivity activity;
    private List<String> galleryItems;
    private RequestManager glide;

    public GalleryAdapter(CustomCameraActivity activity, RequestManager glide) {
        this.activity = activity;
        this.glide = glide;
        galleryItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new GalleryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        try {
            holder.itemPath = galleryItems.get(position);
            glide.load(holder.itemPath).into(holder.galleryItemThumbnail);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder(): ", e);
        }
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public void update(List<String> galleryItems) {
        this.galleryItems = galleryItems;
        notifyDataSetChanged();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gallery_item_thumbnail)
        ImageView galleryItemThumbnail;

        private String itemPath;
        private final WeakReference<GalleryAdapter> ref;

        GalleryViewHolder(@NonNull View itemView, GalleryAdapter galleryAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ref = new WeakReference<>(galleryAdapter);
        }

        @OnClick(R.id.gallery_item_thumbnail)
        public void onGalleryImageSelected() {
            UploadActivity.launch(ref.get().activity, ref.get().activity.isForImage() ? IMAGE : VIDEO, ref.get().activity.boardId, itemPath);
        }
    }
}