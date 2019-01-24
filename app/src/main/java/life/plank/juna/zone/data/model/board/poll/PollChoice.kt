package life.plank.juna.zone.data.model.board.poll

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PollChoice(var choice: String, var imageUrl: String? = null, var option: Int, var percentage: Int) : Parcelable