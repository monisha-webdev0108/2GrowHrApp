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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanHistoryList(navController: NavController, loanViewModel: LoanDetailViewModel, approvalListViewModel: ApprovalListViewModel, loanData: LoanApprovalDataDetails)
{

    val loanApprovalData = LoanApprovalDataDetails(employeeID = loanData.employeeID, employeeCode = loanData.employeeCode, employeeName = loanData.employeeName,loanTypeSlNo = loanData.loanTypeSlNo,
        loanApplySlNo = loanData.loanApplySlNo, loanRuleName = loanData.loanRuleName, loanStartDate = loanData.loanStartDate
        , loanRequestAmount = loanData.loanRequestAmount, loanPeriod = loanData.loanPeriod, remarks = loanData.remarks, instalmentAmount = loanData.instalmentAmount, nameInPayslip = loanData.nameInPayslip, employeeRequestAmount = loanData.loanRequestAmount,
        deductionType = loanData.deductionType, deductionStartMonth = loanData.deductionStartMonth, numberOfMonths = loanData.numberOfMonths, loanSanctionDate = loanData.loanSanctionDate, interestRate = loanData.interestRate, interestRateType = loanData.interestRateType)

    val loanDataJson = Gson().toJson(loanApprovalData).toString()

    val url = "${Screen.LoanApprovalDetail.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan History", url = url) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { LoanHistoryList_Screen(navController = navController, loanViewModel= loanViewModel, approvalListViewModel = approvalListViewModel, loanData = loanData) }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanHistoryList_Screen(navController: NavController, loanViewModel: LoanDetailViewModel, approvalListViewModel: ApprovalListViewModel, loanData: LoanApprovalDataDetails)
{
    val loanApprovalData = LoanApprovalDataDetails(employeeID = loanData.employeeID, employeeCode = loanData.employeeCode, employeeName = loanData.employeeName,loanTypeSlNo = loanData.loanTypeSlNo,
        loanApplySlNo = loanData.loanApplySlNo, loanRuleName = loanData.loanRuleName, loanStartDate = loanData.loanStartDate
        , loanRequestAmount = loanData.loanRequestAmount, loanPeriod = loanData.loanPeriod, remarks = loanData.remarks, instalmentAmount = loanData.instalmentAmount, nameInPayslip = loanData.nameInPayslip, employeeRequestAmount = loanData.loanRequestAmount,
        deductionType = loanData.deductionType, deductionStartMonth = loanData.deductionStartMonth, numberOfMonths = loanData.numberOfMonths, loanSanctionDate = loanData.loanSanctionDate, interestRate = loanData.interestRate, interestRateType = loanData.interestRateType)

    val loanDataJson = Gson().toJson(loanApprovalData).toString()

    val context = LocalContext.current

    val url = "${Screen.LoanApprovalDetail.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"

    BackPressHandler(onBackPressed = { navController.navigate(url) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 55.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
    {



        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = loanViewModel.loadingStatus

        flag = loanViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        //FOR RECEIVED LIST RESPONSE

        if (statusLoading.value) {
            circularProgression1(statusLoading.value)
        }

        loanViewModel.loanList.collectAsState().also {
            loanDataList1 = it as MutableState<List<LoanData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            loanViewModel.getLoanDetails1(navController, context, loanData.employeeID, loanData, "2")
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

        //FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = approvalListViewModel.loadingStatus1

        if(loadingCircular)
        {
            circularProgression()
        }




        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLoanHistory()
        {


            Box(
                Modifier
                    .pullRefresh(state)
                    .padding(top = 10.dp)) {

                Column(modifier = Modifier.fillMaxWidth())
                {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .height(45.dp)
                            .width(180.dp)
                            .padding(top = 5.dp, bottom = 3.dp))
                    {
                        val employee = "${loanData.employeeName} (${loanData.employeeCode})"

                        Row()
                        {
                            TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.lightthemecolor), 500, 17)
                        }
                    }


                    LazyColumn {

                        Log.d("Approval... LOAN HISTORY", "Inside true DataList:${loanDataList1.value}")

                        items(loanDataList1.value) { data ->

                            val statusTextColor = when(data.loanStatus)
                            {
                                "Yet To Start" -> colorResource(id = R.color.lightthemecolor)
                                "Closed" -> colorResource(id = R.color.red)
                                "On Progress" -> colorResource(id = R.color.green)
                                else -> colorResource(id = R.color.yellow)
                            }

                            val statusButtonColor = when(data.loanStatus)
                            {
                                "Yet To Start" -> colorResource(id = R.color.dark_theme_color)
                                "Closed" -> colorResource(id = R.color.light_bright_red)
                                "On Progress" -> colorResource(id = R.color.light_bright_green)
                                else -> colorResource(id = R.color.light_bright_yellow)
                            }

                            Card(modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                            {
                                Column()
                                {

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 14)
                                            }

                                            Column(modifier = Modifier.padding(top = 10.dp))
                                            {
                                                TextFieldNameSizeValidation(data.LoanName, 13, colorResource(id = R.color.black), 500, 14)
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Status", 13, colorResource(id = R.color.paraColor), 500, 14)
                                            }

                                            Column(modifier = Modifier.padding(top = 3.dp))
                                            {
                                                Button(
                                                    onClick = { },
                                                    shape = RoundedCornerShape(5),
                                                    colors = ButtonDefaults.buttonColors(statusButtonColor),
                                                    contentPadding = PaddingValues(5.dp),
                                                    modifier = Modifier
                                                        .align(Alignment.CenterHorizontally)
                                                        .height(27.dp)
                                                ) {

                                                    TextFieldNameSizeValidation(data.loanStatus, 12, statusTextColor, 500, 20)

                                                }
                                            }
                                        }
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 5.dp, bottom = 5.dp))
                                    {
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Instalment Amount", 13, colorResource(id = R.color.paraColor), 500, 20)
                                            }

                                            Column(modifier = Modifier.padding(top = 10.dp))
                                            {
                                                TextFieldNameSizeValidation(data.InstalmentAmount.toString(), 13, colorResource(id = R.color.black), 500, 14)
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Total Amount", 13, colorResource(id = R.color.paraColor), 500, 14)
                                            }

                                            Column(modifier = Modifier.padding(top = 10.dp))
                                            {
                                                TextFieldNameSizeValidation(data.TotalAmount.toString(), 13, colorResource(id = R.color.black), 500, 14)
                                            }
                                        }
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
                                    {
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 5.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Period", 13, colorResource(id = R.color.paraColor), 500, 20)
                                            }

                                            Column(modifier = Modifier.padding(top = 10.dp))
                                            {
                                                TextFieldNameSizeValidation(data.loanPeriod, 13, colorResource(id = R.color.black), 500, 19)
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            val rateOfInterest = "${data.interestRate} %"

                                            Column(modifier = Modifier)
                                            {
                                                TextFieldNameSizeValidation("Rate of Interest", 13, colorResource(id = R.color.paraColor), 500, 17)
                                            }

                                            Column(modifier = Modifier.padding(top = 10.dp))
                                            {
                                                TextFieldNameSizeValidation(rateOfInterest, 13, colorResource(id = R.color.black), 500, 14)
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

            if(loanDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        loanViewModel.getLoanDetails1(navController, context, loanData.employeeID, loanData, "2")
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
                        uiUpdateLoanHistory()
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



@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLoanHistoryReporteePreview() {


    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan History", url = "Loan History") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 55.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
        {


            val flag = 1

             val loanData = generateLoanApprovalData()
             val loanDataList = generateLoanHistoryList()


            val refreshScope = rememberCoroutineScope()
            var refreshing by remember { mutableStateOf(false) }

            fun refresh() = refreshScope.launch {
                refreshing = true
                delay(1500)
                refreshing = false
            }

            val state = rememberPullRefreshState(refreshing, ::refresh)


            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateLoanHistory()
            {


                Box(Modifier.pullRefresh(state).padding(top = 10.dp))
                {

                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(10),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .height(45.dp)
                                .width(180.dp)
                                .padding(top = 5.dp, bottom = 3.dp))
                        {
                            val employee = "${loanData.employeeName} (${loanData.employeeCode})"

                            Row()
                            {
                                TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.lightthemecolor), 500, 17)
                            }
                        }


                        LazyColumn {

                            Log.d("Approval... LOAN HISTORY", "Inside true DataList:${loanDataList1.value}")

                            items(loanDataList) { data ->

                                val statusTextColor = when(data.loanStatus)
                                {
                                    "Yet To Start" -> colorResource(id = R.color.lightthemecolor)
                                    "Closed" -> colorResource(id = R.color.red)
                                    "On Progress" -> colorResource(id = R.color.green)
                                    else -> colorResource(id = R.color.yellow)
                                }

                                val statusButtonColor = when(data.loanStatus)
                                {
                                    "Yet To Start" -> colorResource(id = R.color.dark_theme_color)
                                    "Closed" -> colorResource(id = R.color.light_bright_red)
                                    "On Progress" -> colorResource(id = R.color.light_bright_green)
                                    else -> colorResource(id = R.color.light_bright_yellow)
                                }

                                Card(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                                {
                                    Column()
                                    {

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 14)
                                                }

                                                Column(modifier = Modifier.padding(top = 10.dp))
                                                {
                                                    TextFieldNameSizeValidation(data.LoanName, 13, colorResource(id = R.color.black), 500, 14)
                                                }
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Status", 13, colorResource(id = R.color.paraColor), 500, 14)
                                                }

                                                Column(modifier = Modifier.padding(top = 3.dp))
                                                {
                                                    Button(
                                                        onClick = { },
                                                        shape = RoundedCornerShape(5),
                                                        colors = ButtonDefaults.buttonColors(statusButtonColor),
                                                        contentPadding = PaddingValues(5.dp),
                                                        modifier = Modifier
                                                            .align(Alignment.CenterHorizontally)
                                                            .height(27.dp)
                                                    ) {

                                                        TextFieldNameSizeValidation(data.loanStatus, 12, statusTextColor, 500, 20)

                                                    }
                                                }
                                            }
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 5.dp, bottom = 5.dp))
                                        {
                                            HorizontalDivider(color = colorResource(id = R.color.divider))
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Instalment Amount", 13, colorResource(id = R.color.paraColor), 500, 20)
                                                }

                                                Column(modifier = Modifier.padding(top = 10.dp))
                                                {
                                                    TextFieldNameSizeValidation(data.InstalmentAmount.toString(), 13, colorResource(id = R.color.black), 500, 14)
                                                }
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Total Amount", 13, colorResource(id = R.color.paraColor), 500, 14)
                                                }

                                                Column(modifier = Modifier.padding(top = 10.dp))
                                                {
                                                    TextFieldNameSizeValidation(data.TotalAmount.toString(), 13, colorResource(id = R.color.black), 500, 14)
                                                }
                                            }
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
                                        {
                                            HorizontalDivider(color = colorResource(id = R.color.divider))
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 5.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Period", 13, colorResource(id = R.color.paraColor), 500, 20)
                                                }

                                                Column(modifier = Modifier.padding(top = 10.dp))
                                                {
                                                    TextFieldNameSizeValidation(data.loanPeriod, 13, colorResource(id = R.color.black), 500, 19)
                                                }
                                            }

                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                val rateOfInterest = "${data.interestRate} %"

                                                Column(modifier = Modifier)
                                                {
                                                    TextFieldNameSizeValidation("Rate of Interest", 13, colorResource(id = R.color.paraColor), 500, 17)
                                                }

                                                Column(modifier = Modifier.padding(top = 10.dp))
                                                {
                                                    TextFieldNameSizeValidation(rateOfInterest, 13, colorResource(id = R.color.black), 500, 14)
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
                    uiUpdateLoanHistory()
                }
                else -> {
                    noDataView()
                }
            }

        }
    }
}



