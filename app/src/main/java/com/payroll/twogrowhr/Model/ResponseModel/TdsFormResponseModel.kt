package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TdsFormResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<TdsFormMonthData>

): Parcelable

@Keep
@Parcelize
data class TdsFormMonthData (

    @SerializedName("year")
    val year : Int,

    @SerializedName("url")
    val url : String,

    @SerializedName("monthname")
    val monthName : String,

    @SerializedName("monthid")
    val month : Int,

    ): Parcelable