package life.plank.juna.zone.data.model

import android.arch.persistence.room.Entity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Stadium(
        var capacity: Int?,
        var name: String?,
        var id: Int?,
        var foreignId: Int?,
        var surface: String?,
        var address: String?,
        var city: String?,
        var imagePath: String?
) : Parcelable