fun generateLoanHistoryList(): List<LoanData>
{
    return listOf(
        LoanData(EmployeeName = "Shradhul", TotalAmount = 50000, DeductionType = "1", NoOfInstalment = 10, InstalmentAmount = 5000, LoanName = "Car Loan", loanStatus = "On Progress", Pending_Amount = 5000, slNo = 230, loanPeriod = "Jan 2024 - Oct 2024", interestRate = 2.0),
        LoanData(EmployeeName = "Shradhul", TotalAmount = 50000, DeductionType = "1", NoOfInstalment = 10, InstalmentAmount = 5000, LoanName = "Home Loan", loanStatus = "Yet To Start", Pending_Amount = 5000, slNo = 231, loanPeriod = "Nov 2024 - Aug 2025", interestRate = 2.0),
        LoanData(EmployeeName = "Shradhul", TotalAmount = 50000, DeductionType = "1", NoOfInstalment = 10, InstalmentAmount = 5000, LoanName = "Education Loan", loanStatus = "Closed", Pending_Amount = 5000, slNo = 232, loanPeriod = "Feb 2023 - Nov 2023", interestRate = 2.0),
        LoanData(EmployeeName = "Shradhul", TotalAmount = 50000, DeductionType = "1", NoOfInstalment = 10, InstalmentAmount = 5000, LoanName = "Medical Loan", loanStatus = "On Progress", Pending_Amount = 5000, slNo = 233, loanPeriod = "Dec 2023 - Sep 2024", interestRate = 2.0)
    )
}


fun generateLoanApprovalData(): LoanApprovalDataDetails
{
    return LoanApprovalDataDetails(employeeID = "EMP100", employeeCode = "EMP100", employeeName = "Shradhul", loanTypeSlNo = "230", loanApplySlNo = "231", loanRuleName = "Car Loan", loanStartDate = "23", loanRequestAmount = "100000", loanPeriod = "Jan 2024 - Oct 2024", remarks = "loan request to buy a car", instalmentAmount = "1000", nameInPayslip = "Car Loan", employeeRequestAmount = "50000", deductionType = "1", deductionStartMonth = "01/01/2024", numberOfMonths = "10", loanSanctionDate = "", interestRate = "2", interestRateType = "1")
}





