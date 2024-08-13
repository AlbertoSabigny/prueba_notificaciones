package com.alberto.prueba.domain.usecase

import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Flow<List<NotificationEntity>> {
        return repository.getAllNotifications()
    }
}