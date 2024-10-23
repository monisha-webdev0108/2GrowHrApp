package com.payroll.twogrowhr.Model.RequestModel

import java.util.Date

data class AttendenceRequestModel (
    var sfCode: String,
    var FDT:Date,
    var TDT:Date,
)
data class AttendenceRegularizedRequestModel (
    var sfCode: String,
    var id:Int,
    var MissedDate:Date,
    var StartTime:String,
    var EndTime:String,
    var Remarks:String,
)
