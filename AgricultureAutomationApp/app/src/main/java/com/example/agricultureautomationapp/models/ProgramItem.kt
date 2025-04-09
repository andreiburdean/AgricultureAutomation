package com.example.agricultureautomationapp.models

class ProgramItem {
    var programId: Int?
    var programTypeId: Int?
    var programName: String?
    var status: Int?
    var temperature: Double?
    var humidity: Double?
    var luminosity: Double?

    constructor(programId: Int?, programTypeId: Int?, programName: String, status: Int?, temperature: Double?, humidity: Double?, luminosity: Double?) {
        this.programId = programId
        this.programTypeId = programTypeId
        this.programName = programName
        this.status = status
        this.temperature = temperature
        this.humidity = humidity
        this.luminosity = luminosity
    }

    constructor(programTypeId: Int, programName: String) :
            this(null, programTypeId, programName, null, null, null, null)

    constructor(programTypeId: Int, programName: String, temperature: Double, humidity: Double, luminosity: Double) :
            this(null, programTypeId, programName, null, temperature, humidity, luminosity)

    constructor(programId: Int, temperature: Double, humidity: Double, luminosity: Double) :
            this(programId, null, null.toString(), null, temperature, humidity, luminosity)
}