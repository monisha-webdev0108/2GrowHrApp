package com.payroll.twogrowhr.loginState

import com.payroll.twogrowhr.Model.ResponseModel.LoginResponseModel

sealed class LoginState
data class LoginSuccess(
    val loginResponse: LoginResponseModel
) : LoginState()
object LoginError : LoginState()
