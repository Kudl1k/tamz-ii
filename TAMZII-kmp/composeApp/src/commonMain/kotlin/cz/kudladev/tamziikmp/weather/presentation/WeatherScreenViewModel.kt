package cz.kudladev.tamziikmp.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kudladev.tamziikmp.core.util.Resource
import cz.kudladev.tamziikmp.permissions.model.Permission
import cz.kudladev.tamziikmp.permissions.service.PermissionsService
import cz.kudladev.tamziikmp.weather.domain.WeatherUseCase
import cz.kudladev.tamziikmp.weather.network.NetworkException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherScreenViewModel(
    private val weatherUseCase: WeatherUseCase,
    private val permissionService: PermissionsService
): ViewModel() {
    private val _state = MutableStateFlow(WeatherScreenState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            permissionService.providePermission(Permission.LOCATION_SERVICE_ON)
        }
        getWeather(50.0833, 14.4167)
    }


    fun onEvent(event: WeatherScreenEvent) {
        when(event) {
            WeatherScreenEvent.getWeather -> {
                getWeather(50.0833, 14.4167)
            }
        }
    }


    private fun getWeather(
        longitude: Double,
        latitude: Double
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val result = weatherUseCase.execute(latitude, longitude)
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