@file:JvmName("BoomMenuUtil")

package life.plank.juna.zone.util.view

import android.app.Activity
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.AppConstants.BoomMenuPage.*
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.customview.BoomMenu
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.activity.PostCommentActivity
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity
import life.plank.juna.zone.view.activity.camera.UploadActivity
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.activity.profile.UserProfileActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find

fun Activity.setupBoomMenu(@BoomMenuPage page: Int, boardId: String?, bottomSheetBehaviour: BottomSheetBehavior<*>? = null) {
    boomMenu().setup(page, this, boardId, bottomSheetBehaviour)
}

fun Fragment.setupBoomMenu(@BoomMenuPage page: Int, activity: Activity, boardId: String?, bottomSheetBehaviour: BottomSheetBehavior<*>? = null) {
    boomMenu().setup(page, activity, boardId, bottomSheetBehaviour)
}

fun BoomMenu.setup(@BoomMenuPage page: Int, activity: Activity, boardId: String?, bottomSheetBehaviour: BottomSheetBehavior<*>?) {
    val baseElevation: Float = getDp(22f)

    get().run {
        setIcon(
                R.drawable.ic_un,
                R.drawable.ic_close_white
        )
        setDuration(200)
        elevation = baseElevation
        setMinRadius(130)
        if (page == BOOM_MENU_SETTINGS_AND_HOME) {
            setArc(240f, 300f)
        }
        if (page == BOOM_MENU_HOME) {
            setArc(270f, 275f)
        }
    }
    val fabImages: IntArray? = getBoomMenuFabImages(page)
    val backgroundColors: IntArray? = getBoomMenuBackgroundColors(page)
    val titles: Array<String>? = getBoomMenuTitles(page)
    if (fabImages == null || backgroundColors == null || titles == null) {
        return
    }
    for (i in fabImages.indices) run {
        val arcMenuChild: View = activity.layoutInflater.inflate(R.layout.layout_floating_action_button, null)
        val fabRelativeLayout: RelativeLayout = arcMenuChild.findViewById(R.id.fab_relative_layout)
        val childImageView: ImageView = arcMenuChild.findViewById(R.id.fab_image_view)
        fabRelativeLayout.background = ContextCompat.getDrawable(activity, backgroundColors[i])
        childImageView.setImageResource(fabImages[i])
        get().addItem(arcMenuChild, titles[i], getBoomMenuListener(page, activity, boardId, i, bottomSheetBehaviour))
    }
}

fun BoomMenu.setupWith(nestedScrollView: NestedScrollView) {
    nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
        if (scrollY > oldScrollY) {
            hide()
        } else {
            show()
        }
    }
}

fun BoomMenu.setupWith(recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 5) {
                hide()
            } else if (dy < -5) {
                show()
            }
        }
    })
}

fun getBoomMenuTitles(@BoomMenuPage page: Int): Array<String>? {
    val resources: Resources = ZoneApplication.getContext().resources
    return when (page) {
        BOOM_MENU_FULL -> {
            resources.getStringArray(R.array.boom_menu_titles_full)
        }
        BOOM_MENU_BASIC_INTERACTION -> {
            resources.getStringArray(R.array.boom_menu_without_settings)
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            resources.getStringArray(R.array.boom_menu_titles_home_settings)
        }
        BOOM_MENU_HOME -> {
            resources.getStringArray(R.array.boom_menu_titles_home)
        }
        else -> {
            null
        }
    }
}

fun getBoomMenuFabImages(@BoomMenuPage page: Int): IntArray? {
    return when (page) {
        BOOM_MENU_FULL -> {
            intArrayOf(
                    R.drawable.ic_settings_white,
                    R.drawable.ic_home_purple,
                    R.drawable.ic_gallery_white,
                    R.drawable.ic_emoji_white,
                    R.drawable.ic_camera_white,
                    R.drawable.ic_text_white,
                    R.drawable.ic_mic_white
            )
        }
        BOOM_MENU_BASIC_INTERACTION -> {
            intArrayOf(
                    R.drawable.ic_gallery_white,
                    R.drawable.ic_camera_white,
                    R.drawable.ic_mic_white,
                    R.drawable.ic_link_white
            )
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            intArrayOf(
                    R.drawable.ic_settings_white,
                    R.drawable.ic_home_purple
            )
        }
        BOOM_MENU_HOME -> {
            intArrayOf(R.drawable.ic_home_purple)
        }
        else -> {
            null
        }
    }
}

