package com.alberto.prueba.domain

import com.alberto.prueba.domain.model.Location

interface LocationRepository {
    suspend fun getUserLocation(): Result<Location>
}