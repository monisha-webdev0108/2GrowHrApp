@file:Suppress("NAME_SHADOWING")

package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomSheetScaffold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveDurationResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveTypeDetailData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import com.google.gson.Gson
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.Date
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale

// Define a data class to represent each date's information
data class LeaveItem(
    val date: LocalDate,
    var leaveType: String = "--Select Type--", // Initialize as "--Select Type--"
    var selectedIndex: Int = -1, // Initialize as -1
)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LeaveApply(navController: NavController, leaveViewModel: LeaveViewModel)
{
    rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Leave Request",
            "leave"
        ) },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { LeaveApply_Screen(navController = navController, leaveViewModel = leaveViewModel) }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaveApply_Screen(navController: NavController, leaveViewModel: LeaveViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 10.dp)
    )
    {
        Box()
        {
            Scaffold { MyContent(navController = navController, leaveViewModel = leaveViewModel) }
        }
    }
}

// Function to format time in 12-hour format with AM/PM
fun formatTimeIn12HourFormat(hourOfDay: Int, minute: Int): String
{
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
    calendar.set(Calendar.MINUTE, minute)

    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}


// Function to parse time from a formatted string
private fun parseTime(timeString: String): Calendar?
{
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    try {
        val date = dateFormat.parse(timeString)
        if (date != null) {
            calendar.time = date
        }
        return calendar
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

//-----------test4------------

@Suppress("DEPRECATION")
@SuppressLint("PrivateResource")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyContent(navController: NavController, leaveViewModel: LeaveViewModel)
{

    val context = LocalContext.current
    LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()


    val empID = userViewModel.getSFCode()

    // Fetch and update the Leave list when entering the page
    var leaveTypeList by remember(empID) { mutableStateOf<List<LeaveTypeDetailData>>(emptyList()) }

    var flag by remember { mutableIntStateOf(0) }

    val leaveDurationList = mutableListOf<String>()
    var leaveDuration by remember { mutableStateOf("") }
    var selectedLeaveType by remember { mutableStateOf("") }
    var leaveTypeSelected by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var selectedOption by remember { mutableStateOf<String?>(null) }


    // Initialize a list of LeaveItem
    val leaveItems = mutableListOf<LeaveItem>()

    // variables for calculated values
    var availableBalance: String by remember { mutableStateOf("00") }
    var appliedValue: String by remember { mutableStateOf("00") }
    var remainingValue: String by remember { mutableStateOf("00") }
    var appliedHours: String by remember { mutableStateOf("1 : 00") }

    var leaveListFlag by remember { mutableIntStateOf(0) }

    val showLoad =   remember { mutableStateOf(false) }

//    val showLoad by remember { mutableStateOf(leaveViewModel.loading)}

    val leaveListState = leaveViewModel.leaveList.collectAsState()
    leaveTypeList = leaveListState.value

    Log.d("LeaveType... LeaveTypeListDetails", "Launch : $leaveTypeList")
    Log.d("LeaveType... LeaveTypeListDetails", "Flag Before set : $leaveListFlag")
    leaveListFlag = if(leaveTypeList.isEmpty()) {1} else {2}
    Log.d("LeaveType... LeaveTypeListDetails", "Flag After set : $leaveListFlag")



    fun validateTime(startTime: MutableState<String>, endTime: MutableState<String>, previousStartTime: MutableState<String>, previousEndTime: MutableState<String>) {
        val startTimeString = startTime.value
        val endTimeString = endTime.value

        if (startTimeString.isNotBlank() && endTimeString.isNotBlank()) {
            val startTimeCalendar = parseTime(startTimeString)
            val endTimeCalendar = parseTime(endTimeString)

            if (startTimeCalendar != null && endTimeCalendar != null) {
                if (!startTimeCalendar.before(endTimeCalendar))
                {
                    Constant.showToast(context, "Start time should be lesser than end time")

                    // Reset the text values to their previous values
                    startTime.value = previousStartTime.value
                    endTime.value = previousEndTime.value
                }
                else
                {
                    val totalMinutes = leaveBalanceHours(selectedLeaveType, startTime = startTime.value, endTime = endTime.value)
                    val hoursFloat = totalMinutes / 60.0f
                    val hours = totalMinutes / 60
                    val remainingMinutes = totalMinutes % 60
                    appliedValue = String.format("%.2f", hoursFloat)
                    appliedHours = "$hours : $remainingMinutes"
                    remainingValue = String.format("%.2f",availableBalance.toFloat() - hoursFloat)
                    previousStartTime.value = startTime.value
                    previousEndTime.value = endTime.value
                }
            }
        }
    }

    // Declaring a Boolean value to store bottom sheet collapsed state
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val leaveId = remember { mutableIntStateOf(0) }
    val leaveName = remember { mutableStateOf("--Select Leave Type--") }
    var selectedLeaveName by remember { mutableStateOf(leaveName.value) }


    var reasonForLeave by remember { mutableStateOf("") }
    var selectedOption1 by remember { mutableStateOf<String?>(null) }


    //For Calendar

    val calendarStateFrom = rememberSheetState()
    val calendarStateTo = rememberSheetState()

    val currentDate = remember { LocalDate.now() }
    var fromDate by remember { mutableStateOf(currentDate) }
    var toDate by remember { mutableStateOf(currentDate) }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Convert LocalDate to Date
    val fromDateAsDate = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    val toDateAsDate = Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

    // Initialize the selected dates list with the current date when entering the page
    val initialSelectedDates = remember { mutableListOf<LocalDate>(currentDate) }
    val selectedDates = remember { mutableStateListOf(*initialSelectedDates.toTypedArray()) }

    // Define a list of Boolean values to track the expanded state of each dropdown
    var expandedStates by remember { mutableStateOf(List(selectedDates.size) { false }) }



    //For Time Picker

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("00:00") }
    val endTime = remember { mutableStateOf("00:00") }
    val previousStartTime = remember { mutableStateOf(startTime.value) }
    val previousEndTime = remember { mutableStateOf(endTime.value) }
//    val startTime = remember { mutableStateOf("09:00 am") }
//    val endTime = remember { mutableStateOf("10:00 am") }




    // Declaring and initializing a calendar
    val startTimeCalendar = Calendar.getInstance()
    val startTimeHour = startTimeCalendar[Calendar.HOUR_OF_DAY]
    val startTimeMinute = startTimeCalendar[Calendar.MINUTE]

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, startTimeHour : Int, startTimeMinute: Int ->
            val formattedTime = formatTimeIn12HourFormat(startTimeHour, startTimeMinute)
            startTime.value = formattedTime
            validateTime(startTime, endTime,previousStartTime,previousEndTime)
        }, startTimeHour, startTimeMinute, false
    )


    // Declaring and initializing a calendar
    val endTimeCalendar = Calendar.getInstance()
    val endTimeHour = endTimeCalendar[Calendar.HOUR_OF_DAY]
    val endTimeMinute = endTimeCalendar[Calendar.MINUTE]


    // Creating a TimePicker dialog
    val endTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, endTimeHour : Int, endTimeMinute: Int ->
            val formattedTime = formatTimeIn12HourFormat(endTimeHour, endTimeMinute)
            endTime.value = formattedTime
            validateTime(startTime, endTime,previousStartTime,previousEndTime)
        }, endTimeHour, endTimeMinute, false

    )




    // Function to update the list of selected dates between "From" and "To" dates
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDates(
        fromDate: LocalDate,
        toDate: LocalDate,
        selectedDates: MutableList<LocalDate>,
    ) {
        selectedDates.clear()
        var currentDate = fromDate

        while (currentDate <= toDate) {
            selectedDates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }


        // Display selected dates in log
        if(selectedLeaveType == "D")
        {
            for (date in selectedDates)
            {
                Log.d("SelectedDate...............", date.toString())
            }
        }
        else
        {
            Log.d("SelectedDate...............", selectedDates.toString())
        }
    }



    // Calendar Dialog for "From" Date
    Constant.AppTheme{
        CalendarDialog(
            state = calendarStateFrom,
            selection = CalendarSelection.Date { date ->

                if (leaveTypeSelected) {
                    if (selectedLeaveType == "D" && date > toDate)
                    {
                        Constant.showToast(context, "From date cannot be greater than To date")
                    }
                    else
                    {
                        fromDate = date

                        startTime.value = "00:00"
                        endTime.value = "00:00"
                        appliedValue = "00"
                        appliedHours = "00 : 00"

                        remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                        updateSelectedDates(fromDate, toDate, selectedDates)

                        // Display selected dates in log
                        for (date in selectedDates) {
                            Log.d("SelectedDate1......", date.toString())
                        }
                    }
                }
                else
                {
                    Constant.showToast(context, "Please select a leave type first")
                }
            }
        )
    }


    // Calendar Dialog for "To" Date
    Constant.AppTheme{
        CalendarDialog(
            state = calendarStateTo,
            selection = CalendarSelection.Date { date ->
                if (leaveTypeSelected) {
                    if (selectedLeaveType == "D" && date < fromDate) {

                        Constant.showToast(context, "To date cannot be less than From date")

                    } else {
                        toDate = date

                        startTime.value = "00:00"
                        endTime.value = "00:00"
                        appliedValue = "00"
                        appliedHours = "00 : 00"

                        remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())



                        updateSelectedDates(fromDate, toDate, selectedDates)

                        for (date in selectedDates) {
                            Log.d("SelectedDate2......", date.toString())
                        }
                    }
                }
                else
                {
                    Constant.showToast(context, "Please select a leave type first")
                }
            }
        )
    }



    fun formatBalance(balance: Double): String {
        val df = DecimalFormat("#,##0.##")
        df.maximumFractionDigits = 2
        return df.format(balance)
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
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
                            text = "Available Leave type",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.themeColor),
                            modifier = Modifier.padding(10.dp)
                        )
                        HorizontalDivider(color = colorResource(id = R.color.divider))
                    }


                    when (leaveListFlag) {
                        2 -> {
                            LazyColumn {
                                items(leaveTypeList) { data ->
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp, end = 10.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                                    } else {
                                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                                    }
                                                }

                                                leaveName.value = data.name
                                                leaveId.intValue = data.id
                                                leaveDuration = data.durationAllow
                                                selectedLeaveType = data.leaveType
                                                availableBalance = data.balance.toString()
                                                leaveTypeSelected =
                                                    true // Set leaveTypeSelected to true

                                                Log.d(
                                                    "Leave Duration...",
                                                    "Leave Name: $leaveName ,Leave Balance: $availableBalance , Duration : $leaveDuration ,Type: $selectedLeaveType"
                                                )

                                            },
                                    ) {
                                        Row(modifier = Modifier.padding(15.dp)) {
                                            Column {

                                                if (data.name.length > 25) {
                                                    PlainTooltipBox(
                                                        tooltip = {
                                                            Text(
                                                                text = data.name,
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                ),
                                                            )
                                                        }
                                                    ) {
                                                        Text(
                                                            text = data.name.take(25) + "..." ,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            ),
                                                            modifier = Modifier.tooltipTrigger()
                                                        )
                                                    }
                                                } else {
                                                    Text(
                                                        text = data.name,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                    )
                                                }
                                            }



                                            Column(
                                                modifier = Modifier.fillMaxWidth(1f),
                                                horizontalAlignment = Alignment.End
                                            ) {

                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = data.balance.toString(),
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                )
                                                {
                                                    Text(
                                                        text = formatBalance(data.balance),
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                        modifier = Modifier.tooltipTrigger()
                                                    )
                                                }


                                            }
                                        }
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }
                                }
                            }
                        }
                        1 -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "No data to show",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.paraColor)
                                    )
                                )
                            }
                        }
                        0 -> {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .padding(bottom = 8.dp)
                            )
                        }
                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                Text(
                                    text = "Something went wrong...",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.paraColor)
                                    )
                                )
                            }
                            Constant.showToast(context, "Please try again...")
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
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.backgroundColor))
                .padding(start = 10.dp, end = 10.dp)
                .verticalScroll(state = rememberScrollState()), // Add verticalScroll modifier
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // Creating a button that changes bottomSheetScaffoldState value upon click
            // when bottomSheetScaffoldState is collapsed, it expands and vice-versa
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .clickable {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    },
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                shape = RoundedCornerShape(5.dp),

                ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row {

                        if (leaveName.value.length > 21) {
                            PlainTooltipBox(
                                tooltip = {
                                    Text(
                                        text = leaveName.value,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }
                            ) {
                                Text(
                                    text = leaveName.value.take(21) + if (leaveName.value.length > 21) "..." else "",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    ),
                                    modifier = Modifier.tooltipTrigger()
                                )
                            }
                        } else {
                            Text(
                                text = leaveName.value,
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                ),
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth(1f)) {
                            Icon(
                                painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                contentDescription = "",
                                modifier = Modifier.align(Alignment.End)
                            )
                        }

                    }
                }
            }

            Button(
                onClick = { },
                modifier = Modifier.padding(top = 18.dp, bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade))
            ) {
                Column(modifier = Modifier.padding(top = 7.dp, bottom = 7.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Icon(
                                painterResource(id = R.drawable.attendance),
                                contentDescription = "calender",
                                tint = colorResource(id = R.color.themeColor),
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .width(20.dp)
                                    .height(20.dp)
                                    .size(22.dp)
                            )
                        }
                        Column(modifier = Modifier.clickable { calendarStateFrom.show() }) {
                            Text(
                                text = dateFormat.format(fromDateAsDate),
                                style = TextStyle(fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black)
                            ),
                            )
                        }
                        Column {
                            Icon(
                                painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                contentDescription = "arrow",
                                tint = colorResource(
                                    id = R.color.themeColor
                                ),
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(18.dp)
                                    .size(20.dp)
                            )
                        }
                        Column(modifier = Modifier.clickable { calendarStateTo.show() }) {
                            Text(
                                text = dateFormat.format(toDateAsDate),
                                style = TextStyle(fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                ),
                            )
                        }
                    }
                }
            }

            if (selectedLeaveName != leaveName.value)
            {
                selectedLeaveName = leaveName.value
                if(selectedLeaveType == "D")
                {
                    leaveItems.clear()
                    leaveItems.add(LeaveItem(fromDate))
                    selectedIndex = -1
                    flag = 0
                    appliedValue = "00"
                    remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                }
                if(selectedLeaveType == "H")
                {

                    startTime.value = "00:00"
                    endTime.value = "00:00"
                    appliedValue = "00"
                    appliedHours = "00 : 00"

                    remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                }
                reasonForLeave = ""
            }

            Column()
            {
                when (selectedLeaveType) {
                    "D" -> {
                        if(flag == 0 || leaveItems.isEmpty()) {
                            Log.d("Inside Lazy column....","selectedLeaveType : $selectedLeaveType")
                            leaveItems.clear()

                            for (date in selectedDates) {
                                leaveItems.add(LeaveItem(date))
                            }
                            flag = 1
                        }


                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .background(color = colorResource(id = R.color.backgroundColor))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = colorResource(id = R.color.backgroundColor))
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {

                                HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                leaveDaysHeader()

                                HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                if (leaveItems.isEmpty()) {
                                    val currentDate = LocalDate.now()
                                    leaveItems.add(LeaveItem(currentDate))
                                }

                                LazyColumn {

                                    item{
                                        Spacer(modifier = Modifier.height(5.dp))
                                    }

                                    for (date in selectedDates) {
                                        Log.d("InsideLazy Column......", "SelectedDate2 : $date")
                                    }

                                    // Ensure that expandedStates has the same size as selectedDates
                                    if (expandedStates.size != selectedDates.size) {
                                        expandedStates = MutableList(selectedDates.size) { false }
                                    }

                                    items(leaveItems) { leaveItem  ->
                                        val index = leaveItems.indexOf(leaveItem) // Capture the index here
                                        val date = leaveItem.date
                                        val isExpanded = expandedStates[selectedDates.indexOf(date)]
                                        Log.d("LeaveItem", "Date: ${leaveItem.date}, selectedIndex: ${leaveItem.selectedIndex}")

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {

                                            // First column (Date)
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(start = 10.dp, top = 5.dp)
                                            ) {
                                                Text(
                                                    text = dateFormat.format(
                                                        Date.from(
                                                            date.atStartOfDay(ZoneId.systemDefault())
                                                                .toInstant()
                                                        )
                                                    ),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.black),
                                                    modifier = Modifier.padding(top = 10.dp)
                                                )
                                            }

                                            // Second column (Conditionally change design)
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(top = 5.dp, start = 10.dp),
                                            ) {
                                                Card(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = colorResource(id = R.color.white)
                                                    ),
                                                    shape = RoundedCornerShape(5.dp),
                                                    modifier = Modifier.clickable {
                                                        // Toggle the expanded state for this dropdown
                                                        expandedStates = expandedStates.toMutableList().apply {
                                                            set(index, !isExpanded)
                                                            // Close all other dropdowns when opening this one
                                                            for (i in indices) {
                                                                if (i != index) {
                                                                    set(i, false)
                                                                }
                                                            }
                                                        }
                                                    }
                                                ) {
                                                    Box(modifier = Modifier.padding(10.dp))
                                                    {
                                                        selectedIndex = leaveItem.selectedIndex
                                                        selectedOption = leaveDurationList.getOrNull(selectedIndex!!)
                                                        Row {
                                                            Column {
                                                                Text(
                                                                    text = selectedOption ?: "Select Type",
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 14.sp,
                                                                        fontWeight = FontWeight(500),
                                                                        color = colorResource(id = R.color.black)
                                                                    )
                                                                )
                                                            }
                                                            Column(
                                                                modifier = Modifier.fillMaxWidth(1f),
                                                                horizontalAlignment = Alignment.End,
                                                            ) {
                                                                Icon(
                                                                    painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                                                    contentDescription = "arrow",
                                                                    tint = colorResource(id = R.color.black),
                                                                )
                                                            }
                                                        }

                                                        if (isExpanded) {
                                                            DropdownMenu(
//                                                                expanded = isExpanded, --> shows always true
                                                                expanded = true,
                                                                onDismissRequest = {
                                                                    // Close the dropdown when dismissed
                                                                    expandedStates = expandedStates.toMutableList().apply {
                                                                        set(index, false)
                                                                    } },
                                                                modifier = Modifier.wrapContentSize(
                                                                    Alignment.Center
                                                                ),
                                                            ) {

                                                                val jsonArray = JSONArray(leaveDuration)

                                                                Log.d(
                                                                    "LeaveType... LeaveDurationTypeListDetails",
                                                                    "JsonArray:$jsonArray"
                                                                )

                                                                val leaveTypeDurationList =
                                                                    Gson().fromJson(
                                                                        jsonArray.toString(),
                                                                        Array<LeaveDurationResponseModel>::class.java
                                                                    ).toList()

                                                                leaveDurationList.clear()

                                                                for (leaveDurationType in leaveTypeDurationList) {
                                                                    leaveDurationList.add(leaveDurationType.des)
                                                                }

                                                                leaveDurationList.forEachIndexed { optionIndex, option ->
                                                                    DropdownMenuItem(
                                                                        onClick = {

                                                                            leaveItem.selectedIndex = optionIndex // Update the selected index for this item
                                                                            selectedIndex = optionIndex // Update the selected index for the DropdownMenuItem
                                                                            selectedOption1 = option
                                                                            leaveItem.leaveType = option
                                                                            leaveItems[index] = leaveItem
                                                                            val applied = leaveBalanceDays(selectedLeaveType, selectedDates = selectedDates, leaveItems = leaveItems)
                                                                            appliedValue = String.format("%.2f", applied )
                                                                            remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                                                                            expandedStates = expandedStates.toMutableList().apply {
                                                                                set(index, false)
                                                                            }
                                                                        },
                                                                        modifier = Modifier
                                                                            .width(IntrinsicSize.Max)
                                                                            .height(IntrinsicSize.Min)
                                                                    ) {
                                                                        Text(
                                                                            text = option ,
                                                                            style = MaterialTheme.typography.titleMedium,
                                                                            color = colorResource(id = R.color.black),
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HorizontalDivider(color = colorResource(id = R.color.divider))
                                    }//Items
                                }//LazyColumns
                            }
                        }
                    }
                    "H" -> {
                        leaveItems.clear() // Clear any previously added leave items
                        toDate = fromDate // Automatically update "To Date" when Hour leave is selected
                        updateSelectedDates( fromDate, toDate, selectedDates) // Update selected dates accordingly

                        Box(
                            modifier = Modifier
                                .height(130.dp)
                                .background(color = colorResource(id = R.color.backgroundColor))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = colorResource(id = R.color.backgroundColor))
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {

                                HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                leaveHoursHeader()

                                HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
                                ) {


                                    //Start Time
                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(5.dp)
                                    ) {

                                        Button(
                                            onClick = {  startTimePickerDialog.show() },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                        ) {
                                            Row(
                                                Modifier
                                                    .padding(top = 4.dp, bottom = 4.dp)
                                                    .fillMaxWidth())
                                            {

                                                Column(modifier = Modifier.weight(1.5f)){
                                                    BasicTextField(
                                                        readOnly = true,
                                                        value = startTime.value,
                                                        onValueChange = { /* Handle value change if needed */ },
                                                        textStyle = TextStyle(fontFamily = poppins_font,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.white)
                                                        ), singleLine = true,
                                                        modifier = Modifier
                                                            .padding(start = 5.dp)
                                                            .clickable { startTimePickerDialog.show() }
                                                    )
                                                }





                                                Card(modifier = Modifier.padding(start = 5.dp,end=5.dp),
                                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                                                {
                                                    Icon(
                                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                                        contentDescription = "clock",
                                                        tint = colorResource(id = R.color.themeColor),
                                                        modifier = Modifier
                                                            .height(20.dp)
                                                            .width(20.dp)
                                                            .size(25.dp)
                                                            .clickable { startTimePickerDialog.show() }
                                                    )
                                                }

                                            }

                                        }
                                    }






                                    //End Time
                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(5.dp)
                                    ) {

                                        Button(
                                            onClick = {  endTimePickerDialog.show()  },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                        ) {
                                            Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                            {


                                                Column(modifier = Modifier.weight(1.5f)){
                                                    BasicTextField(
                                                        readOnly = true,
                                                        value = endTime.value,
                                                        onValueChange = {  },
                                                        textStyle = TextStyle(fontFamily = poppins_font,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.white)
                                                        ), singleLine = true,
                                                        modifier = Modifier
                                                            .padding(start = 5.dp)
                                                            .clickable { endTimePickerDialog.show() }
                                                    )
                                                }


                                                Card(modifier = Modifier.padding(start = 5.dp,end=5.dp),
                                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                                                {
                                                    Icon(
                                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                                        contentDescription = "clock",
                                                        tint = colorResource(id = R.color.themeColor),
                                                        modifier = Modifier
                                                            .height(20.dp)
                                                            .width(20.dp)
                                                            .size(25.dp)
                                                            .clickable { endTimePickerDialog.show() }
                                                    )
                                                }

                                            }

                                        }
                                    }




                                    //Applied Value
                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(5.dp)
                                    ) {

                                        Button(
                                            onClick = {  },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.divider)),
                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                        ) {

                                            Icon(
                                                painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                                contentDescription = "clock",
                                                tint = colorResource(id = R.color.divider),
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .width(20.dp)
                                                    .size(25.dp)
                                            )

                                            Column(modifier = Modifier.fillMaxWidth())
                                            {
                                                BasicTextField(
                                                    readOnly = true,
                                                    value = appliedHours,
                                                    onValueChange = { /* Handle value change if needed */ },
                                                    textStyle = TextStyle(fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.themeColor)
                                                    ), singleLine = true,
                                                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text horizontally
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                    else -> {
                        leaveItems.clear()
                        Box(modifier = Modifier
                            .height(10.dp)
                            .background(color = colorResource(id = R.color.backgroundColor))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = colorResource(id = R.color.backgroundColor))
                                    .padding(top = 5.dp, bottom = 5.dp)
                            )
                            {
                                Box(modifier = Modifier.padding(10.dp)) {
                                    Row {
                                        Text(
                                            text = "",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }

            //1st set

            Column {

                Text(
                    text = "Balance", //Leave balance
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.black)
                    ),
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    Row {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Available",style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.green)
                                ),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = formatBalance(availableBalance.toDouble()),
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.green)
                                ),
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Applied", style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.red)
                                ),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = formatBalance(appliedValue.toDouble()),
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.red)
                                ),
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Remaining", style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            Text(
                                text = formatBalance(remainingValue.toDouble()),
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),
                            )
                        }
                    }
                }
            }

            val maxLength = 250
            Column {
                Text(
                    text = "Reason...",//Reason for Leave
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.black)
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                TextField(
                    value = reasonForLeave,
                    onValueChange = {
                        if (it.length <= maxLength)
                            reasonForLeave = it },

//                    { reasonForLeave = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                        .background(color = colorResource(id = R.color.white)),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.titleMedium,
                    placeholder = { Text(text = "Enter Reason...", style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                    ),) },
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.black),
                        focusedBorderColor = colorResource(id = R.color.themeColor),
                        unfocusedBorderColor = colorResource(R.color.white),
                    ),
                )
            }

