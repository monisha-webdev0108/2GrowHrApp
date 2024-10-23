package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanApprovalStagesData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanApprovalStages(
    navController: NavController,
    approvalListViewModel: ApprovalListViewModel,
    loanData: LoanApprovalDataDetails // Add loanData parameter
) {

    val loanApprovalData = LoanApprovalDataDetails(employeeID = loanData.employeeID, employeeCode = loanData.employeeCode, employeeName = loanData.employeeName,loanTypeSlNo = loanData.loanTypeSlNo,
        loanApplySlNo = loanData.loanApplySlNo, loanRuleName = loanData.loanRuleName, loanStartDate = loanData.loanStartDate
        , loanRequestAmount = loanData.loanRequestAmount, loanPeriod = loanData.loanPeriod, remarks = loanData.remarks, instalmentAmount = loanData.instalmentAmount, nameInPayslip = loanData.nameInPayslip, employeeRequestAmount = loanData.loanRequestAmount,
        deductionType = loanData.deductionType, deductionStartMonth = loanData.deductionStartMonth, numberOfMonths = loanData.numberOfMonths, loanSanctionDate = loanData.loanSanctionDate, interestRate = loanData.interestRate, interestRateType = loanData.interestRateType)

    val loanDataJson = Gson().toJson(loanApprovalData).toString()

    val url = "${Screen.LoanApprovalDetail.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan Approval Stages", url = url) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { LoanApprovalStages_Screen(navController = navController,approvalListViewModel = approvalListViewModel, loanData = loanData ) }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanApprovalStages_Screen(navController: NavController, approvalListViewModel: ApprovalListViewModel, loanData: LoanApprovalDataDetails)
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

        val loadingStatus = approvalListViewModel.loadingStatus4

        flag = approvalListViewModel.flag4

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        //FOR RECEIVED LIST RESPONSE

        if (statusLoading.value) {
            circularProgression1(statusLoading.value)
        }

        val loanApprovalStagesDetailList = approvalListViewModel.loanApprovalStagesDetailList.collectAsState()

        // LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(loanApprovalStagesDetailList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        approvalListViewModel.getLoanApprovalStagesDetails(navController, context, loanData.employeeID, loanData.loanApplySlNo, userViewModel.getSFCode())
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
                        uiUpdateLoanApprovalStages(navController = navController, context = context, loanApprovalStages = loanApprovalStagesDetailList.value, loanData = loanData, approvalListViewModel = approvalListViewModel)
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
@SuppressLint("NewApi")
@Composable
fun uiUpdateLoanApprovalStages(navController: NavController, context: Context, loanApprovalStages: List<LoanApprovalStagesData>, loanData: LoanApprovalDataDetails, approvalListViewModel: ApprovalListViewModel)
{


    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        approvalListViewModel.getLoanApprovalStagesDetails(navController, context, loanData.employeeID, loanData.loanApplySlNo, userViewModel.getOrg().toString())
        delay(1500)
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

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

            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 3.dp))
            {
                loanDataDetailsCard(loanData)
            }

            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 5.dp))
            {
                loanApprovalStagesCard(loanApprovalStages)
            }


        }


        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun loanApprovalStagesCard(loanStages: List<LoanApprovalStagesData>)
{



    Card(modifier = Modifier.fillMaxWidth(1f),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
    {

        LazyColumn {

            Log.d("Approval... LOAN HISTORY", "Inside true DataList:${loanStages}")

            items(loanStages) { data ->

                val cardColor = when(data.loanApprovalStatus){
                    "Pending" -> colorResource(id = R.color.light_bright_blue)
                    "Partially Approved" -> colorResource(id = R.color.light_bright_yellow)
                    "Request Send" -> colorResource(id = R.color.light_bright_pink)
                    "Approved" -> colorResource(id = R.color.light_bright_green)
                    "Reject" -> colorResource(id = R.color.light_bright_red)
                    else -> colorResource(id = R.color.white)
                }


                val stagesColor = when(data.loanApprovalStatus){
                    "Pending" -> colorResource(id = R.color.blue)
                    "Partially Approved" -> colorResource(id = R.color.yellow)
                    "Request Send" -> colorResource(id = R.color.pink)
                    "Approved" -> colorResource(id = R.color.green)
                    "Reject" -> colorResource(id = R.color.red)
                    else -> colorResource(id = R.color.white)
                }

                val cardText = when(data.loanApprovalStatus){
                    "Pending" -> data.loanAppEmpName
                    "Partially Approved" -> "Approved by ${data.loanAppEmpName}"
                    "Request Send" -> "Apply by ${data.loanAppEmpName}"
                    "Approved" -> "Approved by ${data.loanAppEmpName}"
                    "Reject" -> "Rejected by ${data.loanAppEmpName}"
                    else -> ""
                }


                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
                {

                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp))
                    {
                        Icon(painterResource(id =R.drawable.dot )  , contentDescription ="dot", tint = stagesColor , modifier = Modifier.size(10.dp))

                        VerticalDivider(modifier = Modifier.height(70.dp).padding(start = 5.dp, top = 3.dp), color = colorResource(id = R.color.divider))

                    }


                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp, top = 8.dp, bottom = 8.dp)
                        .drawBehind {
                            drawLine(color = stagesColor, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 4.dp.toPx())
                        }, shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(containerColor = cardColor))
                    {

                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            Column(modifier = Modifier.weight(0.70f).padding(start = 5.dp))
                            {

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(data.loanApprovalStatus, 13, stagesColor, 500, 20)
                                }

                                Column(modifier = Modifier.align(Alignment.Start).padding(top = 5.dp))
                                {
                                    TextFieldNameSizeValidation(cardText, 13, colorResource(id = R.color.black), 500, 25)

                                }

                            }


                            Column(modifier = Modifier.weight(0.30f).padding(end = 10.dp))
                            {
                                Column(modifier = Modifier.align(Alignment.End))
                                {
                                    TextFieldNameSizeValidation(data.loanApprovalDate, 13, colorResource(id = R.color.black), 500, 20)
                                }

                                Column(modifier = Modifier.align(Alignment.End).padding(top = 5.dp))
                                {
                                    TextFieldNameSizeValidation(data.loanApprovalTime, 13, colorResource(id = R.color.paraColor), 500, 20)
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun uiUpdateLoanApprovalStagesPreview()
{


    val loanData = generateLoanData()
    val loanApprovalStages = generateLoanApprovalStagesData()

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

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

                val employee = "${loanData.employeeName} (${loanData.employeeID})"
                Row()
                {
                    TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.lightthemecolor), 500, 10)
                }
            }

            Column(modifier = Modifier.padding(top = 2.dp, bottom = 3.dp))
            {
                loanDataDetailsCard(loanData)
            }

            Column(modifier = Modifier.padding(top = 2.dp, bottom = 5.dp))
            {
                loanApprovalStagesCard(loanApprovalStages)
            }


        }


        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

    }

}