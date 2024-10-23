package com.payroll.twogrowhr.viewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch


class BirthdayDetailViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val birthdayList = savedStateHandle.getStateFlow("birthdayList", emptyList<WishesData>())
    fun getBirthdayDetails(
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
                        Log.d("WishesViewModel", "qwerty : getBirthdayDetails API call was successful/true : ${data.Head}")
                        savedStateHandle["birthdayList"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["birthdayList"] = emptyList<WishesData>()
                        Log.d("WishesViewModel", "qwerty : getBirthdayDetails API call was successful/false : ${data?.Head}")

                    }

                }

            }
            is Resource.Error->{
                Log.d("BirthdayDetails", "qwerty : getBirthdayDetails API call not successful. ")
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
                        Log.d("BirthdayDetails", "qwerty : getBirthdayDetails API call was successful/Data Not Found")
                        savedStateHandle["birthdayList"] = emptyList<WishesData>()
                    }
                }
                return@launch
            }
        }



/*
        val data = repository.getWishResponse(sfCode = empId, id = org)
        data?.let {
            savedStateHandle["birthdayList"] = it.Head
        }
        if (data == null) {
            Log.d("","")
        }

        */
    }
}
