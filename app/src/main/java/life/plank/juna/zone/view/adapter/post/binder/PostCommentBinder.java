package life.plank.juna.zone.view.adapter.post.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahamed.multiviewadapter.ItemBinder;
import com.ahamed.multiviewadapter.ItemViewHolder;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.view.adapter.post.CommentReplyAdapter;

import static life.plank.juna.zone.ZoneApplication.getContext;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.hideSoftKeyboard;

public class PostCommentBinder extends ItemBinder<FeedItemComment, PostCommentBinder.PostCommentViewHolder> {

    private static String fragment = null;
    private final Picasso picasso;
    private final FeedInteractionListener listener;

    public PostCommentBinder(Picasso picasso, FeedInteractionListener listener, String fragment) {
        this.picasso = picasso;
        this.listener = listener;
        this.fragment = fragment;
    }

    @Override
    public PostCommentViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        return new PostCommentViewHolder(inflater.inflate(R.layout.item_post_comment, parent, false), this);
    }

    @Override
    public void bind(PostCommentViewHolder holder, FeedItemComment item) {
        holder.comment = item;
        holder.commentTextView.setText(item.getMessage());
        if (fragment.equals("forumFragment")) {
            holder.commentTime.setVisibility(View.VISIBLE);

        }

        holder.profileNameTextView.setText(item.getCommenterDisplayName());
        picasso.load(item.getCommenterProfilePicUrl())
                .resize((int) getDp(20), (int) getDp(20))
                .into(holder.profilePic);

        holder.likeTextView.setText(item.getHasLiked() ? R.string.unlike : R.string.like);
        holder.viewRepliesTextView.setVisibility(isNullOrEmpty(item.getReplies()) ? View.GONE : View.VISIBLE);
        holder.repliesRecyclerView.setAdapter(new CommentReplyAdapter(picasso, item.getReplies()));
        if (holder.isItemExpanded()) {
            holder.viewRepliesTextView.setText(R.string.hide_replies);
            holder.repliesRecyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.viewRepliesTextView.setText(R.string.show_replies);
            holder.repliesRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof FeedItemComment;
    }

    static class PostCommentViewHolder extends ItemViewHolder<FeedItemComment> {

        private final WeakReference<PostCommentBinder> ref;
        @BindView(R.id.profile_pic)
        ImageView profilePic;
        @BindView(R.id.profile_name_text_view)
        TextView profileNameTextView;
        @BindView(R.id.comment_text_view)
        TextView commentTextView;
        @BindView(R.id.like_text_view)
        TextView likeTextView;
        @BindView(R.id.reply_text_view)
        TextView replyTextView;
        @BindView(R.id.view_replies_text_view)
        TextView viewRepliesTextView;
        @BindView(R.id.reply_layout)
        RelativeLayout replyLayout;
        @BindView(R.id.reply_edit_text)
        EditText replyEditText;
        @BindView(R.id.post_reply)
        ImageButton postReply;
        @BindView(R.id.replies_list)
        RecyclerView repliesRecyclerView;
        @BindView(R.id.comment_time_text)
        TextView commentTime;
        private FeedItemComment comment;

        PostCommentViewHolder(View itemView, PostCommentBinder postCommentBinder) {
            super(itemView);
            this.ref = new WeakReference<>(postCommentBinder);
            ButterKnife.bind(this, itemView);

            if (fragment.equals("forumFragment")) {
                viewRepliesTextView.setVisibility(View.GONE);
//                viewRepliesTextView.setText(isItemExpanded() ? R.string.hide_replies : R.string.show_replies);
//                toggleItemExpansion();
                repliesRecyclerView.setVisibility(View.VISIBLE);

            }

        }

        @OnClick(R.id.like_text_view)
        public void likeComment() {
            likeTextView.setText(likeTextView.getText().toString().equals(getContext().getString(R.string.like)) ? R.string.unlike : R.string.like);
            ref.get().listener.onCommentLiked();
        }

        @OnClick(R.id.reply_text_view)
        public void toggleReplyOnComment() {
            replyTextView.setText(replyLayout.getVisibility() == View.VISIBLE ? R.string.reply : R.string.cancel);
            replyLayout.setVisibility(replyLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }

        @OnClick(R.id.post_reply)
        public void postReplyOnComment() {
            if (replyLayout.getVisibility() == View.VISIBLE) {
                ref.get().listener.onPostReplyOnComment(replyEditText.getText().toString(), getAdapterPosition(), comment);
            }
            replyEditText.setText(null);
            replyEditText.clearFocus();
            hideSoftKeyboard(replyEditText);
        }

        @OnClick(R.id.view_replies_text_view)
        public void ShowCommentReplies() {
            viewRepliesTextView.setText(isItemExpanded() ? R.string.hide_replies : R.string.show_replies);
            toggleItemExpansion();
            repliesRecyclerView.setVisibility(isItemExpanded() ? View.VISIBLE : View.GONE);
        }
    }
}