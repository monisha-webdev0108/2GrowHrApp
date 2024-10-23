package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SalaryDetailResponseModel (

    @SerializedName("success" )
    val success : Boolean,

    @SerializedName("Earning" )
    val earning : List<Earning>,

    @SerializedName("Deduction" )
    val deduction : List<Deduction>,

    @SerializedName("CTC" )
    val ctc : List<CTC>,

    @SerializedName("tot" )
    val tot : List<Tot>,

    ):Parcelable


@Keep
@Parcelize
data class SalaryDetailItem (

    @SerializedName("Sc_Name" )
    val Sc_Name  : String,

    @SerializedName("Monthly" )
    val Monthly : String,

    @SerializedName("Annual"  )
    val Annual  : String

):Parcelable

@Keep
@Parcelize
data class Earning (

    @SerializedName("Sc_Name" )
    val Sc_Name  : String,

    @SerializedName("Monthly" )
    val Monthly : String,

    @SerializedName("Annual"  )
    val Annual  : String

):Parcelable


@Keep
@Parcelize
data class Deduction (

    @SerializedName("Sc_Name" )
    val Sc_Name  : String,

    @SerializedName("Monthly" )
    val Monthly : String,

    @SerializedName("Annual"  )
    val Annual  : String

):Parcelable

@Keep
@Parcelize
data class CTC (

    @SerializedName("Sc_Name" )
    val Sc_Name  : String,

    @SerializedName("Monthly" )
    val Monthly : String,

    @SerializedName("Annual"  )
    val Annual  : String

):Parcelable

@Keep
@Parcelize
data class Tot (

    @SerializedName("Sc_Name" )
    val Sc_Name  : String,

    @SerializedName("Monthly" )
    val Monthly : String,

    @SerializedName("Annual"  )
    val Annual  : String

):Parcelable