package life.plank.juna.zone;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import junit.framework.Assert;

/**
 * Created by plank-niraj on 07-03-2018.
 */

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<FragmentTestingActivity> {
    private final Class<F> fragmentClass;
    private F fragment;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(FragmentTestingActivity.class, true, false);
        this.fragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        getActivity().runOnUiThread(() -> {
            try {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                fragment = fragmentClass.newInstance();
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            } catch (InstantiationException | IllegalAccessException e) {
                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                        getClass().getSimpleName(),
                        fragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }
}