package life.plank.juna.zone.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created to see how the layout is, in the XML file.
 */
public class HorizontalLinearLayoutManager extends LinearLayoutManager {

    public HorizontalLinearLayoutManager(Context context) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
    }

    public HorizontalLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, LinearLayoutManager.HORIZONTAL, reverseLayout);
    }

    public HorizontalLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override public int getOrientation() {
        return LinearLayoutManager.HORIZONTAL;
    }
}