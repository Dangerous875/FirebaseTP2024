package com.tp.spotidogs.data.database.firestore.model

import androidx.room.ColumnInfo

data class DogStore(
    val id: Int? = null,
    val imageUrl: String? = null,
    val iconFavorite: Boolean? = null
)
