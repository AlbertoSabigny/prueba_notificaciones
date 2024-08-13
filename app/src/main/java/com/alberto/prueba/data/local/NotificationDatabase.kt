package com.alberto.prueba.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase: RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao
}