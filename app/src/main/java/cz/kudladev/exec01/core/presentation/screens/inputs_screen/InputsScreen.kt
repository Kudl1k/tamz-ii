package cz.kudladev.exec01.core.presentation.screens.inputs_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                TextField(
                    value = state.login,
                    onValueChange = { change ->
                        onEvent(InputScreenEvents.setLogin(change))
                    },
                    label = {
                        Text("Login")
                    },
                    maxLines = 1,
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(8.dp))
                TextField(
                    value = state.password,
                    onValueChange = { change ->
                        onEvent(InputScreenEvents.setPassword(change))
                    },
                    label = {
                        Text("Password")
                    },
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (state.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        val description = if (state.passwordVisible) "Hide password" else "Show password"
                        IconButton(
                            onClick = {
                                onEvent(InputScreenEvents.toggleVisibility)
                            }
                        ){
                            Icon(
                                imageVector = image,
                                contentDescription = description
                            )
                        }
                    }
                )
                Button(
                    onClick = {
                        Toast.makeText(context,"Login: ${state.login}, Password: ${state.password}", Toast.LENGTH_LONG).show()
                        onEvent(InputScreenEvents.submit)
                    }
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            }
        }

    }
}