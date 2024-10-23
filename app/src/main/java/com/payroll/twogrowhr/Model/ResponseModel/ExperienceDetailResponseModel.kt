package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class ExperienceDetailResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<ExperienceDocumentData>

): Parcelable

@Keep
@Parcelize
data class ExperienceDocumentData (

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("Type")
    val type : String,

    @SerializedName("Company_Name")
    val companyName : String,

    @SerializedName("Job_Title")
    val jobTitle : String,

    @SerializedName("Date_Of_Joining")
    val doj : String,

    @SerializedName("Date_Of_Relieving")
    val dor : String,

    @SerializedName("Location")
    val location : String,

    @SerializedName("File_Path")
    val filePath : String,

    @SerializedName("File_Name")
    val fileName : String,

    ): Parcelable