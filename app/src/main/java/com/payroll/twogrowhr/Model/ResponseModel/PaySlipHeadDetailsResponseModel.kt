package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PaySlipHeadDetailsResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<PaySlipHeadDetails>

): Parcelable

@Keep
@Parcelize
data class PaySlipHeadDetails (

    @SerializedName("Sl_No")
    val sl_No : Int,

    @SerializedName("Emp_Id")
    val emp_Id : String,

    @SerializedName("Month")
    val Month : Int,

    @SerializedName("Year")
    val Year : String,

    @SerializedName("DOJ")
    val DOJ : String,

    @SerializedName("DOB")
    val DOB : String,

    @SerializedName("Bank_Name")
    val Bank_Name : String,

    @SerializedName("Bank_Account_Number")
    val Bank_Account_Number : String,

    @SerializedName("Department")
    val Department : String,

    @SerializedName("Designation")
    val Designation : String,

    @SerializedName("PF_No")
    val PF_No : String,

    @SerializedName("ESI_No")
    val ESI_No : String,

    @SerializedName("UAN")
    val UAN : String,

    @SerializedName("Day_Month")
    val Day_Month : String,

    @SerializedName("Working_Day")
    val Working_Day : String,

    @SerializedName("Loss_Of_Days")
    val Loss_Of_Days : String,

    @SerializedName("Gross")
    val Gross : Int,

    @SerializedName("Deduction")
    val Deduction : Int,

    @SerializedName("Net_Salary")
    val Net_Salary : Int,

    @SerializedName("Salary_Days")
    val Salary_Days : String,

    @SerializedName("Arrear_Days")
    val Arrear_Days : Int,

    @SerializedName("Total_Earning")
    val Total_Earning : Int,

    @SerializedName("Total_Deduction")
    val Total_Deduction : Int,

    @SerializedName("Loss_Amount")
    val Loss_Amount : Int,

    @SerializedName("Total_Days")
    val Total_Days : String,

    @SerializedName("CTC_Sale")
    val CTC_Sale : Int,

    @SerializedName("Place")
    val Place : String,

    @SerializedName("IFSC_Code")
    val IFSC_Code : String,

    @SerializedName("Lop_Amount")
    val Lop_Amount : Int,

    @SerializedName("HQ_Name")
    val HQ_Name : String,

    @SerializedName("Gender")
    val Gender : String,

    @SerializedName("Emp_Code")
    val Emp_Code : String,

    @SerializedName("Emp_Name")
    val Emp_Name : String,

    @SerializedName("Father_Name")
    val Father_Name : String,

    @SerializedName("PAN_Card")
    val PAN_Card : String,

    @SerializedName("Org_PAN")
    val Org_PAN : String,

    @SerializedName("Org_GST")
    val Org_GST : String,

    @SerializedName("Org_TIN")
    val Org_TIN : String,

    @SerializedName("Adhar_Card_Number")
    val Adhar_Card_Number : String,

    @SerializedName("Work_Location")
    val Work_Location : String,

    @SerializedName("Biometric_Id")
    val Biometric_Id : String,

    @SerializedName("Organization_Id")
    val Organization_Id : String,

    @SerializedName("Organization_Name")
    val Organization_Name : String,

    @SerializedName("File_Path")
    val File_Path : String,

    @SerializedName("Org_Mail_Id")
    val Org_Mail_Id : String,

    @SerializedName("Org_Website")
    val Org_Website : String,

    @SerializedName("Org_Othrdet")
    val Org_Othrdet : String,

    @SerializedName("Org_Mobile")
    val Org_Mobile : String,

    @SerializedName("Sub_Division_Id")
    val Sub_Division_Id : String,

    @SerializedName("Sub_Division_Name")
    val Sub_Division_Name : String,

    @SerializedName("Division_Id")
    val Division_Id : String,

    @SerializedName("Division_Name")
    val Division_Name : String,

    @SerializedName("Address_line1")
    val Address_line1 : String,

    @SerializedName("Address_line2")
    val Address_line2 : String,

    @SerializedName("Land_Mark")
    val Land_Mark : String,

    @SerializedName("Location")
    val Location : String,

    @SerializedName("Divcd")
    val Divcd : String,

    @SerializedName("Process_Date")
    val Process_Date : String,

    @SerializedName("Formatted_Date1")
    val Formatted_Date1 : String,

    @SerializedName("One_Formatted_Date1")
    val One_Formatted_Date1 : String,

    @SerializedName("GRADE")
    val GRADE : String,

    @SerializedName("State")
    val State : String,

    @SerializedName("Work_State")
    val Work_State : String,

    @SerializedName("Formatted_Date")
    val Formatted_Date : String,

    @SerializedName("One_Formatted_Date")
    val One_Formatted_Date : String,

    @SerializedName("Sub_Division_Address_line1")
    val Sub_Division_Address_line1 : String,

    @SerializedName("Sub_Division_Address_line2")
    val Sub_Division_Address_line2 : String,

    @SerializedName("Sub_Division_Landmark")
    val Sub_Division_Landmark : String,

    @SerializedName("Sub_Division_Location")
    val Sub_Division_Location : String,

    @SerializedName("Url_PDF")
    val Url_PDF : String,

    ): Parcelable