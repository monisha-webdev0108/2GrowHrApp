package com.payroll.twogrowhr.Model.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.MainCardModel
import com.payroll.twogrowhr.Model.ResponseModel.EmpShiftData
import com.payroll.twogrowhr.Model.ResponseModel.HolidayListData
import com.payroll.twogrowhr.Model.ResponseModel.ShiftData
import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.DrawerContent
import com.payroll.twogrowhr.components.TopBar
import com.payroll.twogrowhr.components.checkInViewModel
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.CheckInViewModel
import com.payroll.twogrowhr.viewModel.HolidayViewModel
import com.payroll.twogrowhr.viewModel.HomeWishesViewModel
import com.payroll.twogrowhr.viewModel.LoginViewModel
import com.payroll.twogrowhr.viewModel.UserDataViewModel
import com.payroll.twogrowhr.viewModel.MainViewModel
import com.payroll.twogrowhr.viewModel.OnDutyViewModel
import com.payroll.twogrowhr.viewModel.WorkFromHomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.payroll.twogrowhr.Model.ResponseModel.TodayAttendanceLogData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.components.startRedirectTimer
import com.payroll.twogrowhr.findActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

var lats = 0.0
var longs = 0.0
lateinit var locationProvider: FusedLocationProviderClient
var checkinflag = ""
var checkInCount = -1
lateinit var cameraExecutor: ExecutorService
var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
var round = 1
lateinit var loginViewModel: LoginViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel,
    loginViewModel1: LoginViewModel,
    userViewModel1: UserDataViewModel,
    wishesViewModel:HomeWishesViewModel,
    checkInViewModel1: CheckInViewModel,
    holidayViewModel1 : HolidayViewModel,
    onDutyViewModel1: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    loginViewModel = loginViewModel1
    userViewModel = userViewModel1
    checkInViewModel = checkInViewModel1

    val context = LocalContext.current

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBar(navController = navController, title = userViewModel.getDivisionName(), drawerState = drawerState) },
        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        bottomBarContent = { BottomNav(navController) },
        onBack = { context.findActivity()?.finish() }
    )
    {
        Home(
            navController = navController,
            viewModel = viewModel,
            checkInViewModel = checkInViewModel1,
            userViewModel = userViewModel,
            wishesViewModel=wishesViewModel,
            holidayViewModel = holidayViewModel1,
            onDutyViewModel = onDutyViewModel1,
            wfhViewModel = wfhViewModel
        )
    }
}

@SuppressLint(
    "SuspiciousIndentation", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState", "AutoboxingStateCreation"
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    navController: NavController,
    viewModel: MainViewModel,
    checkInViewModel: CheckInViewModel,
    userViewModel: UserDataViewModel,
    wishesViewModel:HomeWishesViewModel,
    holidayViewModel: HolidayViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel
) {

    Log.d("HOMEScreen", "Recomposition")

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current





    viewModel.checkInState.observe(lifecycleOwner) {
        checkinflag = when (it) {
            1 -> "ckout"
            0 -> "ckin"
            else -> " "
        }
        SharedPreferenceManager.setCheckInOut(context, checkinflag)
        loading.value = false
    }
    HomeDetails(
        userViewModel = userViewModel,
        wishesViewModel=wishesViewModel,
        checkInViewModel = checkInViewModel,
        onDutyViewModel = onDutyViewModel,
        wfhViewModel = wfhViewModel,
        holidayViewModel = holidayViewModel,
        navController = navController,
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeDetails(
    userViewModel: UserDataViewModel,
    wishesViewModel: HomeWishesViewModel,
    checkInViewModel: CheckInViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel,
    holidayViewModel: HolidayViewModel,
    navController: NavController
) {
    val empID = userViewModel.getSFCode()
    val shiftListDetails = checkInViewModel.shiftList.collectAsState()


    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(3000)
            refreshing = false
        }
    }


    if (loading.value) {
        circularProgression()
        startRedirectTimer(navController, LocalContext.current,"HomeScreen")
    }
    runCatching {

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = {
                refreshing = true
                navController.navigate("HomeScreen")
            },
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ShiftTime(
                    navController = navController,
                    checkInViewModel = checkInViewModel,
                    onDutyViewModel = onDutyViewModel,
                    wfhViewModel = wfhViewModel,
                    shiftListDetails = shiftListDetails.value,
                    empID = empID
                )
                TodayTimeLog(navController = navController, checkInViewModel = checkInViewModel)
                ShowTime(wfhViewModel = wfhViewModel, onDutyViewModel = onDutyViewModel, navController= navController)
                ShowWorkingRemotely()
                ShowLeaveODRWCards(navController = navController, wishesViewModel= wishesViewModel)
                ShowHoliday(navController = navController, holidayViewModel=holidayViewModel)
                ShowWeddingBirthWorkCards(navController = navController,wishesViewModel=wishesViewModel)
            }
        }

    }.onFailure { exception ->
        // Handle the exception
        Log.e("From App Navigation", "Error during navigation: ${exception.message}", exception)
    }
}

//Home

