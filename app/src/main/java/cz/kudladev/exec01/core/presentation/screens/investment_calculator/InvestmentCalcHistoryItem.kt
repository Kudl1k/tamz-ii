package cz.kudladev.exec01.core.presentation.screens.investment_calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InvestmentCalcHistoryItem(
    modifier: Modifier = Modifier,
    index: Int,
    calcHistoryStates: InvestmentCalcHistoryStates,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().height(100.dp).padding(top = 2.dp, bottom = 2.dp),
        onClick = {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${index + 1}",
                modifier = Modifier.weight(1f),
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier.fillMaxSize().weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                if (calcHistoryStates.repeatableInvestment){
                    Text(
                        text = "${calcHistoryStates.repeatableInvestmentAmount.format()} Kč"
                    )
                }
                Text(
                    text = "${calcHistoryStates.startbalance.format()} Kč"
                )
                Text(
                    text = "${calcHistoryStates.interest.format()} %"
                )
                Text(
                    text = "${calcHistoryStates.length} měsíců"
                )
            }
        }
    }
}