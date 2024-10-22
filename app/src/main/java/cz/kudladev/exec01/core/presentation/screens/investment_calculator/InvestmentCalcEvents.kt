package cz.kudladev.exec01.core.presentation.screens.investment_calculator

sealed class InvestmentCalcEvents {

    data class setBalance(val balance: Double): InvestmentCalcEvents()
    data class setInterest(val interest: Double): InvestmentCalcEvents()
    data class setLength(val length: Int): InvestmentCalcEvents()
    data class setRepetableInvestmentAmount(val amount: Double): InvestmentCalcEvents()

    object ToggleChartType: InvestmentCalcEvents()
    object ToggleRepeatableInvestment: InvestmentCalcEvents()

    object SaveToHistory: InvestmentCalcEvents()
    data class SetHistoryItem(val index: Int): InvestmentCalcEvents()
    object RemoveHistory: InvestmentCalcEvents()

}