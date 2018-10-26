package life.plank.juna.zone.interfaces;

import life.plank.juna.zone.data.model.FeedItemComment;

public interface FeedInteractionListener {

    void onCommentLiked();

    void onCommentDisliked();

    void onPostCommentOnFeed();

    void onPostReplyOnComment(String reply, int position, FeedItemComment comment);
}
