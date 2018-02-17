package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;
import life.plank.juna.zone.view.holder.PinBoardFootballFeedViewHolder;

public class PinBoardFootballFeedAdapter extends RecyclerView.Adapter<PinBoardFootballFeedViewHolder> {

    private Context context;
    private int screenHeight;
    private int screenWidth;
    private int heightsToBeRemoved;
    private List<FootballFeed> footballFeedList = new ArrayList<>();

    public PinBoardFootballFeedAdapter(Context context, int height, int width, int heightsToBeRemoved) {
        screenHeight = height;
        screenWidth = width;
        this.heightsToBeRemoved = heightsToBeRemoved;
        this.context = context;
    }

    @Override
    public PinBoardFootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.football_feed_row, parent, false);
        return new PinBoardFootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PinBoardFootballFeedViewHolder holder, int position) {

        holder.newsFeedLabel.setText(footballFeedList.get(position).getHeadline());

        if (footballFeedList.get(position).getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeedList.get(position).getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.newFeedImage);
        } else {
            holder.newFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent footballFeedDetails = new Intent(context.getApplicationContext(), FootballFeedDetailActivity.class);
                Gson gson = new Gson();
                String jsonString = gson.toJson(footballFeedList.get(position));
                footballFeedDetails.putExtra("FOOTBALL_FEED", jsonString);
                footballFeedDetails.putExtra("web_url", footballFeedList.get(position).getUrl());
                context.startActivity(footballFeedDetails);
            }
        });
        holder.newsFeedRelativeLayout.getLayoutParams().width = (screenWidth / 2) - UIDisplayUtil.dpToPx(4, context);
        int marginFeedRow = (int) context.getResources().getDimension(R.dimen.football_feed_row_margin);
        int marginBanterRow = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        int footballToolbarMarginBottom = (int) context.getResources().getDimension(R.dimen.football_toolbar_margin_bottom);
        int footballToolbarMarginMargin = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        // marginFeedRow* 4 because of padding in grid view (2 grids).
        // marginBanterRow*2 : single grid.
        holder.newsFeedRelativeLayout.getLayoutParams().height = (screenHeight - heightsToBeRemoved) / 2 -
                (marginFeedRow * 4) - (marginBanterRow * 2) - footballToolbarMarginBottom - footballToolbarMarginMargin;


    }

    @Override
    public int getItemCount() {
        return footballFeedList.size();

    }

    public void setPinnedFootballFeedList() {

        footballFeedList.addAll(new Gson().fromJson(new PreferenceManager(context).getPinnedFeeds(AppConstants.PINNED_FEEDS),
                new TypeToken<List<FootballFeed>>() {
                }.getType()));
        notifyDataSetChanged();
    }

}