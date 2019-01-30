package life.plank.juna.zone.ui.user.card

import android.content.Intent
import android.os.Bundle
import com.prembros.facilis.util.doAfterDelay
import kotlinx.android.synthetic.main.activity_card_wallet.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.local.CardMockData
import life.plank.juna.zone.data.model.card.JunaCardTemplate
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.user.card.CreateCardActivity.Companion.CREATE_CARD_REQUEST_CODE
import life.plank.juna.zone.ui.user.profile.ProfileCardFragment
import life.plank.juna.zone.util.common.errorToast
import okhttp3.*
import retrofit2.Response
import javax.inject.Inject

class CardWalletActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var cardWalletController: CardWalletController
    private lateinit var cardCreationController: CardWalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_wallet)
        ZoneApplication.getApplication().uiComponent.inject(this)

        initViews()
        initRecyclerView()
        getCardWallet()
    }

    private fun initViews() {
        toolbar.setProfilePic(CurrentUser.profilePicUrl)
        swipe_refresh_layout.setOnRefreshListener { getCardWallet((true)) }
    }

    private fun initRecyclerView() {
        cardWalletController = CardWalletController(this)
        cardCreationController = CardWalletController(this)
        card_wallet_list.setController(cardWalletController)
        card_creation_list.setController(cardCreationController)
    }

    private fun getCardWallet(isRefreshing: Boolean = false) {
//        TODO: remove following three lines and un-comment the commented part below to use API calls instead of mocked data
        cardWalletController.setData(Response.success(CardMockData.mockedCards), true, null)
        if (isRefreshing) swipe_refresh_layout.isRefreshing = false
        cardCreationController.setData(Response.success(CardMockData.mockedSelfCards), false, null)

        /*restApi.getCardWallet()
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({ e ->
                    cardWalletController.setData(null, false, e.message)
                }, { response ->
                    val cards = response.body()
                    cardWalletController.setData(
                            response,
                            false,
                            when (response.code()) {
                                HTTP_OK, HTTP_NOT_FOUND -> if (!isNullOrEmpty(cards)) null else getString(R.string.no_wallet_content)
                                else -> getString(R.string.failed_to_get_wallet)
                            }
                    )
                })*/
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) return
        when (requestCode) {
            CREATE_CARD_REQUEST_CODE -> when (resultCode) {
                RESULT_OK -> {
                    val cardTemplate = data.getParcelableExtra<JunaCardTemplate>(getString(R.string.intent_juna_card))
                    doAfterDelay(100) { pushFragment(ProfileCardFragment.newInstance(cardTemplate, true)) }
                }
                RESULT_CANCELED -> {
                    val responseCode = data.getIntExtra(getString(R.string.response_code), 204)
                    errorToast(
                            R.string.failed_to_create_update_card,
                            Response.error<JunaCardTemplate>(responseCode, ResponseBody.create(MediaType.parse(getString(R.string.text_content_type)), getString(R.string.failed_to_create_update_card)))
                    )
                }
            }
        }
    }
}
