package life.plank.juna.zone.view.base.fragment

import com.prembros.facilis.fragment.BaseFragment
import rx.Subscription

abstract class BaseJunaFragment : BaseFragment() {

    val subscriptionList: MutableList<Subscription> = ArrayList()

    override fun onDestroyView() {
        subscriptionList.forEach { it.unsubscribe() }
        super.onDestroyView()
    }
}