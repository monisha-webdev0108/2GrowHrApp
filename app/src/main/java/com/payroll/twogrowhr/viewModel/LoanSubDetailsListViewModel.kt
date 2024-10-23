package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanSubDetailsData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveCompoOffData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class LoanSubDetailsListViewModel
@Inject
constructor(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loanSubDetailList = savedStateHandle.getStateFlow("loanSubDetailList", emptyList<LoanSubDetailsData>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0


    fun getLoanSubDetails(
        navController:NavController,
        context: Context,
        Sl_No: Int
    ) = viewModelScope.launch {

        when(val response = repository.getLoanSubDetailResponse(context = context, Sl_No = Sl_No))
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
                        Log.d("LoanSubDetailViewModel", "qwerty : getLoanSubDetails API call successful true. Message: ${data.Detail}")
                        savedStateHandle["loanSubDetailList"] = data.Detail
                        flag = 1
                    }
                    else
                    {
                        savedStateHandle["loanSubDetailList"] = emptyList<LoanSubDetailsData>()
                        Log.d("LoanSubDetailViewModel", "qwerty : getLoanSubDetails API call successful false. Message: ${data?.Detail}")
                        flag = 2
                    }

                }

            }
            is Resource.Error->{
                Log.d("Loan List", "qwerty : getLoanSubDetails API call not successful. ")

                loadingStatus = false

                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/Loan")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        Log.d("Loan List", "qwerty : getLoanSubDetails API call was successful/Data Not Found")
                        savedStateHandle["loanSubDetailList"] = emptyList<LoanSubDetailsData>()
                    }
                }


                savedStateHandle["compoOffApprovalList"] = emptyList<UnApproveCompoOffData>()

                flag = 2
            }
        }

        loadingStatus = false
    }
}