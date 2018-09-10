package life.plank.juna.zone.util.customview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Highlights;
import life.plank.juna.zone.util.BaseRecyclerView;

public class HighlightsAdapter extends BaseRecyclerView.Adapter<HighlightsAdapter.MatchHighlightsViewHolder> {

    private List<Highlights> highlightsList;

    public HighlightsAdapter() {
        highlightsList = new ArrayList<>();
    }

    @Override
    public MatchHighlightsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchHighlightsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highlights, parent, false), this
        );
    }

    @Override
    public int getItemCount() {
        return highlightsList.size();
    }

    public void update(List<Highlights> highlightsList) {
        this.highlightsList.addAll(highlightsList);
        notifyDataSetChanged();
    }

    static class MatchHighlightsViewHolder extends BaseRecyclerView.ViewHolder {

        private final WeakReference<HighlightsAdapter> ref;
        @BindView(R.id.highlights_player)
        WebView webView;
        private Highlights highlights;

        MatchHighlightsViewHolder(View itemView, HighlightsAdapter highlightsAdapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.ref = new WeakReference<>(highlightsAdapter);
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void bind() {
            highlights = ref.get().highlightsList.get(getAdapterPosition());
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(highlights.getHighlightsLink());
        }
    }
}