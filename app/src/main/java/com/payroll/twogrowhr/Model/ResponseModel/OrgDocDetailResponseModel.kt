package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OrgDocDetailResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val Data: List<OrgDocDetailData>
) : Parcelable

@Keep
@Parcelize
data class OrgDocDetailData(
    @SerializedName("Document_Id")
    val documentId: Int,

    @SerializedName("Document_Name")
    val documentName: String,

    @SerializedName("Document_Description")
    val documentDescription: String,

    @SerializedName("Document_Path")
    val documentPath: String,

    @SerializedName("Modified_Date")
    val modifiedDate: String,

    @SerializedName("Folder_Name")
    val folderName: String,
) : Parcelable