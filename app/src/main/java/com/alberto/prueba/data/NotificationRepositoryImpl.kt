package com.alberto.prueba.data

import android.util.Log
import com.alberto.prueba.data.local.NotificationDao
import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.NotificationRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    override suspend fun saveNotification(notification: NotificationEntity) {
        Log.d(TAG, "Insertando notificación  $notification")
        notificationDao.insertNotification(notification)
        Log.d(TAG, "Notificación insertada")
    }

    override suspend fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotifications()
    }
    companion object {
        private const val TAG = "NotificationRepository"
    }

}
