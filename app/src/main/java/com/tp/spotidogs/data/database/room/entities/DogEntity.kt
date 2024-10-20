package com.tp.spotidogs.data.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tp.spotidogs.data.database.firestore.model.DogStore

@Entity(tableName = "favorite_table")
data class DogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "iconFavorite") var iconFavorite: Boolean
)


fun DogEntity.toFireStore() =
    DogStore(id = this.id, imageUrl = this.imageUrl, iconFavorite = this.iconFavorite)
