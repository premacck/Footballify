package life.plank.juna.zone.view.controller

import com.airbnb.epoxy.AutoModel
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.card.JunaCard
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.epoxy.EpoxyController3
import life.plank.juna.zone.util.epoxy.modelview.*
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.activity.card.CreateCardActivity
import life.plank.juna.zone.view.fragment.profile.ProfileCardFragment
import retrofit2.Response

class CardWalletController(private val activity: BaseJunaCardActivity) : EpoxyController3<Response<List<JunaCard>>, Boolean, String>() {

    @AutoModel
    lateinit var cardWalletHeader: CardWalletHeaderModel_
    @AutoModel
    lateinit var textModelView: TextModelViewModel_

    override fun buildModels(response: Response<List<JunaCard>>?, isCollection: Boolean, errorMessage: String?) {
        val cards = response?.body()

        cardWalletHeader.withHeader(if (isCollection) R.string.collection else R.string.creations)
                .withHeaderCount(if (isCollection) findString(R.string.collection_status, 25, 30) else null)
                .withHeaderAction(if (isCollection) R.string.increase_capacity else R.string.create_new)
                .onClick { CreateCardActivity.launch(activity) }
                .addTo(this)

        textModelView
                .withText(StringBuilder(errorMessage ?: findString(R.string.failed_to_get_wallet))
                        .maybeAppend(AppConstants.NEW_LINE, errorMessage != null)
                        .maybeAppend("Error : ", errorMessage != null)
                        .maybeAppend(response?.code()?.toString()
                                ?: errorMessage, errorMessage != null))
                .withVerticalMargins(getDp(50f).toInt())
                .addIf(isNullOrEmpty(cards), this)

        cards?.forEach { junaCard ->
            CardWalletViewModel_()
                    .id(junaCard.id)
                    .withUser(junaCard.owner)
                    .withBorder(junaCard.template.cardColor)
                    .onClick { activity.pushFragment(ProfileCardFragment.newInstance(junaCard, true)) }
                    .addTo(this)
        }
    }
}