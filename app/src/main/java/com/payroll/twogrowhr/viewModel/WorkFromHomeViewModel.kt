package com.payroll.twogrowhr.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.WFHData
import com.payroll.twogrowhr.Model.ResponseModel.WFHListData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class WorkFromHomeViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {


    sealed class PostWFHResult {
        object Success :  PostWFHResult()
        data class Failure(val message: String) : PostWFHResult()
    }


//------------------------------------------GET WORK FROM HOME DETAILS--------------------------------------------------//

    var wfhDataList1 =  emptyList<WFHListData>()


    var allowWFH = mutableStateOf(0)
    var allowToApplyPastDays = mutableStateOf(0)
    var allowPastDays =  mutableStateOf(0.0)
    var isCommentRequired = mutableStateOf(0)
    var isHalfDayNeeded =  mutableStateOf(0)
    var isRestrictByDays = mutableStateOf(0)
    var wfhRestrictDays = mutableStateOf(0.0) // need to be int
    var wfhRestrictSession = mutableStateOf(0)
    var wfhAppliedDays = mutableStateOf(0)
    var isWFHAppliedEarlier = mutableStateOf(0)
    var isSelectedPastDay = mutableStateOf(false)
    var isRestrictEmpOnHW = mutableStateOf(0)
    var restrictOnHW = mutableStateOf(0)
    var dateOfJoining =  mutableStateOf("")
    var isHoliday = mutableStateOf("1900-01-01T00:00:00")
    var isWeeklyOff =  mutableStateOf("")
    var ruleId = mutableStateOf(0)
    var isODAppliedEarlier = mutableStateOf(0)
    var isWFHCheckInApplied = mutableStateOf(0)
    var isWFHCheckInApproved = mutableStateOf(0)

    var isCheckInAllowedForApplied = mutableStateOf("1900-01-01T00:00:00")
    var isCheckInAllowedOnlyApproved = mutableStateOf("1900-01-01T00:00:00")

    val calendar: Calendar = Calendar.getInstance()
    var startDate = mutableStateOf(System.currentTimeMillis())  // Initialize with the current time

    val org = userViewModel.getOrg()
    val empID = userViewModel.getSFCode()

    private val wfhList = MutableStateFlow<List<WFHListData>>(emptyList())
    val wfhDataList: StateFlow<List<WFHListData>> = wfhList


    @SuppressLint("SuspiciousIndentation")
    fun getWFHDetails(navController: NavController, context: Context, empId: String, date: String, org: String, callback: (List<WFHListData>?) -> Unit) = viewModelScope.launch {

        try {
            Log.d("WFH", "$empId       $date        $org")

            when(val response = repository.getWFHListResponse(context = context, sfCode = empId, date = date, org = org ))
            {
                is Resource.Loading->{

                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("WFHViewModel", "qwerty : getWFHDetails API call successful true. Message: ${data.head}")
                            wfhList.value = data.head
                            callback(data.head)
                            return@launch
                        }
                        else
                        {
                            Log.d("WFHViewModel", "qwerty : getWFHDetails API call successful false.")
                            wfhList.value = emptyList()
                            callback(wfhList.value)
                            return@launch
                        }
                    }

                }
                is Resource.Error->{
                    Log.d("WFHViewModel", "qwerty : getWFHDetails API call was not successful.")
                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/HomeScreen")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    wfhList.value = emptyList()
                    callback(wfhList.value)
                    return@launch
                }

            }

        }
        catch (e: Exception)
        {
            Log.e("WFHViewModel", "Error during API call: ${e.message}")
            wfhList.value = emptyList()
            callback(wfhList.value)
        }


    }


//------------------------------------------POST WORK FROM HOME DETAILS--------------------------------------------------//




    suspend fun postWFHDetails(
        navController: NavController, context: Context, empId: String, ruleID: String, date: String, wfhType: String, wfhHalfDayType: String, checkIn: String, checkOut: String, remarks: String, org: String
    ): PostWFHResult {
        try {
            Log.d("WFHViewModel", " empId: $empId, ruleID: $ruleID, date: $date, wfhType: $wfhType, wfhHalfDayType: $wfhHalfDayType, checkIn: $checkIn, checkOut:$checkOut, remarks:$remarks, org:$org ")

            when(val response = repository.postWFHDetailResponse(context = context, sfCode = empId, ruleID, date, wfhType,  wfhHalfDayType, checkIn, checkOut,remarks, org)){
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
                            Log.d("WorkFromHomeViewModel", "qwerty : postWFHDetails API call was successful/true : ${data.success}")
                            return PostWFHResult.Success
                        }
                        else
                        {
                            Log.d("WorkFromHomeViewModel", "qwerty : postWFHDetails API call was successful/false : ${data?.success}")
                            return PostWFHResult.Failure("Failed to post  Work From Home  details")
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
                                navController.navigate("${Screen.Network.route}/HomeScreen")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("WorkFromHomeViewModel", "qwerty : postWFHDetails1 API call was not successful")
                    return PostWFHResult.Failure("Null response")
                }
            }

            return PostWFHResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostWFHResult.Failure("An error occurred: ${e.message}")
        }
    }





