package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.CTC
import com.payroll.twogrowhr.Model.ResponseModel.Deduction
import com.payroll.twogrowhr.Model.ResponseModel.Earning
import com.payroll.twogrowhr.Model.ResponseModel.Tot
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SalaryDetailViewModel
@Inject
constructor(private var repository: Repository,
            private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var earningList = savedStateHandle.getStateFlow("earningList", emptyList<Earning>())
    var deductionList = savedStateHandle.getStateFlow("deductionList", emptyList<Deduction>())
    var ctcList = savedStateHandle.getStateFlow("ctcList", emptyList<CTC>())
    var totList = savedStateHandle.getStateFlow("totList", emptyList<Tot>())

    var loadingStatus = savedStateHandle.get<Boolean>("status") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0

    fun getSalaryDetails(
        navController: NavController,
        context: Context,
        empId: String
    ) =
        viewModelScope.launch {

            when(val response = repository.getSalaryResponse(context = context, sfCode = empId)){
                is Resource.Loading->{
                    loadingStatus = true
                    flag = 0
                }
                is Resource.Success->{

                    response.let {

                        val data = response.data

                        savedStateHandle["success"] = data?.success

                        if(data?.success == true)
                        {

                            savedStateHandle["earningList"] = data.earning
                            savedStateHandle["deductionList"] = data.deduction
                            savedStateHandle["ctcList"] = data.ctc
                            savedStateHandle["totList"] = data.tot
                            flag = 1

                            Log.d("SalaryDetailViewModel", "qwerty : getSalaryDetails API call was successful/true")

                            Log.d("SALARY DETAIL VIEW MODEL...", "After changing flag value : $flag")

                        }
                        else
                        {
                            savedStateHandle["earningList"] = emptyList<Earning>()
                            savedStateHandle["deductionList"] = emptyList<Deduction>()
                            savedStateHandle["ctcList"] = emptyList<CTC>()
                            savedStateHandle["totList"] = emptyList<Tot>()
                            flag = 2
                            Log.d("SalaryDetailViewModel", "qwerty : getSalaryDetails API call was successful/false")
                            Log.d("SALARY DETAIL VIEW MODEL...", "After changing flag value : $flag")

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
                                navController.navigate("${Screen.Network.route}/reports")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            Log.d("SalaryDetailViewModel", "qwerty : getSalaryDetails API call was not successful")
                        }
                        else
                        {
                            savedStateHandle["earningList"] = emptyList<Earning>()
                            savedStateHandle["deductionList"] = emptyList<Deduction>()
                            savedStateHandle["ctcList"] = emptyList<CTC>()
                            savedStateHandle["totList"] = emptyList<Tot>()

                            Log.d("SalaryDetailViewModel", "qwerty : getSalaryDetails API call was successful/ Data Not Found")

                        }
                    }
                    flag = 2
                }

            }


            // Set loading to false after data is fetched
            loadingStatus = false

        }

}