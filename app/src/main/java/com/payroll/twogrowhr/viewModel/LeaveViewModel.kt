package com.payroll.twogrowhr.viewModel

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveDateListData
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryData
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryDetailData
import com.payroll.twogrowhr.Model.ResponseModel.LeaveTypeDetailData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveDetailLeaveData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLeaveData
import com.payroll.twogrowhr.Model.View.LeaveItem
import com.payroll.twogrowhr.Model.View.LeaveItem1
import com.payroll.twogrowhr.Model.View.filePathClearance
import com.payroll.twogrowhr.Model.View.formatLeaveData
import com.payroll.twogrowhr.Model.View.formatLeaveData1
import com.payroll.twogrowhr.Model.View.leaveFilePath
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale


val statusLoading : MutableState<Boolean> = mutableStateOf(false)

class LeaveViewModel(private var repository: Repository,
                     private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loading : MutableState<Boolean> = mutableStateOf(false)

//------------------------------------------FOR APPLY LEAVE--------------------------------------------------//

//------------------------------------------GET LEAVE DETAILS--------------------------------------------------//


    val leaveList = savedStateHandle.getStateFlow("leaveList", emptyList<LeaveTypeDetailData>())


    @SuppressLint("SuspiciousIndentation")
    fun getLeaveDetails(navController: NavController, context : Context, empId: String) = viewModelScope.launch {

        Log.d("LeaveInViewModel", "Inside API Call $empId")

        try {

            Log.d("LEAVE", "$empId ")

            when(val response = repository.getLeaveResponse(context = context, sfCode = empId)){
                is Resource.Loading->{

                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call successful true. Message: ${data.Data}")
                            savedStateHandle["leaveList"] = data.Data
                        }
                        else
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call successful false. Message: ${data?.Data}")
                            savedStateHandle["leaveList"] = emptyList<LeaveTypeDetailData>()
                        }

                    }

                }
                is Resource.Error->{

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/leave")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }

                    Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call was not successful.")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            Log.e("LeaveInViewModel", "Error during API call: ${e.message}")
        }


    }



