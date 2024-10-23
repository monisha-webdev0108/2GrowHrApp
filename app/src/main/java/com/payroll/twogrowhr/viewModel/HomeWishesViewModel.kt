package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.BirthdayWishesData
import com.payroll.twogrowhr.Model.ResponseModel.WeddingWishesData
import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch


class HomeWishesViewModel  (
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val wishList = savedStateHandle.getStateFlow("wishList", emptyList<WishesData>())
    fun getWishesDetails(
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
                        Log.d("WishesViewModel", "qwerty : getWishesDetails API call was successful/true : ${data.Head}")
                        savedStateHandle["wishList"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["wishList"] = emptyList<WishesData>()
                        Log.d("WishesViewModel", "qwerty : getWishesDetails API call was successful/false : ${data?.Head}")

                    }

                }

            }
            is Resource.Error->{
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        Constant.showToast(context, response.message ?: "Something went wrong")
                    }
                }
                Log.d("WishesViewModel", "qwerty : getWishesDetails API call was not successful")
                return@launch
            }
        }


/*
        val data = repository.getWishResponse(sfCode = empId, id = org)
        data?.let {
            val success = it.success
            if(success){
                savedStateHandle["wishList"] = it.Head
            }else{
                savedStateHandle["wishList"] = emptyList<WishesData>()
            }

        }
        if (data == null) {
            Log.d("","")
        }

        */
    }
    fun updateLists() {
        getBirthDayList()
        getWeddingWeekList()
        getWorkingWeekList()
        getLeaveWeekList()
        getOnDutyWeekList()
        getRemoteWorkWeekList()
    }
    fun getBirthDayList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "B" }
    } else {
        emptyList()
    }
    fun getWeddingWeekList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "W" }
    } else {
        emptyList()
    }
    fun getLeaveWeekList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "L" }
    } else {
        emptyList()
    }
    fun getOnDutyWeekList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "OD" }
    } else {
        emptyList()
    }
    fun getRemoteWorkWeekList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "WFH" }
    } else {
        emptyList()
    }
    fun getWorkingWeekList() = if(wishList.value.isNotEmpty()) {
        wishList.value.filter { it.wish == "J" }
    } else {
        emptyList()
    }

}
