package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.itemanimators.ScaleXAnimator;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.interfaces.OnItemClickListener;
import life.plank.juna.zone.interfaces.ScrubberPointerUpdate;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.util.helper.ItemTouchHelperCallback;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;
import life.plank.juna.zone.view.fragment.ChatFragment;

public class LiveZoneActivity extends OnBoardDialogActivity implements ScrubberPointerUpdate,
        OnItemClickListener {

    public boolean isChatScreenVisible = false;
    @BindView(R.id.live_zone_text_view)
    TextView liveZoneTextView;
    @BindView(R.id.live_zone_grid_view_relative_layout)
    RecyclerView liveZoneGridViewRecyclerView;
    @BindView(R.id.close_image)
    ImageView closeImage;
    @BindView(R.id.scrubber_recycler_view)
    RecyclerView scrubberView;
    @BindView(R.id.arrow)
    ImageView arrow;
    @BindView(R.id.commentary_text)
    TextView commentaryTextView;
    @BindView(R.id.home_team_score_text_view)
    TextView homeTeamScoreTextView;
    @BindView(R.id.visiting_team_score_text_view)
    TextView visitingTeamScoreTextView;
    @BindView(R.id.scrubber_linear_layout)
    LinearLayout scrubberLinearLayout;
    @BindView(R.id.banter_zone_layout)
    RelativeLayout banterZoneLayout;
    Context context;
    LiveZoneGridAdapter adapter;
    int liveZoneGridViewHeight;
    ArrayList<Integer> scrubberProgressData = new ArrayList<>();
    @BindView(R.id.fragment_container_frame_layout)
    FrameLayout fragmentContainerFrameLayout;
    ScrubberViewAdapter scrubberViewAdapter;
    int progressStatus = 0;
    int currentMatch = 1;
    LinearLayoutManager scrubberLinearLayoutManager;
    private HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_zone);
        ButterKnife.bind(this);
        context = this;
        setUpScrubber();
        getHeightDetails();
        setUpGridView();
    }

    private void setUpGridView() {
        SnapHelper snapHelper = new StartSnapHelper();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.supportsPredictiveItemAnimations();
        //TODO will be removed later
        liveZoneGridViewRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new LiveZoneGridAdapter(this);
        liveZoneGridViewRecyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        //TODO needed in future
        //snapHelper.attachToRecyclerView(liveZoneGridViewRecyclerView);
        ScaleXAnimator scaleXAnimator = new ScaleXAnimator();
        scaleXAnimator.setAddDuration(AppConstants.DELAY);
        liveZoneGridViewRecyclerView.setItemAnimator(scaleXAnimator);
        adapter.setOnItemClickListener(this);
    }

    private void setUpScrubber() {
        scrubberViewDataHolder = new HashMap<>();
        int matchNumber = getIntent().getExtras().getInt(ScrubberConstants.SCRUBBER_MATCH_NUMBER);
        scrubberLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        scrubberView.setLayoutManager(scrubberLinearLayoutManager);
        scrubberViewAdapter = new ScrubberViewAdapter(context, scrubberProgressData, scrubberViewDataHolder, this, matchNumber);
        scrubberView.setAdapter(scrubberViewAdapter);
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(scrubberViewAdapter, scrubberProgressData);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scrubberView);
        scrubberViewAdapter.setItemTouchHelper(touchHelper);
        new Thread(this::updateScrubber).start();
    }

    private void updateScrubber() {
        while (progressStatus < ScrubberConstants.getScrubberViewTotalWindow()) {
            try {
                if (scrubberProgressData != null && !scrubberProgressData.isEmpty())
                    scrubberProgressData.remove(scrubberProgressData.size() - 1);
                if (scrubberViewDataHolder.containsKey(progressStatus) && scrubberViewDataHolder.get(progressStatus).isTriggerEvents()) {
                    scrubberProgressData.add(scrubberViewDataHolder.get(progressStatus).getType());
                    onNewEvent(scrubberViewDataHolder.get(progressStatus));
                } else {
                    scrubberProgressData.add(ScrubberConstants.getScrubberViewProgress());
                }
                scrubberProgressData.add(ScrubberConstants.getScrubberViewCursor());
                if (!scrubberViewAdapter.trigger) {
                    runOnUiThread(() -> scrubberViewAdapter.notifyItemChanged(scrubberProgressData.size() - 1));
                }
                moveScrubberPointer(null, scrubberProgressData.size());
                progressStatus++;
                //TODO sleep time will be removed once we get the scrubberProgressData from backend
                Thread.sleep(2000);
            } catch (Exception e) {
                // TODO: 21-02-2018 remove after stable.
                Log.e("trace scrubber", e.toString());
                e.printStackTrace();
            }
        }
    }

    public void onNewEvent(ScrubberViewData scrubberViewData) {
        // TODO: 06-02-2018 Animate grid item position = position + tilePosition
        runOnUiThread(() -> {
            if (scrubberViewData.getLiveFeedTileData().getTiles().size() > 0) {
                int position = ((GridLayoutManager) liveZoneGridViewRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                try {
                    for (int i = 0; i <= scrubberViewData.getLiveFeedTileData().getTiles().size() - 1; i++) {
                        int tilePosition = i;
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                        adapter.addGridItemsToView(0, scrubberViewData.getLiveFeedTileData().getTiles().get(tilePosition));
                    }
                    liveZoneGridViewRecyclerView.scrollToPosition(0);
                } catch (Exception e) {
                    //do nothing, as it will take up next event
                }
            }
        });
    }

    @OnClick({R.id.close_image, R.id.next_match_text_view, R.id.previous_match_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_image:
                onBackPressed();
                break;
            case R.id.next_match_text_view:
                //TODO: comment will be removed once more than one matches will be added
                //((SwipePageActivity) context).goToLiveMatch(currentMatch + 1);
                break;
            case R.id.previous_match_text_view:
                //TODO: comment will be removed once more than one matches will be added
                //((SwipePageActivity) context).goToLiveMatch(currentMatch - 1);
                break;
            default:
                break;
        }
    }

    /**
     * Get linearLayout after it is drawn.
     */
    public void getHeightDetails() {
        // TODO: 01-02-2018 Check the performance and change accordingly once implement the server scrubberProgressData
        liveZoneGridViewRecyclerView.post(() -> {
            liveZoneGridViewHeight = liveZoneGridViewRecyclerView.getHeight();
            adapter.computeNewDimensions(liveZoneGridViewHeight);
            liveZoneGridViewRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void moveScrubberPointer(View view, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (position > 0) {
                    int[] xyViewAfter = new int[]{0, 0};
                    View scrubberViewItems = scrubberLinearLayoutManager.findViewByPosition(scrubberProgressData.size() - 2);
                    if (scrubberViewItems != null) {
                        scrubberViewItems.getLocationOnScreen(xyViewAfter);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScrubberConstants.scrubberPointerImageWidth,
                                ScrubberConstants.scrubberPointerImageWidth);
                        layoutParams.setMarginStart(xyViewAfter[0] - ScrubberConstants.scrubberPointerImageWidth + ScrubberConstants.ScrubberCursorWidth);
                        arrow.setLayoutParams(layoutParams);
                    }
                }
            }
        });
    }

    @Override
    public void updateRecentEvents(int position) {
        new Handler(Looper.getMainLooper()).post(() -> commentaryTextView.setText(scrubberViewDataHolder.get(position).getMessage()));
    }

    public void chatFragment() {
        fragmentContainerFrameLayout.removeAllViews();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
                .replace(R.id.fragment_container_frame_layout, new ChatFragment())
                .commit();
    }

    public void retainLayout() {
        if (isChatScreenVisible) {
            liveZoneGridViewRecyclerView.setVisibility(View.GONE);
            banterZoneLayout.setVisibility(View.GONE);
            fragmentContainerFrameLayout.setVisibility(View.VISIBLE);
        } else {
            scrubberLinearLayout.setVisibility(View.VISIBLE);
            liveZoneGridViewRecyclerView.setVisibility(View.VISIBLE);
            banterZoneLayout.setVisibility(View.VISIBLE);
            fragmentContainerFrameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(int positon) {
        isChatScreenVisible = true;
        retainLayout();
        chatFragment();
    }

    public void expandCollapseChatView(boolean status) {
        if (status) {
            scrubberLinearLayout.setVisibility(View.GONE);
        } else {
            scrubberLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}
