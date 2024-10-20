package com.tp.spotidogs.core

import com.tp.spotidogs.data.database.room.entities.DogEntity
import com.tp.spotidogs.domain.model.Dog


fun String.toDomain() =  Dog(this, false)

fun DogEntity.toDomain() = Dog(this.imageUrl,this.iconFavorite)

fun Dog.toDataBase() = DogEntity(imageUrl = this.imageUrl , iconFavorite = this.iconFavorite)
