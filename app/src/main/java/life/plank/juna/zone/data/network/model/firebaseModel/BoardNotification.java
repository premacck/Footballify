package life.plank.juna.zone.data.network.model.firebaseModel;

public class BoardNotification {

    private NotificationFeedItem FeedItem;
    private String Operation;

    public NotificationFeedItem getFeedItem() {
        return FeedItem;
    }

    public void setFeedItem(NotificationFeedItem feedItem) {
        FeedItem = feedItem;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        this.Operation = operation;
    }

}