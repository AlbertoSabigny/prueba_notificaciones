package com.alberto.prueba.domain

import com.alberto.prueba.data.local.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun saveNotification(notification: NotificationEntity)
    suspend fun getAllNotifications(): Flow<List<NotificationEntity>>
}