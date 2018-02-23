package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnLongClickListener;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.util.helper.PopUpWindowHelper;
import life.plank.juna.zone.view.activity.FootballFeedDetailActivity;

public class FootballFeedAdapter extends RecyclerView.Adapter<FootballFeedAdapter.FootballFeedViewHolder> {

    @BindView(R.id.reaction_like)
    ImageView reactionLike;
    private int popUpPosition;
    private Context context;
    private LayoutInflater mInflater;
    private int screenHeight;
    private int screenWidth;
    private int heightsToBeRemoved;
    private int imageWidth;
    private int popupImageWidth;
    private int gridWidth;
    public PopupWindow popupWindow;
    private List<FootballFeed> footballFeedList = new ArrayList<>();
    private OnLongClickListener onLongClickListner;
    private int popupImageHeight;

    public FootballFeedAdapter(Context context, int height, int width, int heightsToBeRemoved) {
        screenHeight = height;
        screenWidth = width;
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
        holder.newsFeedLabel.setText(footballFeed.getTitle());
        holder.moreImageView.post(() -> popupImageWidth = holder.moreImageView.getWidth());
        holder.newsFeedImage.post(() -> popupImageHeight = holder.newsFeedImage.getHeight());
        holder.moreImageView.setOnClickListener((View view) -> feedPopupMenu(view, holder));

        if (footballFeed.getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.newsFeedImage);
        } else {
            holder.newsFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FootballFeedDetailActivity.class);
                intent.putExtra(String.valueOf(R.string.position), String.valueOf(position));
                context.startActivity(intent);
            }
        });
        gridWidth = (screenWidth / 2) - UIDisplayUtil.dpToPx(4, context);
        holder.newsFeedRelativeLayout.getLayoutParams().width = (screenWidth / 2) - UIDisplayUtil.dpToPx(4, context);
        int marginFeedRow = (int) context.getResources().getDimension(R.dimen.football_feed_row_margin);
        int marginBanterRow = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);
        int footballToolbarMarginBottom = (int) context.getResources().getDimension(R.dimen.football_toolbar_margin_bottom);
        int footballToolbarMarginMargin = (int) context.getResources().getDimension(R.dimen.football_banter_view_margin);

        int gridHeight = (screenHeight - heightsToBeRemoved) / 2 - (marginFeedRow * 4) - (marginBanterRow * 2) - footballToolbarMarginBottom - footballToolbarMarginMargin;

        // marginFeedRow* 4 because of padding in grid view (2 grids).
        // marginBanterRow*2 : single grid.
        final int sdk = android.os.Build.VERSION.SDK_INT;

        //TODO: Will be uncommented after backend returns data
