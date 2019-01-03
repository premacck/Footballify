package life.plank.juna.zone.util.toro

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.*
import com.google.android.exoplayer2.upstream.cache.*
import com.prembros.facilis.util.*
import im.ene.toro.exoplayer.*
import life.plank.juna.zone.ZoneApplication
import java.io.File

class ToroUtil {
    companion object {
        private const val cacheByteLength = 10 * 1024 * 1024L
        private val config: Config = Config.Builder()
                .setMediaSourceBuilder(MediaSourceBuilder.LOOPING)
                .setCache(SimpleCache(File("${ZoneApplication.getContext().filesDir.path}/juna_cache"), LeastRecentlyUsedCacheEvictor(cacheByteLength)))
                .build()
        val exoCreator: ExoCreator = ToroExo.with(ZoneApplication.getContext()).getCreator(config)
    }
}

fun Player.isPlaying() = playWhenReady

fun Player.play() {
    playWhenReady = true
}

fun Player.pause() {
    playWhenReady = false
}

fun Player.toggle() {
    playWhenReady = !playWhenReady
}

fun SimpleExoPlayer.addPlayPauseListener(
        playerView: PlayerView?,
        playPauseBtn: View? = null,
        loadingProgressBar: ProgressBar? = null,
        action: (playWhenReady: Boolean, playbackState: Int) -> Unit = { _, _ -> }
): SimpleExoPlayer {
    addListener(object : Playable.DefaultEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)

            val active = playbackState > Player.STATE_IDLE && playbackState < Player.STATE_ENDED
            playerView?.keepScreenOn = active

            when (playbackState) {
                Player.STATE_IDLE -> playPauseBtn?.makeVisible()
                Player.STATE_BUFFERING -> {
                    playPauseBtn?.makeGone()
                    loadingProgressBar?.makeVisible()
                }
                Player.STATE_READY -> {
                    if (playWhenReady) {
                        playPauseBtn?.makeGone()
                    } else playPauseBtn?.makeVisible()
                    loadingProgressBar?.makeGone()
                }
            }
            action(playWhenReady, playbackState)
        }
    })
    return this
}

fun SimpleExoPlayer.playWhenReady(): SimpleExoPlayer {
    playWhenReady = true
    return this
}

fun Context.getExoPlayer() = ToroExo.with(this).requestPlayer(ToroUtil.exoCreator)

fun Fragment.getExoPlayer() = context!!.getExoPlayer()

class ExoBuilder {
    companion object {
        fun with(context: Context?) = context!!.getExoPlayer()

        fun with(fragment: Fragment) = fragment.getExoPlayer()
    }
}

fun SimpleExoPlayer.withMediaSource(videoUri: Uri): SimpleExoPlayer {
    prepare(ToroUtil.exoCreator.createMediaSource(videoUri, null))
    return this
}

fun SimpleExoPlayer.withContainer(container: ViewGroup?): SimpleExoPlayer {
    container?.onDebouncingClick { toggle() }
    return this
}

fun SimpleExoPlayer.applyTo(playerView: PlayerView): SimpleExoPlayer {
    playerView.player = this
    return this
}

fun String.getVideoWidthAndHeight(): IntArray {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this)
    val width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
    val height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
    retriever.release()
    return intArrayOf(width, height)
}

fun AspectRatioFrameLayout.setAspectRatioFrom(videoPath: String) {
    val widthHeight = videoPath.getVideoWidthAndHeight()
    setAspectRatio(widthHeight[0] / widthHeight[1].toFloat())
}