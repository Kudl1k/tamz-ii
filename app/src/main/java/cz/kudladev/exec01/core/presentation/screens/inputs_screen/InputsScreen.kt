package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cz.kudladev.exec01.core.presentation.components.BottomAppNavBar
import cz.kudladev.exec01.core.presentation.components.NavDrawer
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch

@Composable
fun InputsScreen(modifier: Modifier = Modifier, navController: NavController, state: InputScreenState, onEvent: (InputScreenEvents) -> Unit) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val context = LocalContext.current

    NavDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            topBar = {
                TopAppBarWithDrawer(
                    title = "Inputs",
                    icon = Icons.Default.Menu,
                    onIconClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(top = 16.dp),
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
                                text = "Naspořená suma:",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            )
                            Text(
                                text = "${String.format("%.2f", state.resultedMoney)} Kč",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Z toho úrok:",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            )
                            Text(
                                text = "${String.format("%.2f", state.resultedInterestMoney)} Kč",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
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
                                onEvent(InputScreenEvents.ToggleChartType)
                            },
                            label = {
                                Text("Bar")
                            },
                            selected = state.barChartToggle
                        )
                        Spacer(modifier.padding(horizontal = 8.dp))
                        FilterChip(
                            onClick = {
                                onEvent(InputScreenEvents.ToggleChartType)
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
                        Text(
                            text = "Počátný stav: ${String.format("%.2f", state.startbalance)} Kč"
                        )
                        Slider(
                            value = state.startbalance.toFloat(),
                            onValueChange = {
                                onEvent(InputScreenEvents.setBalance(it.toDouble()))
                            },
                            valueRange = 0f..10000f,
                        )

                        Text(
                            text = "Počáteční úrok: ${String.format("%.2f", state.interest)} %"
                        )
                        Slider(
                            value = state.interest.toFloat(),
                            onValueChange = {
                                onEvent(InputScreenEvents.setInterest(it.toDouble()))
                            },
                            valueRange = 0f..10f
                        )
                        Text(
                            text = "Délka: ${state.length}"
                        )
                        Slider(
                            value = state.length.toFloat(),
                            onValueChange = {
                                onEvent(InputScreenEvents.setLength(it.toInt()))
                            },
                            valueRange = 0f..48f
                        )
                    }
                }

            }
        }

    }
}