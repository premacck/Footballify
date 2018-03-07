package life.plank.juna.zone;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import junit.framework.Assert;

import life.plank.juna.zone.util.FragmentTestingActivity;

/**
 * Created by plank-niraj on 07-03-2018.
 */

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<FragmentTestingActivity> {

    private final Class<F> mFragmentClass;
    private F mFragment;
    private int frameLayoutId;

    public FragmentTestRule(final Class<F> fragmentClass,int frameLayoutId) {
        super(FragmentTestingActivity.class, true, false);
        mFragmentClass = fragmentClass;
        this.frameLayoutId = frameLayoutId;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(() -> {
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = mFragmentClass.newInstance();
                transaction.replace(R.id.container, mFragment);
                transaction.commit();
            } catch (InstantiationException | IllegalAccessException e) {
                Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                        getClass().getSimpleName(),
                        mFragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }
    public F getFragment(){
        return mFragment;
    }
}