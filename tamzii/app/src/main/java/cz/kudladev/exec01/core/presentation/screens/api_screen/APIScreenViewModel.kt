package cz.kudladev.exec01.core.presentation.screens.api_screen

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.kudladev.exec01.core.data.api.StoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import retrofit2.HttpException

class APIScreenViewModel(
    private val api: StoreApi
): ViewModel() {

    private val _state = MutableStateFlow(APIScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val result = api.getProducts()
                _state.value = _state.value.copy(
                    products = result,
                    isLoading = false
                )
            } catch (e : HttpException){
                _state.value = _state.value.copy(
                    error = "${e.code()}: ${e.message()}"
                )
            }
        }
    }

    fun onEvent(event: APIScreenEvents) {
    }

}