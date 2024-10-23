package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocListData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch


class OrgDocListViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val orgDocList = savedStateHandle.getStateFlow("orgDocList", emptyList<OrgDocListData>())
    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false
    var flag = savedStateHandle.get<Int>("flag") ?: 0

    fun getOrgDocListDetails(
        navController: NavController,
        context: Context,
        empId: String,
        org: Int,
    ) = viewModelScope.launch {

        when(val response = repository.getOrgDocListResponse(context = context, sfCode = empId, id = org))
        {
            is Resource.Loading->{
                flag = 0
                loadingStatus = true
            }
            is Resource.Success->{

                response.let {

                    val data = response.data
                    loadingStatus = false
                    if(data?.success == true)
                    {
                        Log.d("orgDocListDetailViewModel", "qwerty : getOrgDocListDetails API call was successful/true : ${data.Data}")
                        savedStateHandle["orgDocList"] = data.Data
                        flag = 1
                    }
                    else
                    {
                        savedStateHandle["orgDocList"] = emptyList<OrgDocListData>()
                        Log.d("orgDocListDetailViewModel", "qwerty : getOrgDocListDetails API call was successful/false : ${data?.Data}")
                        flag = 2
                    }

                }

            }
            is Resource.Error->{
                Log.d("orgDocList View Model", "qwerty : getOrgDocListDetails API call not successful. ")
                loadingStatus = false
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
                        Log.d("orgDocList", "qwerty : getOrgDocListDetails API call was successful/Data Not Found")
                        savedStateHandle["orgDocList"] = emptyList<OrgDocListData>()
                    }
                }
                savedStateHandle["orgDocList"] = emptyList<OrgDocListData>()

                flag = 2
                return@launch
            }

        }
        loadingStatus = false
    }
}