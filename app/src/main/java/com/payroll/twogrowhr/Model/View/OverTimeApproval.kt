package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OverTimeData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.OverTimeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
var overTimeApprovalDataList1 = mutableStateOf<List<OverTimeData>>(emptyList())
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OTApproval(
    navController: NavController,
    overTimeViewModel: OverTimeViewModel
){
    val isLoggedIn = remember { mutableStateOf(true) }
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Over Time Approvals",
            "approvallist"
        )},
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { OTApproval_Screen(navController = navController, overTimeViewModel = overTimeViewModel) }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OTApproval_Screen(navController: NavController, overTimeViewModel: OverTimeViewModel) {

/*
    var flag by remember { mutableIntStateOf(0) }

    flag = overTimeViewModel.flag

    val loading by remember { mutableStateOf(false) }

    if(loading)
    {
        linearProgression()
    }

    overTimeViewModel.overTimeApprovalList.collectAsState().also {
        overTimeApprovalDataList1 = it as MutableState<List<OverTimeData>>
    }
*/

    val context = LocalContext.current

    val employeeID = userViewModel.getSFCode()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
        .fillMaxSize()

    ) {

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

        val org = userViewModel.getOrg()

        //FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        val loadingStatus = overTimeViewModel.loadingStatus

        flag = overTimeViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        overTimeViewModel.overTimeApprovalList.collectAsState().also {
            overTimeApprovalDataList1 = it as MutableState<List<OverTimeData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            overTimeViewModel.fetchAndUpdateOverTimeData(navController,context,employeeID,org.toString())
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

        //FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = overTimeViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }

        //LOGIC TO DISPLAY THE LIST

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateOverTime()
        {
            var selectedItemIndex by remember { mutableStateOf(-1) }
            var customDialog by remember { mutableStateOf(false) }

            Box(Modifier.pullRefresh(state)) {

                LazyColumn {
                    Log.d("Approval... ON DUTY", "Inside true DataList:${overTimeApprovalDataList1.value}")

                    items(overTimeApprovalDataList1.value) { data ->
                        var isExpanded by remember { mutableStateOf(false) }
                        val index = overTimeApprovalDataList1.value.indexOf(data)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            val employeeName = data.empName
                            val empCode = data.empCode
                            val shiftStartTime = data.shiftStartTime
                            val shiftEndTime = data.shiftEndTime
                            val inTime = data.checkin_time
                            val outTime = data.checkout_time
                            val overTimeHours = data.workingHours
                            val ratePerHour = data.rate_per_hr
                            val extraHour = data.extra_Hrs
                            val extraHourAmount = data.hrs

                            // Data to be passed

                            val empId = data.empId
                            val hours= data.hrs.toFloat()
                            val checkin  = data.checkin_time
                            val checkout  = data.checkout_time
                            val weekStart  = data.weekStart
                            val weekEnd  = data.weekEnd
                            val month  = data.month
                            val year  = data.year
                            val ratePerHourNationalHolidayOT  = data.otHoli_rate_per_hr_National.toFloat()
                            val maxHourFestivalHolidayOT  = data.otMax_hr_holi_Fastival.toFloat()
                            val ratePerHourWeeklyOT  = data.otWkend_per_hr_Weekly.toFloat()
                            val isMaximumHoursAllowedRequestOT  = data.isMaximumHoursAllowedReq
                            val maxHoursLimitOT  = data.maxHoursLimit
                            val adjustDurationOT  = data.adjustduration
                            val isBalanceFlowReq = data.isBalanceFlowReq
                            val balanceLap = data.balanceLaps
                            val isApprovalFlowReq = data.isApprovalFlowReq
                            val approval = data.approval
                            val isHoursExceeds = data.is_HoursExceeds
                            val hoursExceeds = data.hoursExceeds
                            val durationOff = data.durationOff.toString()
                            val goToApproval = data.goToApproval
                            val ratePerHourToSent = data.rate_per_hr.toFloat()
                            val extraHourToSent = data.extra_Hrs.toFloat()

                            val paidAmount = ratePerHour*extraHourAmount
                            /* ----------------------------------------------------------------------------  Time Formatting AM-PM --------------------------------------------------------------------------*/
                            val shfStartTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalTime.parse(shiftStartTime.substring(0, 8))
                            } else {throw RuntimeException("Unsupported SDK version") }

                            val shfEndTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalTime.parse(shiftEndTime.substring(0, 8))
                            } else { throw RuntimeException("Unsupported SDK version") }

                            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                            val outputFormat = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")

                            val inTimeFormat = LocalDateTime.parse(inTime, inputFormat)
                            val inTimeFormatString = inTimeFormat.format(outputFormat)

                            val outTimeFormat = LocalDateTime.parse(outTime, inputFormat)
                            val outTimeFormatString = outTimeFormat.format(outputFormat)
                            // Format the time in 12-hour format with AM/PM
                            val formatter12Hour  = DateTimeFormatter.ofPattern("hh:mm a")
                            val shfStartTimeFormatted = shfStartTime.format(formatter12Hour )
                            val shfEndTimeFormatted = shfEndTime.format(formatter12Hour )

                            Log.d("inTimeFormatString", inTimeFormatString)
                            Log.d("outTimeFormatString", outTimeFormatString)
                            /* ----------------------------------------------------------------------------  Time Formatting AM-PM --------------------------------------------------------------------------*/
                            // Parse the input date string
                            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                            } else {
                                TODO("VERSION.SDK_INT < O")
                            }
                            Log.d("formatter","$formatter")
//                        val date = LocalDateTime.parse(onDutyDate, formatter)
                            // Format the date as "06 Nov 2023"
                            /* val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                             val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String
     */

                            var showDialog by remember { mutableStateOf(false) }

                            @Composable
                            fun RejectDialogBox()
                            {
                                val maxLength = 250
                                Dialog(onDismissRequest = {
                                    showDialog = false
                                    customDialog = false
                                    reasonForReject = ""
                                    selectedItemIndex = -1 // Reset the selected item index
                                })
                                {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        // text and buttons
                                        Column(
                                            modifier = Modifier
                                                .padding(top = 20.dp) // this is the empty space at the top
                                                .fillMaxWidth()
                                                .background(
                                                    color = colorResource(id = R.color.backgroundColor),
                                                    shape = RoundedCornerShape(percent = 10)
                                                ),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Spacer(modifier = Modifier.height(height = 36.dp))

                                            Text(text = "Reject Reason", color = colorResource(id = R.color.black), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 10.dp))

                                            Row(modifier = Modifier
                                                .padding(10.dp)
                                                .fillMaxWidth())
                                            {
                                                Text(
                                                    text = "Reason", style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.paraColor),
                                                )
                                            }

                                            TextField(
                                                value = reasonForReject,
                                                onValueChange =  {if (it.length <= maxLength)reasonForReject = it },
                                                modifier = Modifier
                                                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                                    .fillMaxWidth()
                                                    .heightIn(min = 70.dp)
                                                    .background(color = colorResource(id = R.color.white)),
                                                maxLines = 5,
                                                textStyle = MaterialTheme.typography.titleMedium,
                                                placeholder = { Text(text = "Enter Reason...") },
                                                singleLine = false,
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    cursorColor = colorResource(id = R.color.black),
                                                    focusedBorderColor = colorResource(id = R.color.themeColor),
                                                    unfocusedBorderColor = colorResource(R.color.white),
                                                )
                                            )

                                            // buttons
                                            Button(onClick = {
                                                if(reasonForReject == "" || reasonForReject.isBlank())
                                                {
                                                    Constant.showToast(context, "Enter Reason")
                                                    reasonForReject = ""
                                                }
                                                else
                                                {
                                                    showDialog = true
                                                    customDialog = false
                                                }

                                            }, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                                Text(text = "Reject", color = colorResource(id = R.color.white), fontSize = 14.sp )
                                            }
                                        }
                                    }
                                }
                            }

                            if (showDialog)
                            {
                                Constant.AppTheme {
                                    AlertDialog(
                                        onDismissRequest = {
                                            selectedItemIndex = -1 // Reset the selected item index
                                            showDialog = false
                                            approvalAction = 0 // Reset the approval action
                                            reasonForReject = "" // Reset the reasonForReject
                                        },
                                        title = {
                                            Text(
                                                text = "Confirmation",
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 17.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                            )
                                        },
                                        text = {
                                            Text(
                                                text = "Are you sure you want to ${if (approvalAction == 1) "approve" else "reject"} this?",
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                            )
                                        },
                                        confirmButton = {
                                            Button(
                                                onClick = {

                                                    overTimeApprovalDataList1.value = emptyList()

                                                    // Perform the approval or rejection logic here

                                                    when (approvalAction)
                                                    {
                                                        1 -> {
                                                            showDialog = false // Close the dialog
//
                                                            overTimeViewModel.postOverTimeStatusUpdate(navController,context, empId, org,
                                                                hours, ratePerHourToSent, approvalAction, checkin, checkout, weekStart, weekEnd, month, year, ratePerHourNationalHolidayOT, maxHourFestivalHolidayOT, ratePerHourWeeklyOT, extraHourToSent, isMaximumHoursAllowedRequestOT, maxHoursLimitOT, adjustDurationOT, isBalanceFlowReq,balanceLap,isApprovalFlowReq,approval,isHoursExceeds,hoursExceeds,durationOff, goToApproval ,reasonForReject)
                                                        }
                                                        2 -> {

                                                            overTimeViewModel.postOverTimeStatusUpdate(navController,context, empId, org,
                                                                hours, ratePerHourToSent, approvalAction, checkin, checkout, weekStart, weekEnd, month, year, ratePerHourNationalHolidayOT, maxHourFestivalHolidayOT, ratePerHourWeeklyOT, extraHourToSent, isMaximumHoursAllowedRequestOT, maxHoursLimitOT, adjustDurationOT, isBalanceFlowReq,balanceLap,isApprovalFlowReq,approval,isHoursExceeds,hoursExceeds,durationOff, goToApproval ,reasonForReject)

                                                            showDialog = false
                                                            reasonForReject = ""
                                                        }
                                                        else -> {
                                                            showDialog = false // Close the dialog for other cases
                                                            reasonForReject = "" // Reset the reasonForReject
                                                        }
                                                    }
                                                    selectedItemIndex = -1
                                                    approvalAction = 0 // Reset the approval action
                                                    reasonForReject = "" // Reset the reasonForReject
                                                    overTimeViewModel.fetchAndUpdateOverTimeData(navController,context,employeeID,org.toString())

                                                },
                                            ) {
                                                Text(
                                                    text = "OK",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.white)
                                                    ),
                                                )
                                            }
                                        },
                                        dismissButton = {
                                            Button(
                                                onClick = {
                                                    showDialog = false // Close the dialog
                                                    approvalAction = 0 // Reset the approval action
                                                },
                                            ) {
                                                Text(
                                                    text = "Cancel",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.white)
                                                    ),
                                                )
                                            }
                                        }
                                    )

                                }
                            }

                            if(customDialog && selectedItemIndex == index)
                            {
                                RejectDialogBox()
                            }


                            //Title Row

                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {

                                    Row(
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {

                                                BasicTextField(
                                                    readOnly = true,
                                                    value ="$employeeName" ,
                                                    onValueChange = { /* Handle value change if needed */ },
                                                    textStyle = TextStyle(
                                                        fontSize = 14.sp,
                                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    singleLine = true,
                                                )
                                                Text(
                                                    text = empCode,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier
                                                        .align(Alignment.Start)
                                                        .padding(top = 4.dp)
                                                )

                                        }
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row( horizontalArrangement = Arrangement.SpaceBetween)
                                            {
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = "Shift time",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
                                                    Box(modifier = Modifier
                                                        .padding(top = 4.dp)
                                                        .background(
                                                            colorResource(id = R.color.lightshade),
                                                            shape = RoundedCornerShape(5.dp)
                                                        )
                                                    ){
                                                        Text(
                                                            text = "$shfStartTimeFormatted - $shfEndTimeFormatted",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.lightthemecolor)
                                                            ),
                                                            modifier = Modifier
                                                                .align(Alignment.Center)
                                                                .padding(
                                                                    top = 5.dp,
                                                                    bottom = 5.dp,
                                                                    start = 8.dp,
                                                                    end = 8.dp
                                                                )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            //Divider

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }


                            //For Remarks Title

                            Row(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "In time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = inTimeFormatString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }


                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Out time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = outTimeFormatString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }
                            }
                            //Divider

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }
                            Row(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "OT hours(HH:MM:SS)",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = overTimeHours,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }


                                /*Column(modifier = Modifier.weight(1f))
                                {
                                    Row(modifier = Modifier.align(Alignment.Start)) {
                                        Text(
                                            text = "Rate Per Hour(in Rs)",
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.paraColor)),
                                            )
                                    }

                                    Text(
                                        text = "$ratePerHour ",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                }*/
                            }

                            // Action Button
                            Row( modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp))
                            {
                                Button(
                                    onClick = {
                                        showDialog = true
                                        approvalAction = 1 // Set approval action to 1 (Approve)
                                    },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = "Approve",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        ),
                                    )
                                }

                                Button(
                                    onClick = {


                                        selectedItemIndex = index
                                        customDialog = true
                                        approvalAction = 2 // Set approval action to 2 (reject)
                                    },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "Reject", style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

            }


        }


// LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(overTimeApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
//                        Log.d("Approval..","On Duty list is empty.... flag is 0")
                        loading = true
                    }
                    1 -> {
//                        Log.d("Approval..","On Duty list is empty.... but flag is 1")
                        overTimeViewModel.fetchAndUpdateOverTimeData(navController,context,employeeID, userViewModel.getOrg().toString())
                    }
                    2 -> {
//                        Log.d("Approval..","On Duty list is empty.... flag is 2")
                        noDataView()
                    }
                    3 -> {
//                        Log.d("Approval..","On Duty list is empty.... flag is 3")
                        exceptionScreen()
                    }
                    else -> {
//                        Log.d("Approval..","On Duty list is empty.... flag is else")
                        Constant.showToast(context,"Please try again later...!")
                    }
                }

            }
            else
            {
                when (flag)
                {
                    0 -> {
//                        Log.d("Approval..","On Duty list is not empty.... flag is 0")
                        loading = true
                    }
                    1 -> {
//                        Log.d("Approval..","On Duty list is not empty.... flag is 1")
                        uiUpdateOverTime()
                    }
                    2 -> {
//                        Log.d("Approval..","On Duty list is not empty.... flag is 2")
                        noDataView()
                    }

                    3 -> {
//                        Log.d("Approval..","On Duty list is not empty.... flag is 3")
                        exceptionScreen()
                    }
                    else -> {
                        Constant.showToast(context,"Please try again later...!")
                    }
                }
            }
        }
    }
}


