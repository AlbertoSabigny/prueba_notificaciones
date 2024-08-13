package com.alberto.prueba.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.usecase.GetAllNotificationsUseCase
import com.alberto.prueba.domain.usecase.SaveNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getAllNotificationsUseCase: GetAllNotificationsUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val notifications: StateFlow<List<NotificationEntity>> = _notifications

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Log.d("NotificationViewModel", "Inicializando ViewModel")
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            Log.d("NotificationViewModel", "Iniciando carga de notificaciones")
            _isLoading.value = true
            getAllNotificationsUseCase()
                .catch { e ->
                    Log.e("NotificationViewModel", "Error al cargar notificaciones", e)
                    _error.value = "Error al cargar notificaciones: ${e.message}"
                    _isLoading.value = false
                }
                .collect { notificationList ->
                    Log.d("NotificationViewModel", "Notificaciones cargadas: ${notificationList.size}")
                    _notifications.value = notificationList
                    _isLoading.value = false
                }
        }
    }

    fun saveNotification(notification: NotificationEntity) {
        viewModelScope.launch {
            try {
                Log.d("NotificationViewModel", "Guardando notificación: $notification")
                saveNotificationUseCase(notification)
                Log.d("NotificationViewModel", "Notificación guardada, recargando lista")
                loadNotifications() // Recargar la lista después de guardar
            } catch (e: Exception) {
                Log.e("NotificationViewModel", "Error al guardar la notificación", e)
                _error.value = "Error al guardar la notificación: ${e.message}"
            }
        }
    }
}