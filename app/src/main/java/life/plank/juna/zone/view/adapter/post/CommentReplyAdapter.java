package life.plank.juna.zone.view.adapter.post;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedItemCommentReply;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.PostCommentReplyViewHolder> {

    private RequestManager glide;
    private List<FeedItemCommentReply> replies;

    public CommentReplyAdapter(RequestManager glide, List<FeedItemCommentReply> replies) {
        this.glide = glide;
        this.replies = replies;
    }

    @NonNull
    @Override
    public PostCommentReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_comment, parent, false);
        return new PostCommentReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostCommentReplyViewHolder holder, int position) {
        holder.commentTextView.setText(replies.get(position).getMessage());
        holder.profileNameTextView.setText(replies.get(position).getCommenterDisplayName());
        glide.load(replies.get(position).getCommenterProfilePicUrl())
                .apply(RequestOptions.overrideOf((int) getDp(20), (int) getDp(20)))
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