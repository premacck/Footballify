package life.plank.juna.zone.util.customview

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.*
import android.util.*
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.prembros.facilis.util.onDebouncingClick
import kotlinx.android.synthetic.main.layout_board_engagement.view.*
import kotlinx.android.synthetic.main.public_board_toolbar.view.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.MatchDetails
import life.plank.juna.zone.interfaces.*
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.common.JunaDataUtil.*
import life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup
import life.plank.juna.zone.util.time.*
import life.plank.juna.zone.util.time.DateUtil.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import java.util.*

class PublicBoardToolbar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr), CustomViewListener, EngagementInfoTilesToolbar {

    private var layoutRefreshState = -1

    private var matchTimeValue: Int = 0
    private var lastStopTime: Long = 0
    private var baseTime: Long = 0
    private lateinit var matchDetails: MatchDetails
    private var countDownTimer: CountDownTimer? = null
    private var listener: BoardHeaderListener? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val rootView = View.inflate(context, R.layout.public_board_toolbar, this)
        ButterKnife.bind(this, rootView)
        val array = context.obtainStyledAttributes(attrs, R.styleable.PublicBoardToolbar)

        val initWithDefaults = array.getBoolean(R.styleable.PublicBoardToolbar_useDefaults, true)
        if (initWithDefaults) {
            initWithDefaults(context)
            showLock(array.getBoolean(R.styleable.PublicBoardToolbar_showLock, false))
            return
        }
        setScore(array.getString(R.styleable.PublicBoardToolbar_score)?.run { this } ?: "0 - 0")
        setLeagueLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0))
        setTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0), home_team_logo)
        setTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0), visiting_team_logo)
        setPeopleCount(array.getString(R.styleable.PublicBoardToolbar_peopleCount)!!)
        setPostCount(array.getString(R.styleable.PublicBoardToolbar_postCount)!!)
        setBoardTitle(array.getString(R.styleable.PublicBoardToolbar_boardTitle)!!)
        showLock(array.getBoolean(R.styleable.PublicBoardToolbar_showLock, false))

        array.recycle()
    }

    fun prepare(matchDetails: MatchDetails, @DrawableRes leagueLogo: Int) {
        this.matchDetails = matchDetails
        setLeagueLogo(leagueLogo)
        setTeamLogo(matchDetails.homeTeam.logoLink, home_team_logo)
        setTeamLogo(matchDetails.awayTeam.logoLink, visiting_team_logo)
        setScore(getSeparator(matchDetails, win_pointer, true))
        setBoardTitle(ZoneApplication.getContext().getString(R.string.matchday_) + matchDetails.matchDay)
        when (getMatchTimeValue(matchDetails.matchStartTime, false)) {
            MATCH_PAST, MATCH_COMPLETED_TODAY -> {
                setFullTimeStatus()
                matchTimeValue = MATCH_PAST
            }
            MATCH_LIVE -> {
                setLiveTimeStatus(matchDetails.matchStartTime, matchDetails.timeStatus)
                matchTimeValue = MATCH_LIVE
            }
            MATCH_ABOUT_TO_START, MATCH_ABOUT_TO_START_BOARD_ACTIVE, MATCH_SCHEDULED_TODAY -> {
                setScheduledTimeStatus(true)
                setTodayMatchCountdown()
                matchTimeValue = MATCH_ABOUT_TO_START
            }
            MATCH_SCHEDULED_LATER -> {
                setScheduledTimeStatus(false)
                matchTimeValue = MATCH_SCHEDULED_LATER
            }
        }
    }

    fun setUpPopUp(activity: Activity, currentMatchId: Long?) {
        options_menu.setOnClickListener { view ->
            val location = IntArray(2)

            view.getLocationOnScreen(location)

            //Initialize the Point with x, and y positions
            val point = Point()
            point.x = location[0]
            point.y = location[1]
            showOptionPopup(activity, point, BOARD_POPUP, currentMatchId, -400, 100)
        }
    }

    override fun initListeners(fragment: Fragment) {
        if (fragment is BoardHeaderListener) {
            listener = fragment
        } else
            throw IllegalStateException("Fragment must implement PublicBoardHeaderListener")

        addInfoTilesListener()
    }

    override fun initListeners(activity: Activity) {
        if (activity is BoardHeaderListener) {
            listener = activity
        } else
            throw IllegalStateException("Activity must implement PublicBoardHeaderListener")

        addInfoTilesListener()
    }

    private fun addInfoTilesListener() {
        share_btn.onDebouncingClick { listener?.onShareClick() }

        when (matchTimeValue) {
            MATCH_LIVE -> {
            }
            MATCH_ABOUT_TO_START -> setScheduledTimeStatus(true)
            MATCH_SCHEDULED_LATER -> setScheduledTimeStatus(false)
        }
    }

    private fun initWithDefaults(context: Context) {
        league_logo.setImageResource(R.drawable.img_epl_logo)
        home_team_logo.setImageResource(R.drawable.ic_arsenal_logo)
        visiting_team_logo.setImageResource(R.drawable.ic_blackpool_logo)
        score_text_view.text = context.getString(R.string._8_8)
    }

    override fun dispose() {
        listener = null
        options_menu.setOnClickListener(null)
        when (matchTimeValue) {
            MATCH_LIVE -> {
                time_status.stop()
                lastStopTime = SystemClock.elapsedRealtime() - (Date().time - matchDetails.matchStartTime.time)
            }
            MATCH_ABOUT_TO_START, MATCH_SCHEDULED_LATER -> resetCountDownTimer()
        }
    }

    fun setScore(score: String) {
        if (score_layout.paddingTop > 0) {
            score_layout.setPadding(0, 0, 0, 0)
        }
        score_text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32f)
        score_text_view.text = score
    }

    /**
     * Time status setter for past matches
     */
    private fun setFullTimeStatus() {
        this.time_status.text = FULL_TIME_LOWERCASE
    }

    /**
     * Time status setter for live matches
     */
    fun setLiveTimeStatus(matchStartTime: Date, timeStatus: String?) {
        when (timeStatus) {
            NS, LIVE -> {
                resetCountDownTimer()
                baseTime = Date().time - matchStartTime.time
                time_status.setOnChronometerTickListener {
                    baseTime += 1000
                    time_status.text = getFootballTimeElapsed(baseTime)
                }
                time_status.start()
            }
            else -> {
                time_status.stop()
                time_status.text = getDisplayTimeStatus(timeStatus)
            }
        }
    }

    /**
     * Time status setter for live matches
     */
    private fun setTodayMatchCountdown() {
        val timeDiffFromNow = Math.abs(matchDetails.matchStartTime.time - Date().time)
        resetCountDownTimer()
        DateUtil.HOUR_MINUTE_SECOND_DATE_FORMAT.timeZone = TimeZone.getTimeZone(GMT)
        countDownTimer = object : CountDownTimer(timeDiffFromNow, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time_status.text = getHourMinuteSecondFormatDate(Date(millisUntilFinished))
                if (millisUntilFinished in (ONE_HOUR_MILLIS + 1)..FOUR_HOURS_MILLIS && layoutRefreshState == -1) {
                    layoutRefreshState = MATCH_ABOUT_TO_START_BOARD_ACTIVE
                    listener?.onMatchTimeStateChange()
                } else if (millisUntilFinished <= ONE_HOUR_MILLIS && layoutRefreshState == MATCH_ABOUT_TO_START_BOARD_ACTIVE) {
                    layoutRefreshState = MATCH_ABOUT_TO_START
                    listener?.onMatchTimeStateChange()
                }
            }

            override fun onFinish() {
                time_status.text = LIVE
                matchDetails.timeStatus = LIVE
                setLiveTimeStatus(matchDetails.matchStartTime, matchDetails.timeStatus)
                setScore(getSeparator(matchDetails, win_pointer, true))
                listener?.onMatchTimeStateChange()
            }
        }
        countDownTimer?.start()
    }

    /**
     * Time status setter for scheduled matches
     */
    private fun setScheduledTimeStatus(isScheduledToday: Boolean) {
        win_pointer.visibility = View.GONE
        score_text_view.visibility = if (isScheduledToday) View.VISIBLE else View.GONE
        score_text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, (if (isScheduledToday) 10 else 32).toFloat())
        score_layout.setPadding(0, getDp(9f).toInt(), 0, 0)
        val scheduledMatchDateString = getScheduledMatchDateString(matchDetails.matchStartTime)
        if (isScheduledToday) {
//            set time of match in score view and set countdown in time status view
            score_text_view.text = scheduledMatchDateString
        } else {
//            set time of match in time status view
            time_status.text = scheduledMatchDateString
        }
    }

    private fun resetCountDownTimer() {
        countDownTimer?.cancel()
    }

    override fun setLeagueLogo(logoUrl: String) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override(getDp(30f).toInt(), getDp(30f).toInt())
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(league_logo)
    }

    override fun setLeagueLogo(@DrawableRes resource: Int) {
        if (resource != 0) league_logo.setImageDrawable(findDrawable(resource))
    }

    private fun setTeamLogo(logoUrl: String, logoImageView: ImageView) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override(getDp(30f).toInt(), getDp(30f).toInt())
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(logoImageView)
    }

    private fun setTeamLogo(@DrawableRes resource: Int, logoImageView: ImageView) {
        if (resource != 0) logoImageView.setImageResource(resource)
    }

    override fun setPeopleCount(peopleCount: String) {
        people_count.text = peopleCount
    }

    override fun setPostCount(postCount: String) {
        post_count.text = postCount
    }

    override fun setBoardTitle(boardTitle: String) {}

    override fun showLock(showLock: Boolean) {}

    override fun getInfoTilesTabLayout(): TabLayout {
        return info_tiles_tab_layout
    }

    override fun setupWithViewPager(viewPager: ViewPager, defaultSelection: Int) {
        info_tiles_tab_layout.setupWithViewPager(viewPager)
        viewPager.setCurrentItem(defaultSelection, true)
    }

//    TODO: un-comment if required
/*    fun setBoardTemperature(boardTemperature: BoardTemperature) {
        people_count.text = NumberFormatter.format(boardTemperature.userCount)
        post_count.text = NumberFormatter.format(boardTemperature.postCount)
        interaction_count.text = NumberFormatter.format(boardTemperature.interactionCount)
    }*/
}