//------------------------------------------POST LEAVE FORM VALIDATE--------------------------------------------------//




    fun leaveFormValidate(navController: NavController, context : Context, empID: String, leaveId: MutableState<Int>, fromDate: LocalDate, toDate: LocalDate, appliedDays: String, callback: (String?) -> Unit) = viewModelScope.launch {

        try {
            val json = JSONObject()
            json.put("Leave_Type", leaveId.value)
            json.put("From_Date", fromDate)
            json.put("To_Date", toDate)
            json.put("Apply_Days", appliedDays)

            val jsonString = json.toString()
            Log.d("LEAVE", "$empID       $jsonString")



            when(val response = repository.postLeaveFormValidateResponse(context = context, sfCode = empID, data = jsonString))
            {
                is Resource.Loading->
                {
                    statusLoading.value = true
                }
                is Resource.Success->
                {
                    statusLoading.value = false

                    val data = response.data

                    savedStateHandle["success"] = data?.success

                    val leaveFormResponse = data?.response

                    // Check if the response is not null and has elements
                    if (leaveFormResponse?.isNotEmpty() == true) {
                        Log.e("LeaveViewModel", "leaveFormResponse : $leaveFormResponse")
                        Log.d("LeaveViewModel", "qwerty : leaveFormValidate API call was successful/true")
                        val message = leaveFormResponse[0].message
                        callback(message)
                        return@launch
                    }
                    else
                    {
                        filePathClearance()
                        Log.d("LeaveViewModel", "qwerty : leaveFormValidate API call was successful/false")
                        callback(null)
                    }

                }
                is Resource.Error->
                {
                    filePathClearance()

                    statusLoading.value = false

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/LeaveApply")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }

                    Log.d("LeaveViewModel", "qwerty : leaveFormValidate API call was not successful")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            filePathClearance()
            statusLoading.value = false
            Log.d("LeaveViewModel", "catch : leaveFormValidate API call was not successful")
            Log.e("LeaveViewModel", "Error during API call: ${e.message}")
            callback(null)
        }


    }


//------------------------------------------POST LEAVE DETAILS VALIDATE--------------------------------------------------//


    var success = savedStateHandle.get<Boolean>("success") ?: false

    fun postLeaveForm(context: Context, navController: NavController, empID: String, leaveId: MutableState<Int>, leaveItems: MutableList<LeaveItem>, fromDate: LocalDate, toDate: LocalDate, startTime: String, endTime: String, selectedLeaveType: String, reason: String, appliedDays: String, availableDays: String, balanceDays: String) = viewModelScope.launch {


        val formattedDataString: String
        val formattedDataWithEscapes: String
        val formattedData = JSONObject()

        try
        {

            formattedData.put("Leave_Type_Id", leaveId.value)
            formattedData.put("From_Date", fromDate)
            formattedData.put("To_Date", toDate)
            formattedData.put("Reason", reason)
            formattedData.put("Leave_Type", selectedLeaveType)
            formattedData.put("Available_Days", availableDays)
            formattedData.put("Apply_Days", appliedDays)
            formattedData.put("Balance_Days", balanceDays)

            // Call formatLeaveData to format your data
            val dateDetailsArray = formatLeaveData(fromDate.toString(), selectedLeaveType, startTime, endTime, appliedDays, leaveItems )
            Log.d("LeaveViewModel", "postLeaveForm : dateDetailsArray : $dateDetailsArray")


            val dateDetailsArrayString = dateDetailsArray.toString()
            Log.d("LeaveViewModel", "postLeaveForm : dateDetailsArrayString : $dateDetailsArrayString")

            formattedData.put("DateDetails", dateDetailsArray.toString())

            // Convert the formattedData JSON object to a string
            formattedDataString = formattedData.toString()
            Log.d("LeaveViewModel", "postLeaveForm : formattedDataString : $formattedDataString")


            // Replace escape characters in the DateDetails field
            formattedDataWithEscapes = formattedDataString.replace("\\", "\\\\")
            Log.d("LeaveViewModel", "postLeaveForm : formattedDataWithEscapes : $formattedDataWithEscapes")


            when(val response = repository.postLeaveResponse(context = context, sfCode = empID, data = formattedDataString)){
                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        statusLoading.value = false

                        if(data?.success == true)
                        {
//                    Leave Form Submitted Successfully....
                            filePathClearance()
                            Constant.showToast(context, "Submitted Successfully....")
                            Log.d("LeaveViewModel", "qwerty : postLeaveForm API call was successful/true : ${data.success}")
                            navController.navigate("leave")
                        }
                        else
                        {
                            filePathClearance()
                            Constant.showToast(context, "Please try again later....")
                            Log.d("LeaveViewModel", "qwerty : postLeaveForm API call was successful/false : ${data?.success}")
                            navController.navigate("LeaveApply")
                        }
                    }

                }
                is Resource.Error->{
                    statusLoading.value = false
                    filePathClearance()
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/LeaveApply")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    Log.d("LeaveViewModel", "qwerty : postLeaveForm API call was not successful")
                    navController.navigate("LeaveApply")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            statusLoading.value = false
            filePathClearance()
            Log.e("LeaveViewModel", "Error during API call: ${e.message}")
            Constant.showToast(context, "Something went wrong....")
            navController.navigate("LeaveApply")
        }

    }







//------------------------------------------FOR LEAVE HISTORY--------------------------------------------------//


//------------------------------------------LEAVE HISTORY--------------------------------------------------//


    val leaveHistoryList = savedStateHandle.getStateFlow("leaveHistoryList", emptyList<LeaveHistoryData>())

    var loadingStatus2 = savedStateHandle.get<Boolean>("loadingStatus3") ?: false

    var flag2 = savedStateHandle.get<Int>("flag2") ?: 0

    @SuppressLint("SuspiciousIndentation")
    fun getLeaveHistory(navController: NavController, context : Context, empId: String) = viewModelScope.launch {

        Log.d("LeaveInViewModel", "Inside API Call $empId")

        try {

            Log.d("LEAVE", "$empId ")
            when(val response = repository.getLeaveHistoryResponse(context = context, sfCode = empId))
            {
                is Resource.Loading->{
                    flag2 = 0
                }
                is Resource.Success->{

                    response.let {

                        loadingStatus2 = true

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistory API call successful true. Message: ${data.data}")
                            savedStateHandle["leaveHistoryList"] = data.data
                            flag2 = 1
                        }
                        else
                        {
                            savedStateHandle["leaveHistoryList"] = emptyList<LeaveHistoryData>()
                            flag2 = 2
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistory API call successful false. Message: ${data?.data}")
                        }
                    }

                }
                is Resource.Error->{

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/leave")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                        else
                        {
                            savedStateHandle["leaveHistoryList"] = emptyList<LeaveHistoryData>()
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistory API call successful/ Data Not Found")
                        }
                    }
                    savedStateHandle["leaveHistoryList"] = emptyList<LeaveHistoryData>()
                    flag2 = 2
                    Log.d("LeaveInViewModel", "qwerty : getLeaveHistory API call not successful.")
                    return@launch
                }

            }

            loadingStatus2 = false
        }
        catch (e: Exception)
        {
            Log.e("LeaveInViewModel", "Error during API call: ${e.message}")
        }


    }