/*
            if(showLoad.value)
            {
                circularProgression1(showLoad.value)
            }
*/

            if(statusLoading.value)
            {
                circularProgression1(statusLoading.value)

            }


            Column {
                Button(
                    onClick = {



                        val formattedStartTime = convertTo24HourFormat(startTime.value)
                        val formattedEndTime = convertTo24HourFormat(endTime.value)

                        if (selectedLeaveType == "D") {
                            val hasInvalidDuration = leaveItems.any { it.leaveType == "--Select Type--" }
                            if (hasInvalidDuration) {
                                Constant.showToast(context, "Please select the Duration....")
                            }
                            else if (reasonForLeave.isEmpty() || reasonForLeave.isBlank()) {
                                reasonForLeave = ""
                                Constant.showToast(context, "Please enter the reason....")
                            }
                            else
                            {
//                                showLoad.value = true
                                statusLoading.value = true
                                applyLeave(
                                    context,
                                    leaveViewModel,
                                    navController,
                                    empID,
                                    leaveId,
                                    leaveItems = leaveItems,
                                    fromDate,
                                    toDate,
                                    selectedLeaveType,
                                    reasonForLeave,
                                    formattedStartTime,
                                    formattedEndTime,
                                    appliedValue,
                                    availableBalance,
                                    remainingValue
                                )

//                                showLoad.value = false

                            }
                        }
                        else if(selectedLeaveType == "H")
                        {
                            if (startTime.value == "00:00") {
                                Constant.showToast(context, "Please select the Start Time....")
                            } else if (endTime.value == "00:00") {
                                Constant.showToast(context, "Please select the End Time....")
                            }
                            else if (reasonForLeave.isEmpty() || reasonForLeave.isBlank()) {
                                reasonForLeave = ""
                                Constant.showToast(context, "Please enter the reason....")
                            }
                            else {
//                                showLoad.value = true

                                statusLoading.value = true

                                applyLeave(
                                    context,
                                    leaveViewModel,
                                    navController,
                                    empID,
                                    leaveId,
                                    leaveItems = leaveItems,
                                    fromDate,
                                    toDate,
                                    selectedLeaveType,
                                    reasonForLeave,
                                    formattedStartTime,
                                    formattedEndTime,
                                    appliedValue,
                                    availableBalance,
                                    remainingValue
                                )

//                                showLoad.value = false


                            }

                        }
                        else
                        {
                            Constant.showToast(context, "Please select a Leave Type....")
                        }



                    }, modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "Apply", //Apply Leave
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.white)
                        ),
                    )
                }
            }
        }
    }
}



