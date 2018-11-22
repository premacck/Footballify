package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class Board(
        var id: String,
        var displayname: String?,
        var name: String?,
        var boardType: String,
        var isActive: Boolean = false,
        var boardEvent: BoardEvent?,
        var zone: String?,
        var description: String?,
        var color: String?,
        var owner: User = User(),
        var boardIconUrl: String?,
        var interactions: Interaction?,
        var startDate: @RawValue Date?,
        var endDate: @RawValue Date?
) : Parcelable {
    constructor(name: String, boardType: String, zone: String, description: String, color: String) :
            this("", "", name, boardType, true, null, zone, description, color, User(), "", null, null, null)

    constructor(name: String) : this("", "", name, "", true, null, "", "", "", User(), "", null, null, null)
}