package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class WFHApprovalListResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var head: List<WFHData>

): Parcelable


@Keep
@Parcelize
data class WFHData (

    @SerializedName("WFH_Date")
    val wfhDate : String,

    @SerializedName("WFHDt")
    val wfhDt : String,

    @SerializedName("Emp_Code")
    val empCode : String,

    @SerializedName("Rule_id")
    val ruleId : String,

    @SerializedName("Rule_Name")
    val ruleName : String,

    @SerializedName("emp_name" )
    val empName : String,

    @SerializedName("Emp")
    val emp : String,

    @SerializedName("WFH_Reamarks" )
    val wfhRemarks : String,

    @SerializedName("Day_Type")
    val dayType : String,

    ): Parcelable