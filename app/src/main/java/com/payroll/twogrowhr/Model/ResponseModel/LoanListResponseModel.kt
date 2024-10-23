package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class LoanListResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val Head: List<LoanData>
) : Parcelable

@Keep
@Parcelize
data class LoanData(
    @SerializedName("Employee_Name")
    var EmployeeName: String,

    @SerializedName("Total_Amount")
    var TotalAmount: Int,

    @SerializedName("Deduction_Type")
    var DeductionType: String,

    @SerializedName("No_of_Instalment")
    var NoOfInstalment: Int,

    @SerializedName("Instalment_Amount")
    var InstalmentAmount: Int,

    @SerializedName("Loan_Name")
    var LoanName: String,

    @SerializedName("loanStatus")
    var loanStatus: String,

    @SerializedName("Pending_Amount")
    var Pending_Amount: Int,

    @SerializedName("sl_no")
    var slNo: Int,

    @SerializedName("loanPeriod")
    var loanPeriod: String,

    @SerializedName("interest_rate")
    var interestRate: Double

) : Parcelable