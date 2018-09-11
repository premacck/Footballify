package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Highlights;

public class MatchHighlights extends FrameLayout {

    @BindView(R.id.list_highlights)
    RecyclerView highlightsRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private PagerSnapHelper snapHelper;

    private HighlightsAdapter adapter;

    public MatchHighlights(@NonNull Context context) {
        super(context);
    }

    public MatchHighlights(@NonNull Context context, PagerSnapHelper snapHelper) {
        this(context, null, snapHelper);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs, PagerSnapHelper snapHelper) {
        this(context, attrs, 0, snapHelper);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, PagerSnapHelper snapHelper) {
        this(context, attrs, defStyleAttr, 0, snapHelper);
    }

    public MatchHighlights(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes, PagerSnapHelper snapHelper) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.snapHelper = snapHelper;
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_match_highlights_layout, this);
        ButterKnife.bind(this, rootView);
        snapHelper.attachToRecyclerView(highlightsRecyclerView);
    }

    public void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? VISIBLE : GONE);
        highlightsRecyclerView.setVisibility(isLoading ? GONE : VISIBLE);
    }

    public void setAdapter(HighlightsAdapter adapter) {
        this.adapter = adapter;
        highlightsRecyclerView.setAdapter(this.adapter);
    }

    public void setHighlights(List<Highlights> highlights) {
        adapter.update(highlights);
    }
}