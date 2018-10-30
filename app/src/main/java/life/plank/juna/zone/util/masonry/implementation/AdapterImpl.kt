package life.plank.juna.zone.util.masonry.implementation

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.support.v4.util.ArrayMap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.LayoutParams
import android.widget.AbsListView.LayoutParams.MATCH_PARENT
import android.widget.AbsListView.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import life.plank.juna.zone.R
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.masonry.base.AsymmetricBaseAdapter
import life.plank.juna.zone.util.masonry.base.AsymmetricItem
import life.plank.juna.zone.util.masonry.base.AsymmetricView
import life.plank.juna.zone.util.masonry.base.MasonryUtils
import life.plank.juna.zone.util.masonry.model.RowInfo
import life.plank.juna.zone.util.masonry.model.RowItem
import life.plank.juna.zone.util.masonry.pool.LinearLayoutPoolObjectFactory
import life.plank.juna.zone.util.masonry.pool.ObjectPool
import life.plank.juna.zone.util.masonry.widget.AsymmetricViewHolder
import org.jetbrains.anko.collections.forEach
import java.lang.ref.WeakReference
import java.util.*

@Suppress("UNCHECKED_CAST", "DEPRECATION")
class AdapterImpl<T : RecyclerView.ViewHolder>(
        private val context: Context,
        private val asymmetricAdapter: AsymmetricBaseAdapter<T>,
        private val listView: AsymmetricView
) : View.OnClickListener, View.OnLongClickListener {
    private val itemsPerRow = SparseArray<RowInfo>()
    private val linearLayoutPool: ObjectPool<LinearLayout> = ObjectPool(LinearLayoutPoolObjectFactory(context))
    private val viewHoldersMap = ArrayMap<Int, ObjectPool<AsymmetricViewHolder<T>>>()
    private val debugEnabled: Boolean = listView.isDebugging
    private var asyncTask: ProcessRowsTask<T>? = null

    val rowCount: Int
        get() = itemsPerRow.size()

    private fun calculateItemsForRow(items: List<RowItem>, initialSpaceLeft: Float = listView.numColumns.toFloat()): RowInfo {
        val itemsThatFit = ArrayList<RowItem>()
        var currentItem = 0
        var rowHeight = 1
        var areaLeft = initialSpaceLeft

        while (areaLeft > 0 && currentItem < items.size) {
            val item = items[currentItem++]
            val itemArea = (item.item.rowSpan * item.item.columnSpan).toFloat()

            if (debugEnabled) {
                Log.d(TAG, String.format(context.getString(R.string.row_item_info), item, rowHeight, itemArea))
            }

            if (rowHeight < item.item.rowSpan) {
                // restart with double height
                itemsThatFit.clear()
                rowHeight = item.item.rowSpan
                currentItem = 0
                areaLeft = initialSpaceLeft * item.item.rowSpan
            } else if (areaLeft >= itemArea) {
                areaLeft -= itemArea
                itemsThatFit.add(item)
            } else if (!listView.isAllowReordering) {
                break
            }
        }

        return RowInfo(rowHeight, itemsThatFit, areaLeft)
    }

    fun recalculateItemsPerRow() {
        if (asyncTask != null) {
            asyncTask!!.cancel(true)
        }

        linearLayoutPool.clear()
        itemsPerRow.clear()

        asyncTask = ProcessRowsTask(this)
        asyncTask!!.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR)
    }

    override fun onClick(v: View) {
        val rowItem = v.tag as ViewState<T>
        listView.fireOnItemClick(rowItem.rowItem.index, v)
    }

    override fun onLongClick(v: View): Boolean {
        val rowItem = v.tag as ViewState<T>
        return listView.fireOnItemLongClick(rowItem.rowItem.index, v)
    }

    fun onBindViewHolder(holder: ViewHolder, position: Int, parent: ViewGroup) {
        if (debugEnabled) {
            Log.d(TAG, "onBindViewHolder($position)")
        }

        val rowInfo = itemsPerRow[position] ?: return

        val rowItems = ArrayList(rowInfo.items)
        val layout = initializeLayout(holder.itemView())
        // Index to control the current position of the current column in this row
        var columnIndex = 0
        // Index to control the current position in the array of all the items available for this row
        var currentIndex = 0
        var spaceLeftInColumn = rowInfo.rowHeight

        while (!rowItems.isEmpty() && columnIndex < listView.numColumns) {
            val currentItem = rowItems[currentIndex]

            if (spaceLeftInColumn == 0) {
                // No more space in this column. Move to next one
                columnIndex++
                currentIndex = 0
                spaceLeftInColumn = rowInfo.rowHeight
                continue
            }

            // Is there enough space in this column to accommodate currentItem?
            if (spaceLeftInColumn >= currentItem.item.rowSpan) {
                rowItems.remove(currentItem)

                val actualIndex = currentItem.index
                val viewType = asymmetricAdapter.getItemViewType(actualIndex)
                var pool = viewHoldersMap[viewType]
                if (pool == null) {
                    pool = ObjectPool()
                    viewHoldersMap[viewType] = pool
                }
                var viewHolder = pool.get()
                if (viewHolder == null) {
                    viewHolder = asymmetricAdapter.onCreateAsymmetricViewHolder(actualIndex, parent, viewType)
                }
                asymmetricAdapter.onBindAsymmetricViewHolder(viewHolder, parent, actualIndex)
                val view = viewHolder.itemView
                view.tag = ViewState(viewType, currentItem, viewHolder)
                view.setOnClickListener(this)
                view.setOnLongClickListener(this)

                spaceLeftInColumn -= currentItem.item.rowSpan
                currentIndex = 0

                view.layoutParams = LinearLayout.LayoutParams(getRowWidth(currentItem.item),
                        getRowHeight(currentItem.item))

                val childLayout = findOrInitializeChildLayout(layout, columnIndex)
                childLayout.addView(view)
            } else if (currentIndex < rowItems.size - 1) {
                // Try again with next item
                currentIndex++
            } else {
                break
            }
        }

        if (debugEnabled && position % 20 == 0) {
            Log.d(TAG, linearLayoutPool.getStats("LinearLayout"))
            for ((key, value) in viewHoldersMap) {
                Log.d(TAG, value.getStats("ConvertViewMap, viewType = $key"))
            }
        }
    }

    fun onCreateViewHolder(): ViewHolder {
        if (debugEnabled) {
            Log.d(TAG, "onCreateViewHolder()")
        }

        val layout = LinearLayout(context, null)
        if (debugEnabled) {
            layout.setBackgroundColor(findColor(R.color.frog_green))
        }

        layout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        layout.dividerDrawable = ColorDrawable(findColor(R.color.transparent))

        val layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        layout.layoutParams = layoutParams
        return ViewHolder(layout)
    }

    fun getRowHeight(item: AsymmetricItem): Int {
        return getRowHeight(item.rowSpan)
    }

    fun getRowHeight(rowSpan: Int): Int {
        val rowHeight = listView.columnWidth * rowSpan
        // when the item spans multiple rows, we need to account for the vertical padding
        // and add that to the total final height
        return rowHeight + (rowSpan - 1) * listView.dividerHeight
    }

    fun getRowWidth(item: AsymmetricItem): Int {
        return getRowWidth(item.columnSpan)
    }

    protected fun getRowWidth(columnSpan: Int): Int {
        val rowWidth = listView.columnWidth * columnSpan
        // when the item spans multiple columns, we need to account for the horizontal padding
        // and add that to the total final width
        return Math.min(rowWidth + (columnSpan - 1) * listView.requestedHorizontalSpacing, MasonryUtils.getScreenWidth(context) - getDp(20f).toInt())
    }

    private fun initializeLayout(layout: LinearLayout): LinearLayout {
        // Clear all layout children before starting
        val childCount = layout.childCount
        for (j in 0 until childCount) {
            val tempChild = layout.getChildAt(j) as LinearLayout
            linearLayoutPool.put(tempChild)
            val innerChildCount = tempChild.childCount
            for (k in 0 until innerChildCount) {
                val innerView = tempChild.getChildAt(k)
                val viewState = innerView.tag as ViewState<T>
                val pool = viewHoldersMap[viewState.viewType]
                pool!!.put(viewState.viewHolder)
            }
            tempChild.removeAllViews()
        }
        layout.removeAllViews()

        return layout
    }

    private fun findOrInitializeChildLayout(parentLayout: LinearLayout, childIndex: Int): LinearLayout {
        var childLayout: LinearLayout? = parentLayout.getChildAt(childIndex) as LinearLayout?

        if (childLayout == null) {
            childLayout = linearLayoutPool.get()
            childLayout!!.orientation = LinearLayout.VERTICAL

            if (debugEnabled) {
                childLayout.setBackgroundColor(Color.parseColor("#837BF2"))
            }

            childLayout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            childLayout.dividerDrawable = ColorDrawable(findColor(R.color.transparent))

            childLayout.layoutParams = LayoutParams(
                    WRAP_CONTENT,
                    MATCH_PARENT)

            parentLayout.addView(childLayout)
        }
        return childLayout
    }

    internal class ProcessRowsTask<VH : RecyclerView.ViewHolder>(adapterImpl: AdapterImpl<VH>) : AsyncTask<Void, Void, List<RowInfo>>() {

        private val ref: WeakReference<AdapterImpl<VH>> = WeakReference(adapterImpl)

        override fun doInBackground(vararg params: Void): List<RowInfo> {
            // We need a map in order to associate the item position in the wrapped adapter.
            val itemsToAdd = ArrayList<RowItem>()
            ref.get()?.run {
                for (i in 0 until asymmetricAdapter.actualItemCount) {
                    try {
                        itemsToAdd.add(RowItem(i, asymmetricAdapter.getItem(i)))
                    } catch (e: CursorIndexOutOfBoundsException) {
                        Log.w(TAG, e)
                    }
                }
            }

            return calculateItemsPerRow(itemsToAdd)
        }

        override fun onPostExecute(rows: List<RowInfo>) {
            ref.get()?.run {
                for (row in rows) {
                    itemsPerRow.put(rowCount, row)
                }
                if (debugEnabled) {
                    itemsPerRow.forEach { key, value -> Log.d(TAG, "row: $key, items: ${value.items.size}") }
                }
                asymmetricAdapter.notifyDataSetChanged()
            }
        }

        private fun calculateItemsPerRow(itemsToAdd: MutableList<RowItem>): List<RowInfo> {
            val rows = ArrayList<RowInfo>()
            ref.get()?.run {
                while (!itemsToAdd.isEmpty()) {
                    val stuffThatFit = calculateItemsForRow(itemsToAdd)
                    val itemsThatFit = stuffThatFit.items
                    if (itemsThatFit.isEmpty()) {
                        // we can't fit a single item inside a row.
                        // bail out.
                        break
                    }
                    for (entry in itemsThatFit) {
                        itemsToAdd.remove(entry)
                    }
                    rows.add(stuffThatFit)
                }
            }
            return rows
        }
    }

    private class ViewState<T : RecyclerView.ViewHolder> constructor(val viewType: Int, val rowItem: RowItem, val viewHolder: AsymmetricViewHolder<T>)

    class ViewHolder(itemView: LinearLayout) : RecyclerView.ViewHolder(itemView) {
        fun itemView(): LinearLayout {
            return itemView as LinearLayout
        }
    }

    companion object {
        private const val TAG = "AdapterImpl"
    }
}
