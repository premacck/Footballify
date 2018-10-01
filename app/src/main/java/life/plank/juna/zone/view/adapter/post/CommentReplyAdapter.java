package life.plank.juna.zone.view.adapter.post;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.FeedItemCommentReply;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.PostCommentReplyViewHolder> {

    private Picasso picasso;
    private List<FeedItemCommentReply> replies;

    public CommentReplyAdapter(Picasso picasso, List<FeedItemCommentReply> replies) {
        this.picasso = picasso;
        this.replies = replies;
    }

    @Override
    public PostCommentReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_comment, parent, false);
        return new PostCommentReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostCommentReplyViewHolder holder, int position) {
        holder.commentTextView.setText(replies.get(position).getMessage());
        holder.profileNameTextView.setText(replies.get(position).getCommenterDisplayName());
        picasso.load(replies.get(position).getCommenterProfilePicUrl())
                .resize((int) getDp(20), (int) getDp(20))
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    static class PostCommentReplyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_pic)
        ImageView profilePic;
        @BindView(R.id.profile_name_text_view)
        TextView profileNameTextView;
        @BindView(R.id.comment_text_view)
        TextView commentTextView;

        PostCommentReplyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}