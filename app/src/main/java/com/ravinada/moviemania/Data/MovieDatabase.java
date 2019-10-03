package com.ravinada.moviemania.Data;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Movie.class, exportSchema = false, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;

    public static synchronized MovieDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "Movie").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public abstract MovieDao MovieDao();

}