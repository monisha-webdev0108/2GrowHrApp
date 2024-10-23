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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CompoOffApprovalDetail(
    navController: NavController,
    approvalListViewModel: ApprovalListViewModel,
    date : String,
    empId : String,
    empName : String
) {

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = empName, "CompoOffApproval") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { CompoOffApprovalDetail_Screen(
        navController = navController,
        approvalListViewModel = approvalListViewModel,
        date = date,
        empId = empId
    ) }
}



@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
@Composable
fun CompoOffApprovalDetail_Screen(navController: NavController, approvalListViewModel: ApprovalListViewModel, date: String, empId: String)
{

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val orgId = userViewModel.getOrg()
    val employeeID = userViewModel.getSFCode()

    runCatching {

        val filteredData = compoOffApprovalDataList1.value.filter {

            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")

            val date1 = inputFormat.parse(it.dates)
            val date2String = date1?.let { it1 -> outputFormat.format(it1) }

            it.empId == empId && date2String == date
        }

        if (filteredData.isNotEmpty()) {
            val data = filteredData.first() // Assuming you only want the first matching data

            // Now you can access details of the first matching data

            val checkInTime = data.chckin
            val checkOutTime = data.chkout

            val filteredDate = data.date

            val compensate = data.compenstate.toInt()

            val workingHours = data.hoursTime.toDouble()

            val adjustFullDay = data.adjustFullDay.toDouble()
            val adjustHalfDay = data.adjustHalfDay.toDouble()



            //values for post the compo off approval

            val empIdSelected = data.empId
            val compoOffDate = data.date
            val compoOffRatePerHour = data.ratePerHour.toFloat()
            val divId = data.divId.toInt()
            val holidayType = data.holidayType
            val ratePerDayCR = data.CRHr_Per_Day.toFloat()
            val ratePerHourCR = data.ratePerHourCR.toFloat()
            val weekOffPerHourCR = data.weekOffHourCR.toFloat()
            val ratePerHourFestiveHolidayCR = data.festiveHourCR.toFloat()
            val ratePerHourNationalHolidayCR = data.natHolidayHourCR.toFloat()
            val salaryCalculation = data.salaryCalculation.toInt()
            val salaryCount = data.salaryCount.toFloat()
            val otRatePerHourNationalHoliday = data.rphHolidayForOT.toFloat() //doubt
            val adjustSalary = data.adjustSalary.toInt()
            val maxHourForFestiveHolidayOT = data.maxHolidayHourForOT.toFloat()
            val ratePerHourWeekendOT = data.rphWeekendForOT.toFloat()




            val inputDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val outputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val dateFormat = LocalDate.parse(filteredDate, inputDateFormat)
            val dateToShown = dateFormat.format(outputDateFormat)


            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val outputFormat = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")

            val dateCheckIn = LocalDateTime.parse(checkInTime, inputFormat)
            val dateCheckInString = dateCheckIn.format(outputFormat)

            val dateCheckOut = LocalDateTime.parse(checkOutTime, inputFormat)
            val dateCheckOutString = dateCheckOut.format(outputFormat)

            var customDialog by remember { mutableStateOf(false) }
            var approvalAction by remember { mutableIntStateOf(0) } // 1 for approve, 2 for reject

            var showDialog by remember { mutableStateOf(false) }

            val compensateListType : List<String> = when (compensate) {
                0 -> listOf("Compo Off")
                1 -> listOf("Over Time")
                2 -> listOf("Compo Off Rate")
                3 -> listOf("Compo Off", "Over Time", "Compo Off Rate")
                else -> emptyList()
            }

            var selectedApprovalType by remember { mutableStateOf(compensateListType.firstOrNull() ?: "") }


            val title = when(selectedApprovalType)
            {
                "Compo Off" -> { "Day" }

                "Over Time" -> { "Hour" }

                "Compo Off Rate" -> { "Hour" }

                else -> { "Day" }
            }


            fun compoOffCredit(): Double {

                Log.d("compoOffCredit", "workingHours/adjustHalfDay/adjustFullDay : $workingHours/$adjustHalfDay/$adjustFullDay")

/*                return if (workingHours >= adjustFullDay) { 1.0 }
                else if (workingHours >= adjustHalfDay) { 0.5 }
                else { 0.0 }
                */


                return if (workingHours > adjustHalfDay) { 1.0 }
                else { 0.5 }
            }


            val creditedValue = when(selectedApprovalType)
            {
                "Compo Off" -> {
                    compoOffCredit()
                }
                "Over Time" -> {
                    workingHours
                }
                "Compo Off Rate" -> {
                    workingHours
                }
                else -> {
                    workingHours
                }
            }

            val compoOffType = when(selectedApprovalType)
            {
                "Compo Off" -> {
                    0
                }
                "Over Time" -> {
                    1
                }
                "Compo Off Rate" -> {
                    2
                }
                else -> {
                    3
                }
            }

            Log.d("CompoOffApprovalDetail", "compensateType : $compensateListType")


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
                                .background(
                                    color = colorResource(id = R.color.white),
                                    shape = RoundedCornerShape(percent = 10)
                                ),
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
                                modifier = Modifier
                                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                    .fillMaxWidth()
                                    .heightIn(min = 70.dp)
                                    .background(color = colorResource(id = R.color.white)),
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

//                                    regularizedList1.value = emptyList()

                                    // Perform the approval or rejection logic here

                                    when (approvalAction) {
                                        1 -> {

                                            approvalListViewModel.postCompoOffStatusUpdate(
                                                navController,
                                                context,
                                                empIdSelected,
                                                compoOffDate,
                                                compoOffType.toString(),
                                                creditedValue.toString(),
                                                checkInTime,
                                                checkOutTime,
                                                compoOffRatePerHour,
                                                divId,
                                                holidayType,
                                                ratePerDayCR,
                                                ratePerHourCR,
                                                weekOffPerHourCR,
                                                ratePerHourFestiveHolidayCR,
                                                ratePerHourNationalHolidayCR,
                                                salaryCalculation,
                                                salaryCount,
                                                otRatePerHourNationalHoliday,
                                                adjustSalary,
                                                maxHourForFestiveHolidayOT,
                                                ratePerHourWeekendOT,
                                                orgId,
                                                approvalAction,
                                                reasonForReject
                                            )
                                            showDialog = false // Close the dialog
                                            reasonForReject = "" // Reset the reasonForReject


                                        }

                                        2 -> {

                                            approvalListViewModel.postCompoOffStatusUpdate(
                                                navController,
                                                context,
                                                empIdSelected,
                                                compoOffDate,
                                                compoOffType.toString(),
                                                creditedValue.toString(),
                                                checkInTime,
                                                checkOutTime,
                                                compoOffRatePerHour,
                                                divId,
                                                holidayType,
                                                ratePerDayCR,
                                                ratePerHourCR,
                                                weekOffPerHourCR,
                                                ratePerHourFestiveHolidayCR,
                                                ratePerHourNationalHolidayCR,
                                                salaryCalculation,
                                                salaryCount,
                                                otRatePerHourNationalHoliday,
                                                adjustSalary,
                                                maxHourForFestiveHolidayOT,
                                                ratePerHourWeekendOT,
                                                orgId,
                                                approvalAction,
                                                reasonForReject
                                            )
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
                                    approvalListViewModel.fetchAndUpdateCompoOffData(navController, context, employeeID, orgId.toString())
                                    navController.navigate("CompoOffApproval")
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


            val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


            ModalBottomSheetLayout(
                sheetContent = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .background(colorResource(id = R.color.white))
                    )
                    {


                        Column(Modifier.fillMaxSize())
                        {
                            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                            {
                                Text(
                                    text = "Select Approval type",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = colorResource(id = R.color.themeColor),
                                    modifier = Modifier.padding(10.dp)
                                )
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            LazyColumn {
                                items(compensateListType) { data ->
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp, end = 10.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    if (modalBottomSheetState.isVisible) {
                                                        modalBottomSheetState.hide()
                                                    } else {
                                                        modalBottomSheetState.show()
                                                    }
                                                }

                                                selectedApprovalType = data
                                            },
                                    ) {
                                        Row(modifier = Modifier.padding(15.dp)) {
                                            Column {
                                                Text(
                                                    text = data,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                )
                                            }
                                        }
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }
                                }
                            }
                        }
                    }
                },
                sheetBackgroundColor = Color.Transparent,
                sheetContentColor = Color.Black,
                sheetElevation = 0.dp,
                sheetShape = MaterialTheme.shapes.large,
                scrimColor = Color.Black.copy(alpha = 0.4f), // Semi-transparent black scrim
                sheetState = modalBottomSheetState // Use modalBottomSheetState here

            ) {


                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, start = 10.dp, end = 10.dp))
                {

                    Column(modifier = Modifier.weight(1.8f)){
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(10),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Compo Off Approval",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 12.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.lightthemecolor)
                                ),
                            )
                        }

                        Card(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                        {


                            //FIRST ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top =15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Worked On",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Day",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }

                            // DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top =15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateToShown,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = data.weekDay,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }


                            //Divider
                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top = 15.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            //SECOND ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "In Time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Out Time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }


                            //DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateCheckInString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateCheckOutString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }

                            //Divider
                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top = 15.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            //THIRD ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {

                                    Text(
                                        text = "Total hours",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                    )

                                    /*
                                                                    Row(modifier = Modifier.align(Alignment.Start))
                                                                    {
                                                                        PlainTooltipBox(
                                                                            tooltip = {
                                                                                Text(
                                                                                    text = "employeeName",
                                                                                    style = TextStyle(
                                                                                        fontFamily = poppins_font,
                                                                                        fontSize = 14.sp,
                                                                                        fontWeight = FontWeight(500),
                                                                                    ),
                                                                                )
                                                                            }
                                                                        ) {

                                                                            Image(
                                                                                painter = painterResource(id = R.drawable.information),
                                                                                contentDescription = null,
                                                                                modifier = Modifier
                                                                                    .size(20.dp)
                                                                                    .clip(CircleShape)
                                                                                    .tooltipTrigger(),
                                                                                contentScale = ContentScale.Crop
                                                                            )

                                                                        }
                                                                    }
                                    */

                                }

                                Column(modifier = Modifier.weight(1f))
                                {

                                }
                            }

                            //DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = data.hours,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {

                                }
                            }
                        }


                        Column()
                        {
                            Text(
                                text = "Select Type",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            )


                            // Creating a button that changes bottomSheetScaffoldState value upon click
                            // when bottomSheetScaffoldState is collapsed, it expands and vice-versa
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                                    .height(51.dp)
                                    .clickable {
                                        coroutineScope.launch {

                                            if (modalBottomSheetState.isVisible) {
                                                modalBottomSheetState.hide()
                                            } else {
                                                modalBottomSheetState.show()
                                            }

                                        }
                                    },
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(5.dp),

                                ) {
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Row {

                                        Text(
                                            text = selectedApprovalType,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                        )

                                        Column(modifier = Modifier.fillMaxWidth(1f)) {
                                            Icon(
                                                painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .size(15.dp)
                                            )
                                        }

                                    }
                                }
                            }
                        }


                        Column()
                        {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                                    .height(48.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(5.dp),
                            ) {
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = creditedValue.toString(),
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                    )

                                }
                            }
                        }
                    }

                    Column(modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth()){

                        Card(modifier = Modifier
                            .fillMaxWidth(1f),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))){

                            // Action Button

                            Row( modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 10.dp
                                ))
                            {
                                Button(
                                    onClick = {
                                        showDialog = true
                                        approvalAction = 1 // Set approval action to 1 (Approve)
                                    },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = "Approve",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        ),

                                        )
                                }

                                Button(
                                    onClick = {
                                        customDialog = true
//                                        showDialog = true
                                        approvalAction = 2 // Set approval action to 2 (reject)
                                    },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "Reject", style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }


        }

    }.onFailure { exception ->
        // Handle the exception
        Log.e("From CompoOffApprovalDetail_Screen", "Error during navigation: ${exception.message}", exception)
    }







}


