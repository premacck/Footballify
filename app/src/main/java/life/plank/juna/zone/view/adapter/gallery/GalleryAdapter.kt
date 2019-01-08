package life.plank.juna.zone.view.adapter.gallery

import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_gallery.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.view.activity.camera.*
import java.util.*

class GalleryAdapter(private val activity: CustomCameraActivity, private var isForImage: Boolean) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    private var galleryItems: MutableList<String> = ArrayList()

    companion object {
        private val TAG = GalleryAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): GalleryViewHolder =
            GalleryViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_gallery, viewGroup, false))

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        try {
            holder.itemView.run {
                val itemPath = galleryItems[position]
                Glide.with(activity).load(itemPath).into(gallery_item_thumbnail)

                if (isForImage) video_play_icon.makeGone() else video_play_icon.makeVisible()

                onDebouncingClick {
                    if (activity.boardId.isEmpty()) {
                        activity.setResult(itemPath)
                    } else {
                        UploadActivity.launch(activity, if (isForImage) IMAGE else VIDEO, activity.boardId, itemPath)
                        activity.finish()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder(): ", e)
        }
    }

    override fun getItemCount(): Int = galleryItems.size

    fun update(galleryItems: MutableList<String>, isForImage: Boolean) {
        this.isForImage = isForImage
        this.galleryItems.clear()
        this.galleryItems.addAll(galleryItems)
        notifyDataSetChanged()
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}