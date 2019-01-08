package life.plank.juna.zone.view.fragment.profile

import android.os.Bundle
import android.view.*
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.user_profile_card.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.card.JunaCard
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.fragment.base.BaseJunaCard
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class ProfileCardFragment : BaseJunaCard() {

    @Inject
    lateinit var restApi: RestApi

    private var cardId: String? = null
    private var junaCard: JunaCard? = null

    companion object {
        val TAG: String = ProfileCardFragment::class.java.simpleName
        fun newInstance(): ProfileCardFragment = ProfileCardFragment()
        fun newInstance(cardId: String): ProfileCardFragment = ProfileCardFragment().apply { arguments = Bundle().apply { putString(findString(R.string.intent_card_id), cardId) } }
        fun newInstance(junaCard: JunaCard): ProfileCardFragment = ProfileCardFragment().apply { arguments = Bundle().apply { putParcelable(findString(R.string.intent_juna_card), junaCard) } }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            cardId = getString(getString(R.string.intent_card_id))
            junaCard = getParcelable(getString(R.string.intent_juna_card))
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.user_profile_card, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        options_menu.fadeIn().then { collect_description_text.fadeIn().then { collect_button.fadeIn() } }

        collect_button.onDebouncingClick {
            pushFragment(ProfileCardDetailFragment.newInstance(), true)
        }

        if (cardIdAvailableOnly()) {
            getCardDetail()
            return
        }
        updateUi()
    }

    private fun updateUi() {
        junaCard?.run {
            Glide.with(this@ProfileCardFragment)
                    .load(owner.profilePictureUrl)
                    .into(profile_pic)
            name_text_view.text = owner.handle
            followers_count.text = owner.followersCount.toString()
            card_count.text = owner.cardCount.toString()
        }
    }

    private fun getCardDetail() {
        restApi.getCardDetail(cardId, getToken()).setObserverThreadsAndSmartSubscribe({
            errorToast(R.string.failed_to_get_card_details, it)
        }, { response ->
            when (response.code()) {
                HTTP_OK -> {
                    junaCard = response.body()
                    updateUi()
                }
                else -> errorToast(R.string.failed_to_get_card_details, response)
            }
        }, this)
    }

    private fun cardIdAvailableOnly() = junaCard == null && !isNullOrEmpty(cardId)

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area
}
