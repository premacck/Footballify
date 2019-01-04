package life.plank.juna.zone.util.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.ahamed.multiviewadapter.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.leocardz.link.preview.library.*
import com.prembros.facilis.util.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItem
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.adapter.common.EmojiAdapter

fun BottomSheetBehavior<*>.show(peekHeight: Int = 850) {
    state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
    this.peekHeight = peekHeight
}

fun BottomSheetBehavior<*>.showFor(emojiAdapter: EmojiAdapter?, id: String?, peekHeight: Int = 850) {
    emojiAdapter?.update(id!!)
    show(peekHeight)
}

fun BottomSheetBehavior<*>.hide() {
    state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
    peekHeight = 0
}

fun BottomSheetBehavior<*>.hideIfShown(): Boolean {
    if (peekHeight == 0 || state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN || state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED) {
        return true
    }
    hide()
    return false
}

fun TextView.setEmoji(emoji: Int) {
    text = StringBuilder().appendCodePoint(emoji).toString()
}

fun <BM> RecyclerAdapter.addDataManagerAndRegisterBinder(dataManager: DataItemManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
    addDataManager(dataManager)
    registerBinder(binderToRegister)
}

fun <BM> RecyclerAdapter.addDataManagerAndRegisterBinder(dataManager: DataListManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
    addDataManager(dataManager)
    registerBinder(binderToRegister)
}

fun TextCrawler.beginPreview(url: String, onPostAction: (sourceContent: SourceContent?, isNull: Boolean) -> Unit, onPreAction: () -> Unit = {}) {
    val matcher = URL_PATTERN.matcher(url)
    var isFirstLinkRecognized = false
    while (matcher.find()) {
        if (!isFirstLinkRecognized) {
            isFirstLinkRecognized = true
            makePreview(object : LinkPreviewCallback {
                override fun onPre() = onPreAction()
                override fun onPos(sourceContent: SourceContent?, isNull: Boolean) = onPostAction(sourceContent, isNull)
            }, url)
        }
    }
}

fun View.setGradientBackground(drawableText: String) {
    when (drawableText) {
        findString(R.string.blue_color) -> background = findDrawable(R.drawable.blue_gradient)
        findString(R.string.purple_color) -> background = findDrawable(R.drawable.purple_gradient)
        findString(R.string.green_color) -> background = findDrawable(R.drawable.green_gradient)
        findString(R.string.orange_color) -> background = findDrawable(R.drawable.orange_gradient)
    }
}

fun TextView.setRootCommentPost(feedItem: FeedItem) {
    if (!isNullOrEmpty(feedItem.backgroundColor)) {
        feedItem.backgroundColor?.run { setGradientBackground(get(0)) }
        text = feedItem.title.formatLinks().formatMentions()
    } else {
//        TODO: remove comment $ prepend usage
        val comment: String = feedItem.title.replace("^\"|\"$".toRegex(), "")
        background = getCommentColor(comment)
        text = getCommentText(comment)?.toString()?.formatLinks()?.formatMentions()
    }
}

fun ViewGroup.initLayout(@LayoutRes layoutRes: Int) {
    View.inflate(context, layoutRes, this)
}

fun View.setBorder(color: Int, borderWidth: Int = getDp(1f).toInt(), cornerRadius: Float = getDp(8f)) {
    val gradientDrawable = GradientDrawable().apply {
        orientation = GradientDrawable.Orientation.LEFT_RIGHT
        setStroke(borderWidth, color)
        setCornerRadius(cornerRadius)
    }
    background = gradientDrawable
}

fun String.getCardColor(): Int {
    return when (this) {
        "BLUE" -> Color.BLUE
        "BRONZE" -> 0xFFCD7F32.toInt()
        "SILVER" -> 0xFFC0C0C0.toInt()
        "GOLD" -> 0xFFFFD700.toInt()
        "BLACK" -> Color.BLACK
        else -> Color.WHITE
    }
}