//------------------------------------------LEAVE HISTORY DETAILS--------------------------------------------------//


    val leaveHistoryDetailList = savedStateHandle.getStateFlow("leaveHistoryDetailList", emptyList<LeaveHistoryDetailData>())

    var loadingStatus3 = savedStateHandle.get<Boolean>("loadingStatus3") ?: false

    var flag3 = savedStateHandle.get<Int>("flag3") ?: 0


    @SuppressLint("SuspiciousIndentation")
    fun getLeaveHistoryDetail(navController: NavController, context : Context, empId: String, slNo: String) = viewModelScope.launch {

        Log.d("LeaveInViewModel", "Inside API Call $empId")

        try {

            Log.d("LEAVE", "$empId ")


            when(val response = repository.getLeaveHistoryDetailResponse(context = context, sfCode = empId, slNo = slNo)){

                is Resource.Loading->
                {
                    flag3 = 0
                }

                is Resource.Success->
                {

                    response.let {

                        loadingStatus3 = true

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistoryDetail API call successful true. Message: ${data.data}")
                            savedStateHandle["leaveHistoryDetailList"] = data.data
                            flag3 = 1
                        }
                        else
                        {
                            savedStateHandle["leaveHistoryDetailList"] = emptyList<LeaveHistoryDetailData>()
                            flag3 = 2
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistoryDetail API call successful false. Message: ${data?.data}")
                        }
                    }

                }

                is Resource.Error->
                {

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/leaveHistory")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                        else
                        {
                            savedStateHandle["leaveHistoryDetailList"] = emptyList<LeaveHistoryDetailData>()
                            Log.d("LeaveInViewModel", "qwerty : getLeaveHistoryDetail API call successful/ Data Not Found")
                        }
                    }
                    flag3 = 2
                    savedStateHandle["leaveHistoryDetailList"] = emptyList<LeaveHistoryDetailData>()
                    Log.d("LeaveInViewModel", "qwerty : getLeaveHistoryDetail API call was not successful.")
                }
            }

            loadingStatus3 = false

        }
        catch (e: Exception)
        {
            Log.e("LeaveInViewModel", "Error during API call: ${e.message}")
            Log.d("LeaveInViewModel", "catch : getLeaveHistoryDetail API call was not successful.")
        }


    }












//------------------------------------------FOR APPROVAL--------------------------------------------------//


    sealed class PostLeaveApprovalResult {
        object Success : PostLeaveApprovalResult()
        data class Failure(val message: String) : PostLeaveApprovalResult()
    }


//------------------------------------------LIST OF APPROVALS FOR LEAVE--------------------------------------------------//


    val leaveApprovalList = savedStateHandle.getStateFlow("leaveApprovalList", emptyList<UnApproveLeaveData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getLeaveApprovalList(navController: NavController, context : Context, empId: String) = viewModelScope.launch {

        when(val response = repository.getLeaveApprovalListResponse(context = context,sfCode = empId)){
            is Resource.Loading->
            {
                flag = 0
                loadingStatus = true
            }
            is Resource.Success->{
                response.let {
                    val data = response.data

                    loadingStatus = false

                    if (data?.success == true)
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalList API call was successful/true : ${data.data}")

                        savedStateHandle["leaveApprovalList"] = data.data
                        flag = 1
                    }
                    else
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalList API call was successful/false : ${data?.data}")

                        savedStateHandle["leaveApprovalList"] = emptyList<UnApproveLeaveData>()

                        flag = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/leaveListApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalList API call was successful/Data Not Found")

                        savedStateHandle["leaveApprovalList"] = emptyList<UnApproveLeaveData>()

                    }
                }
                Log.d("LeaveViewModel", "qwerty : getLeaveApprovalList API call was not successful")

                savedStateHandle["leaveApprovalList"] = emptyList<UnApproveLeaveData>()
                flag = 2
            }
        }

        loadingStatus = false

    }


    fun fetchAndUpdateLeaveData(navController: NavController, context: Context, empId: String)
    {
        getLeaveApprovalList(navController,context,empId)
    }



