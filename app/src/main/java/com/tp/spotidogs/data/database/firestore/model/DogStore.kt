package com.tp.spotidogs.data.database.firestore.model

import com.tp.spotidogs.domain.model.Dog

data class DogStore(
    val id: Int? = null,
    val imageUrl: String? = null,
    val iconFavorite: Boolean? = null
)

fun DogStore.toDomain() = Dog(imageUrl = this.imageUrl!!, iconFavorite = this.iconFavorite!!)