@SuppressLint("ComposableNaming")
@Composable
fun leaveHoursHeader()
{
    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, bottom = 10.dp, start = 25.dp),

            )
        {
            Text(text = "Start Time",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),)
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {
            Text(text = "End Time",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
            )
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {
            Text(text = "Applied Time",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
            )
        }
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun leaveDaysHeader()
{
    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, bottom = 10.dp, start = 25.dp),

            )
        {
            Text(text = "Date",
                style = TextStyle(fontFamily = poppins_font,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.themeColor)
            ),)
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {
            Text(text = "Time",  style = TextStyle(fontFamily = poppins_font,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.themeColor)
            ),)
        }
    }
}

fun leaveBalanceHours(
    selectedLeaveType: String,
    startTime: String,
    endTime: String,
): Int {

    val totalMinutes: Int
    if(selectedLeaveType == "H")
    {

        val startTime1 = startTime.trim().split(" ")
        val endTime1 = endTime.trim().split(" ")

        val startTimeParts = startTime1[0].trim().split(":")
        val endTimeParts = endTime1[0].trim().split(":")
        var startHour = startTimeParts[0].toInt()
        val startMinute = startTimeParts[1].toInt()
        var endHour = endTimeParts[0].toInt()
        val endMinute = endTimeParts[1].toInt()

        val startAMPM = startTime1[1].trim()
        val endAMPM = endTime1[1].trim()

// Adjust hours for PM
        if (startAMPM == "pm" && startHour != 12) {
            startHour += 12
        }
        if (endAMPM == "pm" && endHour != 12) {
            endHour += 12
        }

// Calculate the difference in minutes
        totalMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)

    }
    else
    {
        totalMinutes = 0
    }

    Log.d("LeaveType...", "LeaveApplied : $totalMinutes")
    return totalMinutes
}


