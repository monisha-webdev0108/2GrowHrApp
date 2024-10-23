package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaveHistory(
    navController: NavController,
    leaveViewModel: LeaveViewModel,
) {
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave History", "leave") },
        bottomBarContent = {  },
        onBack = { navController.navigateUp() }
    )
    {
        LeaveHistory_Screen(navController = navController, leaveViewModel = leaveViewModel)
    }
}




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LeaveHistory_Screen(navController: NavController, leaveViewModel: LeaveViewModel)
{

    val context = LocalContext.current

    val employeeID = userViewModel.getSFCode()


    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.backgroundColor))
        .fillMaxSize()
//        .padding(16.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = leaveViewModel.loadingStatus2

        flag = leaveViewModel.flag2

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        leaveViewModel.leaveHistoryList.collectAsState().also {
            leaveHistoryDataList1 = it as MutableState<List<LeaveHistoryData>>
        }


        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            leaveViewModel.getLeaveHistory(navController,context,empId = employeeID)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)


        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLeaveHistory()
        {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            ) {


                Box(Modifier.pullRefresh(state)) {

                    LazyColumn (Modifier.fillMaxSize()) {


                        // Sort the list by descending order using the Created_Date
                        val sortedList = leaveHistoryDataList1.value.sortedByDescending {
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(it.fromDate)
                        }

                        if (!refreshing) {
                            items(sortedList)
                            {data ->


                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                        .clickable {
                                            navController.navigate("${Screen.LeaveHistoryDetail.route}/${data.slNo}/${data.empId}/${data.noOfDays}")
                                        },
                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                ) {



                                    val leaveTypeName = data.leaveTypeName
                                    val fromDate = data.fromDate
                                    val toDate = data.toDate
                                    val noOfDays = data.noOfDays
                                    val leaveUnit = data.leaveUnit
                                    val leaveStatus = data.leaveStatus

                                    val leaveType = if(leaveUnit == "Day") "Days" else "Hours"



                                    val bgColor = when (data.leaveStatus) {
                                        "Approved" -> R.color.toolight_green
                                        "Reject" -> R.color.toolight_red
                                        "Pending" -> R.color.toolight_themecolor
                                        "Partially Approved" -> R.color.light_bright_yellow
                                        "Cancelled" -> R.color.light_pink
                                        else -> R.color.white
                                    }

                                    val statusColor1 = when (data.leaveStatus) {
                                        "Approved" -> R.color.green
                                        "Reject" -> R.color.red
                                        "Pending" -> R.color.themeColor
                                        "Partially Approved" -> R.color.yellow
                                        "Cancelled" -> R.color.pink
                                        else -> R.color.white
                                    }


                                    val inputFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                    val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                                    val fromDateFormat = inputFormatter.parse(fromDate)
                                    val fromDateFormatted = outputFormatter.format(fromDateFormat!!)


                                    val toDateFormat = inputFormatter.parse(toDate)
                                    val toDateFormatted = outputFormatter.format(toDateFormat!!)

                                    Column(modifier = Modifier.padding( 5.dp))
                                    {
                                        Row(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 5.dp),horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            //Leave Date and Type

                                            Column(modifier = Modifier.padding(top = 2.dp, start = 5.dp)){

                                                //For Date
                                                Row(modifier = Modifier.align(Alignment.Start)){
                                                    Column {
                                                        Text(text = "$fromDateFormatted - $toDateFormatted",
                                                            style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                                                        )
                                                    }
                                                }



                                                //For Leave Name
                                                Row(modifier = Modifier.padding(top = 3.dp).align(Alignment.Start)){
                                                    Column {
                                                        if (leaveTypeName.length > 15)
                                                        {

                                                            PlainTooltipBox(
                                                                tooltip = {
                                                                    Text(
                                                                        text = leaveTypeName,
                                                                        style = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 13.sp,
                                                                            fontWeight = FontWeight(500),
                                                                        ),
                                                                    )
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = leaveTypeName.take(15) + "..." + " $noOfDays $leaveType",
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 12.sp,
                                                                        fontWeight = FontWeight(500),
                                                                        color = colorResource(id = R.color.paraColor)
                                                                    ),
                                                                    modifier = Modifier.tooltipTrigger()
                                                                )
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Text(
                                                                text = "$leaveTypeName $noOfDays $leaveType",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 12.sp, fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.paraColor)
                                                                ),
                                                            )
                                                        }
                                                    }
                                                }
                                            }

                                            //Leave Status

                                            Column(modifier = Modifier.padding(top = 2.dp, end = 5.dp))
                                            {


                                                Button(onClick = {
                                                    navController.navigate("${Screen.LeaveHistoryDetail.route}/${data.slNo}/${data.empId}/${data.noOfDays}")
                                                },
                                                    colors = ButtonDefaults.buttonColors(colorResource(id = bgColor)),
                                                    shape = RoundedCornerShape(20),
                                                    modifier = Modifier.align(Alignment.End)
                                                ) {


                                                    if (leaveStatus.length > 9) {

                                                        PlainTooltipBox(
                                                            tooltip = {
                                                                Text(
                                                                    text = leaveStatus,
                                                                    style = TextStyle(fontFamily = poppins_font, fontSize = 12.sp, fontWeight = FontWeight(500)),
                                                                )
                                                            }
                                                        ) {
                                                            Text(
                                                                text = leaveStatus.take(10) + "..." ,
//                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  Color(android.graphics.Color.parseColor(statusColor1))),
                                                                style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                                modifier = Modifier
                                                                    .padding(
                                                                        top = 2.dp,
                                                                        bottom = 2.dp,
                                                                        start = 4.dp,
                                                                        end = 4.dp
                                                                    )
                                                                    .tooltipTrigger())
                                                        }
                                                    } else {
                                                        Text(
                                                            text = leaveStatus,
                                                            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
                                                            style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
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

                    PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

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

            if(leaveHistoryDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        leaveViewModel.getLeaveHistory(navController,context,empId = employeeID)
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
                        uiUpdateLeaveHistory()
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


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLeaveHistoryListPreview() {

    val navController = rememberNavController()

    val leaveHistoryDataList = generateLeaveHistoryDataList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave History", "leave") },
        bottomBarContent = {  },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .background(color = colorResource(id = R.color.backgroundColor))
            .fillMaxSize()
        ) {

            val flag by remember { mutableIntStateOf(1) }

            val refreshScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }

            fun refresh() = refreshScope.launch {
                refreshing = true
                delay(1500)
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)


            @Suppress("DEPRECATION")
            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateLeaveHistory()
            {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.backgroundColor))
                        .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                ) {


                    Box(Modifier.pullRefresh(state)) {

                        LazyColumn (Modifier.fillMaxSize()) {


                            // Sort the list by descending order using the Created_Date
                            val sortedList = leaveHistoryDataList.sortedByDescending {
                                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(it.fromDate)
                            }

                            if (!refreshing) {
                                items(sortedList)
                                {data ->


                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 10.dp)
                                            .clickable {
                                                navController.navigate("${Screen.LeaveHistoryDetail.route}/${data.slNo}/${data.empId}/${data.noOfDays}")
                                            },
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                    ) {

                                        val leaveTypeName = data.leaveTypeName
                                        val fromDate = data.fromDate
                                        val toDate = data.toDate
                                        val noOfDays = data.noOfDays
                                        val leaveUnit = data.leaveUnit
                                        val leaveStatus = data.leaveStatus

                                        val leaveType = if(leaveUnit == "Day") "Days" else "Hours"



                                        val bgColor = when (data.leaveStatus) {
                                            "Approved" -> R.color.toolight_green
                                            "Reject" -> R.color.toolight_red
                                            "Pending" -> R.color.toolight_themecolor
                                            "Partially Approved" -> R.color.light_bright_yellow
                                            "Cancelled" -> R.color.light_pink
                                            else -> R.color.white
                                        }

                                        val statusColor1 = when (data.leaveStatus) {
                                            "Approved" -> R.color.green
                                            "Reject" -> R.color.red
                                            "Pending" -> R.color.themeColor
                                            "Partially Approved" -> R.color.yellow
                                            "Cancelled" -> R.color.pink
                                            else -> R.color.white
                                        }


                                        val inputFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                        val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                                        val fromDateFormat = inputFormatter.parse(fromDate)
                                        val fromDateFormatted = outputFormatter.format(fromDateFormat!!)


                                        val toDateFormat = inputFormatter.parse(toDate)
                                        val toDateFormatted = outputFormatter.format(toDateFormat!!)

                                        Column(modifier = Modifier.padding( 5.dp))
                                        {
                                            Row(modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 5.dp),horizontalArrangement = Arrangement.SpaceBetween)
                                            {

                                                //Leave Date and Type

                                                Column(modifier = Modifier.padding(top = 2.dp, start = 5.dp)){

                                                    //For Date
                                                    Row(modifier = Modifier.align(Alignment.Start)){
                                                        Column {
                                                            Text(text = "$fromDateFormatted - $toDateFormatted",
                                                                style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                                                            )
                                                        }
                                                    }



                                                    //For Leave Name
                                                    Row(modifier = Modifier.padding(top = 3.dp).align(Alignment.Start)){
                                                        Column {
                                                            if (leaveTypeName.length > 15)
                                                            {

                                                                PlainTooltipBox(
                                                                    tooltip = {
                                                                        Text(
                                                                            text = leaveTypeName,
                                                                            style = TextStyle(
                                                                                fontFamily = poppins_font,
                                                                                fontSize = 13.sp,
                                                                                fontWeight = FontWeight(500),
                                                                            ),
                                                                        )
                                                                    }
                                                                ) {
                                                                    Text(
                                                                        text = leaveTypeName.take(15) + "..." + " $noOfDays $leaveType",
                                                                        style = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            color = colorResource(id = R.color.paraColor)
                                                                        ),
                                                                        modifier = Modifier.tooltipTrigger()
                                                                    )
                                                                }
                                                            }
                                                            else
                                                            {
                                                                Text(
                                                                    text = "$leaveTypeName $noOfDays $leaveType",
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 12.sp, fontWeight = FontWeight(500),
                                                                        color = colorResource(id = R.color.paraColor)
                                                                    ),
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                //Leave Status

                                                Column(modifier = Modifier.padding(top = 2.dp, end = 5.dp))
                                                {


                                                    Button(onClick = {
                                                        navController.navigate("${Screen.LeaveHistoryDetail.route}/${data.slNo}/${data.empId}/${data.noOfDays}")
                                                    },
                                                        colors = ButtonDefaults.buttonColors(colorResource(id = bgColor)),
                                                        shape = RoundedCornerShape(20),
                                                        modifier = Modifier.align(Alignment.End)
                                                    ) {


                                                        if (leaveStatus.length > 9) {

                                                            PlainTooltipBox(
                                                                tooltip = {
                                                                    Text(
                                                                        text = leaveStatus,
                                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 12.sp, fontWeight = FontWeight(500)),
                                                                    )
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = leaveStatus.take(10) + "..." ,
                                                                    style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp).tooltipTrigger())
                                                            }
                                                        } else {
                                                            Text(
                                                                text = leaveStatus,
                                                                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
                                                                style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
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

                        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
                    }
                }
            }


            when (flag)
            {
                1 -> {
                    uiUpdateLeaveHistory()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }
}

fun generateLeaveHistoryDataList(): List<LeaveHistoryData>
{
    return listOf(
        LeaveHistoryData(createdDate = "05-02-2024", reason  = "going to function", leaveTypeName  = "Yearly ", fromDate  = "21-02-2024", lastUpdateDate  = "0", toDate  = "21-02-2024", noOfDays  = "", leaveStatus  = "Approved", statusColor  = "#d93649", rejectedReason  = "", leaveActiveFlag  = "1", slNo  = "1948", empId  = "EMP12754", leaveUnit  = "Day"),
        LeaveHistoryData(createdDate = "05-02-2024", reason  = "feeling not well", leaveTypeName  = "Yearly ", fromDate  = "17-02-2024", lastUpdateDate  = "1", toDate  = "17-02-2024", noOfDays  = "", leaveStatus  = "Reject", statusColor  = "#d93649", rejectedReason  = "25 APPROVED BY ADMIN", leaveActiveFlag  = "3", slNo  = "1949", empId  = "EMP12754", leaveUnit  = "Day"),
        LeaveHistoryData(createdDate = "05-02-2024", reason  = "feeling sick", leaveTypeName  = "Yearly ", fromDate  = "09-02-2024", lastUpdateDate  = "1", toDate  = "09-02-2024", noOfDays  = "", leaveStatus  = "Partially Approved", statusColor  = "#d93649", rejectedReason  = "", leaveActiveFlag  = "2", slNo  = "1945", empId  = "EMP12754", leaveUnit  = "Day"),
)
}

