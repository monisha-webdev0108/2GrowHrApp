package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class OtherDocumentDetailResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data"    )
    val data: List<OtherDocumentData>

): Parcelable

@Keep
@Parcelize
data class OtherDocumentData (

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("Type")
    val type : String,

    @SerializedName("Document_Name")
    val documentName : String,

    @SerializedName("File_Path")
    val filePath : String,

    @SerializedName("File_Name")
    val fileName : String,

    ): Parcelable
