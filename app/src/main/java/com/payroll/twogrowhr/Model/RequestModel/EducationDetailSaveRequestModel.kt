package com.payroll.twogrowhr.Model.RequestModel


data class EducationDetailSaveRequestModel(
    var empId: String,
    var degree: String,
    var branch: String,
    var doj: String,
    var doc: String,
    var university: String,
    var location: String,
    var filePath: String,
)