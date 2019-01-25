package life.plank.juna.zone.ui.common

import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

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