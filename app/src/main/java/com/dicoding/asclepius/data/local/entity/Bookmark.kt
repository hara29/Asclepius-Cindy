package com.dicoding.asclepius.data.local.entity

import androidx.room.*

@Entity(tableName = "bookmark_item")
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "label")
    var label: String? = null,

    @ColumnInfo(name = "score")
    var score: String? = null,

    @ColumnInfo(name = "bookmarked")
    var isbookmarked: Boolean? = null
)