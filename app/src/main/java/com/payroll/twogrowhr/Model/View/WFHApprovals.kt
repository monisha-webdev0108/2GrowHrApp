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
import com.payroll.twogrowhr.Model.ResponseModel.WFHData
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
import com.payroll.twogrowhr.viewModel.WorkFromHomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var wfhApprovalDataList1 = mutableStateOf<List<WFHData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WFHApproval(
    navController: NavController,
    wfhViewModel: WorkFromHomeViewModel,
) {
    val isLoggedIn = remember { mutableStateOf(true) }
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Work From Home ", "approvallist") },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { WFHApproval_Screen(navController = navController,wfhViewModel = wfhViewModel) }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun WFHApproval_Screen(navController: NavController, wfhViewModel: WorkFromHomeViewModel) {
    val context = LocalContext.current

    val employeeID = userViewModel.getSFCode()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
    ) {

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        var reasonForReject by remember { mutableStateOf("") }

        val loadingStatus = wfhViewModel.loadingStatus

        flag = wfhViewModel.flag

        var loading by remember { mutableStateOf(false) }

        val maxLineLength = 30 // Maximum number of characters to display before truncating

        val org = userViewModel.getOrg()

        if(loading)
        {
            linearProgression()
        }

        wfhViewModel.wfhApprovalList.collectAsState().also {
            wfhApprovalDataList1 = it as MutableState<List<WFHData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            wfhViewModel.fetchAndUpdateWFHData(navController, context, employeeID, org.toString())
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

//FOR RECEIVED APPROVAL RESPONSE



        val loadingCircular = wfhViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }


        //LOGIC TO DISPLAY THE LIST
        @RequiresApi(Build.VERSION_CODES.O)
        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateWFH()
        {

            var selectedItemIndex by remember { mutableStateOf(-1) }

            var customDialog by remember { mutableStateOf(false) }

            Box(Modifier.pullRefresh(state)) {


                LazyColumn {
                    Log.d("Approval... WFH", "Inside true DataList:${wfhApprovalDataList1.value}")

                    items(wfhApprovalDataList1.value) { data ->
                        var isExpanded by remember { mutableStateOf(false) }

                        val index = wfhApprovalDataList1.value.indexOf(data)


                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            val wfhDate = data.wfhDate
                            val ruleId = data.ruleId
                            val employeeName = data.empName
                            val empId = data.emp
                            val remarks = data.wfhRemarks
                            val session = data.dayType

                            // Parse the input date string
                            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                            } else {
                                TODO("VERSION.SDK_INT < O")
                            }
                            val date = LocalDateTime.parse(wfhDate, formatter)

                            // Format the date as "06 Nov 2023"
                            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String


                            var showDialog by remember { mutableStateOf(false) }

                            val maxLength = 250
                            @Composable
                            fun RejectDialogBox()
                            {
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
                                            reasonForReject = ""
                                            selectedItemIndex = -1 // Reset the selected item index
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

                                                    selectedItemIndex = -1 // Reset the selected item index
                                                    approvalAction = 0 // Reset the approval action
                                                    reasonForReject = "" // Reset the reasonForReject
                                                    wfhViewModel.fetchAndUpdateWFHData(navController, context, employeeID, org.toString())
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
                                                    selectedItemIndex = -1 // Reset the selected item index
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



@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun uiWFHApprovalListPreview() {

    val navController = rememberNavController()

    val flag = 1

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Work From Home ", "approvallist") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        val wfhApprovalDataList = generateWFHApprovalList()

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 0.dp, start = 10.dp, end = 10.dp)
        ) {


//FOR GETTING RESULT

            val maxLineLength = 30 // Maximum number of characters to display before truncating

            //LOGIC TO DISPLAY THE LIST
            @RequiresApi(Build.VERSION_CODES.O)
            @Suppress("DEPRECATION")
            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateWFH()
            {
                LazyColumn {
                    Log.d("Approval... WFH", "Inside true DataList:${wfhApprovalDataList1.value}")

                    items(wfhApprovalDataList) { data ->

                        val isExpanded by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            val wfhDate = data.wfhDate
                            val employeeName = data.empName
                            val remarks = data.wfhRemarks
                            val session = data.dayType

                            // Parse the input date string
                            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                            } else {
                                TODO("VERSION.SDK_INT < O")
                            }
                            val date = LocalDateTime.parse(wfhDate, formatter)

                            // Format the date as "06 Nov 2023"
                            val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

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
                                                id = R.color.white
                                            )
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                            .clickable {   },
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


            when (flag)
            {
                1 -> {
                    uiUpdateWFH()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }

}


fun generateWFHApprovalList(): List<WFHData>
{

    return  listOf(
        WFHData(wfhDate = "2024-01-12T00:00:00", wfhDt = "12/01/2024", empCode = "2610", ruleId = "70", ruleName = "WORK FROM HOME WITH LEAVE BASED APPROVAL", empName = "Rangaraja", emp = "EMP13877", wfhRemarks = "wfh", dayType = "Full Day"),
        WFHData(wfhDate = "2024-01-02T00:00:00", wfhDt = "02/01/2024", empCode = "EMP619", ruleId = "49", ruleName = "Work from home request", empName = "Rahul", emp = "EMP619", wfhRemarks = "auto approves", dayType = "Full Day")
    )

}
