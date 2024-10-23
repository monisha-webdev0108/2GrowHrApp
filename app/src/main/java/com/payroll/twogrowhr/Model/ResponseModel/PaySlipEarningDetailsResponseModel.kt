package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaySlipEarningDetailsResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<PaySlipEarningDetails>

): Parcelable

@Keep
@Parcelize
data class PaySlipEarningDetails (

    @SerializedName("Salary_Component_Id")
    val Salary_Component_Id : Int,

    @SerializedName("Salary_Component_Name")
    val Salary_Component_Name : String,

    @SerializedName("Salary_Component_Payslip_Name")
    val Salary_Component_Payslip_Name : String,

    @SerializedName("Amount")
    val Amount : Int,

    @SerializedName("Salary_Type")
    val Salary_Type : Int,

    @SerializedName("PaySd")
    val PaySd : Int,

    @SerializedName("Amount_Era")
    val Amount_Era : Int,

    @SerializedName("Ytd")
    val Ytd : String,

    @SerializedName("Earning_Salary")
    val Earning_Salary : String,

    @SerializedName("Emp_Id")
    val Emp_Id : String,

    @SerializedName("Emp_Code")
    val Emp_Code : String,

    @SerializedName("Earning_Type")
    val Earning_Type : String,


    ): Parcelable