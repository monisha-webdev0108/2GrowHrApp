package com.payroll.twogrowhr.Model.RequestModel

data class DocumentUploadSubmitRequestModel
(

    var accountNumber : String,
    var name:String,
    var fathername: String,
    var dob:String,
    var address:String,
    var expiresOn:String,
    var dateOfIssue : String,
    var placeOfIssue : String,
    var placeOfBirth : String,
    var session : String,
    var org : String,
    var mode : String,
    var filePath : String,
    var type : String,
    var empID : String
)
