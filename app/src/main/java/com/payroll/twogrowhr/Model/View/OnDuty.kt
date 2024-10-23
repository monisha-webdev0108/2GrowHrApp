package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import android.view.ContextThemeWrapper
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OnDutyListData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.OnDutyViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnDutyScreen(navController: NavController, onDutyViewModel: OnDutyViewModel)
{
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "On Duty Request",
            "HomeScreen"
        ) },
        bottomBarContent = { }, //if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    { onDuty(navController = navController, onDutyViewModel = onDutyViewModel) }
}

@SuppressLint("PrivateResource")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun onDuty(navController: NavController, onDutyViewModel: OnDutyViewModel)
{

    val context = LocalContext.current
    LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()


    val empID = userViewModel.getSFCode()
    val org  = userViewModel.getOrg()


    val index = 0 // Replace with the desired index


    onDutyViewModel.onDutyDataList1 = onDutyViewModel.onDutyDataList.value

    var flagSubmit by remember { mutableStateOf(0) }



    fun getDetailsOnDuty(date: String)
    {

            onDutyViewModel.getOnDutyDetails(navController, context, empID, date, org.toString()){onDutyListData ->


                onDutyViewModel.onDutyDataList1 = onDutyListData as List<OnDutyListData>

                if ((index >= 0) && (index < onDutyViewModel.onDutyDataList1.size))
                {
                    val onDutyItem = onDutyViewModel.onDutyDataList1[index]
                    onDutyViewModel.allowToApplyPastDays.value = onDutyItem.restrictPastDate
                    onDutyViewModel.allowPastDays.value = onDutyItem.restrictPastOnDutyDays + 1
                    onDutyViewModel.isCommentRequired.value = onDutyItem.commentReq
                    onDutyViewModel.isHalfDayNeeded.value = onDutyItem.hdWorkReq
                    onDutyViewModel.isRestrictByDays.value = onDutyItem.allowDaysOnDuty //is on duty restrict by days or not
                    onDutyViewModel.onDutyRestrictDays.value = onDutyItem.numberOfDaysOnDuty // on Duty restriction count
                    onDutyViewModel.onDutyRestrictSession.value = onDutyItem.durationOfOnDuty // on Duty restriction days by which session[month,year,week,quarterly,half]
                    onDutyViewModel.onDutyAppliedDays.value = onDutyItem.odMonCnt  // appliedDays < allowed days
                    onDutyViewModel.isRestrictEmpOnHW.value = onDutyItem.restrictEmpOnDuty
                    onDutyViewModel.restrictOnHW.value = onDutyItem.restrictDayOff
                    onDutyViewModel.isHoliday.value = onDutyItem.restrictEmpHolidayDate
                    onDutyViewModel.isWeeklyOff.value = onDutyItem.restrictEmpWeeklyOff
                    onDutyViewModel.isODAppliedEarlier.value = onDutyItem.odDateCnt
                    onDutyViewModel.isWFHAppliedEarlier.value = onDutyItem.wfhDatecnt
                    onDutyViewModel.isCheckOutOrNot.value = onDutyItem.odCheckOutOrNot
                    onDutyViewModel.dateOfJoining.value = onDutyItem.doj
                    onDutyViewModel.ruleId.value = onDutyItem.ruleId

                    onDutyViewModel.calendar.timeInMillis = System.currentTimeMillis()

                    if(onDutyViewModel.allowToApplyPastDays.value == 1)
                    {
                        onDutyViewModel.calendar.add(Calendar.DAY_OF_YEAR,
                            (-(onDutyViewModel.allowPastDays.value)).roundToInt()
                        )
                    }
                    else
                    {
                        onDutyViewModel.calendar.set(1900,0,0)

                    }



                    onDutyViewModel.startDate.value = onDutyViewModel.calendar.timeInMillis
                    val date1 = Date(onDutyViewModel.startDate.value)

                    Log.d("On Duty : ","allowPastDays : ${onDutyViewModel.allowPastDays.value}")
                    Log.d("On Duty : ","startDate : ${onDutyViewModel.startDate.value}")
                    Log.d("On Duty : ","dateFormat : $date1")//for comment required
                    Log.d("On Duty : ","isCommentRequired : ${onDutyViewModel.isCommentRequired.value}")//for comment required
                    Log.d("On Duty : ","isHalfDayNeeded : ${onDutyViewModel.isHalfDayNeeded.value}")//for Half Day required or not
                }


            }

//        }

    }

    Log.d("On Duty : ","isCommentRequired : ${onDutyViewModel.isCommentRequired.value}")


    //For Time Picker

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("00:00") }
    val endTime = remember { mutableStateOf("00:00") }
