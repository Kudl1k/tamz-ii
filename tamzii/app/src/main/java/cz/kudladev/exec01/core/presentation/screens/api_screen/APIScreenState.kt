package cz.kudladev.exec01.core.presentation.screens.api_screen

import cz.kudladev.exec01.core.domain.dto.store.Product

data class APIScreenState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
