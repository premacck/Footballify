package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Highlights;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class MatchHighlights extends FrameLayout {

    @BindView(R.id.highlights_player)
    WebView webView;
    @BindView(R.id.highlights_thumbnail)
    ImageView highlightsThumbnail;
    @BindView(R.id.play_btn)
    ImageView playButton;

    private boolean areHighlightsSet;
    private Highlights highlights;

    public MatchHighlights(@NonNull Context context) {
        this(context, null);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_match_highlights, this);
        ButterKnife.bind(this, rootView);
        areHighlightsSet = false;
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
    }

    @OnClick(R.id.play_btn)
    public void playVideo() {
        if (highlights != null && areHighlightsSet) {
            playButton.setVisibility(GONE);
            highlightsThumbnail.setVisibility(GONE);
            webView.setWebViewClient(new WebViewClient());
            webView.loadData(highlights.getHighlightsUrl(), getContext().getString(R.string.mime_type_m3u8), getContext().getString(R.string.utf_8_));
        }
    }

    public void setHighlights(Picasso picasso, Highlights highlights) {
        this.highlights = highlights;
        if (!isNullOrEmpty(highlights.getHighlightsThumbUrl()) && !highlights.getHighlightsThumbUrl().equals("TBA")) {
            picasso.load(highlights.getHighlightsThumbUrl())
                    .into(highlightsThumbnail);
        }
        areHighlightsSet = true;
    }
}