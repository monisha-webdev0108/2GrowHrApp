package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.DrawerContent
import com.payroll.twogrowhr.components.TopBar
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.getLoginDetails
import com.payroll.twogrowhr.viewModel.AttendanceRegularizedViewModel
import com.payroll.twogrowhr.viewModel.MainViewModel
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.noDataView
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Calendar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Attendance(navController: NavController,attendance_regularizedViewModel: AttendanceRegularizedViewModel, viewModel : MainViewModel) {

    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)

    val jsonObject = getLoginDetails()
    var divisionName = ""

    if (jsonObject != null)
    {
        divisionName = jsonObject.getString("Division_Name")
    }

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBar(navController = navController, title = divisionName,drawerState = drawerState) },
        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { Attendance_Screen(navController=navController, attendance_regularizedViewModel = attendance_regularizedViewModel) }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Attendance_Screen(navController: NavController, attendance_regularizedViewModel: AttendanceRegularizedViewModel) {
    val calendarFromState = rememberSheetState()
    val calendarToState = rememberSheetState()
//    val selectedFromDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedFromDate = remember { mutableStateOf(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())) }
    val selectedToDate = remember { mutableStateOf(LocalDate.now()) }
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    var textCheckIn by remember {mutableStateOf("") }
    var text by remember {mutableStateOf("") }
    var textCheckOut by remember {mutableStateOf("") }
    var regularizedSelectedDate by remember {mutableStateOf("") }
    var regularizedSelectedDateCICO by remember {mutableStateOf("") }
    val context1 = LocalContext.current

    // Creating a TimePicker dialed

    val mTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(mContext, R.style.AppTheme),

        {_, mHour : Int, mMinute: Int ->
//            textCheckIn = "$mHour:$mMinute"
            // Format the hour and minute values with two digits
            val formattedHour = String.format("%02d", mHour)
            val formattedMinute = String.format("%02d", mMinute)

            // Combine the formatted values to create the final time string
            textCheckIn = "$formattedHour:$formattedMinute"

        }, mHour, mMinute, true
    )
    val mTimePickerDialog2 = TimePickerDialog(

        ContextThemeWrapper(mContext, R.style.AppTheme),
        {_, mHour : Int, mMinute: Int ->
//            textCheckOut = "$mHour:$mMinute"
            // Format the hour and minute values with two digits
            val formattedHour = String.format("%02d", mHour)
            val formattedMinute = String.format("%02d", mMinute)

            // Combine the formatted values to create the final time string
            textCheckOut = "$formattedHour:$formattedMinute"
        }, mHour, mMinute, true
    )

    Constant.AppTheme{
        CalendarDialog(
            state = calendarFromState,
            selection = CalendarSelection.Date { date ->
                if (date <= selectedToDate.value) {
                    selectedFromDate.value = date
                    calendarToState.hide()
                } else {
                    Toast.makeText(context1, "Invalid date selection", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    Constant.AppTheme{
        CalendarDialog(
            state = calendarToState,
            selection = CalendarSelection.Date { date ->
                if (date >= selectedFromDate.value) {
                    selectedToDate.value = date
                    calendarToState.hide()
                } else {
                    Toast.makeText(context1, "Invalid date selection", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val empID = userViewModel.getSFCode()
    val org = userViewModel.getOrg()
    val selectedFromDate1 = selectedFromDate.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val selectedToDate1 = selectedToDate.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val mode = "RegularizeCount"
    val attendanceList = attendance_regularizedViewModel.attendanceList.collectAsState()
    var attendanceCICOList = attendance_regularizedViewModel.attendanceCICOList.collectAsState()
    val attendanceRBHList = attendance_regularizedViewModel.attendanceRBHList.collectAsState()
    val attendanceRegularizeRules = attendance_regularizedViewModel.attendanceRegularizeRules.collectAsState()
    val attendanceRegularizeCount = attendance_regularizedViewModel.attendanceRegularizeCount.collectAsState()
    val isAdjustNeeda1 = attendanceRegularizeRules.value.any { it.isAdjustNeed == 1 && it.adjustMode == 1 && it.adjustDays > 0}
    val isAdjustNeeda2 = attendanceRegularizeRules.value.any { it.isAdjustNeed == 1 && it.adjustMode == 2 && it.adjustDays > 0}
    val isAdjustNeeda3 = attendanceRegularizeRules.value.any { it.isAdjustNeed == 1 && it.adjustMode == 3 && it.adjustDays > 0}
    val isAdjustNeeda4 = attendanceRegularizeRules.value.any { it.isAdjustNeed == 1 && it.adjustMode == 4 && it.adjustDays > 0}
    val isAdjustNeeda5 = attendanceRegularizeRules.value.any { it.isAdjustNeed == 1 && it.adjustMode == 5 && it.adjustDays > 0}

    Log.d("Attendance", "isAdjustNeeda1/isAdjustNeeda2/isAdjustNeeda3/isAdjustNeeda4/isAdjustNeeda5 : $isAdjustNeeda1/$isAdjustNeeda2/$isAdjustNeeda3/$isAdjustNeeda4/$isAdjustNeeda5")
    var expandedIndex by remember { mutableStateOf(-1) }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var regularizedDate = if (regularizedSelectedDate.isNotEmpty()) {
        LocalDate.parse(regularizedSelectedDate) // Parse the selected date
    } else {
        LocalDate.now() // Default to the current date when regularized_selected_date is empty
    }
    Log.d("regularizedDate","${regularizedDate} regularizedDate clicked date for regularized")

    LaunchedEffect(empID,selectedFromDate1,selectedToDate1) {
        isLoading = true
        attendance_regularizedViewModel.getAttendanceRegularizedDetails(
            navController=navController,
            context = context,
            empId = empID,
            fromdate = selectedFromDate1,
            todate = selectedToDate1,
        )
        isLoading = false
    }

    LaunchedEffect(empID) {
        attendance_regularizedViewModel.getAttendanceRegularizeRulesDetail(context=context, navController = navController, empId = empID)
    }

    fun checkRegularize(regularizedSelectedDate : String) : Boolean
    {

        Log.d("Inside checkRegularize", "RegularizedSelectedDate/ $regularizedSelectedDate")

        val regularizedDate = if (regularizedSelectedDate.isNotEmpty()) {
            LocalDate.parse(regularizedSelectedDate) // Parse the selected date
        } else {
            LocalDate.now() // Default to the current date when regularized_selected_date is empty
        }

        Log.d("Inside checkRegularize", "RegularizeDate/ $regularizedDate")

        if (isAdjustNeeda1) {

            val fdt = regularizedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) // monday end sunday
            val tdt = fdt.plusDays(6)

            Log.d("Inside checkRegularize", "isAdjustNeeda1 - RegularizeDate/fdt/tdt : $regularizedDate/$fdt/$tdt")

            attendance_regularizedViewModel.getAttendanceRegularizeCountDetail(context=context, navController = navController, empId = empID, mode = mode, Fdt = fdt.toString(), Tdt = tdt.toString())
            return true
        }
        else if (isAdjustNeeda2){

            val fdt = regularizedDate.withDayOfMonth(1) // First day of the month
            val tdt = regularizedDate.withDayOfMonth(regularizedDate.lengthOfMonth()) // Last day of the month

            Log.d("Inside checkRegularize", "isAdjustNeeda2 - RegularizeDate/fdt/tdt : $regularizedDate/$fdt/$tdt")

            attendance_regularizedViewModel.getAttendanceRegularizeCountDetail(context=context, navController = navController, empId = empID, mode = mode, Fdt = fdt.toString(), Tdt = tdt.toString())

            return true
        }
        else if (isAdjustNeeda3)
        {
            val quarter = (regularizedDate.monthValue - 1) / 3 + 1
            val fdt = regularizedDate.withMonth((quarter - 1) * 3 + 1).withDayOfMonth(1) // First day of the quarter
            val tdt = fdt.plusMonths(2).withDayOfMonth(fdt.lengthOfMonth()) // Last day of the quarter

            Log.d("Inside checkRegularize", "isAdjustNeeda3 - RegularizeDate/fdt/tdt : $regularizedDate/$fdt/$tdt")

            attendance_regularizedViewModel.getAttendanceRegularizeCountDetail(context=context, navController = navController, empId = empID, mode = mode, Fdt = fdt.toString(), Tdt = tdt.toString())
            return true
        }
        else if (isAdjustNeeda4)
        {
            val fdt: LocalDate
            val tdt: LocalDate
            if (regularizedDate.monthValue <= 6) {
                fdt = regularizedDate.withDayOfYear(1)
                tdt = fdt.plusMonths(6).minusDays(1)
            } else {
                fdt = regularizedDate.withMonth(7).withDayOfMonth(1)
                tdt = fdt.withDayOfMonth(fdt.lengthOfMonth())
            }

            Log.d("Inside checkRegularize", "isAdjustNeeda4 - RegularizeDate/fdt/tdt : $regularizedDate/$fdt/$tdt")

            attendance_regularizedViewModel.getAttendanceRegularizeCountDetail(context=context,navController = navController, empId = empID, mode = mode, Fdt = fdt.toString(), Tdt = tdt.toString())
            return true
        }
        else if(isAdjustNeeda5)
        {
            val fdt = regularizedDate.withDayOfYear(1) // Start of the year
            val tdt = regularizedDate.withDayOfYear(regularizedDate.lengthOfYear()) // End of the year

            Log.d("Inside checkRegularize", "isAdjustNeeda5 - RegularizeDate/fdt/tdt : $regularizedDate/$fdt/$tdt")

            attendance_regularizedViewModel.getAttendanceRegularizeCountDetail(context=context,navController = navController, empId = empID, mode = mode, Fdt = fdt.toString(), Tdt = tdt.toString())
            return true
        }
        else
        {
            Log.d("attendanceRegularizeCount", "qwerty : Inside else part of adjustNeed")
            return false
        }
    }




    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(750.dp)
                    .background(colorResource(id = R.color.white))
            ) {
                Column(
                    Modifier.fillMaxSize(),
                ) {
                    Row(modifier = Modifier.fillMaxWidth(1f)) {
                        Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                            Text(
                                text = "Regularize",
                                style= MaterialTheme.typography.titleLarge,
                                color = colorResource(id = R.color.themeColor), modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f),
                        ) {
                            Image(
                                painterResource(id =R.drawable.close),
                                contentDescription ="close",
                                modifier = Modifier
                                    .size(35.dp)
                                    .align(Alignment.End)
                                    .padding(end = 10.dp)
                                    .clickable {
                                        coroutineScope.launch {
                                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                bottomSheetScaffoldState.bottomSheetState.expand()
                                            } else {
                                                bottomSheetScaffoldState.bottomSheetState.collapse()
                                            }
                                        }
                                    },
                            )
                        }
                    }
                    HorizontalDivider(color = colorResource(id = R.color.divider))
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        Text(text = "Check-IN",
                            style = MaterialTheme.typography.titleSmall,
                            color = colorResource(id = R.color.paraColor)
                        )
                        Box( modifier = Modifier.padding(bottom = 10.dp)){
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(
                                readOnly = true,
                                value = textCheckIn,
                                onValueChange = { textCheckIn = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                modifier = Modifier.fillMaxWidth(1f)
                            )
                            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                Box(modifier = Modifier
                                    .background(
                                        color = colorResource(id = R.color.themeColor),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .size(58.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(painterResource(id = R.drawable.clock_1),
                                        contentDescription ="clock",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable { mTimePickerDialog.show() }
                                    )
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        Text(text = "Check-OUT",
                            style = MaterialTheme.typography.titleSmall,
                            color = colorResource(id = R.color.paraColor)
                        )
                        Box( modifier = Modifier.padding(bottom = 10.dp)){
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(
                                readOnly = true,
                                value =textCheckOut ,
                                onValueChange = { textCheckOut = it },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                            )
                            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                Box(modifier = Modifier
                                    .background(
                                        color = colorResource(id = R.color.themeColor),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .size(58.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(painterResource(id = R.drawable.clock_1) ,
                                        contentDescription ="clock" ,
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable { mTimePickerDialog2.show() }
                                    )
                                }
                            }
                        }
                    }
                    val isCommentNeed = attendanceRegularizeRules.value.any { it.isCommentNeed == 1}
                    if(isCommentNeed){
                        Column(modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                        ) {
                            TextField(
                                value = text,
                                onValueChange = { text = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .background(color = colorResource(id = R.color.backgroundColor)),

                                maxLines = 5,
                                textStyle = MaterialTheme.typography.titleMedium,
                                placeholder = { Text(text = "Remarks") },
                                singleLine = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = colorResource(id = R.color.black),
                                    focusedBorderColor = colorResource(id = R.color.themeColor),
                                    unfocusedBorderColor = colorResource(R.color.white),
                                )
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)) {

//                        Regularized two
                        val isCommentNeedOne = attendanceRegularizeRules.value.any { it.isCommentNeed == 1}
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val checkinDate = "$regularizedSelectedDate $textCheckIn"
                                    val checkoutDate = "$regularizedSelectedDate $textCheckOut"
                                    if (textCheckIn.isEmpty()) {
                                        Constant.showToast(context, "Check-in time is required")
                                    } else if (textCheckOut.isEmpty()) {
                                        Constant.showToast(context, "Check-out time is required")
                                    } else if (isCommentNeedOne && text.isEmpty()) {
                                        Constant.showToast(context, "Remarks are required")
                                    } else{
                                        val regularizedCount: List<Int> = attendanceRegularizeCount.value.map { it.count }
                                        Log.d("regularizedCount", "$regularizedCount")

                                        val adjustDays: List<Int> = attendanceRegularizeRules.value.map { it.adjustDays }
                                        Log.d("adjustDays", "${adjustDays.first()}")

                                        val canRegularize = regularizedCount.zip(adjustDays).all { (rc, ad) -> rc < ad }
                                        Log.d("canRegularize", "$canRegularize")
                                        if (canRegularize) {
                                            attendance_regularizedViewModel.getAttendanceRegulaziedDetails(
                                                empId = empID,
                                                org = org,
                                                MissedDate = regularizedSelectedDate,
                                                StartTime = checkinDate,
                                                EndTime = checkoutDate,
                                                Remarks = text,
                                                navController=navController,
                                                context=context,
                                            ).let {
                                                /*Constant.showToast(context, "Regularized successfully")
                                                navController.navigate("Attendance")*/
                                            }
                                        } else {
                                            Constant.showToast(context, "You can only regularize $adjustDays times")
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp)
                        )
                        {
                            Text(text = "Regularize", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = Color.Black,
        sheetElevation = 0.dp,
        sheetShape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)

        ) {
            Row()
            {
                Column(modifier = Modifier) {
                    Row {
                        Column(modifier = Modifier.padding(end = 10.dp)) {
                            Button(
                                onClick = { navController.navigate("leave") },
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                                contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp, start = 15.dp, end = 15.dp)
                            ) {
                                Row(Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                                    Icon(
                                        painterResource(id = R.drawable.attendance),
                                        contentDescription = "leave",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                    Text(
                                        text = "Leave",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                }

                            }
                        }

/*
                        //TEAM ATTENDANCE
                        Column() {
                            Button(
                                onClick = { navController.navigate("teamAttendance") },
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blue)),
                                modifier = Modifier.fillMaxWidth(1f),
                                contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp, start = 15.dp, end = 15.dp)
                            ) {

                                Row(Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                                    Icon(
                                        painterResource(id = R.drawable.users),
                                        contentDescription = "TEAM",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                    Text(
                                        text = "Team Attendance",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        */
                    }
                }
            }


            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade)),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            ) {
                Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                painterResource(id = R.drawable.attendance),
                                contentDescription = "attendance",
                                tint = colorResource(id = R.color.themeColor),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column(modifier = Modifier.clickable {
                            calendarFromState.show()
//                            expanded = false
//                            expandedIndex = -1
                        }) {
                            Text(
                                text = selectedFromDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black)
                            )
                        }
                        Column {
                            Icon(
                                painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                contentDescription = "front",
                                tint = colorResource(id = R.color.themeColor)
                            )
                        }
                        Column(modifier = Modifier.clickable {
                            calendarToState.show()
//                            expanded = false
//                            expandedIndex = -1
                        }) {
                            Text(
                                text = selectedToDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black)

                            )
                        }
                    }
                }
            }
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.padding(top = 3.dp, bottom = 3.dp).verticalScroll(scrollState)) {

                if (isLoading) {
                    // Show your loading indicator here
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                else if(!attendanceList.value.isNullOrEmpty()){
                    for ((index, data) in attendanceList.value.withIndex()) {
                        val inputDateFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        val outputDateFormat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val typeAR=data.type!="AR"

                        fun regularize()
                        {
                            val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                            regularizedSelectedDate = attendanceDate.format(outputDateFormat)

                            Log.d("Button Clicked Date", "Date: $regularizedSelectedDate")
                            coroutineScope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }

                        var isExpanded = index == expandedIndex
                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            onClick = {
                                // Toggle the expanded state
                                isExpanded = !isExpanded

                                // Update the expanded index
                                expandedIndex = if (isExpanded) index else -1

                                regularizedSelectedDateCICO = data.attendance_Date
                                // Assuming data.attendance_Date is in the "dd/MM/yyyy" format
                                val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                                try {
                                    // Parse the existing date format into a LocalDate
                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)

                                    // Format the LocalDate into your desired format
                                    val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)

                                    coroutineScope.launch {
                                        attendance_regularizedViewModel.getAttendenceCICORegularizedDetails(navController=navController, context = context,empId = empID, DT = regularizedSelectedDateCICO)
                                        attendance_regularizedViewModel.getAttendenceRBHRegularizedDetails(context = context, navController = navController, empId = empID, FT = regularizedSelectedDateCICO)
                                    }
                                } catch (e: Exception) {
                                    // Reset loading indicator in case of an error
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.padding(top = 3.dp, bottom = 3.dp).animateContentSize(
                                animationSpec = tween(
                                    delayMillis = 300,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                        ) {

                            val currentDate: LocalDate = LocalDate.now()
                            val currentFo =  currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            val isFutureDate = LocalDate.parse(data.attendance_Date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).isAfter(currentDate)
                            if(data.notes == "Absent" && isFutureDate || currentFo == data.attendance_Date && data.notes == "Absent" || data.notes == "Absent" || data.notes == "Holiday" || data.notes =="WeekOff" || data.notes == "Weekly Off" || data.notes =="Leave"){
                                Row(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(
                                        top = 12.dp,
                                        bottom = 10.dp,
                                        start = 14.dp,
                                        end = 14.dp
                                    )){
                                    Column(modifier = Modifier.weight(1f)){
                                        Row(modifier = Modifier.fillMaxWidth(1f)){
                                            Column(modifier = Modifier.weight(0.8f)){
                                                Text(
                                                    text = "${data.month} ${data.day} ${data.day_name}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.black)
                                                )
                                            }
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f),horizontalAlignment = Alignment.End) {
                                        if(currentFo == data.attendance_Date && data.notes == "Absent"){
                                            Text(
                                                text = "No logged entries",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                            )
                                        }
                                        else if(data.notes == "Absent" && isFutureDate){
                                            Text(
                                                text = "--",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.themeColor),
                                            )

                                        }else if(data.notes == "Absent"){
                                            Text(
                                                text = data.notes,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.red),
                                            )

                                        } else if (data.notes == "Holiday"){
                                            Text(
                                                text = data.notes,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.green),
                                            )
                                        } else if(data.notes == "WeekOff"){
                                            Text(
                                                text = data.notes,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.yellow),
                                            )
                                        }else if(data.notes == "Weekly Off"){
                                            Text(
                                                text = data.notes,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.themeColor),
                                            )
                                        }else if (data.notes == "Leave"){
                                            Text(
                                                text = data.notes,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.blue),
                                            )
                                        }
                                    }
                                }

                            }
                            else{
                                Row(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 10.dp,
                                        start = 14.dp,
                                        end = 14.dp
                                    )){
                                    Column(modifier = Modifier.weight(1f)){
                                        Row(Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween){
                                            Column(){
                                                Text(
                                                    text = "${data.month} ${data.day} ${data.day_name}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.black)
                                                )
                                            }
                                            Column(modifier = Modifier.padding(start = 10.dp),horizontalAlignment = Alignment.End){
                                                if(data.type.isNullOrEmpty()){
                                                    Text(
                                                        text = "",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = colorResource(id = R.color.blue)
                                                    )
                                                }
                                                else{
                                                    Text(
                                                        text = "${data.type}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = colorResource(id = R.color.blue),
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(start = 14.dp, end = 14.dp, bottom = 10.dp)) {
                                    Column(modifier = Modifier.weight(1f)){
                                        Row(modifier = Modifier.fillMaxWidth(1f),
                                            horizontalArrangement = Arrangement.SpaceBetween){
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "Check-IN",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = colorResource(id = R.color.paraColor),
                                                )
                                                Text(
                                                    text = data.checkin_Time,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.green),
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = "Check-OUT",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = colorResource(id = R.color.paraColor),
                                                )
                                                Text(
                                                    //                                                    text = data.checkout_Time,
                                                    text = if (data.checkout_Time.isNullOrEmpty())"--" else data.checkout_Time,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.red),
                                                )
                                            }
                                        }
                                    }
                                }
                                HorizontalDivider(thickness = 1.dp,color = colorResource(R.color.backgroundColor))
                                Column(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(start = 14.dp, end = 14.dp, bottom = 10.dp, top=10.dp)) {
                                    Row(){
                                        Column() {
                                            Text(
                                                text = "Total Working",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                            )
                                        }
                                        Column(modifier = Modifier.padding(start = 10.dp)) {
                                            Text(
                                                text = if (data.working_hours.isNullOrEmpty()) "00:00" else data.working_hours,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.yellow),
                                            )
                                        }
                                    }
                                }
                            }

                            if (isExpanded) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = colorResource(R.color.backgroundColor)
                                )
                                if (attendanceCICOList.value.isNullOrEmpty()) {
                                    Log.d("attendanceCICOList.value","${attendanceCICOList.value}")

                                    Text(
                                        text = "No Record Found",
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center,
                                        color = colorResource(id = R.color.black),
                                        modifier = Modifier
                                            .padding(top = 5.dp, bottom = 5.dp)
                                            .fillMaxWidth(1f)
                                    )
                                }
                                else {
                                    for (event in attendanceCICOList.value) {
                                        Column {
                                            if( event.notes == "Absent" || event.notes == "Holiday" || event.notes =="WeekOff" || event.notes == "Weekly Off" || event.notes =="Leave"){
                                                Log.d("event notes","${event.notes} event notes")
                                                Text(
                                                    text = "No Record Found", style = MaterialTheme.typography.titleMedium,
                                                    textAlign = TextAlign.Center,
                                                    color= colorResource(id = R.color.black),
                                                    modifier = Modifier
                                                        .padding(top = 5.dp, bottom = 5.dp)
                                                        .fillMaxWidth(1f)
                                                )
                                            }else{
                                                // check in location
                                                if(event.checkin_time != null) {
                                                    Column(
                                                        modifier = Modifier.padding(
                                                            top = 0.dp,
                                                            bottom = 0.dp,
                                                            start = 10.dp,
                                                            end = 10.dp
                                                        )
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(1f),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Row {
                                                                    Icon(
                                                                        painter = painterResource(id = R.drawable.location),
                                                                        contentDescription = "location",
                                                                        tint = colorResource(id = R.color.paraColor),
                                                                        modifier = Modifier
                                                                            .padding(end = 10.dp)
                                                                            .width(20.dp)
                                                                            .height(20.dp)
                                                                    )
                                                                    BasicTextField(
                                                                        readOnly = true,
                                                                        value = if (event.checkinLoc.isNullOrEmpty()) "--" else event.checkinLoc,
                                                                        onValueChange = { /* Handle value change if needed */ },
                                                                        textStyle = TextStyle(
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                                            color = colorResource(id = R.color.paraColor)
                                                                        ),
                                                                        singleLine = true,
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .padding(end = 16.dp) // Adjust padding as needed
                                                                    )
                                                                }
                                                            }

                                                            Column(
                                                                modifier = Modifier.weight(0.5f),
                                                                horizontalAlignment = Alignment.End
                                                            ) {
                                                                Button(
                                                                    onClick = { /*TODO*/ },
                                                                    colors = ButtonDefaults.buttonColors(
                                                                        colorResource(id = R.color.toolight_green)
                                                                    ),
                                                                    shape = RoundedCornerShape(20),
                                                                    contentPadding = PaddingValues(
                                                                        top = 5.dp,
                                                                        bottom = 5.dp,
                                                                        start = 10.dp,
                                                                        end = 10.dp
                                                                    )
                                                                ) {
                                                                    Text(
                                                                        text = event.checkin_time,
                                                                        style = TextStyle(
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                                            color = colorResource(
                                                                                id = R.color.green
                                                                            )
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                //check out location
                                                if(event.checkout_time != null){
                                                    Column(modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 10.dp, end=10.dp)) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(1f),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Column(modifier = Modifier.weight(1f)) {
                                                                Row {
                                                                    Icon(
                                                                        painter = painterResource(id = R.drawable.location),
                                                                        contentDescription = "location",
                                                                        tint = colorResource(id = R.color.paraColor),
                                                                        modifier = Modifier
                                                                            .padding(end = 10.dp)
                                                                            .width(20.dp)
                                                                            .height(20.dp)
                                                                    )
                                                                    BasicTextField(
                                                                        readOnly = true,
                                                                        value = if (event.checoutLoc.isNullOrEmpty()) "--" else event.checoutLoc,
                                                                        onValueChange = { /* Handle value change if needed */ },
                                                                        textStyle = TextStyle(
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                                            color = colorResource(id = R.color.paraColor)
                                                                        ),
                                                                        singleLine = true,
                                                                        modifier = Modifier
                                                                            .fillMaxWidth()
                                                                            .padding(end = 16.dp) // Adjust padding as needed
                                                                    )
                                                                }
                                                            }
                                                            Column(
                                                                modifier = Modifier.weight(0.5f),
                                                                horizontalAlignment = Alignment.End
                                                            ) {
                                                                Button(
                                                                    onClick = { /*TODO*/ },
                                                                    colors = ButtonDefaults.buttonColors(
                                                                        colorResource(id = R.color.toolight_red)
                                                                    ),
                                                                    shape = RoundedCornerShape(20),
                                                                    contentPadding = PaddingValues(
                                                                        top = 5.dp,
                                                                        bottom = 5.dp,
                                                                        start = 10.dp,
                                                                        end = 10.dp
                                                                    )
                                                                ) {
                                                                    Text(
                                                                        text = event.checkout_time,
                                                                        style = TextStyle(
                                                                            fontSize = 12.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                                            color = colorResource(id = R.color.red)
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = colorResource(R.color.backgroundColor)
                                        )
                                    }
                                }
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = colorResource(R.color.backgroundColor)
                                )
                                // regularized one
                                Column(modifier = Modifier.padding(10.dp)) {
                                    val isRegularizeNeedc1 = attendanceRegularizeRules.value.any { it.isRegularizeNeed == 1 && it.criteria == 1}
                                    val isRegularizeNeedc2 = attendanceRegularizeRules.value.any { it.isRegularizeNeed == 1 && it.criteria == 2}
                                    val isRegularizeNeedc3 = attendanceRegularizeRules.value.any { it.isRegularizeNeed == 1 && it.criteria == 3 && it.criteriaTime >= 0}
                                    val isRestrictionNeed = attendanceRegularizeRules.value.any { it.isRestrictionNeed == 1 && it.restrictDays >= 0}
                                    //val isRestrictionNeed_0 = attendanceRegularizeRules.value.any { it.isRestrictionNeed == 0}
                                    Log.d("isRestrictionNeed","$isRestrictionNeed")
                                    val restrictDays: List<Int> = attendanceRegularizeRules.value.map { it.restrictDays }
                                    Log.d("restrictDays","$restrictDays")
                                    val flagRBH: List<Int> = attendanceRBHList.value.map { it.flag }
                                    Log.d("flagRBH List","$flagRBH List")
                                    val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    if(isRegularizeNeedc1 || isRegularizeNeedc2 || isRegularizeNeedc3){
                                        if (LocalDate.parse(data.attendance_Date, inputDateFormat).isBefore(LocalDate.now())) {
                                            if(isRegularizeNeedc1)
                                            {
                                                if(isRestrictionNeed)
                                                {
                                                    if( restrictDays.isNotEmpty() && restrictDays.first() == 0){
                                                        Log.d("restrictDays.first() is 0","restrictDays.first() is 0")
                                                        if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                            if(typeAR){
                                                                Button(
                                                                    onClick = {
                                                                        Log.d("At Button Click ", "scenerio 1 ${regularizedSelectedDate}")

                                                                        val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                        regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                        val flag = checkRegularize(regularizedSelectedDate)
                                                                        Log.d("In Button One", "$flag")

                                                                        if(flag)
                                                                        {
                                                                            regularize()
                                                                        }
                                                                        else
                                                                        {
                                                                            regularize()
                                                                        }

                                                                    },
                                                                    shape = RoundedCornerShape(20),
                                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                    contentPadding = PaddingValues(
                                                                        top = 5.dp,
                                                                        bottom = 5.dp,
                                                                        start = 10.dp,
                                                                        end = 10.dp
                                                                    )
                                                                ) {
                                                                    Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                        Icon(
                                                                            painterResource(id = R.drawable.clock_1),
                                                                            contentDescription = "clock",
                                                                            modifier = Modifier
                                                                                .padding(end = 10.dp)
                                                                                .width(20.dp)
                                                                                .height(20.dp),
                                                                            tint = Color.White
                                                                        )
                                                                        Text(
                                                                            text = "Regularize",
                                                                            style = TextStyle(
                                                                                fontSize = 14.sp,
                                                                                fontWeight = FontWeight(500),
                                                                                color = Color.White
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else if(restrictDays.isNotEmpty() && restrictDays.first() > 0){
                                                        Log.d("restrictDays.first() is greater than 0","restrictDays.first() is greater than 0")
                                                        val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                        val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                        val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                        val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)
                                                        Log.d("Clicked Date CiCo two", "Date: $regularizedSelectedDateCICO")
                                                        val currentDate = LocalDate.now()
                                                        val dateRange = attendanceDate..currentDate
                                                        val numberOfDaysBetween = ChronoUnit.DAYS.between(attendanceDate, currentDate)
                                                        val canRegularize =  restrictDays.first()
                                                        val days_between_check =numberOfDaysBetween<=canRegularize
                                                        Log.d("days_between_check", "days_between_check $days_between_check")
                                                        if(numberOfDaysBetween<=canRegularize){
                                                            if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                                if(typeAR){
                                                                    Button(
                                                                        onClick = {


                                                                            val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                            regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                            Log.d("At Button Click ", "scenerio 2 ${regularizedSelectedDate}")

                                                                            val flag = checkRegularize(regularizedSelectedDate)
                                                                            Log.d("In Button two", "$flag")

                                                                            if(flag)
                                                                            {
                                                                                regularize()
                                                                            }
                                                                            else
                                                                            {
                                                                                regularize()
                                                                            }

                                                                        },
                                                                        shape = RoundedCornerShape(20),
                                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                        contentPadding = PaddingValues(
                                                                            top = 5.dp,
                                                                            bottom = 5.dp,
                                                                            start = 10.dp,
                                                                            end = 10.dp
                                                                        )
                                                                    ) {
                                                                        Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                            Icon(
                                                                                painterResource(id = R.drawable.clock_1),
                                                                                contentDescription = "clock",
                                                                                modifier = Modifier
                                                                                    .padding(end = 10.dp)
                                                                                    .width(20.dp)
                                                                                    .height(20.dp),
                                                                                tint = Color.White
                                                                            )
                                                                            Text(
                                                                                text = "Regularize",
                                                                                style = TextStyle(
                                                                                    fontSize = 14.sp,
                                                                                    fontWeight = FontWeight(500),
                                                                                    color = Color.White
                                                                                )
                                                                            )
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                        if(typeAR){
                                                            Button(
                                                                onClick = {

                                                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                    regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                    Log.d("At Button Click ", "scenerio 3 ${regularizedSelectedDate}")


                                                                    val flag = checkRegularize(regularizedSelectedDate)
                                                                    Log.d("In Button three", "$flag")

                                                                    if(flag)
                                                                    {
                                                                        regularize()
                                                                    }
                                                                    else
                                                                    {
                                                                        regularize()
                                                                    }


                                                                },
                                                                shape = RoundedCornerShape(20),
                                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                contentPadding = PaddingValues(
                                                                    top = 5.dp,
                                                                    bottom = 5.dp,
                                                                    start = 10.dp,
                                                                    end = 10.dp
                                                                )
                                                            ) {
                                                                Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                    Icon(
                                                                        painterResource(id = R.drawable.clock_1),
                                                                        contentDescription = "clock",
                                                                        modifier = Modifier
                                                                            .padding(end = 10.dp)
                                                                            .width(20.dp)
                                                                            .height(20.dp),
                                                                        tint = Color.White
                                                                    )
                                                                    Text(
                                                                        text = "Regularize",
                                                                        style = TextStyle(
                                                                            fontSize = 14.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            color = Color.White
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                            //Only missed Punch
                                            else if (isRegularizeNeedc2) {
//                                            val coNull = attendanceCICOList.value.any { it.checkout_time.isNullOrEmpty()}
                                                val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                regularizedSelectedDate = attendanceDate.format(outputDateFormat)

                                                Log.d("Button Clicked Date", "Date: $regularizedSelectedDate")
                                                val attendanceForSelectedDate = attendanceList.value.filter {
                                                    LocalDate.parse(it.attendance_Date, inputDateFormat).format(outputDateFormat) == regularizedSelectedDate
                                                }
                                                val checkoutTimesForSelectedDateList: List<String> = attendanceForSelectedDate.map { it.checkout_Time }
                                                val notesForSelectedDateList: List<String> = attendanceForSelectedDate.map { it.notes }
//                                            val checkoutTimesForSelectedDate = checkoutTimesForSelectedDateList.first().isNullOrEmpty()
                                                val coNull = checkoutTimesForSelectedDateList.first().isNullOrEmpty()
                                                val notesForSelectedDate = notesForSelectedDateList.first()
                                                Log.d("Checkout Times", "Checkout Times for Selected Date: ${checkoutTimesForSelectedDateList.first()}")
                                                // Log the filtered attendance list
                                                Log.d("regularizedSelectedDate", "Regularized Selected Date: $regularizedSelectedDate")
                                                Log.d("attendanceList", "All Attendance Records: ${attendanceList.value}")
                                                Log.d("attendanceForSelectedDate", "Attendance for Selected Date: $attendanceForSelectedDate")
//                                             val coNull = attendanceList.value.any { it.checkout_Time.isNullOrEmpty()}
                                                val checkout_Time: List<String> = attendanceList.value.map { it.checkout_Time }
                                                val attendance_Date: List<String> = attendanceList.value.map { it.attendance_Date }


                                                Log.d("notesList","${notesForSelectedDate}")
                                                Log.d("checkout_Time","${checkout_Time}")
                                                Log.d("attendance_Date","${attendance_Date}")
                                                Log.d("coNull","${coNull}")
                                                Log.d("isRestrictionNeed inside","$isRestrictionNeed")
                                                if(notesForSelectedDate != "Holiday" && notesForSelectedDate != "WeekOff" && notesForSelectedDate != "Weekly Off" && notesForSelectedDate != "Leave") {
                                                    if (isRestrictionNeed) {
                                                        Log.d("isRestrictionNeed if inside","$isRestrictionNeed")

                                                        if(coNull){
                                                            if( restrictDays.isNotEmpty() && restrictDays.first() == 0){
                                                                if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                                    Log.d("isRegularizeNeedc2 restrictDays","isRegularizeNeedc2 first if ==0 ${restrictDays.first()}")
                                                                    if(typeAR){
                                                                        Button(
                                                                            onClick = {

                                                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                                Log.d("At Button Click ", "scenerio 4 ${regularizedSelectedDate}")


                                                                                val flag = checkRegularize(regularizedSelectedDate)
                                                                                Log.d("In Button2 one ", "$flag")

                                                                                if(flag)
                                                                                {
                                                                                    regularize()
                                                                                }
                                                                                else
                                                                                {
                                                                                    regularize()
                                                                                }


                                                                            },
                                                                            shape = RoundedCornerShape(20),
                                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                            contentPadding = PaddingValues(
                                                                                top = 5.dp,
                                                                                bottom = 5.dp,
                                                                                start = 10.dp,
                                                                                end = 10.dp
                                                                            )
                                                                        ) {
                                                                            Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                Icon(
                                                                                    painterResource(id = R.drawable.clock_1),
                                                                                    contentDescription = "clock",
                                                                                    modifier = Modifier
                                                                                        .padding(end = 10.dp)
                                                                                        .width(20.dp)
                                                                                        .height(20.dp),
                                                                                    tint = Color.White
                                                                                )
                                                                                Text(
                                                                                    text = "Regularize",
                                                                                    style = TextStyle(
                                                                                        fontSize = 14.sp,
                                                                                        fontWeight = FontWeight(500),
                                                                                        color = Color.White
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }
                                                            else if(restrictDays.isNotEmpty() && restrictDays.first() > 0){
                                                                Log.d("isRegularizeNeedc2 restrictDays","isRegularizeNeedc2 first if >0 ${restrictDays.first()}")
                                                                val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                                Log.d("inputDateFormat","$inputDateFormat")
                                                                val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                                Log.d("inputDateFormat","$inputDateFormat")
                                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                                Log.d("attendanceDate","$attendanceDate")
                                                                val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)
                                                                Log.d("Clicked Date CiCo two", "Date: $regularizedSelectedDateCICO")
                                                                val currentDate = LocalDate.now()
                                                                Log.d("currentDate","$currentDate")
                                                                val dateRange = attendanceDate..currentDate
                                                                val numberOfDaysBetween = ChronoUnit.DAYS.between(attendanceDate, currentDate)
                                                                Log.d("numberOfDaysBetween","$numberOfDaysBetween")
                                                                val canRegularize =  restrictDays.first()
                                                                Log.d("canRegularize","$canRegularize")
                                                                val days_between_check =numberOfDaysBetween<=canRegularize
                                                                Log.d("days_between_check", "days_between_check $days_between_check")
                                                                if(numberOfDaysBetween<=canRegularize){
                                                                    if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){

                                                                        if(typeAR){
                                                                            Button(
                                                                                onClick = {

                                                                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                    regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                                    Log.d("At Button Click ", "scenerio 5 ${regularizedSelectedDate}")


                                                                                    val flag = checkRegularize(regularizedSelectedDate)
                                                                                    Log.d("In Button2 two", "$flag")

                                                                                    if(flag)
                                                                                    {
                                                                                        regularize()
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        regularize()
                                                                                    }


                                                                                },
                                                                                shape = RoundedCornerShape(20),
                                                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                                contentPadding = PaddingValues(
                                                                                    top = 5.dp,
                                                                                    bottom = 5.dp,
                                                                                    start = 10.dp,
                                                                                    end = 10.dp
                                                                                )
                                                                            ) {
                                                                                Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                    Icon(
                                                                                        painterResource(id = R.drawable.clock_1),
                                                                                        contentDescription = "clock",
                                                                                        modifier = Modifier
                                                                                            .padding(end = 10.dp)
                                                                                            .width(20.dp)
                                                                                            .height(20.dp),
                                                                                        tint = Color.White
                                                                                    )
                                                                                    Text(
                                                                                        text = "Regularize",
                                                                                        style = TextStyle(
                                                                                            fontSize = 14.sp,
                                                                                            fontWeight = FontWeight(500),
                                                                                            color = Color.White
                                                                                        )
                                                                                    )
                                                                                }
                                                                            }
                                                                        }

                                                                    }

                                                                }

                                                            }
                                                        }
                                                        else if(attendanceCICOList.value.isNullOrEmpty()){
                                                            Log.d("isRegularizeNeedc2 restrictDays","isRegularizeNeedc2 else last if >0 ${restrictDays.first()}")
                                                            val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                            Log.d("inputDateFormat","$inputDateFormat")
                                                            val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                            Log.d("inputDateFormat","$inputDateFormat")
                                                            val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                            Log.d("attendanceDate","$attendanceDate")
                                                            val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)
                                                            Log.d("Clicked Date CiCo two", "Date: $regularizedSelectedDateCICO")
                                                            val currentDate = LocalDate.now()
                                                            Log.d("currentDate","$currentDate")
                                                            val dateRange = attendanceDate..currentDate
                                                            val numberOfDaysBetween = ChronoUnit.DAYS.between(attendanceDate, currentDate)
                                                            Log.d("numberOfDaysBetween","$numberOfDaysBetween")
                                                            val canRegularize =  restrictDays.first()
                                                            Log.d("canRegularize","$canRegularize")
                                                            val days_between_check =numberOfDaysBetween<=canRegularize
                                                            Log.d("days_between_check", "days_between_check $days_between_check")
                                                            if(numberOfDaysBetween<=canRegularize){
                                                                if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){   //checked line

                                                                    if(typeAR){
                                                                        Button(
                                                                            onClick = {
                                                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                                Log.d("At Button Click ", "scenerio 6 ${regularizedSelectedDate}")


                                                                                val flag = checkRegularize(regularizedSelectedDate)
                                                                                Log.d("In Button2 three", "$flag")

                                                                                if(flag)
                                                                                {
                                                                                    regularize()
                                                                                }
                                                                                else
                                                                                {
                                                                                    regularize()
                                                                                }


                                                                            },
                                                                            shape = RoundedCornerShape(20),
                                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                            contentPadding = PaddingValues(
                                                                                top = 5.dp,
                                                                                bottom = 5.dp,
                                                                                start = 10.dp,
                                                                                end = 10.dp
                                                                            )
                                                                        ) {
                                                                            Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                Icon(
                                                                                    painterResource(id = R.drawable.clock_1),
                                                                                    contentDescription = "clock",
                                                                                    modifier = Modifier
                                                                                        .padding(end = 10.dp)
                                                                                        .width(20.dp)
                                                                                        .height(20.dp),
                                                                                    tint = Color.White
                                                                                )
                                                                                Text(
                                                                                    text = "Regularize",
                                                                                    style = TextStyle(
                                                                                        fontSize = 14.sp,
                                                                                        fontWeight = FontWeight(500),
                                                                                        color = Color.White
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    }

                                                                }
                                                            }

                                                        }
                                                    }
                                                    else if(coNull) {
                                                        Log.d("coNull inside","${coNull}")
                                                        if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                            Log.d("flagRBH List","$flagRBH List inside else if")
                                                            Log.d("isRegularizeNeedc2 restrictDays","isRegularizeNeedc2 first if ==0 ${restrictDays.first()}")


                                                            if(typeAR){
                                                                Button(
                                                                    onClick = {

                                                                        val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                        regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                        Log.d("At Button Click ", "scenerio 7 ${regularizedSelectedDate}")


                                                                        val flag = checkRegularize(regularizedSelectedDate)
                                                                        Log.d("In Button2 four", "$flag")

                                                                        if(flag)
                                                                        {
                                                                            regularize()
                                                                        }
                                                                        else
                                                                        {
                                                                            regularize()
                                                                        }

                                                                    },
                                                                    shape = RoundedCornerShape(20),
                                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                    contentPadding = PaddingValues(
                                                                        top = 5.dp,
                                                                        bottom = 5.dp,
                                                                        start = 10.dp,
                                                                        end = 10.dp
                                                                    )
                                                                ) {
                                                                    Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                        Icon(
                                                                            painterResource(id = R.drawable.clock_1),
                                                                            contentDescription = "clock",
                                                                            modifier = Modifier
                                                                                .padding(end = 10.dp)
                                                                                .width(20.dp)
                                                                                .height(20.dp),
                                                                            tint = Color.White
                                                                        )
                                                                        Text(
                                                                            text = "Regularize",
                                                                            style = TextStyle(
                                                                                fontSize = 14.sp,
                                                                                fontWeight = FontWeight(500),
                                                                                color = Color.White
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else if(attendanceCICOList.value.isNullOrEmpty()){
                                                        Log.d("attendanceCICOList.value.isNullOrEmpty()","attendanceCICOList.value.isNullOrEmpty()")
                                                        if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){   //checked line
                                                            if(typeAR){
                                                                Button(
                                                                    onClick = {

                                                                        val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                        regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                        Log.d("At Button Click ", "scenerio 8 ${regularizedSelectedDate}")

                                                                        val flag = checkRegularize(regularizedSelectedDate)
                                                                        Log.d("In Button dfgdfg", "$flag")

                                                                        if(flag)
                                                                        {
                                                                            regularize()
                                                                        }
                                                                        else
                                                                        {
                                                                            regularize()
                                                                        }
                                                                    },
                                                                    shape = RoundedCornerShape(20),
                                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                    contentPadding = PaddingValues(
                                                                        top = 5.dp,
                                                                        bottom = 5.dp,
                                                                        start = 10.dp,
                                                                        end = 10.dp
                                                                    )
                                                                ) {
                                                                    Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                        Icon(
                                                                            painterResource(id = R.drawable.clock_1),
                                                                            contentDescription = "clock",
                                                                            modifier = Modifier
                                                                                .padding(end = 10.dp)
                                                                                .width(20.dp)
                                                                                .height(20.dp),
                                                                            tint = Color.White
                                                                        )
                                                                        Text(
                                                                            text = "Regularize",
                                                                            style = TextStyle(
                                                                                fontSize = 14.sp,
                                                                                fontWeight = FontWeight(500),
                                                                                color = Color.White
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }

                                                        }
                                                    }
                                                }

                                            }
                                            //Minutes Based
                                            else if (isRegularizeNeedc3) {
                                                Log.d("isRegularizeNeedc3","$isRegularizeNeedc3")
                                                Log.d("data.working_minutes","${data.working_minutes.isNotEmpty()} data.working_minutes")
                                                if(isRestrictionNeed){
                                                    if (data.working_minutes.isNotEmpty()) {
                                                        Log.d("data.working_minutes","${data.working_minutes}data.working_minutes inside if")
                                                        val regularizedSelectedDateWorkingHour = data.diff_minutes.toInt()
                                                        Log.d("regularizedSelectedDateWorkingHour","$regularizedSelectedDateWorkingHour")
                                                        val criteriaTime: List<Int> = attendanceRegularizeRules.value.map { it.criteriaTime }
                                                        Log.d("criteriaTime","${criteriaTime.firstOrNull()}")
                                                        if(data.diff_minutes.toInt()!=0){
                                                            if (regularizedSelectedDateWorkingHour > criteriaTime.firstOrNull()!!) {
                                                                if(restrictDays.isNotEmpty() && restrictDays.first() == 0){
                                                                    var type=data.type!="AR"
                                                                    Log.d("type min1","${type}")
                                                                    if(type){
                                                                        Log.d("type min2","${type}")
                                                                        Button(
                                                                            onClick = {
                                                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                                Log.d("At Button Click ", "scenerio 9 ${regularizedSelectedDate}")


                                                                                val flag = checkRegularize(regularizedSelectedDate)
                                                                                Log.d("In Button2 four", "$flag")

                                                                                if(flag)
                                                                                {
                                                                                    regularize()
                                                                                }
                                                                                else
                                                                                {
                                                                                    regularize()
                                                                                }


                                                                            },
                                                                            shape = RoundedCornerShape(20),
                                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                            contentPadding = PaddingValues(
                                                                                top = 5.dp,
                                                                                bottom = 5.dp,
                                                                                start = 10.dp,
                                                                                end = 10.dp
                                                                            )
                                                                        ) {
                                                                            Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                Icon(
                                                                                    painterResource(id = R.drawable.clock_1),
                                                                                    contentDescription = "clock",
                                                                                    modifier = Modifier
                                                                                        .padding(end = 10.dp)
                                                                                        .width(20.dp)
                                                                                        .height(20.dp),
                                                                                    tint = Color.White
                                                                                )
                                                                                Text(
                                                                                    text = "Regularize",
                                                                                    style = TextStyle(
                                                                                        fontSize = 14.sp,
                                                                                        fontWeight = FontWeight(500),
                                                                                        color = Color.White
                                                                                    )
                                                                                )
                                                                            }
                                                                        }
                                                                    }


                                                                }
                                                                else if(restrictDays.isNotEmpty() && restrictDays.first() > 0){
                                                                    val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                                    Log.d("inputDateFormat","$inputDateFormat")
                                                                    val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                                    Log.d("inputDateFormat","$inputDateFormat")
                                                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                                    Log.d("attendanceDate","$attendanceDate")
                                                                    val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)
                                                                    Log.d("Clicked Date CiCo two", "Date: $regularizedSelectedDateCICO")
                                                                    val currentDate = LocalDate.now()
                                                                    Log.d("currentDate","$currentDate")
                                                                    val dateRange = attendanceDate..currentDate
                                                                    val numberOfDaysBetween = ChronoUnit.DAYS.between(attendanceDate, currentDate)
                                                                    Log.d("numberOfDaysBetween","$numberOfDaysBetween")
                                                                    val canRegularize =  restrictDays.first()
                                                                    Log.d("canRegularize","$canRegularize")
                                                                    if(numberOfDaysBetween<=canRegularize){
                                                                        if(typeAR)
                                                                        {
                                                                            Button(
                                                                                onClick = {
                                                                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                    regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                                    Log.d("At Button Click ", "scenerio 10 ${regularizedSelectedDate}")


                                                                                    val flag = checkRegularize(regularizedSelectedDate)
                                                                                    Log.d("In Button2 four", "$flag")


                                                                                    if(flag)
                                                                                    {
                                                                                        regularize()
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        regularize()
                                                                                    }


                                                                                },
                                                                                shape = RoundedCornerShape(20),
                                                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                                contentPadding = PaddingValues(
                                                                                    top = 5.dp,
                                                                                    bottom = 5.dp,
                                                                                    start = 10.dp,
                                                                                    end = 10.dp
                                                                                )
                                                                            ) {
                                                                                Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                    Icon(
                                                                                        painterResource(id = R.drawable.clock_1),
                                                                                        contentDescription = "clock",
                                                                                        modifier = Modifier
                                                                                            .padding(end = 10.dp)
                                                                                            .width(20.dp)
                                                                                            .height(20.dp),
                                                                                        tint = Color.White
                                                                                    )
                                                                                    Text(
                                                                                        text = "Regularize",
                                                                                        style = TextStyle(
                                                                                            fontSize = 14.sp,
                                                                                            fontWeight = FontWeight(500),
                                                                                            color = Color.White
                                                                                        )
                                                                                    )
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            else{
                                                                Log.d("no button","no button")
                                                            }
                                                        }
                                                    }
                                                    else if(attendanceCICOList.value.isNullOrEmpty()){
                                                        Log.d("attendanceCICOList.value.isNullOrEmpty()","attendanceCICOList.value.isNullOrEmpty()")
                                                        val inputDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                                        Log.d("inputDateFormat","$inputDateFormat")
                                                        val outputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                                        Log.d("inputDateFormat","$inputDateFormat")
                                                        val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat)
                                                        Log.d("attendanceDate","$attendanceDate")
                                                        val regularizedSelectedDateCICO = attendanceDate.format(outputDateFormat)
                                                        Log.d("Clicked Date CiCo two", "Date: $regularizedSelectedDateCICO")
                                                        val currentDate = LocalDate.now()
                                                        Log.d("currentDate","$currentDate")
                                                        val dateRange = attendanceDate..currentDate
                                                        val numberOfDaysBetween = ChronoUnit.DAYS.between(attendanceDate, currentDate)
                                                        Log.d("numberOfDaysBetween","$numberOfDaysBetween")
                                                        val canRegularize =  restrictDays.first()
                                                        Log.d("canRegularize","$canRegularize")
                                                        if(numberOfDaysBetween<=canRegularize){
                                                            if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){   //checked line

                                                                if(typeAR){
                                                                    Button(
                                                                        onClick = {
                                                                            val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                            regularizedSelectedDate = attendanceDate.format(outputDateFormat1)
                                                                            Log.d("At Button Click ", "scenerio 11 ${regularizedSelectedDate}")


                                                                            val flag = checkRegularize(regularizedSelectedDate)
                                                                            Log.d("In Button2 four gfhf", "$flag")


                                                                            if(flag)
                                                                            {
                                                                                regularize()
                                                                            }
                                                                            else
                                                                            {
                                                                                regularize()
                                                                            }

                                                                        },
                                                                        shape = RoundedCornerShape(20),
                                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                        contentPadding = PaddingValues(
                                                                            top = 5.dp,
                                                                            bottom = 5.dp,
                                                                            start = 10.dp,
                                                                            end = 10.dp
                                                                        )
                                                                    ) {
                                                                        Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                            Icon(
                                                                                painterResource(id = R.drawable.clock_1),
                                                                                contentDescription = "clock",
                                                                                modifier = Modifier
                                                                                    .padding(end = 10.dp)
                                                                                    .width(20.dp)
                                                                                    .height(20.dp),
                                                                                tint = Color.White
                                                                            )
                                                                            Text(
                                                                                text = "Regularize",
                                                                                style = TextStyle(
                                                                                    fontSize = 14.sp,
                                                                                    fontWeight = FontWeight(500),
                                                                                    color = Color.White
                                                                                )
                                                                            )
                                                                        }
                                                                    }
                                                                }




                                                            }
                                                        }


                                                    }
                                                    else{
                                                        Log.d("data.working_minutes","${data.working_minutes}data.working_minutes inside if else")
                                                        if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){   //checked line
                                                            if(data.diff_minutes.toInt()!=0){
                                                                if(typeAR){
                                                                    Button(
                                                                        onClick = {
                                                                            val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                            regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                            Log.d("At Button Click ", "scenerio 12 ${regularizedSelectedDate}")


                                                                            val flag = checkRegularize(regularizedSelectedDate)
                                                                            Log.d("In Button2 four hgth", "$flag")


                                                                            if(flag)
                                                                            {
                                                                                regularize()
                                                                            }
                                                                            else
                                                                            {
                                                                                regularize()
                                                                            }

                                                                        },
                                                                        shape = RoundedCornerShape(20),
                                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                        contentPadding = PaddingValues(
                                                                            top = 5.dp,
                                                                            bottom = 5.dp,
                                                                            start = 10.dp,
                                                                            end = 10.dp
                                                                        )
                                                                    ) {
                                                                        Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                            Icon(
                                                                                painterResource(id = R.drawable.clock_1),
                                                                                contentDescription = "clock",
                                                                                modifier = Modifier
                                                                                    .padding(end = 10.dp)
                                                                                    .width(20.dp)
                                                                                    .height(20.dp),
                                                                                tint = Color.White
                                                                            )
                                                                            Text(
                                                                                text = "Regularize",
                                                                                style = TextStyle(
                                                                                    fontSize = 14.sp,
                                                                                    fontWeight = FontWeight(500),
                                                                                    color = Color.White
                                                                                )
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                else if(attendanceCICOList.value.isNullOrEmpty()){
                                                    if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){   //checked line

                                                        if(typeAR){
                                                            Button(
                                                                onClick = {

                                                                    val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                    regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                    Log.d("At Button Click ", "scenerio 13 ${regularizedSelectedDate}")


                                                                    val flag = checkRegularize(regularizedSelectedDate)

                                                                    Log.d("In Button2 four ghrftj", "$flag")


                                                                    if(flag)
                                                                    {
                                                                        regularize()
                                                                    }
                                                                    else
                                                                    {
                                                                        regularize()
                                                                    }


                                                                },
                                                                shape = RoundedCornerShape(20),
                                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                contentPadding = PaddingValues(
                                                                    top = 5.dp,
                                                                    bottom = 5.dp,
                                                                    start = 10.dp,
                                                                    end = 10.dp
                                                                )
                                                            ) {
                                                                Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                    Icon(
                                                                        painterResource(id = R.drawable.clock_1),
                                                                        contentDescription = "clock",
                                                                        modifier = Modifier
                                                                            .padding(end = 10.dp)
                                                                            .width(20.dp)
                                                                            .height(20.dp),
                                                                        tint = Color.White
                                                                    )
                                                                    Text(
                                                                        text = "Regularize",
                                                                        style = TextStyle(
                                                                            fontSize = 14.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            color = Color.White
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }

                                                    }
                                                }
                                                else{
                                                    if (data.working_minutes.isNotEmpty()) {
                                                        Log.d("data.working_minutes","${data.working_minutes}data.working_minutes inside last if")
                                                        val regularizedSelectedDateWorkingHour = data.diff_minutes.toInt()
                                                        Log.d("regularizedSelectedDateWorkingHour","$regularizedSelectedDateWorkingHour")
                                                        val criteriaTime: List<Int> = attendanceRegularizeRules.value.map { it.criteriaTime }
                                                        Log.d("criteriaTime","${criteriaTime.firstOrNull()}")
                                                        if(data.diff_minutes.toInt()!=0){
                                                            if (regularizedSelectedDateWorkingHour > criteriaTime.firstOrNull()!!) {
                                                                if(flagRBH.isNullOrEmpty() || (flagRBH.isNotEmpty() && flagRBH.first() == 0)){
                                                                    if(typeAR){
                                                                        Button(
                                                                            onClick = {
                                                                                val attendanceDate = LocalDate.parse(data.attendance_Date, inputDateFormat1)
                                                                                regularizedSelectedDate = attendanceDate.format(outputDateFormat1)

                                                                                Log.d("At Button Click ", "scenerio 14 ${regularizedSelectedDate}")


                                                                                val flag = checkRegularize(regularizedSelectedDate)
                                                                                Log.d("In Button2 four retgrg", "$flag")

                                                                                if(flag)
                                                                                {
                                                                                    regularize()
                                                                                }
                                                                                else
                                                                                {
                                                                                    regularize()
                                                                                }
                                                                            },
                                                                            shape = RoundedCornerShape(20),
                                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                                            contentPadding = PaddingValues(
                                                                                top = 5.dp,
                                                                                bottom = 5.dp,
                                                                                start = 10.dp,
                                                                                end = 10.dp
                                                                            )
                                                                        ) {
                                                                            Row(Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                                                                                Icon(
                                                                                    painterResource(id = R.drawable.clock_1),
                                                                                    contentDescription = "clock",
                                                                                    modifier = Modifier
                                                                                        .padding(end = 10.dp)
                                                                                        .width(20.dp)
                                                                                        .height(20.dp),
                                                                                    tint = Color.White
                                                                                )
                                                                                Text(
                                                                                    text = "Regularize",
                                                                                    style = TextStyle(
                                                                                        fontSize = 14.sp,
                                                                                        fontWeight = FontWeight(500),
                                                                                        color = Color.White
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
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                else{
                    noDataView()
                }
            }
        }
    }
}