//------------------------------------------FOR APPROVAL--------------------------------------------------//


    sealed class PostWFHApprovalResult {
        object Success : PostWFHApprovalResult()
        data class Failure(val message: String) : PostWFHApprovalResult()
    }


//------------------------------------------LIST OF APPROVALS FOR WORK FROM HOME--------------------------------------------------//


    val wfhApprovalList = savedStateHandle.getStateFlow("wfhApprovalList", emptyList<WFHData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getWFHApprovalDetails(navController: NavController, context: Context, empId: String, org: String) = viewModelScope.launch {

        Log.d("WORK FROM HOME DETAIL VIEW MODEL...", "Inside getWFHApprovalDetails ")

        when(val response =  repository.getWFHApprovalListResponse(context = context, sfCode = empId, org = org)){
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
                        Log.d("WFH DETAIL VIEW MODEL...", "qwerty : getWFHApprovalDetails API call was successful/true  : ${data.head}")
                        savedStateHandle["wfhApprovalList"] = data.head
                        flag = 1
                    }
                    else
                    {

                        Log.d("WFH DETAIL VIEW MODEL", "qwerty : getWFHApprovalDetails API call was successful/false : ${data?.head}")

                        savedStateHandle["wfhApprovalList"] = emptyList<WFHData>()

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
                            navController.navigate("${Screen.Network.route}/wfhApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {

                        Log.d("WFH DETAIL VIEW MODEL", "qwerty : getWFHApprovalDetails API call was successful/Data Not Found")

                        savedStateHandle["wfhApprovalList"] = emptyList<WFHData>()

                    }
                }
                Log.d("WFH DETAIL VIEW MODEL", "qwerty : getWFHApprovalDetails API call was not successful")

                savedStateHandle["wfhApprovalList"] = emptyList<WFHData>()
                flag = 2
            }
        }

        loadingStatus = false

    }


    fun fetchAndUpdateWFHData(navController: NavController, context: Context, empId: String, org: String)
    {
        getWFHApprovalDetails(navController,context,empId, org)
    }


//------------------------------------------SENT FOR APPROVAL--------------------------------------------------//


    var loading1 = mutableStateOf(false)


    private suspend fun postWFHApprovalDetails(navController: NavController, context: Context, empId: String, ruleID: String, wfhDate: String, org: String, status: String,reason: String): PostWFHApprovalResult {



        try {

            when(val response = repository.postWFHApprovalResponse(context = context, empId, ruleID,wfhDate, org, status, reason)){
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
                            Log.d("WorkFromHomeViewModel", "qwerty : postWFHApprovalDetails API call was successful/true : ${data.success}")
                            return PostWFHApprovalResult.Success

                        }
                        else
                        {
                            Log.d("WorkFromHomeViewModel", "qwerty : postWFHApprovalDetails API call was successful/false : ${data?.success}")
                            return PostWFHApprovalResult.Failure("Failed to post WFH approval")
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
                                navController.navigate("${Screen.Network.route}/wfhApproval")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("WorkFromHomeViewModel", "qwerty : postWFHApprovalDetails API call was not successful")
                    return PostWFHApprovalResult.Failure("Null response")
                }
            }

            return PostWFHApprovalResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostWFHApprovalResult.Failure("An error occurred: ${e.message}")
        }

    }



    fun postWFHStatusUpdate(navController: NavController, empId: String, ruleID: String, wfhDate: String, org: String, approvalAction: String, reason: String, context : Context)
    {
        viewModelScope.launch {
            try
            {
                loading1.value = true

                Log.d("WFH APPROVAL VIEW MODEL...","Inside postWFHStatusUpdate : Before when")
                // Perform the approval and rejection logic

                when (val result = postWFHApprovalDetails(navController, context, empId, ruleID, wfhDate, org, approvalAction, reason)) {
                    is PostWFHApprovalResult.Success ->
                    {

                        Log.d("WFH APPROVAL VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (approvalAction == "1") "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateWFHData(navController, context, empID, org)
                    }
                    is PostWFHApprovalResult.Failure ->
                    {
                        Log.d("WFH APPROVAL VIEW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (approvalAction == "1") "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateWFHData(navController, context, empID, org)
                    }
                }

                Log.d("WFH APPROVAL VIEW MODEL...","Inside postWFHStatusUpdate : After when.. fetchAndUpdateWFHData ")
                fetchAndUpdateWFHData(navController, context, empID, org)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("WFH APPROVAL VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
            }
            finally
            {
                Log.d("WFH APPROVAL VIEW MODEL...","Inside finally")
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateWFHData(navController, context, empID, org)
            }
        }
    }
}