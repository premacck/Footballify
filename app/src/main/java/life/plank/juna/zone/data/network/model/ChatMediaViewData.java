package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 07-03-2018.
 */
public class ChatMediaViewData {
    private String mediaData;
    private boolean isSelected;
    private boolean isImage;
    private int mediaType;

    public ChatMediaViewData(String mediaData, boolean selected, boolean image) {
        this.mediaData = mediaData;
        this.isSelected = selected;
        this.isImage = image;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isImage() {
        return isImage;
    }

    public String getMediaData() {
        return mediaData;
    }
}