package cz.kudladev.tamziikmp.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kudladev.tamziikmp.core.util.Resource
import cz.kudladev.tamziikmp.permissions.getLocation
import cz.kudladev.tamziikmp.weather.domain.usecase.CurrentWeather
import cz.kudladev.tamziikmp.weather.network.NetworkException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherScreenViewModel(
    private val currentWeather: CurrentWeather,
): ViewModel() {
    private val _state = MutableStateFlow(WeatherScreenState())
    val state = _state.asStateFlow()


    init {
        getWeather()
    }


    fun onEvent(event: WeatherScreenEvent) {
        when(event) {
            WeatherScreenEvent.getWeather -> {
                getWeather(50.0833, 14.4167)
            }
            WeatherScreenEvent.TogglePermissions -> {
                _state.update { it.copy(
                    permissionsGranted = true
                ) }
            }
        }
    }


    private fun getWeather(latitude: Double = 0.0, longitude: Double = 0.0) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            var location = getLocation()
            if (location == null){
                location = Pair(latitude, longitude)
            }

            val result = currentWeather.execute(location!!.first, location!!.second)

            when(result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            weather = result.data
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = (result.throwable as? NetworkException)?.error
                        )
                    }
                }
            }
        }
    }

}