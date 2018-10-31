package life.plank.juna.zone.view.activity.camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_custom_camera.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture
import life.plank.juna.zone.view.activity.base.StackableCardActivity
import life.plank.juna.zone.view.fragment.camera.CameraFragment
import life.plank.juna.zone.view.fragment.camera.CustomGalleryFragment

class CustomCameraActivity : StackableCardActivity() {

    var isForImage: Boolean = false
    lateinit var boardId: String
    private lateinit var pagerAdapter: CameraPagerAdapter

    companion object {
        fun launch(from: Activity, isForImage: Boolean, boardId: String) {
            val intent = Intent(from, CustomCameraActivity::class.java)
            intent.putExtra(ZoneApplication.getContext().getString(R.string.intent_is_camera_for_image), isForImage)
            intent.putExtra(from.getString(R.string.intent_board_id), boardId)
            from.startActivity(intent)
            from.overridePendingTransition(R.anim.float_up, R.anim.sink_up)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_camera)
        ButterKnife.bind(this)
        val intent = intent ?: return
        isForImage = intent.getBooleanExtra(getString(R.string.intent_is_camera_for_image), true)
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))

        setupSwipeGesture(this, drag_area, root_card, null)
        setupViewPager()
    }

    private fun setupViewPager() {
        pagerAdapter = CameraPagerAdapter(supportFragmentManager, boardId, isForImage)
        camera_view_pager.adapter = pagerAdapter
    }

    class CameraPagerAdapter(fm: FragmentManager, val boardId: String, private val isForImage: Boolean) : FragmentPagerAdapter(fm) {

        var currentFragment: Fragment? = null

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CameraFragment.newInstance(boardId)
                else -> CustomGalleryFragment.newInstance(isForImage)
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

    override fun getScreenshotLayout(): View {
        return root_card
    }

    override fun onBackPressed() {
        if (pagerAdapter.currentFragment is CustomGalleryFragment) {
            camera_view_pager.currentItem = 0
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.float_down, R.anim.sink_down)
        }
    }
}