@SuppressLint("NewApi")
@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShiftTime(
    navController: NavController,
    checkInViewModel: CheckInViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel,
    shiftListDetails: List<ShiftData>,
    empID: String
) {

    runCatching {
        var shiftDetails: MutableState<List<EmpShiftData>>


        checkInViewModel.empShiftList.collectAsState().also {
            shiftDetails = it as MutableState<List<EmpShiftData>>
        }

        val shiftId : Int? = shiftDetails.value.firstOrNull()?.shiftId

        val shiftName : String? = shiftDetails.value.firstOrNull()?.shiftName
        val shiftStartTimeString  = shiftDetails.value.firstOrNull()?.shiftStartTime
        val shiftEndTimeString  = shiftDetails.value.firstOrNull()?.shiftEndTime

        var startTimeString  = ""

        var endTimeString  = ""

        if(!shiftStartTimeString.isNullOrEmpty())
        {
            val shiftStartTimeParts = shiftStartTimeString.toString().split("T")  // Split the string at 'T' to get date and time parts

            val startTimePart = shiftStartTimeParts.getOrNull(1)

            startTimeString = startTimePart?.substring(0, 8) ?: ""
        }

        if(!shiftEndTimeString.isNullOrEmpty())
        {
            val shiftEndTimeParts = shiftEndTimeString.toString().split("T")

            val endTimePart = shiftEndTimeParts.getOrNull(1)

            endTimeString = endTimePart?.substring(0, 8) ?: ""
        }

        val context = LocalContext.current

        var imageUri by remember { mutableStateOf<Uri?>(null) }

        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            if (permissionsMap.isNotEmpty()) {

                when
                {
                    permissionsMap.values.all { it } -> { Constant.showToast(context, "Permission Granted") }
                    permissionsMap.values.none { it } -> {
                        Constant.showToast(context, "Permissions Denied, Please give the permission to access..!")
                        redirectToAppPermissionsScreen(context)
                    }
                    else -> {

                        val cameraPermissions: Array<String> = if (Build.VERSION.SDK_INT >= 33) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            {
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                            }
                            else
                            {
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                        else
                        {
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }

                        val locationPermission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

                        val locationPermissionsGranted = locationPermission.all {
                            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
                        }

                        val cameraPermissionsGranted = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE  )
                        {

                            when
                            {
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> true

                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> true

                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> false

                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> false

                                else -> false

                            }
                        }
                        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
                        {
                            when
                            {
                                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> true

                                else -> false

                            }
                        }
                        else
                        {
                            checkInViewModel.arePermissionsGranted(context, cameraPermissions)
                        }


                        if(userViewModel.isImageCaptureNeed() && (userViewModel.hasGeoFencing() || userViewModel.hasLocationCheckNeed()))
                        {
                            if(!locationPermissionsGranted || !cameraPermissionsGranted)
                            {
                                Constant.showToast(context, "Please give the necessary access")
                                redirectToAppPermissionsScreen(context)
                            }
                        }
                        else if(userViewModel.isImageCaptureNeed())
                        {
                            if(!cameraPermissionsGranted)
                            {
                                Constant.showToast(context, "Please give the necessary access")
                                redirectToAppPermissionsScreen(context)
                            }
                        }
                        else
                        {
                            Constant.showToast(context, "Please give the necessary access")
                            redirectToAppPermissionsScreen(context)
                        }

                    }
                }



/*                val areGranted = permissionsMap.values.all { it }
                if (areGranted) {
                    Constant.showToast(context, "Permission Granted")
//                permissionsToRequest.clear()
                } else {
                    Constant.showToast(context, "Permission Denied")
                }*/
            }
        }


        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f))
            {
                if(shiftId != 0)
                {
                    //SHIFT NAME

                    if (shiftName != null) {
                        if (shiftName.length > 16)
                        {
                            PlainTooltipBox(
                                tooltip = {
                                    Text(
                                        text = shiftName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }
                            ) {
                                Text(
                                    text = shiftName.take(16) +  "..." ,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    ),
                                    modifier = Modifier.tooltipTrigger()
                                )
                            }
                        } else {
                            Text(
                                text = shiftName.ifEmpty { "" },
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                ),
                            )
                        }
                    }

                    //SHIFT TIME

                    if(startTimeString.isNotEmpty() || endTimeString.isNotEmpty() )
                    {
                        Text(
                            text = "$startTimeString - $endTimeString",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)
                            ),
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {

                val checkIn = SharedPreferenceManager.getCheckInOut(context)
                Log.d("Home1_checkIn", "from SharedPreferenceManager-----$checkIn")

                val color =
                    when (checkIn) {
                        "ckout" -> colorResource(id = R.color.red)
                        "ckin" -> colorResource(
                            id = R.color.green
                        )
                        else -> colorResource(id = R.color.backgroundColor)
                    }
                val buttonText1 = if (checkIn == "ckout") "Check Out" else if (checkIn == "ckin") "Check In" else ""

                var buttonNeedFlag = 0 // 1 - CHECK IN BUTTON NEED , 2 - NOT NEED
                var buttonNeedFor = 0 // 1 - WFH, 2-OnDuty, 3-NormalCheckIn

                //Date Conversion for check the date is equal to current date or not


                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format the selectedDate2 to "yyyy-MM-dd"
                val currentDateFormatted = dateFormat.format(Date(System.currentTimeMillis()))
                val currentDay = LocalDate.parse(currentDateFormatted)

                val defaultDate = LocalDateTime.parse("1900-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME )
                val defaultDateFormatted = defaultDate.toLocalDate()
                val defaultDay = LocalDate.parse(defaultDateFormatted.toString())

                Log.d("Home", "currentDay-----$currentDay")


                val applyDateWFH = LocalDateTime.parse(wfhViewModel.isCheckInAllowedForApplied.value, DateTimeFormatter.ISO_DATE_TIME )
                val applyDateWFH1 = applyDateWFH.toLocalDate()
                val checkInApplyDayWFH = LocalDate.parse(applyDateWFH1.toString())


                val approveDateWFH = LocalDateTime.parse(wfhViewModel.isCheckInAllowedOnlyApproved.value, DateTimeFormatter.ISO_DATE_TIME )
                val approveDateWFH1 = approveDateWFH.toLocalDate()
                val checkInApproveDayWFH = LocalDate.parse(approveDateWFH1.toString())


                val applyDateOD = LocalDateTime.parse(onDutyViewModel.isCheckInAllowedForApplied.value, DateTimeFormatter.ISO_DATE_TIME )
                val applyDateOD1 = applyDateOD.toLocalDate()
                val checkInApplyDayOD = LocalDate.parse(applyDateOD1.toString())


                val approveDateOD = LocalDateTime.parse(onDutyViewModel.isCheckInAllowedOnlyApproved.value, DateTimeFormatter.ISO_DATE_TIME )
                val approveDateOD1 = approveDateOD.toLocalDate()
                val checkInApproveDayOD = LocalDate.parse(approveDateOD1.toString())

                var buttonClicked by remember { mutableStateOf(false) }


                //FOR NORMAL CHECK IN


                fun checkInOut()
                {
                    if (userViewModel.isMobileCheckIn())
                    {
                        buttonNeedFor = 3

                        Log.d("Home", "Normal check In")

                        if(!checkIn.isNullOrEmpty())
                        {
                            buttonNeedFlag = 1
                        }
                    }
                }


                //FOR CHECK ON DUTY

                fun checkOnDuty()
                {
                    buttonNeedFor = 2
                    val conditionNumber: Int

                    if(currentDay.isEqual(checkInApplyDayOD))// For OD Applied Day
                    {
                        if(onDutyViewModel.isOnDutyCheckInApplied.value == 1) // Applied setup button is enabled
                        {
                            buttonNeedFlag = 1
                            conditionNumber = 1
                        }
                        else if (onDutyViewModel.isOnDutyCheckInApproved.value == 1 && checkInApproveDayOD.isEqual(currentDay)) // Approved - Applied setup button is enabled
                        {
                            buttonNeedFlag = 1
                            conditionNumber = 2
                        }
                        else if(onDutyViewModel.isOnDutyCheckInApproved.value == 1 && checkInApproveDayOD.isEqual(defaultDay)) // In Approval - Approved setup button is enabled
                        {
                            buttonNeedFlag = 0
                            conditionNumber = 3
                        }
                        else // Applied & Approved setup button is not enabled
                        {
                            buttonNeedFlag = 0
                            conditionNumber = 4
//                            checkInOut()
                        }
                        Log.d("Home", "OnDuty - condition $conditionNumber")
                    }
                    else // For OD NOT Applied Day
                    {
                        checkInOut()
                    }
                }

                //FOR CHECK WORK FROM HOME

                fun checkRemote()
                {
                    buttonNeedFor = 1
                    val conditionNumber: Int

                    if(currentDay.isEqual(checkInApplyDayWFH))// For WFH Applied Day
                    {
                        if(wfhViewModel.isWFHCheckInApplied.value == 1) // Applied setup button is enabled
                        {
                            buttonNeedFlag = 1
                            conditionNumber = 1
                        }
                        else if (wfhViewModel.isWFHCheckInApproved.value == 1 && checkInApproveDayWFH.isEqual(currentDay)) // Approved - Applied setup button is enabled
                        {
                            buttonNeedFlag = 1
                            conditionNumber = 2
                        }
                        else if(wfhViewModel.isWFHCheckInApproved.value == 1 && checkInApproveDayWFH.isEqual(defaultDay)) // In Approval - Approved setup button is enabled
                        {
                            buttonNeedFlag = 0
                            conditionNumber = 3
                        }
                        else // applied and approved button is disabled
                        {

                            // No need to check on duty applied - either WFH or OD is applied per day

                            buttonNeedFlag = 0
                            conditionNumber = 4
//                            checkInOut()
                        }
                        Log.d("Home", "Remote - condition $conditionNumber")

                    }
                    else
                    {
                        if(onDutyViewModel.allowOnDuty.value == 1)
                        {
                            checkOnDuty()
                        }
                        else
                        {
                            checkInOut()
                        }
                    }
                }

                //Check In button


                if(wfhViewModel.allowWFH.value == 1) //Priority 1 - WORK FROM HOME
                {
                    checkRemote()
                }
                else if(onDutyViewModel.allowOnDuty.value == 1) //Priority 2 - ON DUTY
                {
                    checkOnDuty()
                }
                else
                {
                    checkInOut()
                }



                //If Button Needed

                if(buttonNeedFlag == 1)
                {

                    if (!buttonClicked) {

                        Button(
                            onClick = {

/*

                                    checkInViewModel.clearValues()
                                    if (!Constant.isNetworkAvailable(context)) {
                                        Constant.showToast(context, "Please check your network connection")
                                        navController.navigate("${Screen.Network.route}/HomeScreen")
                                    }
                                    else{

                                        if(buttonNeedFor == 1) // FOR REMOTE CHECK IN
                                        {

                                            val granted = checkAndRequestPermissionsForLocation(
                                                context = context,
                                                launcher = launcherMultiplePermissions
                                            )
                                            if (granted) {

                                                doCheckInRemote(context = context, navController = navController)

                                            }

                                        }
                                        else if(buttonNeedFor == 2) // FOR ON DUTY CHECK IN
                                        {
                                            doCheckInOnDuty(
                                                context = context,
                                                navController = navController
                                            )

                                        }
                                        else
                                        {
                                            if (userViewModel.hasShiftSelection()) {
                                                checkInViewModel.getShiftDetails1(context = context, navController = navController, empID)
                                            }

                                            if(buttonNeedFor == 3) // FOR MOBILE CHECK IN NEED
                                            {
                                                val granted = checkAndRequestPermissions(
                                                    context = context,
                                                    launcher = launcherMultiplePermissions
                                                )
                                                if (granted) {
                                                    doCheckIn(
                                                        context = context,
                                                        navController = navController,
                                                        shiftListDetails = shiftListDetails,
                                                        empID = empID
                                                    )
                                                }
                                            }
                                        }
                                    }

*/



                                if (!buttonClicked) {

                                    checkInViewModel.clearValues()
                                    if (!Constant.isNetworkAvailable(context)) {
                                        Constant.showToast(context, "Please check your network connection")
                                        navController.navigate("${Screen.Network.route}/HomeScreen")
                                    }
                                    else{

                                        if(buttonNeedFor == 1) // FOR REMOTE CHECK IN
                                        {

                                            val granted = checkAndRequestPermissionsForLocation(
                                                context = context,
                                                launcher = launcherMultiplePermissions
                                            )
                                            if (granted)
                                            {

                                                if(isMockLocation.value)
                                                {
                                                    navController.navigate("fakeGPS")
                                                }
                                                else if(!isTimeZoneAutomaticEnabled.value)
                                                {
                                                    navController.navigate("fakeTime")
                                                }
                                                else
                                                {
                                                    doCheckInRemote(context = context, navController = navController)
                                                }

                                            }


                                        }
                                        else if(buttonNeedFor == 2) // FOR ON DUTY CHECK IN
                                        {

                                            if(!isTimeZoneAutomaticEnabled.value)
                                            {
                                                navController.navigate("fakeTime")
                                            }
                                            else
                                            {
                                                doCheckInOnDuty(
                                                    context = context,
                                                    navController = navController
                                                )
                                            }

                                        }
                                        else
                                        {
                                            if (userViewModel.hasShiftSelection()) {
                                                checkInViewModel.getShiftDetails1(context = context, navController = navController, empID)
                                            }

                                            if(buttonNeedFor == 3) // FOR MOBILE CHECK IN NEED
                                            {
                                                val granted = checkAndRequestPermissions(
                                                    context = context,
                                                    launcher = launcherMultiplePermissions
                                                )
                                                if (granted)
                                                {

                                                    if(userViewModel.hasGeoFencing() || userViewModel.hasLocationCheckNeed())
                                                    {

/*                                                        CoroutineScope(Dispatchers.Main).launch {
                                                            isFakeGPSDetected(context, navController)
                                                            delay(1000)
                                                        }*/

                                                        if(isMockLocation.value)
                                                        {
                                                            navController.navigate("fakeGPS")
                                                        }
                                                        else if(!isTimeZoneAutomaticEnabled.value)
                                                        {
                                                            navController.navigate("fakeTime")
                                                        }
                                                        else
                                                        {

                                                            doCheckIn(
                                                                context = context,
                                                                navController = navController,
                                                                shiftListDetails = shiftListDetails,
                                                                empID = empID
                                                            )

                                                        }
                                                    }
                                                    else
                                                    {

                                                        if(!isTimeZoneAutomaticEnabled.value)
                                                        {
                                                            navController.navigate("fakeTime")
                                                        }
                                                        else
                                                        {
                                                            doCheckIn(
                                                                context = context,
                                                                navController = navController,
                                                                shiftListDetails = shiftListDetails,
                                                                empID = empID
                                                            )
                                                        }

                                                    }


                                                }

                                            }
                                        }
                                    }

                                    buttonClicked = true
//                                        loading.value = true

                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(2000)
                                        buttonClicked = false
                                    }

                                }

                            },
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = buttonText1,
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                    }


                }


            }

            if (shouldShowCamera.value) {

                val uri = ComposeFileProvider.getImageUri(context)
                imageUri = uri
                navController.navigate("CameraScreen")
                shouldShowCamera.value = false

            }
            cameraExecutor = Executors.newSingleThreadExecutor()

        }



    }.onFailure { exception ->
        // Handle the exception
        Log.e("From App Navigation", "Error during navigation: ${exception.message}", exception)
    }


}


