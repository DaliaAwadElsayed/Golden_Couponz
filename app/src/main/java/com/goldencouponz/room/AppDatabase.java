package com.goldencouponz.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {SavedData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract Golden taskDao();

}
