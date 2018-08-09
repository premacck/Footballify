package life.plank.juna.zone.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import life.plank.juna.zone.R;

public class ZoneToolBar extends LinearLayout {

    private TextView titleView;
    private TextView coinCountView;
    private ImageView profilePicView;

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
        inflate(context, R.layout.new_tool_bar, this);

        titleView = findViewById(R.id.toolbar_title);
        coinCountView = findViewById(R.id.toolbar_coin_count);
        profilePicView = findViewById(R.id.toolbar_profile_pic);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ZoneToolBar);
        try {
            setTitle(array.getString(R.styleable.ZoneToolBar_title));
            String coinCount = array.getString(R.styleable.ZoneToolBar_coinCount);
            setCoinCount(coinCount != null && !coinCount.isEmpty() ? coinCount : "32K");
            setProfilePic(array.getResourceId(R.styleable.ZoneToolBar_profilePic, R.drawable.ic_profile_dummy));
        } catch (Exception e) {
            e.printStackTrace();
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

    public String getCoinCount() {
        return coinCountView.getText().toString();
    }

    public void setCoinCount(String coinCount) {
        coinCountView.setText(coinCount);
    }

    public void setProfilePic(String url) {
        Picasso.with(getContext()).load(url).into(profilePicView);
    }

    public void setProfilePic(@DrawableRes int drawableRes) {
        profilePicView.setImageResource(drawableRes);
    }
}