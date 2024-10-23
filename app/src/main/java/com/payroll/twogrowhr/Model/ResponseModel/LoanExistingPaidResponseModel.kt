package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanExistingPaidResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Data")
    var data: List<ExistingLoanPaidData>

): Parcelable


@Keep
@Parcelize
data class ExistingLoanPaidData (

    @SerializedName("Loaninstamt")
    val existingInstallmentAmount : Int,

): Parcelable