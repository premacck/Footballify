package life.plank.juna.zone.view.activity.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.fragment.post.PostDetailFragment;

import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.post_detail_view_pager)
    ViewPager postDetailViewPager;
    @BindView(R.id.drag_area)
    View dragArea;

    @Inject
    Gson gson;

    private List<FootballFeed> feedList;
    private String boardId;
    private int position;

    public static void launch(Activity from, String feedListString, String boardId, int position) {
        Intent intent = new Intent(from, PostDetailActivity.class);
        intent.putExtra(from.getString(R.string.intent_feed_items), feedListString);
        intent.putExtra(from.getString(R.string.intent_board_id), boardId);
        intent.putExtra(from.getString(R.string.intent_position), position);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        setupSwipeGesture(this, dragArea);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        feedList = gson.fromJson(intent.getStringExtra(getString(R.string.intent_feed_items)), new TypeToken<List<FootballFeed>>() {}.getType());
        boardId = intent.getStringExtra(getString(R.string.intent_board_id));
        position = intent.getIntExtra(getString(R.string.intent_position), 0);
        populateViewPager();
    }

    private void populateViewPager() {
        postDetailViewPager.setAdapter(new PostDetailPagerAdapter(getSupportFragmentManager(), this));
        postDetailViewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.float_down, R.anim.sink_down);
    }

    static class PostDetailPagerAdapter extends FragmentStatePagerAdapter {

        private final WeakReference<PostDetailActivity> ref;

        PostDetailPagerAdapter(FragmentManager fm, PostDetailActivity postDetailActivity) {
            super(fm);
            this.ref = new WeakReference<>(postDetailActivity);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return PostDetailFragment.newInstance(ref.get().gson.toJson(ref.get().feedList.get(position).getFeedItem()), ref.get().boardId);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public int getCount() {
            return ref.get().feedList.size();
        }
    }
}
