package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltipBox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.RegularizeData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.RegularizedApprovalViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

var regularizedList1 = mutableStateOf<List<RegularizeData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegularizedApproval(navController: NavController, regularizedApprovalViewModel: RegularizedApprovalViewModel ) {
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Regularized Approvals", "approvallist") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { RegularizedApproval_Screen(navController = navController,regularizedApprovalViewModel = regularizedApprovalViewModel) }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun RegularizedApproval_Screen(navController: NavController, regularizedApprovalViewModel: RegularizedApprovalViewModel )
{
    val context = LocalContext.current

    val employeeID = userViewModel.getSFCode()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
    ) {
        var customDialog by remember { mutableStateOf(false) }
        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        flag = regularizedApprovalViewModel.flag

        var loading by remember { mutableStateOf(false) }

        val maxLineLength = 30 // Maximum number of characters to display before truncating

     /*   if(loading)
        {
            linearProgression()
        }*/

        regularizedApprovalViewModel.regularizedList.collectAsState().also {
            regularizedList1 = it as MutableState<List<RegularizeData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            regularizedApprovalViewModel.getRegularizedDetails(navController, context, employeeID)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = regularizedApprovalViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }

        Box(Modifier.pullRefresh(state)) {

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
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
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
                                                    color = colorResource(id = R.color.white),
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
                                                    regularizedApprovalViewModel.fetchAndUpdateData(navController,context, employeeID)

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
                                                id = R.color.white
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

            PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

        }



    }
}

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun uiRegularizeApprovalListPreview() {

    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Regularized Approvals", "approvallist") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {

        val regularizedList = generateRegularizeApprovalList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 0.dp, start = 10.dp, end = 10.dp)
        ) {


            LazyColumn {

                items(regularizedList) { data ->
                    val isExpanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {

                        val checkInTime = data.Checkin_Time
                        val checkOutTime = data.Checkout_Time
                        val attendanceDate = data.Attendance_Date
                        val remarks = data.Remark
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
                                            id = R.color.white
                                        )
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Start)
                                        .clickable { },
                                ) {
                                    Box( modifier = Modifier.fillMaxWidth())
                                    {
                                        Text(
                                            text = if (isExpanded) remarks else remarks.take(15) + if (remarks.length > 15) "..." else "",
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
                                onClick = { },
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
    }
}

fun generateRegularizeApprovalList(): List<RegularizeData>
{

    return  listOf(
        RegularizeData(Name = "Priya", Remark = "firctckut", Emp_Id = "EMP12755", Emp_Code = "INF007", Attendance_Date = "Apr 29 2024 12:00AM",  Checkin_Time = "Apr 29 2024 12:42AM", Checkout_Time = "Apr 29 2024  6:42PM", sl_no = 858),
        RegularizeData(Name = "Priya", Remark = "firctckut", Emp_Id = "EMP12755", Emp_Code = "INF007", Attendance_Date = "Apr 23 2024 12:00AM",  Checkin_Time = "Apr 23 2024 09:40AM", Checkout_Time = "Apr 29 2024 6:30PM", sl_no = 840)
    )

}
