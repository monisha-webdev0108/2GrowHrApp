package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Parcelize
@Keep
data class EmployeeDetailsResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<EmployeeData>

) : Parcelable

@Parcelize
@Keep
data class EmployeeData(

    @SerializedName("Empid")
    val empId: String,

    @SerializedName("Emp_Code")
    val empCode: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("MName")
    val middleName: String,

    @SerializedName("LName")
    val lastName: String,

    @SerializedName("Empname")
    val empFullName: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("Doj")
    val doj: String,

    @SerializedName("DOB")
    val dob: String,

    @SerializedName("DOW")
    val dow: String,

    @SerializedName("DOC")
    val doc: String,

    @SerializedName("Designation")
    val designation: Int,

    @SerializedName("Email")
    val email: String,

    @SerializedName("Department")
    val department: Int,

    @SerializedName("WorkLoc")
    val workLoc: String,

    @SerializedName("Reportingto")
    val reportingTo: String,

    @SerializedName("BloodGrp")
    val bloodGroup: String,

    @SerializedName("City")
    val city: Int,

    @SerializedName("mobile")
    val mobile: String,

    @SerializedName("OfficeNo")
    val officeNumber: String,

    @SerializedName("EmergencyNo")
    val emergencyNumber: String,

    @SerializedName("Jobtype")
    val jobType: String,

    @SerializedName("FatherName")
    val fatherName: String,

    @SerializedName("Nominee")
    val nominee: String,

    @SerializedName("Pan")
    val pan: String,

    @SerializedName("Address")
    val address: String,

    @SerializedName("Address1")
    val address1: String,

    @SerializedName("State")
    val state: Int,

    @SerializedName("Pincode")
    val pincode: String,

    @SerializedName("State_Name")
    val stateName: String,

    @SerializedName("Designation_Name")
    val designationName: String,

    @SerializedName("ReportingtoName")
    val reportingToName: String,

    @SerializedName("Dep_Name")
    val departmentName: String,

    @SerializedName("City_Name")
    val cityName: String,

    @SerializedName("Div_id")
    val divId: Int,

    @SerializedName("ESI_enable")
    val enableESI: Int,

    @SerializedName("EPF_enable")
    val enableEPF: Int,

    @SerializedName("PT_enable")
    val enablePT: Int,

    @SerializedName("TDS")
    val tds: Int,

    @SerializedName("Portal_access")
    val portalAccess: Int,

    @SerializedName("PFN")
    val pfn: String,

    @SerializedName("UAN")
    val uan: String,

    @SerializedName("Health")
    val health: String,

    @SerializedName("Adhr")
    val adhr: String,

    @SerializedName("Bank")
    val bank: String,

    @SerializedName("BankAc")
    val bankAcc: String,

    @SerializedName("ifc_code")
    val ifscCode: String,

    @SerializedName("EPFN")
    val epfn: String,

    @SerializedName("ESIN")
    val esin: String,

    @SerializedName("cnt")
    val cnt: Int,

    @SerializedName("AdharDoc")
    val aadhaarDoc: String,

    @SerializedName("VoterDoc")
    val voterDoc: String,

    @SerializedName("FilePath")
    val filePath: String,

    @SerializedName("Sal_fix")
    val salFix: Int,

    @SerializedName("Emp_Shift")
    val empShift: String,

    @SerializedName("salutation")
    val salutation: Int,

    @SerializedName("marital")
    val marital: Int,

    @SerializedName("Template_Id")
    val templateId: Int


): Parcelable


