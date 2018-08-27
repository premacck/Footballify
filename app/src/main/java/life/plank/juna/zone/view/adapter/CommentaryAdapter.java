package life.plank.juna.zone.view.adapter;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.util.BaseRecyclerView;

import static life.plank.juna.zone.util.AppConstants.CORNER_;
import static life.plank.juna.zone.util.AppConstants.FIRST_HALF_ENDED_;
import static life.plank.juna.zone.util.AppConstants.FREE_KICK_;
import static life.plank.juna.zone.util.AppConstants.GOAL_;
import static life.plank.juna.zone.util.AppConstants.OFFSIDE_;
import static life.plank.juna.zone.util.AppConstants.RED_CARD_;
import static life.plank.juna.zone.util.AppConstants.SECOND_HALF_ENDED_;
import static life.plank.juna.zone.util.AppConstants.SUBSTITUTION_;
import static life.plank.juna.zone.util.AppConstants.YELLOW_CARD_;
import static life.plank.juna.zone.util.UIDisplayUtil.alternateBackgroundColor;
import static life.plank.juna.zone.util.UIDisplayUtil.getDesignedString;

public class CommentaryAdapter extends BaseRecyclerView.Adapter<CommentaryAdapter.CommentsViewHolder> {

    private List<Commentary> commentaries;

    public CommentaryAdapter() {
        commentaries = new ArrayList<>();
    }

    public CommentaryAdapter(List<Commentary> commentaryList) {
        commentaries = commentaryList;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commentary, parent, false),
                this
        );
    }

    @Override
    public int getItemCount() {
        return commentaries.size();
    }

    public void update(List<Commentary> commentaries) {
        this.commentaries.addAll(commentaries);
        notifyDataSetChanged();
    }

    public void updateNew(List<Commentary> commentaries) {
        int previousIndex = this.commentaries.size();
        this.commentaries.addAll(commentaries);
        notifyItemRangeInserted(previousIndex, commentaries.size());
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    static class CommentsViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView timeView;
        @BindView(R.id.commentary)
        TextView commentaryView;

        private final WeakReference<CommentaryAdapter> ref;

        CommentsViewHolder(View itemView, CommentaryAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.ref = new WeakReference<>(adapter);
        }

        @Override
        public void bind() {
            Commentary commentary = ref.get().commentaries.get(getAdapterPosition());
            alternateBackgroundColor(itemView, getAdapterPosition());
            String timeText;
            timeText = "" + (commentary.getExtraMinute() > 0 ?
                    commentary.getMinute() + "+" + commentary.getExtraMinute() :
                    commentary.getMinute());
            timeView.setText(timeText);

            commentaryView.setText(getFormattedCommentary(commentary.getComment()));
        }

        private SpannableStringBuilder getFormattedCommentary(String rawCommentaryText) {
            if (rawCommentaryText.contains(GOAL_)) {
                return getDesignedString(GOAL_,
                        rawCommentaryText,
                        R.color.purple_timeline,
                        R.drawable.ic_goal_left,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(CORNER_)) {
                return getDesignedString(CORNER_,
                        rawCommentaryText,
                        R.color.black,
                        -1,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(SUBSTITUTION_)) {
                return getDesignedString(
                        SUBSTITUTION_,
                        rawCommentaryText,
                        R.color.black,
                        R.drawable.ic_sub_right,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(OFFSIDE_)) {
                return getDesignedString(
                        OFFSIDE_,
                        rawCommentaryText,
                        R.color.black,
                        -1,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(YELLOW_CARD_)) {
                return getDesignedString(
                        YELLOW_CARD_,
                        rawCommentaryText,
                        R.color.commentary_yellow,
                        R.drawable.yellow_right,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(RED_CARD_)) {
                return getDesignedString(
                        RED_CARD_,
                        rawCommentaryText,
                        R.color.commentary_red,
                        R.drawable.red_right,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(FREE_KICK_)) {
                return getDesignedString(
                        FREE_KICK_,
                        rawCommentaryText,
                        R.color.black,
                        -1,
                        true,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(FIRST_HALF_ENDED_)) {
                return getDesignedString(
                        rawCommentaryText,
                        rawCommentaryText,
                        R.color.dark_sky_blue,
                        R.drawable.ic_whistle,
                        false,
                        commentaryView
                );
            } else if (rawCommentaryText.contains(SECOND_HALF_ENDED_)) {
                return getDesignedString(
                        rawCommentaryText,
                        rawCommentaryText,
                        R.color.dark_sky_blue,
                        R.drawable.ic_whistle,
                        false,
                        commentaryView
                );
            } else {
                return getDesignedString(
                        null,
                        rawCommentaryText,
                        -1,
                        -1,
                        false,
                        commentaryView
                );
            }
        }
    }
}