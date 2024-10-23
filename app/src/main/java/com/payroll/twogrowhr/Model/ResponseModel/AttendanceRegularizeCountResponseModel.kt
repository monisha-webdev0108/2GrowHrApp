package com.payroll.twogrowhr.Model.ResponseModel

import com.google.gson.annotations.SerializedName

data class AttendanceRegularizeCountResponseModel(
    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Head"    ) val Head: List<AttendanceRegularizeCount>
)
data class AttendanceRegularizeCount (
    @SerializedName("count")
    val count: Int,
)