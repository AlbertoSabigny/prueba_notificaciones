package com.alberto.prueba.domain.usecase

import android.util.Log
import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.NotificationRepository
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationEntity) {
        Log.d(TAG, "Guardando notificación: $notification")
        try {
            repository.saveNotification(notification)
            Log.d(TAG, "Notificación guardada exitosamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al guardar la notificación", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "SaveNotificationUseCase"
    }
}