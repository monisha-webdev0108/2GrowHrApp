package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class AttendanceRegularizeRulesResponseModel(

    @SerializedName("success" ) val success: Boolean,
    @SerializedName("Head"    ) val Head: List<AttendanceRegularizeRules>

): Parcelable

@Keep
@Parcelize
data class AttendanceRegularizeRules (
    @SerializedName("IsRegularizeNeed")
    val isRegularizeNeed: Int,

    @SerializedName("Criteria")
    val criteria: Int,

    @SerializedName("CriteriaTime")
    val criteriaTime: Int,

    @SerializedName("IsAdjustNeed")
    val isAdjustNeed: Int,

    @SerializedName("AdjustMode")
    val adjustMode: Int,

    @SerializedName("AdjustDays")
    val adjustDays: Int,

    @SerializedName("IsRestrictionNeed")
    val isRestrictionNeed: Int,

    @SerializedName("RestrictDays")
    val restrictDays: Int,

    @SerializedName("IsCommentNeed")
    val isCommentNeed: Int,

    @SerializedName("CalendarDisplay")
    val calendarDisplay: Int,

    @SerializedName("IsRequestNeed")
    val isRequestNeed: Int,

    @SerializedName("TotalRequestTimes")
    val totalRequestTimes: Int,

    @SerializedName("RequestTo")
    val requestTo: Int,

    @SerializedName("IsAutoApproveNeed")
    val isAutoApproveNeed: Int,

    @SerializedName("AutoApproveDays")
    val autoApproveDays: Int,

    @SerializedName("AutoApproveMode")
    val autoApproveMode: Int,

): Parcelable