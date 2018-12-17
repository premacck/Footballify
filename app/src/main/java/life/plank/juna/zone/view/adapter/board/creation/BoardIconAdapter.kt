package life.plank.juna.zone.view.adapter.board.creation

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_board_icon.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.view.Image
import java.io.File
import java.util.*

class BoardIconAdapter : RecyclerView.Adapter<BoardIconAdapter.BoardIconViewHolder>() {

    var selectedIndex = -1
    var boardIconList: MutableList<String> = ArrayList()

    val selectedPath: String?
        get() = if (selectedIndex >= 0) boardIconList[selectedIndex] else null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardIconViewHolder =
            BoardIconViewHolder(from(parent.context).inflate(R.layout.item_board_icon, parent, false))

    override fun onBindViewHolder(holder: BoardIconViewHolder, position: Int) {
        try {
            val imgFile = File(boardIconList[position])
            if (imgFile.exists()) {
                val bitmap = Image().compress(imgFile, boardIconList[position])
                val imageDrawable = BitmapDrawable(ZoneApplication.getContext().resources, bitmap)
                holder.itemView.icon_image_view.setImageDrawable(imageDrawable)
            }
        } catch (e: Exception) {
            Log.e("TAG", "CAMERA_IMAGE_RESULT : $e")
            Toast.makeText(ZoneApplication.getContext(), R.string.could_not_process_image, Toast.LENGTH_LONG).show()
        }

        holder.itemView.image_selection_marker.visibility = if (selectedIndex == position) View.VISIBLE else View.INVISIBLE

        holder.itemView.onDebouncingClick {
            val previousSelection = selectedIndex
            holder.itemView.image_selection_marker.visibility = if (selectedIndex == position) View.INVISIBLE else View.VISIBLE
            selectedIndex = position
            notifyItemChanged(previousSelection)
            notifyItemChanged(selectedIndex)
        }
    }

    override fun getItemCount(): Int = boardIconList.size

    class BoardIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}