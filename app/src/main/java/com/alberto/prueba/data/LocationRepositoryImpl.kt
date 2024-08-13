package com.alberto.prueba.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.alberto.prueba.domain.LocationRepository
import com.alberto.prueba.domain.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationRepository {

    override suspend fun getUserLocation(): Result<Location> = withContext(Dispatchers.IO) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@withContext Result.failure(Exception("Location permission not granted"))
            }

            val locationResult = fusedLocationProviderClient.lastLocation.await()
            locationResult?.let {
                Result.success(Location(it.latitude, it.longitude))
            } ?: Result.failure(Exception("Location not available"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}