package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.BoardTemperature;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.common.NumberFormatter;
import life.plank.juna.zone.util.time.DateUtil;

import static life.plank.juna.zone.util.common.AppConstants.BOARD_POPUP;
import static life.plank.juna.zone.util.common.AppConstants.FOUR_HOURS_MILLIS;
import static life.plank.juna.zone.util.common.AppConstants.FULL_TIME_LOWERCASE;
import static life.plank.juna.zone.util.common.AppConstants.GMT;
import static life.plank.juna.zone.util.common.AppConstants.LIVE;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START_BOARD_ACTIVE;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_PAST;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_SCHEDULED_LATER;
import static life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.MATCH_SCHEDULED_TODAY;
import static life.plank.juna.zone.util.common.AppConstants.NS;
import static life.plank.juna.zone.util.common.AppConstants.ONE_HOUR_MILLIS;
import static life.plank.juna.zone.util.common.DataUtil.getDisplayTimeStatus;
import static life.plank.juna.zone.util.common.DataUtil.getSeparator;
import static life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup;
import static life.plank.juna.zone.util.time.DateUtil.getHourMinuteSecondFormatDate;
import static life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue;
import static life.plank.juna.zone.util.time.DateUtil.getScheduledMatchDateString;
import static life.plank.juna.zone.util.time.DateUtil.getTimeInFootballFormat;
import static life.plank.juna.zone.util.view.UIDisplayUtil.getDp;

public class PublicBoardToolbar extends Toolbar implements CustomViewListener, EngagementInfoTilesToolbar {

    @BindView(R.id.logo)
    ImageView leagueLogoView;
    @BindView(R.id.score_layout)
    LinearLayout scoreLayout;
    @BindView(R.id.score)
    TextView scoreView;
    @BindView(R.id.time_status)
    Chronometer timeStatusView;
    @BindView(R.id.win_pointer)
    ImageView winPointer;
    @BindView(R.id.home_team_logo)
    ImageView homeTeamLogoView;
    @BindView(R.id.visiting_team_logo)
    ImageView visitingTeamLogoView;
    @BindView(R.id.options_menu)
    ImageButton optionsMenu;
    @BindView(R.id.share_btn)
    ImageButton shareBtn;
    @BindView(R.id.people_count)
    TextView peopleCountView;
    @BindView(R.id.post_count)
    TextView postCountView;
    @BindView(R.id.interaction_count)
    TextView interactionCountView;
    @BindView(R.id.info_tiles_tab_layout)
    TabLayout infoTilesTabLayout;

    private boolean isFavourite;
    private boolean isNotificationOn;
    private boolean isFollowing;
    private int layoutRefreshState = -1;

    private int matchTimeValue;
    private long lastStopTime;
    private long baseTime;
    private MatchDetails matchDetails;
    private CountDownTimer countDownTimer;
    private PublicBoardHeaderListener listener;

    public PublicBoardToolbar(Context context) {
        this(context, null);
    }

