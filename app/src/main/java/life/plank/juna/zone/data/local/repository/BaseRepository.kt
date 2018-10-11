package life.plank.juna.zone.data.local.repository

import life.plank.juna.zone.data.local.db.RoomDb

open class BaseRepository {
    protected var roomDb: RoomDb = RoomDb.getInstance()
}