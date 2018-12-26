package life.plank.juna.zone.util.customview

import android.app.Activity
import android.content.Context
import android.util.*
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.zone_tool_bar.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.interfaces.*

class ZoneToolBar @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0,
                                            defStyleRes: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr, defStyleRes), CustomViewListener {

    private var listener: ZoneToolbarListener? = null

    var title: String?
        get() = toolbar_title.text.toString()
        set(title) {
            this.toolbar_title.text = title
        }

    var userGreeting: String
        get() = toolbar_user_greeting.text.toString()
        set(title) {
            this.toolbar_user_greeting.text = title
        }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.zone_tool_bar, this)

        val array = context.obtainStyledAttributes(attrs, R.styleable.ZoneToolBar)
        notification_badge?.visibility = if (array.getInt(R.styleable.ZoneToolBar_notificationBadgeVisibility, 1) == 1) View.INVISIBLE else View.VISIBLE
        try {
            title = array?.getString(R.styleable.ZoneToolBar_title)
            setProfilePic(array.getResourceId(R.styleable.ZoneToolBar_profilePic, R.drawable.ic_default_profile))
        } catch (e: Exception) {
            Log.e("ZoneToolBar", e.message)
        } finally {
            array?.recycle()
        }
    }

    fun showNotificationBadge(showNotificationBadge: Boolean) {
        notification_badge?.visibility = if (showNotificationBadge) View.VISIBLE else View.GONE
    }

    fun isNotificationViewVisible(visibility: Int) {
        toolbar_notification.visibility = visibility
    }

    fun setProfilePic(url: String) {
        if (isNullOrEmpty(url)) {
            setProfilePic(R.drawable.ic_default_profile)
            return
        }
        Glide.with(this).load(url).into(toolbar_profile_pic)
    }

    fun setProfilePic(@DrawableRes drawableRes: Int) {
        toolbar_profile_pic.setImageResource(drawableRes)
    }

    override fun initListeners(fragment: Fragment) {
        if (fragment is ZoneToolbarListener) {
            listener = fragment
        } else
            throw IllegalStateException("Fragment must implement ZoneToolbarListener")

        addToolbarListener()
    }

    override fun initListeners(activity: Activity) {
        if (activity is ZoneToolbarListener) {
            listener = activity
        } else
            throw IllegalStateException("Activity must implement ZoneToolbarListener")

        addToolbarListener()
    }

    private fun addToolbarListener() {
        toolbar_profile_pic.onDebouncingClick { listener?.profilePictureClicked(toolbar_profile_pic) }
        toolbar_notification.onDebouncingClick { listener?.notificationIconClicked(toolbar_notification) }
    }

    override fun dispose() {
        toolbar_profile_pic.setOnClickListener(null)
        toolbar_notification.setOnClickListener(null)
    }
}