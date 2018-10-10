package life.plank.juna.zone.data.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class FootballTeam(
        @PrimaryKey var id: Int?,
        var foreignId: Int?,
        var name: String?,
        var isNationalTeam: Boolean?,
        var founded: Int?,
        var logoLink: String
) : Parcelable