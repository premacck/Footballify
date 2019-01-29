package life.plank.juna.zone.ui.home

import androidx.fragment.app.testing.launchFragmentInContainer
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config
class HomeFragmentTest {

    private var homeFragment: HomeFragment? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val fragmentScenario = launchFragmentInContainer<HomeFragment>()
//        homeFragment = SupportFragmentController.of<HomeFragment>(HomeFragment.newInstance()).create().start().resume().get()
    }

    @Test
    @Throws(Exception::class)
    fun shouldBeNotNull() = assert(homeFragment != null)
}