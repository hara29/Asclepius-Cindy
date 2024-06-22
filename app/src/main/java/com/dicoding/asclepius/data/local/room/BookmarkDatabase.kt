package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.*
import com.dicoding.asclepius.data.local.entity.Bookmark

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase: RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    companion object {
        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        fun getDatabase(context: Context): BookmarkDatabase {
            val tempInstance = INSTANCE
            if (tempInstance !== null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDatabase::class.java,
                    "bookmark_database")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}