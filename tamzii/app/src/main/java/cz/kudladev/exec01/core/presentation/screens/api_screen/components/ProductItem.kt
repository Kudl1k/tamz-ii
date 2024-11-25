package cz.kudladev.exec01.core.presentation.screens.api_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cz.kudladev.exec01.core.domain.dto.store.Product
import cz.kudladev.exec01.core.domain.dto.store.Rating

@Composable
fun ProductItem(modifier: Modifier = Modifier, product: Product, onClick: () -> Unit) {
    ElevatedCard(
        modifier = modifier
           .aspectRatio(1f)
            .padding(4.dp),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.title,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.price.toString() + " $",
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.price.toString() + " $",
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductItemPreview() {
    ProductItem(
        product = Product(
            description = "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
            category = "men's clothing",
            id = 1,
            image = "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
            price = 109.95,
            rating = Rating(
                count = 120,
                rate = 3.9
            ),
            title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"
        )
    ) { }
}