fun leaveBalanceDays(
    selectedLeaveType: String,
    selectedDates: SnapshotStateList<LocalDate>,
    leaveItems: MutableList<LeaveItem>,
): Double
{
    var appliedValue = 0.0

    Log.d("Inside leaveBalanceDays", "selectedLeaveType : $selectedLeaveType")
    for(dates in selectedDates)
    {
        Log.d("Inside leaveBalanceDays", "selectedDates : $dates")

    }
    for(items in leaveItems)
    {
        Log.d("Inside leaveBalanceDays", "leaveType : ${items.date} : ${items.leaveType} : ${items.selectedIndex}")
    }

    if(selectedLeaveType == "D")
    {
        for (leaveItem in leaveItems)
        {
            when (leaveItem.leaveType) {
                "Full Day" -> {
                    appliedValue += 1.0
                }
                "1st Half", "2nd Half" -> {
                    appliedValue += 0.5
                }
                else -> {
                    appliedValue +=0
                }
            }
        }
    }
    Log.d("LeaveType...", "LeaveApplied : $appliedValue")
    return appliedValue
}



@SuppressLint("SuspiciousIndentation")
fun applyLeave(context: Context, leaveViewModel: LeaveViewModel, navController: NavController, empID: String, leaveId: MutableState<Int>, leaveItems: MutableList<LeaveItem>, fromDate: LocalDate, toDate: LocalDate, selectedLeaveType: String, reason: String, startTime: String, endTime: String, appliedDays: String, availableDays: String, balanceDays: String)
{

    // Submit the leave request or call API here if validation passes



    leaveViewModel.leaveFormValidate(navController, context, empID, leaveId, fromDate, toDate, appliedDays) { message ->
        Log.e("Apply Leave : message", "message : $message")
        val validate = message.isNullOrEmpty()

//        val validate = message == "0"
        Log.e("Apply Leave : Msg", "Msg : $message")
        Log.e("Apply Leave : Msg", "validate : $validate")

        if(validate)
        {
            Log.e("Apply Leave : Msg", "true : jsonObject1 : $message")
            leaveViewModel.postLeaveForm(context, navController,empID, leaveId, leaveItems = leaveItems, fromDate, toDate, startTime, endTime, selectedLeaveType, reason, appliedDays, availableDays, balanceDays)
        }
        else
        {
            Log.e("Apply Leave : Msg", "false : jsonObject1 : $message")
            Constant.showToast(context, message.toString())
            navController.navigate("LeaveApply")
        }
    }

}


