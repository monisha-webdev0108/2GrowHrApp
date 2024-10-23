@file:Suppress("RedundantWith")

package com.payroll.twogrowhr.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.payroll.twogrowhr.Model.View.Approval
import com.payroll.twogrowhr.Model.View.Birthday
import com.payroll.twogrowhr.Model.View.Change_password
import com.payroll.twogrowhr.Model.View.Finance
import com.payroll.twogrowhr.Model.View.Holiday
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyListData
import com.payroll.twogrowhr.Model.ResponseModel.WFHListData
import com.payroll.twogrowhr.Model.View.AadhaarCard
import com.payroll.twogrowhr.Model.View.ApplyLeaveNew
import com.payroll.twogrowhr.Model.View.ApplyLoan
import com.payroll.twogrowhr.Model.View.Asset
import com.payroll.twogrowhr.Model.View.CameraScreen
import com.payroll.twogrowhr.Model.View.CheckInLocation
import com.payroll.twogrowhr.Model.View.Currentlocation
import com.payroll.twogrowhr.Model.View.HomeScreen
//import com.payroll.twogrowhr.Model.View.HomeScreen_Two
//import com.payroll.twogrowhr.Model.View.HomeScreenTwo
import com.payroll.twogrowhr.Model.View.Leave
import com.payroll.twogrowhr.Model.View.LeaveApply
import com.payroll.twogrowhr.Model.View.Attendance
import com.payroll.twogrowhr.Model.View.CompoOffApproval
import com.payroll.twogrowhr.Model.View.CompoOffApprovalDetail
import com.payroll.twogrowhr.Model.View.DrivingLicenseDoc
import com.payroll.twogrowhr.Model.View.EditReporteeLoan
import com.payroll.twogrowhr.Model.View.EducationDoc
import com.payroll.twogrowhr.Model.View.EducationDocList
import com.payroll.twogrowhr.Model.View.ExperienceDoc
import com.payroll.twogrowhr.Model.View.ExperienceDocumentList
import com.payroll.twogrowhr.Model.View.FormTds
import com.payroll.twogrowhr.Model.View.LeaveHistory
import com.payroll.twogrowhr.Model.View.LeaveHistoryDetail
import com.payroll.twogrowhr.Model.View.LeaveListApproval
import com.payroll.twogrowhr.Model.View.Leave_approvals
import com.payroll.twogrowhr.Model.View.Loan
import com.payroll.twogrowhr.Model.View.LoanApprovalDetail
import com.payroll.twogrowhr.Model.View.LoanApprovalList
import com.payroll.twogrowhr.Model.View.LoanApprovalStages
import com.payroll.twogrowhr.Model.View.LoanHistoryList
import com.payroll.twogrowhr.Model.View.Loan_view
import com.payroll.twogrowhr.Model.View.Login
import com.payroll.twogrowhr.Model.View.MyDocument
import com.payroll.twogrowhr.Model.View.Notify
import com.payroll.twogrowhr.Model.View.ODApproval
import com.payroll.twogrowhr.Model.View.OTApproval
import com.payroll.twogrowhr.Model.View.OnDutyList
import com.payroll.twogrowhr.Model.View.OnDutyScreen
import com.payroll.twogrowhr.Model.View.OnLeave
import com.payroll.twogrowhr.Model.View.OnRWList
import com.payroll.twogrowhr.Model.View.OrgDoc
import com.payroll.twogrowhr.Model.View.OrgDocList
import com.payroll.twogrowhr.Model.View.OtherDoc
import com.payroll.twogrowhr.Model.View.OtherDocList
import com.payroll.twogrowhr.Model.View.PanCard
import com.payroll.twogrowhr.Model.View.PassportCard
import com.payroll.twogrowhr.Model.View.Payslip
import com.payroll.twogrowhr.Model.View.Profile
import com.payroll.twogrowhr.Model.View.RegularizedApproval
import com.payroll.twogrowhr.Model.View.RemoteWorkRequest
import com.payroll.twogrowhr.Model.View.Report
import com.payroll.twogrowhr.Model.View.Salary
import com.payroll.twogrowhr.Model.View.ShiftSelection
import com.payroll.twogrowhr.Model.View.TeamAttendance
import com.payroll.twogrowhr.Model.View.ViewPayslip
import com.payroll.twogrowhr.Model.View.VoterIDDoc
import com.payroll.twogrowhr.Model.View.WFHApproval
import com.payroll.twogrowhr.Model.View.Wedding
import com.payroll.twogrowhr.Model.View.Work
import com.payroll.twogrowhr.Model.View.approvalList
import com.payroll.twogrowhr.Model.View.emptyLoanData
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.Model.View.onLoginClicked
import com.payroll.twogrowhr.Model.View.teamAttendanceDetailView
import com.payroll.twogrowhr.Process.FakeGPSAlert
import com.payroll.twogrowhr.Process.FakeTimeAlert
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.di.InjectedNavigationEntryPoint
import com.payroll.twogrowhr.findActivity
import com.payroll.twogrowhr.redirectJob
import com.payroll.twogrowhr.ui.theme.Updates
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.AssetsListViewModel
import com.payroll.twogrowhr.viewModel.AttendanceRegularizedViewModel
import com.payroll.twogrowhr.viewModel.BirthdayDetailViewModel
import com.payroll.twogrowhr.viewModel.ChangePasswordView
import com.payroll.twogrowhr.viewModel.CheckInViewModel
import com.payroll.twogrowhr.viewModel.DocumentViewModel
import com.payroll.twogrowhr.viewModel.HolidayViewModel
import com.payroll.twogrowhr.viewModel.HomeWishesViewModel
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.LoanSubDetailsListViewModel
import com.payroll.twogrowhr.viewModel.LoginViewModel
import com.payroll.twogrowhr.viewModel.MainViewModel
import com.payroll.twogrowhr.viewModel.OnDutyListViewModel
import com.payroll.twogrowhr.viewModel.OnDutyViewModel
import com.payroll.twogrowhr.viewModel.OnLeaveViewModel
import com.payroll.twogrowhr.viewModel.OnRWViewModel
import com.payroll.twogrowhr.viewModel.OrgDocDetailViewModel
import com.payroll.twogrowhr.viewModel.OrgDocListViewModel
import com.payroll.twogrowhr.viewModel.OverTimeViewModel
import com.payroll.twogrowhr.viewModel.PaySlipHeadDetailsViewModel
import com.payroll.twogrowhr.viewModel.PaySlipViewModel
import com.payroll.twogrowhr.viewModel.ProfileViewModel
import com.payroll.twogrowhr.viewModel.RegularizedApprovalViewModel
import com.payroll.twogrowhr.viewModel.SalaryDetailViewModel
import com.payroll.twogrowhr.viewModel.SavedStateViewModelFactory
import com.payroll.twogrowhr.viewModel.TdsFormViewModel
import com.payroll.twogrowhr.viewModel.UserDataViewModel
import com.payroll.twogrowhr.viewModel.WeddingDetailViewModel
import com.payroll.twogrowhr.viewModel.WorkDetailViewModel
import com.payroll.twogrowhr.viewModel.WorkFromHomeViewModel
import dagger.hilt.EntryPoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.time.LocalDate
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

