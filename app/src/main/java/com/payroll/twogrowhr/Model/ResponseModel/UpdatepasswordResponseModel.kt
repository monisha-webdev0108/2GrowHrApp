package com.payroll.twogrowhr.Model.ResponseModel

import com.google.gson.annotations.SerializedName

data class  UpdatePasswordResponseModel(
    @SerializedName("success" ) var success: Boolean,
)
