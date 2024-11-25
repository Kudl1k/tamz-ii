package cz.kudladev.exec01.core.presentation.screens.investment_calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.kudladev.exec01.core.presentation.components.TopAppBarWithDrawer
import kotlinx.coroutines.launch

@Composable
fun InvestmentCalcHistory(
    modifier: Modifier = Modifier,
    onEvent: (InvestmentCalcEvents) -> Unit,
    state: InvestmentCalcState,
    navController: NavController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var enableDropDownMenu by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBarWithDrawer(
                title = "Historie",
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
                    DropdownMenu(expanded = enableDropDownMenu, onDismissRequest = { enableDropDownMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Vymazat historii") },
                            onClick = {
                                onEvent(InvestmentCalcEvents.RemoveHistory)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                itemsIndexed(state.history){ index, item ->
                    InvestmentCalcHistoryItem(
                        index = index,
                        calcHistoryStates = item,
                        onClick = {
                            onEvent(InvestmentCalcEvents.SetHistoryItem(index))
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }




}