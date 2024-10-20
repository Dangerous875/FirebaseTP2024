package com.tp.spotidogs.data.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tp.spotidogs.data.database.room.entities.DogEntity

@Dao
interface DogsDao {

    @Query("SELECT * FROM favorite_table")
    suspend fun getAllDogs():List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDogDataBase(dog : DogEntity)

    @Query("DELETE FROM favorite_table WHERE imageUrl LIKE :imageUrl")
    suspend fun deleteDogByUrl(imageUrl: String): Int

    @Query("DELETE FROM favorite_table")
    suspend fun deleteAllFavoritesBreed()
}