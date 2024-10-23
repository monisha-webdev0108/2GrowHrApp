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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AppliedLoanInstalmentData
import com.payroll.twogrowhr.Model.ResponseModel.LoanApprovalStagesData
import com.payroll.twogrowhr.Model.ResponseModel.LoanInstallmentData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarWithEdit
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanApprovalDetail(
    navController: NavController,
    approvalListViewModel: ApprovalListViewModel,
    loanData: LoanApprovalDataDetails // Add loanData parameter
) {

    Log.d("LoanApprovalDetail", "employeeID / loanApplySlNo : ${loanData.employeeID} / ${loanData.loanApplySlNo}")

    val loanDataJson = Gson().toJson(loanData).toString()

    val url = "${Screen.EditReporteeLoan.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"

    AppScaffold1(
        topBarContent = { TopBarWithEdit(
            navController = navController,
            title = "Loan Approvals",
            backUrl = "LoanApprovalList",
            editUrl = url,
            data = loanData.toString(),
            dataClassType = "LoanApprovalDataDetails"
        ) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { LoanApprovalDetail_Screen(navController = navController,approvalListViewModel = approvalListViewModel, loanData = loanData ) }
}

@Composable
fun LoanApprovalDetail_Screen(navController: NavController, approvalListViewModel: ApprovalListViewModel, loanData: LoanApprovalDataDetails)
{


    val context = LocalContext.current

    BackPressHandler(onBackPressed = { navController.navigate("LoanApprovalList") })

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 65.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = approvalListViewModel.loadingStatus2

        flag = approvalListViewModel.flag2

        var loading by remember { mutableStateOf(false) }

        var customDialog by remember { mutableStateOf(false) }

        var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

        var showDialog by remember { mutableStateOf(false) }

        if (loading) {
            linearProgression()
        }

        val loanInstallmentDataList = approvalListViewModel.loanInstalmentDetailList.collectAsState()

        //FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = approvalListViewModel.loadingStatus2

        if (loadingCircular) {
            circularProgression()
        }

        var reasonForReject by remember { mutableStateOf("") }

        @Composable
        fun RejectDialogBox()
        {

            val maxLength = 250

            Dialog(onDismissRequest = {
                showDialog = false
                customDialog = false
                reasonForReject = ""
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
                            .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(percent = 10)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(height = 15.dp))

                        Text(text = "Reject Reason", color = colorResource(id = R.color.black), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 10.dp))

                        Row(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth())
                        {
                            Text(
                                text = "Reason", style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.paraColor),
                            )
                        }

                        val containerColor = colorResource(id = R.color.backgroundColor)

                        TextField(
                            value = reasonForReject,
                            onValueChange = {if (it.length <= maxLength)
                                reasonForReject = it },
                            modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp).fillMaxWidth().heightIn(min = 70.dp).background(color = colorResource(id = R.color.white)),
                            maxLines = 5,
                            textStyle = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.black)),
                            placeholder = {
                                TextFieldNameSizeValidation("Enter Reason...", 13, colorResource(id = R.color.paraColor), 500, 16)
                              },
                            singleLine = false,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = colorResource(id = R.color.white),
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = colorResource(id = R.color.themeColor),
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                        )


                        // buttons
                        Button(onClick = {

                            if(reasonForReject == "" || reasonForReject.isBlank())
                            {
                                Constant.showToast(context, "Enter Reason")
                                reasonForReject = ""
                            }
                            else
                            {
                                showDialog = true
                                customDialog = false
                            }

                        }, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                            Text(text = "Reject", color = colorResource(id = R.color.white), fontSize = 14.sp )
                        }
                    }

                }

            }

        }


        if (showDialog) {

            Constant.AppTheme {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                        approvalAction = 0 // Reset the approval action
                        reasonForReject = "" // Reset the reasonForReject
                    },
                    title = {
                        Text(
                            text = "Confirmation",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 17.sp, fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black)
                            ),
                        )
                    },
                    text = {
                        Text(
                            text = "Are you sure you want to ${if (approvalAction == 1) "approve" else "reject"} this?",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor)
                            ),
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {

                                // Perform the approval or rejection logic here

                                when (approvalAction) {
                                    1 -> {
                                        approvalListViewModel.postLoanStatusUpdate(navController = navController, context = context, orgId = userViewModel.getOrg().toString(), loanApplySlNo = loanData.loanApplySlNo, empId = loanData.employeeID, appEmpId = userViewModel.getSFCode(), status = "Approve", rejectReason = reasonForReject)
                                        showDialog = false // Close the dialog
                                        reasonForReject = "" // Reset the reasonForReject
                                    }

                                    2 -> {

                                        approvalListViewModel.postLoanStatusUpdate(navController = navController, context = context, orgId = userViewModel.getOrg().toString(), loanApplySlNo = loanData.loanApplySlNo, empId = loanData.employeeID, appEmpId = userViewModel.getSFCode(), status = "Reject", rejectReason = reasonForReject)
                                        showDialog = false // Close the dialog
                                        reasonForReject = "" // Reset the reasonForReject

                                    }

                                    else -> {
                                        reasonForReject = "" // Reset the reasonForReject
                                        showDialog = false // Close the dialog for other cases
                                    }
                                }
                                approvalAction = 0 // Reset the approval action
                                reasonForReject = "" // Reset the reasonForReject
                                approvalListViewModel.fetchAndUpdateLoanData(navController, context, userViewModel.getSFCode(), userViewModel.getOrg().toString())
                                navController.navigate("LoanApprovalList")
                            },
                        ) {
                            Text(
                                text = "OK",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.white)
                                ),
                            )
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false // Close the dialog
                                approvalAction = 0 // Reset the approval action
                            },
                        ) {
                            Text(
                                text = "Cancel",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.white)
                                ),
                            )
                        }
                    }
                )
            }
        }


        if(customDialog)
        {
            RejectDialogBox()
        }



        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("NewApi")
        @Composable
        fun uiUpdate()
        {

            Column()
            {
                Column(modifier = Modifier.weight(1.0f))
                {

                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    {
                        loanExtendCard(navController, loanData)
                        loanDataDetailsCard(loanData)
                    }

                    loanInstalmentDetailsCard(loanInstalmentData = loanInstallmentDataList.value)

                }

                Box(modifier = Modifier)
                {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(0),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            Button(
                                onClick = {


                                    customDialog = true
                                    approvalAction = 2

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(vertical = 13.dp)
                            ) {
                                Text(
                                    text = "Reject",
                                    color = colorResource(id = R.color.white),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp)) // Adjust the width as needed for the space between buttons

                            Button(
                                onClick = {

                                    showDialog = true
                                    approvalAction = 1

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(vertical = 13.dp)
                            ) {
                                Text(
                                    text = "Approve",
                                    color = colorResource(id = R.color.white),
                                    style = MaterialTheme.typography.titleMedium
                                )
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

            if(loanInstallmentDataList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        approvalListViewModel.getAppliedLoanInstalmentDetails(navController, context, loanData.employeeID, loanData.loanApplySlNo)
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
                        uiUpdate()
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


    Log.d("LoanApprovalDetail_Screen", "employeeID / loanApplySlNo : ${loanData.employeeID} / ${loanData.loanApplySlNo}")


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loanExtendCard(navController: NavController, loanData: LoanApprovalDataDetails)
{
    val loanDataJson = Gson().toJson(loanData).toString()

    val url = "${Screen.LoanHistoryList.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"
    val urlStages = "${Screen.LoanApprovalStages.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"


    Column(modifier = Modifier.fillMaxWidth())
    {
        Button(
            onClick = { },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally).height(45.dp).width(180.dp).padding(top = 5.dp, bottom = 7.dp))
        {

            val employee = "${loanData.employeeName} (${loanData.employeeCode})"


            Row()
            {
                TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.lightthemecolor), 500, 17)
            }
        }
    }


    Card(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 7.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        onClick = { navController.navigate(urlStages) }
    )
    {

        Row(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.weight(0.1f))
            {
                IconButton(onClick = { }, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 10.dp))
                {
                    Icon(painterResource(id =R.drawable.loan_stages )  , contentDescription ="loan_icon", tint = colorResource(id = R.color.green) , modifier = Modifier.size(20.dp))
                }
            }

            Column(modifier = Modifier.weight(0.8f))
            {

                Text(text = "Approval Stages",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.black),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.Start))

            }

            Column(modifier = Modifier.weight(0.1f))
            {
                IconButton(onClick = { navController.navigate(urlStages) },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(size = 20.dp),
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = "",
                        tint = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }

    Card(modifier = Modifier
        .fillMaxWidth(1f)
        .padding(bottom = 10.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        onClick = { navController.navigate(url) }
    )
    {

        Row(modifier = Modifier.fillMaxWidth()) {


            Column(modifier = Modifier.weight(0.1f))
            {
                IconButton(onClick = { }, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 10.dp))
                {
                    Icon(painterResource(id =R.drawable.loan_history )  , contentDescription ="loan_icon", tint = colorResource(id = R.color.blue) , modifier = Modifier.size(20.dp))
                }
            }

            Column(modifier = Modifier.weight(0.8f)) {

                Text(text = "Loan History For Employee",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.black),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.Start)
                )

            }

            Column(modifier = Modifier.weight(0.1f))
            {
                IconButton(onClick = {navController.navigate(url)},
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(size = 20.dp),
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = "",
                        tint = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}

@Composable
fun loanDataDetailsCard(loanData: LoanApprovalDataDetails)
{

    val deductionTypeShown = when(loanData.deductionType){
        "1" -> "Monthly"
        "2" -> "Quarterly"
        "3"  -> "Half-Yearly"
        "4" -> "Yearly"
        else -> ""
    }


    Card(modifier = Modifier
        .fillMaxWidth(1f),
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
                        TextFieldNameSizeValidation(loanData.loanRuleName, 13, colorResource(id = R.color.black), 500, 17)
                    }
                }

                Column(modifier = Modifier.weight(1f))
                {
                    Column(modifier = Modifier)
                    {
                        TextFieldNameSizeValidation("Deduction Type", 13, colorResource(id = R.color.paraColor), 500, 14)
                    }

                    Column(modifier = Modifier.padding(top = 10.dp))
                    {
                        TextFieldNameSizeValidation(deductionTypeShown, 13, colorResource(id = R.color.black), 500, 14)
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
                        TextFieldNameSizeValidation("Number of Instalment", 13, colorResource(id = R.color.paraColor), 500, 20)
                    }

                    Column(modifier = Modifier.padding(top = 10.dp))
                    {
                        TextFieldNameSizeValidation(loanData.numberOfMonths, 13, colorResource(id = R.color.black), 500, 14)
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
                        TextFieldNameSizeValidation(loanData.loanRequestAmount, 13, colorResource(id = R.color.black), 500, 14)
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
                        TextFieldNameSizeValidation("Deduction Start From", 13, colorResource(id = R.color.paraColor), 500, 20)
                    }

                    Column(modifier = Modifier.padding(top = 10.dp))
                    {
                        TextFieldNameSizeValidation(loanData.loanStartDate, 13, colorResource(id = R.color.black), 500, 14)
                    }
                }

                Column(modifier = Modifier.weight(1f))
                {

                    val rateOfInterest = "${loanData.interestRate} %"

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

@Composable
fun loanInstalmentDetailsCard(loanInstalmentData: List<AppliedLoanInstalmentData>) {

    Column {

        HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 8.dp), color = colorResource(id = R.color.lightshade))


        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 15.dp, end = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.weight(0.5f))
            {
                Text(
                    text = "Month",
                    style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.themeColor)),
                    modifier = Modifier
                        .align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(0.5f))
            {
                Text(
                    text = "Instalment Amount",
                    style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.themeColor)),
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }


        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = colorResource(id = R.color.lightshade))

        LazyColumn (modifier = Modifier){
            items(loanInstalmentData) { data ->

                Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 15.dp, end = 15.dp)) {

                    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween)
                    {
                        Column(modifier = Modifier.weight(0.5f))
                        {
                            Text(
                                text = data.monthName,
                                style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.black)),
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .align(Alignment.Start)
                            )
                        }

                        Column(modifier = Modifier.weight(0.5f))
                        {
                            Text(
                                text = data.amount,
                                style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.black)),
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .align(Alignment.End)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(top = 5.dp), color = colorResource(id = R.color.divider))

                }
            }
        }
    }



}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun LoanApprovalDetail_ScreenPreview()
{

    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarWithEdit(
            navController = navController,
            title = "Loan Approvals",
            backUrl = "LoanApprovalList",
            editUrl = "LoanApprovalList",
            data = generateLoanData().toString(),
            dataClassType = "LoanApprovalDataDetails"
        ) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {

        Column(modifier = Modifier.padding(top = 65.dp))
        {

            Column(modifier = Modifier.weight(1.0f))
            {

                Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                {
                    loanExtendCard(navController, loanData = generateLoanData())
                    loanDataDetailsCard(loanData = generateLoanData())
                }

                loanInstalmentDetailsCard(loanInstalmentData = generateLoanInstalmentData())

            }


            Box(modifier = Modifier)
            {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(0),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 13.dp), horizontalArrangement = Arrangement.SpaceBetween)
                    {

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.red)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 13.dp)
                        ) {
                            Text(
                                text = "Reject",
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp)) // Adjust the width as needed for the space between buttons

                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 13.dp)
                        ) {
                            Text(
                                text = "Approve",
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

fun emptyLoanData(): LoanApprovalDataDetails {

    return LoanApprovalDataDetails(
        employeeID = "",
        employeeCode = "",
        employeeName = "",
        loanTypeSlNo = "",
        loanApplySlNo = "",
        loanRuleName = "",
        loanStartDate = "",
        loanRequestAmount = "",
        loanPeriod = "",
        remarks = "",
        instalmentAmount = "",
        nameInPayslip = "",
        employeeRequestAmount = "",
        deductionType = "",
        deductionStartMonth = "",
        numberOfMonths = "",
        loanSanctionDate = "",
        interestRate = "",
        interestRateType = ""
    )

}

fun generateLoanData(): LoanApprovalDataDetails {

    return LoanApprovalDataDetails(
        employeeID = "EMP123",
        employeeCode = "SAN123",
        employeeName = "Matheesha Pathirana",
        loanTypeSlNo = "100",
        loanApplySlNo = "101",
        loanRuleName = "Car Loan",
        loanStartDate = "01/10/2023",
        loanRequestAmount = "50000",
        loanPeriod = "Feb 2024 - May 2024",
        remarks = "for bought a new Car",
        instalmentAmount = "5000",
        nameInPayslip = "Car loan",
        employeeRequestAmount = "150000",
        deductionType = "2",
        deductionStartMonth = "Oct 2023",
        numberOfMonths = "5",
        loanSanctionDate = "10/01/2023",
        interestRate = "10",
        interestRateType = "1"
    )

}

fun generateInstalmentData(): List<LoanInstallmentData>
{
    return listOf(
        LoanInstallmentData(monthName = "Feb 2024", installmentAmount = 510.00, month= "2", year = 2024),
        LoanInstallmentData(monthName = "Mar 2024", installmentAmount = 510.00, month= "3", year = 2024),
        LoanInstallmentData(monthName = "Apr 2024", installmentAmount = 510.00, month= "4", year = 2024),
        LoanInstallmentData(monthName = "May 2024", installmentAmount = 510.00, month= "5", year = 2024),
        LoanInstallmentData(monthName = "Jun 2024", installmentAmount = 510.00, month= "6", year = 2024)
    )
}

fun generateLoanInstalmentData(): List<AppliedLoanInstalmentData>
{
    return listOf(
        AppliedLoanInstalmentData(monthName = "Feb 2024", amount = "510", month= "2", year = "2024"),
        AppliedLoanInstalmentData(monthName = "Mar 2024", amount = "510", month= "3", year = "2024"),
        AppliedLoanInstalmentData(monthName = "Apr 2024", amount = "510", month= "4", year = "2024"),
        AppliedLoanInstalmentData(monthName = "May 2024", amount = "510", month= "5", year = "2024"),
        AppliedLoanInstalmentData(monthName = "Jun 2024", amount = "510", month= "6", year = "2024")
    )
}



fun generateLoanApprovalStagesData(): List<LoanApprovalStagesData>
{
    return listOf(
        LoanApprovalStagesData(loanApprovalDate = "", loanApprovalTime = "", loanApprovalStatus = "Pending", loanAppEmpName = "Nithish ramesh raj kumar-INF004", status = "1"),
        LoanApprovalStagesData(loanApprovalDate = "Dec 01 2023", loanApprovalTime = "5:23 PM", loanApprovalStatus = "Partially Approved", loanAppEmpName = "Arjun m-INF003", status = "2"),
        LoanApprovalStagesData(loanApprovalDate = "Dec 01 2023", loanApprovalTime = "5:20 PM", loanApprovalStatus = "Request Send", loanAppEmpName = "Abhu-INF001", status = "3"),
    )
}

