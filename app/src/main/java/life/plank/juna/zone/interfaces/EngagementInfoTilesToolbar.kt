package life.plank.juna.zone.interfaces

import android.support.annotation.DrawableRes
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

interface EngagementInfoTilesToolbar {

    fun getInfoTilesTabLayout(): TabLayout

    fun setLeagueLogo(logoUrl: String)

    fun setLeagueLogo(@DrawableRes resource: Int)

    fun setPeopleCount(peopleCount: String)

    fun setPostCount(postCount: String)

    fun setBoardTitle(boardTitle: String)

    fun showLock(showLock: Boolean)

    fun setupWithViewPager(viewPager: ViewPager, defaultSelection: Int)
}