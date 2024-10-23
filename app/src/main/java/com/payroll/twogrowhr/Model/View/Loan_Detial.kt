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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.PayslipMonthData
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveCompoOffData
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
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var loanDataList1 = mutableStateOf<List<LoanData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Loan(
    navController: NavController,
    loanViewModel: LoanDetailViewModel,

) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan details", "Finance") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        Loan_Screen(
            navController = navController,
            loanViewModel = loanViewModel
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable

fun Loan_Screen(
    navController: NavController,
    loanViewModel: LoanDetailViewModel
) {


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

        val loadingStatus = loanViewModel.loadingStatus

        flag = loanViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if (loading) {
            linearProgression()
        }

        loanViewModel.loanList.collectAsState().also {
            loanDataList1 = it as MutableState<List<LoanData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true

            loanViewModel.getLoanDetails1(navController,context,employeeID, emptyLoanData(), "1")
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = loanViewModel.loadingStatus

        if (loadingCircular) {
            circularProgression()
        }


        @Suppress("DEPRECATION")
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLoan()
        {
            Box(Modifier.pullRefresh(state)) {

                LazyColumn {
                    items(loanDataList1.value) { loan ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 10.dp)
                                .clickable {
                                    navController.navigate("${Screen.LoanView.route}?res=${loan.slNo}&loanName=${loan.LoanName}&installmentAmount=${loan.InstalmentAmount}&deductionType=${loan.DeductionType}&noInstallment=${loan.NoOfInstalment}&totalAmount=${loan.TotalAmount}&pendingAmount=${loan.Pending_Amount}")
                                },
//                            .clickable { navController.navigate("${Screen.LoanView.route}?res=${loan.slNo}") },
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .weight(1f)) {
                                    BasicTextField(
                                        readOnly = true,
                                        value ="${loan.LoanName} (${loan.DeductionType})" ,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        singleLine = true,
                                    )
                                    Text(
                                        text = "${loan.Pending_Amount}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = colorResource(
                                            id = R.color.paraColor
                                        )
                                    )
                                }
                                val instalmentAmount = loan.TotalAmount
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .weight(0.4f)) {
                                    Row {
                                        Text(
                                            text = "₹ $instalmentAmount",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black),
                                            modifier = Modifier.padding(end = 10.dp)
                                        )

                                    }
                                    Text(
                                        text = loan.loanStatus,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = colorResource(
                                            id = R.color.paraColor
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

            if(loanDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        loanViewModel.getLoanDetails1(navController,context,employeeID, emptyLoanData(), "1")
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
                        uiUpdateLoan()
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
fun uiLoanPreview() {

    val navController = rememberNavController()
    val loanDataList = generateLoanList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan details", "Finance") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, start = 10.dp, end = 10.dp)
        ) {

            var flag by remember { mutableIntStateOf(1) }

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
            fun uiUpdateLoan()
            {
                Box(Modifier.pullRefresh(state)) {

                    LazyColumn {
                        items(loanDataList) { loan ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(bottom = 10.dp)
                                    .clickable {
                                        navController.navigate("${Screen.LoanView.route}?res=${loan.slNo}&loanName=${loan.LoanName}&installmentAmount=${loan.InstalmentAmount}&deductionType=${loan.DeductionType}&noInstallment=${loan.NoOfInstalment}&totalAmount=${loan.TotalAmount}&pendingAmount=${loan.Pending_Amount}")
                                    },
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))

                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier
                                        .padding(10.dp)
                                        .weight(1f)) {
                                        BasicTextField(
                                            readOnly = true,
                                            value ="${loan.LoanName} (${loan.DeductionType})" ,
                                            onValueChange = {},
                                            textStyle = TextStyle(
                                                fontSize = 14.sp,
                                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                color = colorResource(id = R.color.black)
                                            ),
                                            singleLine = true,
                                        )
                                        Text(
                                            text = "${loan.Pending_Amount}",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = colorResource(
                                                id = R.color.paraColor
                                            )
                                        )
                                    }
                                    val instalmentAmount = loan.TotalAmount
                                    Column(modifier = Modifier
                                        .padding(10.dp)
                                        .weight(0.4f)) {
                                        Row {
                                            Text(
                                                text = "₹ $instalmentAmount",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.black),
                                                modifier = Modifier.padding(end = 10.dp)
                                            )

                                        }
                                        Text(
                                            text = loan.loanStatus,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = colorResource(
                                                id = R.color.paraColor
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }



            when (flag)
            {
                1 -> {
                    uiUpdateLoan()
                }
                else -> {
                    noDataView()
                }
            }



        }
    }
}


fun generateLoanList(): List<LoanData>
{
    return listOf(
        LoanData(EmployeeName = "Pathirana", TotalAmount = 25000, DeductionType = "Monthly", NoOfInstalment = 5, InstalmentAmount = 5000, LoanName = "Car Loan", loanStatus = "Yet To Start", Pending_Amount = 25000, slNo = 250, loanPeriod = "", interestRate = 10.00),
        LoanData(EmployeeName = "Pathirana", TotalAmount = 25000, DeductionType = "Yearly", NoOfInstalment = 5, InstalmentAmount = 5000, LoanName = "Home Loan", loanStatus = "Pending", Pending_Amount = 25000, slNo = 250, loanPeriod = "", interestRate = 10.00),
        LoanData(EmployeeName = "Pathirana", TotalAmount = 25000, DeductionType = "Quarterly", NoOfInstalment = 5, InstalmentAmount = 5000, LoanName = "Education Loan", loanStatus = "In Progress", Pending_Amount = 25000, slNo = 250, loanPeriod = "", interestRate = 10.00),
    )
}

