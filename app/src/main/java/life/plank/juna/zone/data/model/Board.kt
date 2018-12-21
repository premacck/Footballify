package life.plank.juna.zone.data.model

import android.os.Parcelable
import kotlinx.android.parcel.*
import java.util.*

@Parcelize
data class Board(
        var id: String,
        var displayName: String?,
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
    constructor(displayName: String, boardType: String, zone: String, description: String, color: String) :
            this("", displayName, boardType, true, null, zone, description, color, User(), "", null, null, null)

    constructor(displayName: String) : this("", displayName, "", true, null, "", "", "", User(), "", null, null, null)
}