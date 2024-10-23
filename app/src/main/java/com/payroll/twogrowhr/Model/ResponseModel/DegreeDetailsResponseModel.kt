package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DegreeDetailsResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<DegreeDetailsData>

): Parcelable

@Keep
@Parcelize
data class DegreeDetailsData (

    @SerializedName("Degree_Code")
    val degreeCode : String,

    @SerializedName("Qualification")
    val qualification : String,

    ): Parcelable