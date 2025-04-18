package com.example.agricultureautomationapp.monitor

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.agricultureautomationapp.R
import android.content.Context
import com.example.agricultureautomationapp.apiservices.MonitorApiService
import com.example.agricultureautomationapp.models.SensorData
import com.example.agricultureautomationapp.sharedpreferences.SharedPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MonitorActivity : AppCompatActivity() {
//    private val BASE_URL = "http://10.0.2.2:8080"
    private val BASE_URL = "http://192.168.100.63:8080"
    private var pollingJob: Job? = null

    private lateinit var environmentName: TextView
    private lateinit var temperatureText: TextView
    private lateinit var temperatureValue: TextView
    private lateinit var humidityText: TextView
    private lateinit var humidityValue: TextView
    private lateinit var luminosityText: TextView
    private lateinit var luminosityValue: TextView
    private lateinit var pressureText: TextView
    private lateinit var pressureValue: TextView
    private lateinit var moistureText: TextView
    private lateinit var moistureValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.monitor_activity)

        environmentName = findViewById(R.id.environment_text)
        temperatureText = findViewById(R.id.temperature_text)
        temperatureValue = findViewById(R.id.temperature_value)
        humidityText = findViewById(R.id.humidity_text)
        humidityValue = findViewById(R.id.humidity_value)
        luminosityText = findViewById(R.id.luminosity_text)
        luminosityValue = findViewById(R.id.luminosity_value)
        pressureText = findViewById(R.id.pressure_text)
        pressureValue = findViewById(R.id.pressure_value)
        moistureText = findViewById(R.id.soil_moisture_text)
        moistureValue = findViewById(R.id.soil_moisture_value)

        environmentName.text = SharedPreferences.getEnvironmentName(this)
    }

//    private fun initializeViews() {
//    }

    override fun onResume() {
        super.onResume()
        startPolling()
    }

    override fun onPause() {
        super.onPause()
        stopPolling()
    }

    private fun startPolling() {
        pollingJob = lifecycleScope.launch {
            while (isActive) {
                getSensorData(SharedPreferences.getEnvironmentId(this@MonitorActivity))
                delay(1000)
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
    }

    private fun getSensorData(environmentId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val monitorApi = retrofit.create(MonitorApiService::class.java)

        monitorApi.getSensorDataByEnvironment(environmentId).enqueue(object : Callback<SensorData> {
            override fun onResponse(call: Call<SensorData>, response: Response<SensorData>) {
                if(response.isSuccessful) {
                    val sensorDataResponse = response.body()
                    if (sensorDataResponse != null) {
                        temperatureValue.setText(sensorDataResponse.temperature.toString())
                        humidityValue.setText(sensorDataResponse.humidity.toString())
                        luminosityValue.setText(sensorDataResponse.luminosity.toString())
                        pressureValue.setText(sensorDataResponse.pressure.toString())

                        if(sensorDataResponse.soilMoisture == 0.0){
                            moistureValue.setText("Yes")
                        }
                        else{
                            moistureValue.setText("No")
                        }
                    }
                } else {
                    Log.d(TAG, "Sensor Data Retrieval: $response")
                }
            }

            override fun onFailure(call: Call<SensorData>, t: Throwable) {
                Log.d("MonitorActivity", "API call error: ${t.message}", t)
            }

        })
    }
}