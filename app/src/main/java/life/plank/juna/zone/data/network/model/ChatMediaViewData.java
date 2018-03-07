package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 07-03-2018.
 */
public class ChatMediaViewData {
    private String mediaData;
    private boolean selected;
    private boolean image;
    private int mediaType;

    public ChatMediaViewData(String mediaData, boolean selected, boolean image) {
        this.mediaData = mediaData;
        this.selected = selected;
        this.image = image;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isImage() {
        return image;
    }

    public String getMediaData() {
        return mediaData;
    }
}