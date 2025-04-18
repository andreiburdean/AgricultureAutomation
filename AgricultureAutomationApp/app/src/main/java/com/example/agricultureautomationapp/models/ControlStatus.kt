package com.example.agricultureautomationapp.models

class ControlStatus {
    var switchControl: Int
    var fan: Int
    var pump: Int
    var led: Int

    constructor(switchControl: Int, fan: Int, pump: Int, led: Int) {
        this.switchControl = switchControl
        this.fan = fan
        this.pump = pump
        this.led = led
    }
}
