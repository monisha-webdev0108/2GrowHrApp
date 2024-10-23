package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.getLoginDetails
import com.payroll.twogrowhr.viewModel.ChangePasswordView
import com.payroll.twogrowhr.viewModel.MainViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Change_password(navController: NavController, viewModel : MainViewModel, changepasswordViewModel:ChangePasswordView) {

    val isLoggedIn = remember { mutableStateOf(true) }
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Change Password",
            "HomeScreen"
        ) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    {
        ChangePassword_Screen(navController = navController, viewModel = viewModel, changepasswordViewModel = changepasswordViewModel)
    }
}

@Composable
fun ChangePassword_Screen(navController: NavController, viewModel : MainViewModel, changepasswordViewModel:ChangePasswordView) {
    var passwordOld by rememberSaveable { mutableStateOf("") }
    var passwordNew by rememberSaveable { mutableStateOf("") }
    var confirmPassword by remember {mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(true) }
    var oldPasswordVisible by remember { mutableStateOf(true) }
    var oldPasswordError by remember { mutableStateOf(false) }
    var newPasswordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var confirmBorderColor by remember { mutableStateOf(Color.Transparent) }
    var newBorderColor by remember { mutableStateOf(Color.Transparent) }
    var oldBorderColor by remember { mutableStateOf(Color.Transparent) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    // Fetch empID using ViewModel or other method
    val jsonObject = getLoginDetails()
    val empID = jsonObject?.getString("Sf_code") ?: ""
    val empPassword = jsonObject?.getString("Sf_Password") ?: ""
    val org = jsonObject?.getInt("org") ?: 0

    val containerColor = colorResource(id = R.color.white)
    val textStyle = TextFieldDefaults.colors(
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        cursorColor = Color.Black,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)

    ){
        Text(
            text = "Old Password", style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Box( modifier = Modifier.padding(bottom = 10.dp)
        ){
            TextField(
                value = passwordOld,
                onValueChange = {
                    passwordOld = it
                    oldPasswordError = false
                    oldBorderColor = if (it != empPassword ) {
                        Color.Transparent
                    } else {
                        Color.Transparent
                    }
                },
                placeholder = {
                    Text(
                        text = "Enter your old password",
                        color =colorResource(id = R.color.paraColor),
                        fontSize = 12.sp
                    )
                },
                visualTransformation = if (oldPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = textStyle,
                modifier = Modifier.fillMaxWidth(1f)
            )

            Icon(painter = if (oldPasswordVisible) painterResource(id = R.drawable.eye) else painterResource(id = R.drawable.password_cross),
                contentDescription ="password" ,
                tint = colorResource(id = R.color.divider),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .clickable { oldPasswordVisible = !oldPasswordVisible }
            )
        }
        val visibleOld =  (passwordOld.contains("\\s".toRegex()))
        if (visibleOld){
            Text(
                text = "Old Password did not allow whitespace",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (oldPasswordError){
            Text(
                text = "Current password is incorrect",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                )
            )
        }
        //new password
        Text(
            text = "New Password", style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Box( modifier = Modifier
            .padding(bottom = 10.dp)
            .border(
                width = 1.dp,
                color = newBorderColor,
                shape = RoundedCornerShape(4.dp)
            )
        ){
            TextField(
                value = passwordNew,
                onValueChange = {
                    passwordNew = it
                    newPasswordError = false
                    newBorderColor = if (it == passwordOld ) {
                        Color.Transparent
                    } else {
                        Color.Transparent
                    }
                },
                placeholder = {
                    Text(
                        text = "Enter your new password",
                        color =colorResource(id = R.color.paraColor),
                        fontSize = 12.sp
                    )

                },
                visualTransformation = if (newPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = textStyle,
                modifier = Modifier
                    .fillMaxWidth(1f)
            )

            Icon(
                painter = if (newPasswordVisible) painterResource(id = R.drawable.eye) else painterResource(id = R.drawable.password_cross),
                contentDescription ="password" ,
                tint = colorResource(id = R.color.divider),
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .clickable { newPasswordVisible = !newPasswordVisible }
            )
        }

        val visibleNew =  (passwordNew.contains("\\s".toRegex()))
        if (visibleNew){
            Text(
                text = "New Password did not allow whitespace",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (newPasswordError){

            Text(
                text = "New password must be different from current password",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                )
            )
        }
//      Repeat password
        Text(
            text = "Confirm Password", style = MaterialTheme.typography.titleMedium,
            color = colorResource(id = R.color.paraColor),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Box( modifier = Modifier
            .padding(bottom = 10.dp)
            .border(
                width = 1.dp,
                color = confirmBorderColor,
                shape = RoundedCornerShape(4.dp)
            )
        ){
            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = false
                    confirmBorderColor = if (it != passwordNew ) {
                        Color.Transparent
                    } else {
                        Color.Transparent
                    }
                },
                placeholder = {
                    Text(
                        text = "Enter your confirm password",
                        color =colorResource(id = R.color.paraColor),
                        fontSize = 12.sp
                    )

                },
                colors = textStyle,
                modifier = Modifier.fillMaxWidth(1f)
            )
        }

        if(confirmPassword.contains("\\s".toRegex())){
            Text(
                text = "Confirm Password did not allow whitespace",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (confirmPasswordError){

            Text(
                text = "New password does not match with confirm password",
                color = colorResource(id = R.color.red),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(onClick = {
            val oldPassword = passwordOld.lowercase(Locale.ROOT)
            val confirmPassword1 = confirmPassword.lowercase(Locale.ROOT)
            val newPassword = passwordNew.lowercase(Locale.ROOT)
            confirmPasswordError = false
            oldPasswordError = false
            newPasswordError = false
            confirmBorderColor = Color.Transparent
            oldBorderColor = Color.Transparent
            newBorderColor = Color.Transparent

            // Validation checks
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword1.isEmpty()) {
                if (oldPassword.isEmpty()) {
                    Constant.showToast(context, "Old Password should not be empty")
                }
                else if (newPassword.isEmpty()) {
                    Constant.showToast(context, "New Password should not be empty")
                }
                else if (confirmPassword1.isEmpty()) {
                    Constant.showToast(context, "Confirm Password should not be empty")
                }
                return@Button
            }

            if (newPassword != confirmPassword1 || oldPassword != empPassword.lowercase(Locale.ROOT) || oldPassword == newPassword ) {

                if(oldPassword != empPassword.lowercase(Locale.ROOT) ){
                    oldPasswordError = true
                    oldBorderColor = Color.Red
                }
                else if(oldPassword == newPassword){
                    newPasswordError = true
                    newBorderColor = Color.Red
                }
                else if(newPassword != confirmPassword1){
                    confirmPasswordError = true
                    confirmBorderColor = Color.Red
                }
                return@Button
            }

            if (newPassword.contains("\\s".toRegex())) {
                confirmPasswordError = true
                newBorderColor = Color.Red
                return@Button
            }

            if (oldPassword == empPassword.lowercase(Locale.ROOT)) {
                // Proceed with password change API call
                coroutineScope.launch {
                    changepasswordViewModel.getChangePasswordRegularizedDetails(
                        empId = empID,
                        id = org,
                        password = confirmPassword1,
                        context = context,
                    navController = navController).let {
                        SharedPreferenceManager.setLoggedIn(context, false)
                        SharedPreferenceManager.setCheckInOut(context, "")
                        Constant.clearVariables()
                        Constant.clearSQLiteData(context)
//                        viewModel.clearUiState()
                        viewModel.clearUiState1()
                        viewModel.clearCheckInState()
                    }
                }
            } else {
                Constant.showToast(context, "Old Password does not match")
            }
        },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 20.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(text = "Update Password",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.white)
            )
        }
    }
}


