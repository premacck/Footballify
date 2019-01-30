package life.plank.juna.zone.ui.user.profile

import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.View.*
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.onDebouncingClick
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_profile_card_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.card.JunaCardTemplate
import life.plank.juna.zone.service.CommonDataService
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.fragment.BaseJunaCard


class ProfileCardDetailFragment : BaseJunaCard() {

    private var profileCardAdapter: ProfileCardAdapter? = null
    private val maxLines = 2
    private lateinit var cardTemplate: JunaCardTemplate

    companion object {
        fun newInstance(cardTemplate: JunaCardTemplate) =
                ProfileCardDetailFragment().apply { arguments = Bundle().apply { putParcelable(CommonDataService.findString(R.string.intent_juna_card), cardTemplate) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments!!.run { cardTemplate = getParcelable(getString(R.string.intent_juna_card))!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile_card_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCardRecyclerView()
        val initialText = bio.text
        val initialTextViewLines = bio.lineCount

        bio.post {
            if (bio.lineCount > maxLines) {
                truncateText(bio)
            }
        }

        bio.onDebouncingClick {
            if (bio.lineCount > maxLines) {
                truncateText(bio)
            } else {
                bio.text = initialText
                bio.maxLines = initialTextViewLines
            }
        }
        updateUi()
    }

    private fun updateUi() {
        Glide.with(this)
                .load(cardTemplate.issuer?.cardPictureUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.shimmer_rectangle))
                .into(profile_image_view)

        display_name.text = cardTemplate.issuer?.displayName
        val handle = "@${cardTemplate.issuer?.handle}"
        handle_name.text = handle
        followers_count.text = cardTemplate.issuer?.followersCount?.toString()
        card_count.text = cardTemplate.issuer?.cardCount?.toString()

        collect_card_button.visibility = if (CurrentUser.handle == cardTemplate.issuer?.handle) GONE else VISIBLE
    }

    //TODO: Create a separate ExpandableTextView class and move out this piece of code
    private fun truncateText(textView: TextView) {
        val lastCharShown = textView.layout.getLineVisibleEnd(maxLines - 1)

        bio.maxLines = maxLines

        val suffix = getString(R.string.see_more_with_space)

        val displayText = textView.text.substring(0, lastCharShown - suffix.length - 3) + "..." + suffix

        val truncatedSpannableString = SpannableString(displayText)
        val startIndex = displayText.indexOf(suffix)
        truncatedSpannableString.setSpan(ForegroundColorSpan(context?.getColor(R.color.colorAccent)!!), startIndex, startIndex + suffix.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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