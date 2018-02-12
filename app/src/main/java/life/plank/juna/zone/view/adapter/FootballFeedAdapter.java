package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.FootballFeedViewHolder> {

    @BindView(R.id.reaction_like)
    ImageView reactionLike;
    PopupWindow popupWindow;
    int imageWidth;
    private Context context;
    private LayoutInflater mInflater;
    private int screenHeight;
    private int screenWidth;
    private int heightsToBeRemoved;
    private List<FootballFeed> footballFeedList = new ArrayList<>();

    public FootballFeedAdapter(Context context, int height, int width, int heightsToBeRemoved) {
        screenHeight = height;
        screenWidth = width;
        this.heightsToBeRemoved = heightsToBeRemoved;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.football_feed_row, parent, false);
        context = parent.getContext();
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        FootballFeed footballFeed = footballFeedList.get(position);
        holder.newsFeedLabel.setText(footballFeed.getHeadline());
        holder.newsFeedRelativeLayout.getLayoutParams().width = (screenWidth / 2) - UIDisplayUtil.dpToPx(4, context);
        int marginFeedRow = (int) context.getResources().getDimension(R.dimen.football_feed_row_margin);
        int marginBanterRow = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        int footballToolbarMarginBottom = (int) context.getResources().getDimension(R.dimen.football_toolbar_margin_bottom);
        int footballToolbarMarginMargin = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        // marginFeedRow* 4 because of padding in grid view (2 grids).
        // marginBanterRow*2 : single grid.
        holder.newsFeedRelativeLayout.getLayoutParams().height = (screenHeight - heightsToBeRemoved) / 2 - (marginFeedRow * 4) - (marginBanterRow * 2) - footballToolbarMarginBottom - footballToolbarMarginMargin;
        final int sdk = android.os.Build.VERSION.SDK_INT;

        //TODO: Will be uncommented after backend returns data
//        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            holder.gradientRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(bgColor[position]));
//        } else {
//            holder.gradientRelativeLayout.setBackground(context.getResources().getDrawable(bgColor[position]));
//        }


        holder.likeImage.post(new Runnable() {
            @Override
            public void run() {
                imageWidth = holder.likeImage.getWidth();
            }
        });

        holder.likeImage.setOnClickListener((View view) -> {
            int[] locationRecyclerContainer = new int[2];
            holder.newsFeedRelativeLayout.getLocationInWindow(locationRecyclerContainer);
            BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.reaction_layout, null);
            ButterKnife.bind(this, bubbleLayout);
            int height = (screenHeight - heightsToBeRemoved) / 2 - (marginFeedRow * 4) - (marginBanterRow * 2) - footballToolbarMarginBottom - footballToolbarMarginMargin;
            popupWindow = new PopupWindow(context);
            popupWindow.setContentView(bubbleLayout);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(holder.newsFeedRelativeLayout.getLayoutParams().width);
            popupWindow.setHeight(height / 4);
            popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context,
                    com.daasuu.bl.R.drawable.popup_window_transparent));
            int[] locationLIkeView = new int[2];
            view.getLocationInWindow(locationLIkeView);
            bubbleLayout.setArrowPosition(
                    locationLIkeView[0] - locationRecyclerContainer[0] -
                            (bubbleLayout.getArrowWidth() / 2) + (imageWidth / 2)
            );
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, locationRecyclerContainer[0],
                    locationLIkeView[1] - (UIDisplayUtil.dpToPx(2, context) + height / 4)
            );
        });

        if (footballFeed.getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.newFeedImage);
        } else {
            holder.newFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent footballFeedDetails = new Intent(context.getApplicationContext(),
                    FootballFeedDetailActivity.class);
            Gson gson = new Gson();
            String jsonString = gson.toJson(footballFeed);
            footballFeedDetails.putExtra("FOOTBALL_FEED", jsonString);
            footballFeedDetails.putExtra("web_url", footballFeed.getUrl());
            context.startActivity(footballFeedDetails);
        });
    }

    @OnClick({R.id.reaction_like, R.id.reaction_angry, R.id.reaction_cry, R.id.reaction_smile})
    public void onReactionsClicked(View view) {
        // TODO: 12-02-2018 Add click
        if (popupWindow != null)
            popupWindow.dismiss();
    }

    @Override
    public int getItemCount() {
        return footballFeedList.size();
    }

    public void setFootballFeedList(List<FootballFeed> footballFeeds) {
        if (footballFeeds == null) {
            return;
        }
        footballFeedList.addAll(footballFeeds);
        notifyDataSetChanged();
    }

    public class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_label)
        TextView newsFeedLabel;
        @BindView(R.id.news_feed_image)
        ImageView newFeedImage;
        @BindView(R.id.gradient_header)
        RelativeLayout gradientRelativeLayout;
        @BindView(R.id.category)
        TextView categoryLabel;
        @BindView(R.id.relative_layout_container)
        RelativeLayout newsFeedRelativeLayout;
        @BindView(R.id.football_feed_card)
        CardView newsFeedCardView;
        @BindView(R.id.like_image)
        ImageView likeImage;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}