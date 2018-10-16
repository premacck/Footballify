@file:JvmName("BoomMenuUtil")

package life.plank.juna.zone.util

import android.app.Activity
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.bvapp.arcmenulibrary.ArcMenu
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.AppConstants.BoomMenuPage.*
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager.getToken
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.view.activity.PostCommentActivity
import life.plank.juna.zone.view.activity.UserFeedActivity
import life.plank.juna.zone.view.activity.UserProfileActivity
import life.plank.juna.zone.view.activity.camera.CustomCameraActivity
import life.plank.juna.zone.view.activity.camera.UploadActivity

fun setupBoomMenu(@BoomMenuPage page: Int, activity: Activity, boardId: String?, arcMenu: ArcMenu) {
    val baseElevation: Float = getDp(22f)
    arcMenu.run {
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
        arcMenu.addItem(arcMenuChild, titles[i], getBoomMenuListener(page, activity, boardId, i))
    }
}

fun getBoomMenuTitles(@BoomMenuPage page: Int): Array<String>? {
    val resources: Resources = ZoneApplication.getContext().resources
    return when (page) {
        BOOM_MENU_FULL -> {
            resources.getStringArray(R.array.boom_menu_titles_full)
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            resources.getStringArray(R.array.boom_menu_titles_home_settings)
        }
        BOOM_MENU_HOME -> {
            resources.getStringArray(R.array.boom_menu_titles_home)
        }
        else -> { null }
    }
}

fun getBoomMenuFabImages(@BoomMenuPage page: Int): IntArray? {
    return when (page) {
        BOOM_MENU_FULL -> {
            intArrayOf(
                    R.drawable.ic_settings_white,
                    R.drawable.ic_home_purple,
                    R.drawable.ic_gallery_white,
                    R.drawable.ic_camera_white,
                    R.drawable.ic_video_white,
                    R.drawable.ic_text_white,
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
        else -> { null }
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
                    R.drawable.fab_circle_background_pink,
                    R.drawable.fab_circle_background_pink
            )
        }
        BOOM_MENU_SETTINGS_AND_HOME -> {
            intArrayOf(R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white)
        }
        BOOM_MENU_HOME -> { intArrayOf(R.drawable.fab_circle_background_white) }
        else -> { null }
    }
}

fun getBoomMenuListener(@BoomMenuPage page: Int, activity: Activity, boardId: String?, position: Int) : View.OnClickListener? {
    return when(page) {
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
                        UserFeedActivity.launch(activity, true)
                        activity.finish()
                    }
                    2 -> {
                        if (boardId != null) {
                            UploadActivity.launch(activity, GALLERY, boardId)
                        }
                    }
                    3 -> {
                        if (boardId != null) {
                            CustomCameraActivity.launch(activity, true, boardId)
                        }
                    }
                    4 -> {
                        if (boardId != null) {
                            CustomCameraActivity.launch(activity, false, boardId)
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
                        UserFeedActivity.launch(activity, true)
                        activity.finish()
                    }
                }
            }
        }
        BOOM_MENU_HOME -> {
            View.OnClickListener {
                UserFeedActivity.launch(activity, true)
                activity.finish()
            }
        }
        else -> null
    }
}