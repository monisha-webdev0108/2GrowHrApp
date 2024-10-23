package com.payroll.twogrowhr.Model.ResponseModel

import com.google.gson.annotations.SerializedName

data class LeaveTypeResponseModel
    (

    @SerializedName("LevTyp" ) var LevTyp: String = "",
    @SerializedName("bal" ) var bal: Double = 0.0

)