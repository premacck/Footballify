package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.NewsFeed;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by plank-sobia on 9/4/2017.
 */

public class NewsFeedsAdapter extends RecyclerView.Adapter<NewsFeedsAdapter.ViewHolder> {

    private final Random random = new Random();
    private List<NewsFeed> newsFeedList = new ArrayList<>();
    private Context context;
    private PublishSubject<NewsFeed> itemViewClickSubject = PublishSubject.create();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_image)
        ImageView newsFeedImage;
        @BindView(R.id.news_feed_title)
        TextView newsFeedTitle;
        @BindView(R.id.card_view)
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public Observable<NewsFeed> getViewClickedObservable() {
        return itemViewClickSubject.asObservable();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewsFeed newsFeed = newsFeedList.get(position);
        final String imageName = context.getResources().getString(R.string.image) + random.nextInt(4);
        final int id = context.getResources().getIdentifier(imageName, context.getResources().getString(R.string.drawable), context.getPackageName());
        holder.newsFeedImage.setImageResource(id);
        holder.newsFeedTitle.setText(newsFeed.getTitle());
        holder.cardView.setOnClickListener(v -> itemViewClickSubject.onNext(newsFeed));
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