private fun doCheckInRemote(
    context: Context,
    navController: NavController
) {

    val coroutineScope = CoroutineScope(Dispatchers.Main)

    coroutineScope.launch {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        try {
            if (!isLocationEnabled)
            {
                Constant.showToast(context, "Please turn on the location")
                Log.d("Home", "Location services not enabled. Launching settings activity.")

                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                try
                {
                    context.startActivity(intent)
                    Log.d("Home", "Settings activity launched successfully.")
                }
                catch (e: Exception)
                {
                    Log.e("Home", "Error launching settings activity: ${e.message}")
                }
                checkInViewModel.latLong(context, navController)
            }
            else
            {
                loading.value = true

                Log.d("Home... ", "Going to location screen")
                checkInViewModel.latLong(context, navController)
                Constant.showToast(context = context, "Fetching Location")
                navController.navigate("CheckInLocation") { popUpTo("CheckInLocation")
                { inclusive = true } }


            }
        }
        catch (e: Exception) {
            Log.e("CheckInViewModel...", "Error during location check: ${e.message}")
        }
    }


}


private fun doCheckInOnDuty(
    context: Context,
    navController: NavController
){


    if (checkinflag == "ckin")
    {
        Log.d("Home... ", "Check In OnDuty --> OnDuty")

        loading.value = true

        checkInViewModel.submitCheckin3(context,navController)
    }
    else
    {
        Log.d("Home", "Check Out OnDuty --> OnDuty")

        loading.value = true

        checkInViewModel.submitCheckout(context, navController)
    }

}

