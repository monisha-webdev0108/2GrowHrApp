package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocDetailData
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocListData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch



class OrgDocDetailViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val orgDocDetailList = savedStateHandle.getStateFlow("orgDocDetailList", emptyList<OrgDocDetailData>())
    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false
    var flag = savedStateHandle.get<Int>("flag") ?: 0
    fun fetchAndUpdateOrgDocDetailData(navController: NavController,context: Context,empId: String,org: Int,folderId: Int)
    {
        getOrgDocDetailDetails(navController,context,empId,org,folderId)
    }
    fun getOrgDocDetailDetails(
        navController: NavController,
        context: Context,
        empId: String,
        org: Int,
        folderId: Int
    ) = viewModelScope.launch {

        when(val response = repository.getOrgDocDetailResponse(context = context, sfCode = empId, id = org,folderId = folderId))
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
                        savedStateHandle["orgDocDetailList"] = data.Data
                        flag = 1
                        Log.d("response","${response}")
                    }
                    else
                    {
                        savedStateHandle["orgDocDetailList"] = emptyList<OrgDocDetailData>()
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
                        savedStateHandle["orgDocDetailList"] = emptyList<OrgDocDetailData>()
                    }
                }
                savedStateHandle["orgDocDetailList"] = emptyList<OrgDocDetailData>()

                flag = 2
                return@launch
            }

        }
        loadingStatus = false
    }

    sealed class PostOrgDocShowCount{
        object Success :  PostOrgDocShowCount()
        data class Failure(val message: String) : PostOrgDocShowCount()
    }
    suspend fun getOrgDocShowCountDetails(
        navController: NavController, context: Context, empId: String, org: Int, documentId:Int
    ) = viewModelScope.launch{
        when(val response = repository.getOrgDocShowCountResponse(context = context, sfCode = empId, org = org, documentId=documentId ))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {
                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("OrgDOcShowCount", "qwerty : getOrgDOcShowCountResponse API call was successful/true ")
                        PostOrgDocShowCount.Success

                    }
                    else
                    {
                        Log.d("OrgDOcShowCount", "qwerty : getOrgDOcShowCountResponse API call was successful/false :")
                        PostOrgDocShowCount.Failure("Failed to post OrgDOcShowCount details")
                    }
                }
            }
            is Resource.Error->{
                Log.d("OrgDOcShowCount", "qwerty : getOrgDOcShowCountResponse API call not successful. ")
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Attendance")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("OrgDOcShowCount", "qwerty : getOrgDOcShowCountResponse API call was successful/Data Not Found")

                    }
                }
                return@launch
            }

        }
    }
}

