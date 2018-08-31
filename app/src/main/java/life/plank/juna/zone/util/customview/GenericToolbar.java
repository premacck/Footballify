package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.EngagementInfoTilesToolbar;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;

public class GenericToolbar extends FrameLayout implements CustomViewListener, EngagementInfoTilesToolbar {

    @BindView(R.id.logo)
    ImageView logoImageView;
    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.share_btn)
    ImageButton shareBtn;
    @BindView(R.id.options_menu)
    ImageButton optionsMenu;

    @BindView(R.id.lock)
    ImageView lockImageView;
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

    PublicBoardHeaderListener listener;

    public GenericToolbar(@NonNull Context context) {
        this(context, null);
    }

    public GenericToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GenericToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GenericToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.generic_toolbar, this);
        ButterKnife.bind(this, rootView);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GenericToolbar);
        setBackgroundColor(array.getColor(R.styleable.GenericToolbar_backgroundColor, getResources().getColor(R.color.transparent, null)));
        setTitle(array.getString(R.styleable.GenericToolbar_toolbarTitle));
        setBoardTitle(array.getString(R.styleable.GenericToolbar_boardTypeTitle));
        setLeagueLogo(array.getResourceId(R.styleable.GenericToolbar_logo, R.drawable.ic_pvt_board));
        shareBtn.setVisibility(array.getInt(R.styleable.GenericToolbar_shareButtonVisibility, 0) == 0 ? VISIBLE : INVISIBLE);
        showLock(array.getBoolean(R.styleable.GenericToolbar_isLockVisible, false));
        array.recycle();
    }

    private void initViews(Context context) {
        followBtn.setOnClickListener(view -> listener.followClicked(followBtn));

        initPopupMenu(context);

        optionsMenu.setOnClickListener(view -> menu.show());
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

    private void addInfoTilesListener() {
        followBtn.setOnClickListener(view -> listener.followClicked(followBtn));
        optionsMenu.setOnClickListener(view -> menu.show());
    }

    @Override
    public void dispose() {
        listener = null;
        followBtn.setOnClickListener(null);
        optionsMenu.setOnClickListener(null);
    }

    public void setTitle(String title) {
        this.titleTextView.setText(title);
    }

    @Override
    public void setLeagueLogo(Picasso picasso, String logoUrl) {
        picasso.load(logoUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(logoImageView);
    }

    @Override
    public void setLeagueLogo(@DrawableRes int resource) {
        logoImageView.setImageResource(resource);
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

    public void setBoardTitle(@StringRes int boardTitle) {
        boardTitleView.setText(boardTitle);
    }

    @Override
    public void showLock(boolean showLock) {
        lockImageView.setVisibility(showLock ? VISIBLE : GONE);
    }

    @Override
    public boolean isFollowing() {
        return isFollowing;
    }

    @Override
    public void setFollowing(boolean following) {
        this.isFollowing = following;
    }

    @Override
    public TabLayout getInfoTilesTabLayout() {
        return infoTilesTabLayout;
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(infoTilesTabLayout));
        infoTilesTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    public void setupForPreview() {
        shareBtn.setVisibility(INVISIBLE);
        optionsMenu.setVisibility(INVISIBLE);
        followBtn.setVisibility(INVISIBLE);
        infoTilesTabLayout.setVisibility(INVISIBLE);
    }
}