lateinit var userViewModel: UserDataViewModel



val loading : MutableState<Boolean> = mutableStateOf(false)

val isMockLocation : MutableState<Boolean> = mutableStateOf(false)

val isDeveloperEnabled : MutableState<Boolean> = mutableStateOf(false)
val isTimeZoneAutomaticEnabled : MutableState<Boolean> = mutableStateOf(false)

var isUpdateAvailable : MutableState<Boolean> = mutableStateOf(false)

lateinit var checkInViewModel: CheckInViewModel

//var isConnected  : MutableState<Boolean> =  mutableStateOf(false)


fun startRedirectTimer(navController: NavController, context : Context, url : String) {
    redirectJob = CoroutineScope(Dispatchers.Main).launch {
        delay(TimeUnit.SECONDS.toMillis(30))  // Delay for 30 seconds
        Constant.showToast(context, "Requested Timeout : Please refresh and Try again...!" )
        loading.value = false
        navController.navigate(url)
        delay(TimeUnit.SECONDS.toMillis(2))  // Delay for 2 seconds
    }
}

@Composable
fun FakeGPSAlert1() {
    Log.d("app_nav", "Inside FakeGPSAlert")

    val context = LocalContext.current

//     isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.light_black))
            .alpha(0.9f),
        contentAlignment = Alignment.Center
    ) {

        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(330.dp)
                .padding(16.dp)
                .background(color = Color.Transparent),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Image(
                    painter = painterResource(id = R.drawable.alert_image),
                    contentDescription = "imageDescription",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(160.dp)
                )

//                    HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(5.dp))

                Text(
                    text = "Fake GPS Detected..!",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                )

                Text(
                    text = "Turn off mock location to proceed...",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 11.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.padding(5.dp),
                )

                HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(5.dp))

                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                    context.startActivity(intent)
                }, modifier = Modifier.padding(5.dp),
                ) {
                    Text(
                        text = "Open Settings",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 11.sp,
                            fontWeight = FontWeight(500),
                        ),
                    )
                }

                HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(5.dp))

                TextButton(onClick = {

                     context.findActivity()?.finish()

                }, modifier = Modifier.padding(5.dp),
                ) {
                    Text(
                        text = "Close",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 11.sp,
                            fontWeight = FontWeight(500),
                        ),
                    )
                }
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun App_Nav(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    val isLoggedIn = SharedPreferenceManager.isLoggedIn(LocalContext.current)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val privacyKey = SharedPreferenceManager.getPrivacyPolicy(LocalContext.current)


    // Dispose of resources when the Composable is removed from the composition

    DisposableEffect(Unit) {
        onDispose {
            redirectJob?.cancel()// Cancel redirectJob if it exists
        }
    }

    if(loading.value)
    {
        circularProgression()
        startRedirectTimer(navController, LocalContext.current,"HomeScreen")
    }


    Log.d("isMockLocation.value", "${isMockLocation.value}")



/*    LaunchedEffect(Unit) {

        isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1
        isFakeGPSDetected(context, navController)
        Log.d("Inside Launch Effect", "isDeveloperEnabled : ${isDeveloperEnabled.value}")

    }*/

    /*

        LaunchedEffect(Unit) {
            Log.d("Inside Launch Effect" ,"for call isMockLocation")

            isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1

            isFakeGPSDetected(context, navController)

            // Continuously check for fake GPS usage
    */
/*        while (true) {
            mainViewModel.checkForFakeGPS(context, navController)
            delay(5000)
        }*//*


        delay(2000) // Adjust the delay as needed

    }

*/


    //NEED TO WRITE IT FOR NETWORK CONTINOUS CHECKING




    // Show alert dialog if fake GPS is detected
//    if (isMockLocation.value && isDeveloperEnabled.value)
/*    if (isMockLocation.value)
    {
        FakeGPSAlert(navController)
    }
    else
    {

    }*/


    NavHost(navController = navController, startDestination = getStartDestination(isLoggedIn,privacyKey)) {


        composable(Screen.Network.route+ "/{url}") {navBackStackEntry ->

            val url = navBackStackEntry.arguments?.getString("url") as String

            noNetworkConnection(context , navController = navController, url = url)
        }


        composable(Screen.FakeGPS.route) {
            FakeGPSAlert(navController = navController)
        }

        composable(Screen.FakeTime.route) {
            FakeTimeAlert(navController = navController)
        }

        composable(
            route = "${Screen.HomeScreen.route}?res={res}",
            arguments = listOf(navArgument("res") {
                defaultValue = ""
                type = NavType.StringType
            })
        ) { navBackStackEntry ->

            loading.value = false
            redirectJob?.cancel()



            val resD = navBackStackEntry.arguments?.getString("res")

            Log.d("resD Before Decode ", "$resD")


            val resDecoded = URLDecoder.decode(resD, "UTF-8")

            Log.d("resD After Decode", resDecoded)


            val lres = mainViewModel.getLoginResponse(context = context, res = resDecoded)

            val loginViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
                LoginViewModel(
                    navController = navController,
                    data = lres
                )
            }
            val userData = loginViewModel.getUSerData() ?: return@composable

            userViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    userData = userData,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )


            checkInViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val onDutyViewModel: OnDutyViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val holidayViewModel: HolidayViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )


            val wfhViewModel: WorkFromHomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )


            val currentDate = remember { mutableStateOf(LocalDate.now()) }
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val org = userViewModel.getOrg()

            val index = 0 // Replace with the desired index
            val index1 = 0 // Replace with the desired index


            val wishesViewModel: HomeWishesViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )


            val empID = userViewModel.getSFCode()

            var orgModeState by remember { mutableStateOf<Int?>(null) }


            LaunchedEffect(empID, currentDate, org) {


/*                Log.d("Inside Launch Effect" ,"for call isMockLocation")

                isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1

                isFakeGPSDetected(context, navController)

                delay(1000) // Adjust the delay as needed*/

                mainViewModel.getCheckInVisibility(navController = navController, context = context, sfCode = empID)
                delay(10)
                checkInViewModel.getTodaysLogDetails(context = context, navController = navController, empId = userViewModel.getSFCode())
                delay(10)
                wishesViewModel.getWishesDetails(context = context, empId = empID, org = org)
                delay(10)
                checkInViewModel.getEmpShiftDetails(context = context, navController = navController, empId = empID)
                delay(10)
                holidayViewModel.getHoliday(navController = navController, context = context, empID, currentYear.toString())
                delay(10)
                if (userViewModel.hasShiftSelection()) {
                    checkInViewModel.getShiftDetails1(context = context, navController = navController, empID)
                }
                delay(10)
                onDutyViewModel.getOnDutyDetails(navController, context = context, empID,currentDate.value.toString(), org.toString()){onDutyListData ->
                    onDutyViewModel.onDutyDataList1 = onDutyListData as List<OnDutyListData>
                    try
                    {
                        if (index < onDutyViewModel.onDutyDataList1.size)
                        {
                            val onDutyItem = onDutyViewModel.onDutyDataList1[index]
                            onDutyViewModel.allowOnDuty.value = onDutyItem.allowOnDuty
                            onDutyViewModel.isOnDutyCheckInApplied.value = onDutyItem.onDutyApplied
                            onDutyViewModel.isOnDutyCheckInApproved.value = onDutyItem.onDutyAppApprove
                            onDutyViewModel.isCheckInAllowedForApplied.value = onDutyItem.odAppliedDaysCheckInOut
                            onDutyViewModel.isCheckInAllowedOnlyApproved.value = onDutyItem.odAppliedDaysApproveCheckInOut
                        }
                    }
                    catch (e : Exception)
                    {
                        Log.e("From App Navigation", "Error during navigation: ${e.message}")
                    }

                }


                delay(10)

                wfhViewModel.getWFHDetails(navController = navController,context = context,empID,currentDate.value.toString(), org.toString()){wfhListData ->
                    wfhViewModel.wfhDataList1 = wfhListData as List<WFHListData>
                    try
                    {
                        if (index1 < wfhViewModel.wfhDataList1.size)
                        {
                            val wfhItem = wfhViewModel.wfhDataList1[index]
                            wfhViewModel.allowWFH.value = wfhItem.allowWFH
                            wfhViewModel.isWFHCheckInApplied.value = wfhItem.wfhApplied
                            wfhViewModel.isWFHCheckInApproved.value = wfhItem.WFHAppApprove
                            wfhViewModel.isCheckInAllowedForApplied.value = wfhItem.wfhAppliedDaysCheckInOut
                            wfhViewModel.isCheckInAllowedOnlyApproved.value = wfhItem.wfhAppliedDaysApproveCheckInOut
                        }
                    }
                    catch (e : Exception)
                    {
                        Log.e("From App Navigation", "Error during navigation: ${e.message}")
                    }

                }

/*                isFakeGPSDetected(context, navController)

                delay(2000) // Adjust the delay as needed*/



            }

            mainViewModel.orgMode.observe(lifecycleOwner) {
                orgModeState = it
            }

            if(orgModeState == 2 || orgModeState == 3)
            {
                // Perform logout actions here
                SharedPreferenceManager.setLoggedIn(context, false)
                SharedPreferenceManager.setCheckInOut(context, "")
                Constant.clearSQLiteData(context)
                Constant.clearVariables()
                mainViewModel.clearUiState1()
                mainViewModel.clearCheckInState()
                navController.navigate("Login")
            }
            else
            {
                HomeScreen(
                    navController = navController,
                    viewModel = mainViewModel,
                    loginViewModel1 = loginViewModel,
                    userViewModel1 = userViewModel,
                    checkInViewModel1 = checkInViewModel,
                    holidayViewModel1 = holidayViewModel,
                    onDutyViewModel1 = onDutyViewModel,
                    wfhViewModel = wfhViewModel,
                    wishesViewModel=wishesViewModel
                )

            }



        }


        composable(Screen.OnDutyScreen.route)
        {navBackStackEntry ->

            val onDutyViewModel: OnDutyViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val currentDate = remember { mutableStateOf(LocalDate.now()) }
            val index = 0 // Replace with the desired index

            val org = userViewModel.getOrg()
            val empID = userViewModel.getSFCode()


            LaunchedEffect(empID, currentDate, org)
            {

                onDutyViewModel.getOnDutyDetails(navController,context,empID,currentDate.value.toString(),org.toString()){onDutyListData ->

                    onDutyViewModel.onDutyDataList1 = onDutyListData as List<OnDutyListData>

                    if (index < onDutyViewModel.onDutyDataList1.size)
                    {
                        val onDutyItem = onDutyViewModel.onDutyDataList1[index]
                        onDutyViewModel.allowOnDuty.value = onDutyItem.allowOnDuty
                        onDutyViewModel.allowToApplyPastDays.value = onDutyItem.restrictPastDate
                        onDutyViewModel.allowPastDays.value = onDutyItem.restrictPastOnDutyDays + 1
                        onDutyViewModel.isCommentRequired.value = onDutyItem.commentReq
                        onDutyViewModel.isHalfDayNeeded.value = onDutyItem.hdWorkReq
                        onDutyViewModel.isRestrictByDays.value = onDutyItem.allowDaysOnDuty //is on duty restrict by days or not
                        onDutyViewModel.onDutyRestrictDays.value = onDutyItem.numberOfDaysOnDuty // on Duty restriction count
                        onDutyViewModel.onDutyRestrictSession.value = onDutyItem.durationOfOnDuty // on Duty restriction days by which session[month,year,week,quarterly,half]
                        onDutyViewModel.onDutyAppliedDays.value = onDutyItem.odMonCnt  // appliedDays < allowed days
                        onDutyViewModel.isRestrictEmpOnHW.value = onDutyItem.restrictEmpOnDuty
                        onDutyViewModel.restrictOnHW.value = onDutyItem.restrictDayOff
                        onDutyViewModel.isHoliday.value = onDutyItem.restrictEmpHolidayDate
                        onDutyViewModel.isWeeklyOff.value = onDutyItem.restrictEmpWeeklyOff
                        onDutyViewModel.isODAppliedEarlier.value = onDutyItem.odDateCnt
                        onDutyViewModel.isWFHAppliedEarlier.value = onDutyItem.wfhDatecnt
                        onDutyViewModel.isCheckOutOrNot.value = onDutyItem.odCheckOutOrNot
                        onDutyViewModel.dateOfJoining.value = onDutyItem.doj
                        onDutyViewModel.ruleId.value = onDutyItem.ruleId
                        onDutyViewModel.calendar.timeInMillis = System.currentTimeMillis()
                        if(onDutyViewModel.allowToApplyPastDays.value == 1)
                        {
                            onDutyViewModel.calendar.add(Calendar.DAY_OF_YEAR,
                                (-(onDutyViewModel.allowPastDays.value)).roundToInt()
                            )
                        }
                        else
                        {
                            onDutyViewModel.calendar.set(1900,0,0)
                        }
                        onDutyViewModel.startDate.value = onDutyViewModel.calendar.timeInMillis
                    }
                }
            }

            OnDutyScreen(navController = navController, onDutyViewModel = onDutyViewModel)
        }

        composable(Screen.RemoteWorkRequest.route)
        {navBackStackEntry ->

            val wfhViewModel: WorkFromHomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )


            val currentDate = remember { mutableStateOf(LocalDate.now()) }
            val index = 0 // Replace with the desired index
            val org = userViewModel.getOrg()
            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID, currentDate, org)
            {
                wfhViewModel.getWFHDetails(navController, context, empID, currentDate.value.toString(), org.toString()){wfhListData ->

                    wfhViewModel.wfhDataList1 = wfhListData as List<WFHListData>

                    if (index < wfhViewModel.wfhDataList1.size)
                    {
                        val wfhItem = wfhViewModel.wfhDataList1[index]
                        wfhViewModel.allowToApplyPastDays.value = wfhItem.restrictPastDate
                        wfhViewModel.allowWFH.value = wfhItem.allowWFH
                        wfhViewModel.allowPastDays.value = wfhItem.restrictPastWFHDays + 1
                        wfhViewModel.isCommentRequired.value = wfhItem.commentReq
                        wfhViewModel.isHalfDayNeeded.value = wfhItem.hdWorkReq
                        wfhViewModel.isRestrictByDays.value = wfhItem.allowDaysWFH //is on duty restrict by days or not
                        wfhViewModel.wfhRestrictDays.value = wfhItem.numberOfDaysWFH // on Duty restriction count
                        wfhViewModel.wfhRestrictSession.value = wfhItem.durationOfWFH // on Duty restriction days by which session[month,year,week,quarterly,half]
                        wfhViewModel.wfhAppliedDays.value = wfhItem.wfhMonCnt  // appliedDays < allowed days
                        wfhViewModel.isRestrictEmpOnHW.value = wfhItem.restrictEmpWFH
                        wfhViewModel.restrictOnHW.value = wfhItem.restrictDayOff
                        wfhViewModel.isHoliday.value = wfhItem.restrictEmpHolidayDate
                        wfhViewModel.isWeeklyOff.value = wfhItem.restrictEmpWeeklyOff
                        wfhViewModel.isODAppliedEarlier.value = wfhItem.odDatecnt
                        wfhViewModel.isWFHAppliedEarlier.value = wfhItem.wfhDateCnt
                        wfhViewModel.dateOfJoining.value = wfhItem.doj
                        wfhViewModel.ruleId.value = wfhItem.ruleId
                        wfhViewModel.calendar.timeInMillis = System.currentTimeMillis()
                        if(wfhViewModel.allowToApplyPastDays.value == 1)
                        {
                            wfhViewModel.calendar.add(Calendar.DAY_OF_YEAR,
                                (-(wfhViewModel.allowPastDays.value)).roundToInt()
                            )
                        }
                        else{
                            wfhViewModel.calendar.set(1900,0,0)
                        }
                        wfhViewModel.startDate.value = wfhViewModel.calendar.timeInMillis
                    }
                }
            }

            RemoteWorkRequest(navController = navController, wfhViewModel = wfhViewModel)
        }

        composable(Screen.CameraScreen.route)
        {
            CameraScreen(navController = navController)
        }

        composable(Screen.Attendance.route) { navBackStackEntry ->

            val attendanceRegularizedViewModel: AttendanceRegularizedViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            Attendance(navController = navController,  attendance_regularizedViewModel = attendanceRegularizedViewModel, viewModel = mainViewModel)
        }

        composable(Screen.Reports.route) {
            Report(navController = navController, viewModel = mainViewModel)
        }


        composable(Screen.Finance.route) {navBackStackEntry ->

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            LaunchedEffect(userViewModel.getSFCode(), userViewModel.getOrg())
            {
                loanViewModel.getLoanEnableDetails(navController,context, userViewModel.getSFCode(), userViewModel.getOrg().toString())
            }

            Finance(navController = navController, viewModel = mainViewModel, loanViewModel = loanViewModel)
        }


        composable(Screen.Birthday.route) { navBackStackEntry ->

            val birthdayViewModel: BirthdayDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                birthdayViewModel.getBirthdayDetails(navController=navController, context = context, empId = empID, org = org)
            }

            Birthday(navController = navController, birthdayViewModel = birthdayViewModel)
        }

        composable(Screen.Payslip.route) {navBackStackEntry ->

            val paySlipViewModel: PaySlipViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val year by remember { mutableStateOf(currentYear) }

            LaunchedEffect(year)
            {
                paySlipViewModel.getPaidMonthList(navController,context,year.toString())
            }

            Payslip(navController = navController, paySlipViewModel = paySlipViewModel)
        }

        /*sample for payslip*/
        composable(
            route = "${Screen.LoanView.route}?res={res}&loanName={loanName}&installmentAmount={installmentAmount}&deductionType={deductionType}&noInstallment={noInstallment}&totalAmount={totalAmount}&pendingAmount={pendingAmount}",
            arguments = listOf(
                navArgument("res") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("loanName") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("installmentAmount") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("deductionType") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("noInstallment") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("totalAmount") {
                    defaultValue = ""
                    type = NavType.StringType
                },
            )
        ) { navBackStackEntry ->

            val loanSubDetailListViewModel: LoanSubDetailsListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )
            val res = navBackStackEntry.arguments?.getString("res") ?.toIntOrNull() ?: 0

            LaunchedEffect(res) {
                loanSubDetailListViewModel.getLoanSubDetails(navController=navController,context = context,Sl_No = res)
            }


            Loan_view(navController = navController, loanSubDetailViewModel = loanSubDetailListViewModel, slNo = res)
        }

        composable(Screen.FormTds.route) {navBackStackEntry ->

            val tdsFormViewModel: TdsFormViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val year by remember { mutableStateOf(currentYear) }

            LaunchedEffect(year)
            {
                tdsFormViewModel.getTdsFormMonthList(navController,context,year.toString())
            }

            FormTds(navController = navController, tdsFormViewModel = tdsFormViewModel)
        }

        composable(Screen.Salary.route) { navBackStackEntry ->

            val salaryDetailViewModel: SalaryDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID)
            {
                salaryDetailViewModel.getSalaryDetails(navController,context,empID)
            }

            Salary(navController = navController, salaryDetailViewModel)

        }

        //UPDATED APPLY LOAN
        composable(Screen.ApplyLoan.route) {navBackStackEntry ->

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            LaunchedEffect(userViewModel.getSFCode(), userViewModel.getOrg())
            {
                loanViewModel.getLoanTypeDetails(navController, context, userViewModel.getSFCode(),  userViewModel.getOrg().toString())
            }

            ApplyLoan(navController = navController, loanViewModel = loanViewModel)
        }



        composable(Screen.LoanApprovalList.route) {navBackStackEntry ->
            val approvalViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                approvalViewModel.getLoanApprovalDetails(navController, context, empID, org.toString())

            }

            LoanApprovalList(navController = navController, approvalListViewModel = approvalViewModel)
        }



        composable(route = "${Screen.LoanApprovalDetail.route}?res={res}&loanDataJson={loanDataJson}",
            arguments = listOf(
                navArgument("res") { type = NavType.StringType },
                navArgument("loanDataJson") { type = NavType.StringType }
            )

        ) { navBackStackEntry ->

            val loanApplySlNo = navBackStackEntry.arguments?.getString("res") ?.toIntOrNull() ?: 0

            val loanDataJson = navBackStackEntry.arguments?.getString("loanDataJson")
            val loanData = Gson().fromJson(loanDataJson, LoanApprovalDataDetails::class.java)

            val approvalListViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                approvalListViewModel.getAppliedLoanInstalmentDetails(navController, context, loanData.employeeID, loanApplySlNo.toString())
            }

            LoanApprovalDetail(navController = navController, approvalListViewModel = approvalListViewModel, loanData = loanData )
        }


        composable(route = "${Screen.LoanHistoryList.route}?res={res}&loanDataJson={loanDataJson}",
            arguments = listOf(
                navArgument("res") { type = NavType.StringType },
                navArgument("loanDataJson") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->

            val loanApplySlNo = navBackStackEntry.arguments?.getString("res") ?.toIntOrNull() ?: 0

            val loanDataJson = navBackStackEntry.arguments?.getString("loanDataJson")
            val loanData = Gson().fromJson(loanDataJson, LoanApprovalDataDetails::class.java)

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val approvalListViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                loanViewModel.getLoanDetails1(navController, context, loanData.employeeID, loanData, "2")
            }

            LoanHistoryList(navController = navController, loanViewModel= loanViewModel, approvalListViewModel = approvalListViewModel, loanData = loanData )
        }

        composable(route = "${Screen.LoanApprovalStages.route}?res={res}&loanDataJson={loanDataJson}",
            arguments = listOf(
                navArgument("res") { type = NavType.StringType },
                navArgument("loanDataJson") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->

            val loanApplySlNo = navBackStackEntry.arguments?.getString("res") ?.toIntOrNull() ?: 0

            val loanDataJson = navBackStackEntry.arguments?.getString("loanDataJson")
            val loanData = Gson().fromJson(loanDataJson, LoanApprovalDataDetails::class.java)

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val approvalListViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                approvalListViewModel.getLoanApprovalStagesDetails(navController, context, loanData.employeeID, loanData.loanApplySlNo, org.toString())
            }

            LoanApprovalStages(navController = navController, approvalListViewModel = approvalListViewModel, loanData = loanData )
        }

        composable(route = "${Screen.EditReporteeLoan.route}?res={res}&loanDataJson={loanDataJson}",
            arguments = listOf(
                navArgument("res") { type = NavType.StringType },
                navArgument("loanDataJson") { type = NavType.StringType }
            )) {navBackStackEntry ->

            val loanDataJson = navBackStackEntry.arguments?.getString("loanDataJson")
            val loanData = Gson().fromJson(loanDataJson, LoanApprovalDataDetails::class.java)

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val approvalListViewModel: ApprovalListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            LaunchedEffect(userViewModel.getSFCode(), userViewModel.getOrg())
            {
                loanViewModel.getLoanEligibilityDetails(
                    navController = navController,
                    context = context,
                    empId = loanData.employeeID,
                    ruleID = loanData.loanTypeSlNo
                ) { count -> }
            }

            EditReporteeLoan(navController = navController, loanViewModel = loanViewModel, approvalListViewModel = approvalListViewModel, loanData = loanData)
        }




        composable(Screen.Loan.route) { navBackStackEntry ->

            val loanViewModel: LoanDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )
            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID) {
                loanViewModel.getLoanDetails1(navController, context, empID, emptyLoanData(), "1")
            }

            Loan(navController = navController, loanViewModel = loanViewModel)
        }



        composable(route = "${Screen.OrgDoc.route}?res={res}",
            arguments = listOf(
                navArgument("res") {
                    defaultValue = ""
                    type = NavType.StringType
                },

            )) {navBackStackEntry ->

            val orgDocDetailViewModel: OrgDocDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val folderId = navBackStackEntry.arguments?.getString("res") ?.toIntOrNull() ?: 0
            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

//            val folderId = navBackStackEntry.arguments?.getString("folderId")
            Log.d("folderId passed id","$folderId")

            LaunchedEffect(empID,org,folderId) {
                orgDocDetailViewModel.getOrgDocDetailDetails(navController = navController, context = context, empId = empID, org = org, folderId = folderId)
            }

            OrgDoc(navController = navController, orgDocDetailViewModel = orgDocDetailViewModel)
        }


        composable(Screen.Profile.route) {navBackStackEntry ->
            val profileViewModel : ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )
            Profile(navController = navController, profileViewModel = profileViewModel)
        }

        composable(Screen.Updates.route) {
            Updates(navController = navController, viewModel = mainViewModel)
        }



        /*sample for payslip*/
        composable(

            route = "${Screen.ViewPayslip.route}?month={month}&year={year}&urlpdf={urlpdf}",
            arguments = listOf(
                navArgument("month") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("year") {
                    defaultValue = ""
                    type = NavType.StringType
                },
                navArgument("urlpdf") {
                    defaultValue = ""
                    type = NavType.StringType
                }

            )
        ) { navBackStackEntry ->

            val paySlipHeadDetailsViewModel: PaySlipHeadDetailsViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )
            val empID = userViewModel.getSFCode()
            val Month = navBackStackEntry.arguments?.getString("month") ?.toString() ?: ""
            val Year = navBackStackEntry.arguments?.getString("year") ?.toString() ?: ""



            LaunchedEffect(empID) {
                paySlipHeadDetailsViewModel.getPaySlipHeadList(navController, context, empID, Month, Year )
                paySlipHeadDetailsViewModel.getPaySlipEarningList(navController, context, empID, Month, Year)
                paySlipHeadDetailsViewModel.getPaySlipDeductionList(navController, context, empID, Month, Year)
                paySlipHeadDetailsViewModel.getPaySlipOtherDeductionList(navController, context, empID, Month, Year)
            }

            ViewPayslip(navController = navController, paySlipHeadDetailsViewModel = paySlipHeadDetailsViewModel)
        }


        composable(Screen.MyDocument.route) {navBackStackEntry ->
            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()
            LaunchedEffect(empID) {
                documentViewModel.getDocumentList(navController, context = context, empID = empID)
            }

            MyDocument(navController = navController, documentViewModel = documentViewModel)
        }
        composable(Screen.Asset.route) {navBackStackEntry ->
            val assetViewModel: AssetsListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()


            LaunchedEffect(empID) {
                assetViewModel.getAssetDetails(navController=navController, context = context, empId = empID,)
            }
            Asset(navController = navController, assetViewModel = assetViewModel)
        }

        composable(Screen.Birthday.route) { navBackStackEntry ->

            val birthdayViewModel: BirthdayDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                birthdayViewModel.getBirthdayDetails(navController=navController, context = context, empId = empID, org = org)
            }

            Birthday(navController = navController, birthdayViewModel = birthdayViewModel)
        }
        composable(Screen.PanCard.route  + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "PAN card : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDocumentDataDetails(navController, context, empID, type)
            }

            PanCard(navController = navController, documentViewModel = documentViewModel, type = type)
        }

        composable(Screen.PassportCard.route  + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "Passport : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDocumentDataDetails(navController, context, empID, type)
            }

            PassportCard(navController = navController, documentViewModel = documentViewModel, type = type)
        }

        composable(Screen.AadhaarCard.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "AadhaarCard : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDocumentDataDetails(navController, context, empID, type)
            }

            AadhaarCard(navController = navController, documentViewModel = documentViewModel, type = type)
        }
        composable(Screen.VoterIDDoc.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "VoterIDDoc : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDocumentDataDetails(navController, context, empID, type)
            }

            VoterIDDoc(navController = navController, documentViewModel = documentViewModel, type = type)
        }
        composable(Screen.DrivingLicenseDoc.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "DrivingLicenseDoc : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDocumentDataDetails(navController, context, empID, type)
            }

            DrivingLicenseDoc(navController = navController, documentViewModel = documentViewModel, type = type)
        }


        composable(Screen.EducationDocList.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID) {
                documentViewModel.getEducationDocumentDataDetails(navController, context, empID, type)
            }

            EducationDocList(navController = navController, documentViewModel = documentViewModel, type = type)
        }

        composable(Screen.EducationDoc.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "EducationDoc : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getDegreeDetails(navController, context, empID, type)
            }

            EducationDoc(navController = navController, documentViewModel = documentViewModel, type = type)
        }

        composable(Screen.ExperienceDocumentList.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID) {
                documentViewModel.getExperienceDocumentDataDetails(navController, context, empID, type)
            }

            ExperienceDocumentList(navController = navController, documentViewModel = documentViewModel, type = type)
        }



        composable(Screen.ExperienceDoc.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            Log.d("App Nav", "ExperienceDoc : Type : $type")


            ExperienceDoc(navController = navController, documentViewModel = documentViewModel, type = type)
        }


        composable(Screen.OtherDocList.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            Log.d("App Nav", "OtherDoc : Type : $type")

            LaunchedEffect(empID) {
                documentViewModel.getOtherDocumentDataDetails(navController, context, empID, type)
            }

            OtherDocList(navController = navController, documentViewModel = documentViewModel, type = type)
        }

        composable(Screen.OtherDoc.route + "/{type}") {navBackStackEntry ->

            val type = navBackStackEntry.arguments?.getString("type") as String

            val documentViewModel : DocumentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            Log.d("App Nav", "OtherDoc : Type : $type")

            OtherDoc(navController = navController, documentViewModel = documentViewModel, type = type)
        }


        composable(Screen.OrgDocList.route) {navBackStackEntry ->

            val orgDocListViewModel: OrgDocListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID,org) {
                orgDocListViewModel.getOrgDocListDetails(navController = navController, context = context, empId = empID, org = org)
            }

            OrgDocList(navController = navController, orgDocListViewModel = orgDocListViewModel)
        }

        /*composable(Screen.OrgDoc.route) {navBackStackEntry ->

            val orgDocDetailViewModel: OrgDocDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            val folderId = navBackStackEntry.arguments?.getString("folderId")
            Log.d("folderId passed id","$folderId")
            LaunchedEffect(empID,org) {
                folderId?.let {
                    orgDocDetailViewModel.getOrgDocDetailDetails(
                        navController = navController,
                        context = context,
                        empId = empID,
                        org = org,
                        folderId = it.toInt()
                    )
                }
            }
            OrgDoc(navController = navController, orgDocDetailViewModel = orgDocDetailViewModel)
        }*/

        composable(Screen.Wedding.route) { navBackStackEntry ->

            val weddingViewModel: WeddingDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()
            LaunchedEffect(empID,org) {
                weddingViewModel.getWeddingDetails(
                    navController = navController,
                    context = context,
                    empId = empID,
                    org = org,
                )
            }
            Wedding(navController = navController, weddingViewModel = weddingViewModel)
        }
        composable(Screen.OnLeave.route) { navBackStackEntry ->
            val onLeaveViewModel: OnLeaveViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )
            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()
            LaunchedEffect(empID, org) {
                onLeaveViewModel.getOnLeaveDetails(
                    navController=navController,
                    context = context,
                    empId = empID,
                    org = org,
                )
            }
            OnLeave(navController = navController, onLeaveViewModel = onLeaveViewModel)
        }

        composable(Screen.OnRW.route) { navBackStackEntry ->

            val onRWViewModel: OnRWViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                onRWViewModel.getOnRWDetails(navController=navController, context = context, empId = empID, org = org)
            }

            OnRWList(navController = navController, onRWViewModel = onRWViewModel)
        }

        composable(Screen.OnDutyList.route) { navBackStackEntry ->

            val onDutyListViewModel: OnDutyListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                onDutyListViewModel.getOnDutyListDetails(navController=navController, context = context, empId = empID, org = org)
            }

            OnDutyList(navController = navController, onDutyViewModel = onDutyListViewModel)
        }

        composable(Screen.Work.route) { navBackStackEntry ->

            val workViewModel: WorkDetailViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
               workViewModel.getWorkDetails(navController=navController, context = context, empId = empID, org = org)
            }

            Work(navController = navController, workViewModel = workViewModel)

        }

        composable(Screen.Holiday.route) {navBackStackEntry ->

            val holidayViewModel : HolidayViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val year by remember { mutableStateOf(currentYear) }

            LaunchedEffect(empID, year) {
                holidayViewModel.getHolidayDetails(navController, context, empID, year.toString())
            }

            Holiday(navController = navController, holidayViewModel = holidayViewModel)

        }

        composable(Screen.Approval.route) {navBackStackEntry ->

            val regularizedApprovalViewModel : RegularizedApprovalViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val onDutyViewModel : OnDutyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val wfhViewModel: WorkFromHomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val leaveViewModel: LeaveViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            regularizedApprovalViewModel.getRegularizedDetails(navController,context,empID)
            onDutyViewModel.getOnDutyApprovalDetails(navController,context,empID, org.toString())
            wfhViewModel.getWFHApprovalDetails(navController,context,empID,org.toString())
            leaveViewModel.getLeaveApprovalList(navController, context, empId = empID)

            Approval(navController = navController, regularizedApprovalViewModel = regularizedApprovalViewModel, onDutyViewModel = onDutyViewModel, wfhViewModel = wfhViewModel, leaveViewModel = leaveViewModel)
        }

        composable(Screen.ApprovalList.route) {navBackStackEntry ->

            val approvalViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                approvalViewModel.getApprovalDetails(navController = navController, context = context, empId = empID, org = org)
            }

            approvalList(navController = navController, approvalViewModel = approvalViewModel)
        }


        composable(Screen.Notify.route) {
            Notify(navController = navController)
        }

        composable(Screen.ChangePassword.route) { navBackStackEntry ->

            val changepasswordViewModel: ChangePasswordView =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            Change_password(navController = navController, viewModel = mainViewModel, changepasswordViewModel = changepasswordViewModel)
        }

        composable(Screen.Leave.route) {navBackStackEntry ->

            val leaveViewModel: LeaveViewModel= androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID)
            {
                leaveViewModel.getLeaveHistory(navController,context,empId = empID)
            }

            Leave(navController = navController, leaveViewModel = leaveViewModel)
        }

        composable(Screen.LeaveApprovals.route + "/{empName}/{slNo}/{empId}") {navBackStackEntry ->

            val slNo1 = navBackStackEntry.arguments?.getString("slNo") as String
            val empId1 = navBackStackEntry.arguments?.getString("empId") as String
            val empName1 = navBackStackEntry.arguments?.getString("empName") as String


            val leaveViewModel: LeaveViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            leaveViewModel.getLeaveApprovalListDetails(navController,context,empId1,slNo1)

            Leave_approvals(navController = navController,leaveViewModel = leaveViewModel, slNo = slNo1, empId = empId1, empName = empName1)
        }

        composable(Screen.LeaveHistory.route) {navBackStackEntry ->

            val leaveViewModel: LeaveViewModel= androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID)
            {
                leaveViewModel.getLeaveHistory(navController,context,empId = empID)
            }

            LeaveHistory(navController = navController, leaveViewModel = leaveViewModel)

        }

        composable(Screen.LeaveHistoryDetail.route+ "/{slNo}/{empId}/{balance}") {navBackStackEntry ->

            val slNo1 = navBackStackEntry.arguments?.getString("slNo") as String
            val empId1 = navBackStackEntry.arguments?.getString("empId") as String
            val balance = navBackStackEntry.arguments?.getString("balance") as String

            val leaveViewModel: LeaveViewModel= androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            Log.d("Inside composable", "slNo1/empId1 : $slNo1/$empId1")

            leaveViewModel.getLeaveHistoryDetail(navController,context, empId1, slNo1)

            LeaveHistoryDetail(navController = navController, leaveViewModel = leaveViewModel, slNo = slNo1, empId = empId1, balance = balance)

        }

        composable(Screen.LeaveApply.route) {navBackStackEntry ->

            val leaveViewModel: LeaveViewModel=
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID)
            {
                leaveViewModel.getLeaveDetails(navController = navController, context = context,empId = empID)
            }

            LeaveApply(navController = navController, leaveViewModel = leaveViewModel)
        }

        //New Leave Details

        composable(Screen.ApplyLeaveNew.route) {navBackStackEntry ->

            val leaveViewModel: LeaveViewModel=
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID)
            {
                leaveViewModel.getLeaveDetails1(navController = navController, context = context, empId = empID)
            }

            ApplyLeaveNew(navController = navController, leaveViewModel = leaveViewModel)
        }


        composable(Screen.RegularizedApproval.route) {navBackStackEntry ->

            val regularizedApprovalViewModel : RegularizedApprovalViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID) {
                regularizedApprovalViewModel.getRegularizedDetails(navController,context,empID)
            }

            RegularizedApproval(navController = navController, regularizedApprovalViewModel = regularizedApprovalViewModel)
        }

        composable(Screen.LeaveListApproval.route) {navBackStackEntry ->

            val leaveViewModel: LeaveViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()

            LaunchedEffect(empID) {
                leaveViewModel.getLeaveApprovalList(navController, context, empId = empID)
            }

            LeaveListApproval(navController = navController, leaveViewModel = leaveViewModel)
        }


        composable(Screen.CompoOffApproval.route) {navBackStackEntry ->
            val approvalViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID, org) {
                approvalViewModel.getCompoOffApprovalDetails(navController = navController, context = context, empId = empID, org = org.toString())
            }

            CompoOffApproval(navController = navController, approvalListViewModel = approvalViewModel)
        }

        composable(Screen.CompoOffApprovalDetail.route + "/{date}/{empId}/{empName}") {navBackStackEntry ->

            val date = navBackStackEntry.arguments?.getString("date") as String
            val empId = navBackStackEntry.arguments?.getString("empId") as String
            val empName = navBackStackEntry.arguments?.getString("empName") as String

            val approvalViewModel: ApprovalListViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            CompoOffApprovalDetail(navController = navController, approvalListViewModel = approvalViewModel, date = date, empId = empId, empName = empName)
        }

        composable(Screen.ODApproval.route) {navBackStackEntry ->

            val onDutyViewModel : OnDutyViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID) {
                onDutyViewModel.getOnDutyApprovalDetails(navController,context,empID, org.toString())
            }

            ODApproval(navController = navController,  onDutyViewModel = onDutyViewModel)
        }

        composable(Screen.OTApproval.route) {navBackStackEntry ->
            val overTimeViewModel : OverTimeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID) {
                overTimeViewModel.getOverTimeApprovalDetails(navController,context,empID, org.toString())
            }

            OTApproval(navController = navController,  overTimeViewModel = overTimeViewModel)
        }

        composable(Screen.WFHApproval.route) {navBackStackEntry ->
            val wfhViewModel: WorkFromHomeViewModel =
                androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SavedStateViewModelFactory(
                        repository = provideInjectedNavigationEntryPoint().repository,
                        savedStateRegistryOwner = navBackStackEntry
                    )
                )

            val empID = userViewModel.getSFCode()
            val org = userViewModel.getOrg()

            LaunchedEffect(empID) {
                wfhViewModel.getWFHApprovalDetails(navController,context,empID,org.toString())
            }

            WFHApproval(navController = navController, wfhViewModel = wfhViewModel)
        }

        composable(Screen.TeamAttendance.route) {
            TeamAttendance(navController = navController, viewModel = mainViewModel)
        }

        composable(Screen.TeamAttendanceDetailView.route) {
            teamAttendanceDetailView(navController = navController)
        }

        composable(Screen.CheckInLocation.route) {
            CheckInLocation(navController = navController, context)
        }

        composable(Screen.Currentlocation.route) {
            Currentlocation(navController = navController, context)
        }

        composable(Screen.ShiftSelection.route) {
            ShiftSelection(navController = navController, context)
        }

        composable(Screen.Login.route) {navBackStackEntry ->
            val profileViewModel : ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = SavedStateViewModelFactory(
                    repository = provideInjectedNavigationEntryPoint().repository,
                    savedStateRegistryOwner = navBackStackEntry
                )
            )
            Login(true)
            { username, password ->
                onLoginClicked(username, password, context, mainViewModel, profileViewModel, lifecycleOwner, navController)
            }
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicy(navController = navController,viewModel = mainViewModel)
        }

    }











}

