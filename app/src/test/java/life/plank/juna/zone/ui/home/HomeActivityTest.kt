package life.plank.juna.zone.ui.home

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.*
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config
class HomeActivityTest {

    private var homeActivity: HomeActivity? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        homeActivity = Robolectric.buildActivity(HomeActivity::class.java).create().get()
    }
}