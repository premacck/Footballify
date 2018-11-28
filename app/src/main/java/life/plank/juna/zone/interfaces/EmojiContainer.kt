package life.plank.juna.zone.interfaces

import life.plank.juna.zone.data.model.Emoji

interface EmojiContainer {
    fun onEmojiPosted(emoji: Emoji)
}