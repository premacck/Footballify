package life.plank.juna.zone.interfaces;

import android.app.Activity;
import android.support.v4.app.Fragment;

public interface CustomViewListener {

    void initListeners(Fragment fragment);

    void initListeners(Activity activity);

    void dispose();
}