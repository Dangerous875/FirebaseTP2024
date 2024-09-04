package com.tp.spotidogs.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class DogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "iconFavorite") var iconFavorite: Boolean)
