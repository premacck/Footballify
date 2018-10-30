package life.plank.juna.zone.util.masonry.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class MasonryItemDecoration(private val gridSpacing: Int, private val gridSize: Int = 3) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - gridSpacing.toFloat() * (gridSize - 1)) / gridSize).toInt()
        val padding = parent.width / gridSize - frameWidth
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        when (itemPosition % 18) {
            0, 3, 6, 9, 11, 12, 15 -> {
                outRect.bottom = padding
                outRect.right = padding
                outRect.top = padding
            }
            1, 2, 5, 8, 10, 14, 17 -> {
                outRect.bottom = padding
                outRect.left = padding
                outRect.top = padding
            }
            4, 7, 13, 16 -> {
                outRect.bottom = padding
                outRect.right = padding
                outRect.top = padding
                outRect.left = padding
            }
        }
    }
}