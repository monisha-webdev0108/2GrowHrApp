package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LoanData
import com.payroll.twogrowhr.Model.ResponseModel.LoanSubDetailsData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.viewModel.LoanSubDetailsListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var loanSubDetailList = mutableStateOf<List<LoanSubDetailsData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Loan_view(
    navController: NavController,
    loanSubDetailViewModel: LoanSubDetailsListViewModel,
    slNo: Int
) {
    val isLoggedIn = remember { mutableStateOf(true) }
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan details","Loan") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { Loan_view_Screen(loanSubDetailViewModel=loanSubDetailViewModel,navController=navController, slNo = slNo) }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Loan_view_Screen(navController: NavController,loanSubDetailViewModel:LoanSubDetailsListViewModel,slNo: Int) {

    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val loanName = navBackStackEntry?.arguments?.getString("loanName")
    val loanNameShown = if(loanName.isNullOrEmpty()) "" else loanName

    val installmentAmount = navBackStackEntry?.arguments?.getString("installmentAmount")
    val installmentAmountShown = if(installmentAmount.isNullOrEmpty()) "" else installmentAmount

    val deductionType = navBackStackEntry?.arguments?.getString("deductionType")
    val deductionTypeShown = if(deductionType.isNullOrEmpty()) "" else deductionType

    val noInstallment = navBackStackEntry?.arguments?.getString("noInstallment")
    val noInstallmentShown = if(noInstallment.isNullOrEmpty()) "" else noInstallment

    val totalAmount = navBackStackEntry?.arguments?.getString("totalAmount")
    val totalAmountShown = if(totalAmount.isNullOrEmpty()) "" else totalAmount

    val pendingAmount = navBackStackEntry?.arguments?.getString("pendingAmount")
    val pendingAmountShown = if(pendingAmount.isNullOrEmpty()) "" else pendingAmount

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, start = 10.dp, end = 10.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

        val loadingStatus = loanSubDetailViewModel.loadingStatus

        flag = loanSubDetailViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if (loading) {
            linearProgression()
        }

        loanSubDetailViewModel.loanSubDetailList.collectAsState().also {
            loanSubDetailList = it as MutableState<List<LoanSubDetailsData>>
        }

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            loanSubDetailViewModel.getLoanSubDetails(navController,context, slNo)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = loanSubDetailViewModel.loadingStatus

        if (loadingCircular) {
            circularProgression()
        }



        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLoanDetail()
        {

            Loan_view_details(loanName = loanNameShown,installmentAmount=installmentAmountShown,deductionType=deductionTypeShown,noInstallment=noInstallmentShown,totalAmount=totalAmountShown,pendingAmount=pendingAmountShown)

            Box(Modifier.pullRefresh(state)) {

                LazyColumn{
                    item {
                        tableRowLoanHead()
                    }
                    if(loanSubDetailList.value.isNotEmpty()){
                        items(loanSubDetailList.value) { loanSubDetails ->
                            TableRow_Loan(loanSubDetails)
                        }
                    }
                    else{
                        item{
                            Column(modifier = Modifier.fillMaxWidth(1f)) {
                                Image(
                                    painterResource(id = R.drawable.noleavesvg),
                                    contentDescription = "No Loan",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "No Data Found",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = colorResource(id = R.color.paraColor),
                                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 10.dp)
                                )
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

            if(loanSubDetailList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        loanSubDetailViewModel.getLoanSubDetails(navController,context,slNo)
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
                        uiUpdateLoanDetail()
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

@Composable
fun Loan_view_details(loanName: String?,installmentAmount:String?,deductionType:String?,noInstallment:String?,totalAmount:String?,pendingAmount:String?) {

    Card(modifier = Modifier.fillMaxWidth(1f), colors = CardDefaults.cardColors(containerColor = colorResource(
        id = R.color.white
    ))) {
        Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Loan Name" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                BasicTextField(
                    readOnly = true,
                    value ="$loanName" ,
                    onValueChange = { /* Handle value change if needed */ },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                        color = colorResource(id = R.color.black)
                    ),
                    singleLine = true,
                )
            }
            Column(modifier = Modifier.padding(10.dp).weight(0.6f)) {
                Text(text = "Instalment Amount" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                Text(text = "$installmentAmount" , style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.black))
            }
        }
        Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Deduction type" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                Text(text = "$deductionType" , style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.black))
            }
            Column(modifier = Modifier.padding(10.dp).weight(0.6f)) {
                Text(text = "No of Instalment" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                Text(text = "$noInstallment" , style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.black))
            }
        }
        Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(10.dp).weight(1f)) {
                Text(text = "Total Amount" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                Text(text = "$totalAmount" , style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.black))
            }
            Column(modifier = Modifier.padding(10.dp).weight(0.6f)) {
                Text(text = "Pending Amount" , style = MaterialTheme.typography.titleSmall, color = colorResource(id = R.color.paraColor))
                Text(text = "$pendingAmount" , style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.black))
            }
        }
    }
    HorizontalDivider(modifier = Modifier.padding(top = 10.dp), color = colorResource(id = R.color.lightthemecolor))
}

