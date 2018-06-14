package life.plank.juna.zone.data.network.model.firebaseModel;


public class BoardNotification {

    private NotificationFeedItem FeedItem;
    private Activity Activity;

    public NotificationFeedItem getFeedItem() {
        return FeedItem;
    }

    public void setFeedItem(NotificationFeedItem feedItem) {
        FeedItem = feedItem;
    }

    public life.plank.juna.zone.data.network.model.firebaseModel.Activity getActivity() {
        return Activity;
    }

    public void setActivity(life.plank.juna.zone.data.network.model.firebaseModel.Activity activity) {
        Activity = activity;
    }
}