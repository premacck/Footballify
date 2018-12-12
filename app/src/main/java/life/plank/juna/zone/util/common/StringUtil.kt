package life.plank.juna.zone.util.common

import android.app.Activity
import android.support.annotation.ColorRes
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.web.WebCard
import java.util.regex.Pattern

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

fun String.toClickableWebLink(activity: Activity?): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(object : ClickableSpan() {

        override fun onClick(widget: View) {
            (activity as? BaseCardActivity)?.pushFragment(WebCard.newInstance(this@toClickableWebLink), true)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
        }
    }, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.color(R.color.dark_sky_blue, 0, length)
    return spannableString
}

fun String.formatMentions(): SpannableString {
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