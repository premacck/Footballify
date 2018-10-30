package life.plank.juna.zone.util.masonry.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import life.plank.juna.zone.util.masonry.base.AsymmetricRecyclerViewListener
import life.plank.juna.zone.util.masonry.base.AsymmetricView
import life.plank.juna.zone.util.masonry.implementation.AsymmetricViewImpl

class AsymmetricRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs, 0), AsymmetricView {

    private val viewImpl: AsymmetricViewImpl = AsymmetricViewImpl()
    private var listener: AsymmetricRecyclerViewListener? = null
    private var adapter: AsymmetricRecyclerViewAdapter<*>? = null

    override var isDebugging: Boolean
        get() = viewImpl.isDebugging
        set(debugging) {
            viewImpl.isDebugging = debugging
        }

    override val numColumns: Int
        get() = viewImpl.numColumns

    override val isAllowReordering: Boolean
        get() = viewImpl.isAllowReordering

    override val columnWidth: Int
        get() = viewImpl.getColumnWidth(availableSpace)

    private val availableSpace: Int
        get() = measuredWidth - paddingLeft - paddingRight

    override val dividerHeight: Int
        get() = 0

    override var requestedHorizontalSpacing: Int
        get() = viewImpl.requestedHorizontalSpacing
        set(spacing) {
            viewImpl.requestedHorizontalSpacing = spacing
        }

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val vto = viewTreeObserver
        vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                viewTreeObserver.removeGlobalOnLayoutListener(this)
                viewImpl.determineColumns(availableSpace)
                if (adapter != null) {
                    adapter!!.recalculateItemsPerRow()
                }
            }
        })
    }

    fun setClickListener(listener: AsymmetricRecyclerViewListener) {
        this.listener = listener
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (adapter != null) {
            if (adapter !is AsymmetricRecyclerViewAdapter<*>) {
                throw UnsupportedOperationException("Adapter must be an instance of AsymmetricRecyclerViewAdapter")
            }

            this.adapter = adapter
            super.setAdapter(adapter)

            this.adapter!!.recalculateItemsPerRow()
        } else
            super.setAdapter(null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewImpl.determineColumns(availableSpace)
    }

    override fun fireOnItemClick(index: Int, v: View) {
        listener?.fireOnItemClick(index, v)
    }

    override fun fireOnItemLongClick(index: Int, v: View): Boolean {
        return listener?.fireOnItemLongClick(index, v) ?: false
    }

    fun setRequestedColumnCount(requestedColumnCount: Int) {
        viewImpl.requestedColumnCount = requestedColumnCount
    }
}
