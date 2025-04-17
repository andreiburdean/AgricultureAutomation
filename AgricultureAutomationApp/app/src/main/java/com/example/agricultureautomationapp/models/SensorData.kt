package com.example.agricultureautomationapp.models

class SensorData {
    var temperature: Double
    var humidity: Double
    var luminosity: Double
    var pressure: Double
    var soilMoisture: Double

    constructor(temperature: Double, humidity: Double, luminosity: Double, pressure: Double, soilMoisture: Double) {
        this.temperature = temperature
        this.humidity = humidity
        this.luminosity = luminosity
        this.pressure = pressure
        this.soilMoisture = soilMoisture
    }
}