@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiOTApprovalListPreview() {

    val navController = rememberNavController()

    val flag = 1


    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Over Time Approvals", "approvallist")},
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {

        val overTimeApprovalDataList = generateOTApprovalList()

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 0.dp, start = 10.dp, end = 10.dp)
            .fillMaxSize()

        ) {

            //LOGIC TO DISPLAY THE LIST

            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateOverTime()
            {

                LazyColumn {

                    items(overTimeApprovalDataList) { data ->


                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            val employeeName = data.empName
                            val empCode = data.empCode
                            val shiftStartTime = data.shiftStartTime
                            val shiftEndTime = data.shiftEndTime
                            val inTime = data.checkin_time
                            val outTime = data.checkout_time
                            val overTimeHours = data.workingHours


                            /* ----------------------------------------------------------------------------  Time Formatting AM-PM --------------------------------------------------------------------------*/
                            val shfStartTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalTime.parse(shiftStartTime.substring(0, 8))
                            } else {throw RuntimeException("Unsupported SDK version") }

                            val shfEndTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalTime.parse(shiftEndTime.substring(0, 8))
                            } else { throw RuntimeException("Unsupported SDK version") }

                            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                            val outputFormat = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")

                            val inTimeFormat = LocalDateTime.parse(inTime, inputFormat)
                            val inTimeFormatString = inTimeFormat.format(outputFormat)

                            val outTimeFormat = LocalDateTime.parse(outTime, inputFormat)
                            val outTimeFormatString = outTimeFormat.format(outputFormat)
                            // Format the time in 12-hour format with AM/PM
                            val formatter12Hour  = DateTimeFormatter.ofPattern("hh:mm a")
                            val shfStartTimeFormatted = shfStartTime.format(formatter12Hour )
                            val shfEndTimeFormatted = shfEndTime.format(formatter12Hour )

                            Log.d("inTimeFormatString", inTimeFormatString)
                            Log.d("outTimeFormatString", outTimeFormatString)
                            /* ----------------------------------------------------------------------------  Time Formatting AM-PM --------------------------------------------------------------------------*/


                            //Title Row

                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {

                                    Row(
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            BasicTextField(
                                                readOnly = true,
                                                value = employeeName,
                                                onValueChange = { /* Handle value change if needed */ },
                                                textStyle = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                                singleLine = true,
                                            )
                                            Text(
                                                text = empCode,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier
                                                    .align(Alignment.Start)
                                                    .padding(top = 4.dp)
                                            )

                                        }
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row( horizontalArrangement = Arrangement.SpaceBetween)
                                            {
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = "Shift time",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
                                                    Box(modifier = Modifier
                                                        .padding(top = 4.dp)
                                                        .background(
                                                            colorResource(id = R.color.lightshade),
                                                            shape = RoundedCornerShape(5.dp)
                                                        )
                                                    ){
                                                        Text(
                                                            text = "$shfStartTimeFormatted - $shfEndTimeFormatted",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.lightthemecolor)
                                                            ),
                                                            modifier = Modifier
                                                                .align(Alignment.Center)
                                                                .padding(top = 5.dp, bottom = 5.dp, start = 8.dp, end = 8.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            //Divider

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }


                            //For Remarks Title

                            Row(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "In time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = inTimeFormatString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }


                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Out time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = outTimeFormatString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }
                            }
                            //Divider

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }
                            Row(
                                modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            )
                            {
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "OT hours(HH:MM:SS)",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                    Text(
                                        text = overTimeHours,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                }

                            }

                            // Action Button
                            Row( modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp))
                            {
                                Button(
                                    onClick = {  },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = "Approve",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        ),
                                    )
                                }

                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "Reject", style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

            }



            when (flag)
            {
                1 -> {
                    uiUpdateOverTime()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }

}


