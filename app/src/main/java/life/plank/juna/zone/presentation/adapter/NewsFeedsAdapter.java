package life.plank.juna.zone.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.presentation.model.NewsFeed;

/**
 * Created by plank-sobia on 9/4/2017.
 */

public class NewsFeedsAdapter extends RecyclerView.Adapter<NewsFeedsAdapter.ViewHolder> {

    private List<NewsFeed> newsFeedList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_image)
        ImageView newsFeedImage;
        @BindView(R.id.news_feed_title)
        TextView newsFeedTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsFeed newsFeed = newsFeedList.get(position);
        holder.newsFeedImage.setImageResource(newsFeed.getNewsFeedImage());
        holder.newsFeedTitle.setText(newsFeed.getNewsFeedTitle());
    }

    @Override
    public int getItemCount() {
        return newsFeedList.size();
    }

    public void setNewsFeedList(List<NewsFeed> newsFeeds) {
        if (newsFeeds == null) {
            return;
        }
        newsFeedList.clear();
        newsFeedList.addAll(newsFeeds);
        notifyDataSetChanged();
    }
}
