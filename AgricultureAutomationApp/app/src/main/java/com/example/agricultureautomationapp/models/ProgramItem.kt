package com.example.agricultureautomationapp.models

class ProgramItem {
    var programId: Int?
    var programTypeId: Int
    var programName: String
    var status: Int?

    constructor(programId: Int?, programTypeId: Int, programName: String, status: Int?) {
        this.programId = programId
        this.programTypeId = programTypeId
        this.programName = programName
        this.status = status
    }

    constructor(programTypeId: Int, programName: String) :
            this(null, programTypeId, programName, null)
}