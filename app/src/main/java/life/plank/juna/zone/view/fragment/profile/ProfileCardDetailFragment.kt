package life.plank.juna.zone.view.fragment.profile

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_profile_card_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.adapter.profile.ProfileCardAdapter


class ProfileCardDetailFragment : BaseCard() {

    private var profileCardAdapter: ProfileCardAdapter? = null
    private val maxLines = 2

    companion object {
        fun newInstance() = ProfileCardDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile_card_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCardRecyclerView()
        var initialText = bio.text
        var initialTextViewLines = bio.lineCount

        bio.post {
            if (bio.lineCount > maxLines) {
                truncateText(bio)
            }
        }

        bio.setOnClickListener {
            if (bio.lineCount > maxLines) {
                truncateText(bio)
            } else {
                bio.text = initialText
                bio.maxLines = initialTextViewLines
            }
        }
    }

    //TODO: Create a separate ExpandableTextView class and move out this piece of code
    private fun truncateText(textView: TextView) {
        val lastCharShown = textView.layout.getLineVisibleEnd(maxLines - 1)

        bio.maxLines = maxLines

        val suffix = getString(R.string.see_more_with_space)

        val displayText = textView.text.substring(0, lastCharShown - suffix.length - 3) + "..." + suffix

        val truncatedSpannableString = SpannableString(displayText)
        val startIndex = displayText.indexOf(suffix)
        truncatedSpannableString.setSpan(ForegroundColorSpan(context?.getColor(R.color.accent)!!), startIndex, startIndex + suffix.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = truncatedSpannableString
    }

    private fun initCardRecyclerView() {
        profileCardAdapter = ProfileCardAdapter()
        cards_recycler_view.adapter = profileCardAdapter
    }

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area
}