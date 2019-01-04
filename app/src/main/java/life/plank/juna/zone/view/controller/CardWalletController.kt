package life.plank.juna.zone.view.controller

import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.card.JunaCard
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.epoxy.EpoxyController2
import life.plank.juna.zone.util.epoxy.modelview.CardWalletViewModel_

class CardWalletController : EpoxyController2<List<JunaCard>, Boolean>() {

    override fun buildModels(cards: List<JunaCard>?, isCollection: Boolean) {
        CardWalletHeaderModel_()
                .withHeader(if (isCollection) R.string.collection else R.string.creations)
                .withHeaderCount(if (isCollection) findString(R.string.collection_status, 25, 30) else null)
                .withHeaderAction(if (isCollection) R.string.increase_capacity else R.string.create_new)
                .addIf(!isNullOrEmpty(cards), this)

        cards?.forEach { junaCard ->
            CardWalletViewModel_()
                    .withUser(junaCard.owner)
                    .withBorder(junaCard.template.cardColor)
                    .addTo(this)
        }
    }
}