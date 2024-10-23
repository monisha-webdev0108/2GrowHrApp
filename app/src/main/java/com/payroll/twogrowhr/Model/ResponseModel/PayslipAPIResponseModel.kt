package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class PayslipAPIResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<PayslipMonthData>

): Parcelable

@Keep
@Parcelize
data class PayslipMonthData (

    @SerializedName("monthid")
    val month : Int,

    @SerializedName("monthname")
    val monthName : String,

    @SerializedName("year")
    val year : Int,

    @SerializedName("url")
    val url : String,

    @SerializedName("Sub_Div")
    val Sub_Div_Id : Int,

    @SerializedName("urlpdf")
    val urlPdf : String,

    ): Parcelable