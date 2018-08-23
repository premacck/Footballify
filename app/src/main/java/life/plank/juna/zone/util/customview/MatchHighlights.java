package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Highlights;

public class MatchHighlights extends FrameLayout {

    @BindView(R.id.highlights_player)
    SimpleExoPlayerView highlightsPlayer;
    @BindView(R.id.play_btn)
    ImageView playButton;

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
    }

    public void initExoPlayer() {
//        TODO : initialize ExoPlayer here.
    }

    public void setHighlights(Highlights highlights) {
//        TODO : set highlights thumbnail and video URL here
    }
}