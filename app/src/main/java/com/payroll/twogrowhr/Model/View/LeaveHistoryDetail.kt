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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryDetailData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


var leaveHistoryDetailDataList1 = mutableStateOf<List<LeaveHistoryDetailData>>(emptyList())


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaveHistoryDetail(navController: NavController, leaveViewModel: LeaveViewModel, slNo: String, empId: String, balance: String)
{

    Log.d("LeaveHistoryDetail", "slno/empId/reason : $slNo/$empId")

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave History", "leaveHistory") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        LeaveHistoryDetailScreen(
            navController = navController,
            leaveViewModel = leaveViewModel,
            slNo = slNo,
            empId = empId,
            balance = balance)
    }
}




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LeaveHistoryDetailScreen(
    navController: NavController,
    leaveViewModel: LeaveViewModel,
    slNo: String,
    empId: String,
    balance: String
)
{

    Log.d("LeaveHistoryDetailScreen", "slno/empId : $slNo/$empId")


    val context = LocalContext.current

    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.backgroundColor))
        .fillMaxSize()
        .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = leaveViewModel.loadingStatus3

        flag = leaveViewModel.flag3

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        leaveViewModel.leaveHistoryDetailList.collectAsState().also {
            leaveHistoryDetailDataList1 = it as MutableState<List<LeaveHistoryDetailData>>
        }


        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            leaveViewModel.getLeaveHistoryDetail(navController = navController, context, empId = empId, slNo = slNo)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)



        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLeaveHistoryDetail()
        {
            var isExpanded by remember { mutableStateOf(false) }

            Box(Modifier.pullRefresh(state))
            {

                LazyColumn (Modifier.fillMaxSize())
                {

                    if (!refreshing)
                    {
                        items(leaveHistoryDetailDataList1.value)
                        {data ->


                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                            ) {



                                val leaveTypeName = data.leaveTypeName
                                val fromDate = data.fromDate
                                val status = data.status
//                            val actionTakenBy = data.actionTakenBy.ifEmpty { "-" }
//                            val nextLevel = data.nextLevel
                                val approvedBy = data.approvedBy
                                val rejectedBy = data.rejectedBy
                                val reason = data.reason

                                val flag1 = data.status == "Reject" || data.status == "Cancelled"


                                val nextLevel = if(data.status == "Pending" && data.nextLevel.isEmpty()) "Admin" else data.nextLevel


                                val actionTakenBy = when (data.status) {
                                    "Approved" -> approvedBy
                                    "Reject" -> rejectedBy
                                    "Pending" -> "-"
                                    "Partially Approved" -> approvedBy
                                    "Cancelled" -> rejectedBy
                                    else -> ""
                                }



                                val unit = when(data.dayType){
                                    "Full Day" -> "(Full Day)"
                                    "1st Half" -> "(1st Half)"
                                    "2nd Half" -> "(2nd Half)"
                                    "Hour" -> "($balance Hours)"
                                    else -> ""
                                }


                                val nextLevelDisplay = if(nextLevel == "-") "" else nextLevel

                                val bgColor = when (data.status) {
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

                                Column(modifier = Modifier.fillMaxWidth().padding(10.dp)){

                                    //LeaveType and Date
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){

                                        //LeaveType
                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            Row(modifier = Modifier){
                                                Text(
                                                    text = "Leave type",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 11.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                )
                                            }

                                            Row(modifier = Modifier.padding(top = 10.dp)){
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
                                                            text = leaveTypeName.take(15) + "...",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                            modifier = Modifier.tooltipTrigger()
                                                        )
                                                    }
                                                }
                                                else
                                                {
                                                    Text(
                                                        text = leaveTypeName,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                    )
                                                }
                                            }

                                        }

                                        //Date
                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            Button(onClick = { },
                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_themecolor)),
                                                shape = RoundedCornerShape(20),
                                                modifier = Modifier.align(Alignment.End)
                                            ) {

                                                Text(
                                                    text = "$fromDateFormatted\n$unit",
                                                    modifier = Modifier.fillMaxWidth().padding(1.dp),
                                                    textAlign = TextAlign.Center,
                                                    style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(500),color = colorResource(id = R.color.themeColor)),
                                                )

                                            }

                                        }


                                    }


                                    //ActionTakenBy and NextLevel
                                    Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween){

                                        //ActionTakenBy
                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            Row(modifier = Modifier.align(Alignment.Start)){
                                                Text(
                                                    text = "Approve or Reject by",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 11.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                )
                                            }

                                            Row(modifier = Modifier.padding(top = 10.dp).align(Alignment.Start)){

                                                if (actionTakenBy.length > 15)
                                                {

                                                    PlainTooltipBox(
                                                        tooltip = {
                                                            Text(
                                                                text = actionTakenBy,
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight(500),
                                                                ),
                                                            )
                                                        }
                                                    ) {
                                                        Text(
                                                            text = actionTakenBy.take(15) + "...",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                            modifier = Modifier.tooltipTrigger()
                                                        )
                                                    }
                                                }
                                                else
                                                {
                                                    Text(
                                                        text = actionTakenBy,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                    )
                                                }
                                            }

                                        }

                                        //NextLevel
                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            if(nextLevelDisplay.isNotEmpty())
                                            {

                                                Row(modifier = Modifier.align(Alignment.End))
                                                {
                                                    Text(
                                                        text = "Next Level",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                    )
                                                }

                                                Row(modifier = Modifier.padding(top = 10.dp).align(Alignment.End))
                                                {
                                                    if (nextLevel.length > 15)
                                                    {

                                                        PlainTooltipBox(
                                                            tooltip = {
                                                                Text(
                                                                    text = nextLevel,
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 12.sp,
                                                                        fontWeight = FontWeight(500),
                                                                    ),
                                                                )
                                                            }
                                                        ) {
                                                            Text(
                                                                text = nextLevel.take(15) + "...",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.black)
                                                                ),
                                                                modifier = Modifier.tooltipTrigger()
                                                            )
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Text(
                                                            text = nextLevel,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                        )
                                                    }
                                                }

                                            }

                                        }


                                    }


                                    //Divider
                                    HorizontalDivider(modifier = Modifier.padding(top = 10.dp), color = colorResource(id = R.color.divider))


                                    //Reason Title and Status
                                    Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween){

                                        //Reason Title
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            if(reason.isNotEmpty() && flag1)
                                            {
                                                Row(modifier = Modifier.align(Alignment.Start)){
                                                    Text(
                                                        text = "Reason",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                    )
                                                }
                                            }
                                        }

                                        //Status
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Row(modifier = Modifier.align(Alignment.End)){
                                                Text(
                                                    text = status,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 12.sp, fontWeight = FontWeight(500),
                                                        color =  colorResource(id = bgColor)
                                                    ),
                                                )
                                            }
                                        }

                                    }


                                    //Reason Data
                                    if(reason.isNotEmpty() && flag1)
                                    {
                                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)){

                                            Column(modifier = Modifier.fillMaxWidth())
                                            {
                                                Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                                    modifier = Modifier.fillMaxWidth().align(Alignment.Start).clickable {
                                                        // Toggle the expanded state and tooltip visibility
                                                        isExpanded = !isExpanded
                                                    },
                                                ) {
                                                    Box( modifier = Modifier.fillMaxWidth())
                                                    {
                                                        Text(
                                                            text = if (isExpanded) reason else reason.take(35) + if (reason.length > 35) "..." else "",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
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

            if(leaveHistoryDetailDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        leaveViewModel.getLeaveHistoryDetail(navController, context, empId = empId, slNo = slNo)
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
                        uiUpdateLeaveHistoryDetail()
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
fun uiLeaveHistoryDetailPreview() {

    val navController = rememberNavController()

    val leaveHistoryDetailDataList = generateLeaveHistoryDetailDataList()

    val balance = 0.5

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave History", "leaveHistory") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .background(color = colorResource(id = R.color.backgroundColor))
            .fillMaxSize()
            .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
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
            fun uiUpdateLeaveHistoryDetail()
            {
                var isExpanded by remember { mutableStateOf(false) }

                Box(Modifier.pullRefresh(state))
                {

                    LazyColumn (Modifier.fillMaxSize())
                    {

                        if (!refreshing)
                        {
                            items(leaveHistoryDetailDataList)
                            {data ->


                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                ) {



                                    val leaveTypeName = data.leaveTypeName
                                    val fromDate = data.fromDate
                                    val status = data.status
                                    val approvedBy = data.approvedBy
                                    val rejectedBy = data.rejectedBy
                                    val reason = data.reason

                                    val flag1 = data.status == "Reject" || data.status == "Cancelled"


                                    val nextLevel = if(data.status == "Pending" && data.nextLevel.isEmpty()) "Admin" else data.nextLevel


                                    val actionTakenBy = when (data.status) {
                                        "Approved" -> approvedBy
                                        "Reject" -> rejectedBy
                                        "Pending" -> "-"
                                        "Partially Approved" -> approvedBy
                                        "Cancelled" -> rejectedBy
                                        else -> ""
                                    }



                                    val unit = when(data.dayType){
                                        "Full Day" -> "(Full Day)"
                                        "1st Half" -> "(1st Half)"
                                        "2nd Half" -> "(2nd Half)"
                                        "Hour" -> "($balance Hours)"
                                        else -> ""
                                    }


                                    val nextLevelDisplay = if(nextLevel == "-") "" else nextLevel

                                    val bgColor = when (data.status) {
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

                                    Column(modifier = Modifier.fillMaxWidth().padding(10.dp)){

                                        //LeaveType and Date
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){

                                            //LeaveType
                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                Row(modifier = Modifier){
                                                    Text(
                                                        text = "Leave type",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                    )
                                                }

                                                Row(modifier = Modifier.padding(top = 10.dp)){
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
                                                                text = leaveTypeName.take(15) + "...",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.black)
                                                                ),
                                                                modifier = Modifier.tooltipTrigger()
                                                            )
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Text(
                                                            text = leaveTypeName,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                        )
                                                    }
                                                }

                                            }

                                            //Date
                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                Button(onClick = { },
                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_themecolor)),
                                                    shape = RoundedCornerShape(20),
                                                    modifier = Modifier.align(Alignment.End)
                                                ) {

                                                    Text(
                                                        text = "$fromDateFormatted\n$unit",
                                                        modifier = Modifier.fillMaxWidth().padding(1.dp),
                                                        textAlign = TextAlign.Center,
                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(500),color = colorResource(id = R.color.themeColor)),
                                                    )

                                                }

                                            }


                                        }


                                        //ActionTakenBy and NextLevel
                                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween){

                                            //ActionTakenBy
                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                Row(modifier = Modifier.align(Alignment.Start)){
                                                    Text(
                                                        text = "Approve or Reject by",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                    )
                                                }

                                                Row(modifier = Modifier.padding(top = 10.dp).align(Alignment.Start)){

                                                    if (actionTakenBy.length > 15)
                                                    {

                                                        PlainTooltipBox(
                                                            tooltip = {
                                                                Text(
                                                                    text = actionTakenBy,
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 12.sp,
                                                                        fontWeight = FontWeight(500),
                                                                    ),
                                                                )
                                                            }
                                                        ) {
                                                            Text(
                                                                text = actionTakenBy.take(15) + "...",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.black)
                                                                ),
                                                                modifier = Modifier.tooltipTrigger()
                                                            )
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Text(
                                                            text = actionTakenBy,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                        )
                                                    }
                                                }

                                            }

                                            //NextLevel
                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                if(nextLevelDisplay.isNotEmpty())
                                                {

                                                    Row(modifier = Modifier.align(Alignment.End))
                                                    {
                                                        Text(
                                                            text = "Next Level",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 11.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.paraColor)
                                                            ),
                                                        )
                                                    }

                                                    Row(modifier = Modifier.padding(top = 10.dp).align(Alignment.End))
                                                    {
                                                        if (nextLevel.length > 15)
                                                        {

                                                            PlainTooltipBox(
                                                                tooltip = {
                                                                    Text(
                                                                        text = nextLevel,
                                                                        style = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                        ),
                                                                    )
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = nextLevel.take(15) + "...",
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 13.sp,
                                                                        fontWeight = FontWeight(500),
                                                                        color = colorResource(id = R.color.black)
                                                                    ),
                                                                    modifier = Modifier.tooltipTrigger()
                                                                )
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Text(
                                                                text = nextLevel,
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.black)
                                                                ),
                                                            )
                                                        }
                                                    }

                                                }

                                            }


                                        }


                                        //Divider
                                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp), color = colorResource(id = R.color.divider))


                                        //Reason Title and Status
                                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween){

                                            //Reason Title
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                if(reason.isNotEmpty() && flag1)
                                                {
                                                    Row(modifier = Modifier.align(Alignment.Start)){
                                                        Text(
                                                            text = "Reason",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 11.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.paraColor)
                                                            ),
                                                        )
                                                    }
                                                }
                                            }

                                            //Status
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Row(modifier = Modifier.align(Alignment.End)){
                                                    Text(
                                                        text = status,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                                            color =  colorResource(id = bgColor)
                                                        ),
                                                    )
                                                }
                                            }

                                        }


                                        //Reason Data
                                        if(reason.isNotEmpty() && flag1)
                                        {
                                            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)){

                                                Column(modifier = Modifier.fillMaxWidth())
                                                {
                                                    Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                                        modifier = Modifier.fillMaxWidth().align(Alignment.Start).clickable {
                                                            // Toggle the expanded state and tooltip visibility
                                                            isExpanded = !isExpanded
                                                        },
                                                    ) {
                                                        Box( modifier = Modifier.fillMaxWidth())
                                                        {
                                                            Text(
                                                                text = if (isExpanded) reason else reason.take(35) + if (reason.length > 35) "..." else "",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.black)
                                                                ),
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

                    PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

                }

            }



            when (flag)
            {
                1 -> {
                    uiUpdateLeaveHistoryDetail()
                }
                else -> {
                    noDataView()
                }
            }


        }
    }
}