@Composable
fun tableRowLoanHead() {

    Row(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            text = "Month",
            style= MaterialTheme.typography.titleSmall,
            color= colorResource(id = R.color.themeColor),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "Instalment",
            style= MaterialTheme.typography.titleSmall,
            color= colorResource(id = R.color.themeColor),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Status",
            style= MaterialTheme.typography.titleSmall,
            color= colorResource(id = R.color.themeColor),
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider(color = colorResource(id = R.color.lightthemecolor))
}
@Composable
fun TableRow_Loan(loanSubDetails: LoanSubDetailsData) {

    Row(
        modifier = Modifier.padding(10.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = loanSubDetails.months,
                style = MaterialTheme.typography.titleSmall,
                color= colorResource(id = R.color.black),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "${loanSubDetails.instalment_Amount}", // Replace with the correct property
                style = MaterialTheme.typography.titleSmall,
                color= colorResource(id = R.color.black),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = loanSubDetails.status, // Replace with the correct property
                style = MaterialTheme.typography.titleSmall,
                color= colorResource(id = R.color.black),
            )
        }
    }
    HorizontalDivider(color = colorResource(id = R.color.divider))
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLoanDetailPreview() {

    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan details","Loan") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {

        val loanNameShown = "Car Loan"
        val installmentAmountShown = "5000"
        val deductionTypeShown = "Monthly"
        val noInstallmentShown = "10"
        val totalAmountShown = "50000"
        val pendingAmountShown = "45000"

        val loanSubDetailList = generateLoanSubDetailList()

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


            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateLoanDetail()
            {

                Loan_view_details(loanName = loanNameShown,installmentAmount=installmentAmountShown,deductionType=deductionTypeShown,noInstallment=noInstallmentShown,totalAmount=totalAmountShown,pendingAmount=pendingAmountShown)

                Box(Modifier.pullRefresh(state)) {

                    LazyColumn{
                        item {
                            tableRowLoanHead()
                        }
                        if(loanSubDetailList.isNotEmpty())
                        {
                            items(loanSubDetailList) { loanSubDetails ->
                                TableRow_Loan(loanSubDetails)
                            }
                        }
                        else{
                            item{
                                Column(modifier = Modifier.fillMaxWidth(1f)) {
                                    Image(
                                        painterResource(id = R.drawable.noleavesvg),
                                        contentDescription = "No Loan",
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )

                                    Text(
                                        text = "No Data Found",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = colorResource(id = R.color.paraColor),
                                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 10.dp)
                                    )
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
                    uiUpdateLoanDetail()
                }
                else -> {
                    noDataView()
                }
            }
        }
    }

}

fun generateLoanSubDetailList(): List<LoanSubDetailsData>
{
    return listOf(
        LoanSubDetailsData(instalment_Amount = 5000, months = "January", status = "Paid", sl_No = 200),
        LoanSubDetailsData(instalment_Amount = 5000, months = "February", status = "Paid", sl_No = 201),
        LoanSubDetailsData(instalment_Amount = 5000, months = "March", status = "Unpaid", sl_No = 202),
        LoanSubDetailsData(instalment_Amount = 5000, months = "April", status = "Unpaid", sl_No = 203),
        LoanSubDetailsData(instalment_Amount = 5000, months = "May", status = "Unpaid", sl_No = 204)
    )
}


