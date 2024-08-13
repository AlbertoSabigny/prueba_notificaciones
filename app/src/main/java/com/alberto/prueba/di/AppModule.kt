package com.alberto.prueba.di

import android.content.Context
import androidx.room.Room
import com.alberto.prueba.data.NotificationRepositoryImpl
import com.alberto.prueba.data.local.NotificationDao
import com.alberto.prueba.data.local.NotificationDatabase
import com.alberto.prueba.domain.NotificationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val NOTIFICATION_DATABASE_NAME = "notification_database"


    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NotificationDatabase::class.java, NOTIFICATION_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideQuoteDao(db: NotificationDatabase) = db.getNotificationDao()

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationDao: NotificationDao): NotificationRepository {
        return NotificationRepositoryImpl(notificationDao)
    }
}