private fun doCheckIn(
    context: Context,
    navController: NavController,
    shiftListDetails: List<ShiftData>,
    empID: String
) {

    val coroutineScope = CoroutineScope(Dispatchers.Main) // or Dispatchers.IO, depending on your needs

    coroutineScope.launch {
        if (checkinflag == "ckin")
        {

            coroutineScope.launch {
                if (userViewModel.hasGeoFencing())
                {
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isLocationEnabled =
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                            LocationManager.NETWORK_PROVIDER
                        )

                    try {
                        if (!isLocationEnabled)
                        {
                            Constant.showToast(context, "Please turn on the location")
                            Log.d("Home", "Location services not enabled. Launching settings activity.")

                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                            try
                            {
                                context.startActivity(intent)
                                Log.d("Home", "Settings activity launched successfully.")
                            }
                            catch (e: Exception)
                            {
                                Log.e("Home", "Error launching settings activity: ${e.message}")
                            }
                            checkInViewModel.latLong(context, navController)
                        }
                        else
                        {

                            checkInViewModel.latLong(context, navController)//for getting current lat long..

                            checkInViewModel.getGeoDetails1(context, navController, empID) { geoDataList ->

                                Log.d("Home_checkIn", "from geoDataList-----${geoDataList}")

                                if (geoDataList.isNullOrEmpty())
                                {
                                    Constant.showToast(context, "Fencing enabled but no bounds found...")
                                }
                                else
                                {
                                    val fenceDistance = checkInViewModel.checkDistance(geoDataList)
                                    if (fenceDistance > 0)
                                    {

                                        Log.d("shift check In", "userViewModel.hasShiftSelection()/checkInCount : $userViewModel.hasShiftSelection()/$checkInCount")

                                        if (userViewModel.hasShiftSelection() && checkInCount == 0)
                                        {
                                            if (shiftListDetails.isEmpty())
                                            {
                                                Constant.showToast(context, "Shift Enabled but no shift mapped...")
                                            }
                                            else
                                            {
                                                Log.d("Home... ", "Going to shift screen --> geofencing")
                                                navController.navigate("ShiftSelection")
                                                {
                                                    popUpTo("ShiftSelection")
                                                }
                                            }
                                        }
                                        else
                                        {
                                            if (userViewModel.isImageCaptureNeed())
                                            {
                                                Log.d("Home... ", "Going to selfie screen --> geofencing")
                                                shouldShowCamera.value = true
                                            }
                                            else if (userViewModel.hasLocationCheckNeed())
                                            {
                                                Log.d("Home... ", "Going to location screen --> geofencing")
                                                checkInViewModel.latLong(context, navController)
                                                Constant.showToast(context = context, "Fetching Location")
                                                navController.navigate("Currentlocation")
                                                {
                                                    popUpTo("Currentlocation"){ inclusive = true }
                                                }
                                            }
                                            else
                                            {
                                                Log.d("Home... ", "nothing is enabled --> geofencing")
                                                loading.value = true
                                                checkInViewModel.submitCheckin3(context,navController)

//                                        checkInViewModel.submitCheckin3(context, lifecycleOwner, navController, viewModel)
                                            }
                                        }
                                    }
                                    else if (lats == 0.0) {
                                        Constant.showToast(context, "Location not Fetched,Please Try Again after 10 seconds")
                                    } else {
                                        Constant.showToast(context, "You are out of bounds from the headquarters or Working place")
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        Log.e("Location", "Error during location check: ${e.message}")
                    }

                }
                else
                {
                    Log.d("shift check In", "userViewModel.hasShiftSelection()/checkInCount : $userViewModel.hasShiftSelection()/$checkInCount")

                    if (userViewModel.hasShiftSelection() && checkInCount == 0)
                    {
                        Log.d("Home... ", "shift is enabled no geofencing")
                        if (shiftListDetails.isEmpty())
                        {
                            Constant.showToast(context, "Shift Enabled but no shift mapped...")
                        }
                        else
                        {
                            Log.d("Home... ", "Going to shift screen no geofencing")
                            navController.navigate("ShiftSelection") { popUpTo("ShiftSelection") }
                        }
                    }
                    else
                    {
                        if (userViewModel.isImageCaptureNeed())
                        {
                            Log.d("Home... ", "selfie is enabled no geofencing")
                            shouldShowCamera.value = true

                        }
                        else if (userViewModel.hasLocationCheckNeed())
                        {

                            coroutineScope.launch {
                                val isLocationEnabled = withContext(Dispatchers.IO) {
                                    val locationManager =
                                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                }

                                try {
                                    if (!isLocationEnabled)
                                    {
                                        Constant.showToast(context, "Please turn on the location")
                                        Log.d("Location", "Location services not enabled. Launching settings activity.")

                                        // Launch the location settings activity
                                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                        try
                                        {
                                            context.startActivity(intent)
                                            Log.d("Location", "Settings activity launched successfully.")
                                        }
                                        catch (e: Exception)
                                        {
                                            Log.e("Location", "Error launching settings activity: ${e.message}")
                                        }
                                        checkInViewModel.latLong(context, navController)
                                    }
                                    else
                                    {
                                        checkInViewModel.latLong(context, navController)//for getting current lat long..
                                        Log.d("Home... ", "location is enabled no geofencing")
                                        Constant.showToast(context = context, "Fetching Location")
                                        navController.navigate("Currentlocation")
                                        {
                                            popUpTo("Currentlocation"){ inclusive = true }
                                        }
                                        // showload = false
                                    }
                                }
                                catch (e: Exception)
                                {
                                    Log.e("Location", "Error during location check: ${e.message}")
                                    // Handle the exception appropriately
                                }

                            }


                        }
                        else
                        {
                            Log.d("Home... ", "nothing is enabled no geofencing")

                            loading.value = true
                            checkInViewModel.submitCheckin3(context,navController)

//                    checkInViewModel.submitCheckin3(context, lifecycleOwner, navController, viewModel)
                        }
                    }
                }
            }

        }
        else
        {
            coroutineScope.launch {

                if (userViewModel.hasGeoFencing())
                {

                    coroutineScope.launch {
                        val isLocationEnabled = withContext(Dispatchers.IO) {
                            val locationManager =
                                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        }


                        // Check if location services are enabled
//                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                        try {
                            if (!isLocationEnabled) {
                                Constant.showToast(context, "Please turn on the location")
                                // Log to check if this block is executed
                                Log.d("Location", "Location services not enabled. Launching settings activity.")

                                // Launch the location settings activity
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                try
                                {
                                    context.startActivity(intent)
                                    Log.d("Location", "Settings activity launched successfully.")
                                }
                                catch (e: Exception)
                                {
                                    Log.e("Location", "Error launching settings activity: ${e.message}")
                                }
                                checkInViewModel.latLong(context, navController)
                            }
                            else
                            {
                                checkInViewModel.latLong(context, navController)//for getting current lat long..
                                checkInViewModel.getGeoDetails1(context, navController, empID) { geoDataList ->

                                    Log.d("Home_checkOut", "from geoDataList-----$geoDataList")

                                    if (geoDataList.isNullOrEmpty())
                                    {
                                        Constant.showToast(context, "Fencing enabled but no bounds found")
                                    }
                                    else
                                    {
                                        val fencedCheckOut = checkInViewModel.checkDistance(geoDataList)

                                        if (fencedCheckOut > 0)
                                        {
                                            if (userViewModel.isImageCaptureNeed())
                                            {
                                                Log.d("Home_checkOut", "selfie is enabled --> geo fencing")
                                                shouldShowCamera.value = true
                                            }
                                            else if (userViewModel.hasLocationCheckNeed())
                                            {
                                                checkInViewModel.latLong(context, navController)
                                                Log.d("Home_checkOut", "location is enabled --> geo fencing")
                                                navController.navigate("Currentlocation")
                                                {
                                                    popUpTo("Currentlocation"){ inclusive = true }
                                                }
                                            }
                                            else {
                                                Log.d("Home_checkOut", "nothing is enabled --> geo fencing")
                                                loading.value = true

                                                checkInViewModel.submitCheckout(context, navController)

//                                    checkInViewModel.submitCheckout(context, lifecycleOwner, navController, viewModel)
                                            }
                                        }
                                        else if (lats == 0.0)
                                        {
                                            Constant.showToast(context, "Location not Fetched,Please Try Again")
                                        }
                                        else
                                        {
                                            Constant.showToast(context, "You are out of bounds from the headquarters or Working place")
                                        }
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            Log.e("Location", "Error during location check: ${e.message}")
                        }


                    }
                }
                else
                {

                    coroutineScope.launch {

                        if (userViewModel.isImageCaptureNeed())
                        {
                            Log.d("Home_checkOut", "selfie is enabled no geo fencing")
                            shouldShowCamera.value = true
                        }
                        else if (userViewModel.hasLocationCheckNeed())
                        {

                            // Move location services check to background
                            coroutineScope.launch {
                                val isLocationEnabled = withContext(Dispatchers.IO) {
                                    val locationManager =
                                        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                    locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                }


                                // Check if location services are enabled
//                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                                try {
                                    if (!isLocationEnabled)
                                    {
                                        Constant.showToast(context, "Please turn on the location")
                                        // Log to check if this block is executed
                                        Log.d("Location", "Location services not enabled. Launching settings activity.")

                                        // Launch the location settings activity
                                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                        try {
                                            context.startActivity(intent)
                                            Log.d("Location", "Settings activity launched successfully.")
                                        }
                                        catch (e: Exception)
                                        {
                                            Log.e("Location", "Error launching settings activity: ${e.message}")
                                        }
                                        checkInViewModel.latLong(context, navController)

                                    } else {
                                        checkInViewModel.latLong(context, navController)//for getting current lat long..
                                        Log.d("Home_checkOut", "location is enabled no geo fencing")
                                        navController.navigate("Currentlocation") {
                                            popUpTo("Currentlocation" ){ inclusive = true }
                                        }
                                    }
                                }
                                catch (e: Exception)
                                {
                                    Log.e("Location", "Error during location check: ${e.message}")
                                    // Handle the exception appropriately
                                }


                            }





                        }
                        else
                        {
                            Log.d("Home_checkOut", "nothing is enabled no geo fencing")
                            loading.value = true
                            checkInViewModel.submitCheckout(context, navController)

//                checkInViewModel.submitCheckout(context, lifecycleOwner, navController, viewModel)
                        }

                    }

                }

            }

        }
    }


}



@RequiresApi(34)
private fun checkAndRequestPermissions(
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
): Boolean {

//-------------------------------------------------------------FOR PERMISSIONS--------------------------------------------------------//
    var cameraPermissions: Array<String> = if (Build.VERSION.SDK_INT >= 33) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }
        else
        {
//            TODO("VERSION.SDK_INT < TIRAMISU")
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
    else
    {
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionsToRequest: MutableList<String> = mutableListOf()


    val cameraPermissionsGranted = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE  )
    {

        when
        {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> true

            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> true

            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> false

            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> false

            else -> false

        }





/*
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
            {
                true
            }
            else ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED
        }
        else
        {
            false
        }*/

    }
    else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
    {
        when
        {
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> true

            else -> false

        }
    }
    else
    {
        checkInViewModel.arePermissionsGranted(context, cameraPermissions)
    }



    if (userViewModel.isImageCaptureNeed() && !cameraPermissionsGranted)

//    if (userViewModel.isImageCaptureNeed() && !checkInViewModel.arePermissionsGranted(context, cameraPermissions))
    {


            cameraPermissions =

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            {

                when
                {

                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> arrayOf(Manifest.permission.CAMERA)

                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> arrayOf(Manifest.permission.CAMERA)

                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)

                    else -> arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)

                }

            }
            else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
            {
                when
                {
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ->  arrayOf(Manifest.permission.CAMERA)

                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED ->  arrayOf(Manifest.permission.READ_MEDIA_IMAGES)

                    else ->  arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)

                }
            }
            else
            {
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }


        permissionsToRequest.addAll(cameraPermissions)
    }
    if ((userViewModel.hasGeoFencing() || userViewModel.hasLocationCheckNeed()) && !checkInViewModel.arePermissionsGranted(context, locationPermission))
    {
        permissionsToRequest.addAll(locationPermission)
    }

    if (permissionsToRequest.isNotEmpty()) {
        launcher.launch(permissionsToRequest.toTypedArray())
    }

    return permissionsToRequest.isEmpty()
}


private fun checkAndRequestPermissionsForLocation(
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
): Boolean {

//-------------------------------------------------------------FOR PERMISSIONS--------------------------------------------------------//

    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val permissionsToRequest: MutableList<String> = mutableListOf()


    if (!checkInViewModel.arePermissionsGranted(context, locationPermission))
    {
        permissionsToRequest.addAll(locationPermission)
    }

    if (permissionsToRequest.isNotEmpty()) {
        launcher.launch(permissionsToRequest.toTypedArray())
    }

    return permissionsToRequest.isEmpty()
}

@Composable
fun ShowTime( wfhViewModel: WorkFromHomeViewModel, onDutyViewModel: OnDutyViewModel, navController: NavController) {
    val allowOnDuty = onDutyViewModel.allowOnDuty.value
    val allowWFH = wfhViewModel.allowWFH.value

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp))
    {

        if(allowWFH == 1 || allowOnDuty == 1)
        {
            SetupWFH_OD(navController, allowOnDuty, allowWFH)
        }

    }


}

