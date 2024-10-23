package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Database.MyDatabaseHelper
import com.payroll.twogrowhr.Model.ResponseModel.EmployeeData
import com.payroll.twogrowhr.Model.ResponseModel.EmployeeDetailsResponseModel
import com.payroll.twogrowhr.Model.View.sqLite
import com.payroll.twogrowhr.repository.Repository
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    val employeeDetails = savedStateHandle.getStateFlow("employeeDetails", emptyList<EmployeeData>())


    fun getEmployeeDetails(navController: NavController, empID: String, context: Context) = viewModelScope.launch{

        sqLite = MyDatabaseHelper(context)

        var empData: List<EmployeeData>


        Log.d("ProfileViewModel", "qwerty : Inside getEmployeeDetails1, empID : $empID")



        when(val response = repository.getEmployeeDetailResponse(context, sfCode = empID))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{

                statusLoading.value = false

                response.let {
                    val data = response.data

                    val jsonObject = JSONObject(data.toString())
                    val dataJsonArray = jsonObject.getJSONArray("Data")

                    val responseModel = Gson().fromJson(data, EmployeeDetailsResponseModel::class.java)

                    val success = responseModel.success

                    if(success)
                    {
                        savedStateHandle["employeeDetails"] = responseModel.data

                        empData = responseModel.data

                        sqLite?.deleteEmployeeMaster()
                        sqLite?.createEmployeeMaster(dataJsonArray.toString())

                        Log.d("ProfileViewModel", "qwerty : Inside getEmployeeDetails1, empData : $empData")

                    }
                    else
                    {
                        savedStateHandle["employeeDetails"] = emptyList<EmployeeData>()
                    }
                }

            }
            is Resource.Error->{
                Log.d("ProfileViewModel", "qwerty : getEmployeeDetails1 API call not successful. ")

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
                }
            }
        }


        /*        val data = repository.getEmployeeDetailResponse(sfCode = empID)

        data?.let {

            val jsonObject = JSONObject(data.toString())
            val dataJsonArray = jsonObject.getJSONArray("Data")

            val responseModel = Gson().fromJson(data, EmployeeDetailsResponseModel::class.java)

            val success = responseModel.success

            if(success)
            {
                savedStateHandle["employeeDetails"] = responseModel.data

                empData = responseModel.data

                sqLite?.deleteEmployeeMaster()
                sqLite?.createEmployeeMaster(dataJsonArray.toString())

                Log.d("ProfileViewModel", "qwerty : Inside getEmployeeDetails1, empData : $empData")

            }
            else
            {
                savedStateHandle["employeeDetails"] = emptyList<EmployeeData>()
            }
        }?: run {
        }
        if (data == null)
        {
            Log.d("ProfileViewModel", "qwerty : getEmployeeDetails1 API call not successful. ")
        }*/


    }
}


