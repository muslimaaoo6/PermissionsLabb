package com.example.permissionslab

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Qaysi taskni sinab ko'rmoqchi bo'lsangiz, shuning nomini yozasiz:
                    // Task2NetworkScreen()
                    // Task3CameraScreen()
                    // Task4OverlayScreen()
                    // Task5LocationScreen()
                    Task6NotificationScreen()
                }
            }
        }
    }
}


// Task 2: Normal Permissions (Network State)

@Composable
fun Task2NetworkScreen() {
    val context = LocalContext.current
    var isOnline by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = if (isOnline) "Status: Online (Normal Permission)" else "Status: Offline",
            fontSize = 20.sp
        )
    }
}


// Task 3: Camera Permission (Runtime)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Task3CameraScreen() {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            cameraPermissionState.status.isGranted -> {
                Text(text = "Camera Permission Granted! 📸", fontSize = 20.sp)
            }
            cameraPermissionState.status.shouldShowRationale -> {
                Text(text = "Camera is needed to capture photos.", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request Permission")
                }
            }
            else -> {
                Text(text = "Camera permission required.", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
    }
}


// Task 4: Special Permission (Overlay)

@Composable
fun Task4OverlayScreen() {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(Settings.CanDrawOverlays(context))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (hasPermission) "Overlay Granted! ✅" else "Overlay Required ⚠️",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (!hasPermission) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                context.startActivity(intent)
            }
        }) {
            Text(text = if (hasPermission) "Active" else "Open Settings")
        }
    }
}


// Task 5: Location Permissions (Fine & Coarse)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Task5LocationScreen() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (locationPermissionsState.allPermissionsGranted) {
            Text(text = "Location Permissions Granted! 📍", fontSize = 20.sp)
        } else {
            Text(text = "Location access is required.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text("Grant Location Permissions")
            }
        }
    }
}


// Task 6: Notification Permission (Android 13+)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Task6NotificationScreen() {
    // Android 13+ (API 33) uchun POST_NOTIFICATIONS ruxsati
    val permissionState = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (permissionState == null || permissionState.status.isGranted) {
            Text(text = "Notifications Allowed! 🔔", fontSize = 20.sp)
        } else {
            Text(text = "Notification permission is needed.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Grant Notification Permission")
            }
        }
    }
}