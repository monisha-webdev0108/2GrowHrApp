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
import androidx.compose.material3.MaterialTheme
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
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveLoanData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var loanApprovalDataList1 = mutableStateOf<List<UnApproveLoanData>>(emptyList())


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanApprovalList(
    navController: NavController,
    approvalListViewModel: ApprovalListViewModel
) {
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Loan Approvals",
            "approvallist"
        ) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { LoanApprovalList_Screen(navController = navController,approvalListViewModel = approvalListViewModel) }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoanApprovalList_Screen(navController: NavController, approvalListViewModel: ApprovalListViewModel)
{
    val context = LocalContext.current
    val orgId = userViewModel.getOrg()

    BackPressHandler(onBackPressed = { navController.navigate("approvallist") })

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, start = 10.dp, end = 10.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = approvalListViewModel.loadingStatus1

        flag = approvalListViewModel.flag1

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        approvalListViewModel.loanApprovalList.collectAsState().also {
            loanApprovalDataList1 = it as MutableState<List<UnApproveLoanData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            approvalListViewModel.getLoanApprovalDetails(navController, context, userViewModel.getSFCode(),orgId.toString())
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
        fun uiUpdateUnApprovedLoan()
        {

            val maxLineLength = 30 // Maximum number of characters to display before truncating

            Box(Modifier.pullRefresh(state)) {

                LazyColumn {
                    Log.d("Approval... LOAN DETAILS", "Inside true DataList:${loanApprovalDataList1.value}")

                    items(loanApprovalDataList1.value) { data ->


                        val deductionTypeShown = when(data.deductionType){
                            "1" -> "Monthly"
                            "2" -> "Quarterly"
                            "3"  -> "Half-Yearly"
                            "4" -> "Yearly"
                            else -> ""
                        }



                        var isExpanded by remember { mutableStateOf(false) }


                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            Row(modifier = Modifier.fillMaxWidth())
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 10.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation(data.empName, 13, colorResource(id = R.color.themeColor), 600, 14)
                                            }

                                            Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                            {
                                                TextFieldNameSizeValidation(data.empCode, 13, colorResource(id = R.color.paraColor), 500, 14)
                                            }

                                        }


                                        Column(modifier = Modifier.weight(1f))
                                        {

                                            Column(modifier = Modifier)//.padding(top = 10.dp, bottom = 10.dp)
                                            {
                                                Button(
                                                    onClick = { },
                                                    shape = RoundedCornerShape(5),
                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                                                    contentPadding = PaddingValues(10.dp),
                                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                                ) {
                                                    Text(
                                                        text = data.loanPeriod,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.lightthemecolor)
                                                        ),
                                                    )
                                                }
                                            }

                                        }

                                    }

                                    //Divider

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                                    {
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation("Loan Name", 13, colorResource(id = R.color.paraColor), 500, 15)
                                            }

                                            Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                            {
                                                TextFieldNameSizeValidation(data.loanRuleName, 13, colorResource(id = R.color.black), 500, 15)
                                            }
                                        }


                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 18)
                                            }

                                            Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                            {
                                                TextFieldNameSizeValidation(data.loanRequestAmount, 13, colorResource(id = R.color.black), 500, 15)
                                            }
                                        }

                                    }

                                    //Divider

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 3.dp, bottom = 3.dp))
                                    {
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {

                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation("Instalment Amount", 13, colorResource(id = R.color.paraColor), 500, 17)
                                            }

                                            Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                            {
                                                TextFieldNameSizeValidation(data.instalmentAmount, 13, colorResource(id = R.color.black), 500, 15)
                                            }
                                        }


                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation("Deduction Type", 13, colorResource(id = R.color.paraColor), 500, 15)
                                            }

                                            Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp).clickable { isExpanded = !isExpanded }
                                            {
                                                TextFieldNameSizeValidation(deductionTypeShown, 13, colorResource(id = R.color.black), 500, 15)
                                            }
                                        }

                                    }

                                    //Divider

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp))
                                    {
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }


                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.weight(1f))
                                        {
                                            Column(modifier = Modifier)//.padding(top = 10.dp)
                                            {
                                                TextFieldNameSizeValidation("Remarks", 13, colorResource(id = R.color.paraColor), 500, 15)
                                            }

                                            Column(modifier = Modifier.fillMaxWidth().padding(top = 5.dp))//
                                            {

                                                Card(
                                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Start)
                                                        .clickable { isExpanded = !isExpanded },
                                                ) {
                                                    Box( modifier = Modifier.fillMaxWidth())
                                                    {
                                                        Text(
                                                            text = if (isExpanded) data.remarks else data.remarks.take(
                                                                maxLineLength
                                                            ) + if (data.remarks.length > maxLineLength) "..." else "",
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

                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                    {
                                        Column(modifier = Modifier.padding(vertical = 3.dp, horizontal = 3.dp)) {
                                            Button(
                                                onClick = {

                                                    val loanApprovalData = LoanApprovalDataDetails(employeeID = data.empId, employeeCode = data.empCode, employeeName = data.empName,loanTypeSlNo = data.loanTypeSlNo,
                                                        loanApplySlNo = data.loanApplySlNo, loanRuleName = data.loanRuleName, loanStartDate = data.loanStartDate
                                                            , loanRequestAmount = data.loanRequestAmount, loanPeriod = data.loanPeriod, remarks = data.remarks, instalmentAmount = data.instalmentAmount, nameInPayslip = data.nameInPayslip, employeeRequestAmount = data.empRequestAmount,
                                                        deductionType = data.deductionType, deductionStartMonth = data.deductionStartMonth, numberOfMonths = data.NumberOfMonths, loanSanctionDate = data.loanSanctionDate, interestRate = data.interestRate, interestRateType = data.interestRateType)

                                                    val loanDataJson = Gson().toJson(loanApprovalData).toString()

                                                    navController.navigate("${Screen.LoanApprovalDetail.route}?res=${data.loanApplySlNo}&loanDataJson=${loanDataJson}")


                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                                shape = RoundedCornerShape(5.dp),
                                                contentPadding = PaddingValues(vertical = 15.dp)
                                            ) {
                                                Text(
                                                    text = "Approve/Reject",
                                                    color = colorResource(id = R.color.white),
                                                    style = MaterialTheme.typography.titleMedium
                                                )
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

            if(loanApprovalDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        approvalListViewModel.fetchAndUpdateLoanData(navController, context, userViewModel.getSFCode(), orgId.toString())
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
                        uiUpdateUnApprovedLoan()
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
fun uiLoanApprovalListPreview() {


    val navController = rememberNavController()

    val loanApprovalDataList1 = generateLoanApprovalDataList()
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan Approvals", "approvallist") },
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


            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateUnApprovedLoan()
            {

                val maxLineLength = 30 // Maximum number of characters to display before truncating

                Box(Modifier.pullRefresh(state)) {

                    LazyColumn {

                        items(loanApprovalDataList1) { data ->


                            val deductionTypeShown = when(data.deductionType){
                                "1" -> "Monthly"
                                "2" -> "Quarterly"
                                "3"  -> "Half-Yearly"
                                "4" -> "Yearly"
                                else -> ""
                            }

                            var isExpanded by remember { mutableStateOf(false) }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(bottom = 15.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                            ) {

                                Row(modifier = Modifier.fillMaxWidth())
                                {
                                    Column(modifier = Modifier.fillMaxWidth())
                                    {

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 10.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation(data.empName, 13, colorResource(id = R.color.themeColor), 600, 14)
                                                }

                                                Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation(data.empCode, 13, colorResource(id = R.color.paraColor), 500, 14)
                                                }

                                            }


                                            Column(modifier = Modifier.weight(1f))
                                            {

                                                Column(modifier = Modifier)//.padding(top = 10.dp, bottom = 10.dp)
                                                {
                                                    Button(
                                                        onClick = { },
                                                        shape = RoundedCornerShape(5),
                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                                                        contentPadding = PaddingValues(10.dp),
                                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                                    ) {
                                                        Text(
                                                            text = data.loanPeriod,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.lightthemecolor)
                                                            ),
                                                        )
                                                    }
                                                }

                                            }

                                        }

                                        //Divider

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                                        {
                                            HorizontalDivider(color = colorResource(id = R.color.divider))
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation("Loan Name", 13, colorResource(id = R.color.paraColor), 500, 15)
                                                }

                                                Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation(data.loanRuleName, 13, colorResource(id = R.color.black), 500, 15)
                                                }
                                            }


                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 18)
                                                }

                                                Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation(data.loanRequestAmount, 13, colorResource(id = R.color.black), 500, 15)
                                                }
                                            }

                                        }

                                        //Divider

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 3.dp, bottom = 3.dp))
                                        {
                                            HorizontalDivider(color = colorResource(id = R.color.divider))
                                        }

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {

                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation("Instalment Amount", 13, colorResource(id = R.color.paraColor), 500, 17)
                                                }

                                                Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation(data.instalmentAmount, 13, colorResource(id = R.color.black), 500, 15)
                                                }
                                            }


                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation("Deduction Type", 13, colorResource(id = R.color.paraColor), 500, 15)
                                                }

                                                Column(modifier = Modifier.padding(top = 5.dp))//.padding(top = 10.dp, bottom = 10.dp).clickable { isExpanded = !isExpanded }
                                                {
                                                    TextFieldNameSizeValidation(deductionTypeShown, 13, colorResource(id = R.color.black), 500, 15)
                                                }
                                            }

                                        }

                                        //Divider

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp))
                                        {
                                            HorizontalDivider(color = colorResource(id = R.color.divider))
                                        }


                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.weight(1f))
                                            {
                                                Column(modifier = Modifier)//.padding(top = 10.dp)
                                                {
                                                    TextFieldNameSizeValidation("Remarks", 13, colorResource(id = R.color.paraColor), 500, 15)
                                                }

                                                Column(modifier = Modifier.fillMaxWidth().padding(top = 5.dp))//
                                                {

                                                    Card(
                                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .align(Alignment.Start)
                                                            .clickable { isExpanded = !isExpanded },
                                                    ) {
                                                        Box( modifier = Modifier.fillMaxWidth())
                                                        {
                                                            Text(
                                                                text = if (isExpanded) data.remarks else data.remarks.take(
                                                                    maxLineLength
                                                                ) + if (data.remarks.length > maxLineLength) "..." else "",
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

                                        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                        {
                                            Column(modifier = Modifier.padding(vertical = 3.dp, horizontal = 3.dp)) {
                                                Button(
                                                    onClick = {

                                                        val loanApprovalData = LoanApprovalDataDetails(employeeID = data.empId, employeeCode = data.empCode, employeeName = data.empName,loanTypeSlNo = data.loanTypeSlNo,
                                                            loanApplySlNo = data.loanApplySlNo, loanRuleName = data.loanRuleName, loanStartDate = data.loanStartDate
                                                            , loanRequestAmount = data.loanRequestAmount, loanPeriod = data.loanPeriod, remarks = data.remarks, instalmentAmount = data.instalmentAmount, nameInPayslip = data.nameInPayslip, employeeRequestAmount = data.empRequestAmount,
                                                            deductionType = data.deductionType, deductionStartMonth = data.deductionStartMonth, numberOfMonths = data.NumberOfMonths, loanSanctionDate = data.loanSanctionDate, interestRate = data.interestRate, interestRateType = data.interestRateType)

                                                        val loanDataJson = Gson().toJson(loanApprovalData).toString()

                                                        navController.navigate("${Screen.LoanApprovalDetail.route}?res=${data.loanApplySlNo}&loanDataJson=${loanDataJson}")


                                                    },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                                    shape = RoundedCornerShape(5.dp),
                                                    contentPadding = PaddingValues(vertical = 15.dp)
                                                ) {
                                                    Text(
                                                        text = "Approve/Reject",
                                                        color = colorResource(id = R.color.white),
                                                        style = MaterialTheme.typography.titleMedium
                                                    )
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

                1 ->
                {
                    uiUpdateUnApprovedLoan()
                }
                else -> {
                    noDataView()
                }
            }


        }
    }
}


fun generateLoanApprovalDataList(): List<UnApproveLoanData>
{
    return listOf(
        UnApproveLoanData(empId = "EMP1326", loanTypeSlNo = "230", loanRuleName = "Education Loan", loanStartDate = "01/01/2024", empCode = "P004", empName = "Gokul", loanRequestAmount = "10000", loanPeriod = "Jan 2024 - Aug 2024", remarks = "Qwerty App loan test for loan approval in 2growHr Payroll mobile application", instalmentAmount = "1050.00", loanApplySlNo = "310", nameInPayslip = "Education Loan", empRequestAmount = "80000", deductionType = "3", deductionStartMonth = "Jan 2024", NumberOfMonths = "5", loanSanctionDate = "", interestRate = "5", interestRateType = "1"),
        UnApproveLoanData(empId = "EMP100", loanTypeSlNo = "231", loanRuleName = "Car Loan", loanStartDate = "01/09/2024", empCode = "P003", empName = "Shradhul", loanRequestAmount = "10000", loanPeriod = "Sep 2024 - Jan 2025", remarks = "loan apply from web", instalmentAmount = "1025.0", loanApplySlNo = "319", nameInPayslip = "Car Loan", empRequestAmount = "100000", deductionType = "1", deductionStartMonth = "Sep 2024", NumberOfMonths = "10", loanSanctionDate = "", interestRate = "10", interestRateType = "1"),    )
}


