package com.safi.weather.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.safi.weather.roomDB.tables.WeatherTable

@Database(entities = [WeatherTable::class], version = 2)
abstract class RoomDB : RoomDatabase() {

    abstract fun dao() : DAO

}