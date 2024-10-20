package com.tp.spotidogs.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tp.spotidogs.data.database.room.dao.DogsDao
import com.tp.spotidogs.data.database.room.entities.DogEntity

@Database(entities = [DogEntity::class], version = 1)
abstract class DogsDataBase : RoomDatabase() {
    abstract fun getDogDao(): DogsDao
}