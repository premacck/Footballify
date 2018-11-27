package life.plank.juna.zone.util.customview

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.generic_toolbar.view.*
import kotlinx.android.synthetic.main.layout_board_engagement.view.*
import kotlinx.android.synthetic.main.layout_private_board_tabs.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.interfaces.CustomViewListener
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup

class GenericToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), CustomViewListener, EngagementInfoTilesToolbar {

    private var isFollowing: Boolean = false

    private var listener: PublicBoardHeaderListener? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val rootView = View.inflate(context, R.layout.generic_toolbar, this)
        ButterKnife.bind(this, rootView)

        val array = context.obtainStyledAttributes(attrs, R.styleable.GenericToolbar)
        setBackgroundColor(array.getColor(R.styleable.GenericToolbar_backgroundColor, resources.getColor(R.color.transparent, null)))
        setTitle(array.getString(R.styleable.GenericToolbar_toolbarTitle))
        setBoardTitle(array.getString(R.styleable.GenericToolbar_boardTypeTitle))
        setLeagueLogo(array.getResourceId(R.styleable.GenericToolbar_logo, R.drawable.ic_board_beer))
        share_btn?.visibility = if (array.getInt(R.styleable.GenericToolbar_shareButtonVisibility, 0) == 0) View.VISIBLE else View.INVISIBLE
        options_menu?.visibility = if (array.getInt(R.styleable.GenericToolbar_optionsMenuVisibility, 0) == 0) View.VISIBLE else View.INVISIBLE
        following_button?.visibility = if (array.getInt(R.styleable.GenericToolbar_followingTextVisibility, 0) == 0) View.VISIBLE else View.INVISIBLE
        info_tiles_tab_layout?.visibility = if (array.getInt(R.styleable.GenericToolbar_followingTextVisibility, 0) == 0) View.VISIBLE else View.INVISIBLE
        drag_handle?.visibility = if (array.getInt(R.styleable.GenericToolbar_dragHandleVisibility, 0) == 0) View.VISIBLE else View.INVISIBLE
        showLock(array.getBoolean(R.styleable.GenericToolbar_isLockVisible, false))
        array.recycle()
    }

    fun setUpPrivateBoardPopUp(activity: Activity, popupType: String, deleteListener: View.OnClickListener) {
        options_menu?.setOnClickListener { view ->
            val location = IntArray(2)

            view.getLocationOnScreen(location)

            //Initialize the Point with x, and y positions
            val point = Point()
            point.x = location[0]
            point.y = location[1]
            showOptionPopup(activity, point, popupType, null, -400, 100, deleteListener)
        }
    }

    override fun initListeners(fragment: Fragment) {
        if (fragment is PublicBoardHeaderListener) {
            listener = fragment
        } else
            throw IllegalStateException("Fragment must implement PublicBoardHeaderListener")
    }

    override fun initListeners(activity: Activity) {
        if (activity is PublicBoardHeaderListener) {
            listener = activity
        } else
            throw IllegalStateException("Activity must implement PublicBoardHeaderListener")
    }


    override fun dispose() {
        listener = null
        following_button?.setOnClickListener(null)
        options_menu?.setOnClickListener(null)
    }

    fun setTitle(title: String?) {
        this.title?.text = title
    }

    override fun setLeagueLogo(logoUrl: String) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override(getDp(30f).toInt(), getDp(30f).toInt())
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(logo!!)
    }

    override fun setLeagueLogo(@DrawableRes resource: Int) {
        logo?.setImageResource(resource)
    }

    override fun setPeopleCount(peopleCount: String) {
        people_count?.text = peopleCount
    }

    override fun setCommentCount(commentsCount: String) {
        comment_count?.text = commentsCount
    }

    override fun setBoardTitle(boardTitle: String) {
        board_type_title?.text = boardTitle
    }

    fun setBoardTitle(@StringRes boardTitle: Int) {
        board_type_title?.setText(boardTitle)
    }

    override fun showLock(showLock: Boolean) {
        lock?.visibility = if (showLock) View.VISIBLE else View.GONE
    }

    override fun getInfoTilesTabLayout(): TabLayout {
        return info_tiles_tab_layout
    }

    override fun setupWithViewPager(viewPager: ViewPager, defaultSelection: Int) {
        info_tiles_tab_layout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(defaultSelection, true)
        info_tiles_tab_layout.getTabAt(defaultSelection)?.select()
    }

    fun setupForPreview() {
        share_btn?.visibility = View.INVISIBLE
        options_menu?.visibility = View.INVISIBLE
        following_button?.visibility = View.INVISIBLE
        info_tiles_tab_layout?.visibility = View.INVISIBLE
    }
}