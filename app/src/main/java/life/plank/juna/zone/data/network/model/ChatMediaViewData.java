package life.plank.juna.zone.data.network.model;

/**
 * Created by plank-niraj on 07-03-2018.
 */
public class ChatMediaViewData {
    private String mediaData;
    private boolean isMediaSelected;
    private boolean mediaTypeImage;
    private int mediaType;

    public ChatMediaViewData(String mediaData, boolean selected, boolean image) {
        this.mediaData = mediaData;
        this.isMediaSelected = selected;
        this.mediaTypeImage = image;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isMediaSelected() {
        return isMediaSelected;
    }

    public void setMediaSelected(boolean mediaSelected) {
        this.isMediaSelected = mediaSelected;
    }

    public boolean isMediaTypeImage() {
        return mediaTypeImage;
    }

    public String getMediaData() {
        return mediaData;
    }
}