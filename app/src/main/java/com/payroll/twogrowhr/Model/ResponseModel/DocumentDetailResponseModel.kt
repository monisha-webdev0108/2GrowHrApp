package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class DocumentDetailResponseModel(

    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Data"    ) val data: List<DocumentDetailsData>

): Parcelable

@Keep
@Parcelize
data class DocumentDetailsData (

    @SerializedName("Emp_Id")
    val empID: String,

    @SerializedName("Type")
    val type: String,

    @SerializedName("Account_No")
    val accountNumber: String,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Father_Name")
    val fatherName: String,

    @SerializedName("DOB")
    val dob: String,

    @SerializedName("Address")
    val address: String,

    @SerializedName("ExpiresOn")
    val expiresDate: String,

    @SerializedName("Date_Of_Issue")
    val issuedDate: String,

    @SerializedName("Place_Of_Issue")
    val issuedPlace: String,

    @SerializedName("Place_Of_Birth")
    val birthPlace: String,

    @SerializedName("Org")
    val org: String,

    @SerializedName("Session")
    val session: String,

    @SerializedName("File_Path")
    val filePath: String,

    @SerializedName("File_Name")
    val fileName: String


    ): Parcelable