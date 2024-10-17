package cz.kudladev.exec01.core.presentation.screens.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import cz.kudladev.exec01.core.data.api.GeoApi
import cz.kudladev.exec01.core.data.api.WeatherApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException


class WeatherScreenViewModel(
    weatherApi: WeatherApi,
    private val geoApi: GeoApi,
    private val context: () -> Context
): ViewModel() {

    private val _state = MutableStateFlow(WeatherScreenState())
    val state = _state.asStateFlow()



    init {
        fetchLastLocation(weatherApi,geoApi)
        _state.value = _state.value.copy(
            searchQuery = "Horni Bludovice"
        )
        searchLocation(geoApi)
    }

    private fun fetchWeatherForecast(weatherApi: WeatherApi) {
        viewModelScope.launch {
            try {
                val weather = weatherApi.getWeatherForecast(
                    latitude = String.format("%.2f", _state.value.latitude),
                    longitude = String.format("%.2f", _state.value.longitude),
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

    private fun fetchLastLocation(weatherApi: WeatherApi,geoApi: GeoApi) {
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
                        fetchLocationInfo(geoApi)
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


    private fun fetchLocationInfo(geoApi: GeoApi){
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

    private fun searchLocation(geoApi: GeoApi){
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
                _state.value = _state.value.copy(

                )
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
                searchLocation(geoApi = geoApi)
            }

            is WeatherScreenEvents.setPlace -> {
                val mapboxid = event.id

            }
        }
    }
}