package life.plank.juna.zone.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import life.plank.juna.zone.R;

public class ChatFragment extends Fragment {

    Context context;
    @BindView(R.id.back_image_view)
    TextView backImageView;
    @BindView(R.id.expand_collapse_image_view)
    ImageView expandCollapseImageView;
    @BindView(R.id.people_count_text_view)
    TextView peopleCountTextView;
    @BindView(R.id.add_image)
    ImageView addImage;
    @BindView(R.id.camera_image)
    ImageView cameraImage;
    @BindView(R.id.chat_recycler_view)
    RecyclerView chatRecyclerView;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_zone_chat, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecyclerView() {
       /* // Calculate ActionBar height
        int actionBarHeight = 0;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        int spinnerSize = (int) getResources().getDimension(R.dimen.swipe_page_spinner_height);
        int banterSize = (int) getResources().getDimension(R.dimen.swipe_page_banter_zone_height);
        int banterRecyclerSize = (int) getResources().getDimension(R.dimen.football_feed_height);
        // TODO: 29-01-2018 Change based on performance
        SnapHelper snapHelper = new StartSnapHelper();

        //Setup the horizontal recycler view
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizontalRecyclerView.setHasFixedSize(true);
        horizontalfootballFeedAdapter = new HorizontalFootballFeedAdapter(this, horizontalData, UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()));
        horizontalRecyclerView.setAdapter(horizontalfootballFeedAdapter);
        snapHelper.attachToRecyclerView(horizontalRecyclerView);

        //Football Feed recycler view
        int numberOfRows = 2;
        gridLayoutManager = new GridLayoutManager(this, numberOfRows, GridLayoutManager.HORIZONTAL, false);
        feedRecyclerView.setLayoutManager(gridLayoutManager);
        footballFeedAdapter = new FootballFeedAdapter(this, UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayHeight()), UIDisplayUtil.getDisplayMetricsData(this, GlobalVariable.getInstance().getDisplayWidth()), actionBarHeight + spinnerSize + banterSize + banterRecyclerSize);
        feedRecyclerView.setAdapter(footballFeedAdapter);
        feedRecyclerView.setHasFixedSize(true);
        feedRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        footballFeedAdapter.setOnLongClickListener(this);
        SnapHelper snapHelperFeedRecycler = new StartSnapHelper();
        snapHelperFeedRecycler.attachToRecyclerView(feedRecyclerView);
        footballFeeds = new ArrayList<>();*/
    }


    @OnClick({R.id.back_image_view, R.id.expand_collapse_image_view, R.id.people_count_text_view, R.id.add_image, R.id.camera_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image_view:
                break;
            case R.id.expand_collapse_image_view:
                break;
            case R.id.people_count_text_view:
                break;
            case R.id.add_image:
                break;
            case R.id.camera_image:
                break;
        }
    }
}
