package life.plank.juna.zone.interfaces

import androidx.annotation.DrawableRes
import com.google.android.material.tabs.TabLayout

interface EngagementInfoTilesToolbar {

    fun getInfoTilesTabLayout(): TabLayout

    fun setLeagueLogo(logoUrl: String)

    fun setLeagueLogo(@DrawableRes resource: Int)

    fun setPeopleCount(peopleCount: String)

    fun setPostCount(postCount: String)

    fun setBoardTitle(boardTitle: String)

    fun showLock(showLock: Boolean)

    fun setupWithViewPager(viewPager: androidx.viewpager.widget.ViewPager, defaultSelection: Int)
}