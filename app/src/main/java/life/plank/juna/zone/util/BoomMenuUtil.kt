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
import life.plank.juna.zone.view.activity.CameraActivity
import life.plank.juna.zone.view.activity.PostCommentActivity
import life.plank.juna.zone.view.activity.UserFeedActivity
import life.plank.juna.zone.view.activity.UserProfileActivity

fun setupBoomMenu(@BoomMenuPage page: Int, activity: Activity, boardId: String?, arcMenu: ArcMenu) {
    val baseElevation: Float = getDp(22f)
    arcMenu.run {
        setIcon(
                R.drawable.ic_un,
                R.drawable.ic_close_white
        )
        setDuration(200)
        elevation = baseElevation
    }
    val fabImages: IntArray? = getBoomMenuFabImages(page)
    val backgroundColors: IntArray? = getBoomMenuBackgroundColors(page)
    val titles: Array<String>? = getBoomMenuTitles(page)
    if (fabImages == null || backgroundColors == null || titles == null) {
        return
    }
    for (i in fabImages.indices) run {
        val arcMenuChild: View = activity.layoutInflater.inflate(R.layout.layout_floating_action_button, null)
        val fabRelativeLayout: RelativeLayout = arcMenuChild.findViewById(R.id.fab_relative_layout);
        val childImageView: ImageView = arcMenuChild.findViewById(R.id.fab_image_view)
        fabRelativeLayout.background = ContextCompat.getDrawable(activity, backgroundColors[i])
        childImageView.setImageResource(fabImages[i])
        arcMenu.addItem(arcMenuChild, titles[i], getBoomMenuListener(page, activity, boardId, i))
    }
}

fun getBoomMenuTitles(@BoomMenuPage page: Int) : Array<String>? {
    val resources: Resources = ZoneApplication.getContext().resources
    return when(page) {
        BOOM_HOME_PAGE, BOOM_BOARD_TILES_PAGE, BOOM_PRIVATE_BOARD_INFO_PAGE -> {
            resources.getStringArray(R.array.boom_menu_titles_full)
        }
        BOOM_ZONE_PAGE, BOOM_LEAGUE_PAGE -> {
            resources.getStringArray(R.array.boom_menu_titles_home_settings)
        }
        BOOM_SETTINGS_PAGE -> {
            resources.getStringArray(R.array.boom_menu_titles_home)
        }
        else -> { null }
    }
}

fun getBoomMenuFabImages(@BoomMenuPage page: Int) : IntArray? {
    return when(page) {
        BOOM_HOME_PAGE, BOOM_BOARD_TILES_PAGE, BOOM_PRIVATE_BOARD_INFO_PAGE -> {
            intArrayOf(
                    R.drawable.ic_settings_white,
                    R.drawable.ic_home_purple,
                    R.drawable.ic_gallery,
                    R.drawable.ic_camera_white,
                    R.drawable.ic_video,
                    R.drawable.text_icon,
                    R.drawable.ic_mic,
                    R.drawable.ic_link
            )
        }
        BOOM_ZONE_PAGE, BOOM_LEAGUE_PAGE -> {
            intArrayOf(R.drawable.ic_settings_white, R.drawable.ic_home_purple)
        }
        BOOM_SETTINGS_PAGE -> {
            intArrayOf(R.drawable.ic_home_purple)
        }
        else -> { null }
    }
}

fun getBoomMenuBackgroundColors(@BoomMenuPage page: Int) : IntArray? {
    return when(page) {
        BOOM_HOME_PAGE, BOOM_BOARD_TILES_PAGE, BOOM_PRIVATE_BOARD_INFO_PAGE -> {
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
        BOOM_ZONE_PAGE, BOOM_LEAGUE_PAGE -> {
            intArrayOf(R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white)
        }
        BOOM_SETTINGS_PAGE -> { intArrayOf(R.drawable.fab_circle_background_white) }
        else -> { null }
    }
}

fun getBoomMenuListener(@BoomMenuPage page: Int, activity: Activity, boardId: String?, position: Int) : View.OnClickListener? {
    return when(page) {
        BOOM_HOME_PAGE, BOOM_BOARD_TILES_PAGE, BOOM_PRIVATE_BOARD_INFO_PAGE -> {
            View.OnClickListener {
                when(position) {
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
                        if (boardId != null && isInteractionPage(page)) {
                            CameraActivity.launch(activity, GALLERY, boardId, activity.getString(R.string.intent_board_activity))
                        }
                    }
                    3 -> {
                        if (boardId != null && isInteractionPage(page)) {
                            CameraActivity.launch(activity, IMAGE, boardId, activity.getString(R.string.intent_board_activity))
                        }
                    }
                    4 -> {
                        if (boardId != null && isInteractionPage(page)) {
                            CameraActivity.launch(activity, VIDEO, boardId, activity.getString(R.string.intent_board_activity))
                        }
                    }
                    5 -> {
                        if (boardId != null) {
                            PostCommentActivity.launch(activity, boardId)
                        }
                    }
                    6 -> {
                        if (boardId != null && isInteractionPage(page)) {
                            CameraActivity.launch(activity, AUDIO, boardId, activity.getString(R.string.intent_board_activity))
                        }
                    }
                }
            }
        }
        BOOM_ZONE_PAGE, BOOM_LEAGUE_PAGE -> {
            View.OnClickListener {
                when(position) {
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
        BOOM_SETTINGS_PAGE -> {
            View.OnClickListener {
                UserFeedActivity.launch(activity, true)
                activity.finish()
            }
        }
        else -> null
    }
}

fun isInteractionPage(@BoomMenuPage page: Int) : Boolean {
    return page == BOOM_BOARD_TILES_PAGE || page == BOOM_PRIVATE_BOARD_INFO_PAGE
}