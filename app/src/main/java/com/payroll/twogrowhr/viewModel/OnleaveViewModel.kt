package com.payroll.twogrowhr.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch
class OnLeaveViewModel (
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val OnLeaveList = savedStateHandle.getStateFlow("OnLeaveList", emptyList<WishesData>())
    fun getOnLeaveDetails(
        navController:NavController,
        context: Context,
        empId: String,
        org: Int,
    ) = viewModelScope.launch {

        when(val response = repository.getWishResponse(context = context, sfCode = empId, id = org))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{

                response.let {

                    val data = response.data

                    if(data?.success == true)
                    {
                        Log.d("OnLeaveViewModel", "qwerty : getOnLeaveDetails API call was successful/true : ${data.Head}")
                        savedStateHandle["OnLeaveList"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["OnLeaveList"] = emptyList<WishesData>()
                        Log.d("OnLeaveViewModel", "qwerty : getOnLeaveDetails API call was successful/false : ${data?.Head}")

                    }

                }

            }
            is Resource.Error->{
                Log.d("OnLeaveDetails", "qwerty : getOnLeaveDetails API call not successful. ")
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
                        Log.d("OnLeaveDetails", "qwerty : getOnLeaveDetails API call was successful/Data Not Found")
                        savedStateHandle["OnLeaveList"] = emptyList<WishesData>()
                    }
                }
                return@launch
            }
        }


    }
}
