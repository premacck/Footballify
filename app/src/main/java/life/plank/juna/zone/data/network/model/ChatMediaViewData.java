package life.plank.juna.zone.data.network.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by plank-niraj on 07-03-2018.
 */
@Data
public class ChatMediaViewData {
    private String mediaData;
    private boolean isSelected;
    private boolean isImage;
    private int mediaType;

    public ChatMediaViewData(String mediaData, boolean isSelected, boolean isImage) {
        this.mediaData = mediaData;
        this.isSelected = isSelected;
        this.isImage = isImage;
    }
}