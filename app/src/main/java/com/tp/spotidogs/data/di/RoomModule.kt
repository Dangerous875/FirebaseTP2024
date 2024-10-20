package com.tp.spotidogs.data.di

import android.content.Context
import androidx.room.Room
import com.tp.spotidogs.data.database.room.DogsDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DOGS_DATABASE_NAME = "DogsDatabase"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, DogsDataBase::class.java,
        DOGS_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDogsDao(db: DogsDataBase) = db.getDogDao()
}