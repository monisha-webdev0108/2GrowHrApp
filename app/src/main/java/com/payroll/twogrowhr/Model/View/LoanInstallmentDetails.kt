package com.payroll.twogrowhr.Model.View

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale



data class LoanInstalmentItems(
    val mon: String,
    val yr: String,
    var PAmt: Double
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanInstalmentDetails(navController: NavController, loanViewModel: LoanDetailViewModel, loanName: String, nameInPayslip: String, loanType: String, deductionType: String, numberOfInstalments: String, startMonth: String, loanAmount: String, interestRate: String, interestRateType: String, salaryAmount: String) {

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Loan Installment", "LoanApply") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { LoanInstalmentDetails_Screen(navController = navController, loanViewModel = loanViewModel, loanName = loanName, nameInPayslip = nameInPayslip, loanType = loanType, deductionType = deductionType, numberOfInstalments = numberOfInstalments, startMonth = startMonth, loanAmount = loanAmount, interestRate = interestRate, interestRateType = interestRateType, salaryAmount = salaryAmount)}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanInstalmentDetails_Screen(navController: NavController, loanViewModel: LoanDetailViewModel, loanName: String, nameInPayslip: String, loanType: String, deductionType: String, numberOfInstalments: String, startMonth: String, loanAmount: String, interestRate: String, interestRateType: String, salaryAmount: String) {


    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val parsedDate = LocalDate.parse(startMonth)
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    val deductDate =  parsedDate.format(formatter)

    val deductionTypeToDisplay = when(deductionType){
        "1" -> "Monthly"
        "2" -> "Quarterly"
        "3" -> "Half Yearly"
        "4" -> "Yearly"
        else -> ""
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 70.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
    {

        var flag by remember { mutableIntStateOf(0) }

        val loadingStatus = loanViewModel.loadingStatus4

        flag = loanViewModel.flag4

        var loading by remember { mutableStateOf(false) }

        val loanInstallmentDataList = loanViewModel.installmentDetails.collectAsState()

        Log.d("qwerty", "${loanInstallmentDataList.value}")


        // Creating a DateTimeFormatter to format the date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var remarks by remember { mutableStateOf("") }

        val maxLength = 250

        var totalRepayableAmount = 0.00
        var instalmentValue = 0.00


        for (element in loanInstallmentDataList.value)
        {
            totalRepayableAmount += element.installmentAmount

            if(instalmentValue < element.installmentAmount)
            {
                instalmentValue = element.installmentAmount
            }
        }

        val loanInstalmentItemsList = mutableListOf<LoanInstalmentItems>()

        loanInstallmentDataList.value.forEach { installmentData ->

            val loanInstalmentItem = LoanInstalmentItems(
                mon = installmentData.month,
                yr = installmentData.year.toString(),
                PAmt = installmentData.installmentAmount
            )
            loanInstalmentItemsList.add(loanInstalmentItem)

        }



        @Composable
        fun LoanApplyDetailHeader()
        {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
            {
                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                    Column(modifier = Modifier.weight(0.5f))
                    {

                        Text(
                            text = "Loan Type",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 8.dp)
                                .align(Alignment.Start)
                        )

                        Column( modifier = Modifier.align(Alignment.Start))
                        {
                            Text(
                                text = loanName,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)),
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }


                    }

                    Column(modifier = Modifier.weight(0.5f))
                    {
                        Text(
                            text = "Deduction Type",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 5.dp)
                                .align(Alignment.Start)
                        )

                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(10),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade)),
                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp ),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .height(33.dp)
                                .width(130.dp) // Set the width and height of the button
                        ) {
                            Text(
                                text = deductionTypeToDisplay,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.lightthemecolor)),
                            )
                        }
                    }


                }

                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                    Column(modifier = Modifier.weight(0.5f))
                    {

                        Text(
                            text = "No. of Instalment",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.Start)
                        )

                        Column( modifier = Modifier.align(Alignment.Start))
                        {
                            Text(
                                text = numberOfInstalments,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)),
                            )
                        }

                    }

                    Column(modifier = Modifier.weight(0.5f))
                    {

                        Text(
                            text = "Total Amount",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.Start)
                        )

                        Column( modifier = Modifier.align(Alignment.Start))
                        {
                            Text(
                                text = totalRepayableAmount.toString(),
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)),
                            )
                        }

                    }


                }

                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                    Column(modifier = Modifier.weight(0.5f))
                    {

                        Text(
                            text = "Deduct from",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.Start)
                        )


                        Column( modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.Start))
                        {
                            Text(
                                text = deductDate,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                ),
                            )
                        }

                    }

                    Column(modifier = Modifier.weight(0.5f))
                    {

                        Text(
                            text = "Rate of Interest",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.Start)
                        )

                        Column( modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.Start))
                        {
                            Text(
                                text = "$interestRate%",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                ),
                            )
                        }
                    }
                }
            }
        }


        @Composable
        fun LoanInstalmentDetail()
        {

            Column {

                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))


                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Column(modifier = Modifier.weight(0.5f))
                    {
                        Text(
                            text = "Month",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .align(Alignment.Start)
                        )
                    }

                    Column(modifier = Modifier.weight(0.5f))
                    {
                        Text(
                            text = "Instalment Amount",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .align(Alignment.End)
                        )
                    }
                }


                HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))

                LazyColumn (modifier = Modifier.padding(bottom = 10.dp)){
                    items(loanInstallmentDataList.value) { data ->

                        Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 15.dp, end = 15.dp)) {

                            Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Column(modifier = Modifier.weight(0.5f))
                                {
                                    Text(
                                        text = data.monthName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)),
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                            .align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(0.5f))
                                {
                                    Text(
                                        text = "${data.installmentAmount}",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)),
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                            .align(Alignment.End)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp), color = colorResource(id = R.color.divider))

                        }
                    }
                }
            }
        }


        @Composable
        fun uiUpdate()
        {

            var customDialog by remember { mutableStateOf(false) }

            if(statusLoading.value)
            {
                circularProgression()
            }


            Column(modifier = Modifier.fillMaxSize())
            {
                Column(modifier = Modifier.weight(0.34f))
                {
                    LoanApplyDetailHeader()
                }

                Column(modifier = Modifier.weight(0.53f))
                {
                    LoanInstalmentDetail()
                }

                @Composable
                fun RemarksDialogBox()
                {
                    Dialog(onDismissRequest = {
                        customDialog = false
                        remarks = ""
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
                                Spacer(modifier = Modifier.height(height = 30.dp))

                                Text(text = "Remarks",
                                    style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),
                                modifier = Modifier.padding(top = 10.dp))


                                TextField(
                                    value = remarks,
                                    onValueChange = {
                                        if (it.length <= maxLength)
                                            remarks = it },
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .heightIn(min = 100.dp)
                                        .background(color = colorResource(id = R.color.white)),
                                    maxLines = 5,
                                    textStyle = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    ),
                                    placeholder = { Text(text = "Enter Remarks...",
                                            style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        )) },
                                    singleLine = false,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        cursorColor = colorResource(id = R.color.black),
                                        focusedBorderColor = colorResource(id = R.color.themeColor),
                                        unfocusedBorderColor = colorResource(R.color.white),
                                    )
                                )

                                // buttons

                                Button(onClick = {

                                    if(remarks == "" || remarks.isBlank())
                                    {
                                        coroutineScope.launch { Constant.showToast(context, "Please enter Remarks..") }
                                        remarks = ""
                                    }
                                    else
                                    {
                                        customDialog = false

                                        statusLoading.value = true

                                        Log.d("loan Instalment Details", "$loanInstalmentItemsList")

                                        // Parsing the startMonth string into a LocalDate object
                                        val date = LocalDate.parse(startMonth)

                                        // Extracting year and month from the LocalDate object
                                        val year = date.year
                                        val month = date.monthValue


                                        // Extracting fromDate
                                        val firstInstallment = loanInstallmentDataList.value.first()
                                        val fromMonth = firstInstallment.month.toInt()
                                        val fromYear = firstInstallment.year
                                        val fromDate = LocalDate.of(fromYear, fromMonth, 1)
                                        val formattedFromDate = fromDate.format(formatter) + " 00:00:00.000"


                                        // Extracting toDate
                                        val lastInstallment = loanInstallmentDataList.value.last()
                                        val toMonth = lastInstallment.month.toInt()
                                        val toYear = lastInstallment.year
                                        val toDate = LocalDate.of(toYear, toMonth, 1)
                                        val formattedToDate = toDate.format(formatter) + " 00:00:00.000"


                                        val finalAmount = loanAmount.toDouble() / numberOfInstalments.toInt()
                                        val formattedFinalAmount = String.format("%.2f", finalAmount)// Formatting the final amount to display only two decimal points


                                        loanViewModel.postLoanForm(context = context, navController = navController, loanItems = loanInstalmentItemsList, empID = userViewModel.getSFCode(), deductionType = deductionType, loanAmount = loanAmount, month = month.toString(), year = year.toString(), numberOfInstalments = numberOfInstalments, divId = userViewModel.getDivisionCode().toString(), orgId = userViewModel.getOrg().toString(), fromDate = formattedFromDate, toDate = formattedToDate, finalAmount = formattedFinalAmount, interestRate = interestRate, interestRateType = interestRateType, nameInPayslip = nameInPayslip, loanInterestAmount = totalRepayableAmount.toString(), remarks = remarks, loanTypeSlNo = loanType, instalmentInterestType = instalmentValue.toString())
                                    }


                                }, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                    Text(text = "Apply", color = colorResource(id = R.color.white), fontSize = 14.sp )
                                }
                            }

                        }

                    }

                }

                if(customDialog)
                {
                    RemarksDialogBox()
                }

                Column(modifier = Modifier.weight(0.13f))
                {
                    Box(modifier = Modifier
                        .padding(top = 5.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(0),
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                                Button(
                                    onClick = {

                                        statusLoading.value = true

                                        if(instalmentValue < salaryAmount.toDouble())
                                        {
                                            loanViewModel.getExistingLoanAmountDetails(navController = navController, context = context, empId = userViewModel.getSFCode()){instalmentAmount ->

                                                if((instalmentAmount.toDouble() + instalmentValue) < salaryAmount.toDouble())
                                                {
                                                    statusLoading.value = false
                                                    customDialog = true
                                                }
                                                else
                                                {
                                                    statusLoading.value = false
                                                    coroutineScope.launch { Constant.showToast(context,"you had an Outstanding Loan!!!") }
                                                }

                                            }
                                        }
                                        else
                                        {
                                            statusLoading.value = false
                                            coroutineScope.launch { Constant.showToast(context, "Instalment Amount is greater than your salary!!!") }
                                        }

                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                    shape = RoundedCornerShape(5.dp),
                                    contentPadding = PaddingValues(vertical = 15.dp)
                                ) {
                                    Text(
                                        text = "Apply Loan",
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

        if(loading)
        {
            circularProgression()
        }

        // LOGIC TO DISPLAY THE UI

        if(loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(loanInstallmentDataList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        loanViewModel.getInstallmentDetails(navController = navController, context = context, deductionType = deductionType, numberOfInstalments = numberOfInstalments, startMonth = startMonth, loanAmount = loanAmount, interestRate = interestRate, interestType = interestRateType)
                    }
                    2 -> {
                        noDataView()
                    }
                    3 -> {
                        exceptionScreen()
                    }
                    else -> {
                        coroutineScope.launch { Constant.showToast(context, "Please try again later...!") }
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
                        uiUpdate()
                    }
                    2 -> {
                        noDataView()
                    }

                    3 -> {
                        exceptionScreen()
                    }
                    else -> {
                        coroutineScope.launch {  Constant.showToast(context,"Please try again later...!") }
                    }
                }
            }
        }
    }

}
