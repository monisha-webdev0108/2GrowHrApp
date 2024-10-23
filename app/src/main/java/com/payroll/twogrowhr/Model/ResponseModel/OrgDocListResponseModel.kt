package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class OrgDocListResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Data")
    val Data: List<OrgDocListData>
) : Parcelable

@Keep
@Parcelize
data class OrgDocListData(
    @SerializedName("Folder_Count")
    val folderCount: String,

    @SerializedName("Folder_Id")
    val folderId: String,

    @SerializedName("Folder_Name")
    val folderName: String,
) : Parcelable