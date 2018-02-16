package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {

    private Context context;
    private List<FootballFeed> footballFeedsList = new ArrayList<>();

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.tag_text_view)
        TextView tagTextView;
        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.sliding_layout)
        SlidingUpPanelLayout mLayout;
        @BindView(R.id.dummy_list)
        ListView dummyList;
        @BindView(R.id.drag_view)
        LinearLayout dragView;
        @BindView(R.id.expand_arrow)
        ImageView expandArrow;
        @BindView(R.id.bottom_linear_layout)
        LinearLayout bottomLinearLayout;

        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedDetailAdapter(Context context) {
        footballFeedsList = GlobalVariable.getInstance().getFootballFeeds();
        this.context = context;
    }


    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.football_feed_detail_row, parent, false);

        return new FootballFeedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        //TODO confirm max lines for the bottom content
        setUpSlidingLayout(holder);
        holder.expandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.bottomLinearLayout.setVisibility(View.GONE);
                holder.mLayout.setAnchorPoint(0.7f);
                holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    private void setUpSlidingLayout(FootballFeedDetailViewHolder holder) {
        //TODO listview will be replaced with recyclerview after getting actual data
        //TODO dummy content will also be replaced
        holder.dummyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView",
                "That",
                "You",
                "Can",
                "Scroll",
                ".",
                "It",
                "Shows",
                "How",
                "Any",
                "Scrollable",
                "View",
                "Can",
                "Be",
                "Included",
                "As",
                "A",
                "Child",
                "Of",
                "SlidingUpPanelLayout"
        );

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                your_array_list);
        holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        holder.dummyList.setAdapter(arrayAdapter);
        holder.mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case ANCHORED: {
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context, R.color.White));
                        ((FootballFeedDetailActivity) context).setUpRecyclerViewScroll(false);
                        break;
                    }
                    case EXPANDED: {
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context, R.color.White));
                        ((FootballFeedDetailActivity) context).setUpRecyclerViewScroll(false);
                        break;
                    }
                    case COLLAPSED: {
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_grey));
                        holder.bottomLinearLayout.setVisibility(View.VISIBLE);
                        ((FootballFeedDetailActivity) context).setUpRecyclerViewScroll(true);
                        break;
                    }
                }
            }
        });
        holder.mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

}
