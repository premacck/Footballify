package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.FootballFeed;
import retrofit2.Retrofit;

/**
 * Created by plank-prachi on 1/30/2018.
 */

public class FootballFeedDetailAdapter extends RecyclerView.Adapter<FootballFeedDetailAdapter.FootballFeedDetailViewHolder> {
    @Inject
    @Named("default")
    Retrofit retrofit;
    private List<FootballFeed> footballFeedsList;
    private Context context;

    public FootballFeedDetailAdapter(Context context, List<FootballFeed> footballFeedsList) {
        this.context = context;
        this.footballFeedsList = footballFeedsList;
    }

    @Override
    public FootballFeedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        getApplication().getLikeFeedNetworkComponent().inject(this);
        retrofit.create(RestApi.class);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.football_feed_detail_row, parent, false);
        return new FootballFeedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedDetailViewHolder holder, int position) {
        holder.feedTitleTextView.setText(footballFeedsList.get(position).getTitle());
        try {
            Picasso.with(context).
                    load(footballFeedsList.get(position).getThumbnail().getImageUrl())
                    .error(R.drawable.ic_place_holder)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(holder.feedImageView);
        } catch (Exception e) {
            holder.feedImageView.setImageResource(R.drawable.ic_place_holder);
        }
    }

    @Override
    public int getItemCount() {
        return footballFeedsList.size();
    }

    public class FootballFeedDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.feed_image_view)
        ImageView feedImageView;
        @BindView(R.id.feed_title_text_view)
        TextView feedTitleTextView;
        @BindView(R.id.like_image_view)
        ImageView likeImageView;
        @BindView(R.id.number_of_likes_text_view)
        TextView likeCountTextView;

        FootballFeedDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}