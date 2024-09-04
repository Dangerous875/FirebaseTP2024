package com.tp.spotidogs.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tp.spotidogs.data.database.dao.DogsDao
import com.tp.spotidogs.data.database.entities.DogEntity

@Database(entities = [DogEntity::class], version = 1)
abstract class DogsDataBase : RoomDatabase() {
    abstract fun getDogDao(): DogsDao
}