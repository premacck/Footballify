package life.plank.juna.zone.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by plank-hasan on 2/14/2018.
 */


public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(HORIZONTAL);
    }
}
