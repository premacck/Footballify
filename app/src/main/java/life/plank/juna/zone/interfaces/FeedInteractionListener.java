package life.plank.juna.zone.interfaces;

import life.plank.juna.zone.data.network.model.FeedItemComment;

public interface FeedInteractionListener {

    void onPostLiked();

    void onPostUndoLiked();

    void onPostDisliked();

    void onPostUndoDisliked();

    void onCommentLiked();

    void onCommentDisliked();

    void onPostCommentOnFeed();

    void onPostReplyOnComment(String reply, int position, FeedItemComment comment);
}
