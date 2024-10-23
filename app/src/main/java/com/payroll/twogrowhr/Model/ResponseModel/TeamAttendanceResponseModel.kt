package com.payroll.twogrowhr.Model.ResponseModel

import com.google.gson.annotations.SerializedName

data class TeamAttendanceResponseModel(

        @SerializedName("success" ) var success : Boolean,
        @SerializedName("Head"   ) var head: List<TeamAttendance>
)


data class TeamAttendance (
        @SerializedName("Name") var FilePath     : String,
        @SerializedName("Remark") var EmpCode      : String,
        @SerializedName("Name") var Name     : String,
        @SerializedName("Remark") var Remark      : String,
        @SerializedName("Emp_Id") var Emp_Id: String,
        @SerializedName("Emp_Code") var Emp_Code : String,
        @SerializedName("Attendance_Date" ) var Attendance_Date : String,
        @SerializedName("Checkin_Time" ) var Checkin_Time  : String,
        @SerializedName("Checkout_Time" ) var Checkout_Time : String,
        @SerializedName("Notes") var Notes  : String,
        @SerializedName("Working_Hours") var Working_Hours : String,
        @SerializedName("Day_Name") var Day_Name : String,
        @SerializedName("checoutLoc") var checoutLoc : String,
        @SerializedName("checkinLoc") var checkinLoc : String,
        @SerializedName("Day") var Day : Int,
        @SerializedName("flag") var flag : String,
        @SerializedName("Month") var Month : String,
        @SerializedName("Year") var Year : String,
        @SerializedName("sl_no") var sl_no : String,
)

