package com.example.weatherapp.presentation.shared

import com.example.weatherapp.R

fun iconRes(iconCode: String): Int = when {
    iconCode == "01d"             -> R.drawable.sun
    iconCode == "01n"             -> R.drawable.sun
    iconCode.startsWith("02")     -> R.drawable.suncloud
    iconCode.startsWith("03")     -> R.drawable.suncloud
    iconCode.startsWith("04")     -> R.drawable.suncloud
    iconCode.startsWith("09")     -> R.drawable.rainy
    iconCode.startsWith("10")     -> R.drawable.rainy
    iconCode.startsWith("11")     -> R.drawable.thunderstorms
    iconCode.startsWith("13")     -> R.drawable.snow
    iconCode.startsWith("50")     -> R.drawable.fog
    else                          -> R.drawable.sun
}