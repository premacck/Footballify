package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastTransaction(
        var date: String,
        var type: String,
        var debit: Float,
        var credit: Float,
        var balance: Float
) : Parcelable