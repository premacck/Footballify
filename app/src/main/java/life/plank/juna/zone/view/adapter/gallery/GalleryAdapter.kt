package life.plank.juna.zone.view.adapter.gallery

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.prembros.facilis.util.onDebouncingClick
import kotlinx.android.synthetic.main.item_gallery.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.view.activity.camera.*
import life.plank.juna.zone.view.cardmaker.CreateCardActivity
import java.util.*

class GalleryAdapter(private val activity: CustomCameraActivity, private val glide: RequestManager) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private var galleryItems: List<String> = ArrayList()

    companion object {
        private val TAG = GalleryAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): GalleryViewHolder =
            GalleryViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_gallery, viewGroup, false))

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        try {
            holder.itemView.run {
                val itemPath = galleryItems[position]
                glide.load(itemPath).into(gallery_item_thumbnail)

                onDebouncingClick {
                    if (activity.boardId.isEmpty()) {
                        CreateCardActivity.launch(activity, itemPath)
                    } else {
                        UploadActivity.launch(activity, if (activity.isForImage) IMAGE else VIDEO, activity.boardId, itemPath)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder(): ", e)
        }
    }

    override fun getItemCount(): Int = galleryItems.size

    fun update(galleryItems: List<String>) {
        this.galleryItems = galleryItems
        notifyDataSetChanged()
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}