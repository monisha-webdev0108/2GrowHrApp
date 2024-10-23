package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.PayslipMonthData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch


class PaySlipViewModel (private var repository: Repository,
                        private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    // LIST OF PAYSLIP


    val paidMonthList = savedStateHandle.getStateFlow("paidMonthList", emptyList<PayslipMonthData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getPaidMonthList(navController: NavController, context: Context, year : String) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("PayslipViewModel", "qwerty : Inside getPaidMonthList, empID : $empId, year: $year ")


        when (val response = repository.getPayslipResponse(context = context, sfCode = empId, year = year)) {
            is Resource.Loading -> {
                loadingStatus = true
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["paidMonthList"] = data.data
                        Log.d("PayslipViewModel", "qwerty : getPaidMonthList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["paidMonthList"] = emptyList<PayslipMonthData>()
                        Log.d("PayslipViewModel", "qwerty : getPaidMonthList API call successful/false. ${data?.data} ")
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
                        Log.d("PayslipViewModel", "qwerty : getPaidMonthList API call not successful.")
                    }
                    else
                    {
                        savedStateHandle["paidMonthList"] = emptyList<PayslipMonthData>()
                        Log.d("PayslipViewModel", "qwerty : getPaidMonthList API call successful/ Data Not Found ")
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }

}