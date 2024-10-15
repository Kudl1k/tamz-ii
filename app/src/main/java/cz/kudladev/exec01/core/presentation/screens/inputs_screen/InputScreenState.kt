package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

data class InputScreenState(
    val loaded: Boolean = false,
    val resultedMoney: Double = 0.0,
    val resultedInterestMoney: Double = 0.0,
    val startbalance: Double = 1000.0,
    val interest: Double = 2.0,
    val length: Int = 4,
    val pieChartToggle: Boolean = false,
    val barChartToggle: Boolean = true
)


val ResultedMoneyKey = doublePreferencesKey("resulted_money")
val ResultedInterestMoneyKey = doublePreferencesKey("resulted_interest_money")
val StartBalanceKey = doublePreferencesKey("start_balance")
val InterestKey = doublePreferencesKey("interest")
val LengthKey = intPreferencesKey("length")