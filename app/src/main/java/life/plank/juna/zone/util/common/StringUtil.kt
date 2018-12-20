package life.plank.juna.zone.util.common

import android.app.Activity
import android.text.*
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Commentary
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.web.WebCard
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.regex.Pattern
import java.util.regex.Pattern.*
import kotlin.text.append

const val URL_REGEX = ("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)")
val URL_PATTERN: Pattern = Pattern.compile(URL_REGEX, CASE_INSENSITIVE or MULTILINE or DOTALL)

fun String.toSpannableString(): SpannableString = SpannableString(this)

fun String.bold(start: Int = 0, end: Int = length): SpannableString = toSpannableString().bold(start, end)

fun String.semiBold(start: Int = 0, end: Int = length): SpannableString = toSpannableString().semiBold(start, end)

fun String.color(@ColorRes colorRes: Int, start: Int = 0, end: Int = length): SpannableString = toSpannableString().color(colorRes, start, end)

fun SpannableString.bold(start: Int = 0, end: Int = length): SpannableString {
    setSpan(BOLD_STYLE, start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.semiBold(start: Int = 0, end: Int = length): SpannableString {
    setSpan(SEMI_BOLD_STYLE, start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.color(@ColorRes colorRes: Int, start: Int = 0, end: Int = length): SpannableString {
    setSpan(ForegroundColorSpan(findColor(colorRes)), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableString.underline(start: Int = 0, end: Int = length): SpannableString {
    setSpan(UnderlineSpan(), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun CharSequence.toClickableWebLink(activity: Activity?, start: Int = 0, end: Int = length): SpannableString {
    val spannableString: SpannableString = if (this is SpannableString) this else SpannableString(this)
    spannableString.clickAction(start, end) {
        (activity as? BaseCardActivity)?.pushFragment(WebCard.newInstance(this@toClickableWebLink.toString()), true)
    }.color(R.color.dark_sky_blue, start, end)
    return spannableString
}

fun SpannableString.clickAction(start: Int = 0, end: Int = length, action: () -> Unit): SpannableString {
    setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            action()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
        }
    }, start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun CharSequence.formatMentions(): SpannableString {
    val formattedReply = SpannableString(this)

    if (!contains("@")) return formattedReply

    val matcher = Pattern.compile("@[0-9a-zA-Z._]+").matcher(this)

    while (matcher.find()) {
        try {
            formattedReply.semiBold(matcher.start(), matcher.end())
                    .color(R.color.dark_sky_blue, matcher.start(), matcher.end())
        } catch (ignored: Exception) {
        }
    }
    return formattedReply
}

fun SpannableStringBuilder.maybeAppend(charSequence: CharSequence?, condition: Boolean): SpannableStringBuilder {
    if (condition) append(charSequence)
    return this
}

fun StringBuilder.maybeAppend(charSequence: CharSequence?, condition: Boolean): StringBuilder {
    if (condition) append(charSequence)
    return this
}

fun SpannableStringBuilder.appendOneOrOther(condition: Boolean, conditionOneString: CharSequence?, conditionTwoString: CharSequence?): SpannableStringBuilder {
    append(if (condition) conditionOneString else conditionTwoString)
    return this
}

fun String.asciiToInt(): Int {
    var result = 0
    forEach {
        try {
//            Standard hashCode formula
            result = 31 * result + it.toByte().toInt()
        } catch (ignored: Exception) {
        }
    }
    return Math.abs(result)
}

fun TextView.setCommentaryText(commentaryList: List<Commentary>) {
    val commentaryString = SpannableStringBuilder()
    context.doAsync {
        commentaryList.forEach { commentaryString.append(getDesignedCommentaryString(it.comment), SPACE, "|".bold(), SPACE) }
        uiThread {
            text = commentaryString
        }
    }
}

fun getDesignedCommentaryString(rawCommentaryText: String): SpannableStringBuilder {
    return when {
        rawCommentaryText.contains(GOAL_) || rawCommentaryText.contains(OWN_GOAL_LOWERCASE) -> getDesignedString(
                GOAL_,
                rawCommentaryText,
                R.color.purple_timeline,
                R.drawable.ic_goal_left,
                true
        )
        rawCommentaryText.contains(CORNER_) -> getDesignedString(
                CORNER_,
                rawCommentaryText,
                R.color.black,
                -1,
                true
        )
        rawCommentaryText.contains(SUBSTITUTION_) -> getDesignedString(
                SUBSTITUTION_,
                rawCommentaryText,
                R.color.black,
                R.drawable.ic_sub_right,
                true
        )
        rawCommentaryText.contains(OFFSIDE_) -> getDesignedString(
                OFFSIDE_,
                rawCommentaryText,
                R.color.black,
                -1,
                true
        )
        rawCommentaryText.contains(YELLOW_CARD_) -> getDesignedString(
                YELLOW_CARD_,
                rawCommentaryText,
                R.color.commentary_yellow,
                R.drawable.yellow_right,
                true
        )
        rawCommentaryText.contains(RED_CARD_) -> getDesignedString(
                RED_CARD_,
                rawCommentaryText,
                R.color.scarlet_red,
                R.drawable.red_right,
                true
        )
        rawCommentaryText.contains(FREE_KICK_) -> getDesignedString(
                FREE_KICK_,
                rawCommentaryText,
                R.color.black,
                -1,
                true
        )
        rawCommentaryText.contains(FIRST_HALF_ENDED_) -> getDesignedString(
                rawCommentaryText,
                rawCommentaryText,
                R.color.dark_sky_blue,
                R.drawable.ic_whistle,
                false
        )
        rawCommentaryText.contains(SECOND_HALF_ENDED_) -> getDesignedString(
                rawCommentaryText,
                rawCommentaryText,
                R.color.dark_sky_blue,
                R.drawable.ic_whistle,
                false
        )
        else -> getDesignedString(
                null,
                rawCommentaryText,
                -1,
                -1,
                false
        )
    }
}

fun CharSequence.containsLink(): Boolean = URL_PATTERN.matcher(this).find()

fun CharSequence.formatLinks(activity: Activity? = null): SpannableString {
    val formattedString = SpannableString(this)
    val matcher = URL_PATTERN.matcher(this)
    while (matcher.find()) {
        formattedString.toClickableWebLink(activity, matcher.start(), matcher.end())
    }
    return formattedString
}