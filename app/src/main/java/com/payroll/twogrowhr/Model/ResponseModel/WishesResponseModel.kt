package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class WishesResponseModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("Head")
    val Head: List<WishesData>
) : Parcelable

@Keep
@Parcelize
data class WishesData(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Department")
    val department: String,

    @SerializedName("Designation")
    val designation: String,

    @SerializedName("Profile")
    val profile: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Month")
    val month: String,

    @SerializedName("DayOfWeek")
    val dayOfWeek: String,

    @SerializedName("Wish")
    val wish: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("Emp_Code")
    val empCode: String,

) : Parcelable

@Keep
@Parcelize
data class BirthdayWishesData(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Department")
    val department: String,

    @SerializedName("Designation")
    val designation: String,

    @SerializedName("Profile")
    val profile: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Month")
    val month: String,

    @SerializedName("DayOfWeek")
    val dayOfWeek: String,

    @SerializedName("Wish")
    val wish: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("Emp_Code")
    val empCode: String,
) : Parcelable

@Keep
@Parcelize
data class WeddingWishesData(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Department")
    val department: String,

    @SerializedName("Designation")
    val designation: String,

    @SerializedName("Profile")
    val profile: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Month")
    val month: String,

    @SerializedName("DayOfWeek")
    val dayOfWeek: String,

    @SerializedName("Wish")
    val wish: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("Emp_Code")
    val empCode: String,
) : Parcelable

@Keep
@Parcelize
data class WorkWishesData(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Department")
    val department: String,

    @SerializedName("Designation")
    val designation: String,

    @SerializedName("Profile")
    val profile: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Month")
    val month: String,

    @SerializedName("DayOfWeek")
    val dayOfWeek: String,

    @SerializedName("Wish")
    val wish: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("Emp_Code")
    val empCode: String,
) : Parcelable
@Keep
@Parcelize
data class LeaveData(
    @SerializedName("Name")
    val name: String,

    @SerializedName("Department")
    val department: String,

    @SerializedName("Designation")
    val designation: String,

    @SerializedName("Profile")
    val profile: String,

    @SerializedName("Date")
    val date: String,

    @SerializedName("Month")
    val month: String,

    @SerializedName("DayOfWeek")
    val dayOfWeek: String,

    @SerializedName("Wish")
    val wish: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("Emp_Code")
    val empCode: String,
) : Parcelable
