package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
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

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.BoardTemperature;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.DateUtil;
import life.plank.juna.zone.util.NumberFormatter;

import static life.plank.juna.zone.util.AppConstants.FULL_TIME_LOWERCASE;
import static life.plank.juna.zone.util.AppConstants.GMT;
import static life.plank.juna.zone.util.AppConstants.LIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_ABOUT_TO_START;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_COMPLETED_TODAY;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_LIVE;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_PAST;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_LATER;
import static life.plank.juna.zone.util.AppConstants.MatchTimeVal.MATCH_SCHEDULED_TODAY;
import static life.plank.juna.zone.util.AppConstants.NOT_STARTED;
import static life.plank.juna.zone.util.DataUtil.getDisplayTimeStatus;
import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.DateUtil.getMatchTimeValue;
import static life.plank.juna.zone.util.DateUtil.getMinuteSecondFormatDate;
import static life.plank.juna.zone.util.DateUtil.getScheduledMatchDateString;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup;

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

    @BindView(R.id.following_text_view)
    TextView followBtn;
    @BindView(R.id.people_count)
    TextView peopleCountView;
    @BindView(R.id.comment_count)
    TextView commentCountView;
    @BindView(R.id.likes_count)
    TextView likesCountView;

    @BindView(R.id.lock)
    ImageView lockImageView;
    @BindView(R.id.board_type_title)
    TextView boardTitleView;
    @BindView(R.id.info_tiles_tab_layout)
    TabLayout infoTilesTabLayout;

    private boolean isFavourite;
    private boolean isNotificationOn;
    private boolean isFollowing;

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
        setCommentCount(array.getString(R.styleable.PublicBoardToolbar_commentsCount));
        setLikesCount(array.getString(R.styleable.PublicBoardToolbar_likesCount));
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
            showOptionPopup(activity, point, activity.getString(R.string.board_pop_up), currentMatchId, -400, 100);
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
        followBtn.setOnClickListener(null);
        optionsMenu.setOnClickListener(null);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void addInfoTilesListener() {
        followBtn.setOnClickListener(view -> listener.followClicked(followBtn));
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

    public void prepare(Picasso picasso, MatchFixture fixture) {
        setHomeTeamLogo(picasso, fixture.getHomeTeam().getLogoLink());
        setVisitingTeamLogo(picasso, fixture.getAwayTeam().getLogoLink());
        setScore(getSeparator(fixture, winPointer, true));
        setBoardTitle(ZoneApplication.getContext().getString(R.string.matchday_) + fixture.getMatchDay());
        switch (getMatchTimeValue(fixture.getMatchStartTime(), false)) {
            case MATCH_PAST:
            case MATCH_COMPLETED_TODAY:
                setFullTimeStatus();
                break;
            case MATCH_LIVE:
                setLiveTimeStatus(fixture.getMatchStartTime(), fixture.getTimeStatus());
                break;
            case MATCH_SCHEDULED_TODAY:
            case MATCH_ABOUT_TO_START:
                setScheduledTimeStatus(fixture.getMatchStartTime(), true);
                setTodayMatchCountdown(fixture, Math.abs(fixture.getMatchStartTime().getTime() - new Date().getTime()));
                break;
            case MATCH_SCHEDULED_LATER:
                setScheduledTimeStatus(fixture.getMatchStartTime(), false);
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
            case NOT_STARTED:
            case LIVE:
                resetCountDownTimer();
                timeStatusView.setBase(SystemClock.elapsedRealtime() - (new Date().getTime() - matchStartTime.getTime()));
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
    public void setTodayMatchCountdown(MatchFixture fixture, long timeDiffFromNow) {
        resetCountDownTimer();
        DateUtil.HOUR_MINUTE_SECOND_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(GMT));
        countDownTimer = new CountDownTimer(timeDiffFromNow, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeStatusView.setText(getMinuteSecondFormatDate(new Date(millisUntilFinished)));
            }

            @Override
            public void onFinish() {
                timeStatusView.setText(LIVE);
                fixture.setTimeStatus(LIVE);
                setLiveTimeStatus(fixture.getMatchStartTime(), fixture.getTimeStatus());
            }
        };
        countDownTimer.start();
    }

    /**
     * Time status setter for scheduled matches
     */
    public void setScheduledTimeStatus(Date matchStartTime, boolean isScheduledToday) {
        winPointer.setVisibility(GONE);
        scoreView.setVisibility(isScheduledToday ? VISIBLE : GONE);
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, isScheduledToday ? 10 : 32);
        scoreLayout.setPadding(0, (int) getDp(9), 0, 0);
        String scheduledMatchDateString = getScheduledMatchDateString(matchStartTime);
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
    public void setLeagueLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(leagueLogoView);
    }

    @Override
    public void setLeagueLogo(@DrawableRes int resource) {
        if (resource != 0) leagueLogoView.setImageResource(resource);
    }

    public void setHomeTeamLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .centerInside()
                .resize((int) getDp(30), (int) getDp(30))
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(homeTeamLogoView);
    }

    public void setHomeTeamLogo(@DrawableRes int resource) {
        if (resource != 0) homeTeamLogoView.setImageResource(resource);
    }

    public void setVisitingTeamLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .centerInside()
                .resize((int) getDp(30), (int) getDp(30))
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(visitingTeamLogoView);
    }

    public void setVisitingTeamLogo(@DrawableRes int resource) {
        if (resource != 0) visitingTeamLogoView.setImageResource(resource);
    }

    @Override
    public void setPeopleCount(String peopleCount) {
        peopleCountView.setText(peopleCount);
    }

    @Override
    public void setCommentCount(String commentsCount) {
        commentCountView.setText(commentsCount);
    }

    @Override
    public void setLikesCount(String likesCount) {
        likesCountView.setText(likesCount);
    }

    @Override
    public void setBoardTitle(String boardTitle) {
        boardTitleView.setText(boardTitle);
    }

    @Override
    public void showLock(boolean showLock) {
        lockImageView.setVisibility(showLock ? VISIBLE : GONE);
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isNotificationOn() {
        return isNotificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        isNotificationOn = notificationOn;
    }

    @Override
    public boolean isFollowing() {
        return isFollowing;
    }

    @Override
    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    @Override
    public TabLayout getInfoTilesTabLayout() {
        return infoTilesTabLayout;
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(infoTilesTabLayout));
        infoTilesTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        Objects.requireNonNull(infoTilesTabLayout.getTabAt(0)).select();
    }

    public void setBoardTemperature(BoardTemperature boardTemperature) {
        peopleCountView.setText(NumberFormatter.format(boardTemperature.getUserCount()));
        commentCountView.setText(NumberFormatter.format(boardTemperature.getPostCount()));
        likesCountView.setText(NumberFormatter.format(boardTemperature.getInteractionCount()));
    }
}