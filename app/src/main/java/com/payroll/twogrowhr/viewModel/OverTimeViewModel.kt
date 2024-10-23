package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OverTimeData
import com.payroll.twogrowhr.Model.ResponseModel.TdsFormMonthData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class OverTimeViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {


    sealed class PostOverTimeResult {
        object Success :  PostOverTimeResult()
        data class Failure(val message: String) : PostOverTimeResult()
    }

    val org = userViewModel.getOrg()
    val empID = userViewModel.getSFCode()

    private val overTimeList = MutableStateFlow<List<OverTimeData>>(emptyList())


//------------------------------------------LIST OF APPROVALS FOR OVER TIME--------------------------------------------------//


    val overTimeApprovalList = savedStateHandle.getStateFlow("overTimeApprovalList", emptyList<OverTimeData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getOverTimeApprovalDetails(navController:NavController, context: Context, empId: String, org: String) = viewModelScope.launch {



        when(val response = repository.getOverTimeListResponse(context = context, sfCode = empId, org = org)){
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
                        Log.d("OVER TIME DETAIL VIEW MODEL...", "qwerty : getOverTimeApprovalDetails API call was successful/true  : ${data.head}")
                        savedStateHandle["overTimeApprovalList"] = data.head
                        flag = 1
                    }
                    else
                    {

                        Log.d("OVER TIME DETAIL VIEW MODEL", "qwerty : getOverTimeApprovalDetails API call was successful/false : ${data?.head}")

                        savedStateHandle["overTimeApprovalList"] = emptyList<OverTimeData>()

                        flag = 2
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
                            navController.navigate("${Screen.Network.route}/ApprovalList")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                }
                overTimeList.value = emptyList()
                /*callback(overTimeList.value)*/
                return@launch
            }

        }

        loadingStatus = false


    }


    fun fetchAndUpdateOverTimeData(navController: NavController,context: Context, empId: String, org: String)
    {
        getOverTimeApprovalDetails(navController,context,empId, org)
    }


