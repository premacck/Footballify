package life.plank.juna.zone.view.activity.post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.view.activity.base.StackableCardActivity;
import life.plank.juna.zone.view.fragment.post.PostDetailFragment;

import static life.plank.juna.zone.util.FileHandler.getSavedScreenshot;
import static life.plank.juna.zone.util.FileHandler.saveScreenshot;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;

public class PostDetailActivity extends StackableCardActivity {

    @BindView(R.id.faded_card)
    CardView fadedCard;
    @BindView(R.id.blur_background_image_view)
    ImageView blurBackgroundImageView;
    @BindView(R.id.post_detail_view_pager)
    ViewPager postDetailViewPager;
    @BindView(R.id.drag_area)
    View dragArea;

    @Inject
    Gson gson;

    private List<FeedEntry> feedList;
    private String boardId;
    private int position;
    private String target;

    public static void launch(Activity from, List<FeedEntry> feedEntryList, String boardId, int position, View screenshotLayout, String target) {
        Intent intent = new Intent(from, PostDetailActivity.class);
        saveScreenshot(from.getLocalClassName(), screenshotLayout, intent);
        intent.putParcelableArrayListExtra(from.getString(R.string.intent_feed_items), (ArrayList<? extends Parcelable>) feedEntryList);
        intent.putExtra(from.getString(R.string.intent_board_id), boardId);
        intent.putExtra(from.getString(R.string.intent_position), position);
        intent.putExtra(from.getString(R.string.intent_target), target);
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.float_up, R.anim.sink_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        feedList = intent.getParcelableArrayListExtra(getString(R.string.intent_feed_items));
        boardId = intent.getStringExtra(getString(R.string.intent_board_id));
        position = intent.getIntExtra(getString(R.string.intent_position), 0);
        target = intent.getStringExtra(getString(R.string.intent_target));
        blurBg = getSavedScreenshot(intent);
        blurBackgroundImageView.setImageBitmap(blurBg);
        setupSwipeGesture(this, dragArea, postDetailViewPager, fadedCard);
        
        populateViewPager();
    }

    private void populateViewPager() {
        postDetailViewPager.setAdapter(new PostDetailPagerAdapter(getSupportFragmentManager(), this));
        postDetailViewPager.setCurrentItem(position);
    }

    @Override
    public View getScreenshotLayout() {
        return postDetailViewPager;
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
                return PostDetailFragment.newInstance(ref.get().gson.toJson(ref.get().feedList.get(position)), ref.get().boardId, ref.get().target);
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
