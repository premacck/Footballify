package life.plank.juna.zone.view.activity.card

import android.os.Bundle
import androidx.annotation.StringRes
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.activity_card_wallet.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.NEW_LINE
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.controller.CardWalletController
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class CardWalletActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var cardWalletController: CardWalletController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_wallet)
        ZoneApplication.getApplication().uiComponent.inject(this)

        initViews()
        initRecyclerView()
        getCardWallet()
    }

    private fun initViews() {
        toolbar.setProfilePic(PreferenceManager.CurrentUser.getProfilePicUrl())
        swipe_refresh_layout.setOnRefreshListener { getCardWallet((true)) }
    }

    private fun initRecyclerView() {
        cardWalletController = CardWalletController()
        card_wallet_list.setController(cardWalletController)
    }

    private fun getCardWallet(isRefreshing: Boolean = false) {
        restApi.getCardWallet(getToken())
                .onTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .setObserverThreadsAndSmartSubscribe({ e ->
                    updateUi(false, R.string.failed_to_get_wallet, e.message)
                }, { response ->
                    when (response.code()) {
                        HTTP_OK -> {
                            val cards = response.body()
                            if (!isNullOrEmpty(cards)) {
                                updateUi(true)
                                cardWalletController.setData(cards)
                            } else {
                                updateUi(false, R.string.no_wallet_content)
                            }
                        }
                        else -> updateUi(false, R.string.failed_to_get_wallet, response.code().toString())
                    }
                })
    }

    private fun updateUi(isDataAvailable: Boolean, @StringRes message: Int = R.string.something_went_wrong, errorMessage: String? = null) {
        if (isDataAvailable) card_wallet_list.makeVisible() else card_wallet_list.makeInvisible()
        if (isDataAvailable) no_data.makeGone() else no_data.makeVisible()
        if (!isDataAvailable) no_data.text = StringBuilder(getString(message))
                .maybeAppend(NEW_LINE, errorMessage != null)
                .maybeAppend("Error : ", errorMessage != null)
                .maybeAppend(errorMessage, errorMessage != null)
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi
}