//------------------------------------------LIST OF APPROVALS FOR LEAVE DETAIL LIST--------------------------------------------------//


    val leaveApprovalDetailList = savedStateHandle.getStateFlow("leaveApprovalDetailList", emptyList<UnApproveDetailLeaveData>())

    var loadingStatus1 = savedStateHandle.get<Boolean>("loadingStatus1") ?: false

    var flag1 = savedStateHandle.get<Int>("flag1") ?: 0


    fun getLeaveApprovalListDetails(navController: NavController,context: Context, empId: String, ruleId: String) = viewModelScope.launch {



        when(val response = repository.getLeaveDetailListResponse(context = context, sfCode = empId, ruleId = ruleId)){
            is Resource.Loading->
            {
                flag1 = 0
                loadingStatus1 = true
            }
            is Resource.Success->{
                response.let {
                    val data = response.data

                    loadingStatus1 = false

                    if (data?.success == true)
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalListDetails API call was successful/true : ${data.data}")

                        savedStateHandle["leaveApprovalDetailList"] = data.data
                        flag1 = 1
                    }
                    else
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalListDetails API call was successful/false : ${data?.data}")

                        savedStateHandle["leaveApprovalDetailList"] = emptyList<UnApproveDetailLeaveData>()

                        flag1 = 2
                    }

                }
            }
            is Resource.Error->{

                loadingStatus1 = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/leaveListApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("LeaveViewModel", "qwerty : getLeaveApprovalListDetails API call was successful/Data Not Found")

                        savedStateHandle["leaveApprovalDetailList"] = emptyList<UnApproveDetailLeaveData>()

                    }
                }
                Log.d("LeaveViewModel", "qwerty : getLeaveApprovalListDetails API call was not successful")

                savedStateHandle["leaveApprovalDetailList"] = emptyList<UnApproveDetailLeaveData>()
                flag1 = 2
            }
        }

        loadingStatus1 = false


    }


/*
    fun fetchAndUpdateLeaveDetailData(empId: String, ruleId: String)
    {
        getLeaveApprovalListDetails(empId, ruleId)
    }
*/



