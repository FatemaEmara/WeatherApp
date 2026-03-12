package com.example.weatherapp.utils

sealed class ResponseState<T> {
    class Loading<T>: ResponseState<T>()
    data class Success<T>(val data: T): ResponseState<T>()
    data class Failure<T>(val error: String): ResponseState<T>()
}