fun generateLeaveHistoryDetailDataList(): List<LeaveHistoryDetailData>
{
    return listOf(
        LeaveHistoryDetailData(slNo = "1936", empId = "EMP12754", empCode = "INF006", leaveTypeName = "Monthly", fromDate = "23-01-2024", status = "Approved", actionTakenBy = "Admin", approvedBy = "Admin", rejectedBy = "Admin", reason = "23 APPROVED BY ADMIN", actionTakenDate = "2024-02-02 15:38:45.817", nextLevel = "", dayType = "Full Day"),
        LeaveHistoryDetailData(slNo = "1936", empId = "EMP12754", empCode = "INF006", leaveTypeName = "Monthly", fromDate = "24-01-2024", status = "Reject", actionTakenBy = "Admin", approvedBy = "Admin", rejectedBy = "Admin", reason = "24 APPROVED BY ADMIN", actionTakenDate = "2024-02-02 15:38:45.817", nextLevel = "", dayType = "Full Day"),
        LeaveHistoryDetailData(slNo = "1936", empId = "EMP12754", empCode = "INF006", leaveTypeName = "Monthly ", fromDate = "25-01-2024", status = "Cancelled", actionTakenBy = "", approvedBy = "Admin", rejectedBy = "Admin", reason = "", actionTakenDate = "2024-02-02 15:38:45.817", nextLevel = "", dayType = "1st Half"),
    )
}


