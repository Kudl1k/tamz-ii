package cz.kudladev.tamziikmp.permissions

import androidx.compose.runtime.Composable
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenEvent
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted

@Composable
actual fun RequestLocationPermission(onEvent: (WeatherScreenEvent) -> Unit) {
    val locationManager = CLLocationManager()
    println("Requesting location permission")
    when (CLLocationManager.authorizationStatus()) {
        kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse -> {
            // Permission already granted
            onEvent(WeatherScreenEvent.TogglePermissions)
            println("Permission already granted")
        }
        kCLAuthorizationStatusNotDetermined -> {
            // Request permission
            locationManager.requestWhenInUseAuthorization() // Or requestAlwaysAuthorization()
            println("Requesting permission")
        }
        kCLAuthorizationStatusDenied, kCLAuthorizationStatusRestricted -> {
            // Handle denied or restricted cases
            onEvent(WeatherScreenEvent.TogglePermissions) // Or show an alert to guide the user to settings
            println("Permission denied or restricted")
        }
        else -> {
            // Handle other cases
            println("Unknown authorization status")
        }
    }
}