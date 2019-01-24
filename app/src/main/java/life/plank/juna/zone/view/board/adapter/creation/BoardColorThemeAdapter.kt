package life.plank.juna.zone.view.board.adapter.creation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.prembros.facilis.util.onDebouncingClick
import kotlinx.android.synthetic.main.item_board_color_theme.view.*
import life.plank.juna.zone.R
import java.util.*

class BoardColorThemeAdapter : RecyclerView.Adapter<BoardColorThemeAdapter.BoardColorThemeViewHolder>() {

    private val boardColorList = ArrayList(Arrays.asList(
            "#FFB900", "#FF8C00", "#F7630C", "#CA5010", "#DA3B01", "#EF6950",
            "#D13438", "#FF4343", "#E74856", "#E81123", "#EA005E", "#C30052",
            "#E3008C", "#BF0077", "#C239B3", "#9A0089", "#0078D7", "#0063B1",
            "#8E8CD8", "#6B69D6", "#8764B8", "#744DA9", "#B146C2", "#881798",
            "#0099BC", "#2D7D9A", "#00B7C3", "#038387", "#00B294", "#018574",
            "#00CC6A", "#10893E", "#7A7574", "#5D5A58", "#68768A", "#515C6B",
            "#567C73", "#486860", "#498205", "#107C10", "#767676", "#4C4A48",
            "#69797E", "#4A5459", "#647C64", "#525E54", "#847545", "#7E735F"
    ))

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