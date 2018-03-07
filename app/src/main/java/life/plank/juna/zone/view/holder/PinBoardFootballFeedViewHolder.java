package life.plank.juna.zone.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 2/9/2018.
 */

public class PinBoardFootballFeedViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.news_feed_label)
    public TextView newsFeedLabel;
    @BindView(R.id.news_feed_image)
    public ImageView newFeedImage;
    @BindView(R.id.gradient_header)
    RelativeLayout gradientRelativeLayout;
    @BindView(R.id.category)
    TextView categoryLabel;
    @BindView(R.id.relative_layout_container)
    public RelativeLayout newsFeedRelativeLayout;

    public PinBoardFootballFeedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
