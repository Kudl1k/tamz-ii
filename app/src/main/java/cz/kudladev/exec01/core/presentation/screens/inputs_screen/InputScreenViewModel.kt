package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.pow

class InputScreenViewmodel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {

    private val _state = MutableStateFlow(InputScreenState())
    val state = _state.asStateFlow()

    init {
        Log.d("InputScreenViewModel", "Initializing ViewModel")
        Log.d("InputScreenViewModel", "State: ${_state.value.loaded}")
        if (!_state.value.loaded) {
            loadState()
            Log.d("InputScreenViewModel", "State loaded")
            _state.value = _state.value.copy(loaded = true)
        }
        calculateResult()
    }


    fun onEvent(event: InputScreenEvents) {
        when (event) {
            is InputScreenEvents.setBalance -> {
                _state.value = _state.value.copy(
                    startbalance = event.balance
                )
                calculateResult()
            }
            is InputScreenEvents.setInterest -> {
                _state.value = _state.value.copy(
                    interest = event.interest
                )
                calculateResult()
            }
            is InputScreenEvents.setLength -> {
                _state.value = _state.value.copy(
                    length = event.length
                )
                calculateResult()
            }
            InputScreenEvents.ToggleChartType -> {
                _state.value = _state.value.copy(
                    barChartToggle = !_state.value.barChartToggle,
                    pieChartToggle = !_state.value.pieChartToggle
                )
            }
        }
    }

    private fun calculateResult() {
        val balance = state.value.startbalance
        val interest = state.value.interest
        val length = state.value.length


        // Ensure interest rate is in decimal form (e.g., 0.05 for 5%)
        val decimalInterestRate = interest / 100.0

        // Calculate the final capital using the compound interest formula
        val finalCapital = balance * (1 + decimalInterestRate).pow(length)

        // Calculate the total interest earned
        val interestEarned = finalCapital - balance

        _state.value = _state.value.copy(
            resultedMoney = finalCapital,
            resultedInterestMoney = interestEarned
        )
        saveInputScreenState(_state.value)

    }

    fun loadState() {
        viewModelScope.launch {
            loadInputScreenState()
        }
    }

    private suspend fun loadInputScreenState() {
        Log.d("InputScreenViewModel", "Loading input screen state")
        try {
            val data = dataStore.data.map { preferences ->
                InputScreenState(
                    resultedMoney = preferences[ResultedMoneyKey] ?: 0.0,
                    resultedInterestMoney = preferences[ResultedInterestMoneyKey] ?: 0.0,
                    startbalance = preferences[StartBalanceKey] ?: 1000.0,
                    interest = preferences[InterestKey] ?: 2.0,
                    length = preferences[LengthKey] ?: 4
                )
            }.first()
            _state.value = data
            Log.d("InputScreenViewModel", "Loaded input screen state: $data")
        } catch (e: Exception) {
            Log.e("InputScreenViewModel", "Error loading input screen state", e)
        }
    }

    fun saveInputScreenState(state: InputScreenState) {
        Log.d("InputScreenViewModel", "Saving input screen state: $state")
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[ResultedMoneyKey] = state.resultedMoney
                preferences[ResultedInterestMoneyKey] = state.resultedInterestMoney
                preferences[StartBalanceKey] = state.startbalance
                preferences[InterestKey] = state.interest
                preferences[LengthKey] = state.length
            }
        }
    }


}