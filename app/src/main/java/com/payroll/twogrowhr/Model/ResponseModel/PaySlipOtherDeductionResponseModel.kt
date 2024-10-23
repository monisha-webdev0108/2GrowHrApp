package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaySlipOtherDeductionResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<PaySlipOtherDeductionDetails>

): Parcelable

@Keep
@Parcelize
data class PaySlipOtherDeductionDetails (

    @SerializedName("Other_Component")
    val Other_Component : String,

    @SerializedName("Other_Amount")
    val Other_Amount : Int,

    @SerializedName("Total_Other_Earnings")
    val Total_Other_Earnings : Int,

    @SerializedName("Emp_Id")
    val Emp_Id : String,

    @SerializedName("Emp_Code")
    val Emp_Code : String,

    ): Parcelable