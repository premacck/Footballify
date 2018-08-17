package life.plank.juna.zone.interfaces;

import android.support.v4.app.Fragment;

public interface FragmentNavigation {

    void pushFragment(Fragment fragment);

    void popFragment();
}