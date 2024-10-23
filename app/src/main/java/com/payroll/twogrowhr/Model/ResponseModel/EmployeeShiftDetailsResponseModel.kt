package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Keep
data class EmployeeShiftDetailsResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val head: List<EmpShiftData>
) : Parcelable

@Parcelize
@Keep
data class EmpShiftData(

    @SerializedName("Shift_Id")
    val shiftId: Int,

    @SerializedName("Shift_StartTime")
    val shiftStartTime: String,

    @SerializedName("Shift_EndTime")
    val shiftEndTime: String,

    @SerializedName("Shift_Name")
    val shiftName: String
) : Parcelable