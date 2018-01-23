package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-arfaa on 22/01/18.
 */

public class HorizontalFootballFeedAdapter extends RecyclerView.Adapter<HorizontalFootballFeedAdapter.ViewHolder> {

    //TODO:Will be replaced with data from the backend
    private String[] data = new String[0];
    int[] images = {R.drawable.ic_dummy_user, R.drawable.ic_dummy_user, R.drawable.ic_dummy_user, R.drawable.ic_dummy_user};

    private LayoutInflater mInflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.horizontal_news_label)
        TextView horizontalNewsFeedLabel;
        @BindView(R.id.horizontal_news_image)
        ImageView horizontalNewsFeedImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public HorizontalFootballFeedAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.horizontal_football_feed_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = data[position];
        holder.horizontalNewsFeedLabel.setText(text);
        holder.horizontalNewsFeedImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}