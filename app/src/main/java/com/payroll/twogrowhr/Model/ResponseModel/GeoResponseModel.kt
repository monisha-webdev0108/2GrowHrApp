package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class GeoResponseModel(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Head")
    var Head: List<GeoData>
): Parcelable


@Keep
@Parcelize
data class GeoData (

    @SerializedName("Latitude")
    val latitudes  : String,

    @SerializedName("Longitude")
    val longitudes : String,

    @SerializedName("Radius")
    val radius  : String,

    ): Parcelable
