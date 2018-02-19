package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.util.GlobalVariable;

/**
 * Created by plank-prachi on 2/11/2018.
 */

public class FootballFeedCommentAdapter extends RecyclerView.Adapter<FootballFeedCommentAdapter.FootballFeedCommentViewHolder> {

    private Context context;
    private LayoutInflater mInflater;

    public class FootballFeedCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_comment_time)
        TextView textCommentTime;

        @BindView(R.id.text_comment)
        TextView textComment;

        public FootballFeedCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public FootballFeedCommentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public FootballFeedCommentAdapter.FootballFeedCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.football_comment_feed_row, parent, false);
        context = parent.getContext();
        return new FootballFeedCommentAdapter.FootballFeedCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FootballFeedCommentAdapter.FootballFeedCommentViewHolder holder, int position) {
        holder.textComment.setText(R.string.comment_data);
    }

    @Override
    public int getItemCount() {
        return 5;
    }


}
