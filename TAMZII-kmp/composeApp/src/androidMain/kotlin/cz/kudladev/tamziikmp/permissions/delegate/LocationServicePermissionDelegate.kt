package cz.kudladev.tamziikmp.permissions.delegate

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.semantics.dismiss
import androidx.core.content.ContextCompat
import cz.kudladev.tamziikmp.permissions.model.Permission
import cz.kudladev.tamziikmp.permissions.model.PermissionState
import cz.kudladev.tamziikmp.permissions.util.CannotOpenSettingsException
import cz.kudladev.tamziikmp.permissions.util.openPage
import cz.kudladev.tamziikmp.weather.presentation.WeatherScreenEvent
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal class LocationServicePermissionDelegate(
    private val context: Context,
    private val locationManager: LocationManager,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        val granted = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return if (granted)
            PermissionState.GRANTED else PermissionState.DENIED
    }

    override suspend fun providePermission() {
        openSettingPage()
    }

    override fun openSettingPage() {
        context.openPage(
            action = Settings.ACTION_LOCATION_SOURCE_SETTINGS,
            onError = { throw CannotOpenSettingsException(Permission.LOCATION_SERVICE_ON.name) }
        )
    }
}