    public PublicBoardToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublicBoardToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.public_board_toolbar, this);
        ButterKnife.bind(this, rootView);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PublicBoardToolbar);

        boolean initWithDefaults = array.getBoolean(R.styleable.PublicBoardToolbar_useDefaults, true);
        if (initWithDefaults) {
            initWithDefaults(context);
            showLock(array.getBoolean(R.styleable.PublicBoardToolbar_showLock, false));
            return;
        }
        setScore(array.getString(R.styleable.PublicBoardToolbar_score));
        setLeagueLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0));
        setHomeTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0));
        setVisitingTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, 0));
        setPeopleCount(array.getString(R.styleable.PublicBoardToolbar_peopleCount));
        setPostCount(array.getString(R.styleable.PublicBoardToolbar_postCount));
        setBoardTitle(array.getString(R.styleable.PublicBoardToolbar_boardTitle));
        showLock(array.getBoolean(R.styleable.PublicBoardToolbar_showLock, false));

        array.recycle();
    }

    public void setUpPopUp(Activity activity, Long currentMatchId) {
        optionsMenu.setOnClickListener(view -> {
            int[] location = new int[2];

            view.getLocationOnScreen(location);

            //Initialize the Point with x, and y positions
            Point point = new Point();
            point.x = location[0];
            point.y = location[1];
            showOptionPopup(activity, point, BOARD_POPUP, currentMatchId, -400, 100);
        });
    }

    @Override
    public void initListeners(Fragment fragment) {
        if (fragment instanceof PublicBoardHeaderListener) {
            listener = (PublicBoardHeaderListener) fragment;
        } else throw new IllegalStateException("Fragment must implement PublicBoardHeaderListener");

        addInfoTilesListener();
    }

    @Override
    public void initListeners(Activity activity) {
        if (activity instanceof PublicBoardHeaderListener) {
            listener = (PublicBoardHeaderListener) activity;
        } else throw new IllegalStateException("Activity must implement PublicBoardHeaderListener");

        addInfoTilesListener();
    }

    @Override
    public void dispose() {
        listener = null;
        optionsMenu.setOnClickListener(null);
        switch (matchTimeValue) {
            case MATCH_LIVE:
                timeStatusView.stop();
                lastStopTime = SystemClock.elapsedRealtime() - (new Date().getTime() - matchDetails.getMatchStartTime().getTime());
                break;
            case MATCH_ABOUT_TO_START:
            case MATCH_SCHEDULED_LATER:
                resetCountDownTimer();
                break;
        }
    }

    private void addInfoTilesListener() {
        switch (matchTimeValue) {
            case MATCH_LIVE:
                if (baseTime > 0) {
                    if (lastStopTime == 0) {
                        timeStatusView.setBase(baseTime);
                    } else {
                        long intervalOnPause = (baseTime - lastStopTime);
                        timeStatusView.setBase(timeStatusView.getBase() + intervalOnPause);
                    }
                    timeStatusView.start();
                }
                break;
            case MATCH_ABOUT_TO_START:
                setScheduledTimeStatus(true);
                break;
            case MATCH_SCHEDULED_LATER:
                setScheduledTimeStatus(false);
                break;
        }
    }

    private void initWithDefaults(Context context) {
        leagueLogoView.setImageResource(R.drawable.img_epl_logo);
        homeTeamLogoView.setImageResource(R.drawable.ic_arsenal_logo);
        visitingTeamLogoView.setImageResource(R.drawable.ic_blackpool_logo);
        scoreView.setText(context.getString(R.string._8_8));
    }

    public int getSelectedSection() {
        return infoTilesTabLayout.getSelectedTabPosition();
    }

    public void setScore(String score) {
        if (scoreLayout.getPaddingTop() > 0) {
            scoreLayout.setPadding(0, 0, 0, 0);
        }
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        scoreView.setText(score);
    }

    public void prepare(MatchDetails matchDetails, @DrawableRes int leagueLogo) {
        this.matchDetails = matchDetails;
        setLeagueLogo(leagueLogo);
        setHomeTeamLogo(matchDetails.getHomeTeam().getLogoLink());
        setVisitingTeamLogo(matchDetails.getAwayTeam().getLogoLink());
        setScore(getSeparator(matchDetails, winPointer, true));
        setBoardTitle(ZoneApplication.getContext().getString(R.string.matchday_) + matchDetails.getMatchDay());
        switch (getMatchTimeValue(matchDetails.getMatchStartTime(), false)) {
            case MATCH_PAST:
            case MATCH_COMPLETED_TODAY:
                setFullTimeStatus();
                matchTimeValue = MATCH_PAST;
                break;
            case MATCH_LIVE:
                setLiveTimeStatus(matchDetails.getMatchStartTime(), matchDetails.getTimeStatus());
                matchTimeValue = MATCH_LIVE;
                break;
            case MATCH_ABOUT_TO_START:
            case MATCH_ABOUT_TO_START_BOARD_ACTIVE:
            case MATCH_SCHEDULED_TODAY:
                setScheduledTimeStatus(true);
                setTodayMatchCountdown();
                matchTimeValue = MATCH_ABOUT_TO_START;
                break;
            case MATCH_SCHEDULED_LATER:
                setScheduledTimeStatus(false);
                matchTimeValue = MATCH_SCHEDULED_LATER;
                break;
        }
    }

    /**
     * Time status setter for past matches
     */
    public void setFullTimeStatus() {
        this.timeStatusView.setText(FULL_TIME_LOWERCASE);
    }

    /**
     * Time status setter for live matches
     */
    public void setLiveTimeStatus(Date matchStartTime, String timeStatus) {
        switch (timeStatus) {
            case NS:
            case LIVE:
                resetCountDownTimer();
                baseTime = SystemClock.elapsedRealtime() - (new Date().getTime() - matchStartTime.getTime());
                Date baseDate = new Date(baseTime);
                timeStatusView.setOnChronometerTickListener(chronometer -> {
                    baseTime += 1000;
                    baseDate.setTime(baseTime);
                    timeStatusView.setText(getTimeInFootballFormat(baseDate));
                });
                timeStatusView.start();
                break;
            default:
                timeStatusView.stop();
                timeStatusView.setText(getDisplayTimeStatus(timeStatus));
                break;
        }
    }

    /**
     * Time status setter for live matches
     */
    public void setTodayMatchCountdown() {
        long timeDiffFromNow = Math.abs(matchDetails.getMatchStartTime().getTime() - new Date().getTime());
        resetCountDownTimer();
        DateUtil.HOUR_MINUTE_SECOND_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(GMT));
        countDownTimer = new CountDownTimer(timeDiffFromNow, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeStatusView.setText(getHourMinuteSecondFormatDate(new Date(millisUntilFinished)));
                if (millisUntilFinished <= FOUR_HOURS_MILLIS && millisUntilFinished > ONE_HOUR_MILLIS && layoutRefreshState == -1) {
                    layoutRefreshState = MATCH_ABOUT_TO_START_BOARD_ACTIVE;
                    listener.onMatchTimeStateChange();
                } else if (millisUntilFinished <= ONE_HOUR_MILLIS && layoutRefreshState == MATCH_ABOUT_TO_START_BOARD_ACTIVE) {
                    layoutRefreshState = MATCH_ABOUT_TO_START;
                    listener.onMatchTimeStateChange();
                }
            }

            @Override
            public void onFinish() {
                timeStatusView.setText(LIVE);
                matchDetails.setTimeStatus(LIVE);
                setLiveTimeStatus(matchDetails.getMatchStartTime(), matchDetails.getTimeStatus());
                setScore(getSeparator(matchDetails, winPointer, true));
                listener.onMatchTimeStateChange();
            }
        };
        countDownTimer.start();
    }

    /**
     * Time status setter for scheduled matches
     */
    public void setScheduledTimeStatus(boolean isScheduledToday) {
        winPointer.setVisibility(GONE);
        scoreView.setVisibility(isScheduledToday ? VISIBLE : GONE);
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, isScheduledToday ? 10 : 32);
        scoreLayout.setPadding(0, (int) getDp(9), 0, 0);
        String scheduledMatchDateString = getScheduledMatchDateString(matchDetails.getMatchStartTime());
        if (isScheduledToday) {
//            set time of match in score view and set countdown in time status view
            scoreView.setText(scheduledMatchDateString);
        } else {
//            set time of match in time status view
            timeStatusView.setText(scheduledMatchDateString);
        }
    }

    private void resetCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void setLeagueLogo(@NonNull String logoUrl) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override((int) getDp(30), (int) getDp(30))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(leagueLogoView);
    }

    @Override
    public void setLeagueLogo(@DrawableRes int resource) {
        if (resource != 0) leagueLogoView.setImageResource(resource);
    }

    public void setHomeTeamLogo(String logoUrl) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override((int) getDp(30), (int) getDp(30))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(homeTeamLogoView);
    }

    public void setHomeTeamLogo(@DrawableRes int resource) {
        if (resource != 0) homeTeamLogoView.setImageResource(resource);
    }

    public void setVisitingTeamLogo(String logoUrl) {
        Glide.with(this).load(logoUrl)
                .apply(RequestOptions.centerInsideTransform()
                        .override((int) getDp(30), (int) getDp(30))
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder))
                .into(visitingTeamLogoView);
    }

    public void setVisitingTeamLogo(@DrawableRes int resource) {
        if (resource != 0) visitingTeamLogoView.setImageResource(resource);
    }

    @Override
    public void setPeopleCount(@NonNull String peopleCount) {
        peopleCountView.setText(peopleCount);
    }

    @Override
    public void setPostCount(@NonNull String postCount) {
        postCountView.setText(postCount);
    }

    @Override
    public void setBoardTitle(@NonNull String boardTitle) { }

    @Override
    public void showLock(boolean showLock) { }

    @NonNull
    @Override
    public TabLayout getInfoTilesTabLayout() {
        return infoTilesTabLayout;
    }

    @Override
    public void setupWithViewPager(@NonNull ViewPager viewPager, int defaultSelection) {
        infoTilesTabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(defaultSelection, true);
    }

    public void setBoardTemperature(BoardTemperature boardTemperature) {
        peopleCountView.setText(NumberFormatter.format(boardTemperature.getUserCount()));
        postCountView.setText(NumberFormatter.format(boardTemperature.getPostCount()));
        interactionCountView.setText(NumberFormatter.format(boardTemperature.getInteractionCount()));
    }
}