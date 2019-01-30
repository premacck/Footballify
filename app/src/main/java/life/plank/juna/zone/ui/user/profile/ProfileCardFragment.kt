package life.plank.juna.zone.ui.user.profile

import android.os.Bundle
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.*
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_profile_card.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.card.JunaCardTemplate
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.ui.base.fragment.BaseJunaCard
import life.plank.juna.zone.util.common.errorToast
import org.jetbrains.anko.support.v4.toast
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class ProfileCardFragment : BaseJunaCard() {

    @Inject
    lateinit var restApi: RestApi

    private var cardId: String? = null
    private var cardTemplate: JunaCardTemplate? = null
    private var isFollowing: Boolean = false

    companion object {
        const val CARD_OBJECT_AVAILABLE = 1
        const val CARD_ID_AVAILABLE = 2
        const val CARD_NOT_AVAILABLE = 3
        val TAG: String = ProfileCardFragment::class.java.simpleName
        fun newInstance(cardId: String, isFollowing: Boolean): ProfileCardFragment = ProfileCardFragment().apply {
            arguments = Bundle().apply {
                putString(findString(R.string.intent_card_id), cardId)
                putBoolean(findString(R.string.intent_is_following), isFollowing)
            }
        }

        fun newInstance(cardTemplate: JunaCardTemplate, isFollowing: Boolean): ProfileCardFragment = ProfileCardFragment().apply {
            arguments = Bundle().apply {
                putParcelable(findString(R.string.intent_juna_card), cardTemplate)
                putBoolean(findString(R.string.intent_is_following), isFollowing)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            cardId = getString(getString(R.string.intent_card_id))
            cardTemplate = getParcelable(getString(R.string.intent_juna_card))
            isFollowing = getBoolean(getString(R.string.intent_is_following), false)
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_profile_card, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resolveFollowState()

        when (cardLaunchArgumentState()) {
            CARD_OBJECT_AVAILABLE -> updateUi()
            CARD_ID_AVAILABLE -> getCardDetail()
            else -> {
                toast(R.string.card_details_not_provided)
                doAfterDelay(1000) { parentActivity().onBackPressed() }
            }
        }
    }

    private fun resolveFollowState() {
        collect_description_text.visibility = if (isFollowing) View.GONE else View.VISIBLE
        collect_button.visibility = if (isFollowing) View.GONE else View.VISIBLE
        (if (isFollowing) root_card else collect_button).onDebouncingClick {
            pushFragment(ProfileCardDetailFragment.newInstance(cardTemplate!!))
        }
    }

    private fun getCardDetail() {
        restApi.getCardDetail(cardId!!).setObserverThreadsAndSmartSubscribe({
            errorToast(R.string.failed_to_get_card_details, it)
        }, { response ->
            when (response.code()) {
                HTTP_OK -> {
                    cardTemplate = response.body()?.template
                    updateUi()
                }
                else -> errorToast(R.string.failed_to_get_card_details, response)
            }
        }, this)
    }

    private fun updateUi() {
        cardTemplate?.run {
            Glide.with(this@ProfileCardFragment)
                    .load(issuer?.cardPictureUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.shimmer_rectangle))
                    .into(card_pic)
            Glide.with(this@ProfileCardFragment)
                    .load(issuer?.profilePictureUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.shimmer_circle))
                    .into(profile_image_view)
            name_text_view.text = issuer?.handle
            followers_count.text = issuer?.followersCount.toString()
            card_count.text = issuer?.cardCount.toString()
        }
    }

    private fun cardLaunchArgumentState() = when {
        cardTemplate != null -> CARD_OBJECT_AVAILABLE
        !isNullOrEmpty(cardId) -> CARD_ID_AVAILABLE
        else -> CARD_NOT_AVAILABLE
    }

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area
}
