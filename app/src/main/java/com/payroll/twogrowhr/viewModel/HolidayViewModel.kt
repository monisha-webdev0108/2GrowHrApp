package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.HolidayListData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch

class HolidayViewModel (private var repository: Repository,
                        private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val holidayList = savedStateHandle.getStateFlow("holidayList", emptyList<HolidayListData>())

    fun getHolidayDetails(
        navController: NavController, context: Context, empId: String, year : String
    ) = viewModelScope.launch {
        Log.d("HolidayViewModel", "qwerty : Inside getHolidayDetails1, empID : $empId, year: $year ")

        when(val response = repository.getHolidayResponse(context, sfCode = empId, year = year))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {

                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("HolidayViewModel", "qwerty : getHolidayDetails API call was successful/true : ${data.data}")
                        savedStateHandle["holidayList"] = data.data
                    }
                    else
                    {
                        Log.d("HolidayViewModel", "qwerty : getHolidayDetails API call was successful/true : ${data?.data}")
                        savedStateHandle["holidayList"] = emptyList<HolidayListData>()
                    }
                }
            }
            is Resource.Error->{
                Log.d("HolidayViewModel", "qwerty : getHolidayDetails1 API call not successful. ")
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
                    else
                    {
                        Log.d("HolidayViewModel", "qwerty : getHolidayDetails API call was successful/Data Not Found")
                        savedStateHandle["holidayList"] = emptyList<HolidayListData>()
                    }
                }
                return@launch
            }
        }

    }


    fun getHoliday(navController: NavController, context: Context, empID : String, year: String)
    {
        Log.d("HolidayViewModel", "qwerty : Inside getHoliday, empID : $empID, year: $year ")

        getHolidayDetails(navController,context,empID,year)
    }

}
