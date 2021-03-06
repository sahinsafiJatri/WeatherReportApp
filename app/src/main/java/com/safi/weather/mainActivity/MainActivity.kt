package com.safi.weather.mainActivity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.safi.weather.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        adapter = WeatherAdapter(this)

        initView()
        liveDataObserve()
    }

    private fun initView() {

        binding.weatherRV.layoutManager = LinearLayoutManager(this)
        binding.weatherRV.adapter = adapter

        binding.refresh.setOnRefreshListener {
            viewModel.getCityData()
        }
    }

    private fun liveDataObserve() {

        viewModel.onProgressLiveData.observe(this) {
            binding.refresh.isRefreshing = it
        }

        viewModel.onCityListSuccessLiveData.observe(this) {
            adapter.addData(it)
        }

        viewModel.onCityListFailedLiveData.observe(this) {
            binding.refresh.isRefreshing = false
            Toast.makeText(this, it , Toast.LENGTH_LONG).show()
        }

    }


}