package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyData
import com.payroll.twogrowhr.Model.ResponseModel.RegularizeData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLeaveData
import com.payroll.twogrowhr.Model.ResponseModel.WFHData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import com.payroll.twogrowhr.viewModel.OnDutyViewModel
import com.payroll.twogrowhr.viewModel.RegularizedApprovalViewModel
import com.payroll.twogrowhr.viewModel.WorkFromHomeViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale







@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Approval(
    navController: NavController,
    regularizedApprovalViewModel: RegularizedApprovalViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel,
    leaveViewModel: LeaveViewModel,
) {

    val isLoggedIn = remember { mutableStateOf(true) }

    Constant.AppTheme {
        AppScaffold1(
            topBarContent = { TopBarBackNavigation(
                navController = navController,
                title = "Approvals",
                "approvallist"
            ) },
            bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
            onBack = { navController.navigateUp() }
        )
        {
            Approval_Screen(
            navController = navController,
            regularizedApprovalViewModel = regularizedApprovalViewModel,
            onDutyViewModel = onDutyViewModel,
            wfhViewModel = wfhViewModel,
            leaveViewModel = leaveViewModel)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Approval_Screen(
    navController: NavController,
    regularizedApprovalViewModel: RegularizedApprovalViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel,
    leaveViewModel: LeaveViewModel,
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp)

    ){
        Leave_app(
            navController = navController,
            regularizedApprovalViewModel = regularizedApprovalViewModel,
            onDutyViewModel = onDutyViewModel,
            wfhViewModel = wfhViewModel,
            leaveViewModel = leaveViewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Leave_app(
    navController: NavController,
    regularizedApprovalViewModel: RegularizedApprovalViewModel,
    onDutyViewModel: OnDutyViewModel,
    wfhViewModel: WorkFromHomeViewModel,
    leaveViewModel: LeaveViewModel,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val employeeID = userViewModel.getSFCode()
    val orgId = userViewModel.getOrg()

    Column {

        PrimaryTabRow(selectedTabIndex = selectedTabIndex, contentColor = Color.Black, modifier = Modifier.fillMaxWidth())
        {

            Tab(selected = selectedTabIndex==0, onClick = { selectedTabIndex=0 },modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Leave", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.themeColor))
            }

            //REGULARIZATION
            Tab(selected = selectedTabIndex==1, onClick = { selectedTabIndex=1 }, modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Regularize", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.themeColor))
            }

            //ON DUTY
            Tab(selected = selectedTabIndex==2, onClick = { selectedTabIndex=2 }, modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "On Duty", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.themeColor))
            }

            //WORK FROM HOME
            Tab(selected = selectedTabIndex==3, onClick = { selectedTabIndex=3 }, modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Remote Work", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.themeColor))
            }

        }


        when (selectedTabIndex)
        {

            0->
            {
                leaveViewModel.fetchAndUpdateLeaveData(navController,context,employeeID)
                TabOnLeave(context, leaveViewModel, employeeID, navController = navController)
            }

            1->
            {
                regularizedApprovalViewModel.fetchAndUpdateData(navController,context, employeeID)
                TabRegularization(navController, context, regularizedApprovalViewModel, employeeID)
            }

            2->
            {
                onDutyViewModel.fetchAndUpdateOnDutyData(navController,context,employeeID,orgId.toString())
                TabOnDuty(navController, context, onDutyViewModel, employeeID)
            }

            3->
            {
                wfhViewModel.fetchAndUpdateWFHData(navController, context, employeeID, orgId.toString())
                TabOnWFH(navController,context, wfhViewModel, employeeID)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabRegularization(
    navController: NavController,
    context: Context,
    regularizedApprovalViewModel: RegularizedApprovalViewModel,
    employeeID: String,
) {



    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxSize()
        .padding(16.dp)
    ) {

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        val loadingStatus = regularizedApprovalViewModel.loadingStatus

        flag = regularizedApprovalViewModel.flag

        var loading by remember { mutableStateOf(false) }

        val maxLineLength = 30 // Maximum number of characters to display before truncating

        if(loading)
        {
            linearProgression()
        }

        regularizedApprovalViewModel.regularizedList.collectAsState().also {
            regularizedList1 = it as MutableState<List<RegularizeData>>
        }

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = regularizedApprovalViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }

//LOGIC TO DISPLAY THE LIST
        @Suppress("DEPRECATION")
        @Composable
        fun uiUpdateRegularize()
        {
            var customDialog by remember { mutableStateOf(false) }

            LazyColumn {
                Log.d("Approval... Regularize", "Inside true DataList:${regularizedList1.value}")

                if(regularizedList1.value.isEmpty())
                {
                    loading = true
                }
                else
                {
                    items(regularizedList1.value) { data ->
                        var isExpanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.backgroundColor))
                        ) {

                            val checkInTime = data.Checkin_Time
                            val checkOutTime = data.Checkout_Time
                            val attendanceDate = data.Attendance_Date
                            val remarks = data.Remark
                            val slNo = data.sl_no
                            val employeeName = data.Name

                            // Split the string using space as a delimiter
                            val inTimeParts = checkInTime.split(" ")
                            val outTimeParts = checkOutTime.split(" ")

                            // The last element of the parts array contains the time
                            val inTime = inTimeParts.last()
                            val outTime = outTimeParts.last()

                            val inputFormat = SimpleDateFormat("MMM dd yyyy hh:mma", Locale.US)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

                            val date = inputFormat.parse(attendanceDate)
                            val formattedAttendanceDate = date?.let { dateFormat.format(it) }

                            var showDialog by remember { mutableStateOf(false) }


                            @Composable
                            fun RejectDialogBox()
                            {

                                val maxLength = 250

                                Dialog(onDismissRequest = {
                                    showDialog = false
                                    customDialog = false
                                    reasonForReject = ""
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
                                                onValueChange = {if (it.length <= maxLength)
                                                    reasonForReject = it },
                                                modifier = Modifier
                                                    .padding(
                                                        bottom = 10.dp,
                                                        start = 10.dp,
                                                        end = 10.dp
                                                    )
                                                    .fillMaxWidth()
                                                    .heightIn(min = 70.dp)
                                                    .background(color = colorResource(id = R.color.white)),
                                                maxLines = 5,
                                                textStyle = MaterialTheme.typography.titleMedium,
                                                placeholder = { Text(text = "Type...") },
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


                            if (showDialog) {

                                Constant.AppTheme {
                                    AlertDialog(
                                        onDismissRequest = {
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

                                                    regularizedList1.value = emptyList()

                                                    // Perform the approval or rejection logic here

                                                    when (approvalAction) {
                                                        1 -> {
                                                            showDialog = false // Close the dialog
                                                            reasonForReject = "" // Reset the reasonForReject
                                                            regularizedApprovalViewModel.postRegularizeStatusUpdate(navController, employeeID, slNo.toString(), approvalAction.toString(), context)
                                                        }
                                                        2 -> {
                                                            showDialog = false // Close the dialog
                                                            reasonForReject = "" // Reset the reasonForReject
                                                            regularizedApprovalViewModel.postRegularizeStatusUpdate(navController, employeeID, slNo.toString(), approvalAction.toString(), context)                                                    }
                                                        else -> {
                                                            reasonForReject = "" // Reset the reasonForReject
                                                            showDialog = false // Close the dialog for other cases
                                                        }
                                                    }
                                                    approvalAction = 0 // Reset the approval action
                                                    reasonForReject = "" // Reset the reasonForReject
                                                    regularizedApprovalViewModel.fetchAndUpdateData(navController, context, employeeID)

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


                            if(customDialog)
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
                                            if (employeeName.length > 17) {

                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = employeeName,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = employeeName.take(17) + "..." ,
//                                                        text = employeeName.take(17) + if (employeeName.length > 17) "..." else "",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier
                                                            .tooltipTrigger()
                                                            .align(Alignment.Start)
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = employeeName,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 15.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row( horizontalArrangement = Arrangement.SpaceBetween)
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
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            //Data Row

                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {

                                    Row(
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    )
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Text(
                                                text = data.Emp_Code,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row( horizontalArrangement = Arrangement.SpaceBetween)
                                            {
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = inTime,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
                                                }
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = outTime,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
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
                                        text = "Remarks",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Row( horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            if (formattedAttendanceDate != null)
                                            {
                                                Text(
                                                    text = formattedAttendanceDate,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }
                                    }
                                }
                            }


                            //For Remarks

                            Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = colorResource(
                                                id = R.color.backgroundColor
                                            )
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                            .clickable {
                                                // Toggle the expanded state and tooltip visibility
                                                isExpanded = !isExpanded
                                            },
                                    ) {
                                        Box( modifier = Modifier.fillMaxWidth())
                                        {
                                            Text(
                                                text = if (isExpanded) remarks else remarks.take(
                                                    maxLineLength
                                                ) + if (remarks.length > maxLineLength) "..." else "",
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                            )
                                        }
                                    }
                                }
                            }

                            // Action Button

                            Row( modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 10.dp
                                ))
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
//                                        customDialog = true
                                        showDialog = true
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

            if(regularizedList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        regularizedApprovalViewModel.fetchAndUpdateData(navController, context, employeeID)
                    }
                    2 -> {
                        noDataView()
                    }
                    3 -> {
                        exceptionScreen()
                    }
                    else -> {
                        Constant.showToast(context,"Please try again later...!")
                    }
                }

            }
            else
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        uiUpdateRegularize()
                    }
                    2 -> {
                        noDataView()
                    }

                    3 -> {
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TabOnDuty(
    navController: NavController,
    context: Context,
    onDutyViewModel: OnDutyViewModel,
    employeeID: String,
) {



    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxSize()
        .padding(16.dp)
    ) {

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        val loadingStatus = onDutyViewModel.loadingStatus

        flag = onDutyViewModel.flag

        var loading by remember { mutableStateOf(false) }

        val maxLineLength = 30 // Maximum number of characters to display before truncating

        if(loading)
        {
            linearProgression()
        }

        onDutyViewModel.onDutyApprovalList.collectAsState().also {
            onDutyApprovalDataList1 = it as MutableState<List<OnDutyData>>
        }

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = onDutyViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }


//LOGIC TO DISPLAY THE LIST

        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateOnDuty()
        {
            var customDialog by remember { mutableStateOf(false) }

            LazyColumn {
                Log.d("Approval... ON DUTY", "Inside true DataList:${onDutyApprovalDataList1.value}")

                items(onDutyApprovalDataList1.value) { data ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.backgroundColor))
                    ) {

//                        val onDutyDateTime  = data.onDutyDt
                        val onDutyDate = data.onDutyDate
                        val ruleId = data.ruleId
                        val employeeName = data.empName
                        val empId = data.emp
                        val remarks = data.onDutyRemarks
                        val session = data.dayType

                        // Parse the input date string
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        val date = LocalDateTime.parse(onDutyDate, formatter)

                        // Format the date as "06 Nov 2023"
                        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String


                        val org = userViewModel.getOrg()


                        var showDialog by remember { mutableStateOf(false) }


                        @Composable
                        fun RejectDialogBox()
                        {

                            val maxLength = 250

                            Dialog(onDismissRequest = {
                                showDialog = false
                                customDialog = false
                                reasonForReject = ""
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
                                            onValueChange =  {if (it.length <= maxLength)
                                                reasonForReject = it },
                                            modifier = Modifier
                                                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                                .fillMaxWidth()
                                                .heightIn(min = 70.dp)
                                                .background(color = colorResource(id = R.color.white)),
                                            maxLines = 5,
                                            textStyle = MaterialTheme.typography.titleMedium,
                                            placeholder = { Text(text = "Type...") },
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

                                                onDutyApprovalDataList1.value = emptyList()

                                                // Perform the approval or rejection logic here

                                                when (approvalAction)
                                                {
                                                    1 -> {
                                                        showDialog = false // Close the dialog
                                                        onDutyViewModel.postOnDutyStatusUpdate(navController, empId, ruleId, formattedApiDate, org.toString(), approvalAction.toString(), reasonForReject, context )
                                                    }
                                                    2 -> {
                                                        onDutyViewModel.postOnDutyStatusUpdate(navController, empId, ruleId, formattedApiDate, org.toString(), approvalAction.toString(), reasonForReject, context)
                                                        showDialog = false
                                                        reasonForReject = ""
                                                    }
                                                    else -> {
                                                        showDialog = false // Close the dialog for other cases
                                                        reasonForReject = "" // Reset the reasonForReject
                                                    }
                                                }
                                                approvalAction = 0 // Reset the approval action
                                                reasonForReject = "" // Reset the reasonForReject
                                                onDutyViewModel.fetchAndUpdateOnDutyData(navController,context,employeeID,org.toString())

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

                        if(customDialog)
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
                                        if (employeeName.length > 17) {
                                            PlainTooltipBox(
                                                tooltip = {
                                                    Text(
                                                        text = employeeName,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight(500),
                                                        ),
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    text = employeeName.take(17) + "..." ,
//                                                    text = employeeName.take(17) + if (employeeName.length > 17) "..." else "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier
                                                        .tooltipTrigger()
                                                        .align(Alignment.Start)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = employeeName,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 15.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                        }
                                    }


                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Row( horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "Session",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        //Data Row

                        val sessionText = when (session) {
                            "Full Day" -> "Full Day"
                            "Half Day- First Half" -> "First Half"
                            "Half Day-Second Half" -> "Second Half"
                            else -> "--"
                        }

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            Column(modifier = Modifier.fillMaxWidth())
                            {

                                Row(
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = data.empCode,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.paraColor)
                                            ),
                                            modifier = Modifier.align(Alignment.Start)
                                        )
                                    }


                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Row( horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = sessionText,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
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
                                    text = "Remarks",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.paraColor)
                                    ),
                                    modifier = Modifier.align(Alignment.Start)
                                )
                            }


                            Column(modifier = Modifier.weight(1f))
                            {
                                Row( horizontalArrangement = Arrangement.SpaceBetween)
                                {
                                    Column(modifier = Modifier.weight(0.5f))
                                    {
                                    }

                                    Column(modifier = Modifier.weight(2f))
                                    {
                                        if (formattedDate != null)
                                        {

                                            Text(
                                                text = formattedDate,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        //For Remarks

                        Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                        {
                            Column(modifier = Modifier.fillMaxWidth())
                            {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorResource(
                                            id = R.color.backgroundColor
                                        )
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Start)
                                        .clickable {
                                            // Toggle the expanded state and tooltip visibility
                                            isExpanded = !isExpanded
                                        },
                                ) {
                                    Box( modifier = Modifier.fillMaxWidth())
                                    {
                                        Text(
                                            text = if (isExpanded) remarks else remarks.take(
                                                maxLineLength
                                            ) + if (remarks.length > maxLineLength) "..." else "",
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                        )
                                    }
                                }
                            }
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
        }


// LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(onDutyApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
//                        Log.d("Approval..","On Duty list is empty.... flag is 0")
                        loading = true
                    }
                    1 -> {
//                        Log.d("Approval..","On Duty list is empty.... but flag is 1")
                        onDutyViewModel.fetchAndUpdateOnDutyData(navController, context, employeeID, userViewModel.getOrg().toString())
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
                        uiUpdateOnDuty()
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TabOnWFH(
    navController: NavController,
    context: Context,
    wfhViewModel: WorkFromHomeViewModel,
    employeeID: String,
) {



    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.white))
        .fillMaxSize()
        .padding(16.dp)
    ) {

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        val loadingStatus = wfhViewModel.loadingStatus

        flag = wfhViewModel.flag

        var loading by remember { mutableStateOf(false) }

        val maxLineLength = 30 // Maximum number of characters to display before truncating

        if(loading)
        {
            linearProgression()
        }

        wfhViewModel.wfhApprovalList.collectAsState().also {
            wfhApprovalDataList1 = it as MutableState<List<WFHData>>
        }

//FOR RECEIVED APPROVAL RESPONSE



        val loadingCircular = wfhViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }


        //LOGIC TO DISPLAY THE LIST
        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateWFH()
        {
            var customDialog by remember { mutableStateOf(false) }

            LazyColumn {
                Log.d("Approval... WFH", "Inside true DataList:${wfhApprovalDataList1.value}")

                items(wfhApprovalDataList1.value) { data ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.backgroundColor))
                    ) {

                        val wfhDate = data.wfhDate
                        val ruleId = data.ruleId
                        val employeeName = data.empName
                        val empId = data.emp
                        val remarks = data.wfhRemarks
                        val session = data.dayType

                        // Parse the input date string
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        val date = LocalDateTime.parse(wfhDate, formatter)

                        // Format the date as "06 Nov 2023"
                        val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String


                        val org = userViewModel.getOrg()


                        var showDialog by remember { mutableStateOf(false) }

                        val maxLength = 250
                        @Composable
                        fun RejectDialogBox()
                        {
                            Dialog(onDismissRequest = {
                                showDialog = false
                                customDialog = false
                                reasonForReject = ""
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
                                            onValueChange = {
                                                if (it.length <= maxLength)
                                                    reasonForReject = it },
                                        modifier = Modifier
                                                .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                                .fillMaxWidth()
                                                .heightIn(min = 70.dp)
                                                .background(color = colorResource(id = R.color.white)),
                                            maxLines = 5,
                                            textStyle = MaterialTheme.typography.titleMedium,
                                            placeholder = { Text(text = "Type...") },
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


                        if (showDialog) {

                            Constant.AppTheme {
                                AlertDialog(
                                    onDismissRequest = {
                                        showDialog = false
                                        approvalAction = 0 // Reset the approval action
                                        reasonForReject = ""
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

                                                wfhApprovalDataList1.value = emptyList()

                                                // Perform the approval or rejection logic here
                                                when (approvalAction)
                                                {
                                                    1 -> {
                                                        showDialog = false // Close the dialog
                                                        wfhViewModel.postWFHStatusUpdate(navController, empId, ruleId, formattedApiDate, org.toString(), approvalAction.toString(), reasonForReject, context )
                                                    }
                                                    2 -> {
                                                        showDialog = false // Close the dialog
                                                        wfhViewModel.postWFHStatusUpdate(navController, empId, ruleId, formattedApiDate, org.toString(), approvalAction.toString(), reasonForReject, context )
                                                        reasonForReject = "" // Reset the reasonForReject
                                                    }
                                                    else -> {
                                                        showDialog = false // Close the dialog for other cases
                                                        reasonForReject = "" // Reset the reasonForReject
                                                    }
                                                }
                                                approvalAction = 0 // Reset the approval action
                                                reasonForReject = "" // Reset the reasonForReject
                                                wfhViewModel.fetchAndUpdateWFHData(navController, context, employeeID,org.toString())
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
                                                reasonForReject = "" // Reset the reasonForReject

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

                        if(customDialog)
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
                                        if (employeeName.length > 17) {
                                            PlainTooltipBox(
                                                tooltip = {
                                                    Text(
                                                        text = employeeName,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight(500),
                                                        ),
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    text = employeeName.take(17) + "..." ,
//                                                    text = employeeName.take(17) + if (employeeName.length > 17) "..." else "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier
                                                        .tooltipTrigger()
                                                        .align(Alignment.Start)
                                                )
                                            }
                                        } else {
                                            Text(
                                                text = employeeName,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 15.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                        }
                                    }


                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Row( horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "Session",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        //Data Row

                        val sessionText = when (session) {
                            "Full Day" -> "Full Day"
                            "Half Day- First Half" -> "First Half"
                            "Half Day-Second Half" -> "Second Half"
                            else -> "--"
                        }

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            Column(modifier = Modifier.fillMaxWidth())
                            {

                                Row(
                                    modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )
                                {

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = data.empCode,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.paraColor)
                                            ),
                                            modifier = Modifier.align(Alignment.Start)
                                        )
                                    }


                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Row( horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = "",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Text(
                                                    text = sessionText,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
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
                                    text = "Remarks",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp, fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.paraColor)
                                    ),
                                    modifier = Modifier.align(Alignment.Start)
                                )
                            }


                            Column(modifier = Modifier.weight(1f))
                            {
                                Row( horizontalArrangement = Arrangement.SpaceBetween)
                                {
                                    Column(modifier = Modifier.weight(0.5f))
                                    {
                                    }

                                    Column(modifier = Modifier.weight(2f))
                                    {
                                        if (formattedDate != null)
                                        {

                                            Text(
                                                text = formattedDate,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        }
                                    }
                                }
                            }
                        }


                        //For Remarks

                        Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                        {
                            Column(modifier = Modifier.fillMaxWidth())
                            {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorResource(
                                            id = R.color.backgroundColor
                                        )
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Start)
                                        .clickable {
                                            // Toggle the expanded state and tooltip visibility
                                            isExpanded = !isExpanded
                                        },
                                ) {
                                    Box( modifier = Modifier.fillMaxWidth())
                                    {
                                        Text(
                                            text = if (isExpanded) remarks else remarks.take(
                                                maxLineLength
                                            ) + if (remarks.length > maxLineLength) "..." else "",
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                        )
                                    }
                                }
                            }
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
        }


// LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(wfhApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        wfhViewModel.fetchAndUpdateWFHData(navController, context, employeeID, userViewModel.getOrg().toString())
                    }
                    2 -> {
                        noDataView()
                    }
                    3 -> {
                        exceptionScreen()
                    }
                    else -> {
                        Constant.showToast(context,"Please try again later...!")
                    }
                }

            }
            else
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        uiUpdateWFH()
                    }
                    2 -> {
                        noDataView()
                    }
                    3 -> {
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




@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TabOnLeave(
    context: Context,
    leaveViewModel: LeaveViewModel,
    employeeID: String,
    navController: NavController,
) {

    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.backgroundColor))
        .fillMaxSize()
        .padding(16.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        val loadingStatus = leaveViewModel.loadingStatus

        flag = leaveViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        leaveViewModel.leaveApprovalList.collectAsState().also {
            leaveApprovalDataList1 = it as MutableState<List<UnApproveLeaveData>>
        }

//FOR RECEIVED APPROVAL RESPONSE



        val loadingCircular = leaveViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }


        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLeave()
        {

            LazyColumn {

                Log.d("Approval... Leave", "Inside true DataList:${leaveApprovalDataList1.value}")

                if(leaveApprovalDataList1.value.isEmpty())
                {
                    loading = true
                }
                else
                {
                    items(leaveApprovalDataList1.value) { data ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp)
                                .clickable {
//                                    leaveViewModel.fetchAndUpdateLeaveDetailData(data.slNo,data.empId)
                                    navController.navigate("${Screen.LeaveApprovals.route}/${data.empName}/${data.slNo}/${data.empId}")
                                },
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            val employeeName = data.empName
                            val leaveType = data.leaveTypeName
                            val leavePeriod = data.leavePeriod
                            val appliedDays = data.count


                            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)


                            val dateParts = leavePeriod.split(" - ")



                            // Parse start and end dates
                            val startDate = inputFormat.parse(dateParts[0])
                            val endDate = inputFormat.parse(dateParts[1])


                            // Format the dates
                            val formattedStartDate = startDate?.let { dateFormat.format(it) }
                            val formattedEndDate = endDate?.let { dateFormat.format(it) }


                            // First Row

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {

                                //First column
                                Column(modifier = Modifier.weight(1f))
                                {

                                    //NAME ROW

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            if (employeeName.length > 17) {

                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = employeeName,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = employeeName.take(17) + "..." ,
//                                                        text = employeeName.take(17) + if (employeeName.length > 17) "..." else "",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier
                                                            .tooltipTrigger()
                                                            .align(Alignment.Start)
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = employeeName,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 15.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }
                                    }

                                    //EMP CODE

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Text(
                                                text = data.empCode,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                        }
                                    }
                                }


                                //Second column
                                Column(modifier = Modifier.weight(1f))
                                {

                                    //From Date ROW

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row(modifier = Modifier.fillMaxSize())
                                            {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "From",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.themeColor)
                                                        ),
                                                    )
                                                }

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = formattedStartDate.toString(),
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    //EMP CODE

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row(modifier = Modifier.fillMaxSize())
                                            {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "To",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.themeColor)
                                                        ),
                                                    )
                                                }

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = formattedEndDate.toString(),
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                    )
                                                }
                                            }
                                        }

                                    }
                                }

                            }


                            //Divider

                            Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top =5.dp, bottom = 5.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }


                            // Second Row


                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {

                                //First column
                                Column(modifier = Modifier.weight(1f))
                                {

                                    //Leave Title

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Text(
                                                text = "Leave Type",
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                                modifier = Modifier.align(Alignment.Start)
                                            )
                                        }
                                    }

                                    //Leave Type

                                    Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            if (leaveType.length > 17) {

                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = leaveType,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 15.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = leaveType.take(17) + "..." ,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier
                                                            .tooltipTrigger()
                                                            .align(Alignment.Start)
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = leaveType,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 15.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.align(Alignment.Start)
                                                )
                                            }
                                        }

                                    }
                                }


                                //Second column
                                Column(modifier = Modifier.weight(1f))
                                {
                                    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween)
                                    {

                                        Column(modifier = Modifier.weight(1.5f))
                                        {

                                            //Applied Days Title

                                            Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                            {
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = "Applied Duration",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
                                                }
                                            }

                                            //Applied Days Count

                                            Row(Modifier.padding(top =5.dp, bottom = 5.dp))
                                            {
                                                Column(modifier = Modifier.weight(1f))
                                                {
                                                    Text(
                                                        text = appliedDays,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        modifier = Modifier.align(Alignment.Start)
                                                    )
                                                }
                                            }

                                        }

                                        Column(modifier = Modifier.weight(0.5f))
                                        {
                                            Row(Modifier.padding(top = 5.dp, bottom = 5.dp))
                                            {
                                                Column()
                                                {
                                                    IconButton(onClick = { navController.navigate("${Screen.LeaveApprovals.route}/${data.empName}/${data.slNo}/${data.empId}") }
                                                    ) {
                                                        Icon(
                                                            modifier = Modifier
                                                                .size(size = 25.dp)
                                                                .align(Alignment.Start),
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
                            }
                        }
                    }
                }
            }
        }

// LOGIC TO DISPLAY THE UI


        // LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(leaveApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        leaveViewModel.fetchAndUpdateLeaveData(navController,context,employeeID)
                    }
                    2 -> {
                        noDataView()
                    }
                    3 -> {
                        exceptionScreen()
                    }
                    else -> {
                        Constant.showToast(context,"Please try again later...!")
                    }
                }

            }
            else
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        uiUpdateLeave()
                    }
                    2 -> {
                        noDataView()
                    }

                    3 -> {
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