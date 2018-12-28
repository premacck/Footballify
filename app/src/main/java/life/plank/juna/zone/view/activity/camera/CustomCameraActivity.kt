package life.plank.juna.zone.view.activity.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.*
import kotlinx.android.synthetic.main.activity_custom_camera.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.view.UIDisplayUtil.setupSwipeGesture
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.fragment.camera.*

class CustomCameraActivity : BaseJunaCardActivity() {

    private var isBoard: Boolean = true
    lateinit var boardId: String
    private lateinit var pagerAdapter: CameraPagerAdapter

    companion object {
        fun launch(from: Activity, boardId: String, isBoard: Boolean) {
            val intent = Intent(from, CustomCameraActivity::class.java)
            intent.putExtra(from.getString(R.string.intent_board_id), boardId)
            intent.putExtra(from.getString(R.string.intent_is_board), isBoard)
            from.startActivity(intent)
            from.overridePendingTransition(R.anim.float_up, R.anim.sink_up)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera)
        val intent = intent ?: return
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        isBoard = intent.getBooleanExtra(getString(R.string.intent_is_board), true)
        setupSwipeGesture(this, drag_area, root_card, null)
        setupViewPager()
    }

    private fun setupViewPager() {
        pagerAdapter = CameraPagerAdapter(supportFragmentManager, boardId, isBoard)
        camera_view_pager.adapter = pagerAdapter
    }

    class CameraPagerAdapter(fm: FragmentManager, val boardId: String, private val isBoard: Boolean) : FragmentPagerAdapter(fm) {

        var currentFragment: Fragment? = null

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CameraFragment.newInstance(boardId, isBoard)
                else -> CustomGalleryFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            if (currentFragment !== `object`) {
                currentFragment = `object` as Fragment
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container

    override fun restApi(): RestApi? = null

    override fun onBackPressed() {
        if (pagerAdapter.currentFragment is CustomGalleryFragment) {
            camera_view_pager.currentItem = 0
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.float_down, R.anim.sink_down)
        }
    }
}