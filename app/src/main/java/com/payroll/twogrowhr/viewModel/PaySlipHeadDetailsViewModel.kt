package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipDeductionDetails
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipEarningDetails
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipHeadDetails
import com.payroll.twogrowhr.Model.ResponseModel.PaySlipOtherDeductionDetails
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch

class PaySlipHeadDetailsViewModel (private var repository: Repository,
                        private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    // LIST OF PAYSLIP

    val paySlipHeadList = savedStateHandle.getStateFlow("paySlipHeadList", emptyList<PaySlipHeadDetails>())
    val paySlipEarningList = savedStateHandle.getStateFlow("paySlipEarningList", emptyList<PaySlipEarningDetails>())
    val paySlipDeductionList = savedStateHandle.getStateFlow("paySlipDeductionList", emptyList<PaySlipDeductionDetails>())
    val paySlipOtherDeductionList = savedStateHandle.getStateFlow("paySlipOtherDeductionList", emptyList<PaySlipOtherDeductionDetails>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getPaySlipHeadList(navController: NavController, context: Context, Emp_Id: String,Month:String, Year : String,) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("PayslipHeadViewModel", "qwerty : Inside getPaySlipHeadList, empID : $empId, year: $Year ,Month:$Month, ")


        when (val response = repository.getPayslipHeadResponse(context = context, Emp_Id = empId, Month=Month, Year = Year,)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["paySlipHeadList"] = data.data
                        Log.d("PayslipHeadViewModel", "qwerty : getPaySlipHeadList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["paySlipHeadList"] = emptyList<PaySlipHeadDetails>()
                        Log.d("PayslipHeadViewModel", "qwerty : getPaySlipHeadList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }
            }

            is Resource.Error -> {
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/reports")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        Log.d("PayslipHeadViewModel", "qwerty : getPaySlipHeadList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["paySlipHeadList"] = emptyList<PaySlipHeadDetails>()
                        Log.d("PayslipHeadViewModel", "qwerty : getPaySlipHeadList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }


    fun getPaySlipEarningList(navController: NavController, context: Context, Emp_Id: String,Month:String, Year : String) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("PayslipEarningViewModel", "qwerty : Inside PayslipEarningViewModel, empID : $empId, year: $Year ,Month:$Month" )

        when (val response = repository.getPayslipEarningResponse(context = context, Emp_Id = empId, Month=Month, Year = Year,)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["paySlipEarningList"] = data.data
                        Log.d("PayslipEarningViewModel", "qwerty : getPaySlipEarningList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["paySlipEarningList"] = emptyList<PaySlipEarningDetails>()
                        Log.d("PayslipViewModel", "qwerty : getPaySlipEarningList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }
            }

            is Resource.Error -> {
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/reports")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        Log.d("PayslipViewModel", "qwerty : getPaySlipEarningList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["paySlipEarningList"] = emptyList<PaySlipEarningDetails>()
                        Log.d("PayslipViewModel", "qwerty : getPaySlipEarningList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }
    fun getPaySlipDeductionList(navController: NavController, context: Context, Emp_Id: String,Month:String, Year : String) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("paySlipOtherDeductionList", "qwerty : Inside getPaySlipOtherDeductionList, empID : $empId, year: $Year ,Month:$Month" ,)

        when (val response = repository.getPayslipDeductionResponse(context = context, Emp_Id = empId, Month=Month, Year = Year)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["paySlipDeductionList"] = data.data
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["paySlipDeductionList"] = emptyList<PaySlipDeductionDetails>()
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }
            }

            is Resource.Error -> {
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/reports")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["paySlipDeductionList"] = emptyList<PaySlipDeductionDetails>()
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }


    fun getPaySlipOtherDeductionList(navController: NavController, context: Context, Emp_Id: String,Month:String, Year : String) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("getPaySlipDeductionList", "qwerty : Inside getPaySlipDeductionList, empID : $empId, year: $Year ,Month:$Month" ,)

        when (val response = repository.getPayslipOtherDeductionResponse(context = context, Emp_Id = empId, Month=Month, Year = Year)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["paySlipOtherDeductionList"] = data.data
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipOtherDeductionList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["paySlipOtherDeductionList"] = emptyList<PaySlipOtherDeductionDetails>()
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipOtherDeductionList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }
            }

            is Resource.Error -> {
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/reports")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["paySlipOtherDeductionList"] = emptyList<PaySlipOtherDeductionDetails>()
                        Log.d("getPaySlipOtherDeductionList", "qwerty : getPaySlipDeductionList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }

}