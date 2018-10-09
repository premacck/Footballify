package life.plank.juna.zone.data.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.FootballTeam;

@Database(entities = {FootballTeam.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {

    private static final String ROOM_DB_NAME = "junaDatabase.db";
    private static RoomDb INSTANCE;

    RoomDb() {
//        Reflection safety
        if (INSTANCE != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class");
        }
    }

    public static RoomDb getInstance() {
        if (INSTANCE == null) {
//            synchronized block and a second null-check for thread safety
            synchronized (RoomDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ZoneApplication.getContext(), RoomDb.class, ROOM_DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    //    returning same instance when serializing/de-serializing
    protected RoomDb readResolve() {
        return getInstance();
    }
}