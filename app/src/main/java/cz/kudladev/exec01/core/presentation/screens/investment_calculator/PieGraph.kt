package cz.kudladev.exec01.core.presentation.screens.investment_calculator

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter


@Composable
fun CompoundInterestPieChart(finalCapital: Double, interestEarned: Double) {
    val context = LocalContext.current
    val chart = remember { PieChart(context) } // Use PieChart instead of BarChart
    val color1 = MaterialTheme.colorScheme.primary.toArgb()
    val color2 = MaterialTheme.colorScheme.secondary.toArgb()
    val color1text = MaterialTheme.colorScheme.primary.invert().toArgb()
    val color2text = MaterialTheme.colorScheme.secondary.invert().toArgb()

    LaunchedEffect(finalCapital, interestEarned) {
        val entries = listOf(
            PieEntry(finalCapital.toFloat(), ""),
            PieEntry(interestEarned.toFloat(), "")
        )

        val dataSet = PieDataSet(entries, "") // Create PieDataSet
        dataSet.colors = listOf(color1, color2) // Set colors
        dataSet.valueTextSize = 12f // Set value text size
        dataSet.valueFormatter = PercentFormatter(chart) // Format values as percentage

        dataSet.setValueTextColors(listOf(color1text, color2text))



        val pieData = PieData(dataSet) // Create PieData
        chart.data = pieData

        chart.setHoleColor(Color.Transparent.toArgb())

        // Chart configurations
        chart.description.isEnabled = false
        chart.legend.isEnabled = false // Enable legend
        chart.setUsePercentValues(true) // Display values as percentage
        chart.isClickable = false

        chart.setTouchEnabled(false)

        chart.invalidate() // Refresh chart
    }

    // PieChart composable
    AndroidView(
        factory = { context ->
            chart
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    )
}

fun Color.invert(): Color {
    return Color(255 - red, 255 - green, 255 - blue, alpha)
}