package cz.kudladev.exec01.core.presentation.screens.weather

import cz.kudladev.exec01.R


fun getIcon(weather_code: Int): Int {
    when (weather_code) {
        0 -> return R.drawable.sunny_day
        1,2,3 -> return R.drawable.cloudy_day
        45,48 -> return R.drawable.fog
        51,53,55 -> return R.drawable.sleet
        56,57 -> return R.drawable.rain_and_sleet_mix
        61,63,65 -> return R.drawable.rain
        66,67 -> return R.drawable.rain_and_snow_mix
        71,73,75 -> return R.drawable.snowy_4
        77 -> return R.drawable.snowy_6
        80,81,82 -> return R.drawable.rainy_7
        85,86 -> return R.drawable.snowy_6
        95 -> return R.drawable.thunder
        96,99 -> return R.drawable.severe_thunderstorm
    }
    return  0
}