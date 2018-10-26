package life.plank.juna.zone.util.customview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.CustomViewListener;
import life.plank.juna.zone.interfaces.ZoneToolbarListener;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class ZoneToolBar extends LinearLayout implements CustomViewListener {

    @BindView(R.id.toolbar_title)
    TextView titleView;
    @BindView(R.id.toolbar_notification)
    ImageView notificationView;
    @BindView(R.id.toolbar_profile_pic)
    ImageView profilePicView;
    @BindView(R.id.toolbar_user_greeting)
    TextView userGreetingView;
    private ZoneToolbarListener listener;

    public ZoneToolBar(Context context) {
        this(context, null);
    }

    public ZoneToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoneToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ZoneToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.zone_tool_bar, this);
        ButterKnife.bind(this, rootView);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZoneToolBar);
        try {
            setTitle(array.getString(R.styleable.ZoneToolBar_title));
            setProfilePic(array.getResourceId(R.styleable.ZoneToolBar_profilePic, R.drawable.ic_default_profile));
        } catch (Exception e) {
            Log.e("ZoneToolBar", e.getMessage());
        } finally {
            if (array != null) array.recycle();
        }
    }

    public String getTitle() {
        return titleView.getText().toString();
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }

    public String getUserGreeting() {
        return userGreetingView.getText().toString();
    }

    public void setUserGreeting(String title) {
        this.userGreetingView.setText(title);
    }

    public void isNotificationViewVisible(Integer visibility) {
        notificationView.setVisibility(visibility);
    }

    public void setProfilePic(String url) {
        if (isNullOrEmpty(url)) {
            setProfilePic(R.drawable.ic_default_profile);
            return;
        }
        Picasso.with(getContext()).load(url).into(profilePicView);
    }

    public void setProfilePic(@DrawableRes int drawableRes) {
        profilePicView.setImageResource(drawableRes);
    }

    @Override
    public void initListeners(Fragment fragment) {
        if (fragment instanceof ZoneToolbarListener) {
            listener = (ZoneToolbarListener) fragment;
        } else throw new IllegalStateException("Fragment must implement ZoneToolbarListener");

        addToolbarListener();
    }

    @Override
    public void initListeners(Activity activity) {
        if (activity instanceof ZoneToolbarListener) {
            listener = (ZoneToolbarListener) activity;
        } else throw new IllegalStateException("Activity must implement ZoneToolbarListener");

        addToolbarListener();
    }

    private void addToolbarListener() {
        profilePicView.setOnClickListener(view -> listener.profilePictureClicked(profilePicView));
        notificationView.setOnClickListener(view -> listener.notificationIconClicked(notificationView));
    }

    @Override
    public void dispose() {
        profilePicView.setOnClickListener(null);
        notificationView.setOnClickListener(null);
    }
}