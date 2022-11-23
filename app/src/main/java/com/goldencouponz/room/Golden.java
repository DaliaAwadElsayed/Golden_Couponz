package com.goldencouponz.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Golden {

    @Query("SELECT * FROM SavedData")
    List<SavedData> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SavedData task);

    @Delete
    void delete(SavedData task);

    @Query("SELECT * FROM SavedData WHERE type LIKE :type " )
    public List<SavedData> getPastSearches(String type);

}


