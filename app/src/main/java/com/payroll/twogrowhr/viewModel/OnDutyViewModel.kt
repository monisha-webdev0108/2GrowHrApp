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
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyData
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyListData
import com.payroll.twogrowhr.Model.View.onDutyApprovalDataList1
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
class OnDutyViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {


    sealed class PostOnDutyResult {
        object Success :  PostOnDutyResult()
        data class Failure(val message: String) : PostOnDutyResult()
    }


//------------------------------------------GET ON DUTY DETAILS--------------------------------------------------//

    var onDutyDataList1 =  emptyList<OnDutyListData>()


    var allowOnDuty = mutableStateOf(0)
    var allowToApplyPastDays =  mutableStateOf(0)
    var allowPastDays =  mutableStateOf(0.0)
    var isCommentRequired = mutableStateOf(0)
    var isHalfDayNeeded =  mutableStateOf(0)
    var isRestrictByDays = mutableStateOf(0)
    var onDutyRestrictDays = mutableStateOf(0.0) // need to be int
    var onDutyRestrictSession = mutableStateOf(0)
    var onDutyAppliedDays = mutableStateOf(0)
    var isODAppliedEarlier = mutableStateOf(0)
    var isSelectedPastDay = mutableStateOf(false)
    var isRestrictEmpOnHW = mutableStateOf(0)
    var restrictOnHW = mutableStateOf(0)
    var dateOfJoining =  mutableStateOf("")
    var isHoliday = mutableStateOf("1900-01-01T00:00:00")
    var isWeeklyOff =  mutableStateOf("")
    var ruleId = mutableStateOf(0)
    var isWFHAppliedEarlier = mutableStateOf(0)
    var isCheckOutOrNot = mutableStateOf(0)

    var isOnDutyCheckInApplied = mutableStateOf(0)
    var isOnDutyCheckInApproved = mutableStateOf(0)

    var isCheckInAllowedForApplied = mutableStateOf("1900-01-01T00:00:00")
    var isCheckInAllowedOnlyApproved = mutableStateOf("1900-01-01T00:00:00")

    val calendar: Calendar = Calendar.getInstance()
    var startDate = mutableStateOf(System.currentTimeMillis())  // Initialize with the current time

    val org = userViewModel.getOrg()
    val empID = userViewModel.getSFCode()

    private val onDutyList = MutableStateFlow<List<OnDutyListData>>(emptyList())
    val onDutyDataList: StateFlow<List<OnDutyListData>> = onDutyList


    @SuppressLint("SuspiciousIndentation")
    fun getOnDutyDetails(navController: NavController, context: Context, empId: String, date: String, org: String, callback: (List<OnDutyListData>?) -> Unit) = viewModelScope.launch {

        try {
            Log.d("ON DUTY", "$empId       $date        $org")

            when(val response = repository.getOnDutyListResponse(context = context, sfCode = empId, date = date, org = org )){
                is Resource.Loading->{

                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {
                            Log.d("OnDutyViewModel", "qwerty : getOnDutyDetails API call successful true. Message: ${data.head}")
                            onDutyList.value = data.head
                            callback(data.head)
                        }
                        else
                        {
                            Log.d("OnDutyViewModel", "qwerty : getOnDutyDetails API call successful false.")
                            onDutyList.value = emptyList()
                            callback(onDutyList.value)
                        }
                    }

                }
                is Resource.Error->{
                    Log.d("OnDutyViewModel", "qwerty : getOnDutyDetails API call was not successful.")
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
                    onDutyList.value = emptyList()
                    callback(onDutyList.value)
                    return@launch
                }
            }

            /*
                        Log.d("ON DUTY", "$empId       $date        $org")
                        val data = repository.getOnDutyListResponse(sfCode = empId, date = date, org = org )
                            data?.let {
                                savedStateHandle["success"] = it.success

                                if(it.success)
                                {
                                    Log.d("OnDutyViewModel", "qwerty : getOnDutyDetails API call successful true. Message: ${it.head}")
                                    onDutyList.value = it.head
                                    callback(it.head)
                                    return@launch
                                }
                                else
                                {
                                    Log.d("OnDutyViewModel", "qwerty : getOnDutyDetails API call successful false.")
                                    callback(null)
                                    return@launch
                                }
                            }

                        */

        }
        catch (e: Exception)
        {
            Log.e("OnDutyViewModel", "Error during API call: ${e.message}")
            onDutyList.value = emptyList()
            callback(onDutyList.value)
        }


    }


//------------------------------------------POST ON DUTY DETAILS--------------------------------------------------//


    suspend fun postOnDutyDetails(
       navController: NavController, context: Context, empId: String, ruleID: String, date: String, odType: String, odHalfDayType: String, checkIn: String, checkOut: String, remarks: String, org: String
    ): PostOnDutyResult {
        try {

            when(val response = repository.postOnDutyDetailResponse(context = context, sfCode = empId, ruleID, date, odType,  odHalfDayType, checkIn, checkOut,remarks, org)){
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
                            Log.d("OnDutyViewModel", "qwerty : postOnDutyDetails API call was successful/true : ${data.success}")
                            return PostOnDutyResult.Success
                        }
                        else
                        {
                            Log.d("OnDutyViewModel", "qwerty : postOnDutyDetails API call was successful/false : ${data?.success}")
                            return PostOnDutyResult.Failure("Failed to post OnDuty details")
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
                    Log.d("OnDutyViewModel", "qwerty : postOnDutyDetails API call was not successful")
                    return PostOnDutyResult.Failure("Null response")
                }
            }

            return PostOnDutyResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostOnDutyResult.Failure("An error occurred: ${e.message}")
        }
    }


