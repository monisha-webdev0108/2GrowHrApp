package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AssetListResponseModel(

    @SerializedName("success" )
    val success: Boolean,

    @SerializedName("Data")
    val data: List<AssetListData>

): Parcelable

@Keep
@Parcelize
data class AssetListData (

    @SerializedName("Sl_No")
    val slNo : Int,

    @SerializedName("Emp_Id")
    val empId : String,

    @SerializedName("Emp_Code")
    val empCode : String,

    @SerializedName("Emp_Name")
    val empName : String,

    @SerializedName("Assets_Category_Name")
    val assetsCategoryName : String,

    @SerializedName("Assets_Name")
    val assetsName : String,

    @SerializedName("Cost")
    val cost : String,

    @SerializedName("Invoice_Id")
    val invoiceId : String,

    @SerializedName("Purchase_Date")
    val purchaseDate : String,

    @SerializedName("Given_Date")
    val givenDate : String,

    @SerializedName("Return_Date")
    val returnDate : String,

    @SerializedName("Status")
    val status : String,

    @SerializedName("Assets_PDF_URL")
    val assetsPDFURL : String,


    ): Parcelable