fun convertTo24HourFormat(time12Hour: String): String {
    val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    try {
        val date = inputFormat.parse(time12Hour)
        return outputFormat.format(date!!)
    } catch (e: Exception) {
        // Handle parsing exceptions if needed
        e.printStackTrace()
    }

    return "" // Return an empty string in case of errors
}


fun formatLeaveData(fromDate: String, selectedLeaveType: String, startTime: String, endTime: String, appliedDays: String, leaveItems: List<LeaveItem>): Any {

    try
    {
        val dateDetailsArray = JSONArray()

        if (selectedLeaveType == "D")
        {
            for (leaveItem in leaveItems)
            {
                val interval = when (leaveItem.leaveType)
                {
                    "Full Day" -> 1.0
                    "1st Half", "2nd Half" -> 0.5
                    else -> 0.0
                }

                val dateDetails = JSONObject()
                dateDetails.put("date", leaveItem.date)
                dateDetails.put("interval", interval.toString())
                dateDetails.put("dayTypeId", (leaveItem.selectedIndex + 1).toString())
                dateDetails.put(
                    "dayType", when (leaveItem.leaveType) {
                        "Full Day" -> "Full Day"
                        "1st Half" -> "1st Half"
                        "2nd Half" -> "2nd Half"
                        else -> ""
                    }
                )
                dateDetailsArray.put(dateDetails)
            }
        } else if (selectedLeaveType == "H") {
            // Format data for "H" type
            val dateDetails = JSONObject()
            dateDetails.put("date", fromDate)
            dateDetails.put("interval", appliedDays.toDouble().toString()) // Convert appliedDays to Double
            dateDetails.put("start_time", startTime)
            dateDetails.put("end_time", endTime)
            dateDetailsArray.put(dateDetails)
        }

        Log.d("dateDetailsArray", "dateDetailsArray : $dateDetailsArray")


        return dateDetailsArray

    } catch (e: JSONException) {
        e.printStackTrace()
    }

    return ""
}

