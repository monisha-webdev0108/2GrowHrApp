package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OrgDocShowCountResponseModel(
    @SerializedName("success")
    val success: Boolean,
) : Parcelable
