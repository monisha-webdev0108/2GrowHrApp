package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class LoanMappedDetailsResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val data: List<LoanMappedData>
) : Parcelable

@Keep
@Parcelize
data class LoanMappedData(

    @SerializedName("flag")
    var flag: String

) : Parcelable