//------------------------------------------APPROVALS - OVER TIME DETAILS FOR SELECTED EMPLOYEE--------------------------------------------------//


    var loading1 = mutableStateOf(false)

    private suspend fun postOverTimeApprovalDetails(navController:NavController, context: Context, empId: String, org: Int, hours: Float, ratePerHour:Float, approvalAction: Int, checkin: String, checkout: String, weekStart: String, weekEnd: String, month: Int, year: Int, OtHoli_rate_per_hr_National: Float, OtMax_hr_holi_Festival: Float, OtWkend_per_hr_Weekly: Float, Extra_Hrs: Float, OtIsMaximumHoursAllowedReq: Int, OtMaxHoursLimit: Int, adjustDurationOT: Int, IsBalanceFlowReq: Int, BalanceLaps: Int, IsApprovalFlowReq: Int, Approval: Int, Is_HoursExceeds: Int, HoursExceeds: Int, DurationOff:String, goToApproval: String, Reject_Reason:String): PostOverTimeResult {

        Log.d("postOverTimeApprovalDetails", "empId: $empId \n org:$org \n hours : $hours\n approvalAction :$approvalAction \n checkin: $checkin\n checkout: $checkout\n weekStart: $weekStart\n  weekEnd: $weekEnd\n month: $month\n year: $year\n OtHoli_rate_per_hr_National:$OtHoli_rate_per_hr_National\n OtMax_hr_holi_Festival: $OtMax_hr_holi_Festival\n Extra_Hrs: $Extra_Hrs\n OtIsMaximumHoursAllowedReq: $OtIsMaximumHoursAllowedReq\n  OtMaxHoursLimit: $OtMaxHoursLimit\n adjustDurationOT: $adjustDurationOT\n IsBalanceFlowReq: $IsBalanceFlowReq \n" +
                "BalanceLaps: $BalanceLaps\n IsApprovalFlowReq: $IsApprovalFlowReq\n Approval: $Approval\n Is_HoursExceeds: $Is_HoursExceeds\n HoursExceeds: $HoursExceeds\n DurationOff: $DurationOff\n goToApproval : $goToApproval\n Reject_Reason : $Reject_Reason")

        try {
            when(val response = repository.postOverTimeApprovalResponse(context = context, empId, org, hours, ratePerHour, approvalAction, checkin, checkout, weekStart, weekEnd, month, year, OtHoli_rate_per_hr_National, OtMax_hr_holi_Festival, OtWkend_per_hr_Weekly, Extra_Hrs, OtIsMaximumHoursAllowedReq, OtMaxHoursLimit, adjustDurationOT, IsBalanceFlowReq, BalanceLaps, IsApprovalFlowReq, Approval, Is_HoursExceeds, HoursExceeds, DurationOff, goToApproval, Reject_Reason)){

                is Resource.Loading->{
                    statusLoading.value = true
                }
                is Resource.Success->{

                    statusLoading.value = false

                    response.let {

                        Log.d("postOverTimeApprovalDetails" , "response : $response")

                        val data = response.data

                        Log.d("postOverTimeApprovalDetails" , "response Data: ${response.data}")

                        savedStateHandle["success"] = data?.success

                        if (data?.success == true)
                        {
                            Log.d("OVER TIME VIEW MODEL", "qwerty : postOverTimeApprovalDetails API call was successful/true : ${data.success}")
                            return PostOverTimeResult.Success
                        }
                        else
                        {
                            Log.d("OVER TIME VIEW MODEL", "qwerty : postOverTimeApprovalDetails API call was successful/false : ${data?.success}")
                            return PostOverTimeResult.Failure("Failed to post Compo Off approval")
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
                                navController.navigate("${Screen.Network.route}/ApprovalList")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    overTimeList.value = emptyList()

                }
            }
            return PostOverTimeResult.Failure("Unexpected response")
        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostOverTimeResult.Failure("An error occurred: ${e.message}")
        }
    }

    fun postOverTimeStatusUpdate(navController: NavController,context: Context, empId: String, org: Int, hours: Float, ratePerHour: Float, approvalAction : Int, checkin: String, checkout: String, weekStart: String, weekEnd: String, month: Int, year: Int, OtHoli_rate_per_hr_National: Float, OtMax_hr_holi_Festival: Float, OtWkend_per_hr_Weekly: Float, Extra_Hrs: Float, OtIsMaximumHoursAllowedReq: Int, OtMaxHoursLimit: Int, adjustDurationOT: Int, IsBalanceFlowReq: Int, BalanceLaps: Int, IsApprovalFlowReq: Int, Approval: Int, Is_HoursExceeds: Int, HoursExceeds: Int, DurationOff:String, goToApproval:String, Reject_Reason:String)
    {
        viewModelScope.launch {
            try
            {
                loading1.value = true

                Log.d("OVER TIME VIEW MODEL...","Inside postOverTimeStatusUpdate : Before when")
                // Perform the approval and rejection logic
                when (val result = postOverTimeApprovalDetails(navController,context, empId, org, hours, ratePerHour, approvalAction, checkin, checkout, weekStart, weekEnd, month, year, OtHoli_rate_per_hr_National, OtMax_hr_holi_Festival, OtWkend_per_hr_Weekly, Extra_Hrs, OtIsMaximumHoursAllowedReq, OtMaxHoursLimit, adjustDurationOT, IsBalanceFlowReq, BalanceLaps, IsApprovalFlowReq, Approval, Is_HoursExceeds, HoursExceeds, DurationOff, goToApproval, Reject_Reason )) {
                    is PostOverTimeResult.Success ->
                    {
                        Log.d("OVER TIME VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (approvalAction == 1) "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateOverTimeData(navController,context,empID,org.toString())
                    }
                    is PostOverTimeResult.Failure ->
                    {
                        Log.d("OVER TIME VIEW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (approvalAction == 1) "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateOverTimeData(navController,context,empID,org.toString())
                    }
                }
                Log.d("OVER TIME VIEW MODEL...","Inside postOverTimeStatusUpdate : After when.. fetchAndUpdateOverTimeData ")
                fetchAndUpdateOverTimeData(navController ,context,empID,org.toString())
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("OVER TIME VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
            }
            finally
            {
                Log.d("OVER TIME VIEW MODEL...","Inside finally")
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateOverTimeData(navController ,context,empID,org.toString())
            }
        }
    }
}