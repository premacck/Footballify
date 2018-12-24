package life.plank.juna.zone.util.facilis

import android.view.ViewGroup

abstract class BaseCardChildFragment : BaseCard() {

    override fun getBackgroundBlurLayout(): ViewGroup? = (parentFragment as? BaseCardContainerFragment)?.backgroundBlurLayout()
}