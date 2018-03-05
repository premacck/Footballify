package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-arfaa on 22/01/18.
 */

public class HorizontalFootballFeedAdapter extends RecyclerView.Adapter<HorizontalFootballFeedAdapter.FootballFeedViewHolder> {

    private static final int NORMAL_VIEW = 0;
    private static final int ADD_MORE_VIEW = 1;
    private AddMoreClickListeners clickListeners;
    //TODO:Will be replaced with BanterZoneData from the backend
    private ArrayList BanterZoneData = new ArrayList();
    private LayoutInflater mInflater;
    private int screenWidth;
    private Context context;
    private int footballBanterViewMargin;

    public HorizontalFootballFeedAdapter(Context context, ArrayList BanterZoneData, int width) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.BanterZoneData = BanterZoneData;
        screenWidth = width;
        //Result will be in pixels
        footballBanterViewMargin = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        this.clickListeners = (AddMoreClickListeners) context;
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMAL_VIEW) {
            view = mInflater.inflate(R.layout.horizontal_football_feed_row, parent, false);
        } else {
            view = mInflater.inflate(R.layout.add_more_row, parent, false);
        }
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        String text = (String) BanterZoneData.get(position);
        if (position != BanterZoneData.size()) {
            holder.horizontalNewsFeedLabel.setText(text);
        }
        //Padding of single cell. footballBanterViewMargin*2 (2 sides of cell)
        // TO get equal four cell (screenWidth / 4)
        // TODO: 30-01-2018 deice number based on width
        holder.horizontalNewsCardLayout.getLayoutParams().width = (screenWidth / 4) - (footballBanterViewMargin * 2);
        if (BanterZoneData.get(position).equals(context.getString(R.string.add_more)))
            holder.horizontalNewsCardLayout.setOnClickListener(view -> clickListeners.addMore());
    }

    @Override
    public int getItemViewType(int position) {
        // TODO: 30-01-2018 change this based on server response 
        if (BanterZoneData.get(position).equals(context.getString(R.string.add_more))) {
            return ADD_MORE_VIEW;
        } else {
            return NORMAL_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return BanterZoneData.size();
    }

    public interface AddMoreClickListeners {
        void addMore();
    }

    class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tile_label_text_view)
        TextView horizontalNewsFeedLabel;
        @BindView(R.id.profile_image_view)
        ImageView horizontalNewsFeedImage;
        @BindView(R.id.card_content)
        RelativeLayout horizontalNewsCardLayout;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}