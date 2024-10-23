package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRegularizeCount
import com.payroll.twogrowhr.Model.ResponseModel.AttendanceRegularizeRules
import com.payroll.twogrowhr.Model.ResponseModel.AttendenceCICOData
import com.payroll.twogrowhr.Model.ResponseModel.AttendenceData
import com.payroll.twogrowhr.Model.ResponseModel.AttendenceRBHide
import com.payroll.twogrowhr.Model.ResponseModel.HolidayListData
import com.payroll.twogrowhr.Model.ResponseModel.TdsFormMonthData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.repository.Repository
import com.payroll.twogrowhr.util.Resource
import kotlinx.coroutines.launch

class AttendanceRegularizedViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val attendanceList = savedStateHandle.getStateFlow("attendanceList", emptyList<AttendenceData>())
    fun getAttendanceRegularizedDetails(
        navController:NavController,
        context: Context,
        empId: String,
        fromdate : String,
        todate : String,
    ) = viewModelScope.launch {
        when(val response = repository.getAttendanceRegularizedResponse(context = context, sfCode = empId, fDT = fromdate,tDT=todate))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {

                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("attendanceListViewModel", "qwerty : getAttendanceRegularizedResponse API call was successful/true : ${data.Head}")
                        savedStateHandle["attendanceList"] = data.Head
                    }
                    else
                    {
                        Log.d("attendanceListViewModel", "qwerty : getAttendanceRegularizedResponse API call was successful/true : ${data?.Head}")
                        savedStateHandle["attendanceList"] = emptyList<AttendenceData>()

                    }
                }
            }
            is Resource.Error->{
                Log.d("attendanceListViewModel", "qwerty : getAttendanceRegularizedResponse API call not successful. ")
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
                        Log.d("attendanceListViewModel", "qwerty : getAttendanceRegularizedResponse API call was successful/Data Not Found")
                        savedStateHandle["attendanceList"] = emptyList<AttendenceData>()
                    }
                }
                return@launch
            }
        }

    }

    val attendanceCICOList = savedStateHandle.getStateFlow("attendanceCICOList", emptyList<AttendenceCICOData>())
    fun getAttendenceCICORegularizedDetails(
        navController: NavController,
        context:Context,
        empId: String,
        DT : String,
    ) = viewModelScope.launch {
        when(val response = repository.getAttendanceRegularizedCICOResponse(context = context, sfCode = empId, DT = DT))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {

                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call successful true. Message: ${data.Head}")
                        savedStateHandle["attendanceCICOList"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["attendanceCICOList"] = emptyList<AttendenceCICOData>()
                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call successful false. Message: ${data?.Head}")

                    }
                }
            }
            is Resource.Error->{
                Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call not successful. ")

                savedStateHandle["attendanceCICOList"] = emptyList<AttendenceCICOData>()

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
                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call was successful/Data Not Found")
                        savedStateHandle["attendanceCICOList"] = emptyList<AttendenceCICOData>()
                    }
                }
                else
                {
                    Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call was successful/No Response")
                    savedStateHandle["attendanceCICOList"] = emptyList<AttendenceCICOData>()
                }
                return@launch
            }
        }

    }

    val attendanceRBHList = savedStateHandle.getStateFlow("attendanceRBHList", emptyList<AttendenceRBHide>())
    fun getAttendenceRBHRegularizedDetails(
        context:Context,
        navController: NavController,
        empId: String,
        FT : String,
    ) = viewModelScope.launch {
        Log.d("getAttendanceRegularizedRBHResponse", "empId/FT : $empId/$FT")
        when(val response = repository.getAttendanceRegularizedRBHResponse(context = context, sfCode = empId, FT = FT))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {

                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call successful true. Message: ${data.Head}")
                        savedStateHandle["attendanceRBHList"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["attendanceRBHList"] = emptyList<AttendenceRBHide>()

                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedCICOResponse API call successful false. Message: ${data?.Head}")

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
                        savedStateHandle["attendanceRBHList"] = emptyList<AttendenceRBHide>()
                        Log.d("attendanceRegularizeCount", "qwerty : getAttendenceRBHRegularizedDetails API call successful :true/ Data Not Found")
                    }





/*
                    if(response.message.toString() != "Not Found")
                    {
                        Constant.showToast(context, response.message ?: "Something went wrong")
                    }*/
                }
                return@launch
            }
        }

    }

    val attendanceRegularizeRules = savedStateHandle.getStateFlow("attendanceRegularizeRules", emptyList<AttendanceRegularizeRules>())
    fun getAttendanceRegularizeRulesDetail(
        context:Context,
        navController: NavController,
        empId: String,
    ) = viewModelScope.launch {
        when(val response = repository.getAttendanceRegularizedRulesResponse(context = context, sfCode = empId))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {

                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("attendanceRegularizeRules", "qwerty : getAttendanceRegularizedRulesResponse API call successful true. Message: ${data.Head}")
                        savedStateHandle["attendanceRegularizeRules"] = data.Head
                    }
                    else
                    {
                        savedStateHandle["attendanceRegularizeRules"] = emptyList<AttendanceRegularizeRules>()
                        Log.d("AttendanceRegularizedCICO", "qwerty : getAttendanceRegularizedRulesResponse API call successful false. Message: ${data?.Head}")
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
                        savedStateHandle["attendanceRegularizeRules"] = emptyList<AttendanceRegularizeRules>()
                        Log.d("attendanceRegularizeCount", "qwerty : getAttendanceRegularizeRulesDetail API call successful :true/ Data Not Found")
                    }











/*                    if(response.message.toString() != "Not Found")
                    {
                        Constant.showToast(context, response.message ?: "Something went wrong")
                    }*/
                }
                return@launch
            }
        }

    }

    val attendanceRegularizeCount = savedStateHandle.getStateFlow("attendanceRegularizeCount", emptyList<AttendanceRegularizeCount>())

    var loadingStatus = savedStateHandle.get<Boolean>("loadingStatus") ?: false

    var flag = savedStateHandle.get<Int>("flag") ?: 0
    fun getAttendanceRegularizeCountDetail(
        context:Context,
        navController: NavController,
        empId: String,
        mode : String,
        Fdt : String,
        Tdt : String,
        ) = viewModelScope.launch {

        when(val response = repository.getAttendanceRegularizedCountResponse(context = context, sfCode = empId, mode = mode, Fdt = Fdt, Tdt =Tdt))
        {
            is Resource.Loading->{

            }
            is Resource.Success -> {
                response.let {
                    loadingStatus = true
                    val data = it.data
                    val success = data?.success

                    when (success) {
                        true -> {
                            Log.d("attendanceRegularizeCount", "qwerty : getAttendanceRegularized-countResponse API call successful true. Message: ${data.Head}")
                            savedStateHandle["attendanceRegularizeCount"] = data.Head
                            flag = 1
                        }

                        false -> {
                            savedStateHandle["attendanceRegularizeCount"] = emptyList<AttendanceRegularizeCount>()
                            Log.d("attendanceRegularizeCount", "qwerty : getAttendanceRegularized-countResponse API call successful false. Message: ${data?.Head}")
                            flag = 2
                        }

                        else -> {
                            savedStateHandle["attendanceRegularizeCount"] = emptyList<AttendanceRegularizeCount>()
                            flag = 3
                            Log.d("AttendanceRegularizeCount", "qwerty : AttendanceRegularizeCount API call not successful.")
                        }
                    }

                    loadingStatus = false
                }
            }

            is Resource.Error->{

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
                        savedStateHandle["attendanceRegularizeCount"] = emptyList<AttendanceRegularizeCount>()

                        flag = 3

                        Log.d("attendanceRegularizeCount", "qwerty : getAttendanceRegularizeCountDetail API call successful :true/ Data Not Found")
                    }










/*                    if(response.message.toString() != "Not Found")
                    {
                        Constant.showToast(context, response.message ?: "Something went wrong")
                    }*/
                }
                return@launch
            }
        }

    }



    sealed class PostDashAttendanceRegularized {
        object Success :  PostDashAttendanceRegularized()
        data class Failure(val message: String) : PostDashAttendanceRegularized()
    }
    suspend fun getAttendanceRegulaziedDetails(
        navController: NavController, context: Context, empId: String, org: Int, MissedDate: String,StartTime:String,EndTime:String,Remarks:String
    ) = viewModelScope.launch{
        when(val response = repository.getDashAttendanceRegularizedResponse(context = context, sfCode = empId, org = org, MissedDate = MissedDate, StartTime=StartTime,EndTime=EndTime,Remarks=Remarks ))
        {
            is Resource.Loading->{

            }
            is Resource.Success->{
                response.let {
                    val data = response.data
                    val success = data?.success
                    if(success == true)
                    {
                        Log.d("AttendanceRegulaziedDetails", "qwerty : getDashAttendanceRegularizedResponse API call was successful/true ")
                        PostDashAttendanceRegularized.Success
                        Constant.showToast(context, "Regularized successfully")
                        navController.navigate("Attendance")
                    }
                    else
                    {
                        Log.d("AttendanceRegulaziedDetails", "qwerty : getDashAttendanceRegularizedResponse API call was successful/true :")
                        PostDashAttendanceRegularized.Failure("Failed to post AttendanceRegulaziedDetails details")
                    }
                }
            }
            is Resource.Error->{
                Log.d("AttendanceRegulaziedDetails", "qwerty : getDashAttendanceRegularizedResponse API call not successful. ")
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
                        Log.d("AttendanceRegulaziedDetails", "qwerty : getDashAttendanceRegularizedResponse API call was successful/Data Not Found")

                    }
                }
                return@launch
            }

        }
    }

}
