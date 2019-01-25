package life.plank.juna.zone.ui.emoji

import life.plank.juna.zone.data.model.board.Emoji

interface EmojiContainer {
    fun onEmojiPosted(emoji: Emoji)
}