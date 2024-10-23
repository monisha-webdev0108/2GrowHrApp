package com.payroll.twogrowhr

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import com.payroll.twogrowhr.Database.MyDatabaseHelper
import com.payroll.twogrowhr.Model.ResponseModel.LoginResponseModel
import com.payroll.twogrowhr.Model.View.addressText
import com.payroll.twogrowhr.Model.View.buttonText
import com.payroll.twogrowhr.Model.View.compoOffApprovalDataList1
import com.payroll.twogrowhr.Model.View.imageFileName
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.leaveApprovalDataList1
import com.payroll.twogrowhr.Model.View.leaveApprovalDetailDataList1
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.Model.View.onDutyApprovalDataList1
import com.payroll.twogrowhr.Model.View.overTimeApprovalDataList1
import com.payroll.twogrowhr.Model.View.regularizedList1
import com.payroll.twogrowhr.Model.View.round
import com.payroll.twogrowhr.Model.View.selectedShift
import com.payroll.twogrowhr.Model.View.selectedShiftCutOff
import com.payroll.twogrowhr.Model.View.selectedShiftEndTime
import com.payroll.twogrowhr.Model.View.selectedShiftId
import com.payroll.twogrowhr.Model.View.selectedShiftStartTime
import com.payroll.twogrowhr.Model.View.wfhApprovalDataList1
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.viewModel.statusLoading
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Constant {

    companion object {

//        const val BASE_URL = "http://2growhr.saneforce.info/api/integ/apipayroll/"
//        const val IMAGE_URL = "http://2growhr.saneforce.info/"
//        const val FILE_URL = "2growhr.saneforce.info"


/*        const val BASE_URL = "http://testing.2growhr.io/api/integ/apipayroll/"
        const val IMAGE_URL = "http://testing.2growhr.io/"
        const val FILE_URL = "testing.2growhr.io"*/

/*        const val BASE_URL = "http://2growhr.info/api/integ/apipayroll/"
        const val IMAGE_URL = "http://2growhr.info/"
        const val FILE_URL = "2growhr.info"*/

//        const val IMAGE_URL = "http://2growhr.io/"


        const val BASE_URL = "https://2growhr.io/api/app/apipayroll/"
        const val IMAGE_URL = "https://2growhr.io/"
        const val FILE_URL = "2growhr.io"


        // Define the maximum allowed file size (in bytes)
        const val MAX_FILE_SIZE_BYTES = 2 * 1024 * 1024 // 2 MB
        const val isLoggedIn = false
        const val EmpPersonalDetails = "EmpPersonalDetails"
        const val GetHolidayList = "GetHolidayList"
        const val SalaryDatails = "SalaryDatails"
        const val LoanDatails = "LoanDatails"
        const val LoanSubDatails = "LoanDatailsSub"


        //APPLY LOAN
        const val isLoanEnable = "GetLoanEnable"
        const val isLoanMapped = "LoanRuleMappedCnt"
        const val getLoanType = "Getrulelist_Empwise"
        const val getLoanEligibility = "GetRestrictLoanEmp"
        const val GetInstallmentsMonth = "GetInstallmentsmonth"
        const val postLoanData = "Api_LoanSave"
        const val getExistingLoanInstallment = "LoanInstalmentAmount"
        const val Wishes = "GetWishLoadMoreList"
        const val todaysLog = "get/AttnDySty"
        const val UpdateNewPassword = "UpdateNewPassword"


        //WORK FROM HOME
        const val WorkFromHomeGetEmp = "WorkFromHomeGetEmp"
        const val WorkFromHome_Save = "WorkFromHome_Save"

        // Attendance Api
        const val Attendance = "AttendanceLogApi"
        const val DashboardAttendanceRegularize = "DashboardAttendanceRegularize"
        const val MissedDatePunch = "MissedDatePunch"
        const val AttendanceRegularizeRules = "AttendanceRegularize"
        const val AttendanceRegularizeCount = "AttendanceRegularizeCount"
        const val AttendanceListLocationAPI = "AttendanceListLocationAPI"

        // Apply Leave
        const val LeaveType = "LeaveType"
        const val LeaveFormValidate = "LeaveFormValidate"
        const val LeaveForm = "LeaveForm"
        const val uploadLeaveDocs = "LeaveUpload"
        const val LeaveDateList = "GetLeaveDates"


        //Check-IN and Check-OUT
        const val EmpShiftdetails = "EmpShiftdetails"
        const val shiftDetails = "get/Shift_timing"
        const val saveCheckIn1 = "saveCheckIn"
        const val saveCheckOut = "get/logouttime"
        const val uploadImage = "UploadImage"

        const val GeoDetails = "Get_GeoDetails"
        const val checkInOutVisible = "CheckinoutVisible"
        const val AttendanceAPI = "AttendanceAPI"

        const val AttendanceAPIDateRandom = "AttendanceAPIDateRandom"

        //Get Payslip & TDS

        const val getPayslipList = "Create_Payslip"
        const val getTdsFormList = "Create_TDS_Sheet"
        const val getPaySlipHeadDetail = "API_PaySlipHeadDetail"
        const val getPaySlipEarningDetail = "Api_GetPaySlipEarning_Details"
        const val getPaySlipDeductionDetail = "Api_GetPaySlipDeductions_Details"
        const val getPaySlipOtherDeductionDetail = "Api_GetPaySlip_OtherDeductions_Details"

        //OnDuty

        const val onDutyEmployeeDetail = "OnDutyEmployeeDetail"
        const val onDutyDetailSave = "OnDutyDetailSave"

        //Leave History

        const val leaveHistory = "GetLeave_Status"
        const val leaveHistoryDetail = "GetLeave_Perhistory"

        //Approvals

        const val approveAttendanceReglarizeAPI = "ApproveAttendanceReglarizeAPI"
        const val approveForRegularizeAPI = "ApproveForReglarizeAPI"

        const val unApproveOnDuty = "UnApproveOnDuty"
        const val onDutyApprove = "OnDutyApprove"

        const val unApproveWFH = "UnApproveWorkFromHome"
        const val wfhApprove = "WorkFromHomeApprove"

        const val unApproveLeave = "UnapproveLeavelist"
        const val unApproveLeaveDetail = "LeaveapprovePerAppli"
        const val leaveApprove = "LeaveapproveFinalsave"

        const val unApproveCompoOff = "GetCompoffDetails"
        const val compoOffApprove = "Api_CompoffApprove"

        const val unApproveOverTime = "GetShiftOTDetails"
        const val overTimeApprove = "Api_ShiftOTApprove"

        const val unApprovedLoan = "Api_GetUnapproveLoan"
        const val appliedLoanInstalment = "EmpLoanInstalmentAmt"
        const val loanApprovalStages = "Api_LoanApprovalStages"
        const val loanApprovalSave = "Api_LoanApprove"
        const val loanEditApprovalSave = "Api_LoanEditApprove"
        const val employeeProfile = "Api_GetEmployeeProfileDetails"
        const val ApprovalList = "GetCountList"

        // Organization
        const val OrgDOcList = "Api_Organization_Documents_Count"
        const val OrgDocDetail = "Api_Organization_Documents_Details"
        const val OrgDocCountDetail = "API_Document_Module_View_Count"

        //Documents
        const val getDocumentDetails = "Api_DocumentUpload_Status"
        const val getDocumentUploadDetails = "Api_DocumentUpload_Details"
        const val postDocumentUpload = "DocumentUpload"
        const val postDocumentDetailsSave = "Api_Document_Details_Save"
        const val getEducationDocumentDetails = "Api_Education_Document_Details"
        const val getExperienceDocumentDetails = "Api_Experience_Document_Details"
        const val getOtherDocumentDetails = "Api_Other_Document_Details"
        const val getDegreeDetails = "Api_Educational_Degree"
        const val postEduDocumentDetailsSave = "Api_Educational_Document_Save"
        const val postExpDocumentDetailsSave = "Api_Experience_Document_Save"
        const val postOtherDocumentDetailsSave = "Api_Other_Document_Save"

        //Assets

        const val getAssetListDetails = "Api_Assets_Details"



        private var toast: Toast? = null



        @Suppress("DEPRECATION")
        fun isNetworkAvailable(context: Context): Boolean {
            var isConnected = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null) {
                isConnected = activeNetworkInfo.isConnected
            }
//            Log.d("Network_Check", "isNetworkAvailable()::IsConnected => $isConnected")
            return isConnected
        }


        @SuppressLint("SuspiciousIndentation")
        fun showToast(context: Context, message: String)
        {

            Log.d("message", message)

          val messageToBeShown =  if(message == "Internal Server Error"){ "Something went wrong, Please refresh and Try again..."} else message

            if (toast == null) {
                toast = Toast.makeText(context, messageToBeShown, Toast.LENGTH_SHORT)
            } else {
                toast?.setText(messageToBeShown)
            }

            toast?.show()

            // Create a handler to dismiss the toast after 1 second
            Handler(Looper.getMainLooper()).postDelayed({
                toast?.cancel()
            }, 1000) // 1000 milliseconds = 1 second


//            Toast.makeText(context, message, Toast.LENGTH_LONG).show()

        }


        @SuppressLint("Range")
        fun getUserLoginDetails(context: Context?): JSONObject? {
            var cursorValue = ""
            val jsonObject: JSONObject?
            var jsonObject1: JSONObject? = null
            val sqLite = context?.let { MyDatabaseHelper(it) }
            val cursor: Cursor? = sqLite?.getUserLoginData()
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    cursorValue =
                        cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TLD_LOGIN_VALUE))
                }
            }
            cursor?.close()
            Log.d("Constant", "Login result from table:$cursorValue")
            try {
                jsonObject = JSONObject(cursorValue)

                Log.d("Constant", "Assign Login result from table to JsonObject:$jsonObject")
                Log.d("Constant", " Assign Login result from table to JsonObject:${jsonObject.getBoolean("success")}")

                val success = jsonObject.getBoolean("success")

                if (success) {
                    val jsonArray = jsonObject.getJSONArray("Data")

                    Log.d("Constant", "JsonArray:$jsonArray")

                    // Check if the JSONArray is not empty and has at least one element
                    if (jsonArray.length() > 0)
                    {
                        jsonObject1 = jsonArray.getJSONObject(0)// Get the first element (JSONObject) from the JSONArray
                    }
                    else
                    {
                        Log.d("Constant", "JsonArray is Null")
                    }

                    Log.d("Constant", "JsonObject From Inside IF.......:$jsonObject1")
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d("Constant", "Assign Login result from table to JsonObject:$jsonObject1")
            return jsonObject1
        }

        @SuppressLint("Range")
        fun getUserLoginDetailsForHome(context: Context?): LoginResponseModel? {
            var cursorValue = ""
            val sqLite = context?.let { MyDatabaseHelper(it) }
            val cursor: Cursor = sqLite?.getUserLoginData() ?: return null

            if (cursor.moveToFirst()) {
                cursorValue =
                    cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TLD_LOGIN_VALUE))
            }
            cursor.close()
            Log.d("Constant", "Login result from table:$cursorValue")

            val data = Gson().fromJson(cursorValue, LoginResponseModel::class.java)

            if (data == null || !data.success) return null
            return data
        }

        @SuppressLint("Range")
        fun getEmployeeMasterDetails(context: Context?): JSONObject? {

            var cursorValue = ""
            val jsonArray: JSONArray?
            var jsonObject1: JSONObject? = null
            val sqLite = context?.let { MyDatabaseHelper(it) }
            val cursor: Cursor? = sqLite?.getEmployeeMaster()
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    cursorValue = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.TLD_EMPLOYEE_VALUE))
                }
            }
            cursor?.close()
            Log.d("Constant", "Employee result from table:$cursorValue")

            try {
                jsonArray = JSONArray(cursorValue)

                Log.d("Constant", "JsonArray:$jsonArray")

                // Check if the JSONArray is not empty and has at least one element
                if (jsonArray.length() > 0) {
                    jsonObject1 =
                        jsonArray.getJSONObject(0)// Get the first element (JSONObject) from the JSONArray
                } else {
                    Log.d("Constant", "JsonArray is Null")
                }

                Log.d("Constant", "Assign Employee result from table to JsonObject:$jsonObject1")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Log.d(
                "Constant Return value",
                "Assign Employee result from table to JsonObject:$jsonObject1"
            )
            return jsonObject1
        }

        fun clearVariables()
        {
            selectedShift = ""
            selectedShiftId = ""
            selectedShiftStartTime = ""
            selectedShiftEndTime = ""
            selectedShiftCutOff = ""
            imageFileName =""
            lats =0.0
            longs =0.0
            addressText = ""
            buttonText = ""
            round = 1
            statusLoading.value = false
            loading.value = false
            leaveApprovalDetailDataList1.value = emptyList()
            regularizedList1.value = emptyList()
            onDutyApprovalDataList1.value = emptyList()
            wfhApprovalDataList1.value = emptyList()
            leaveApprovalDataList1.value = emptyList()
            compoOffApprovalDataList1.value = emptyList()
            overTimeApprovalDataList1.value = emptyList()

        }

        fun clearSQLiteData(context: Context?) {
            val dbHelper = context?.let { MyDatabaseHelper(it) }
            dbHelper?.deleteUserLoginData() // Call the function to clear login data
            dbHelper?.deleteEmployeeMaster() // Call the function to clear employee master data
        }

        private val AppThemeColors = lightColorScheme(
            primary = Color(0xFF405189), // Your primary color
            secondary = Color(0xFFDFDFDF), // Your accent color
        )

        @Composable
        fun AppTheme(content: @Composable () -> Unit) {
            MaterialTheme(
                colorScheme = AppThemeColors,
                content = content
            )
        }
    }
}
