package life.plank.juna.zone.view.adapter.post;

import com.ahamed.multiviewadapter.DataListManager;
import com.ahamed.multiviewadapter.RecyclerAdapter;
import com.bumptech.glide.RequestManager;

import java.util.List;

import life.plank.juna.zone.data.model.FeedItemComment;
import life.plank.juna.zone.view.adapter.post.binder.PostCommentBinder;
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment;

public class PostCommentAdapter extends RecyclerAdapter {

    private final DataListManager<FeedItemComment> commentDataManager;

    public PostCommentAdapter(RequestManager glide, BaseCommentContainerFragment commentContainerFragment, String fragment) {
        setExpandableMode(EXPANDABLE_MODE_MULTIPLE);
        commentDataManager = new DataListManager<>(this);
        addDataManager(commentDataManager);
        registerBinder(new PostCommentBinder(glide, commentContainerFragment, fragment));
    }

    public void setComments(List<FeedItemComment> feedItemComments) {
        commentDataManager.set(feedItemComments);
    }

    public void updateComments(List<FeedItemComment> feedItemComments) {
        commentDataManager.addAll(feedItemComments);
    }

    public void addComment(FeedItemComment comment) {
        if (comment != null) commentDataManager.add(0, comment);
    }

    public void onReplyPostedOnComment(int position, FeedItemComment feedItemComment) {
        commentDataManager.set(position, feedItemComment);
    }
}