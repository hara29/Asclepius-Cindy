package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.asclepius.data.local.entity.Bookmark

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("SELECT * from bookmark_item")
    fun readAllData(): LiveData<List<Bookmark>>

    @Query("SELECT * from 'bookmark_item' WHERE id like :id ")
    fun findById(id: Int): Bookmark?
}