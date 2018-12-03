package life.plank.juna.zone.util.view;

import android.util.SparseIntArray;

import life.plank.juna.zone.R;

public class EmojiHashMap {
    private static SparseIntArray emojiMap = new SparseIntArray();

    static {
        emojiMap.put(0x1F609, R.drawable.ic_emoji_winking);
        emojiMap.put(0x1F620, R.drawable.ic_emoji_angry);
        emojiMap.put(0x1F922, R.drawable.ic_emoji_nauseated);
        emojiMap.put(0x1F60D, R.drawable.ic_emoji_heart_eyes);
        emojiMap.put(0x1F92F, R.drawable.ic_emoji_exploding_head);
        emojiMap.put(0x1F604, R.drawable.ic_emoji_happy);
        emojiMap.put(0x1F610, R.drawable.ic_emoji_neutral);
        emojiMap.put(0x1F910, R.drawable.ic_emoji_zipped_mouth);
        emojiMap.put(0x1F917, R.drawable.ic_emoji_hugging);
        emojiMap.put(0x1F92E, R.drawable.ic_emoji_vomiting);
        emojiMap.put(0x1F924, R.drawable.ic_emoji_drool);
        emojiMap.put(0x1F92B, R.drawable.ic_emoji_shush);
    }

    public static SparseIntArray getEmojiHashMap() {
        return emojiMap;
    }
}
