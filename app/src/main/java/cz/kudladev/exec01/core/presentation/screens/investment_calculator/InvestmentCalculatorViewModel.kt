package cz.kudladev.exec01.core.presentation.screens.investment_calculator

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

class InvestmentCalculatorViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {

    private val _state = MutableStateFlow(InvestmentCalcState())
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


    fun onEvent(event: InvestmentCalcEvents) {
        when (event) {
            is InvestmentCalcEvents.setBalance -> {
                _state.value = _state.value.copy(
                    startbalance = event.balance
                )
                if (_state.value.repeatableInvestment){
                    calculateRepeatableInvestment()
                } else {
                    calculateResult()
                }
            }
            is InvestmentCalcEvents.setInterest -> {
                _state.value = _state.value.copy(
                    interest = event.interest
                )
                if (_state.value.repeatableInvestment){
                    calculateRepeatableInvestment()
                } else {
                    calculateResult()
                }
            }
            is InvestmentCalcEvents.setLength -> {
                _state.value = _state.value.copy(
                    length = event.length
                )
                if (_state.value.repeatableInvestment){
                    calculateRepeatableInvestment()
                } else {
                    calculateResult()
                }
            }
            InvestmentCalcEvents.ToggleChartType -> {
                _state.value = _state.value.copy(
                    barChartToggle = !_state.value.barChartToggle,
                    pieChartToggle = !_state.value.pieChartToggle
                )
            }

            InvestmentCalcEvents.ToggleRepeatableInvestment -> {
                _state.value = _state.value.copy(
                    repeatableInvestment = !_state.value.repeatableInvestment
                )
                if (_state.value.repeatableInvestment){
                    calculateRepeatableInvestment()
                } else {
                    calculateResult()
                }
            }

            is InvestmentCalcEvents.setRepetableInvestmentAmount -> {
                _state.value = _state.value.copy(
                    repeatableInvestmentAmount = event.amount
                )
                if (_state.value.repeatableInvestment) {
                    calculateRepeatableInvestment()
                } else {
                    calculateResult()
                }
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

    private fun calculateRepeatableInvestment() {
        val balance = state.value.startbalance
        val interest = state.value.interest
        val length = state.value.length
        val repeatableInvestmentAmount = state.value.repeatableInvestmentAmount

        // Ensure interest rate is in decimal form (e.g., 0.05 for 5%)
        val decimalInterestRate = interest / 100.0

        // Calculate the final capital using the compound interest formula
        var finalCapital = balance
        for (i in 1..length) {
            finalCapital = (finalCapital + repeatableInvestmentAmount) * (1 + decimalInterestRate)
        }

        // Calculate the total interest earned
        val interestEarned = finalCapital - balance - (repeatableInvestmentAmount * length)

        _state.value = _state.value.copy(
            resultedMoney = finalCapital,
            resultedInterestMoney = interestEarned
        )
        saveInputScreenState(_state.value)
    }

    fun loadState() {
        viewModelScope.launch {
            loadInputScreenState()
            saveInputScreenState(_state.value)
        }
    }

    private suspend fun loadInputScreenState() {
        Log.d("InputScreenViewModel", "Loading input screen state")
        try {
            val data = dataStore.data.map { preferences ->
                InvestmentCalcState(
                    resultedMoney = preferences[ResultedMoneyKey] ?: 0.0,
                    resultedInterestMoney = preferences[ResultedInterestMoneyKey] ?: 0.0,
                    startbalance = preferences[StartBalanceKey] ?: 1000.0,
                    interest = preferences[InterestKey] ?: 2.0,
                    length = preferences[LengthKey] ?: 4,
                    pieChartToggle = preferences[PieChartToggleKey] ?: false,
                    barChartToggle = preferences[BarChartToggleKey] ?: true,
                    repeatableInvestment = preferences[RepeatableInvestmentKey] ?: false,
                    repeatableInvestmentAmount = preferences[RepeatableInvestmentAmountKey] ?: 0.0
                )
            }.first()
            _state.value = data
            Log.d("InputScreenViewModel", "Loaded input screen state: $data")
        } catch (e: Exception) {
            Log.e("InputScreenViewModel", "Error loading input screen state", e)
        }
    }

    fun saveInputScreenState(state: InvestmentCalcState) {
        Log.d("InputScreenViewModel", "Saving input screen state: $state")
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[ResultedMoneyKey] = state.resultedMoney
                preferences[ResultedInterestMoneyKey] = state.resultedInterestMoney
                preferences[StartBalanceKey] = state.startbalance
                preferences[InterestKey] = state.interest
                preferences[LengthKey] = state.length
                preferences[PieChartToggleKey] = state.pieChartToggle
                preferences[BarChartToggleKey] = state.barChartToggle
                preferences[RepeatableInvestmentKey] = state.repeatableInvestment
                preferences[RepeatableInvestmentAmountKey] = state.repeatableInvestmentAmount
                Log.d("InputScreenViewModel", "Saved input screen state: $state")
            }
        }
    }


}