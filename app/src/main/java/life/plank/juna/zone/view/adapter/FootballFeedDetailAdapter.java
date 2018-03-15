package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {

    List<FootballFeed> footballFeedsList = new ArrayList<>();
    private Context context;
    private FootballFeedCommentAdapter commentFeedAdapter;

    public FootballFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList) {
        this.context = context;
        this.footballFeedsList = footballFeedsList;
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
        holder.topFeedContentTextView.setText(footballFeedsList.get(position).getSummary());
        holder.bottomFeedContentTextView.setText(R.string.feed_content_subtitle);
        holder.titleTextView.setText(footballFeedsList.get(position).getTitle());
        holder.slidingTitleTextView.setText(footballFeedsList.get(position).getTitle());
        holder.feedDetailsDateTextView.setText(footballFeedsList.get(position).getDatePublished());
        holder.slidingFeedDetailsDateTextView.setText(footballFeedsList.get(position).getDatePublished());
        holder.populateCommentRecyclerView();
        holder.feedCommentRecyclerView.setNestedScrollingEnabled(false);
        setUpSlidingLayout(holder);
        holder.expandArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.slidingPanelLinearLayout.setVisibility(View.GONE);
                holder.slidingUpPanelLayout.setAnchorPoint(0.7f);
                holder.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

            }
        });
       /* if (footballFeedsList.get(position).getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeedsList.get(position).getThumbnail().getImageUrl())
                    .fit()
                    .error(R.drawable.ic_place_holder)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(holder.feedImageView);
        } else {
            holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
        }*/


        try {
            Picasso.with(context).
                    load(footballFeedsList.get(position).getThumbnail().getImageUrl())
                    .error(R.drawable.ic_place_holder)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(new Target() {

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            holder.slidingUpPanelLayout.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(final Drawable errorDrawable) {
                            holder.slidingUpPanelLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_place_holder));
                        }

                        @Override
                        public void onPrepareLoad(final Drawable placeHolderDrawable) {
                            Log.d("TAG", "Prepare Load");
                        }
                    });
        } catch (Exception e) {
            holder.slidingUpPanelLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_place_holder));
        }

    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    private void setUpSlidingLayout(FootballFeedDetailViewHolder holder) {
        holder.slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (COLLAPSED == newState) {
                    setCollapsedSlideUpPanleTitle(holder);
                } else {
                    setExpandedSlideUpPanleTitle(holder);
                }
            }
        });
        holder.slidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.slidingUpPanelLayout.setPanelState(COLLAPSED);
            }
        });
    }

    private void setExpandedSlideUpPanleTitle(FootballFeedDetailViewHolder holder) {
        holder.slideUpPanelLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        holder.tagTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        holder.slidingTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        holder.slidingFeedDetailsDateTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        holder.expandArrow.setVisibility(View.GONE);
        ((FootballFeedDetailActivity) context).setUpRecyclerViewScroll(false);
    }

    private void setCollapsedSlideUpPanleTitle(FootballFeedDetailViewHolder holder) {
        holder.slideUpPanelLinearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_grey));
        holder.tagTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.slidingTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.slidingFeedDetailsDateTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        holder.expandArrow.setVisibility(View.VISIBLE);
        ((FootballFeedDetailActivity) context).setUpRecyclerViewScroll(true);
    }

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.feed_comment_recycler_view)
        RecyclerView feedCommentRecyclerView;
        @BindView(R.id.tag_text_view)
        TextView tagTextView;
        @BindView(R.id.title_text_view)
        TextView titleTextView;
        @BindView(R.id.sliding_layout)
        SlidingUpPanelLayout slidingUpPanelLayout;
        @BindView(R.id.drag_view)
        LinearLayout dragView;
        @BindView(R.id.expand_arrow)
        ImageView expandArrow;
        @BindView(R.id.top_feed_content)
        TextView topFeedContentTextView;
        @BindView(R.id.bottom_feed_content)
        TextView bottomFeedContentTextView;
        @BindView(R.id.sliding_panel_layout)
        LinearLayout slidingPanelLinearLayout;
        @BindView(R.id.comment_submit)
        Button submitButton;
        @BindView(R.id.add_comment)
        Button addCommentButton;
        @BindView(R.id.scroll_view)
        ScrollView scrollView;
        @BindView(R.id.add_comment_view)
        Button addCommentView;
        @BindView(R.id.sliding_title_text_view)
        TextView slidingTitleTextView;
        @BindView(R.id.feed_details_date_text_view)
        TextView feedDetailsDateTextView;
        @BindView(R.id.sliding_feed_details_date_text_view)
        TextView slidingFeedDetailsDateTextView;
        @BindView(R.id.slide_up_panel_linear_layout)
        LinearLayout slideUpPanelLinearLayout;

        public FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.add_comment, R.id.comment_submit, R.id.add_comment_view})
        public void onAddComment(View view) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {

                    RxView.clicks(addCommentButton)
                            .subscribe(v -> {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            });
                    RxView.clicks(submitButton)
                            .subscribe(v -> {
                                scrollView.fullScroll(ScrollView.FOCUS_UP);
                            });
                    RxView.clicks(addCommentView)
                            .subscribe(v -> {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            });
                }
            });
        }

        private ArrayList<String> getCommentsList() {
            ArrayList<String> commentList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                commentList.add(context.getString(R.string.comment_data));
            }
            return commentList;
        }

        private void populateCommentRecyclerView() {
            commentFeedAdapter = new FootballFeedCommentAdapter(context, getCommentsList());
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            feedCommentRecyclerView.setLayoutManager(layoutManager);
            feedCommentRecyclerView.setAdapter(commentFeedAdapter);
        }
    }
}