package cz.kudladev.exec01.core.presentation.screens.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import cz.kudladev.exec01.core.data.api.GeoApi
import cz.kudladev.exec01.core.data.api.WeatherApi
import cz.kudladev.exec01.core.domain.dto.weather.CurrentWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class WeatherScreenViewModel(
    private val weatherApi: WeatherApi,
    private val geoApi: GeoApi,
    private val context: () -> Context
): ViewModel() {

    private val _state = MutableStateFlow(WeatherScreenState())
    val state = _state.asStateFlow()



    init {
        fetchLastLocation()
        viewModelScope.launch {
            _state.collect { state ->
                if (state.weather != null) {
                    val current = getCurrentWeather()
                    Log.d("weather", current.toString())
                    _state.value = _state.value.copy(
                        currentWeather = current
                    )
                }
            }
        }
    }

    private fun fetchWeatherForecast() {
        viewModelScope.launch {
            try {
                val weather = weatherApi.getWeatherForecast(
                    latitude = String.format("%.2f", _state.value.latitude),
                    longitude = String.format("%.2f", _state.value.longitude),
                )
                Log.d("weather", weather.toString())
                val current = getCurrentWeather()
                Log.d("weather", current.toString())
                _state.value = _state.value.copy(
                    weather = weather,
                    currentWeather = current
                )
            } catch (e: HttpException) {
                Log.d("weather", "error:${e.message()}")
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            }
        }
    }

    private fun fetchLastLocation() {
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
                        fetchWeatherForecast()
                        fetchLocationInfo()
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


    private fun fetchLocationInfo(){
        viewModelScope.launch{
            try {
                if (_state.value.latitude == null || _state.value.longitude == null) throw Exception("Location is null");
                val locationInfo = geoApi.reverseGeocode(
                    longitude = _state.value.longitude!!,
                    latitude = _state.value.latitude!!
                )
                _state.value = _state.value.copy(
                    place = locationInfo
                )
                Log.d("location", locationInfo.toString())
            } catch (e: HttpException){
                Log.d("location", "error:${e.message()}")
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            } catch (e: Exception){
                Log.d("location", "error:${e.message}")
                _state.value = _state.value.copy(
                    error = e.message
                )
            }
        }
    }

    private fun searchLocation(){
        viewModelScope.launch{
            try {
                val result = geoApi.searchSuggestions(
                    query = _state.value.searchQuery,
                    sessionToken = _state.value.sessionId
                )
                _state.value = _state.value.copy(
                    searchResults = result
                )
                Log.d("location", result.toString())
            } catch (e: HttpException){
                Log.d("location", "error:${e.message()}")
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            } catch (e : Exception){
                Log.d("location", "error:${e.message}")
                _state.value = _state.value.copy(
                    error = e.message
                )
            }
        }
    }

    private fun retrieveLocation(id: String){
        viewModelScope.launch{
            try {
                val result = geoApi.retrieveFeatureDetails(
                    mapboxId = id,
                    sessionToken = _state.value.sessionId
                )
                Log.d("location", result.features[0].geometry.toString())
                _state.value = _state.value.copy(
                    latitude = result.features[0].geometry.coordinates[1],
                    longitude = result.features[0].geometry.coordinates[0],
                )
                fetchWeatherForecast()
                fetchLocationInfo()
            } catch (e: HttpException){
                Log.d("location", "error:${e.message()}")
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            } catch (e: Exception){
                Log.d("location", "error:${e.message}")
                _state.value = _state.value.copy(
                    error = e.message
                )
            }
        }
    }


    fun onEvent(event: WeatherScreenEvents){
        when (event) {
            WeatherScreenEvents.TogglePermissions -> {
                _state.value = _state.value.copy(
                    permissionsGranted = !_state.value.permissionsGranted
                )
            }

            is WeatherScreenEvents.setSearchQuery -> {
                _state.value = _state.value.copy(
                    searchQuery = event.query
                )
            }
            WeatherScreenEvents.ToggleSearchBox -> {
                _state.value = _state.value.copy(
                    showSearchBox = !_state.value.showSearchBox
                )
            }

            WeatherScreenEvents.Search -> {
                searchLocation()
            }

            is WeatherScreenEvents.setPlace -> {
                retrieveLocation(event.id)
            }
        }
    }

    fun getCurrentWeather(): CurrentWeather? {
        val apiTime = _state.value.weather?.current?.time // Get time from API
        val apiTimeFormatted = apiTime?.substring(0, apiTime.length - 3) + ":00" // Format the API Time
        val hourlyTimeList = _state.value.weather?.hourly?.time
        val currentIndex = hourlyTimeList?.indexOf(apiTimeFormatted) // Find index in hourly.time

        if (currentIndex != null) {
            return CurrentWeather(
                apparent_temperature = _state.value.weather?.hourly?.apparent_temperature!![currentIndex],
                dew_point_2m = _state.value.weather?.hourly?.dew_point_2m!![currentIndex],
                precipitation = _state.value.weather?.hourly?.precipitation!![currentIndex],
                precipitation_probability = _state.value.weather?.hourly?.precipitation_probability!![currentIndex],
                relative_humidity_2m = _state.value.weather?.hourly?.relative_humidity_2m!![currentIndex],
                temperature_2m = _state.value.weather?.hourly?.temperature_2m!![currentIndex],
                time = _state.value.weather?.hourly?.time!![currentIndex],
                weather_code = _state.value.weather?.hourly?.weather_code!![currentIndex],
                wind_speed_10m = _state.value.weather?.hourly?.wind_speed_10m!![currentIndex]
            )
        }

        return null

    }
}