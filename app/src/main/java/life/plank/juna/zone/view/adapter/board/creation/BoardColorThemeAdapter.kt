package life.plank.juna.zone.view.adapter.board.creation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_board_color_theme.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.view.BoardColorFactory

class BoardColorThemeAdapter : RecyclerView.Adapter<BoardColorThemeAdapter.BoardColorThemeViewHolder>() {

    private val boardColorList: List<String> = BoardColorFactory.getAllColors()

    val selectedColor: String?
        get() = if (selectedIndex >= 0) boardColorList[selectedIndex] else null

    companion object {
        private var selectedIndex = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardColorThemeViewHolder =
            BoardColorThemeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_board_color_theme, parent, false))

    override fun onBindViewHolder(holder: BoardColorThemeViewHolder, position: Int) {
        holder.itemView.run {
            color_theme_image_view.setImageDrawable(ColorDrawable(Color.parseColor(boardColorList[position])))
            image_selection_marker.visibility = if (selectedIndex == position) View.VISIBLE else View.INVISIBLE

            onDebouncingClick {
                val previousSelection = selectedIndex
                image_selection_marker.visibility = if (selectedIndex == position) View.INVISIBLE else View.VISIBLE
                selectedIndex = position
                notifyItemChanged(previousSelection)
                notifyItemChanged(selectedIndex)
            }
        }
    }

    override fun getItemCount(): Int = boardColorList.size

    class BoardColorThemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}