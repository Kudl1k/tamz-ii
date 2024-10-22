package cz.kudladev.exec01.core.presentation.screens.investment_calculator

import android.content.Context
import android.util.Log
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.math.pow

class InvestmentCalculatorViewModel(
    private val dataStore: DataStore<Preferences>,
    private val context: Context
): ViewModel() {

    private val _state = MutableStateFlow(InvestmentCalcState())
    val state = _state.asStateFlow()


    init {
        Log.d("InputScreenViewModel", "Initializing ViewModel")
        Log.d("InputScreenViewModel", "State: ${_state.value.loaded}")
        if (!_state.value.loaded) {
            loadHistory()
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

            InvestmentCalcEvents.SaveToHistory -> {
                saveToHistory()
            }

            is InvestmentCalcEvents.SetHistoryItem -> {
                SetHistoryItem(event.index)
            }

            InvestmentCalcEvents.RemoveHistory -> {
                removeHistory()
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

    private fun loadHistory(){
        Log.d("InputScreenViewModel","Loading history")
        try {
            val sharedPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("investment_calc_states", null)
            val type = object : TypeToken<List<InvestmentCalcHistoryStates>>() {}.type
            val retrievedList: List<InvestmentCalcHistoryStates> = gson.fromJson(json, type)


            Log.d("InputScreenViewModel","Loaded history: $retrievedList")

            if (retrievedList.isNotEmpty()){
                _state.value = _state.value.copy(
                    history = retrievedList,
                    loaded = true,
                    resultedMoney = retrievedList.last().resultedMoney,
                    resultedInterestMoney = retrievedList.last().resultedInterestMoney,
                    startbalance = retrievedList.last().startbalance,
                    interest = retrievedList.last().interest,
                    length = retrievedList.last().length,
                    pieChartToggle = retrievedList.last().pieChartToggle,
                    barChartToggle = retrievedList.last().barChartToggle,
                    repeatableInvestment = retrievedList.last().repeatableInvestment,
                    repeatableInvestmentAmount = retrievedList.last().repeatableInvestmentAmount
                )
            } else {
                _state.value = _state.value.copy(
                    loaded = true
                )
            }
        } catch (e: Exception){
            Log.e("InputScreenViewModel","Error loading history",e)
        }
    }

    private fun saveToHistory(){
        Log.d("InputScreenViewModel","Saving to history")
        try {
            val sharedPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val gson = Gson()
            val newHistory = InvestmentCalcHistoryStates(
                resultedMoney = _state.value.resultedMoney,
                resultedInterestMoney = _state.value.resultedInterestMoney,
                startbalance = _state.value.startbalance,
                interest = _state.value.interest,
                length = _state.value.length,
                pieChartToggle = _state.value.pieChartToggle,
                barChartToggle = _state.value.barChartToggle,
                repeatableInvestment = _state.value.repeatableInvestment,
                repeatableInvestmentAmount = _state.value.repeatableInvestmentAmount
            )
            val history = _state.value.history.toMutableList()
            history.add(newHistory)
            val json = gson.toJson(history)
            sharedPreferences.edit().putString("investment_calc_states", json).apply()
            _state.value = _state.value.copy(
                history = history
            )
            Log.d("InputScreenViewModel","Saved to history")
        } catch (e: Exception) {
            Log.e("InputScreenViewModel","Error saving to history",e)
        }
    }

    private fun SetHistoryItem(index: Int){
        Log.d("InputScreenViewModel","Setting history item to index: $index",)
        val historyItem = _state.value.history.getOrNull(index) // Get history item safely

        if (historyItem != null) {
            Log.d("InputScreenViewModel", "Setting history item to: $historyItem")
            _state.value = _state.value.copy(
                resultedMoney = historyItem.resultedMoney,
                resultedInterestMoney = historyItem.resultedInterestMoney,
                startbalance = historyItem.startbalance,
                interest = historyItem.interest,
                length = historyItem.length,
                pieChartToggle = historyItem.pieChartToggle,
                barChartToggle = historyItem.barChartToggle,
                repeatableInvestment = historyItem.repeatableInvestment,
                repeatableInvestmentAmount = historyItem.repeatableInvestmentAmount
            )
        }
    }

    private fun removeHistory(){
        Log.d("InputScreenViewModel","Removing history")
        val sharedPreferences = context.getSharedPreferences("history", Context.MODE_PRIVATE)
        val gson = Gson()
        sharedPreferences.edit().remove("investment_calc_states").apply()
        _state.value = _state.value.copy(
            history = emptyList()
        )
        Log.d("InputScreenViewModel","Removed history")
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