package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.TdsFormMonthData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch



class TdsFormViewModel (
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    // LIST OF PAYSLIP


    val tdsMonthList = savedStateHandle.getStateFlow("tdsMonthList", emptyList<TdsFormMonthData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getTdsFormMonthList(navController:NavController, context: Context, year : String) = viewModelScope.launch {

        val empId = userViewModel.getSFCode()

        Log.d("PayslipViewModel", "qwerty : Inside getPaidMonthList, empID : $empId, year: $year ")


        when (val response = repository.getTdsFormResponse(context = context, sfCode = empId, year = year)) {
            is Resource.Loading -> {
                flag = 0
            }

            is Resource.Success -> {
                response.let {
                    val data = response.data

                    if (data?.success == true) {
                        savedStateHandle["tdsMonthList"] = data.data
                        Log.d("TdsFormViewModel", "qwerty : getTdsMonthList API call successful/true. ${data.data} ")
                        flag = 1
                    } else {
                        savedStateHandle["tdsMonthList"] = emptyList<TdsFormMonthData>()
                        Log.d("TdsFormViewModel", "qwerty : getTdsMonthList API call successful/false. ${data?.data} ")
                        flag = 2
                    }
                }

            }

            is Resource.Error->{
                Log.d("Tds", "qwerty : getLoanDetails1 API call not successful. ")
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Reports")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("Loan List", "qwerty : getLoanDetails1 API call was successful/Data Not Found")
                        savedStateHandle["tdsMonthList"] = emptyList<TdsFormMonthData>()
                    }
                }
                flag = 2
                return@launch
            }
        }

        loadingStatus = false

    }

}