//        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            holder.gradientRelativeLayout.setBackgroundDrawable(context.getResources().getDrawable(bgColor[position]));
//        } else {
//            holder.gradientRelativeLayout.setBackground(context.getResources().getDrawable(bgColor[position]));
//        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                onLongClickListner.onLongClick(position);
                holder.pinImage.setVisibility(View.VISIBLE);
                return true;
            }
        });
        holder.likeImage.post(() -> imageWidth = holder.likeImage.getWidth());

        // TODO: 13-02-2018 remove this code, after replacing proper images
        if (footballFeedList.get(position).getReactionType() != -1) {
            holder.likeImage.setImageResource(footballFeedList.get(position).getReactionType());
            holder.likeImage.getLayoutParams().width = imageWidth;
            holder.likeImage.getLayoutParams().height = imageWidth;
        }
        holder.likeImage.setOnClickListener((View view) -> displayPopup(position, holder, gridHeight, view));
        if (footballFeed.getThumbnail() != null) {
            Picasso.with(context)
                    .load(footballFeed.getThumbnail().getImageUrl())
                    .fit()
                    .into(holder.newsFeedImage);
        } else {
            holder.newsFeedImage.setImageResource(R.drawable.ic_third_dummy);
        }
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, FootballFeedDetailActivity.class);
            intent.putExtra("position", String.valueOf(position));
            context.startActivity(intent);
        });
    }

    private void displayPopup(int position, FootballFeedViewHolder holder, int gridHeight, View view) {
        popUpPosition = position;
        int[] locationRecyclerContainer = new int[2];
        holder.newsFeedRelativeLayout.getLocationInWindow(locationRecyclerContainer);
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.reaction_layout, null);
        ButterKnife.bind(this, bubbleLayout);
        int[] locationLIkeView = new int[2];
        holder.likeImage.getLocationInWindow(locationLIkeView);
        bubbleLayout.setArrowPosition(
                locationLIkeView[0] - locationRecyclerContainer[0] -
                        (bubbleLayout.getArrowWidth() / 2) + (imageWidth / 2)
        );
        PopUpWindowHelper<BubbleLayout> popUpWindowHelper = new PopUpWindowHelper<>();
        popUpWindowHelper.setView(bubbleLayout);
        popUpWindowHelper.setPopUpHeight(gridHeight / 4);
        popUpWindowHelper.setPopUpWidth(holder.newsFeedRelativeLayout.getLayoutParams().width);
        popUpWindowHelper.setParentView(view);
        popUpWindowHelper.setPopUpLocationX(locationRecyclerContainer[0]);
        popUpWindowHelper.setPopUpLocationY(locationLIkeView[1] - (UIDisplayUtil.dpToPx(2, context) + gridHeight / 4));
        popupWindow = popUpWindowHelper.genericPopUpWindow(context);
    }

    @OnClick({R.id.reaction_like, R.id.reaction_angry, R.id.reaction_cry, R.id.reaction_smile})
    public void onReactionsClicked(View view) {
        if (popupWindow != null)
            popupWindow.dismiss();
        // TODO: 13-02-2018 Get the new images with different image size
        switch (view.getId()) {
            case R.id.reaction_like:
                footballFeedList.get(popUpPosition).setReactionType(R.drawable.ic_reactions_heart);
                break;

            case R.id.reaction_angry:
                footballFeedList.get(popUpPosition).setReactionType(R.drawable.ic_reactions_angry);
                break;

            case R.id.reaction_cry:
                footballFeedList.get(popUpPosition).setReactionType(R.drawable.ic_reactions_cry);
                break;

            case R.id.reaction_smile:
                footballFeedList.get(popUpPosition).setReactionType(R.drawable.ic_reactions_smile);
                break;
        }
        notifyItemChanged(popUpPosition);
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

    public class FootballFeedViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_feed_label)
        TextView newsFeedLabel;
        @BindView(R.id.news_feed_image)
        ImageView newsFeedImage;
        @BindView(R.id.gradient_header)
        RelativeLayout gradientRelativeLayout;
        @BindView(R.id.category)
        TextView categoryLabel;
        @BindView(R.id.relative_layout_container)
        RelativeLayout newsFeedRelativeLayout;
        @BindView(R.id.football_feed_card)
        CardView newsFeedCardView;
        @BindView(R.id.pin_image)
        ImageView pinImage;
        @BindView(R.id.like_image)
        ImageView likeImage;
        @BindView(R.id.like_label)
        TextView likeLabelTextView;
        @BindView(R.id.more_option)
        ImageView moreImageView;

        FootballFeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListner = onLongClickListener;
    }

    private void feedPopupMenu(View view, FootballFeedViewHolder footballFeedViewHolder) {
        PopupWindow popupWindowMenu;
        LinearLayout inflateLinearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.football_feed_popup_layout, null);
        ListView listView = inflateLinearLayout.findViewById(R.id.list_item_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.popup_menu_list_view_item, R.id.list_item_text,
                context.getResources().getStringArray(R.array.football_feed_popup_array));
        listView.setAdapter(adapter);
        PopUpWindowHelper<LinearLayout> popUpWindowHelper = new PopUpWindowHelper<>();
        popUpWindowHelper.setView(inflateLinearLayout);
        popUpWindowHelper.setParentView(view);
        popUpWindowHelper.setPopUpWidth(gridWidth);
        popUpWindowHelper.setPopUpHeight(popupImageHeight);
        int[] locationLikeView = new int[2];
        footballFeedViewHolder.moreImageView.getLocationInWindow(locationLikeView);
        popUpWindowHelper.setPopUpLocationX(locationLikeView[0] - gridWidth + popupImageWidth);
        popUpWindowHelper.setPopUpLocationY(locationLikeView[1]);
        popupWindowMenu = popUpWindowHelper.genericPopUpWindow(context);
    }
}

