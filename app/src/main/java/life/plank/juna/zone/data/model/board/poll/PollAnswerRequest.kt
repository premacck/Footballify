package life.plank.juna.zone.data.model.board.poll

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PollAnswerRequest(var pollId: Long, var option: Int) : Parcelable