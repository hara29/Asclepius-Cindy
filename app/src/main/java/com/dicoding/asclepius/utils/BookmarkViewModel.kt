package com.dicoding.asclepius.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.entity.Bookmark
import com.dicoding.asclepius.data.local.room.BookmarkDatabase
import com.dicoding.asclepius.repository.BookmarkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Bookmark>>
    val resultSuccesBookmark = MutableLiveData<Boolean>()
    val resultDeleteBookmark = MutableLiveData<Boolean>()
    private val repository: BookmarkRepository

    init {
        val bookmarkDao = BookmarkDatabase.getDatabase(application).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        readAllData = repository.readAllData
    }

    private fun insert(bookmark: Bookmark, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(bookmark)
            callback()
        }
    }

    private fun delete(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(bookmark)
            resultDeleteBookmark.postValue(true)
        }
    }

    fun setBookmark(image: String, label: String, score: String, isBookmark: Boolean) {
        val bookmark = Bookmark(image.hashCode(), image, label, score, true)
        if (isBookmark) {
            delete(bookmark)
        } else {
            insert(bookmark) {
                resultSuccesBookmark.postValue(true)
            }
        }
    }

    fun findBookmarkItem(id: Int, listenBookmark: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = repository.findById(id)
            if (item != null) {
                listenBookmark()
            }
        }
    }
}
