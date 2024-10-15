package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

@Composable
fun CompoundInterestBarChart(finalCapital: Double, interestEarned: Double) {
    val context = LocalContext.current
    val chart = remember { BarChart(context) } // Remember the chart instance
    val color1 = MaterialTheme.colorScheme.primary.toArgb();
    val color2 = MaterialTheme.colorScheme.secondary.toArgb();
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    // Update chart data when finalCapital or interestEarned change
    LaunchedEffect(finalCapital, interestEarned) {
        val entries1 = listOf(BarEntry(0f, finalCapital.toFloat(), "Final Capital"))
        val dataSet1 = BarDataSet(entries1, "Final Capital")
        dataSet1.valueTextSize = 12f
        dataSet1.color = color1


        val entries2 = listOf(BarEntry(1f, interestEarned.toFloat(), "Interest Earned"))
        val dataSet2 = BarDataSet(entries2, "Interest Earned")
        dataSet2.valueTextSize = 12f
        dataSet2.color = color2

        val barData = BarData(dataSet1, dataSet2) // Add both data sets
        chart.data = barData


        dataSet1.valueTextColor = textColor
        dataSet2.valueTextColor = textColor

        // Remove description label
        chart.description.isEnabled = false

        // Remove x-axis values
        chart.xAxis.isEnabled = false

        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)

        // Remove y-axis
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false



        chart.axisLeft.axisMinimum = 0f
        chart.axisRight.axisMinimum = 0f

        chart.legend.isEnabled = false
        chart.invalidate() // Refresh chart
    }

    // BarChart composable
    AndroidView(
        factory = { context ->
            chart // Use the remembered chart instance
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    )
}