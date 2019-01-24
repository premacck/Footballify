package life.plank.juna.zone.view.user.card

import android.os.Bundle
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.activity_card_wallet.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import java.net.HttpURLConnection.*
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
        toolbar.setProfilePic(CurrentUser.profilePicUrl)
        swipe_refresh_layout.setOnRefreshListener { getCardWallet((true)) }
    }

    private fun initRecyclerView() {
        cardWalletController = CardWalletController(this)
        card_wallet_list.setController(cardWalletController)
    }

    private fun getCardWallet(isRefreshing: Boolean = false) {
        restApi.getCardWallet()
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
                })
    }

    override fun getFragmentContainer(): Int = R.id.popup_container

    override fun restApi(): RestApi? = restApi
}
