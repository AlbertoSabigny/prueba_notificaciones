package com.alberto.prueba.domain.usecase

import com.alberto.prueba.domain.LocationRepository
import com.alberto.prueba.domain.model.Location
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<Location> = locationRepository.getUserLocation()
}