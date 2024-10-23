package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Database.MyDatabaseHelper
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.findActivity
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.util.Resource
import com.payroll.twogrowhr.viewModel.MainViewModel
import com.payroll.twogrowhr.viewModel.ProfileViewModel
import java.net.URLEncoder


var loader = mutableStateOf(false)

typealias LoginCallback = (String, String) -> Unit

@SuppressLint("StaticFieldLeak")
var sqLite: MyDatabaseHelper? = null

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Login(
    isTopBarVisible: Boolean,
    onLogin: LoginCallback,
) {
//fun Login(navController: NavHostController, isTopBarVisible: Boolean) {
    if (isTopBarVisible) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(true) }

        // Create a FocusRequester for the password field
        val passwordFocusRequester = remember { FocusRequester() }

        val context = LocalContext.current

        BackPressHandler(onBackPressed = { context.findActivity()?.finish() })


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxHeight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.offset(x = (-23).dp)) {
                    Image(
                        painterResource(id = R.drawable.circle_one),
                        contentDescription = "circle_1",
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                    )
                }
                Column {
                    Image(
                        painterResource(id = R.drawable.circle_two),
                        contentDescription = "circle_1",
                        modifier = Modifier
                            .width(80.dp)
                            .height(180.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(35.dp)
                    .offset(y = (-140).dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painterResource(id = R.drawable.login),
                        contentDescription = "Login Image",
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .width(300.dp)
                            .height(300.dp)

                    )

                }
                Column(modifier = Modifier.offset(y = (-30).dp)) {
                    Text(
                        text = "Hey,",
                        color = colorResource(R.color.themeColor),
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    )
                    Text(
                        text = "Login Now!",
                        color = colorResource(R.color.themeColor),
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    )
                }

                Column(modifier = Modifier.offset(y = (-30).dp)) {
                    Box {
                        TextField(
                            value = username.value,
                            onValueChange = { username.value = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "User Name") },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = Color.Black,
//                                placeholderColor = colorResource(id = R.color.divider)
                                focusedBorderColor = colorResource(R.color.themeColor),
                                unfocusedBorderColor = colorResource(R.color.lightthemecolor),
                            ),
                            textStyle = TextStyle(
                                color = colorResource(R.color.black),
                                fontWeight = FontWeight.W500,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),

                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),

                            keyboardActions = KeyboardActions(
                                onNext = {
                                    passwordFocusRequester.requestFocus()
                                }
                            )
                        )
                        Icon(
                            painterResource(id = R.drawable.baseline_person_outline_24),
                            contentDescription = "users",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }

                    Box {
                        TextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            placeholder = { Text(text = "Password") },
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = Color.Black,
//                                placeholderColor = colorResource(id = R.color.divider)
                                focusedBorderColor = colorResource(R.color.themeColor),
                                unfocusedBorderColor = colorResource(R.color.lightthemecolor),
                            ),
                            textStyle = TextStyle(
                                color = colorResource(R.color.black),
                                fontWeight = FontWeight.W500,
                                fontFamily = FontFamily(Font(R.font.poppins_bold))
                            ),
                            visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,

                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),

                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Handle Done action, for example, perform login
                                    onLogin(username.value, password.value)
                                }
                            ),

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                                .focusRequester(passwordFocusRequester),

                            )
                        Icon(if (passwordVisible) painterResource(id = R.drawable.eye) else painterResource(
                            id = R.drawable.password_cross
                        ),
                            contentDescription = "password",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier
                                .clickable { passwordVisible = !passwordVisible }
                                .size(20.dp)
                                .align(Alignment.CenterEnd))
                    }
                }

                Button(
                    onClick = {
                        Log.d("Button Click", "Inside Button onClick")
                        onLogin(username.value, password.value)
                    },
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 20.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.white)
                    )
                }

                Column(modifier = Modifier.fillMaxWidth())
                {
                    Text(
//                        text = "Version ${VersionHelper.getVersionName(context)} ${VersionHelper.getVersionCode(context)}",//"Version T8",
                        text = "Version ${VersionHelper.getVersionName(context)}",//"Version T8",
//                        text = "Version T43",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 13.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)
                        ),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            if(loader.value)
            {
                circularProgression()
            }
        }
    }
}
//This Function is used to Fetch and Pass the data entered by the user to another Function for validation

