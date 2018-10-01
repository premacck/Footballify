package life.plank.juna.zone.view.adapter.post;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import life.plank.juna.zone.data.network.model.FeedItemComment;
import life.plank.juna.zone.interfaces.FeedInteractionListener;
import life.plank.juna.zone.view.adapter.post.binder.PostCommentBinder;

public class PostCommentAdapter extends RecyclerAdapter {

    private final DataListManager<FeedItemComment> commentDataManager;

    public PostCommentAdapter(Picasso picasso, FeedInteractionListener listener) {
        setExpandableMode(EXPANDABLE_MODE_MULTIPLE);
        commentDataManager = new DataListManager<>(this);
        addDataManager(commentDataManager);
        registerBinder(new PostCommentBinder(picasso, listener));
    }

    public void setComments(List<FeedItemComment> feedItemComments) {
        commentDataManager.set(feedItemComments);
    }

    public void updateComments(List<FeedItemComment> feedItemComments) {
        commentDataManager.addAll(feedItemComments);
    }

    public void addComment(FeedItemComment comment) {
        commentDataManager.add(comment);
    }

    public void onReplyPostedOnComment(int position, FeedItemComment feedItemComment) {
        commentDataManager.set(position, feedItemComment);
    }
}