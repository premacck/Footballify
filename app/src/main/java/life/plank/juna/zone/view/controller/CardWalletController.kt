package life.plank.juna.zone.view.controller

import life.plank.juna.zone.data.model.card.JunaCard
import life.plank.juna.zone.util.epoxy.EpoxyController
import life.plank.juna.zone.util.epoxy.modelview.CardWalletViewModel_

class CardWalletController : EpoxyController<List<JunaCard>>() {

    override fun buildModels(cards: List<JunaCard>?) {
        cards?.forEach { junaCard ->
            CardWalletViewModel_()
                    .withName(junaCard.owner.displayName)
                    .withHandle(junaCard.owner.handle)
                    .withProfilePic(junaCard.owner.profilePictureUrl)
                    .withBorder(junaCard.template.cardColor)
                    .addTo(this)
        }
    }
}