fun getBoomMenuBackgroundColors(@BoomMenuPage page: Int): IntArray? {
    return when (page) {
        BOOM_MENU_FULL -> {
            intArrayOf(
                    R.drawable.fab_circle_background_grey,
                    R.drawable.fab_circle_background_white,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink
            )
        }
        BOOM_MENU_BASIC_INTERACTION -> {
            intArrayOf(
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink
            )
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            intArrayOf(R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white)
        }
        BOOM_MENU_HOME -> {
            intArrayOf(R.drawable.fab_circle_background_white)
        }
        else -> {
            null
        }
    }
}

fun getBoomMenuListener(@BoomMenuPage page: Int, activity: Activity, boardId: String?, position: Int, bottomSheetBehaviour: BottomSheetBehavior<*>?): View.OnClickListener? {
    return when (page) {
        BOOM_MENU_FULL -> {
            View.OnClickListener {
                when (position) {
                    0 -> {
                        if (isNullOrEmpty(getToken())) {
                            Toast.makeText(activity, R.string.login_signup_to_view_profile, Toast.LENGTH_SHORT).show()
                        } else {
                            UserProfileActivity.launch(activity)
                        }
                    }
                    1 -> {
                        if (activity is HomeActivity) return@OnClickListener
                        HomeActivity.launch(activity)
                        activity.finish()
                    }
                    2 -> {
                        if (boardId != null) {
                            UploadActivity.launch(activity, GALLERY, boardId)
                        }
                    }
                    3 -> {
                        if (boardId != null) {
                            if (bottomSheetBehaviour != null) {
                                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                                bottomSheetBehaviour.peekHeight = 350
                            }
                        }
                    }
                    4 -> {
                        if (boardId != null) {
                            CustomCameraActivity.launch(activity, false, boardId, true)
                        }
                    }
                    5 -> {
                        if (boardId != null) {
                            PostCommentActivity.launch(activity, boardId)
                        }
                    }
                    6 -> {
                        if (boardId != null) {
                            UploadActivity.launch(activity, AUDIO, boardId)
                        }
                    }
                }
            }
        }
        BOOM_MENU_BASIC_INTERACTION -> {
            View.OnClickListener {
                when (position) {
                    0 -> {
                        UploadActivity.launch(activity, GALLERY, "")
                    }
                    1 -> {
                        CustomCameraActivity.launch(activity, true, "", true)
                    }
                    2 -> {
                        UploadActivity.launch(activity, AUDIO, "")
                    }
                    3 ->{
                        PostCommentActivity.launch(activity, "")
                    }
                }
            }
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            View.OnClickListener {
                when (position) {
                    0 -> {
                        if (isNullOrEmpty(getToken())) {
                            Toast.makeText(activity, R.string.login_signup_to_view_profile, Toast.LENGTH_SHORT).show()
                        } else {
                            UserProfileActivity.launch(activity)
                        }
                    }
                    1 -> {
                        if (activity is HomeActivity) return@OnClickListener
                        HomeActivity.launch(activity)
                        activity.finish()
                    }
                }
            }
        }
        BOOM_MENU_HOME -> {
            View.OnClickListener {
                if (activity is HomeActivity) return@OnClickListener
                HomeActivity.launch(activity)
                activity.finish()
            }
        }
        else -> null
    }
}

fun Activity.boomMenu(): BoomMenu = find(R.id.boom_menu)

fun Fragment.boomMenu(): BoomMenu = find(R.id.boom_menu)

fun Activity.dismissBoomMenuIfOpen(): Boolean {
    findViewById<BoomMenu>(R.id.boom_menu)?.run {
        if (isOpen()) {
            toggleBoomMenu()
            return false
        }
        return true
    }
    return true
}

fun Fragment.dismissBoomMenuIfOpen(): Boolean {
    view?.findViewById<BoomMenu>(R.id.boom_menu)?.run {
        if (isOpen()) {
            toggleBoomMenu()
            return false
        }
        return true
    }
    return true
}