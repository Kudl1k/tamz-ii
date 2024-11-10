package cz.kudladev.exec01.core.data.api

import cz.kudladev.exec01.core.presentation.screens.sokoban.data.Level
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface LevelsAPI {

    @GET("levels")
    suspend fun getLevels(): List<Level>


    companion object {
        const val BASE_URL = "http://10.0.2.2:8085/"
    }
}