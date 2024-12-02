package cz.kudladev.exec01.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.kudladev.exec01.core.navigation.Routes

@Composable
fun HomeScreen(modifier: Modifier = Modifier,navController: NavController) {
    val routes = listOf(
        Routes.InvestmentNav,
        Routes.Scanner,
        Routes.Sokoban,
        Routes.Weather,
        Routes.FaceRecognition
    )
    Scaffold(

    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
            modifier = modifier.padding(it)
        ) {
            items(routes) { route ->
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(8.dp)
                        .clickable(
                            onClick = {
                                navController.navigate(route.route)
                            },
                            enabled = true
                        )
                        .clip(RoundedCornerShape(15))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ){
                    Column(
                        modifier = modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = route.icon!!,
                            contentDescription = route.title,
                            modifier = Modifier.size(50.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = route.title!!,
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        }
    }







}