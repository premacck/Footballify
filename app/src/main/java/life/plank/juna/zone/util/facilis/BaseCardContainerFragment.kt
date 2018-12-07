package life.plank.juna.zone.util.facilis

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseFragment
import java.lang.ref.WeakReference

abstract class BaseCardContainerFragment : BaseCard() {

    private lateinit var pagerAdapter: BaseCardContainerPagerAdapter

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = BaseCardContainerPagerAdapter(childFragmentManager, this)
        viewPager().adapter = pagerAdapter
        viewPager().currentItem = initialPosition()
        (activity as? BaseCardActivity)?.run { if (index > 0) viewPager().floatUp() }
    }

    open fun initialPosition(): Int = 0

    abstract fun baseCardCount(): Int

    abstract fun viewPager(): ViewPager

    abstract fun baseCardToInflate(position: Int): BaseFragment

    class BaseCardContainerPagerAdapter(fm: FragmentManager, baseCardContainerFragment: BaseCardContainerFragment) : FragmentStatePagerAdapter(fm) {

        private val ref: WeakReference<BaseCardContainerFragment> = WeakReference(baseCardContainerFragment)
        private var currentCard: BaseFragment? = null

        override fun getItem(position: Int): Fragment? = ref.get()?.baseCardToInflate(position)

        override fun getCount(): Int = ref.get()?.baseCardCount() ?: 0

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentCard !== `object`) {
                currentCard = `object` as? BaseFragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }

    override fun getRootView(): ViewGroup? = viewPager()
}