@Composable
fun ShowWorkingRemotely(){
//    RWOD()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowHoliday(navController: NavController, holidayViewModel: HolidayViewModel)
{
    val holidayData = holidayViewModel.holidayList.collectAsState().value
    HolidayCard(navController = navController, holidaysList = holidayData)
}

@Composable
fun ShowWeddingBirthWorkCards(
    navController: NavController,
    wishesViewModel: HomeWishesViewModel
) {
    val wishesData = wishesViewModel.wishList.collectAsState()
    if (wishesData.value.isNotEmpty())
        wishesViewModel.updateLists()

    Card(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(top = 10.dp, bottom = 10.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
    ){

        ReusableCard(
            model = MainCardModel.BirthdayModel(
                data = wishesViewModel.getBirthDayList(),
                onClick = { navController.navigate("Birthday") }
            )
        )
        Divider(modifier = Modifier.padding( bottom = 10.dp),color = colorResource(id = R.color.lightshade))
        ReusableCard(
            model = MainCardModel.WorkAnniversaryModel(
                data = wishesViewModel.getWorkingWeekList(),
                onClick = { navController.navigate("Work") }
            )
        )
        Divider(modifier = Modifier.padding( bottom = 10.dp),color = colorResource(id = R.color.lightshade))
        ReusableCard(
            model = MainCardModel.WeddingModel(
                data = wishesViewModel.getWeddingWeekList(),
                onClick = { navController.navigate("Wedding") }
            )
        )

    }

}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTimeLog(navController: NavController,checkInViewModel: CheckInViewModel)
{

    var showLog by remember { mutableStateOf(false) }


    var checkInTime by remember { mutableStateOf("") }
    var checkOutTime by remember { mutableStateOf("") }

    var status by remember { mutableStateOf("") }

    var todaysLogDetails: MutableState<List<TodayAttendanceLogData>>


    checkInViewModel.todaysLogList.collectAsState().also {
        todaysLogDetails = it as MutableState<List<TodayAttendanceLogData>>
    }



    val checkInOutTime = calculateCheckInOutTime(todaysLogDetails.value)
    println("Check-In Time: ${checkInOutTime.first}, Check-Out Time: ${checkInOutTime.second}")

    checkInTime = checkInOutTime.first
    checkOutTime = checkInOutTime.second

    for (attendanceData in todaysLogDetails.value)
    {
        status = if(attendanceData.status == "Late") {
            "Late"
        } else {
            todaysLogDetails.value.firstOrNull()?.status ?: ""
        }
    }


    Card(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(top = 5.dp, bottom = 5.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        onClick = {   showLog = !showLog }
    )
    {

        Row(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Attendance Log",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.themeColor),
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Start)
//                        .clickable { showLog = !showLog }
                )
            }

            Column(modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)) {
                IconButton(
                    onClick = { showLog = !showLog },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(size = 20.dp),
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = "",
                        tint = colorResource(id = R.color.themeColor)
                    )
                }
            }

        }

    }


    if(showLog)
    {

        val buttonColor = if(status == "Late") colorResource(id = R.color.toolight_red) else colorResource(id = R.color.toolight_green)
        val buttonTextColor = if(status == "Late") colorResource(id = R.color.red) else colorResource(id = R.color.green)

        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            onClick = { showLog = !showLog }
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp))
            {

                Column(modifier = Modifier.weight(4f))
                {

                    Column(modifier = Modifier.padding(start = 10.dp))
                    {
                        TextFieldNameSizeValidation(value = "In Time", size = 13, color = colorResource(id = R.color.paraColor), weight = 500, maxLength = 16)
                    }
                    Column(modifier = Modifier.padding(start = 10.dp, top = 5.dp))
                    {
                        TextFieldNameSizeValidation(value = checkInTime, size = 14, color = colorResource(id = R.color.black), weight = 500, maxLength = 16)
                    }
                }

                Column(modifier = Modifier.weight(3f))
                {

                    Column()
                    {
                        Text(
                            text = "",
                            color = buttonTextColor,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }

                    Column()
                    {

                        if(status.isNotEmpty() || status.isNotBlank())
                        {
                            Card(
                                shape = RoundedCornerShape(5.dp),
                                colors = CardDefaults.cardColors(containerColor = buttonColor),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ){
                                Text(
                                    text = status,
                                    color = buttonTextColor,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
                                )
                            }
                        }
                    }

                }

                Column(modifier = Modifier.weight(3f))
                {

                    Column(modifier = Modifier .padding(start = 10.dp))
                    {
                        TextFieldNameSizeValidation(value = "Out Time", size = 13, color = colorResource(id = R.color.paraColor), weight = 500, maxLength = 16)
                    }
                    Column(modifier = Modifier.padding(start = 10.dp, top = 5.dp))
                    {
                        TextFieldNameSizeValidation(value = checkOutTime, size = 14, color = colorResource(id = R.color.black), weight = 500, maxLength = 16)
                    }

                }

            }


        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupWFH_OD(navController: NavController, allowOnDuty : Int, allowWFH: Int)
{

//----------------------  EXPANDABLE CARD  ------------------------

    // Work From Home time button

    if(allowWFH == 1)
    {

        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            onClick = { navController.navigate("RemoteWorkRequest") }
        )
        {

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier.weight(0.7f)) {

                    Row( modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Start))
                    {
                        Image(
                            painter = painterResource(id = R.drawable.remoteworksvg),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )

                        Column{
                            Text(text = "Remote Work Request",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.themeColor),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.Start)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(0.3f))
                {
                    IconButton(onClick = { navController.navigate("RemoteWorkRequest") },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(size = 20.dp),
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = "",
                            tint = colorResource(id = R.color.themeColor)
                        )
                    }
                }
            }
        }



    }

    // On Duty time button
    if (allowOnDuty == 1)
    {

        // Code withOUT image

        /*
                Card(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 5.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    onClick = {
                        navController.navigate("OnDutyScreen")

                    }
                )
                {

                    Row(modifier = Modifier.fillMaxWidth()) {

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "On duty Request",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.themeColor),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.Start)
                                    .clickable {

                                        navController.navigate("OnDutyScreen")

                                    })
                        }

                        Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                            IconButton(onClick = {

                                navController.navigate("OnDutyScreen")

                            },
                                modifier = Modifier.align(Alignment.End)

                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(size = 20.dp),
                                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = colorResource(id = R.color.themeColor)
                                )
                            }
                        }

                    }

                }
        */

        // Code with image


        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(bottom = 5.dp),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            onClick = { navController.navigate("OnDutyScreen") }
        )
        {

            Row(modifier = Modifier.fillMaxWidth()) {


                Column(modifier = Modifier.weight(0.7f))
                {
                    Row( modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Start))
                    {
                        Image(
                            painter = painterResource(id = R.drawable.ondutysvg),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )

                        Column{
                            Text(text = "On duty Request",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.themeColor),
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.Start)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(0.3f))
                {
                    IconButton(onClick = { navController.navigate("OnDutyScreen") },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(size = 20.dp),
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = "",
                            tint = colorResource(id = R.color.themeColor)
                        )
                    }
                }
            }
        }

    }

}



