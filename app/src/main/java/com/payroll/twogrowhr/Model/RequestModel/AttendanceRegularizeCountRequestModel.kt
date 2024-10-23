package com.payroll.twogrowhr.Model.RequestModel

import java.util.Date

class AttendanceRegularizeCountRequestModel {
    lateinit var sfCode: String
    lateinit var mode: String
    lateinit var Fdt: Date
    lateinit var Tdt: Date
}