package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.ViewHolder> {

    //TODO:Will be replaced with data from the backend
    private String[] data = new String[0];
    String[] category = {"TEAMS","LEAGUES/CUPS","PUNDITS","LIVEZONE","TEAMS","LEAGUES/CUPS","PUNDITS","LIVEZONE"};
    int[] images = {R.drawable.ic_third_dummy, R.drawable.ic_second_dummy,R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image,R.drawable.ic_third_dummy, R.drawable.ic_second_dummy,R.drawable.ic_fourth_dummy, R.drawable.ic_football_dummy_image};
    int[] bgColor = {R.drawable.football_header_orange_gradient, R.drawable.football_header_green_gradient,R.drawable.football_header_blue_gradient, R.drawable.football_header_purple_gradient,R.drawable.football_header_orange_gradient, R.drawable.football_header_green_gradient,R.drawable.football_header_blue_gradient, R.drawable.football_header_purple_gradient};
    private Context context;
    private LayoutInflater mInflater;
    int screenHeight ,screenWidth;
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_label)
        TextView newsFeedLabel;
        @BindView(R.id.news_feed_image)
        ImageView newFeedImage;
        @BindView(R.id.gradient_header)
        RelativeLayout gradientRelativeLayout;
        @BindView(R.id.category)
        TextView categoryLabel;
        @BindView(R.id.relative_layout_container)
        RelativeLayout relativeLayoutContainer;
        @BindView(R.id.football_feed_card)
        CardView footbalFeedCardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedAdapter(Context context, String[] data, int height, int width) {
        screenHeight = height;
        screenWidth = width;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.football_feed_row, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = data[position];
        holder.newsFeedLabel.setText(text);
        holder.newFeedImage.setImageResource(images[position]);
        holder.categoryLabel.setText(category[position]);
        holder.relativeLayoutContainer.getLayoutParams().width = (screenWidth /2) - dpToPx(4);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            holder.gradientRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(bgColor[position]) );
        } else {
            holder.gradientRelativeLayout.setBackground(context.getResources().getDrawable(bgColor[position]));
        }

    }

    /**
     * Dp to pixel conversion.
     * @param dp : dp to be converted
     * @return pixel : Converted value.
     */
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}