fun generateOTApprovalList(): List<OverTimeData>
{

    return  listOf(
        OverTimeData(empCode = "MGRM1015", empId = "EMP302", empName = "K.V.S.SANDILYA", checkin_time = "2024-03-04 09:00:00.000", checkout_time = "2024-03-04 23:00:00.000", shiftStartTime = "09:00:00.0000000", shiftEndTime = "18:00:00.0000000", hr_per_day = 3, dd = 5,  shiftdiff = 540,  checkdiff = 840, rate_per_hr = 100, otHoli_rate_per_hr_National = 400,  otMax_hr_holi_Fastival = 300, hrs = 5.0, workingHours = "05:00:00", isMaximumHoursAllowedReq = 0, maxHoursLimit = 0, adjustduration = 0, isMinimumHoursReq = 0, minLimithours = 0, otWkend_per_hr_Weekly = 200, isBalanceFlowReq = 0, balanceLaps = 0, extra_Hrs = 5, isApprovalFlowReq = 1, approval = 4, is_HoursExceeds = 0, hoursExceeds = 0, durationOff = 0, goToApproval = "0", month = 3,  year = 2024, weekStart = "2024-03-03 09:00:00.000", weekEnd = "2024-03-09 09:00:00.000"),
        OverTimeData(empCode = "MGRM1015", empId = "EMP302", empName = "K.V.S.SANDILYA", checkin_time = "2024-03-05 09:00:00.000", checkout_time = "2024-03-05 20:00:00.000", shiftStartTime = "09:00:00.0000000", shiftEndTime = "18:00:00.0000000", hr_per_day = 3, dd = 2,  shiftdiff = 540,  checkdiff = 660, rate_per_hr = 100, otHoli_rate_per_hr_National = 400,  otMax_hr_holi_Fastival = 300, hrs = 2.0, workingHours = "02:00:00", isMaximumHoursAllowedReq = 0, maxHoursLimit = 0, adjustduration = 0, isMinimumHoursReq = 0, minLimithours = 0, otWkend_per_hr_Weekly = 200, isBalanceFlowReq = 0, balanceLaps = 0, extra_Hrs = 2, isApprovalFlowReq = 1, approval = 4, is_HoursExceeds = 0, hoursExceeds = 0, durationOff = 0, goToApproval = "0", month = 3,  year = 2024, weekStart = "2024-03-03 09:00:00.000", weekEnd = "2024-03-09 09:00:00.000"),
        OverTimeData(empCode = "MGRM1015", empId = "EMP302", empName = "K.V.S.SANDILYA", checkin_time = "2024-03-06 09:00:00.000", checkout_time = "2024-03-06 21:00:00.000", shiftStartTime = "09:00:00.0000000", shiftEndTime = "18:00:00.0000000", hr_per_day = 3, dd = 3,  shiftdiff = 540,  checkdiff = 720, rate_per_hr = 100, otHoli_rate_per_hr_National = 400,  otMax_hr_holi_Fastival = 300, hrs = 3.0, workingHours = "03:00:00", isMaximumHoursAllowedReq = 0, maxHoursLimit = 0, adjustduration = 0, isMinimumHoursReq = 0, minLimithours = 0, otWkend_per_hr_Weekly = 200, isBalanceFlowReq = 0, balanceLaps = 0, extra_Hrs = 3, isApprovalFlowReq = 1, approval = 4, is_HoursExceeds = 0, hoursExceeds = 0, durationOff = 0, goToApproval = "0", month = 3,  year = 2024, weekStart = "2024-03-03 09:00:00.000", weekEnd = "2024-03-09 09:00:00.000"),
        OverTimeData(empCode = "MGRM1015", empId = "EMP302", empName = "K.V.S.SANDILYA", checkin_time = "2024-03-07 09:00:00.000", checkout_time = "2024-03-07 22:00:00.000", shiftStartTime = "09:00:00.0000000", shiftEndTime = "18:00:00.0000000", hr_per_day = 3, dd = 4,  shiftdiff = 540,  checkdiff = 780, rate_per_hr = 100, otHoli_rate_per_hr_National = 400,  otMax_hr_holi_Fastival = 300, hrs = 4.0, workingHours = "04:00:00", isMaximumHoursAllowedReq = 0, maxHoursLimit = 0, adjustduration = 0, isMinimumHoursReq = 0, minLimithours = 0, otWkend_per_hr_Weekly = 200, isBalanceFlowReq = 0, balanceLaps = 0, extra_Hrs = 4, isApprovalFlowReq = 1, approval = 4, is_HoursExceeds = 0, hoursExceeds = 0, durationOff = 0, goToApproval = "0", month = 3,  year = 2024, weekStart = "2024-03-03 09:00:00.000", weekEnd = "2024-03-09 09:00:00.000"),
    )

}


