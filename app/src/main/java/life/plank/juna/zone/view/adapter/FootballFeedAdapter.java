package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.interfaces.PinFeedListener;
import life.plank.juna.zone.util.GlobalVariable;
import life.plank.juna.zone.view.activity.MatchLeagueActivity;
import life.plank.juna.zone.view.activity.MatchResultActivity;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.FootballFeedViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private int heightsToBeRemoved;
    private List<FootballFeed> footballFeedList = new ArrayList<>();
    private PinFeedListener pinFeedListener;
    private OnClickFeedItemListener onClickFeedItemListener;

    public FootballFeedAdapter(Context context) {
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
        holder.feedTitleTextView.setText(footballFeed.getTitle());
        if (footballFeed.getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .fit().centerCrop()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.feedImageView);
        } else {
            holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getInstance().setTilePosition(position);
                switch (position) {
                    case 0: {
                        context.startActivity(new Intent(context, MatchResultActivity.class));
                        break;
                    }
                    case 1: {
                        context.startActivity(new Intent(context, MatchResultActivity.class));
                        break;
                    }
                    case 2: {
                        context.startActivity(new Intent(context, MatchLeagueActivity.class));
                        break;
                    }
                    case 3: {
                        context.startActivity(new Intent(context, MatchResultActivity.class));
                        break;
                    }
                    case 4: {
                        context.startActivity(new Intent(context, PrivateBoardActivity.class));
                        break;
                    }
                    default: {
                        onClickFeedItemListener.onItemClick(position);
                        break;
                    }
                }
            }
        });
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

    public void setPinFeedListener(PinFeedListener pinFeedListener) {
        this.pinFeedListener = pinFeedListener;
    }

    public void setOnClickFeedItemListener(OnClickFeedItemListener onClickFeedItemListener) {
        this.onClickFeedItemListener = onClickFeedItemListener;
    }

    public class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