@Composable
fun ShowLeaveODRWCards(
    navController: NavController,
    wishesViewModel: HomeWishesViewModel
) {
    val wishesData = wishesViewModel.wishList.collectAsState()
    if (wishesData.value.isNotEmpty())
        wishesViewModel.updateLists()
    Card(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 10.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
    ){
        ReusableCard(
            model = MainCardModel.RemoteWorkModel(
                data = wishesViewModel.getRemoteWorkWeekList(),
                onClick = { navController.navigate("OnRW") },
            )
        )
        Divider(modifier = Modifier.padding( bottom = 10.dp),color = colorResource(id = R.color.lightshade))
        ReusableCard(
            model = MainCardModel.OnDutyModel(
                data = wishesViewModel.getOnDutyWeekList(),
                onClick = { navController.navigate("OnDutyList") },
            )
        )
        Divider(modifier = Modifier.padding( bottom = 10.dp),color = colorResource(id = R.color.lightshade))
        ReusableCard(
            model = MainCardModel.LeaveModel(
                data = wishesViewModel.getLeaveWeekList(),
                onClick = { navController.navigate("OnLeave") },
            )
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HolidayCard(
    navController: NavController,
    holidaysList: List<HolidayListData>
) {
    // holiday card
    Log.d("HolidayCard", "Recomposition${holidaysList.size}")
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 5.dp, bottom = 5.dp)
            .height(130.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Column {
            Box {
                Box(modifier = Modifier.offset(y = 10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Holidays",
                                color = colorResource(id = R.color.black),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        Column {
                            Text(
                                text = "View All",
                                color = colorResource(id = R.color.themeColor),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable {

                                    navController.navigate("Holiday") {
                                        popUpTo("Holiday")
                                    }

                                }
                            )
                        }
                    }
                }
                Box(modifier = Modifier.offset(y = 45.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (holidaysList.isEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth(1f)) {
                                Icon(
                                    painterResource(id = R.drawable.noholidays),
                                    contentDescription = "ring",
                                    tint = colorResource(id = R.color.blue),
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .width(35.dp)
                                        .height(35.dp),
                                )
                                Text(
                                    text = "No Holidays",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.black),
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        } else {
                            val currentMonth = LocalDate.now().monthValue
                            var displayedHolidayCount = 0
                            var holidayFound = false
                            holidaysList.forEach { holiday ->
                                val holidayDate = LocalDate.parse(holiday.holidayDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                                Log.d("holidayDate list", "$holidayDate")
                                val isHolidayDateConditionMet = holiday.holidayDate
                                Log.d("isHolidayDateConditionMet list", isHolidayDateConditionMet)
                                Log.d("holidayDate.monthValue", "${holidayDate.monthValue}")
                                Log.d("currentMonth", "$currentMonth")
                                Log.d("holidayDate.isAfter(LocalDate.now())", "${holidayDate.isAfter(LocalDate.now())}")
                                val currentHoliday = holidayDate.monthValue >= currentMonth && (holidayDate.isEqual(LocalDate.now()) || holidayDate.isAfter(LocalDate.now()))
                                Log.d("currentHoliday", "$currentHoliday")
                                if (currentHoliday && displayedHolidayCount < 2) {
                                    displayedHolidayCount++
                                    holidayFound = true
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(33.dp)
                                                    .background(
                                                        color = colorResource(id = R.color.blue),
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Log.d("Box inside 1", "Rendering Box")
                                                Text(
                                                    text = holiday.date,
                                                    color = colorResource(id = R.color.white),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.offset(y = (-5).dp)
                                                )
                                                Text(
                                                    text = holiday.monthName,
                                                    color = colorResource(id = R.color.white),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    modifier = Modifier.offset(y = 5.dp)
                                                )
                                            }
                                            BasicTextField(
                                                readOnly = true,
                                                value = holiday.holidayName,
                                                onValueChange = { /* Handle value change if needed */ },
                                                textStyle = TextStyle(
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight(500),
                                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                singleLine = true,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 16.dp, start = 10.dp) // Adjust padding as needed
                                            )
                                        }
                                    }
                                }


                            }
                            if (!holidayFound) {
                                // Display a message indicating no holidays were found
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Icon(
                                        painterResource(id = R.drawable.noholidays),
                                        contentDescription = "ring",
                                        tint = colorResource(id = R.color.blue),
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .size(35.dp)
                                    )

                                    Text(
                                        text = "No Holidays",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black),
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReusableCard(
    model: MainCardModel
) {

    Log.d(model.title, "Recomposition ${model.data.size}")
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = model.title,
                color = colorResource(id = R.color.black),
                style = MaterialTheme.typography.titleMedium,
            )

            if (model.data.isNotEmpty())
            {
                Text(
                    text = "View All",
                    color = colorResource(id = R.color.themeColor),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable {
                        model.onClick()
                    }
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (model.data.isEmpty()) {
                Column(modifier = Modifier.fillMaxWidth(1f)) {
                    Image(
                        painterResource(id = model.image),
                        contentDescription = "images",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .alpha(0.45f)
                            .size(with(LocalDensity.current) { 70.dp }),
                    )

                    Text(
                        text = model.defaultText,
                        style = MaterialTheme.typography.titleSmall,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 10.dp)
                    )
                }

            } else {
                model.data.take(2).forEach { event ->
                    // Sample UI components, customize these based on your data structure
                    Log.d("name event", event.name)
                    Column(modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 20.dp, end = 10.dp, start = 10.dp)) {
                        val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                        val img = event.profile // Assuming `wedding.Profile` contains the relative image path
                        val imgUrl = link + img
                        val processedImgUrl = imgUrl.ifEmpty { link }
                        val proName = event.name
                        val firstTwoLetters = proName.take(2).uppercase(Locale.ROOT)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (processedImgUrl != link) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(processedImgUrl)
                                        .crossfade(true)
                                        .transformations(CircleCropTransformation())
                                        .allowHardware(true)
                                        .build(),
                                    contentDescription = "icon",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(35.dp)
                                )
                            } else {
                                Box(contentAlignment = Alignment.BottomCenter){
                                    Box(
                                        modifier = Modifier
                                            .size(33.dp)
                                            .background(
                                                color = colorResource(id = model.iconColor),
                                                shape = CircleShape
                                            ),contentAlignment = Alignment.Center
                                    ){
                                        Text(text = firstTwoLetters, color = colorResource(id = R.color.white), style = MaterialTheme.typography.titleSmall)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)){
                                        repeat(3){
                                            Box(modifier = Modifier
                                                .size(3.dp)
                                                .offset(y = (-4).dp)
                                                .background(
                                                    color = colorResource(id = R.color.white),
                                                    shape = CircleShape
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            Column{
                                BasicTextField(
                                    readOnly = true,
                                    value = event.name,
                                    onValueChange = { /* Handle value change if needed */ },
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        color = colorResource(id = R.color.black)
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 16.dp, start = 10.dp) // Adjust padding as needed
                                )
                                Text(
                                    text = "${event.date} ${event.month}", color = colorResource(id = R.color.paraColor),
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ReusableCardPreview() {



    Column {

        SetupWFH_OD(navController = rememberNavController(), allowOnDuty = 1, allowWFH = 1)
        HolidayCard(navController = rememberNavController(), holidaysList = emptyList<HolidayListData>())
        ReusableCard(
            model = MainCardModel.WeddingModel(
                data = generateWishList(type = "W"),
                onClick = { }
            )
        )

        ReusableCard(
            model = MainCardModel.BirthdayModel(
                data = generateWishList(type = "B"),
                onClick = { }
            )
        )

        ReusableCard(
            model = MainCardModel.WorkAnniversaryModel(
                data = generateWishList(type = "J"),
                onClick = { }
            )
        )
    }
}

fun generateWishList(type: String): List<WishesData> {
    return listOf(
        WishesData(
            name = "Raj M Kumar Singh",
            department = "h.r.",
            designation = "manager",
            profile = "User.png",
            date = "7",
            month = "December",
            dayOfWeek = "Thursday",
            wish = type,
            status = "2",
            empCode = "110011"
        ),
        WishesData(
            name = "test user2",
            department = "h.r.",
            designation = "manager",
            profile = "User.png",
            date = "9",
            month = "December",
            dayOfWeek = "Thursday",
            wish = type,
            status = "2",
            empCode = "110011"
        )
    )
}



private fun getHolidayList(): List<HolidayListData> {
    return listOf(
        HolidayListData(
            monthName = "Jan",
            date = "26",
            weekDay = "Thursday",
            holidayName = "Pongal 2",
            color1 = "#009900",
            color2 = "#ccffcc",
            holidayDate = "2023 - 01-26 T00 :00:00"
        ),
        HolidayListData(
            monthName = "Nov",
            date = "16",
            weekDay = "Thursday",
            holidayName = "Diwali",
            color1 = "#009900",
            color2 = "#ccffcc",
            holidayDate = "2023 - 01-26 T00 :00:00"
        )
    )

}


@RequiresApi(Build.VERSION_CODES.O)
fun calculateCheckInOutTime(data: List<TodayAttendanceLogData>): Pair<String, String> {

    if (data.isEmpty()) return Pair("-", "-")

    val firstData = data.first()
    val lastData = data.last()

    val currentDate = LocalDate.now().toString()
    val startTimePattern = "$currentDate 00:00:00"

    return when {
        data.size == 1 -> {
            if (firstData.attendanceStartTime.startsWith(startTimePattern))
            {
                Pair("-", "-")
            }
            else
            {
                Pair(firstData.checkInTime, firstData.checkOutTime.ifEmpty { "-" })
            }
        }
        data.size == 2 -> {
            if (firstData.attendanceStartTime.startsWith(startTimePattern))
            {
                Pair(data[1].checkInTime, lastData.checkOutTime.ifEmpty { "-"})
            }
            else
            {
                Pair(firstData.checkInTime, lastData.checkOutTime.ifEmpty { lastData.checkInTime })
            }
        }
        data.size >= 3 -> {
            if (firstData.attendanceStartTime.startsWith(startTimePattern))
            {
                Pair(data[1].checkInTime, data.last().checkOutTime.ifEmpty { data.last().checkInTime })
            }
            else
            {
                Pair(data.first().checkInTime, data.last().checkOutTime.ifEmpty { data.last().checkInTime })
            }
        }
        else -> Pair("-", "-")
    }
}

private fun redirectToAppPermissionsScreen(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}