fun onLoginClicked(
    username: String,
    password: String,
    context: Context,
    viewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
//    responseModel: State<Result<LoginResponseModel>?>,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
) {
    sqLite = MyDatabaseHelper(context)

    if (isInputDataValid(username, password, context)) {
        Log.d("Button Click", "Inside onLoginClicked")
        checkLogin(
            username.trim(),
            password,
            viewModel,
            profileViewModel,
//            responseModel,
            lifecycleOwner,
            navController,
            context
        )
    }else
    {
        Log.d("Button Click", "Inside onLoginClicked : return false")
    }
}

//This Function is used to validate the data entered by the user

fun isInputDataValid(cUsername: String, cPassword: String, context: Context): Boolean {

    Log.d("Button Click", "Inside IsInputDataValid")
    var result = false
    val username = cUsername.trim()
    val password = cPassword.trim()
    if (username.isEmpty() && password.isEmpty()) {
        Constant.showToast(context, "Enter Username and Password")
        result = false
    } else if (username.isEmpty()) {
        Log.d("Button Click", "No User Name")
        Constant.showToast(context, "Enter Username")
        result = false
    } else if (password.isEmpty()) {
        Log.d("Button Click", "No Password")
        Constant.showToast(context, "Enter Password")
        result = false
    }
/*    else if (!Constant.isNetworkAvailable(context)) {
        Constant.showToast(context, "Check Your Internet Connectivity")
        result = false
    }*/
    else if (username.isNotEmpty() && password.isNotEmpty()) {
        result = true
    }
    Log.d("ReturnResult", "ReturnResult: $result")
    return result
}

//This function is used to check the credentials enter by the user is valid or not
// and also get the response if the enter credential is valid
/*private fun callToast(context: Context, message: String) {
    Constant.showToast(context = context, message)
}*/

