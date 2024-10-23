package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LeaveFormValidateResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Response"    )
    val response: List<LeaveFormValidateResponse>

): Parcelable

@Keep
@Parcelize
data class LeaveFormValidateResponse (

    @SerializedName("Msg")
    val message : String,

): Parcelable