package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class EmployeeProfileResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val data: List<EmployeeProfileDetail>
) : Parcelable

@Parcelize
@Keep
data class EmployeeProfileDetail(

    @SerializedName("empCode")
    val empCode: String,

    @SerializedName("divId")
    val divId: Int,

    @SerializedName("department")
    val department: String,

    @SerializedName("designation")
    val designation: String,

    @SerializedName("DOJ")
    val doj: String,

    @SerializedName("grossPay")
    val grossPay: Double,

    @SerializedName("filePath")
    val filePath: String
) : Parcelable