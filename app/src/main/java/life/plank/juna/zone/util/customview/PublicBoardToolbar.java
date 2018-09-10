package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;

import static life.plank.juna.zone.util.customview.CustomPopup.showOptionPopup;

public class PublicBoardToolbar extends Toolbar implements CustomViewListener, EngagementInfoTilesToolbar {

    @BindView(R.id.logo)
    ImageView leagueLogoView;
    @BindView(R.id.score)
    TextView scoreView;
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
        setScore(true, array.getString(R.styleable.PublicBoardToolbar_score));
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

    /**
     * @param isScore For scores, keep it true and for upcoming match, set to false and give in the date string.
     */
    public void setScore(boolean isScore, String score) {
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, isScore ? 32 : 10);
        scoreView.setText(score);
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
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(homeTeamLogoView);
    }

    public void setHomeTeamLogo(@DrawableRes int resource) {
        if (resource != 0) homeTeamLogoView.setImageResource(resource);
    }

    public void setVisitingTeamLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .fit().centerCrop()
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
}