//------------------------------------------SENT FOR APPROVAL--------------------------------------------------//


    var loading1 = mutableStateOf(false)


    private suspend fun postLeaveApprovalDetails(navController: NavController, context: Context, empId: String, ruleID: String, data1 : String): PostLeaveApprovalResult {

            Log.d("LEAVE VIEW MODEL....", "postLeaveApprovalDetails : data1 : $data1")

            try {

                when(val response =  repository.postLeaveApprovalResponse(context = context, empId, ruleID, data1)){
                    is Resource.Loading->{
                        statusLoading.value = true
                    }
                    is Resource.Success->{

                        statusLoading.value = false

                        response.let {

                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if (data?.success == true)
                            {
                                Log.d("LeaveViewModel", "qwerty : postLeaveApprovalDetails API call was successful/true : ${data.success}")
                                return PostLeaveApprovalResult.Success

                            }
                            else
                            {
                                Log.d("LeaveViewModel", "qwerty : postLeaveApprovalDetails API call was successful/false : ${data?.success}")
                                return PostLeaveApprovalResult.Failure("Failed to post leave approval")
                            }
                        }

                    }
                    is Resource.Error->{
                        statusLoading.value = false

                        if(response.message.toString().isNotEmpty())
                        {
                            if(response.message.toString() != "Not Found")
                            {
                                if(response.message.toString() == "Please check your network connection")
                                {
                                    navController.navigate("${Screen.Network.route}/leaveListApproval")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        Log.d("LeaveViewModel", "qwerty : postLeaveApprovalDetails API call was not successful")
                        return PostLeaveApprovalResult.Failure("Null response")
                    }
                }

                return PostLeaveApprovalResult.Failure("Unexpected response")

            }
            catch (e: Exception) {
                statusLoading.value = false
                e.printStackTrace()
                return PostLeaveApprovalResult.Failure("An error occurred: ${e.message}")
            }


    }



    fun postLeaveStatusUpdate(navController: NavController, empId: String, ruleID: String, data: String, context : Context)
    {
        viewModelScope.launch {
            try
            {
                loading1.value = true


                // Perform the approval and rejection logic

                when (val result = postLeaveApprovalDetails(navController, context, empId, ruleID, data)) {
                    is PostLeaveApprovalResult.Success ->
                    {
                        Log.d("LEAVE VIEW MODEL.....","Approval/Rejected Successfully")
                        Constant.showToast(context, "Updated Successfully")
                        fetchAndUpdateLeaveData(navController,context,empId)
                    }
                    is PostLeaveApprovalResult.Failure ->
                    {
                        Log.d("LEAVE VIEW MODEL.....","An error occurred: ${result.message}")
                        Constant.showToast(context, " Update Failed: Please try again...!")
                        fetchAndUpdateLeaveData(navController,context,empId)
                    }
                }
                fetchAndUpdateLeaveData(navController,context,empId)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("LEAVE VIEW MODEL.....","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
            }
            finally
            {
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateLeaveData(navController,context,empId)
            }
        }
    }




















//------------------------------------------FOR APPLY LEAVE--------------------------------------------------//

//------------------------------------------GET LEAVE DETAILS--------------------------------------------------//


    val leaveListNew = savedStateHandle.getStateFlow("leaveList", emptyList<LeaveTypeDetailData>())


    @SuppressLint("SuspiciousIndentation")
    fun getLeaveDetails1(navController: NavController, context : Context, empId: String) = viewModelScope.launch {

        Log.d("LeaveInViewModel", "Inside API Call $empId")

        try {

            Log.d("LEAVE", "$empId ")

            when(val response = repository.getLeaveResponse(context = context, sfCode = empId)){
                is Resource.Loading->{

                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call successful true. Message: ${data.Data}")
                            savedStateHandle["leaveList"] = data.Data
                        }
                        else
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call successful false. Message: ${data?.Data}")
                            savedStateHandle["leaveList"] = emptyList<LeaveTypeDetailData>()
                        }

                    }

                }
                is Resource.Error->{

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/leave")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("LeaveInViewModel", "qwerty : getLeaveDetails API call was not successful.")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            Log.e("LeaveInViewModel", "Error during API call: ${e.message}")
        }



    }



//------------------------------------------GET LEAVE DATE LIST--------------------------------------------------//


    val leaveDateList = savedStateHandle.getStateFlow("leaveDateList", emptyList<LeaveDateListData>())

    var leaveDateFlag = savedStateHandle.get<Int>("leaveDateFlag") ?: 0

    var loadingStatus4 = savedStateHandle.get<Boolean>("loadingStatus4") ?: false

    @SuppressLint("SuspiciousIndentation")
    fun getLeaveDateDetails(navController: NavController, context : Context, empId: String, fromDate : LocalDate, toDate: LocalDate, slno: String, callback: (List<LeaveDateListData>?) -> Unit) = viewModelScope.launch {

        Log.d("LeaveInViewModel", "Inside API Call $empId")

        try {

            Log.d("LEAVE", "$empId ")

            when(val response = repository.getLeaveDateListResponse(context = context, sfCode = empId, fromDate = fromDate.toString(), toDate = toDate.toString(), slno = slno)){
                is Resource.Loading->{
                    loadingStatus4 = true
                    leaveDateFlag = 0
                }
                is Resource.Success->{

                    response.let {

                        loadingStatus1 = false

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDateDetails API call successful true. Message: ${data.Head}")
                            savedStateHandle["leaveDateList"] = data.Head
                            callback(data.Head)
                            leaveDateFlag = 1
                            return@launch

                        }
                        else
                        {
                            Log.d("LeaveInViewModel", "qwerty : getLeaveDateDetails API call successful false. Message: ${data?.Head}")
                            savedStateHandle["leaveDateList"] = emptyList<LeaveDateListData>()
                            callback(null)
                            leaveDateFlag = 2
                            return@launch

                        }

                    }

                }
                is Resource.Error->{

                    loadingStatus4 = false


                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/applyLeave")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }

                    savedStateHandle["leaveDateList"] = emptyList<LeaveDateListData>()
                    leaveDateFlag = 3
                    callback(null)

                    Log.d("LeaveInViewModel", "qwerty : getLeaveDateDetails API call was not successful.")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            Log.e("LeaveInViewModel", "Error during API call: ${e.message}")
            callback(null)
        }


    }

//------------------------------------------UPLOAD LEAVE FILE--------------------------------------------------//






    // Function to save the selected file with a common path and retain its original file name
    private suspend fun saveFileWithOriginalName(context: Context, uri: Uri): File? {
        return withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            var savedFile: File? = null

            try {
                val resolver: ContentResolver = context.contentResolver
                val cursorOut = resolver.query(uri, null, null, null, null)
                cursorOut?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val fileName = cursor.getString(nameIndex)


                    // Generate new filename with date and time
                    val date = SimpleDateFormat("yyyy-dd-M--HH-mm-ss_", Locale.getDefault()).format(Date()) //yyyy-dd-M--HH-mm-ss_
                    leaveFilePath.value = "$date$fileName"


                    val outputPath = File(context.filesDir, leaveFilePath.value)
                    inputStream = resolver.openInputStream(uri)
                    outputStream = FileOutputStream(outputPath)
                    inputStream?.copyTo(outputStream!!)
                    savedFile = outputPath
                }
            } catch (e: Exception) {
                Log.e("SaveFileException", "Error saving file: ${e.message}", e)
            } finally {
                inputStream?.close()
                outputStream?.close()
            }

            Log.e("SaveFile", "Saving file name: $savedFile" )

            savedFile

        }
    }



    var flag4 = savedStateHandle.get<Boolean>("flag4") ?: false


    fun uploadFile(context: Context, fileUri: Uri, navController: NavController, empID: String, leaveId: MutableState<Int>, leaveItems: MutableList<LeaveItem1>, fromDate: LocalDate, toDate: LocalDate, selectedLeaveType: String, reason: String, startTime: String, endTime: String, appliedDays: String, availableDays: String, balanceDays: String) = viewModelScope.launch {

        try {

            Log.d("uploadDetails... ", "inside uploadImage : $fileUri")

            val imageUri = saveFileWithOriginalName(context, fileUri)

            Log.e("Inside uploadFile", "Saving file name: $imageUri" )

            when (val response = repository.postLeaveFileResponse(context = context, docs = imageUri.toString())) {

                is Resource.Loading -> {
                    statusLoading.value = true
                }

                is Resource.Success -> {
                    statusLoading.value = false

                    val data = response.data

                    val message = data?.message
                    if (message == "File uploaded successfully")
                    {
                        statusLoading.value = false
                        flag4 = true

//                        Constant.showToast(context, message)

//                        applyLeave1(context,leaveViewModel,navController,empID,leaveId,leaveItems = leaveItems,fromDate,toDate, selectedLeaveType, reason, startTime, endTime, appliedDays, availableDays, balanceDays, isFileNeeded, fileUnits)

                        postLeaveForm1(context, navController,empID, leaveId, leaveItems = leaveItems, fromDate, toDate, startTime, endTime, selectedLeaveType, reason, appliedDays, availableDays, balanceDays)

                    }
                    else
                    {
                        filePathClearance()
                        flag4 = false
                        if (message != null)
                        {
                            Constant.showToast(context, message)
                        }
                    }
                }

                is Resource.Error -> {
                    flag4 = false
                    statusLoading.value = false
                    filePathClearance()
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/applyLeave")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            filePathClearance()
            flag4 = false
            e.printStackTrace()
            Log.d("LeaveViewModel", "catch : uploadFile API call was not successful.")
            Log.d("LeaveViewModel... ", "uploadFile... : $e")
            statusLoading.value = false
            Constant.showToast(context, "Something went wrong....")
        }
    }



//------------------------------------------POST LEAVE FORM VALIDATE--------------------------------------------------//




    fun leaveFormValidate1(navController: NavController, context : Context, empID: String, leaveId: MutableState<Int>, fromDate: LocalDate, toDate: LocalDate, appliedDays: String, callback: (String?) -> Unit) = viewModelScope.launch {

        try {
            val json = JSONObject()
            json.put("Leave_Type", leaveId.value)
            json.put("From_Date", fromDate)
            json.put("To_Date", toDate)
            json.put("Apply_Days", appliedDays)

            val jsonString = json.toString()
            Log.d("LEAVE", "$empID       $jsonString")



            when(val response = repository.postLeaveFormValidateResponse(context = context, sfCode = empID, data = jsonString))
            {
                is Resource.Loading->
                {
                    statusLoading.value = true
                }
                is Resource.Success->
                {
                    statusLoading.value = false

                    val data = response.data

                    savedStateHandle["success"] = data?.success

                    val leaveFormResponse = data?.response

                    // Check if the response is not null and has elements
                    if (leaveFormResponse?.isNotEmpty() == true) {
                        Log.e("LeaveViewModel", "leaveFormResponse : $leaveFormResponse")
                        Log.d("LeaveViewModel", "qwerty : leaveFormValidate1 API call was successful/true")
                        val message = leaveFormResponse[0].message
                        callback(message)
                        return@launch
                    }
                    else
                    {
                        Log.d("LeaveViewModel", "qwerty : leaveFormValidate1 API call was successful/false")
                        callback(null)
                    }

                }
                is Resource.Error->
                {
                    statusLoading.value = false

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/applyLeave")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }

                    Log.d("LeaveViewModel", "qwerty : leaveFormValidate1 API call was not successful")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            statusLoading.value = false
            Log.e("LeaveViewModel", "Error during API call: ${e.message}")
            callback(null)
        }


    }


//------------------------------------------POST LEAVE DETAILS VALIDATE--------------------------------------------------//


    fun postLeaveForm1(context: Context, navController: NavController, empID: String, leaveId: MutableState<Int>, leaveItems: MutableList<LeaveItem1>, fromDate: LocalDate, toDate: LocalDate, startTime: String, endTime: String, selectedLeaveType: String, reason: String, appliedDays: String, availableDays: String, balanceDays: String) = viewModelScope.launch {


        val formattedDataString: String
        val formattedDataWithEscapes: String
        val formattedData = JSONObject()

        try
        {

            formattedData.put("Leave_Type_Id", leaveId.value)
            formattedData.put("From_Date", fromDate)
            formattedData.put("To_Date", toDate)
            formattedData.put("Reason", reason)
            formattedData.put("Leave_Type", selectedLeaveType)
            formattedData.put("Available_Days", availableDays)
            formattedData.put("Apply_Days", appliedDays)
            formattedData.put("Balance_Days", balanceDays)
            formattedData.put("File", leaveFilePath.value)


            // Call formatLeaveData to format your data
            val dateDetailsArray = formatLeaveData1(fromDate.toString(), selectedLeaveType, startTime, endTime, appliedDays, leaveItems )
            Log.d("LeaveViewModel", "postLeaveForm : dateDetailsArray : $dateDetailsArray")


            val dateDetailsArrayString = dateDetailsArray.toString()
            Log.d("LeaveViewModel", "postLeaveForm : dateDetailsArrayString : $dateDetailsArrayString")

            formattedData.put("DateDetails", dateDetailsArray.toString())

            // Convert the formattedData JSON object to a string
            formattedDataString = formattedData.toString()
            Log.d("LeaveViewModel", "postLeaveForm : formattedDataString : $formattedDataString")


            // Replace escape characters in the DateDetails field
            formattedDataWithEscapes = formattedDataString.replace("\\", "\\\\")
            Log.d("LeaveViewModel", "postLeaveForm : formattedDataWithEscapes : $formattedDataWithEscapes")


            when(val response = repository.postLeaveResponse(context = context, sfCode = empID, data = formattedDataString)){
                is Resource.Loading->{

                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
//                    Leave Form Submitted Successfully....
                            filePathClearance()
                            Constant.showToast(context, "Submitted Successfully....")
                            Log.d("LeaveViewModel", "qwerty : postLeaveForm1 API call was successful/true : ${data.success}")
                            navController.navigate("leave")
                        }
                        else
                        {
                            filePathClearance()
                            Constant.showToast(context, "Please try again later....")
                            Log.d("LeaveViewModel", "qwerty : postLeaveForm1 API call was successful/false : ${data?.success}")
                            navController.navigate("applyLeave")
                        }
                    }

                }
                is Resource.Error->{
                    filePathClearance()
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/applyLeave")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("LeaveViewModel", "qwerty : postLeaveForm1 API call was not successful")
                    navController.navigate("applyLeave")
                    return@launch
                }
            }

        }
        catch (e: Exception)
        {
            Log.e("LeaveViewModel", "Error during API call: ${e.message}")
            Constant.showToast(context, "Something went wrong....")
            navController.navigate("applyLeave")
        }

    }

}
