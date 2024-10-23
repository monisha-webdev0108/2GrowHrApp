package com.payroll.twogrowhr.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoginResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UserData
import com.payroll.twogrowhr.Model.View.checkInCount
import com.payroll.twogrowhr.Model.View.sqLite
import com.payroll.twogrowhr.loginState.LoginError
import com.payroll.twogrowhr.loginState.LoginState
import com.payroll.twogrowhr.loginState.LoginSuccess
import com.payroll.twogrowhr.repository.Repository
import com.google.gson.Gson
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private var repository: Repository) : ViewModel() {

    private val _uiState = MutableLiveData<LoginState>(null)
    val uiState: LiveData<LoginState> = _uiState

    private val _uiState1 = MutableLiveData<Resource<LoginResponseModel>?>(null)
    val uiState1: LiveData<Resource<LoginResponseModel>?> = _uiState1

    private val _checkInState = MutableLiveData<Int>(null)
    val checkInState: LiveData<Int> = _checkInState

    private val _orgMode = MutableLiveData<Int>(null)
    val orgMode: LiveData<Int> = _orgMode

    private val _fakeGps = MutableLiveData<Boolean>()
    val fakeGps: LiveData<Boolean> = _fakeGps

    private val _checkInCount = MutableLiveData<Int>(null)
//    val checkInCount: LiveData<Int> = _checkInCount

    fun resetLoginState() {
        _uiState1.value = null
    }

    fun getInitialResponse1(context : Context, username: String, password: String) = viewModelScope.launch {
        Log.d("ViewModelClass_7", "GetLoginResponse() Method is Calling....")
        _uiState1.value = Resource.Loading()
        _uiState1.value = repository.initialRequest1(
            axn = "Login",
            context = context,
            userName = username,
            password = password
        )
    }

    fun clearUiState1() {
        sqLite?.deleteUserLoginData()
        resetLoginState()
        Log.d("ViewModelClass", "clearUiState() Method is Calling.... _uiState.value : ${_uiState.value}")
    }


    fun getInitialResponse(context: Context, username: String, password: String) = viewModelScope.launch {
        Log.d("ViewModelClass_7", "GetLoginResponse() Method is Calling....")
        val data = repository.initialRequest(
            axn = "Login",
            context =  context,
            userName = username,
            password = password
        )

        val success = data?.success

        if (success == true)
        {
            _uiState.value = LoginSuccess(loginResponse = data)
        }
        else
        {
            _uiState.value = data?.let { LoginSuccess(loginResponse = it) }
        }

        if (data == null) {
            _uiState.value = LoginError
            return@launch
        }


    }

    fun clearUiState() {
        val userData = UserData(org = 0, Sf_UserName = "", sf_emp_id = "", DeptName = "", sf_Designation_Short_Name = "", SF_Status = 0, Sf_Name = "", Sf_Password = "", Division_Code = 0, Division_Name = "", DisRad = "", Lattitude = "", Longitude = "", Sf_code  = "",Shift_Selection  = "", CheckCount  = "", Geo_Fencing  = "", SFFType  = "", SFDept = 0, DeptType  = "", OTFlg  = "", HOLocation  = "", HQID  = "", HQ_Name  = "", ERP_Code = "", HQCode = "", THrsPerm = "", Profile = "", ProfPath = "", imageCaptureNeed = 0, cameraFlip = 0, LocBasedCheckNeed = 0, mobile_check_in = 2, portalAccess = 2)
        sqLite?.deleteUserLoginData()
        _uiState.value = LoginSuccess(LoginResponseModel( success = true, data = listOf(userData), blockMsg = "No block message"))
        Log.d("ViewModelClass", "clearUiState() Method is Calling.... _uiState.value : ${_uiState.value}")
    }

    fun getCheckInVisibility(navController: NavController,context: Context,sfCode: String) = viewModelScope.launch {

        Log.d("MainViewModel", "getCheckInVisibility() Method is Calling....")


        when(val response = repository.getCheckInVisibility(context = context, sfCode = sfCode))
        {
            is Resource.Loading->{
                Log.d("MainViewModel", "qwerty : getCheckInVisibility API call is running")
            }
            is Resource.Success -> {
                response.let {
                    val data = response.data
                    if (data?.success == true) {
                        _checkInState.value = data.head.firstOrNull()?.flag
                        _checkInCount.value = data.head.firstOrNull()?.CheckInCount
                        checkInCount = data.head.firstOrNull()?.CheckInCount!!
                        _orgMode.value = data.head.firstOrNull()?.orgMode

                        Log.d("MainViewModel", "qwerty : getCheckInVisibility API call successful/ true...  ${data.head}")

                    } else {
                        Log.d("MainViewModel", "qwerty : getCheckInVisibility API call successful/ false")
                        return@launch
                    }
                }
            }
            is Resource.Error ->{
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
                Log.d("MainViewModel", "qwerty : getCheckInVisibility API call was not successful")
                return@launch
            }
        }
    }

    fun clearCheckInState()
    {
        _checkInState.value = 3
    }


    fun getLoginResponse(context: Context, res: String?): LoginResponseModel? =

        if (res.isNullOrEmpty()) {
            getLoginDetailsForHome(context = context)
        } else {
            Log.d("res json", "$res")
            Gson().fromJson(res, LoginResponseModel::class.java)
        }

    private fun getLoginDetailsForHome(context: Context): LoginResponseModel? {
        return Constant.getUserLoginDetailsForHome(context)
    }

}