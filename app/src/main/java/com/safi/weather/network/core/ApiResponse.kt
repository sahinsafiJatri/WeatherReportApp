package com.safi.weather.network.core

sealed class ApiResponse<out R>{
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Failure<out T>(val message : String, val code : Int) : ApiResponse<T>()
    data class Progress<out T>(val progress: Boolean) : ApiResponse<T>()
}
