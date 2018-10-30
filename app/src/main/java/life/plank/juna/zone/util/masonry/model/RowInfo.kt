package life.plank.juna.zone.util.masonry.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class RowInfo(
        val rowHeight: Int,
        val items: @RawValue MutableList<RowItem>,
        val spaceLeft: Float
) : Parcelable
