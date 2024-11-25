package cz.kudladev.exec01.core.presentation.screens.investment_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.SaveAlt
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.kudladev.exec01.core.navigation.Routes
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun InvestmentCalculator(modifier: Modifier = Modifier, navController: NavController, state: InvestmentCalcState, onEvent: (InvestmentCalcEvents) -> Unit) {
    val route = Routes.InvestmentCalc
    val history = Routes.InvestmentCalcHistory
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var enableDropDownMenu by remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    val focus = remember {
        FocusRequester()
    }



    NavDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = route.title!!,
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },

                    actions = {
                        IconButton(
                            onClick = {
                                enableDropDownMenu = !enableDropDownMenu
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(expanded = enableDropDownMenu, onDismissRequest = { enableDropDownMenu = false }, modifier = Modifier.clip(RoundedCornerShape(15))) {
                            DropdownMenuItem(
                                text = { Text("Pravidelný vklad") },
                                onClick = {
                                    enableDropDownMenu = false
                                    onEvent(InvestmentCalcEvents.ToggleRepeatableInvestment)
                                },
                                leadingIcon = {
                                    if (!state.repeatableInvestment){
                                        Icon(Icons.Outlined.Circle, contentDescription = null)
                                    } else {
                                        Icon(Icons.Filled.CheckCircle, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Uložit do historie") },
                                onClick = {
                                    enableDropDownMenu = false
                                    onEvent(InvestmentCalcEvents.SaveToHistory)
                                },
                                leadingIcon = {
                                    Icon(Icons.Outlined.SaveAlt, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(history.title ?: "")
                                },
                                leadingIcon = {
                                    history.icon?.let {
                                        Icon(
                                            imageVector = it,
                                            contentDescription = ""
                                        )
                                    }
                                },
                                onClick = {
                                    enableDropDownMenu = false
                                    navController.navigate(history.route)
                                }
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Naspořená suma",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                            Text(
                                text = "${state.resultedMoney.format()} Kč",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Z toho úrok",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                            Text(
                                text = "${state.resultedInterestMoney.format()} Kč",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }
                    }
                }
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilterChip(
                            onClick = {
                                onEvent(InvestmentCalcEvents.ToggleChartType)
                            },
                            label = {
                                Text("Bar")
                            },
                            selected = state.barChartToggle
                        )
                        Spacer(modifier.padding(horizontal = 8.dp))
                        FilterChip(
                            onClick = {
                                onEvent(InvestmentCalcEvents.ToggleChartType)
                            },
                            label = {
                                Text("Pie")
                            },
                            selected = state.pieChartToggle
                        )
                    }
                    if (state.barChartToggle){
                        CompoundInterestBarChart(state.resultedMoney,state.resultedInterestMoney)
                    } else if (state.pieChartToggle){
                        CompoundInterestPieChart(state.resultedMoney,state.resultedInterestMoney)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(20))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "Naspořená suma",
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 12.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(20))
                                    .background(MaterialTheme.colorScheme.secondary),
                            )
                            Text(
                                text = "Z toho úrok",
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        if (state.repeatableInvestment){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Pravidelný vklad",
                                )
                                Text(
                                    text = "${state.repeatableInvestmentAmount.format()} Kč"
                                )
                            }
                            Slider(
                                value = state.repeatableInvestmentAmount.toFloat(),
                                onValueChange = {
                                    onEvent(InvestmentCalcEvents.setRepetableInvestmentAmount(it.toDouble()))
                                },
                                valueRange = 0f..100000f,
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Počátný stav"
                            )
                            Text(
                                text = "${state.startbalance.format()} Kč"
                            )
                        }
                        Slider(
                            value = state.startbalance.toFloat(),
                            onValueChange = {
                                onEvent(InvestmentCalcEvents.setBalance(it.toDouble()))
                            },
                            valueRange = 0f..1000000f,
                            colors = SliderDefaults.colors(

                            )
                        )



                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Počáteční úrok"
                            )
                            Text(
                                text = "${state.interest.format()} %"
                            )
                        }
                        Slider(
                            value = state.interest.toFloat(),
                            onValueChange = {
                                onEvent(InvestmentCalcEvents.setInterest(it.toDouble()))
                            },
                            valueRange = 0f..10f
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Délka"
                            )
                            Text(
                                text = "${state.length} měsíců"
                            )
                        }
                        Slider(
                            value = state.length.toFloat(),
                            onValueChange = {
                                onEvent(InvestmentCalcEvents.setLength(it.toInt()))
                            },
                            valueRange = 0f..48f
                        )
                    }
                }
            }
        }

    }
}

fun Double.format(): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' ' // Set space as the grouping separator
        decimalSeparator = ',' // Set comma as the decimal separator
    }
    return DecimalFormat("#,##0.00", symbols).format(this)
}

fun String.format(): String {
    return this.toDouble().format()
}

fun String.formatBack(): Double {
    return this.replace(" ", "").replace(",", ".").toDouble()
}