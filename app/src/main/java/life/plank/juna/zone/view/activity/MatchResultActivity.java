package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.interfaces.PublicBoardHeaderListener;
import life.plank.juna.zone.util.customview.GenericToolbar;
import life.plank.juna.zone.view.fragment.league.LeagueInfoFragment;
import life.plank.juna.zone.view.fragment.league.LeagueTilesFragment;

import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;

public class MatchResultActivity extends AppCompatActivity implements PublicBoardHeaderListener {
    public static Bitmap matchStatsParentViewBitmap = null;
    String TAG = MatchResultActivity.class.getSimpleName();

    @Inject
    Picasso picasso;

    @BindView(R.id.stats_parent_view)
    CardView statsParentView;
    @BindView(R.id.league_toolbar)
    GenericToolbar toolbar;
    @BindView(R.id.league_view_pager)
    ViewPager viewPager;

    private String seasonName;
    private String leagueName;
    private String countryName;
    private String leagueLogo;

    public static void launch(Activity fromActivity, String seasonName, String leagueName, String countryName, String leagueLogo) {
        Intent intent = new Intent(fromActivity, MatchResultActivity.class);
        intent.putExtra(fromActivity.getString(R.string.season_name), seasonName);
        intent.putExtra(fromActivity.getString(R.string.league_name), leagueName);
        intent.putExtra(fromActivity.getString(R.string.country_name), countryName);
        intent.putExtra(fromActivity.getString(R.string.league_logo), leagueLogo);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            seasonName = intent.getStringExtra(getString(R.string.season_name));
            leagueName = intent.getStringExtra(getString(R.string.league_name));
            countryName = intent.getStringExtra(getString(R.string.country_name));
            leagueLogo = intent.getStringExtra(getString(R.string.league_logo));
        }

        prepareLeagueInfo();
        setupViewPagerWithFragments();
    }

    private void prepareLeagueInfo() {
        toolbar.setTitle(leagueName);
        toolbar.setLeagueLogo(picasso, leagueLogo);
    }

    private void setupViewPagerWithFragments() {
        viewPager.setAdapter(new LeaguePagerAdapter(getSupportFragmentManager(), this));
        toolbar.setupWithViewPager(viewPager);
    }

    public void updateBackgroundBitmap() {
        matchStatsParentViewBitmap = loadBitmap(getWindow().getDecorView(), getWindow().getDecorView(), this);
    }

    @Override
    public void followClicked(TextView followBtn) {
        if (followBtn.getText().toString().equalsIgnoreCase(getString(R.string.follow))) {
            followBtn.setText(R.string.follow);
        } else {
            followBtn.setText(R.string.unfollow);
        }
    }

    static class LeaguePagerAdapter extends FragmentPagerAdapter {

        private Fragment currentFragment;
        private WeakReference<MatchResultActivity> ref;

        LeaguePagerAdapter(FragmentManager supportFragmentManager, MatchResultActivity activity) {
            super(supportFragmentManager);
            ref = new WeakReference<>(activity);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LeagueInfoFragment.newInstance(
                            ref.get(),
                            ref.get().seasonName,
                            ref.get().leagueName,
                            ref.get().countryName
                    );
                case 1:
                    return LeagueTilesFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? ref.get().getString(R.string.info) : ref.get().getString(R.string.tiles);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = (Fragment) object;
            }
            super.setPrimaryItem(container, position, object);
        }

        Fragment getCurrentFragment() {
            return currentFragment;
        }
    }
}