fun getStartDestination(isLoggedIn: Boolean,privacyKey: Boolean): String {

    return if(privacyKey)
    {
        if (isLoggedIn)
        {
            Screen.HomeScreen.route
        }
        else
        {
            Screen.Login.route
        }
    }
    else
    {
        Screen.PrivacyPolicy.route
    }

}




@Suppress("DEPRECATION", "DEPRECATION")
@RequiresApi(Build.VERSION_CODES.S)
fun isFakeGPSDetected(context: Context, navController: NavController) {

    Log.d("Location", "Inside isFakeGPSDetected")

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    Log.d("isFakeGPSDetected", "isMockLocation_0: ${isMockLocation.value}")


    @Suppress("DEPRECATION")
    fun checkIsMockLocation() {
        if (!Constant.isNetworkAvailable(context)) {
            Constant.showToast(context, "Please check your network connection")
            navController.navigate("${Screen.Network.route}/HomeScreen")
        } else {
            val locationProvider = LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest().setInterval(2000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationProvider.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                             lats = location.latitude
                             longs = location.longitude


//                            isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1

                            Log.d("Inside Launch Effect" ,"isDeveloperEnabled : ${isDeveloperEnabled.value}")

/*
                            if(isDeveloperEnabled.value)
                            {
                                isMockLocation.value = location.isFromMockProvider
                            }
*/

                            isMockLocation.value = location.isFromMockProvider


                            Log.d("isFakeGPSDetected", "isMockLocation_1: ${isMockLocation.value}")
                            Log.d("isFakeGPSDetected...", "latitude : $lats,   longitude : $longs")
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }
    }

    if (!isLocationEnabled) {
        Constant.showToast(context, "Please turn on the location")
        Log.d("Location", "Location services not enabled. Launching settings activity.")

        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

        try {
            context.startActivity(intent)
            Log.d("Location", "Settings activity launched successfully.")
        } catch (e: Exception) {
            Log.e("Location", "Error launching settings activity: ${e.message}")
        }
    }

    Handler(Looper.getMainLooper()).postDelayed({
        checkIsMockLocation()
    }, 1000)


    Log.d("isFakeGPSDetected", "isMockLocation_2: ${isMockLocation.value}")

//    return isMockLocation.value

}

private lateinit var injectedNavigationEntryPoint: InjectedNavigationEntryPoint

@Composable
fun provideInjectedNavigationEntryPoint(): InjectedNavigationEntryPoint {
    if (!::injectedNavigationEntryPoint.isInitialized) {
        injectedNavigationEntryPoint = EntryPoints.get(
            LocalContext.current.applicationContext,
            InjectedNavigationEntryPoint::class.java
        )
    }

    return injectedNavigationEntryPoint
}


//NETWORK CONTINOUS CHECKING


/*

    LaunchedEffect(Unit) {

        var wasConnected = Constant.isNetworkAvailable(context)

        // Get the current destination
        val currentDestination = navController.currentDestination?.route.toString()
        val currentDestinationLabel = navController.currentDestination?.label


        Log.d("Current Screen" ,"currentDestination/currentDestinationLabel : $currentDestination/$currentDestinationLabel")

        // Simulated network connection monitoring

        while (true) {
            delay(1000) // Adjust the delay as needed
            val currentDestination = navController.currentDestination?.route.toString()
            val isConnectedNow = Constant.isNetworkAvailable(context)

        //TOAST DISPLAYED WHILE THE SCREEN IS WAKE UP

            if (!isConnected.value && isConnectedNow) {
                isConnected.value = true
                Constant.showToast(context, "Internet is connected, please refresh once...!")
            }
            isConnected.value = isConnectedNow

       //TOAST DISPLAYED ONLY WHEN THE INTERNET WAS DISCONNECTED AND CONNECTED

            if (!wasConnected && isConnectedNow) {
                wasConnected = true
                Constant.showToast(context, "Internet is connected, please refresh once...!")
            } else if (wasConnected && !isConnectedNow) {
                wasConnected = false
            }

        }
    }

*/
