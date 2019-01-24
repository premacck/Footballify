package life.plank.juna.zone.data.model.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoinPack(
        var coinCount: Int,
        var earlierPrice: Float,
        var currentPrice: Float
) : Parcelable