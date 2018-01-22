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

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.ViewHolder> {

    //TODO:Will be replaced with data from the backend
    private String[] data = new String[0];
    int[] images = {R.drawable.ic_third_dummy, R.drawable.ic_second_dummy,R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image};

    private LayoutInflater mInflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_label)
        TextView newsFeedLabel;
        @BindView(R.id.news_feed_image)
        ImageView newFeedImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.football_feed_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = data[position];
        holder.newsFeedLabel.setText(text);
        holder.newFeedImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}