package com.example.agricultureautomationapp.models

class EnvironmentItem {
    var environmentId: Int?
    var raspberryId: Int
    var raspberryIp: String
    var environmentName: String

    constructor(environmentId: Int?, raspberryId: Int, raspberryIp: String, environmentName: String) {
        this.environmentId = environmentId
        this.raspberryId = raspberryId
        this.raspberryIp = raspberryIp
        this.environmentName = environmentName
    }

    constructor(raspberryId: Int, raspberryIp: String, environmentName: String) :
            this(null, raspberryId, raspberryIp, environmentName)
}
