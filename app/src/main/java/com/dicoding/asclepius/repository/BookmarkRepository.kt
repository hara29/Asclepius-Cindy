package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.Bookmark
import com.dicoding.asclepius.data.local.room.BookmarkDao

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {
    val readAllData: LiveData<List<Bookmark>> = bookmarkDao.readAllData()

    suspend fun insert(bookmark: Bookmark) {
        bookmarkDao.insert(bookmark)
    }

    suspend fun delete(bookmark: Bookmark){
        bookmarkDao.delete(bookmark)
    }

    fun findById(id: Int) : Bookmark? {
        return bookmarkDao.findById(id)
    }

}