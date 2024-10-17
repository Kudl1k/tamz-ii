package cz.kudladev.exec01.core.presentation.screens.weather

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun SearchBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    onSearchTriggered: () -> Unit,
    focus: FocusRequester,
    shouldShowHint: Boolean
) {

    val focusManager = LocalFocusManager.current
    var shouldTriggerSearch by remember { mutableStateOf(false) }


    TextField(
        modifier = modifier.focusRequester(focus),
        value = value,
        onValueChange = {
            onValueChange(it)
            if (it.isNotBlank()){
                shouldTriggerSearch = true
            }
        },
        placeholder = {
            if (shouldShowHint) {
                Text(text = "Search...")
            }
        },
        singleLine = true,
        maxLines = 1,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            showKeyboardOnFocus = true
        ),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
    )
    LaunchedEffect(Unit) {
        delay(100)
        focus.requestFocus()
    }
    LaunchedEffect(shouldTriggerSearch) {
        if (shouldTriggerSearch) {
            delay(500)
            if (value.isNotBlank()){
                onSearchTriggered()
            }
            shouldTriggerSearch = false
        }
    }
}