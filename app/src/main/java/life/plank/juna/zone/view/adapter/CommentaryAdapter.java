package life.plank.juna.zone.view.adapter;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
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
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.util.BaseRecyclerView;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class CommentaryAdapter extends BaseRecyclerView.Adapter<CommentaryAdapter.CommentsViewHolder> {

    private static final Typeface RAJDHANI_BOLD = Typeface.createFromAsset(ZoneApplication.getContext().getAssets(), "rajdhani_bold.ttf");

    private List<Commentary> commentaries;

    public CommentaryAdapter() {
        commentaries = new ArrayList<>();
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
            String timeText;
            timeText = "" + (commentary.getExtraMinute() > 0 ?
                    commentary.getMinute() + " + " + commentary.getExtraMinute() :
                    commentary.getMinute());
            timeView.setText(timeText);

            commentaryView.setText(commentary.getComment());
        }

        private SpannableString getString(String boldText, String normalText) {
            normalText = normalText.replace(boldText, "");
            SpannableString string = new SpannableString(boldText + normalText);
            string.setSpan(new StyleSpan(RAJDHANI_BOLD.getStyle()), 0, boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
            return string;
        }

        private String getHighlights(ArrayList<String> highlights) {
            return TextUtils.join(", ", highlights);
        }
    }
}