@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiCompoOffApprovalDetailPreview() {


    val navController = rememberNavController()

    val coroutineScope = rememberCoroutineScope()

    val compoOffApprovalDataList = generateCompoOffApprovalDataList()

    val date = "02-03-2024"

    val empId = "EMP12752"

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Nithish", "CompoOffApproval") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {


        val filteredData = compoOffApprovalDataList.filter {

            val inputFormat = SimpleDateFormat("dd/MM/yyyy")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")

            val date1 = inputFormat.parse(it.dates)
            val date2String = date1?.let { it1 -> outputFormat.format(it1) }

            it.empId == empId && date2String == date
        }

        if (filteredData.isNotEmpty()) {

            val data = filteredData.first() // Assuming you only want the first matching data

            // Now you can access details of the first matching data

            val checkInTime = data.chckin
            val checkOutTime = data.chkout

            val date1 = data.date

            val compensate = data.compenstate.toInt()

            val workingHours = data.hoursTime.toDouble()

            val adjustHalfDay = data.adjustHalfDay.toDouble()


            val inputDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val outputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val dateFormat = LocalDate.parse(date1, inputDateFormat)
            val dateToShown = dateFormat.format(outputDateFormat)


            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val outputFormat = DateTimeFormatter.ofPattern("hh:mm a dd/MM/yyyy")

            val dateCheckIn = LocalDateTime.parse(checkInTime, inputFormat)
            val dateCheckInString = dateCheckIn.format(outputFormat)

            val dateCheckOut = LocalDateTime.parse(checkOutTime, inputFormat)
            val dateCheckOutString = dateCheckOut.format(outputFormat)

            val compensateListType : List<String> = when (compensate) {
                0 -> listOf("Compo Off")
                1 -> listOf("Over Time")
                2 -> listOf("Compo Off Rate")
                3 -> listOf("Compo Off", "Over Time", "Compo Off Rate")
                else -> emptyList()
            }

            val selectedApprovalType by remember { mutableStateOf(compensateListType.firstOrNull() ?: "") }


            val title = when(selectedApprovalType)
            {
                "Compo Off" -> { "Day" }

                "Over Time" -> { "Hour" }

                "Compo Off Rate" -> { "Hour" }

                else -> { "Day" }
            }


            fun compoOffCredit(): Double {

                return if (workingHours > adjustHalfDay) { 1.0 }
                else { 0.5 }
            }


            val creditedValue = when(selectedApprovalType)
            {
                "Compo Off" -> {
                    compoOffCredit()
                }
                "Over Time" -> {
                    workingHours
                }
                "Compo Off Rate" -> {
                    workingHours
                }
                else -> {
                    workingHours
                }
            }


            val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded)


            ModalBottomSheetLayout(
                sheetContent = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .background(colorResource(id = R.color.white))
                    )
                    {


                        Column(Modifier.fillMaxSize())
                        {
                            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                            {
                                Text(
                                    text = "Select Approval type",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = colorResource(id = R.color.themeColor),
                                    modifier = Modifier.padding(10.dp)
                                )
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            LazyColumn {
                                items(compensateListType) { data ->
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp, end = 10.dp)
                                            .clickable { },
                                    ) {
                                        Row(modifier = Modifier.padding(15.dp)) {
                                            Column {
                                                Text(
                                                    text = data,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                )
                                            }
                                        }
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }
                                }
                            }
                        }
                    }
                },
                sheetBackgroundColor = Color.Transparent,
                sheetContentColor = Color.Black,
                sheetElevation = 0.dp,
                sheetShape = MaterialTheme.shapes.large,
                scrimColor = Color.Black.copy(alpha = 0.4f), // Semi-transparent black scrim
                sheetState = modalBottomSheetState // Use modalBottomSheetState here

            ) {


                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, start = 10.dp, end = 10.dp))
                {

                    Column(modifier = Modifier.weight(1.8f)){
                        Button(
                            onClick = { },
                            shape = RoundedCornerShape(10),
                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Compo Off Approval",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 12.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.lightthemecolor)
                                ),
                            )
                        }

                        Card(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                        {


                            //FIRST ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top =15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Worked On",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Day",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }

                            // DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top =15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateToShown,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = data.weekDay,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }


                            //Divider
                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top = 15.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            //SECOND ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "In Time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = "Out Time",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }


                            //DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateCheckInString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = dateCheckOutString,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.align(Alignment.Start)
                                    )
                                }
                            }

                            //Divider
                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp,top = 15.dp))
                            {
                                HorizontalDivider(color = colorResource(id = R.color.divider))
                            }

                            //THIRD ROW

                            //TITLE

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {

                                    Text(
                                        text = "Total hours",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                    )

                                }

                                Column(modifier = Modifier.weight(1f))
                                {

                                }
                            }

                            //DATA

                            Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                                Column(modifier = Modifier.weight(1f))
                                {
                                    Text(
                                        text = data.hours,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                    )
                                }

                                Column(modifier = Modifier.weight(1f))
                                {

                                }
                            }
                        }


                        Column()
                        {
                            Text(
                                text = "Select Type",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            )


                            // Creating a button that changes bottomSheetScaffoldState value upon click
                            // when bottomSheetScaffoldState is collapsed, it expands and vice-versa
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                                    .height(51.dp)
                                    .clickable {
                                        coroutineScope.launch {

                                            if (modalBottomSheetState.isVisible) {
                                                modalBottomSheetState.hide()
                                            } else {
                                                modalBottomSheetState.show()
                                            }

                                        }
                                    },
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(5.dp),

                                ) {
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Row {

                                        Text(
                                            text = selectedApprovalType,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)
                                            ),
                                        )

                                        Column(modifier = Modifier.fillMaxWidth(1f)) {
                                            Icon(
                                                painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .size(15.dp)
                                            )
                                        }

                                    }
                                }
                            }
                        }


                        Column()
                        {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                                    .height(48.dp),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(5.dp),
                            ) {
                                Column(modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = creditedValue.toString(),
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                    )

                                }
                            }
                        }
                    }

                    Column(modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth()){

                        Card(modifier = Modifier
                            .fillMaxWidth(1f),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))){

                            // Action Button

                            Row( modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 10.dp
                                ))
                            {
                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                ) {
                                    Text(
                                        text = "Approve",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        ),

                                        )
                                }

                                Button(
                                    onClick = {   },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.red)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "Reject", style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.white)
                                        )
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