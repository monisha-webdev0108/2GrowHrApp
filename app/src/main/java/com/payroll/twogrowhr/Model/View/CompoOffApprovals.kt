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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveCompoOffData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


var compoOffApprovalDataList1 = mutableStateOf<List<UnApproveCompoOffData>>(emptyList())


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompoOffApproval(
    navController: NavController,
    approvalListViewModel: ApprovalListViewModel
) {
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Compo Off Approvals",
            "approvallist"
        ) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { CompoOffApproval_Screen(navController = navController, approvalListViewModel = approvalListViewModel) }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CompoOffApproval_Screen(navController: NavController, approvalListViewModel: ApprovalListViewModel) {


    val context = LocalContext.current
    val employeeID = userViewModel.getSFCode()
    val orgId = userViewModel.getOrg()


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, start = 10.dp, end = 10.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        val loadingStatus = approvalListViewModel.loadingStatus

        flag = approvalListViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        approvalListViewModel.compoOffApprovalList.collectAsState().also {
            compoOffApprovalDataList1 = it as MutableState<List<UnApproveCompoOffData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            approvalListViewModel.getCompoOffApprovalDetails(navController, context,employeeID,orgId.toString())
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = approvalListViewModel.loadingStatus

        if(loadingCircular)
        {
            circularProgression()
        }


        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateCompoOff()
        {

            Box(Modifier.pullRefresh(state)) {

                LazyColumn {

                    Log.d("Approval... Compo Off", "Inside true DataList:${compoOffApprovalDataList1.value}")

                    if(compoOffApprovalDataList1.value.isEmpty())
                    {
                        loading = true
                    }
                    else
                    {
                        items(compoOffApprovalDataList1.value) { data ->



                            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
                            val outputFormat = SimpleDateFormat("dd-MM-yyyy")

                            val date1 = inputFormat.parse(data.dates)
                            val date2String = outputFormat.format(date1)

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(bottom = 10.dp)
                                    .clickable {

                                        Log.e("From uiUpdateCompoOff", "date2String : $date2String")

                                        navController.navigate("${Screen.CompoOffApprovalDetail.route}/${date2String}/${data.empId}/${data.empName}")

                                    },
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                            ) {

                                val employeeName = data.empName


                                val employeeCode = data.empCode
                                val day = data.weekDay
                                val date = data.dates

                                // First Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top =15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        if (employeeName.length > 17)
                                        {

                                            PlainTooltipBox(
                                                tooltip = {
                                                    Text(
                                                        text = employeeName,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp,
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
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier
                                                        .tooltipTrigger()
                                                        .align(Alignment.Start).padding(start = 10.dp)
                                                )
                                            }
                                        }
                                        else
                                        {
                                            Text(
                                                text = employeeName,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                                modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)

                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = date,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                            modifier = Modifier.align(Alignment.End).padding(end = 10.dp)
                                        )
                                    }
                                }

                                //Second Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top =5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = employeeCode,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.paraColor)
                                            ),
                                            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {

                                    }
                                }

                                // Third Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top = 5.dp, bottom = 15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Button(
                                            onClick = { },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_red)),
                                            contentPadding = PaddingValues(10.dp),
                                            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)

                                        ) {
                                            Text(
                                                text = day,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = colorResource(id = R.color.red)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        IconButton(onClick = {

                                            Log.e("From uiUpdateCompoOff", "date2String : $date2String")
                                            navController.navigate("${Screen.CompoOffApprovalDetail.route}/${date2String}/${data.empId}/${data.empName}")

                                        },
                                            modifier = Modifier.align(Alignment.End).padding(end = 10.dp))
                                        {
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

            if(compoOffApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        approvalListViewModel.fetchAndUpdateCompoOffData(navController, context,employeeID,orgId.toString())
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
                        uiUpdateCompoOff()
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
fun uiCompoOffApprovalListPreview() {


    val navController = rememberNavController()

    val compoOffApprovalDataList = generateCompoOffApprovalDataList()


    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Compo Off Approvals", "approvallist") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, start = 10.dp, end = 10.dp)
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
            @SuppressLint("NewApi", "SimpleDateFormat")
            @Composable
            fun uiUpdateCompoOff()
            {

                Box(Modifier.pullRefresh(state)) {

                    LazyColumn {
                        items(compoOffApprovalDataList) { data ->

                            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
                            val outputFormat = SimpleDateFormat("dd-MM-yyyy")

                            val date1 = inputFormat.parse(data.dates)
                            val date2String = date1?.let { it1 -> outputFormat.format(it1) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(bottom = 10.dp)
                                    .clickable {

                                        Log.e("From uiUpdateCompoOff", "date2String : $date2String")

                                        navController.navigate("${Screen.CompoOffApprovalDetail.route}/${date2String}/${data.empId}/${data.empName}")

                                    },
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                            ) {

                                val employeeName = data.empName


                                val employeeCode = data.empCode
                                val day = data.weekDay
                                val date = data.dates

                                // First Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top =15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        if (employeeName.length > 17)
                                        {

                                            PlainTooltipBox(
                                                tooltip = {
                                                    Text(
                                                        text = employeeName,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp,
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
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier
                                                        .tooltipTrigger()
                                                        .align(Alignment.Start).padding(start = 10.dp)
                                                )
                                            }
                                        }
                                        else
                                        {
                                            Text(
                                                text = employeeName,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.black)
                                                ),
                                                modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)

                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = date,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                            modifier = Modifier.align(Alignment.End).padding(end = 10.dp)
                                        )
                                    }
                                }

                                //Second Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top =5.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Text(
                                            text = employeeCode,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.paraColor)
                                            ),
                                            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {

                                    }
                                }

                                // Third Row

                                Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp,top = 5.dp, bottom = 15.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        Button(
                                            onClick = { },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_red)),
                                            contentPadding = PaddingValues(10.dp),
                                            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)

                                        ) {
                                            Text(
                                                text = day,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = colorResource(id = R.color.red)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f))
                                    {
                                        IconButton(onClick = {

                                            Log.e("From uiUpdateCompoOff", "date2String : $date2String")
                                            navController.navigate("${Screen.CompoOffApprovalDetail.route}/${date2String}/${data.empId}/${data.empName}")

                                        },
                                            modifier = Modifier.align(Alignment.End).padding(end = 10.dp))
                                        {
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

                    PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

                }


            }

            when (flag)
            {
                1 -> {
                    uiUpdateCompoOff()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }
}


fun generateCompoOffApprovalDataList(): List<UnApproveCompoOffData>
{
    return listOf(
        UnApproveCompoOffData(chckin = "2024-03-09 09:30:00.000", chkout = "2024-03-09 20:30:00.000", empId = "EMP12751", empCode  = "INF003", date  = "03/09/2024", dates  = "09/03/2024", weekDay = "Saturday", divId  = "249", empName  = "Arjun m  M", ratePerHour  = "0", CRHr_Per_Day  = "7", ratePerHourCR  = "200", weekOffHourCR  = "200", festiveHourCR  = "200", natHolidayHourCR  = "200", compoOffValue  = "3", hours  = "11:00", hoursTime  = "11", checkIn  = " 9:30:00 AM", checkOut  = " 8:30:00 PM", isCompOffNeed  = "1", compOffHours  = "0", adjustHalfDay  = "0",  adjustFullDay  = "0", compOffMiniHours  = "0", adjustMiniHours  = "0", nonEligibleDays  = "0", adjustNonEligibleDays  = "", salaryCalculation  = "0", salaryCount  = "0", adjustSalary  = "0", compOffLimit  = "1", compOffLimitValue  = "2", adjustCompOffLimit  = "1", compenstate  = "3", st  = "Week", holidayType  = "", weeklyOff  = "Weekly", rphHolidayForOT  = "0", rphWeekendForOT  = "0", maxHolidayHourForOT  = "0"),
        UnApproveCompoOffData(chckin = "2024-03-10 10:00:00.000", chkout = "2024-03-10 20:30:00.000", empId = "EMP12751", empCode  = "INF003", date  = "03/10/2024", dates  = "10/03/2024", weekDay = "Sunday", divId  = "249", empName  = "Arjun m  M", ratePerHour  = "0", CRHr_Per_Day  = "7", ratePerHourCR  = "200", weekOffHourCR  = "200", festiveHourCR  = "200", natHolidayHourCR  = "200", compoOffValue  = "3", hours  = "10:30", hoursTime  = "10.3", checkIn  = "10:00:00 AM", checkOut  = " 8:30:00 PM", isCompOffNeed  = "1", compOffHours  = "0", adjustHalfDay  = "0",  adjustFullDay  = "0", compOffMiniHours  = "0", adjustMiniHours  = "0", nonEligibleDays  = "0", adjustNonEligibleDays  = "", salaryCalculation  = "0", salaryCount  = "0", adjustSalary  = "0", compOffLimit  = "1", compOffLimitValue  = "2", adjustCompOffLimit  = "1", compenstate  = "3", st  = "Week", holidayType  = "", weeklyOff  = "Weekly", rphHolidayForOT  = "0", rphWeekendForOT  = "0", maxHolidayHourForOT  = "0"),
        UnApproveCompoOffData(chckin = "2024-03-02 10:00:00.000", chkout = "2024-03-02 17:00:00.000", empId = "EMP12752", empCode  = "INF004", date  = "03/02/2024", dates  = "02/03/2024", weekDay = "Saturday", divId  = "249", empName  = "Nithish ramesh raj kumar  N", ratePerHour  = "100", CRHr_Per_Day  = "7", ratePerHourCR  = "200", weekOffHourCR  = "210", festiveHourCR  = "220", natHolidayHourCR  = "240", compoOffValue  = "3", hours  = "07:00", hoursTime  = "7", checkIn  = "10:00:00 AM", checkOut  = " 5:00:00 PM", isCompOffNeed  = "1", compOffHours  = "1", adjustHalfDay  = "4",  adjustFullDay  = "8", compOffMiniHours  = "0", adjustMiniHours  = "0", nonEligibleDays  = "0", adjustNonEligibleDays  = "", salaryCalculation  = "0", salaryCount  = "0", adjustSalary  = "0", compOffLimit  = "0", compOffLimitValue  = "0", adjustCompOffLimit  = "0", compenstate  = "3", st  = "Week", holidayType  = "", weeklyOff  = "Weekly", rphHolidayForOT  = "0", rphWeekendForOT  = "0", maxHolidayHourForOT  = "0"))
}



















