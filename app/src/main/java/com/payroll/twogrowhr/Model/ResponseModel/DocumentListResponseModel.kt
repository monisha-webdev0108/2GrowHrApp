package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DocumentListResponseModel(

    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Data"    ) val data: List<DocumentListData>

): Parcelable

@Keep
@Parcelize
data class DocumentListData (

    @SerializedName("Document_Name")
    val documentName: String,

    @SerializedName("Type")
    val documentType: String,

    @SerializedName("Count")
    val count: Int,

): Parcelable