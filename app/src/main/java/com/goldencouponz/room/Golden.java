package com.goldencouponz.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Golden {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SavedData task);

    @Query("DELETE FROM SavedData WHERE type LIKE :type")
    void deleteAll(String type);

    @Query("SELECT * FROM SavedData WHERE type LIKE :type ")
    public List<SavedData> getPastSearches(String type);

    @Query("DELETE FROM SavedData WHERE id NOT IN(SELECT MIN(id) FROM SavedData GROUP BY word) ")
    void deleteDuplicates();
}


