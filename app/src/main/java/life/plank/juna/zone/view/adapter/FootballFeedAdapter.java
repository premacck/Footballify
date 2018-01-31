package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.UIDisplayUtil;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.FootballFeedViewHolder> {

    String[] category = {"TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE", "TEAMS", "LEAGUES/CUPS", "PUNDITS", "LIVEZONE"};
    int[] images = {R.drawable.ic_third_dummy, R.drawable.ic_second_dummy, R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image, R.drawable.ic_third_dummy, R.drawable.ic_second_dummy, R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image};
    int[] bgColor = {R.drawable.football_header_orange_gradient, R.drawable.football_header_green_gradient, R.drawable.football_header_blue_gradient, R.drawable.football_header_purple_gradient, R.drawable.football_header_orange_gradient, R.drawable.football_header_green_gradient, R.drawable.football_header_blue_gradient, R.drawable.football_header_purple_gradient};
    //TODO:Will be replaced with data from the backend
    private String[] data = new String[0];
    private Context context;
    private LayoutInflater mInflater;
    private int screenHeight;
    private int screenWidth;
    private int heightsToBeRemoved;

    public FootballFeedAdapter(Context context, String[] data, int height, int width, int heightsToBeRemoved) {
        screenHeight = height;
        screenWidth = width;
        this.heightsToBeRemoved = heightsToBeRemoved;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public FootballFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.football_feed_row, parent, false);
        context = parent.getContext();
        return new FootballFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedViewHolder holder, int position) {
        String text = data[position];
        holder.newsFeedLabel.setText(text);
        holder.newFeedImage.setImageResource(images[position]);
        holder.categoryLabel.setText(category[position]);
        holder.newsFeedRelativeLayout.getLayoutParams().width = (screenWidth / 2) - UIDisplayUtil.dpToPx(4, context);
        int marginFeedRow = (int) context.getResources().getDimension(R.dimen.football_feed_row_margin);
        int marginBanterRow = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        int footballToolbarMarginBottom = (int) context.getResources().getDimension(R.dimen.football_toolbar_margin_bottom);
        int footballToolbarMarginMargin = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        // marginFeedRow* 4 because of padding in grid view (2 grids).
        // marginBanterRow*2 : single grid.
        holder.newsFeedRelativeLayout.getLayoutParams().height = (screenHeight - heightsToBeRemoved) / 2 - (marginFeedRow * 4) - (marginBanterRow * 2) - footballToolbarMarginBottom - footballToolbarMarginMargin;
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            holder.gradientRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(bgColor[position]));
        } else {
            holder.gradientRelativeLayout.setBackground(context.getResources().getDrawable(bgColor[position]));
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
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

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}