package cz.kudladev.exec01.core.presentation.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithDrawer(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    actions: @Composable () -> Unit = {},
    onIconClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onIconClick()
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        },
        actions = {
            actions()
        }
    )
}