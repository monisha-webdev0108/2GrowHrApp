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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import com.google.gson.annotations.SerializedName
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveCompoOffData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLeaveData
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

var leaveApprovalDataList1 = mutableStateOf<List<UnApproveLeaveData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaveListApproval(
    navController: NavController,
    leaveViewModel: LeaveViewModel,
) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Leave Approvals",
            "approvallist"
        ) },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { LeaveListApproval_Screen(navController = navController,leaveViewModel = leaveViewModel) }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LeaveListApproval_Screen(navController: NavController, leaveViewModel: LeaveViewModel) {
    val context = LocalContext.current
    val employeeID = userViewModel.getSFCode()
    val orgId = userViewModel.getOrg()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
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


        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            leaveViewModel.fetchAndUpdateLeaveData(navController,context,employeeID)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)


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

            Box(Modifier.pullRefresh(state)) {

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
                                                    Column(modifier = Modifier.weight(0.5f)) {
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
                                                    Column(modifier = Modifier.weight(0.5f)) {
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
                                                            modifier = Modifier.align(Alignment.End)
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

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

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



@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLeaveApprovalListPreview() {

    val navController = rememberNavController()

    val leaveApprovalDataList = generateLeaveApprovalDataList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave Approvals", "approvallist") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
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
            fun uiUpdateLeave()
            {

                Box(Modifier.pullRefresh(state)) {

                    LazyColumn {
                        items(leaveApprovalDataList) { data ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(bottom = 15.dp)
                                    .clickable {
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
                                                    Column(modifier = Modifier.weight(0.5f)) {
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
                                                    Column(modifier = Modifier.weight(0.5f)) {
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
                                                            modifier = Modifier.align(Alignment.End)
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

                    PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

                }


            }

            when (flag)
            {
                1 -> {
                    uiUpdateLeave()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }

}




fun generateLeaveApprovalDataList(): List<UnApproveLeaveData>
{
    return listOf(
        UnApproveLeaveData(empId = "EMP10769", empCode = "EMP1135", empName = "Prabhu R", leaveTypeName = "CASUAL LEAVE", leavePeriod = "2023-04-12 - 2023-04-14", requestDate = "2023-04-12", count = "1",slNo = "1314", leaveUpload = "http://testing.2growhr.io/leaveupload",  year = "2023", month = "4", fromDate = "04/12/2023", leaveType = "112"),
        UnApproveLeaveData(empId = "EMP10769", empCode = "EMP1135", empName = "Prabhu R", leaveTypeName = "CASUAL LEAVE", leavePeriod = "2023-03-01 - 2023-03-01", requestDate = "2023-04-12", count = "1",slNo = "1320", leaveUpload = "http://testing.2growhr.io/leaveupload",  year = "2023", month = "3", fromDate = "03/01/2023", leaveType = "112"),
        UnApproveLeaveData(empId = "EMP10769", empCode = "EMP1135", empName = "Prabhu R", leaveTypeName = "PLAN LEAVE", leavePeriod = "2023-03-16 - 2023-03-16", requestDate = "2023-03-16", count = "1",slNo = "1228", leaveUpload = "http://testing.2growhr.io/leaveupload",  year = "2023", month = "3", fromDate = "03/16/2023", leaveType = "109")
    )
}

