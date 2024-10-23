package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.RegularizeData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegularizedApprovalViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {



    sealed class PostRegularizeResult {
        object Success : PostRegularizeResult()
        data class Failure(val message: String) : PostRegularizeResult()
    }



// LIST OF APPROVALS FOR REGULARIZE


    val regularizedList = savedStateHandle.getStateFlow("regularizedList", emptyList<RegularizeData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getRegularizedDetails(
        navController: NavController,
        context: Context,
        empId: String
    ) = viewModelScope.launch {



        when(val response = repository.getRegularizedListResponse(context = context, sfCode = empId)){
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
                        Log.d("REGULARIZE VIEW MODEL...", "qwerty : getRegularizedDetails API call was successful/true  : ${data.Head}")
                        savedStateHandle["regularizedList"] = data.Head
                        flag = 1
                    }
                    else
                    {

                        Log.d("REGULARIZE VIEW MODEL", "qwerty : getRegularizedDetails API call was successful/false : ${data?.Head}")

                        savedStateHandle["regularizedList"] = emptyList<RegularizeData>()

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
                            navController.navigate("${Screen.Network.route}/regularizedApproval")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("REGULARIZE VIEW MODEL", "qwerty : getRegularizedDetails API call was successful/Data Not Found")

                        savedStateHandle["regularizedList"] = emptyList<RegularizeData>()
                    }
                }
                Log.d("REGULARIZE VIEW MODEL", "qwerty : getRegularizedDetails API call was not successful")

                savedStateHandle["regularizedList"] = emptyList<RegularizeData>()
                flag = 2
            }
        }

        loadingStatus = false


    }


    fun fetchAndUpdateData(navController: NavController,context: Context,empId: String)
    {
        getRegularizedDetails(navController,context,empId)
    }



// SENT FOR APPROVAL


    var loading1 = mutableStateOf(false)


    private suspend fun postRegularizeApprovalDetails(navController: NavController, context: Context, slNo: String, status: String): PostRegularizeResult {

        try {

            when(val response = repository.postRegularizeApprovalResponse(context = context, slNo = slNo, status = status)){
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
                            Log.d("RegularizedApprovalViewModel", "qwerty : postRegularizeApprovalDetails API call was successful/true : ${data.success}")
                            return PostRegularizeResult.Success

                        }
                        else
                        {
                            Log.d("RegularizedApprovalViewModel", "qwerty : postRegularizeApprovalDetails API call was successful/false : ${data?.success}")
                            return PostRegularizeResult.Failure("Failed to post regularize approval")
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
                                navController.navigate("${Screen.Network.route}/regularizedApproval")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                    }
                    Log.d("RegularizedApprovalViewModel", "qwerty : postRegularizeApprovalDetails API call was not successful")
                    return PostRegularizeResult.Failure("Null response")
                }
            }

            return PostRegularizeResult.Failure("Unexpected response")

        }
        catch (e: Exception) {
            statusLoading.value = false
            e.printStackTrace()
            return PostRegularizeResult.Failure("An error occurred: ${e.message}")
        }


    }



    fun postRegularizeStatusUpdate(navController: NavController, employeeID: String, slNo: String, approvalAction : String, context : Context)
    {
        viewModelScope.launch {
            try
            {

                loading1.value = true

                Log.d("REGULARIZE APPROVAL VIEW MODEL...","Inside postRegularizeStatusUpdate : Before when")
                // Perform the approval and rejection logic

                when (val result = postRegularizeApprovalDetails(navController, context, slNo, approvalAction)) {
                    is PostRegularizeResult.Success ->
                    {

                        Log.d("REGULARIZE APPROVAL VIEW MODEL...","Approval/Rejected Successfully")
                        Constant.showToast(context, if (approvalAction == "1") "Approved Successfully" else "Rejected Successfully")
                        fetchAndUpdateData(navController,context,employeeID)
                    }
                    is PostRegularizeResult.Failure ->
                    {
                        Log.d("REGULARIZE APPROVAL VI EW MODEL...","An error occurred: ${result.message}")
                        Constant.showToast(context, if (approvalAction == "1") "Approval" else "Rejection" + " Failed: Please try again...!")
                        fetchAndUpdateData(navController,context,employeeID)
                    }
                }

                Log.d("REGULARIZE APPROVAL VIEW MODEL...","Inside postRegularizeStatusUpdate : After when.. fetchAndUpdateData ")
                fetchAndUpdateData(navController,context,employeeID)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                Log.d("REGULARIZE APPROVAL VIEW MODEL...","An error occurred: ${e.message}")
                Constant.showToast(context, "Something went wrong...!")
            }
            finally
            {
                Log.d("REGULARIZE APPROVAL VIEW MODEL...","Inside finally")
                loading1.value = false // Set loading state to false after the operation
                fetchAndUpdateData(navController,context,employeeID)
            }
        }
    }

}