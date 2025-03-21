package com.example.agricultureautomationapp.models

class ProgramItem {
    var environmentId: Int
    var programId: Int
    var programName: String
    var status: Int

    constructor(environmentId: Int, programId: Int, programName: String, status: Int) {
        this.environmentId = environmentId
        this.programId = programId
        this.programName = programName
        this.status = status
    }
}