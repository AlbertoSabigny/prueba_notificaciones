package com.alberto.prueba.data.remote

import android.util.Log
import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.usecase.SaveNotificationUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var saveNotificationUseCase: SaveNotificationUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        Log.d(TAG, "MyFirebaseMessagingService initialized")
    }
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyFirebaseMessagingService onCreate called")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived ")


        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification : ${it.body}")
        }

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message: ${remoteMessage.data}")
        }
        Log.d(TAG, "Mensaje recibido: ${remoteMessage.notification}")

        val title = remoteMessage.notification?.title ?: ""
        val body = remoteMessage.notification?.body ?: ""
        val notificationEntity = NotificationEntity(
            title = title,
            message = body
        )

        Log.d(TAG, "Creada NotificationEntity: $notificationEntity")


        serviceScope.launch {
            try {
                Log.d(TAG, "Intentando guardar la notificación")
                saveNotificationUseCase(notificationEntity)
                Log.d(TAG, "Notificación guardada ")
            } catch (e: Exception) {
                Log.e(TAG, "Error al guardar la notificación", e)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo token de FCM: $token")

    }

    companion object {
        private const val TAG = "FirebaseMessaging"
    }
}