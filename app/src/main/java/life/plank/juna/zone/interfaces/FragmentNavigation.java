package life.plank.juna.zone.interfaces;

import androidx.fragment.app.Fragment;

public interface FragmentNavigation {

    void pushFragment(Fragment fragment);

    void popFragment();
}