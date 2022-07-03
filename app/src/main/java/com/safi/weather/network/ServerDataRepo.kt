package com.safi.weather.network

import com.safi.weather.mainActivity.dataModel.WeatherModel
import com.safi.weather.network.core.ApiResponse
import com.safi.weather.network.core.NetworkBoundResource
import com.safi.weather.utils.Config
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ServerDataRepo @Inject constructor(
    private val apiService: ApiService,
    private val networkBoundResource: NetworkBoundResource
) {

    suspend fun getCityList(lat: String, lon: String, cnt: String) : Flow<ApiResponse<WeatherModel>> =
        networkBoundResource.downloadData {
            apiService.getCityList(lat, lon, cnt, Config.apiKey)
        }
}