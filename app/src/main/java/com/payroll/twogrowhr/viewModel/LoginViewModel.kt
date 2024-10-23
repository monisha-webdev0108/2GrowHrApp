package com.payroll.twogrowhr.viewModel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.payroll.twogrowhr.Model.ResponseModel.LoginResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.UserData
import com.payroll.twogrowhr.components.Screen

class LoginViewModel(
    private val navController: NavController,
    private val data: LoginResponseModel?
) : ViewModel() {
    fun navigateToHome() {
        navController.navigate(Screen.HomeScreen.route)
    }

    fun getUSerData(): UserData? {
        return data?.data?.firstOrNull()
    }

    fun navigateToShiftSelection() {
        navController.navigate("ShiftSelection") {
            popUpTo(
                "ShiftSelection"
            )
        }
    }

    fun navigateToCurrentLocation() {
        navController.navigate("Currentlocation") {
            popUpTo(
                "Currentlocation"
            )
        }
    }
}