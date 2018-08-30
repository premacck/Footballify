package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.NetworkStatus;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.view.adapter.FootballFeedAdapter;

import static life.plank.juna.zone.util.DataUtil.getStaticFeedItems;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;


/**
 * Created by plank-hasan on 5/01/18.
 */

public class SwipePageActivity extends AppCompatActivity implements PinFeedListener, OnClickFeedItemListener {
    private static final String TAG = SwipePageActivity.class.getSimpleName();
    public static Bitmap parentViewBitmap = null;
    public SwipePageActivity swipePageActivity;
    @BindView(R.id.football_feed_recycler_view)
    RecyclerView feedRecyclerView;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;
    FootballFeedAdapter adapter;

    @BindView(R.id.options_image)
    ImageView optionsImage;
    Point point;

    PopupWindow changeStatusPopUp;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                arcMenu.show();
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_page);
        ButterKnife.bind(this);
        swipePageActivity = SwipePageActivity.this;
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        initRecyclerView();
        setUpData();
        setUpBoomMenu();
    }

    private void showStatusPopup(final Activity context, Point p) {

        LinearLayout viewGroup = context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.menu_pop_up, viewGroup);

        // Creating the PopupWindow
        changeStatusPopUp = new PopupWindow(context);
        changeStatusPopUp.setContentView(layout);
        changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -440;
        int OFFSET_Y = 100;

        //Clear the default translucent background
        changeStatusPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
    }

    @OnClick(R.id.options_image)
    public void onOptionClick(View v) {
        int[] location = new int[2];

        v.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        point = new Point();
        point.x = location[0];
        point.y = location[1];
        showStatusPopup(this, point);

    }

    private void setUpData() {
        if (NetworkStatus.isNetworkAvailable(parentLayout, this)) {
            getFootballFeed();
        } else {
            Toast.makeText(swipePageActivity, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        adapter = new FootballFeedAdapter(this);
        feedRecyclerView.setAdapter(adapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
    }

    public void getFootballFeed() {
        progressBar.setVisibility(View.GONE);
        adapter.setFootballFeedList(getStaticFeedItems());
    }

    @Override
    public void onPinFeed(int position) {
        savePinnedFeedsToPreference(position);
    }

    private void savePinnedFeedsToPreference(int position) {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        Gson gson = new Gson();
        String pinnedList = preferenceManager.getPinnedFeeds(AppConstants.PINNED_FEEDS);
        List<FootballFeed> pinnedFeedsList;
        if ("".contentEquals(pinnedList)) {
            pinnedFeedsList = new ArrayList<>();
        } else {
            pinnedFeedsList = gson.fromJson(pinnedList,
                    new TypeToken<List<FootballFeed>>() {
                    }.getType());
        }
        pinnedFeedsList.add(adapter.getFootballFeedList().get(position));
        preferenceManager.savePinnedFeeds(gson.toJson(pinnedFeedsList));
    }

    @Override
    public void onItemClick(int position, View fromView) {
        parentViewBitmap = loadBitmap(parentLayout, parentLayout, this);
        Intent intent = new Intent(this, FootballFeedDetailActivity.class);
        intent.putExtra(getString(R.string.intent_position), String.valueOf(position));
        intent.putExtra(getString(R.string.intent_feed_items), new Gson().toJson(adapter.getFootballFeedList()));
        startActivity(intent);
    }

    public void setUpBoomMenu() {
        //todo: will be add in Utils so we can Reuse the Code
        arcMenu.setIcon(R.drawable.ic_un, R.drawable.ic_close_white);
        int[] fabImages = {R.drawable.ic_settings_white,
                R.drawable.ic_person, R.drawable.ic_home_purple, R.drawable.ic_gallery,
                R.drawable.ic_camera_white, R.drawable.ic_mic, R.drawable.ic_link, R.drawable.ic_video};
        int[] backgroundColors = {R.drawable.fab_circle_background_grey,
                R.drawable.fab_circle_background_grey, R.drawable.fab_circle_background_white, R.drawable.fab_circle_background_pink,
                R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink, R.drawable.fab_circle_background_pink};
        String[] titles = {"Settings", "Profile", "Home", "Gallery", "Camera", "Audio", "Attachment", "Video"};
        for (int i = 0; i < fabImages.length; i++) {
            View child = getLayoutInflater().inflate(R.layout.layout_floating_action_button, null);
            //child.setId(i);
            RelativeLayout fabRelativeLayout = child.findViewById(R.id.fab_relative_layout);
            ImageView fabImageVIew = child.findViewById(R.id.fab_image_view);
            fabRelativeLayout.setBackground(ContextCompat.getDrawable(this, backgroundColors[i]));
            fabImageVIew.setImageResource(fabImages[i]);
            final int position = i;
            arcMenu.addItem(child, titles[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            Intent intent = new Intent(SwipePageActivity.this, UserProfileActivity.class);
                            startActivity(intent);
                            break;
                        }
                        case 2: {

                            break;
                        }
                        case 3: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_gallery));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 4: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_camera));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 5: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_audio));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                        case 6: {

                        }
                        case 7: {
                            Intent intent = new Intent(SwipePageActivity.this, CameraActivity.class);
                            intent.putExtra(getString(R.string.intent_open_from), getString(R.string.intent_video));
                            intent.putExtra(getString(R.string.intent_api), getString(R.string.intent_swipe_page_activity));
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }

}