fun checkLogin(
    username: String,
    password: String,
    viewModel: MainViewModel,
    profileViewModel: ProfileViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    context: Context,
) {
/*        if (!Constant.isNetworkAvailable(context)) {
            Constant.showToast(context, "Check Your Internet Connectivity")
            return
        }*/
        var toastShown = false
        Log.d("LoginTextField", "Password entered: $password")
//        viewModel.getInitialResponse(username, password)
    viewModel.getInitialResponse1(context, username, password)

    viewModel.uiState1.observe(lifecycleOwner) { uiState ->
            when (uiState) {

                is Resource.Loading -> {
                    loader.value = true
                }
                is Resource.Success -> {

                    val mLoginDetails = uiState.data ?: return@observe

                    Log.d("mLoginDetails ", "mLoginDetails : $mLoginDetails")

                    sqLite?.deleteUserLoginData()
                    sqLite?.createLogin(result = mLoginDetails.toString())

                    val jsonObject2 = mLoginDetails.data.firstOrNull()

                    if (jsonObject2 != null)
                    {
                        val isAllowToLogin = jsonObject2.portalAccess

                        Log.d("isAllowToLogin ", "isAllowToLogin : $isAllowToLogin")

                        if (isAllowToLogin == 1)
                        {
                            loader.value = false
                            viewModel.getCheckInVisibility(navController,context,sfCode = jsonObject2.Sf_code)
                            viewModel.checkInState.observe(lifecycleOwner) {
                                val checkinflag = when (it) {
                                    1 -> "ckout"
                                    0 -> "ckin"
                                    else -> ""
                                }
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                            }

                            profileViewModel.getEmployeeDetails(navController, empID = jsonObject2.Sf_code, context = context)

                            SharedPreferenceManager.setLoggedIn(context, true)

                            val checkIn = SharedPreferenceManager.getCheckInOut(context)
                            Log.d("Login_checkIn", "from SharedPreferenceManager-----$checkIn")

                            Log.d("Before Navigation res", "$mLoginDetails")

                            val resEncoded = URLEncoder.encode(mLoginDetails.toString(), "UTF-8")


                            navController.navigate("${Screen.HomeScreen.route}?res=$resEncoded")
                            Log.d("After Navigation", "After Navigation")

                        }
                        else
                        {
                            if (jsonObject2.portalAccess == 0 && !toastShown)
                            {
                                viewModel.clearUiState1()
                                Constant.showToast(context, "Contact Admin...! You Don't have a Login Access")
                                loader.value = false
                                viewModel.resetLoginState()
                                toastShown = true // Set the flag to prevent showing the toast again
                            }
                        }
                    }
                }

                is Resource.Error -> {

                    if (!toastShown)
                    {
                        loader.value = false

                        if(uiState.message == "Unauthorized" || uiState.message.isNullOrEmpty())
                        {
                            Constant.showToast(context, "Incorrect Username or Password")
                        }
                        else
                        {
                            Constant.showToast(context, uiState.message)
                        }
                        viewModel.resetLoginState()
                        toastShown = true // Set the flag to prevent showing the toast again
                    }

                }

                else -> {}


/*

                is LoginSuccess -> {
                    val mLoginDetails = uiState.loginResponse
                    Log.d("mLoginDetails ", "mLoginDetails : $mLoginDetails")
                    if (!mLoginDetails.success)
                    {
                        if (!toastShown) {
                            Constant.showToast(context,"Incorrect Username or Password" )
                            viewModel.clearUiState()
                            viewModel.clearUiState1()
                            toastShown = true // Set the flag to prevent showing the toast again
                        }
                        return@observe
                    }
                    else
                    {
                        sqLite?.deleteUserLoginData()
                        sqLite?.createLogin(result = mLoginDetails.toString())
                        val jsonObject2 = mLoginDetails.data.firstOrNull()
                        if (jsonObject2 != null)
                        {
                            val isAllowToLogin = jsonObject2.portalAccess

                            Log.d("isAllowToLogin ", "isAllowToLogin : $isAllowToLogin")

                            if (isAllowToLogin == 1)
                            {

                                loader.value = false


                                viewModel.getCheckInVisibility(sfCode = jsonObject2.Sf_code)
                                viewModel.checkInState.observe(lifecycleOwner) {
                                    val checkinflag = when (it) {
                                        1 -> "ckout"
                                        0 -> "ckin"
                                        else -> ""
                                    }
                                    SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                }

                                profileViewModel.getEmployeeDetails(empID = jsonObject2.Sf_code, context = context)

                                SharedPreferenceManager.setLoggedIn(context, true)

                                val checkIn = SharedPreferenceManager.getCheckInOut(context)
                                Log.d("Login_checkIn", "from SharedPreferenceManager-----$checkIn")

                                navController.navigate("${Screen.HomeScreen.route}?res=$mLoginDetails")
                                Log.d("After Navigation", "After Navigation")

                            }
                            else
                            {
                                if (jsonObject2.portalAccess == 0 && !toastShown)
                                {
                                    viewModel.clearUiState()
                                    viewModel.clearUiState1()
                                    Constant.showToast(context, "Contact Admin...! You Don't have a Login Access")
                                    toastShown = true // Set the flag to prevent showing the toast again
                                }
                            }
                        }
                    }

                }

                is LoginError -> {
                    if (!toastShown)
                    {
                        Constant.showToast(context, "Please try again!!!")
                        toastShown = true // Set the flag to prevent showing the toast again
                    }
                }
*/

            }
        }

}





