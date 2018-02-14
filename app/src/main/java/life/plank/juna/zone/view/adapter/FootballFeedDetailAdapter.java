package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;

import static android.content.Intent.getIntent;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    FootballFeed footballFeed;


    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        /*@BindView(R.id.feed_image)
        ImageView footballFeedImage;
        @BindView(R.id.feed_content)
        TextView footballFeedContent;
        @BindView(R.id.feed_content_details)
        TextView footballFeedContentDetails;*/
        @BindView(R.id.sliding_layout)
        SlidingUpPanelLayout mLayout;
        @BindView(R.id.list)
        ListView list;
        @BindView(R.id.dragView)
        LinearLayout dragView;
        /*@BindView(R.id.expand_arrow)
        ImageView expandArrow;
*/
        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedDetailAdapter(Context context, FootballFeed footballFeed) {
        this.mInflater = LayoutInflater.from(context);
        this.footballFeed = footballFeed;
    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.football_feed_detail_row, parent, false);
        context = parent.getContext();
        return new FootballFeedDetailViewHolder(view);
    }
    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        //TODO confirm max lines for the bottom content
       /* holder.footballFeedContent.setText(footballFeed.getHeadline());
        holder.footballFeedContentDetails.setText(footballFeed.getSummary());
        if (footballFeed.getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.footballFeedImage);
        } else {
            holder.footballFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }*/
        setUpSlidingLayout( holder);
       /* holder.expandArrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               *//*holder.mLayout.setAnchorPoint(0.7f);
               holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
               ((FootballFeedDetailActivity)context).setUpRecyclerViewScroll(false);*//*
           }
       });*/
    }

    @Override
    public int getItemCount() {
        return 10 ;
    }

    private void setUpSlidingLayout(FootballFeedDetailViewHolder holder){
        holder.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                your_array_list );

        holder.list.setAdapter(arrayAdapter);
        holder.mLayout.setPanelHeight(UIDisplayUtil.dpToPx(200,context));
        holder.mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("panelslide", "onPanelSlide, offset " + slideOffset);
                /*holder.mLayout.setAnchorPoint(0.7f);
                holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);*/
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.i(TAG, "onPanelStateChanged " + newState);
                switch (newState){
                    case ANCHORED:{
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context,R.color.White));
                        break;
                    }
                    case EXPANDED:{
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context,R.color.White));
                        break;
                    }
                    case COLLAPSED:{
                        holder.dragView.setBackgroundColor(ContextCompat.getColor(context,R.color.transparent_grey));
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



        holder.mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });



    }

}
