package com.payroll.twogrowhr.Model.ResponseModel

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Keep
@Parcelize
data class LoginResponseModel(

    @Expose
    @SerializedName("success")
    val success: Boolean,

    @Expose
    @SerializedName("Data")
    val data: List<UserData>,

    @Expose
    @SerializedName("BlockMsg")
    val blockMsg: String
) : Parcelable {
    override fun toString(): String {
        return "{\"success\"=$success, " +
                "\"Data\":$data, " +
                "\"BlockMsg\"=\"$blockMsg\"" +
                "}"
    }
}

@Keep
@Parcelize
data class UserData(
    @SerializedName("org")
    val org: Int,

    @SerializedName("Sf_UserName")
    val Sf_UserName: String,

    @SerializedName("sf_emp_id")
    val sf_emp_id: String,

    @SerializedName("DeptName")
    val DeptName: String,

    @SerializedName("sf_Designation_Short_Name")
    val sf_Designation_Short_Name: String,

    @SerializedName("SF_Status")
    val SF_Status: Int,

    @SerializedName("Sf_Name")
    val Sf_Name: String,

    @SerializedName("Sf_Password")
    val Sf_Password: String,

    @SerializedName("Division_Code")
    val Division_Code: Int,

    @SerializedName("Division_Name")
    val Division_Name: String,

    @SerializedName("DisRad")
    val DisRad: String,

    @SerializedName("Lattitude")
    val Lattitude: String,

    @SerializedName("Longitude")
    val Longitude: String,

    @SerializedName("Sf_code")
    val Sf_code: String,

    @SerializedName("Shift_Selection")
    val Shift_Selection: String,

    @SerializedName("CheckCount")
    val CheckCount: String,

    @SerializedName("Geo_Fencing")
    val Geo_Fencing: String,

    @SerializedName("SFFType")
    val SFFType: String,

    @SerializedName("SFDept")
    val SFDept: Int,

    @SerializedName("DeptType")
    val DeptType: String,

    @SerializedName("OTFlg")
    val OTFlg: String,

    @SerializedName("HOLocation")
    val HOLocation: String,

    @SerializedName("HQID")
    val HQID: String,

    @SerializedName("HQ_Name")
    val HQ_Name: String,

    @SerializedName("ERP_Code")
    val ERP_Code: String,

    @SerializedName("HQCode")
    val HQCode: String,

    @SerializedName("THrsPerm")
    val THrsPerm: String,

    @SerializedName("Profile")
    val Profile: String,

    @SerializedName("ProfPath")
    val ProfPath: String,

    @SerializedName("imageCaptureNeed")
    val imageCaptureNeed: Int,

    @SerializedName("cameraFlip")
    val cameraFlip: Int,

    @SerializedName("LocBasedCheckNeed")
    val LocBasedCheckNeed: Int,

    @SerializedName("mobile_check_in")
    val mobile_check_in: Int,

    @SerializedName("portalAccess")
    var portalAccess: Int,

) : Parcelable {
    override fun toString(): String {
        return "{" +
                "\"org\"=$org, " +
                "\"Sf_UserName\"='$Sf_UserName', " +
                "\"sf_emp_id\"='$sf_emp_id', " +
                "\"DeptName\"='$DeptName', " +
                "\"sf_Designation_Short_Name\"='$sf_Designation_Short_Name'," +
                "\"SF_Status\"=$SF_Status, " +
                "\"Sf_Name\"='$Sf_Name', " +
                "\"Sf_Password\"='$Sf_Password', " +
                "\"Division_Code\"=$Division_Code, " +
                "\"Division_Name\"='$Division_Name', " +
                "\"DisRad\"='$DisRad', " +
                "\"Lattitude\"='$Lattitude', " +
                "\"Longitude\"='$Longitude', " +
                "\"Sf_code\"='$Sf_code', " +
                "\"Shift_Selection\"='$Shift_Selection', " +
                "\"CheckCount\"='$CheckCount', " +
                "\"Geo_Fencing\"='$Geo_Fencing', " +
                "\"SFFType\"='$SFFType', " +
                "\"SFDept\"=$SFDept, " +
                "\"DeptType\"='$DeptType', " +
                "\"OTFlg\"='$OTFlg', " +
                "\"HOLocation\"='$HOLocation', " +
                "\"HQID\"='$HQID', " +
                "\"HQ_Name\"='$HQ_Name', " +
                "\"ERP_Code\"='$ERP_Code', " +
                "\"HQCode\"='$HQCode', " +
                "\"THrsPerm\"='$THrsPerm', " +
                "\"Profile\"='$Profile', " +
                "\"ProfPath\"='$ProfPath', " +
                "\"imageCaptureNeed\"=$imageCaptureNeed, " +
                "\"cameraFlip\"=$cameraFlip, " +
                "\"LocBasedCheckNeed\"=$LocBasedCheckNeed, " +
                "\"mobile_check_in\"=$mobile_check_in, " +
                "\"portalAccess\"=$portalAccess" +
                "}"
    }


}


/*        fun clearData(): UserData {
            return this.copy(

                org = 0,

                Sf_UserName = "",

                sf_emp_id = "",

                DeptName = "",

                sf_Designation_Short_Name = "",

                SF_Status = 0,

                Sf_Name = "",

                Sf_Password = "",

                Division_Code = 0,

                Division_Name = "",

                DisRad = "",

                Lattitude = "",

                Longitude = "",

                Sf_code  = "",

                Shift_Selection  = "",

                CheckCount  = "",

                Geo_Fencing  = "",

                SFFType  = "",

                SFDept = 0,

                DeptType  = "",

                OTFlg  = "",

                HOLocation  = "",

                HQID  = "",

                HQ_Name  = "",

                ERP_Code = "",

                HQCode = "",

                THrsPerm = "",

                Profile = "",

                ProfPath = "",

                imageCaptureNeed = 0,

                LocBasedCheckNeed = 0,

                mobile_check_in = 0,

                portalAccess = 2)
        }*/
