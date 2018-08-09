package life.plank.juna.zone.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import life.plank.juna.zone.R;

public class ZoneToolBar extends LinearLayout {

    private TextView titleView;

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
        inflate(context, R.layout.new_tool_bar, this);
        titleView = findViewById(R.id.toolbar_title);
    }

    public String getTitle() {
        return titleView.getText().toString();
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }
}