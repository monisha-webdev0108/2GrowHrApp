package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.HolidayListData
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch

class ChangePasswordView(
    private var repository: Repository,
) : ViewModel() {
    sealed class PostChangePassword {
        object Success :  PostChangePassword()
        data class Failure(val message: String) : PostChangePassword()
    }
    suspend fun getChangePasswordRegularizedDetails(
        navController: NavController, context: Context, empId: String, id: Int, password: String,
    ) = viewModelScope.launch{
        when(val response = repository.getChangePasswordResponse(context = context, sfCode = empId, id = id, password = password))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {
                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("HolidayViewModel", "qwerty : getHolidayDetails API call was successful/true ")
                        PostChangePassword.Success
                        Constant.showToast(context, "Password changed successfully")
                        navController.navigate("Login")
                    }
                    else
                    {
                        Log.d("HolidayViewModel", "qwerty : getHolidayDetails API call was successful/true :")
                        PostChangePassword.Failure("Failed to post OnDuty details")
                    }
                }
            }

            is Resource.Error->{
                Log.d("Change Password", "qwerty : getLoanDetails1 API call not successful. ")
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/HomeScreen")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                            Log.d("Something went wrong","Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("Change Password", "qwerty : getLoanDetails1 API call was successful/Data Not Found")
//                        savedStateHandle["loanList"] = emptyList<LoanData>()
                    }
                }
                return@launch
            }
        }
    }

}

