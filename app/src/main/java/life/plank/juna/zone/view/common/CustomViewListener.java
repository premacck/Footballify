package life.plank.juna.zone.view.common;

import android.app.Activity;

import androidx.fragment.app.Fragment;

public interface CustomViewListener {

    void initListeners(Fragment fragment);

    void initListeners(Activity activity);

    void dispose();
}