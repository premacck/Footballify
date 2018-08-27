package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Highlights;

public class MatchHighlights extends FrameLayout {

    @BindView(R.id.highlights_player)
    SimpleExoPlayerView highlightsPlayer;
    @BindView(R.id.play_btn)
    ImageView playButton;

    private SimpleExoPlayer player;

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

        this.setOnClickListener(view -> {
            if (player != null)
                player.setPlayWhenReady(!player.getPlayWhenReady());
        });
        initExoPlayer(context);
    }

    public void initExoPlayer(Context context) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            highlightsPlayer.setPlayer(player);
        }
    }

    public void prepareExoPlayer(Context context, String videoUrl) {
        player.setPlayWhenReady(false);
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                new DefaultDataSourceFactory(context, "ua"),
                new DefaultExtractorsFactory(), null, null);
        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
        player.prepare(loopingSource, true, false);
    }

    public void play() {
        player.setPlayWhenReady(true);
    }

    public void pause() {
        player.setPlayWhenReady(false);
    }

    public void dispose() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void setHighlights(Context context, Highlights highlights) {
        prepareExoPlayer(context, highlights.getHighlightsUrl());
    }
}