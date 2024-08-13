package com.alberto.prueba.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.alberto.prueba.data.local.NotificationEntity
import com.alberto.prueba.domain.model.Location

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val userLocation by viewModel.userLocation.collectAsState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.getUserLocation()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        LocationCard(
            userLocation = userLocation,
            onRequestLocation = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        viewModel.getUserLocation()
                    }
                    else -> {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        )
        NotificationList(notifications)
    }
}

@Composable
fun LocationCard(
    userLocation: Location?,
    onRequestLocation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ubicación del usuario",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (userLocation != null) {
                Text(
                    text = "Lat: ${userLocation.latitude}, Lon: ${userLocation.longitude}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF34495E)
                )
            } else {
                Button(
                    onClick = onRequestLocation,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB))
                ) {
                    Text("Obtener ubicación", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun NotificationList(notifications: List<NotificationEntity>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(notifications) { notification ->
            NotificationItem(notification)
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFECF0F1))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2980B9)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7F8C8D)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}