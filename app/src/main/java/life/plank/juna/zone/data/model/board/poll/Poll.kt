package life.plank.juna.zone.data.model.board.poll

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Poll(
        var id: Long,
        var boardId: String,
        var question: String,
        var totalVotes: Long = 0,
        var userSelection: Int = 0,
        var choices: List<PollChoice> = ArrayList()
) : Parcelable