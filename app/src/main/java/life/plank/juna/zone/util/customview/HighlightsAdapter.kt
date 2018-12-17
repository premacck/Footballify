package life.plank.juna.zone.util.customview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_highlights.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Highlights
import java.util.*

class HighlightsAdapter(private val height: Int) : RecyclerView.Adapter<HighlightsAdapter.MatchHighlightsViewHolder>() {

    private val highlightsList: MutableList<Highlights> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchHighlightsViewHolder {
        return MatchHighlightsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_highlights, parent, false)
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindViewHolder(holder: MatchHighlightsViewHolder, position: Int) {
        holder.itemView.run {
            val highlights = highlightsList[position]
            highlights_player.webChromeClient = WebChromeClient()
            highlights_player.settings.javaScriptEnabled = true
            highlights_player.loadUrl(highlights.highlightsLink)
            val params = highlights_player.layoutParams as RelativeLayout.LayoutParams
            params.height = this@HighlightsAdapter.height
            highlights_player.layoutParams = params
        }
    }

    override fun getItemCount(): Int {
        return highlightsList.size
    }

    fun update(highlightsList: List<Highlights>) {
        this.highlightsList.addAll(highlightsList)
        notifyDataSetChanged()
    }

    class MatchHighlightsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}