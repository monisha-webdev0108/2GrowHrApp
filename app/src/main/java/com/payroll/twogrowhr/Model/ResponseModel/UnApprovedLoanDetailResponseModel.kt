package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class UnApprovedLoanDetailResponseModel(

    @SerializedName("success")
    var success: Boolean,

    @SerializedName("Data")
    var data: List<UnApproveLoanData>

): Parcelable


@Keep
@Parcelize
data class UnApproveLoanData (

    @SerializedName("EmpId")
    val empId : String,

    @SerializedName("LoanType_Slno")
    val loanTypeSlNo : String,

    @SerializedName("LoanType_RuleName")
    val loanRuleName : String,

    @SerializedName("LoanStart_Date")
    val loanStartDate : String,

    @SerializedName("Emp_Code")
    val empCode : String,

    @SerializedName("Emp_Name" )
    val empName : String,

    @SerializedName("LoanRequest_Amount")
    val loanRequestAmount : String,

    @SerializedName("Loan_Period" )
    val loanPeriod : String,

    @SerializedName("Remarks")
    val remarks : String,

    @SerializedName("Instalment_Amount")
    val instalmentAmount : String,

    @SerializedName("LoanApplysl_no")
    val loanApplySlNo : String,

    @SerializedName("NameinPay")
    val nameInPayslip : String,

    @SerializedName("Emp_RequestAmount")
    val empRequestAmount : String,

    @SerializedName("Deduction_Type")
    val deductionType : String,

    @SerializedName("Deduction_StartMonth" )
    val deductionStartMonth : String,

    @SerializedName("Nmonth")
    val NumberOfMonths : String,

    @SerializedName("Loan_Sanction_Date" )
    val loanSanctionDate : String,

    @SerializedName("Interest_rate")
    val interestRate : String,

    @SerializedName("Interest_Rate_Type")
    val interestRateType : String

    ): Parcelable

