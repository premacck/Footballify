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

public class ZoneToolBar extends LinearLayout implements CustomViewListener {

    @BindView(R.id.toolbar_title)
    TextView titleView;
    @BindView(R.id.toolbar_notification)
    ImageView notificationView;
    @BindView(R.id.toolbar_profile_pic)
    ImageView profilePicView;
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

    public void isNotificationViewVisible(Integer visibility) {
        notificationView.setVisibility(visibility);
    }

    public void setProfilePic(String url) {
        Picasso.with(getContext()).load(url).into(profilePicView);
    }

    public void setProfilePic(@DrawableRes int drawableRes) {
        profilePicView.setImageResource(drawableRes);
    }

    @Override
    public void initListeners(Fragment fragment) {

    }

    @Override
    public void initListeners(Activity activity) {
        if (activity instanceof ZoneToolbarListener) {
            listener = (ZoneToolbarListener) activity;
        } else throw new IllegalStateException("Activity must implement ZoneToolbarListener");

        addProfilePictureListener();
    }

    private void addProfilePictureListener() {
        profilePicView.setOnClickListener(view -> listener.profilePictureClicked(profilePicView));
    }

    @Override
    public void dispose() {

    }

}