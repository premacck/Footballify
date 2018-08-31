package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;

public class PublicBoardToolbar extends LinearLayout implements CustomViewListener, EngagementInfoTilesToolbar {

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

    @BindView(R.id.board_type_title)
    TextView boardTitleView;
    @BindView(R.id.info_tiles_tab_layout)
    TabLayout infoTilesTabLayout;

    PopupMenu menu;

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
        this(context, attrs, defStyleAttr, 0);
    }

    public PublicBoardToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.public_board_toolbar, this);
        ButterKnife.bind(this, rootView);
        initPopupMenu(context);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PublicBoardToolbar);

        boolean initWithDefaults = array.getBoolean(R.styleable.PublicBoardToolbar_useDefaults, true);
        if (initWithDefaults) {
            initWithDefaults(context);
            return;
        }
        setScore(true, array.getString(R.styleable.PublicBoardToolbar_score));
        setLeagueLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, R.drawable.img_epl_logo));
        setHomeTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, R.drawable.ic_arsenal_logo));
        setVisitingTeamLogo(array.getResourceId(R.styleable.PublicBoardToolbar_leagueLogo, R.drawable.ic_blackpool_logo));
        setPeopleCount(array.getString(R.styleable.PublicBoardToolbar_peopleCount));
        setCommentCount(array.getString(R.styleable.PublicBoardToolbar_commentsCount));
        setLikesCount(array.getString(R.styleable.PublicBoardToolbar_likesCount));
        setBoardTitle(array.getString(R.styleable.PublicBoardToolbar_boardTitle));
        showLock(array.getBoolean(R.styleable.PublicBoardToolbar_showLock, false));

        array.recycle();
    }

    private void initPopupMenu(Context context) {
        menu = new PopupMenu(context, optionsMenu);
        menu.getMenu().add(
                R.id.group_board,
                isFavourite ? R.id.action_remove_favourite : R.id.action_mark_favourite,
                1,
                isFavourite ? getContext().getString(R.string.remove_favourite) : getContext().getString(R.string.mark_favourite)
        );
        menu.getMenu().add(
                R.id.group_board,
                isNotificationOn ? R.id.action_hide_notifications : R.id.action_show_notifications,
                2,
                isNotificationOn ? getContext().getString(R.string.hide_notifications) : getContext().getString(R.string.show_notifications)
        );
        menu.getMenu().add(
                R.id.group_board,
                isFollowing ? R.id.action_unfollow_board : R.id.action_follow_board,
                3,
                isFollowing ? getContext().getString(R.string.follow_board) : getContext().getString(R.string.unfollow_board)
        );
        menu.getMenu().add(
                R.id.group_board,
                R.id.action_report_board,
                4,
                getContext().getString(R.string.report_board)
        );
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
        optionsMenu.setOnClickListener(view -> menu.show());
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
        leagueLogoView.setImageResource(resource);
    }

    public void setHomeTeamLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(homeTeamLogoView);
    }

    public void setHomeTeamLogo(@DrawableRes int resource) {
        homeTeamLogoView.setImageResource(resource);
    }

    public void setVisitingTeamLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(visitingTeamLogoView);
    }

    public void setVisitingTeamLogo(@DrawableRes int resource) {
        visitingTeamLogoView.setImageResource(resource);
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
        boardTitleView.setCompoundDrawablesWithIntrinsicBounds(
                showLock ? R.drawable.ic_lock : 0,
                0, 0, 0
        );
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
    public void setFollowing(boolean following) {
        isFollowing = following;
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