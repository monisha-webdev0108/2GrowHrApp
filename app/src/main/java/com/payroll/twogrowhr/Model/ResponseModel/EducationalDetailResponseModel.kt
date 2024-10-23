package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class EducationalDetailResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<EducationalDocumentData>

): Parcelable

@Keep
@Parcelize
data class EducationalDocumentData (

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("Type")
    val type : String,

    @SerializedName("Qualification")
    val qualification : String,

    @SerializedName("Education_Speciality")
    val educationalSpeciality : String,

    @SerializedName("Date_Of_Joining")
    val doj : String,

    @SerializedName("Date_Of_Completion")
    val doc : String,

    @SerializedName("University")
    val university : String,

    @SerializedName("Location")
    val location : String,

    @SerializedName("File_Path")
    val filePath : String,

    @SerializedName("File_Name")
    val fileName : String,

    ): Parcelable