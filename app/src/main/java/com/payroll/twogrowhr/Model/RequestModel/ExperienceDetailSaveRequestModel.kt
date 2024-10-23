package com.payroll.twogrowhr.Model.RequestModel


data class ExperienceDetailSaveRequestModel(
    var empId: String,
    var companyName: String,
    var jobTitle: String,
    var doj: String,
    var dor: String,
    var location: String,
    var filePath: String
)