package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AssetListData
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLoanData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch

class AssetsListViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val AssetList = savedStateHandle.getStateFlow("AssetList", emptyList<AssetListData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0
    fun getAssetDetails(
        navController: NavController,
        context: Context,
        empId: String,

    ) = viewModelScope.launch {

        when(val response = repository.getAssetResponse(context = context, Emp_ID = empId))
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
                        Log.d("AssetDetailViewModel", "qwerty : getAssetDetails API call was successful/true : ${data.data}")
                        savedStateHandle["AssetList"] = data.data
                        flag = 1
                    }
                    else
                    {
                        savedStateHandle["AssetList"] = emptyList<AssetListData>()
                        Log.d("AssetDetailViewModel", "qwerty : getAssetDetails API call was successful/false : ${data?.data}")
                        flag = 2
                    }

                }

            }
            is Resource.Error->{
                Log.d("AssetList View Model", "qwerty : getAssetListDetails API call not successful. ")
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
                        Log.d("AssetList", "qwerty : getAssetDetails API call was successful/Data Not Found")
                        savedStateHandle["AssetList"] = emptyList<AssetListData>()

                    }
                }
                savedStateHandle["AssetList"] = emptyList<AssetListData>()

                flag = 2
                return@launch
            }

        }
        loadingStatus = false
    }

}