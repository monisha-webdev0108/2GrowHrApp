package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Keep
@Parcelize
data class  HolidayResponseModel (

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val data: List<HolidayListData>

):Parcelable




@Keep
@Parcelize
data class HolidayListData(

    @SerializedName("Mname")
    val monthName: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("weekday")
    val weekDay: String,

    @SerializedName("holiname")
    val holidayName: String,

    @SerializedName("color1")
    val color1: String,

    @SerializedName("color2")
    val color2: String,

    @SerializedName("Holidate")
    val holidayDate: String
):Parcelable

@Keep
@Parcelize
data class HolidayData(

    @SerializedName("Mname")
    val monthName: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("weekday")
    val weekDay: String,

    @SerializedName("holiname")
    val holidayName: String,

    @SerializedName("color1")
    val color1: String,

    @SerializedName("color2")
    val color2: String,

    @SerializedName("Holidate")
    val HolidayDate: String
):Parcelable