//    val previousStartTime = remember { mutableStateOf(startTime.value) }
//    val previousEndTime = remember { mutableStateOf(endTime.value) }

    // Declaring and initializing a calendar
    val startTimeCalendar = Calendar.getInstance()
    val startTimeHour = startTimeCalendar[Calendar.HOUR_OF_DAY]
    val startTimeMinute = startTimeCalendar[Calendar.MINUTE]

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, startTimeHourIn : Int, startTimeMinuteIn: Int ->
            val formattedTime = formatTimeIn12HourFormat(startTimeHourIn, startTimeMinuteIn)
            startTime.value = formattedTime
        }, startTimeHour, startTimeMinute, true
    )


    // Declaring and initializing a calendar
    val endTimeCalendar = Calendar.getInstance()
    val endTimeHour = endTimeCalendar[Calendar.HOUR_OF_DAY]
    val endTimeMinute = endTimeCalendar[Calendar.MINUTE]


    // Creating a TimePicker dialog
    val endTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, endTimeHourIn : Int, endTimeMinuteIn: Int ->
            val formattedTime = formatTimeIn12HourFormat(endTimeHourIn, endTimeMinuteIn)
            endTime.value = formattedTime
        }, endTimeHour, endTimeMinute, true

    )

    var reasonForOnDuty by remember { mutableStateOf("") }

    Log.d("On Duty : ","startDate : $onDutyViewModel.calendar")

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var selectedDate2 by remember {
        mutableStateOf(onDutyViewModel.calendar.timeInMillis) // or use mutableStateOf(calendar.timeInMillis)
    }



    //for Check-In time / Check-Out time required

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format the selectedDate2 to "yyyy-MM-dd"
    val selectedDateFormatted = dateFormat.format(Date(selectedDate2))
    val currentDateFormatted = dateFormat.format(Date(System.currentTimeMillis()))
    onDutyViewModel.isSelectedPastDay.value  = selectedDate2 < System.currentTimeMillis() && selectedDateFormatted != currentDateFormatted
    Log.d("On Duty : ","isSelectedPastDay : ${onDutyViewModel.isSelectedPastDay.value}")


    val formatter1 = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val selectedDateShow = formatter1.format(Date(selectedDate2))


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = onDutyViewModel.startDate.value,

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= onDutyViewModel.startDate.value
            }

            // users cannot select the years from 2024
            override fun isSelectableYear(year: Int): Boolean {

                val startDate2Date = Date(onDutyViewModel.startDate.value)
                val calendar1 = Calendar.getInstance()
                calendar1.time = startDate2Date
                val year1 = calendar1.get(Calendar.YEAR)

                return year >= year1
            }
        }
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedSessionType by remember { mutableStateOf("--Select--") }

    if (showDatePicker) {

        DatePickerDialog(
            onDismissRequest = {
//                showDatePicker = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    selectedDate2 = datePickerState.selectedDateMillis!!
                    onDutyViewModel.allowPastDays.value = 0.0
                    getDetailsOnDuty(Date(selectedDate2).toString())
                    reasonForOnDuty = ""
                    startTime.value="00:00"
                    endTime.value="00:00"
                    selectedSessionType ="--Select--"
                }) {
                    Text(text = "Confirm",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)
                        ))
                }
            },
