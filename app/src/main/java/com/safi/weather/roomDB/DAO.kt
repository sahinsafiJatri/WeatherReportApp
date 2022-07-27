package com.safi.weather.roomDB

import androidx.room.*
import com.safi.weather.roomDB.tables.WeatherTable

@Dao
interface DAO {

    @Insert
    suspend fun insertWeather(weatherTable: WeatherTable): Long

    @Query("DELETE FROM city")
    suspend fun deleteAll()

    @Query("SELECT * FROM city")
    fun getAllWeather(): List<WeatherTable>

}