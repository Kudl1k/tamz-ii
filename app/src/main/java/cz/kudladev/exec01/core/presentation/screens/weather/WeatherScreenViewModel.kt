package cz.kudladev.exec01.core.presentation.screens.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import cz.kudladev.exec01.core.data.api.WeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class WeatherScreenViewModel(
    weatherApi: WeatherApi,
    private val context: () -> Context
): ViewModel() {

    private val _state = MutableStateFlow(WeatherScreenState())
    val state = _state.asStateFlow()

    init {
        fetchLastLocation(weatherApi)
    }

    private fun fetchWeatherForecast(weatherApi: WeatherApi) {
        viewModelScope.launch {
            try {
                val weather = weatherApi.getWeatherForecast(
                    latitude = String.format("%.2f", _state.value.latitude),
                    longitude = String.format("%.2f", _state.value.longitude),
                    currentParameters = "temperature_2m,wind_speed_10m,weather_code",
                    hourlyParameters = "temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code",
                )
                Log.d("weather", weather.toString())
                _state.value = _state.value.copy(
                    weather = weather
                )
            } catch (e: HttpException) {
                Log.d("weather", "error:${e.message()}")
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            }
        }
    }

    private fun fetchLastLocation(weatherApi: WeatherApi) {
        try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context.invoke())
            val location = fusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        Log.d("location", "success")
                        val location = task.result
                        _state.value = _state.value.copy(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                        Log.d("location", "latitude: ${location.latitude}, longitude: ${location.longitude}")
                        fetchWeatherForecast(weatherApi)
                    } else {
                        Log.d("location", "error")
                        _state.value = _state.value.copy(
                            permissionsGranted = false,
                            error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                        )
                    }
                }
        } catch (e: SecurityException) {
            _state.value = _state.value.copy(
                permissionsGranted = false
            )
        }
    }



    fun onEvent(event: WeatherScreenEvents){
        when (event) {
            WeatherScreenEvents.TogglePermissions -> {
                _state.value = _state.value.copy(
                    permissionsGranted = !_state.value.permissionsGranted
                )
            }
        }
    }
}