/*            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(text = "Cancel",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)
                        ))
                }
            }*/
        ) {
            Constant.AppTheme {
                DatePicker(state = datePickerState,
                    showModeToggle = false // Edit button for date entry
                )
            }
        }
    }



    val onDutyDataList1 = onDutyViewModel.onDutyDataList.value

    flagSubmit = if(onDutyDataList1.isEmpty()) 0 else 1






    if(statusLoading.value)
    {
        circularProgression1(statusLoading.value)
    }




    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
        .verticalScroll(rememberScrollState())
    ){
        Button(
            onClick = {
                showDatePicker = true

            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade))
        ) {
            Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {

                        Icon(
                            painterResource(id = R.drawable.attendance)  , contentDescription ="calendar",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier.width(20.dp).height(20.dp)
                        )
                    }
                    Column(modifier = Modifier.clickable {

                        showDatePicker = true

                    }) {

                        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                        Text(
                            text = formatter.format(Date(selectedDate2)),
                            style = TextStyle(fontFamily = poppins_font,
                                fontSize = 14.sp, fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black)
                            )
                        )
                    }
                    Column {
/*                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_forward_ios_24)  ,
                            contentDescription ="front",
                            tint = colorResource(id = R.color.themeColor)
                        )*/
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(top = 10.dp)) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.green))
                    .offset(x = 3.dp)
                    .fillMaxWidth(1f)// Offset the content to the right to make room for the border
            ){
                Box(modifier = Modifier
                    .background(color = colorResource(id = R.color.light_green))
                    .padding(10.dp)
                    .fillMaxWidth(1f)

                ) {
                    Text(text = "Requested date $selectedDateShow",
                        style = MaterialTheme.typography.titleSmall,
                        color = colorResource(id = R.color.green)
                    )
                }
            }

        }
        Column(modifier = Modifier.padding(top = 10.dp)) {
            HorizontalDivider(color = colorResource(id = R.color.lightthemecolor))

            Row()
            {
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
                    Text(text = "Session",
                        style = TextStyle(fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)
                        ),
                    )
                }

            }
            HorizontalDivider(color = colorResource(id = R.color.lightthemecolor))
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(1f)
                ) {
                    Text(
                        text = selectedDateShow,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp) // Fill the width of the column
                    )
                }


                val sessionTypeList: List<String> = if (onDutyViewModel.isHalfDayNeeded.value == 1) {
                    listOf(
                        "Full day",
                        "1st Half",
                        "2nd Half"
                    )
                } else {
                    listOf("Full day")
                }


                val dropdownMenuColors = TextFieldDefaults.colors(
                    cursorColor = Color.White, // Change to your desired color
                    focusedIndicatorColor = Color.Transparent, // Change to your desired color
                    unfocusedIndicatorColor = Color.Transparent, // Change to your desired color
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )

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
                    ) {
                        Box(modifier = Modifier.background(Color.White) )
                        {
                            Row {

                                // menu box
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {
                                        expanded = !expanded
                                    },
                                    modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
                                ) {
                                    TextField(
                                        modifier = Modifier
                                            .menuAnchor()
                                            .background(Color.White), // menuAnchor modifier must be passed to the text field for correctness.
                                        readOnly = true,
                                        value = selectedSessionType,
                                        singleLine = true,
                                        onValueChange = {},
                                        //label = { Text("Movies") },
                                        trailingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                                contentDescription = "Down arrow",
                                                modifier = Modifier.clickable {
//                                                    expanded = expanded
                                                }
                                            )
                                        },
                                        colors = dropdownMenuColors
                                    )

                                    // menu
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = {
                                            expanded = false
                                        },
                                        modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
                                    ) {
                                        sessionTypeList.forEach { selectionOption ->
                                            DropdownMenuItem(
                                                text = { Text(text = selectionOption,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    )) },

                                                onClick = {
                                                    selectedSessionType = selectionOption
                                                    expanded = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
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


            //Check in and Check out time

            if(onDutyViewModel.isSelectedPastDay.value)
            {

                //Check in and Check out time tile

                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, bottom = 10.dp, start = 25.dp),
                    )
                    {
                        Text(text = "Check-In Time",
                            style = TextStyle(fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)
                            ),)
                    }
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {
                        Text(text = "Check-Out Time",
                            style = TextStyle(fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)
                            ),
                        )
                    }
                }



                //Check in and Check out time
                Row{

                    //Start Time
                    Column(modifier = Modifier.weight(1f).padding(top = 10.dp, bottom = 10.dp, start = 10.dp))
                    {

                        Row {

                            // For selected Start Time

                            Column(modifier = Modifier.weight(1.5f))
                            {
                                Button(
                                    onClick = {  },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = com.google.android.material.R.color.m3_ref_palette_white)),
                                    contentPadding = PaddingValues(start = 3.dp, top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                ){
                                    BasicTextField(
                                        readOnly = true,
                                        value = startTime.value,
                                        onValueChange = {  },
                                        textStyle = TextStyle(fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)
                                        ), singleLine = true,
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                    )
                                }
                            }

                            // For Icon Start Time

                            Button(
                                onClick = {  startTimePickerDialog.show()  },
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                            ){
                                Icon(
                                    painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                    contentDescription = "clock",
                                    tint = colorResource(id = R.color.white),
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                        .size(20.dp)
                                        .clickable { startTimePickerDialog.show() }
                                )
                            }

                        }


                    }

                    //End Time
                    Column(modifier = Modifier.weight(1f).padding(top = 10.dp, bottom = 10.dp, start = 10.dp))
                    {
                        Row {

                            // For selected End Time

                            Column(modifier = Modifier
                                .weight(1.5f)
                            ) {
                                Button(
                                    onClick = {  },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                    contentPadding = PaddingValues(start = 3.dp, top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                ){
                                    BasicTextField(
                                        readOnly = true,
                                        value = endTime.value,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)
                                        ), singleLine = true,
                                        modifier = Modifier
                                            .padding(start = 5.dp)
                                    )
                                }
                            }

                            // For Icon End Time

                            Button(
                                onClick = {  endTimePickerDialog.show()  },
                                shape = RoundedCornerShape(10),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                            ){
                                Icon(
                                    painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                    contentDescription = "clock",
                                    tint = colorResource(id = R.color.white),
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(20.dp)
                                        .size(20.dp)
                                        .clickable { endTimePickerDialog.show() }
                                )
                            }

                        }

                        /*
                                                Button(
                                                    onClick = {  endTimePickerDialog.show()  },
                                                    shape = RoundedCornerShape(10),
                                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                                    contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                                ) {
                                                    Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                                    {

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

                        }

                        */


                    }

                }








                /*

                                //Check in and Check out time
                                Row{

                                    //Start Time
                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
                                    ) {

                                        Button(
                                            onClick = {  startTimePickerDialog.show() },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                        ) {
                                            Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                            {

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

                                                BasicTextField(
                                                    readOnly = true,
                                                    value = startTime.value,
                                                    onValueChange = { },
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

                                        }
                                    }

                                    //End Time
                                    Column(modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
                                    ) {

                                        Button(
                                            onClick = {  endTimePickerDialog.show()  },
                                            shape = RoundedCornerShape(10),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                        ) {
                                            Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                            {

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

                                        }
                                    }

                                }

                */

                HorizontalDivider(color = colorResource(id = R.color.divider))
            }


            val maxLength = 250

            //Reason

            Column {
                Text(
                    text = "Reason", style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.black),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                TextField(
                    value = reasonForOnDuty,
                    onValueChange = { if (it.length <= maxLength) reasonForOnDuty = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp)
                        .background(color = colorResource(id = R.color.white)),
                    maxLines = 5,
                    textStyle = MaterialTheme.typography.titleMedium,
                    placeholder = { Text(text = "Enter Reason...") },
                    singleLine = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.black),
                        focusedBorderColor = colorResource(id = R.color.themeColor),
                        unfocusedBorderColor = colorResource(R.color.white),
                    )
                )
            }


            Column {


                Button(
                    onClick = {

                        if(flagSubmit == 1)
                        {

                            val sessionName = if(onDutyViewModel.onDutyRestrictSession.value == 1) "Weekly" else if(onDutyViewModel.onDutyRestrictSession.value == 2) "Monthly" else if(onDutyViewModel.onDutyRestrictSession.value == 3) "Quarterly" else if(onDutyViewModel.onDutyRestrictSession.value == 4) "Half yearly" else if(onDutyViewModel.onDutyRestrictSession.value == 5) "Yearly" else ""
                            val localDateTime = LocalDateTime.parse(onDutyViewModel.dateOfJoining.value, DateTimeFormatter.ISO_DATE_TIME)
                            val doj1 = localDateTime.toLocalDate()
                            val doj = LocalDate.parse(doj1.toString())
                            val selectedDay = LocalDate.parse(selectedDateFormatted)


                            Log.d("localDateTime", localDateTime.toString())
                            Log.d("DOJ1", "$doj1")
                            Log.d("DOJ : SelectedDate", "$doj : $selectedDay")



                            val holidayDate = LocalDateTime.parse(onDutyViewModel.isHoliday.value, DateTimeFormatter.ISO_DATE_TIME )

                            Log.d("localDateTime", localDateTime.toString())

                            val holiday1 = holidayDate.toLocalDate()

                            Log.d("On Duty", "Holiday : $holiday1")

                            val holiday = LocalDate.parse(holiday1.toString())

                            val holidayFlag = onDutyViewModel.restrictOnHW.value == 1 || onDutyViewModel.restrictOnHW.value == 2   //Holiday&WeekOff = 1, Holiday = 2
                            val weeklyOffFlag = onDutyViewModel.restrictOnHW.value == 1 || onDutyViewModel.restrictOnHW.value == 3 //Holiday&WeekOff = 1, WeeklyOff = 3

                            Log.d("On Duty", "holidayFlag : $holidayFlag")

                            Log.d("On Duty", "weeklyOffFlag : $weeklyOffFlag")

                            Log.d("On Duty", "weeklyOffFlag : $weeklyOffFlag")



                            // Check if selectedSessionType is empty before setting it
                            if (selectedSessionType == "--Select--")
                            {
                                Constant.showToast(context, "Please select a session type")
                                Log.d("onDuty","session type is not filled")
                            }
                            else if(onDutyViewModel.isSelectedPastDay.value && startTime.value == "00:00")
                            {
                                Constant.showToast(context, "Please select Check-in time")
                                Log.d("onDuty","Check-in is not filled")
                            }
                            else if(onDutyViewModel.isSelectedPastDay.value && endTime.value == "00:00")
                            {
                                Log.d("onDuty","Check-out is not filled")
                                Constant.showToast(context, "Please select Check-out time")
                            }
                            else if(onDutyViewModel.isCommentRequired.value == 1 && reasonForOnDuty.isEmpty())
                            {
                                Log.d("onDuty","comment is not filled")
                                Constant.showToast(context, "Please enter the reason")
                            }
                            else if(onDutyViewModel.isCommentRequired.value == 1 && reasonForOnDuty.isBlank())
                            {
                                reasonForOnDuty = ""
                                Log.d("onDuty","comment is not filled")
                                Constant.showToast(context, "Please enter the reason")
                            }
                            else if(onDutyViewModel.isCheckOutOrNot.value == 1)
                            {
                                Log.d("onDuty","On Duty is allow to apply before Check-In only")
                                Constant.showToast(context, "On Duty is allow to apply before Check-In only")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(selectedDay.isBefore(doj) || selectedDay.isEqual(doj))
                            {
                                Log.d("onDuty","On Duty applied on or before DOJ")
                                Constant.showToast(context, "On-duty date should be greater than DOJ")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(onDutyViewModel.isWFHAppliedEarlier.value == 1)
                            {
                                Log.d("onDuty","Already WFH applied for that day")
                                Constant.showToast(context, "Work From Home has already been requested for selected day $selectedDateShow")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(onDutyViewModel.isODAppliedEarlier.value == 1)
                            {
                                Log.d("onDuty","Already applied for that day")
                                Constant.showToast(context, "On-duty has already been requested for $selectedDateShow")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(onDutyViewModel.isRestrictByDays.value == 1 && (onDutyViewModel.onDutyAppliedDays.value >= onDutyViewModel.onDutyRestrictDays.value))
                            {
                                Log.d("onDuty","onDuty limit exceeds")
                                Constant.showToast(context, "$sessionName on-duty application limit exceeded")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(onDutyViewModel.isRestrictEmpOnHW.value == 1 && holidayFlag && selectedDay.isEqual(holiday))
                            {
                                Log.d("onDuty","Holiday restriction")
                                Constant.showToast(context, "you cant apply on-duty on holidays")
                                navController.navigate("OnDutyScreen")
                            }
                            else if(onDutyViewModel.isRestrictEmpOnHW.value == 1 && weeklyOffFlag && onDutyViewModel.isWeeklyOff.value == "WeekOff")
                            {
                                Log.d("onDuty","onDuty Weekly off restriction")
                                Constant.showToast(context, "you cant apply on-duty on week-off days")
                                navController.navigate("OnDutyScreen")
                            }
                            else
                            {
                                val checkInTime = if(startTime.value == "00:00") "" else startTime.value
                                val checkOutTime = if(endTime.value == "00:00") "" else endTime.value
                                val sessionType = if(selectedSessionType == "1st Half") 1 else if(selectedSessionType == "2nd Half") 2 else 0
                                val odType = if(selectedSessionType == "1st Half" || selectedSessionType == "2nd Half") 2 else if(selectedSessionType == "Full day") 1 else 0


                                Log.d("onDuty","All fields are filled")
                                statusLoading.value = true

                                // Proceed with password change API call
                                coroutineScope.launch {
                                    onDutyViewModel.postOnDutyDetails(navController, context, empID, onDutyViewModel.ruleId.value.toString(), selectedDateFormatted, odType.toString(), sessionType.toString(), checkInTime, checkOutTime, reasonForOnDuty, org.toString()).let { result ->
                                        when (result) {
                                            is OnDutyViewModel.PostOnDutyResult.Success -> {
                                                Constant.showToast( context,"On-duty Applied Successfully" )
                                                navController.navigate("HomeScreen")
                                            }
                                            is OnDutyViewModel.PostOnDutyResult.Failure -> {
                                                Constant.showToast(context,"Please try again later....." )
                                                navController.navigate("OnDutyScreen")
                                            }
                                        }
                                    }
                                }
                            }

                        }
                        else
                        {

                            if (!Constant.isNetworkAvailable(context))
                            {
                                Constant.showToast(context, "Please check your network connection")
                                navController.navigate("OnDutyScreen")
                            }
                            else
                            {
                                Constant.showToast(context, "please try again after sometime...")
                                navController.navigate("OnDutyScreen")
                            }

                        }


                    }, modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = "Submit Request",
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



@SuppressLint("PrivateResource")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiODRequestPreview() {

    val navController = rememberNavController()

    val index = 0

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "On Duty Request", "HomeScreen") },
        bottomBarContent = { }, //if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    {
        val calendar: Calendar = Calendar.getInstance()


        val OnDutyListData = generateODDataDetails()


        val wfhItem = OnDutyListData[index]
        val allowToApplyPastDays = wfhItem.restrictPastDate
        val allowPastDays = wfhItem.restrictPastOnDutyDays + 1

        calendar.timeInMillis = System.currentTimeMillis()

        if (allowToApplyPastDays == 1) {
            calendar.add(Calendar.DAY_OF_YEAR, (-(allowPastDays)).roundToInt())
        } else {
            calendar.set(1900, 0, 0)
        }


        val isHalfDayNeed = 1

        // Value for storing time as a string
        val startTime = remember { mutableStateOf("00:00") }
        val endTime = remember { mutableStateOf("00:00") }

        var reasonForOnDuty by remember { mutableStateOf("") }


        val selectedDate2: Long = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, Calendar.JUNE)
            set(Calendar.DAY_OF_MONTH, 25)
        }.timeInMillis

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Format the selectedDate2 to "yyyy-MM-dd"
        val selectedDateFormatted = dateFormat.format(Date(selectedDate2))
        val currentDateFormatted = dateFormat.format(Date(System.currentTimeMillis()))
        val isSelectedPastDay = selectedDate2 < System.currentTimeMillis() && selectedDateFormatted != currentDateFormatted


        val formatter1 = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val selectedDateShow = formatter1.format(Date(selectedDate2))


        var expanded by remember { mutableStateOf(false) }
        val selectedSessionType by remember { mutableStateOf("--Select--") }

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState())
        ){
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade))
            ) {
                Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {

                            Icon(
                                painterResource(id = R.drawable.attendance)  , contentDescription ="calendar",
                                tint = colorResource(id = R.color.themeColor),
                                modifier = Modifier.width(20.dp).height(20.dp)
                            )
                        }
                        Column(modifier = Modifier.clickable {  }) {

                            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                            Text(
                                text = formatter.format(Date(selectedDate2)),
                                style = TextStyle(fontFamily = poppins_font,
                                    fontSize = 14.sp, fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black)
                                )
                            )
                        }
                        Column {       }
                    }
                }
            }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Box(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.green))
                        .offset(x = 3.dp)
                        .fillMaxWidth(1f)// Offset the content to the right to make room for the border
                ){
                    Box(modifier = Modifier
                        .background(color = colorResource(id = R.color.light_green))
                        .padding(10.dp)
                        .fillMaxWidth(1f)

                    ) {
                        Text(text = "Requested date $selectedDateShow",
                            style = MaterialTheme.typography.titleSmall,
                            color = colorResource(id = R.color.green)
                        )
                    }
                }

            }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                HorizontalDivider(color = colorResource(id = R.color.lightthemecolor))

                Row()
                {
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
                        Text(text = "Session",
                            style = TextStyle(fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)
                            ),
                        )
                    }

                }
                HorizontalDivider(color = colorResource(id = R.color.lightthemecolor))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 12.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .weight(1f)
                    ) {
                        Text(
                            text = selectedDateShow,
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.black),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp) // Fill the width of the column
                        )
                    }


                    val sessionTypeList: List<String> = if (isHalfDayNeed == 1) {
                        listOf(
                            "Full day",
                            "1st Half",
                            "2nd Half"
                        )
                    } else {
                        listOf("Full day")
                    }


                    val dropdownMenuColors = TextFieldDefaults.colors(
                        cursorColor = Color.White, // Change to your desired color
                        focusedIndicatorColor = Color.Transparent, // Change to your desired color
                        unfocusedIndicatorColor = Color.Transparent, // Change to your desired color
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )

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
                        ) {
                            Box(modifier = Modifier.background(Color.White) )
                            {
                                Row {

                                    // menu box
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = {
                                            expanded = !expanded
                                        },
                                        modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
                                    ) {
                                        TextField(
                                            modifier = Modifier
                                                .menuAnchor()
                                                .background(Color.White), // menuAnchor modifier must be passed to the text field for correctness.
                                            readOnly = true,
                                            value = selectedSessionType,
                                            singleLine = true,
                                            onValueChange = {},
                                            trailingIcon = {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                                    contentDescription = "Down arrow",
                                                    modifier = Modifier.clickable { }
                                                )
                                            },
                                            colors = dropdownMenuColors
                                        )

                                        // menu
                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = {
                                                expanded = false
                                            },
                                            modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
                                        ) {
                                            sessionTypeList.forEach { selectionOption ->
                                                DropdownMenuItem(
                                                    text = { Text(text = selectionOption,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        )) },

                                                    onClick = {  },
                                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                    modifier = Modifier.background(Color.White), // Set the background color of the dropdown menu
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


                //Check in and Check out time

                if(isSelectedPastDay)
                {

                    //Check in and Check out time tile

                    Row {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp, bottom = 10.dp, start = 25.dp),
                        )
                        {
                            Text(text = "Check-In Time",
                                style = TextStyle(fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),)
                        }
                        Column(modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {
                            Text(text = "Check-Out Time",
                                style = TextStyle(fontFamily = poppins_font,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.themeColor)
                                ),
                            )
                        }
                    }



                    //Check in and Check out time
                    Row{

                        //Start Time
                        Column(modifier = Modifier.weight(1f).padding(top = 10.dp, bottom = 10.dp, start = 10.dp))
                        {

                            Row {

                                // For selected Start Time

                                Column(modifier = Modifier.weight(1.5f))
                                {
                                    Button(
                                        onClick = {  },
                                        shape = RoundedCornerShape(10),
                                        colors = ButtonDefaults.buttonColors(colorResource(id = com.google.android.material.R.color.m3_ref_palette_white)),
                                        contentPadding = PaddingValues(start = 3.dp, top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                    ){
                                        BasicTextField(
                                            readOnly = true,
                                            value = startTime.value,
                                            onValueChange = {  },
                                            textStyle = TextStyle(fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.themeColor)
                                            ), singleLine = true,
                                            modifier = Modifier
                                                .padding(start = 5.dp)
                                        )
                                    }
                                }

                                // For Icon Start Time

                                Button(
                                    onClick = {  },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                    contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                ){
                                    Icon(
                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                        contentDescription = "clock",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .height(20.dp)
                                            .width(20.dp)
                                            .size(20.dp)
                                            .clickable {}
                                    )
                                }

                            }


                        }

                        //End Time
                        Column(modifier = Modifier.weight(1f).padding(top = 10.dp, bottom = 10.dp, start = 10.dp))
                        {
                            Row {

                                // For selected End Time

                                Column(modifier = Modifier
                                    .weight(1.5f)
                                ) {
                                    Button(
                                        onClick = {  },
                                        shape = RoundedCornerShape(10),
                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                        contentPadding = PaddingValues(start = 3.dp, top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                    ){
                                        BasicTextField(
                                            readOnly = true,
                                            value = endTime.value,
                                            onValueChange = { /* Handle value change if needed */ },
                                            textStyle = TextStyle(fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.themeColor)
                                            ), singleLine = true,
                                            modifier = Modifier
                                                .padding(start = 5.dp)
                                        )
                                    }
                                }

                                // For Icon End Time

                                Button(
                                    onClick = {  },
                                    shape = RoundedCornerShape(10),
                                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                                    contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                ){
                                    Icon(
                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                        contentDescription = "clock",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .height(20.dp)
                                            .width(20.dp)
                                            .size(20.dp)
                                            .clickable { }
                                    )
                                }

                            }

                        }

                    }

                    HorizontalDivider(color = colorResource(id = R.color.divider))
                }


                val maxLength = 250

                //Reason

                Column {
                    Text(
                        text = "Reason", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.black),
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    )
                    TextField(
                        value = reasonForOnDuty,
                        onValueChange = { if (it.length <= maxLength) reasonForOnDuty = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .background(color = colorResource(id = R.color.white)),
                        maxLines = 5,
                        textStyle = MaterialTheme.typography.titleMedium,
                        placeholder = { Text(text = "Enter Reason...") },
                        singleLine = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = colorResource(id = R.color.black),
                            focusedBorderColor = colorResource(id = R.color.themeColor),
                            unfocusedBorderColor = colorResource(R.color.white),
                        )
                    )
                }


                Column {

                    Button(
                        onClick = { }, modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = "Submit Request",
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
}



fun generateODDataDetails(): List<OnDutyListData>
{
    return listOf(
        OnDutyListData(ruleId = 8, allowOnDuty = 1, hdWorkReq = 1, clockInOut = 0, numberOfDaysOnDuty = 2.0, allowDaysOnDuty = 1, durationOfOnDuty = 1, restrictEmpOnDuty = 1, restrictDayOff = 1, restrictEmpHolidayDate = "1900-01-01T00:00:00", restrictEmpWeeklyOff = "", commentReq = 1, requestExceeds = 0, requestTimes = 0.0, requestApproval = 0, autoApprove = 0, approveTimes = 0.0, approveDuration = 0, restrictPastDate = 1, restrictPastOnDutyDays = 100.05, odMonCnt = 0, takeODDate = "", pastODToCurrentDateCnt = 121, onDutyApplied = 1, onDutyAppApprove = 0,  empWebCheckIn = 1, doj = "2021-09-21T00:00:00", odAppliedDaysApproveCheckInOut = "1900-01-01T00:00:00", odAppliedDaysCheckInOut = "1900-01-01T00:00:00", odDateCnt = 0, wfhDatecnt = 0, odCheckOutOrNot = 1),
    )
}
