package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-prachi on 2/11/2018.
 */

public class FootballFeedCommentAdapter extends RecyclerView.Adapter<FootballFeedCommentAdapter.FootballFeedCommentViewHolder> {
    private Context context;
    private ArrayList<String> commentList;

    FootballFeedCommentAdapter(Context context, ArrayList<String> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public FootballFeedCommentAdapter.FootballFeedCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.football_comment_feed_row, parent, false);
        return new FootballFeedCommentAdapter.FootballFeedCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedCommentAdapter.FootballFeedCommentViewHolder holder, int position) {
        holder.commetTextView.setText(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class FootballFeedCommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_time_text_view)
        TextView commentTimeTextView;
        @BindView(R.id.comment_text_view)
        TextView commetTextView;

        FootballFeedCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