//------------------------------------------FOR APPROVAL--------------------------------------------------//


    sealed class PostOnDutyApprovalResult {
        object Success : PostOnDutyApprovalResult()
        data class Failure(val message: String) : PostOnDutyApprovalResult()
    }




//------------------------------------------LIST OF APPROVALS FOR ON DUTY--------------------------------------------------//


    val onDutyApprovalList = savedStateHandle.getStateFlow("onDutyApprovalList", emptyList<OnDutyData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getOnDutyApprovalDetails(navController: NavController,context: Context,empId: String, org: String) = viewModelScope.launch {



        when(val response = repository.getOnDutyApprovalListResponse(context = context, sfCode = empId, org = org)){
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
                        Log.d("ON DUTY DETAIL VIEW MODEL...", "qwerty : getOnDutyApprovalDetails API call was successful/true  : ${data.head}")
                        savedStateHandle["onDutyApprovalList"] = data.head
                        flag = 1
                    }
                    else
                    {

                        Log.d("ON DUTY DETAIL VIEW MODEL", "qwerty : getOnDutyApprovalDetails API call was successful/false : ${data?.head}")

                        savedStateHandle["onDutyApprovalList"] = emptyList<OnDutyData>()

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
                            navController.navigate("${Screen.Network.route}/odApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {

                        Log.d("ON DUTY DETAIL VIEW MODEL", "qwerty : getOnDutyApprovalDetails API call was successful/ Data Not Found")

                        savedStateHandle["onDutyApprovalList"] = emptyList<OnDutyData>()

                    }
                }
                Log.d("ON DUTY DETAIL VIEW MODEL", "qwerty : getOnDutyApprovalDetails API call was not successful")

                savedStateHandle["onDutyApprovalList"] = emptyList<OnDutyData>()
                flag = 2
            }
        }

        loadingStatus = false


    }


    fun fetchAndUpdateOnDutyData(navController: NavController,context: Context,empId: String, org: String)
    {
        getOnDutyApprovalDetails(navController,context,empId, org)
    }


//------------------------------------------SENT FOR APPROVAL--------------------------------------------------//

//    var approvalResponse = savedStateHandle.get<Boolean>("approvalResponse") ?: false
//
//    var loadingCircular = savedStateHandle.get<Boolean>("loadingCircular") ?: false
//
//    var responseFlag = savedStateHandle.get<Int>("flag") ?: 0

    var loading1 = mutableStateOf(false)


    private suspend fun postOnDutyApprovalDetails(navController: NavController, context: Context, empId: String, ruleID: String, odDate: String, org: String, status: String, reason: String): PostOnDutyApprovalResult {

        Log.d("ON DUTY APPROVAL VIEW MODEL...","postOnDutyApprovalDetails : empId :$empId, ruleID: $ruleID, odDate: $odDate, org: $org, approvalAction: $status, reason: $reason ")


        try {

            when(val response = repository.postOnDutyApprovalResponse(context = context, empId, ruleID,odDate, org, status, reason)){
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
                            Log.d("ON DUTY APPROVAL VIEW MODEL", "qwerty : postOnDutyApprovalDetails API call was successful/true : ${data.success}")
                            return PostOnDutyApprovalResult.Success

                        }
                        else
                        {
                            Log.d("ON DUTY APPROVAL VIEW MODEL", "qwerty : postOnDutyApprovalDetails API call was successful/false : ${data?.success}")
                            return PostOnDutyApprovalResult.Failure("Failed to post OnDuty approval")
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
                                navController.navigate("${Screen.Network.route}/odApproval")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("ON DUTY APPROVAL VIEW MODEL", "qwerty : postOnDutyApprovalDetails API call was not successful")
                    return PostOnDutyApprovalResult.Failure("Null response")
                }
            }

            return PostOnDutyApprovalResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostOnDutyApprovalResult.Failure("An error occurred: ${e.message}")
        }

    }



    fun postOnDutyStatusUpdate(navController: NavController, empId: String, ruleID: String, odDate: String, org: String, approvalAction: String, reason: String, context : Context)
    {
        viewModelScope.launch {
            try
            {
                loading1.value = true

                Log.d("ON DUTY APPROVAL VIEW MODEL...","Inside postOnDutyStatusUpdate : Before when")
                // Perform the approval and rejection logic

                Log.d("ON DUTY APPROVAL VIEW MODEL...","empId :$empId, ruleID: $ruleID, odDate: $odDate, org: $org, approvalAction: $approvalAction, reason: $reason ")


                when (val result = postOnDutyApprovalDetails(navController, context, empId, ruleID, odDate, org, approvalAction, reason)) {
                    is PostOnDutyApprovalResult.Success ->
                    {

                        Log.d("ON DUTY APPROVAL VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (approvalAction == "1") "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateOnDutyData(navController,context,empID,org)
                    }
                    is PostOnDutyApprovalResult.Failure ->
                    {
                        Log.d("ON DUTY APPROVAL VIEW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (approvalAction == "1") "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateOnDutyData(navController,context,empID,org)
                    }
                }

                Log.d("ON DUTY APPROVAL VIEW MODEL...","Inside postOnDutyStatusUpdate : After when.. fetchAndUpdateOnDutyData ")
                fetchAndUpdateOnDutyData(navController,context,empID,org)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("ON DUTY APPROVAL VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
            }
            finally
            {
                Log.d("ON DUTY APPROVAL VIEW MODEL...","Inside finally")
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateOnDutyData(navController,context,empID,org)
            }
        }
    }

}