package life.plank.juna.zone.ui.home

import org.junit.*
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

    @Test
    @Throws(Exception::class)
    fun shouldNotBeNull() = assert(homeActivity != null)

    @Test
    @Throws(Exception::class)
    fun shouldHaveHomeFragment() = assert(homeActivity?.supportFragmentManager?.findFragmentByTag("HomeFragment1") != null)
}