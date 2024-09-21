package com.github.libretube.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.libretube.db.obj.LocalBlock
import com.github.libretube.db.obj.LocalSubscription

@Dao
interface LocalBlockDao {
    @Query("SELECT * FROM localBlock")
    suspend fun getAll(): List<LocalBlock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localBlock: LocalBlock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(localBlocks: List<LocalBlock>)

    @Query("DELETE FROM localBlock WHERE channelId = :channelId")
    suspend fun delete(channelId: String)

    @Query("SELECT EXISTS(SELECT * FROM localBlock WHERE channelId = :channelId)")
    suspend fun includes(channelId: String): Boolean
}
