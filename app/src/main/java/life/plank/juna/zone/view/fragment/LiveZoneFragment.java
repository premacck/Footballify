package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.itemanimators.ScaleXAnimator;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScrubberViewData;
import life.plank.juna.zone.util.ScrubberConstants;
import life.plank.juna.zone.util.SpacesItemDecoration;
import life.plank.juna.zone.util.helper.ItemTouchHelperCallback;
import life.plank.juna.zone.util.helper.StartSnapHelper;
import life.plank.juna.zone.view.activity.SwipePageActivity;
import life.plank.juna.zone.view.adapter.LiveZoneGridAdapter;
import life.plank.juna.zone.view.adapter.ScrubberViewAdapter;

public class LiveZoneFragment extends Fragment implements ScrubberViewAdapter.ScrubberPointerUpdate {

    @BindView(R.id.liveZoneTextView)
    TextView liveZoneTextView;
    @BindView(R.id.liveZoneRelativeLayout)
    RelativeLayout liveZoneRelativeLayout;
    @BindView(R.id.liveZoneGridViewRelativeLayout)
    RecyclerView liveZoneGridViewRecyclerView;
    @BindView(R.id.closeImage)
    ImageView closeImage;
    @BindView(R.id.scrubber_recycler_view)
    RecyclerView scrubberView;
    @BindView(R.id.arrow)
    ImageView pointer;
    @BindView(R.id.commentary_text)
    TextView commentaryTextView;
    @BindView(R.id.home_team_score_text_view)
    TextView homeTeamScoreTextView;
    @BindView(R.id.visiting_team_score_text_view)
    TextView visitingTeamScoreTextView;

    Context context;
    LiveZoneGridAdapter adapter;
    int liveZoneGridViewHeight;
    ArrayList<Integer> data = new ArrayList<>();
    ScrubberViewAdapter.ScrubberPointerUpdate scrubberPointerUpdate;
    ScrubberViewAdapter scrubberViewAdapter;
    int progressStatus = 0;
    private Unbinder unbinder;
    private int delay = 1000;
    private HashMap<Integer, ScrubberViewData> scrubberViewDataHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        scrubberPointerUpdate = this;
        scrubberViewDataHolder = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livezone, container, false);
        unbinder = ButterKnife.bind(this, view);

        getHeightDetails();
        setUpGridView();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] location = new int[]{0, 0};
        homeTeamScoreTextView.getLocationOnScreen(location);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setUpGridView() {
        SnapHelper snapHelper = new StartSnapHelper();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.supportsPredictiveItemAnimations();

        scrubberView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        scrubberViewAdapter = new ScrubberViewAdapter(context, data, scrubberViewDataHolder, scrubberPointerUpdate);
        scrubberView.setAdapter(scrubberViewAdapter);
        setUpScrubber();

        liveZoneGridViewRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new LiveZoneGridAdapter(getActivity());
        liveZoneGridViewRecyclerView.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.cardview_compat_inset_shadow);
        liveZoneGridViewRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        snapHelper.attachToRecyclerView(liveZoneGridViewRecyclerView);
        ScaleXAnimator scaleXAnimator = new ScaleXAnimator();
        scaleXAnimator.setAddDuration(1000);
        liveZoneGridViewRecyclerView.setItemAnimator(scaleXAnimator);
    }

    private void setUpScrubber() {
        ItemTouchHelper.Callback callback =
                new ItemTouchHelperCallback(scrubberViewAdapter, data);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(scrubberView);
        scrubberViewAdapter.setItemTouchHelper(touchHelper);
        new Thread(this::updateScrubber).start();
    }


    private void updateScrubber() {
        new Handler(Looper.getMainLooper()).post(() -> pointer.setVisibility(View.VISIBLE));
        while (progressStatus < ScrubberConstants.getScrubberViewTotalWindow()) {
            try {
                progressStatus++;
                if (data != null && !data.isEmpty())
                    data.remove(data.size() - 1);
                if (scrubberViewDataHolder.containsKey(progressStatus) && scrubberViewDataHolder.get(progressStatus).isTriggerEvents()) {
                    data.add(scrubberViewDataHolder.get(progressStatus).getType());
                    onNewEvent(scrubberViewDataHolder.get(progressStatus));
                } else {
                    data.add(ScrubberConstants.getScrubberViewProgress());
                }
                data.add(ScrubberConstants.getScrubberViewCursor());
                Thread.sleep(2000);
                if (!scrubberViewAdapter.trigger) {
                    moveScrubberPointer(null, data.size() - 1);
                    new Handler(Looper.getMainLooper()).post(() -> scrubberViewAdapter.notifyItemChanged(data.size() - 2));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void onNewEvent(ScrubberViewData scrubberViewData) {
        // TODO: 06-02-2018 Animate
        if (scrubberViewData.getLiveFeedTileData().getImages().size() > 0) {
            int position = ((GridLayoutManager) liveZoneGridViewRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            try {
                for (int i = 0; i <= scrubberViewData.getLiveFeedTileData().getImages().size() - 1; i++) {
                    int finalI = i;
                    new Handler(Looper.getMainLooper()).post(() -> adapter.addData(
                            position + finalI, scrubberViewData.getLiveFeedTileData().getImages().get(finalI))
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> liveZoneGridViewRecyclerView.scrollToPosition(0));
        }
    }

    @OnClick(R.id.closeImage)
    public void onCloseImageClicked() {
        ((SwipePageActivity) getActivity()).retainLayout();
    }

    /**
     * Get linearLayout after it is drawn.
     */
    public void getHeightDetails() {
        // TODO: 01-02-2018 Check the performance and change accordingly once implement the server data
        liveZoneGridViewRecyclerView.post(() -> {
            liveZoneGridViewHeight = liveZoneGridViewRecyclerView.getHeight();
            adapter.addData(liveZoneGridViewHeight);
            liveZoneGridViewRecyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void moveScrubberPointer(View view, int position) {
        int[] location = new int[]{0, 0};
        homeTeamScoreTextView.getLocationOnScreen(location);

        int[] xyViewAfter = new int[]{0, 0};
        scrubberView.getLocationOnScreen(xyViewAfter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                25,
                25
        );
        params.addRule(RelativeLayout.BELOW, scrubberView.getId());
        if (position != -1 && scrubberView != null) {
            view = scrubberView.getLayoutManager().findViewByPosition(position);
        }
        if (view == null && position == -1) {
            view = scrubberView.getLayoutManager().findViewByPosition(data.size() - 1);
        }
        int[] xyData;
        if (view != null) {
            xyData = new int[]{0, 0};
            view.getLocationOnScreen(xyData);
            params.leftMargin = xyData[0] - 15;
            view.getLocationInWindow(xyData);
            new Handler(Looper.getMainLooper()).post(() -> pointer.setLayoutParams(params));
        }
    }

    @Override
    public void addCommentary(int position) {
        new Handler(Looper.getMainLooper()).post(() -> commentaryTextView.setText(scrubberViewDataHolder.get(position).getMessage()));
    }
}
