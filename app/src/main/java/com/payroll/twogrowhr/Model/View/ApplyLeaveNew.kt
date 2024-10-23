package com.payroll.twogrowhr.Model.View

//import android.Manifest.permission.READ_EXTERNAL_STORAGE
//import android.Manifest.permission.READ_MEDIA_IMAGES
//import android.Manifest.permission.READ_MEDIA_VIDEO
//import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
//import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.format.Formatter
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveDurationResponseModel
import com.payroll.twogrowhr.Model.ResponseModel.LeaveTypeDetailData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant.Companion.MAX_FILE_SIZE_BYTES
import com.payroll.twogrowhr.Model.ResponseModel.LeaveDateListData
import com.payroll.twogrowhr.Model.data.loadImages
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.checkInViewModel
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.sql.Date
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// Define a data class to represent each date's information
data class LeaveItem1(
    val date: LocalDate,
    val day: String,
    var leaveType: String = "--Select Type--", // Initialize as "--Select Type--"
    var selectedIndex: Int = -1, // Initialize as -1
    var isWeeklyOff: Int,
    var status: Int,
    var previous: Int

)

var leaveFileName =  mutableStateOf("Select File")
var leaveFilePath =  mutableStateOf("")
var selectedUri = mutableStateOf<Uri?>(null)



var leaveDateListDetails = mutableStateOf<List<LeaveDateListData>>(emptyList())


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ApplyLeaveNew(navController: NavController, leaveViewModel: LeaveViewModel)
{
    rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave Request", "leave") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { ApplyLeaveScreen(navController = navController, leaveViewModel = leaveViewModel) }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApplyLeaveScreen(navController: NavController, leaveViewModel: LeaveViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp)
    )
    {
        Box()
        {
            Scaffold { MyContent1(navController = navController, leaveViewModel = leaveViewModel) }
        }
    }
}

// Function to format time in 12-hour format with AM/PM
fun formatTimeIn12HourFormat1(hourOfDay: Int, minute: Int): String
{
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
    calendar.set(Calendar.MINUTE, minute)

    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}


// Function to parse time from a formatted string
private fun parseTime1(timeString: String): Calendar?
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


//-------------------------------------------------------------FOR PERMISSIONS--------------------------------------------------------//


fun checkAndRequestStoragePermissions(
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
): Boolean {

    val accessStorage: Array<String> = // Permission request logic

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            arrayOf(READ_MEDIA_VISUAL_USER_SELECTED, READ_MEDIA_IMAGES)
         }
        else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }



    val permissionsToRequest: MutableList<String> = mutableListOf()


    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED)
    {
        return true
    }
    else
    {
        if (!checkInViewModel.arePermissionsGranted(context, accessStorage))
        {
            permissionsToRequest.addAll(accessStorage)
        }

        if (permissionsToRequest.isNotEmpty()) {
            launcher.launch(permissionsToRequest.toTypedArray())
        }

        return permissionsToRequest.isEmpty()
    }

}

//-----------test4------------

@Suppress("DEPRECATION")
@SuppressLint("PrivateResource", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyContent1(navController: NavController, leaveViewModel: LeaveViewModel)
{

    val context = LocalContext.current
    LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()


    val empID = userViewModel.getSFCode()


//FOR PERMISSION

    /*    val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            if (permissionsMap.isNotEmpty()) {
                val areGranted = permissionsMap.values.all { it }
                if (areGranted) {
                    Constant.showToast(context, "Permission Granted")
                } else {
                    Constant.showToast(context, "Permission Denied, Please give the permission to access..!")
                }
            }
        }*/


    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapResults ->
        mapResults.forEach {
            Log.d("Permissions", "Permission: ${it.key} Status: ${it.value}")
        }

            Log.d("Permissions", "Permissionssss: ${mapResults.values}")

        // check if any of the requested permissions is granted or not
        when
        {
            mapResults.values.all { it } -> { Constant.showToast(context, "Permission Granted") }
            mapResults.values.none { it } -> { Constant.showToast(context, "All Permissions Denied, Please give the permission to access..!") }
            else -> { Constant.showToast(context, "Permission Granted") }

        }
    }

    // Fetch and update the Leave list when entering the page
    var leaveTypeList by remember(empID) { mutableStateOf<List<LeaveTypeDetailData>>(emptyList()) }

    var showDialog by remember { mutableStateOf(false) }

    var flag by remember { mutableIntStateOf(0) }

    val leaveDurationList = mutableListOf<String>()
    var leaveDuration by remember { mutableStateOf("") }
    var selectedLeaveType by remember { mutableStateOf("") }
    var selectedLeaveCode by remember { mutableStateOf("") }
    var leaveTypeSelected by remember { mutableStateOf(false) }
    var selectedIndex1 by remember { mutableStateOf<Int?>(null) }
    var selectedOption by remember { mutableStateOf<String?>(null) }


    // Initialize a list of LeaveItem
    val leaveItems = mutableListOf<LeaveItem1>()

    // variables for calculated values
    var availableBalance: String by remember { mutableStateOf("00") }
    var appliedValue: String by remember { mutableStateOf("00") }
    var remainingValue: String by remember { mutableStateOf("00") }
    var appliedHours: String by remember { mutableStateOf("01 : 00") }
    var isFileNeeded: String by remember { mutableStateOf("0") }
    var fileUnits: String by remember { mutableStateOf("0") }



    var leaveListFlag by remember { mutableIntStateOf(0) }

    val showLoad =   remember { mutableStateOf(false) }



    val leaveListState = leaveViewModel.leaveListNew.collectAsState()
    leaveTypeList = leaveListState.value

    val dateFlag = leaveViewModel.leaveDateFlag

    leaveViewModel.leaveDateList.collectAsState().also {
        leaveDateListDetails = it as MutableState<List<LeaveDateListData>>
    }

    Log.d("LeaveType... LeaveTypeListDetails", "Launch : $leaveTypeList")
    Log.d("LeaveType... LeaveTypeListDetails", "Flag Before set : $leaveListFlag")

    leaveListFlag = if(leaveTypeList.isEmpty()) {1} else {2}
    Log.d("LeaveType... LeaveTypeListDetails", "Flag After set : $leaveListFlag")


    val currentDate = remember { LocalDate.now() }


    // Initialize the selected dates list with the current date when entering the page
    val dateArray: Array<LocalDate> = emptyArray()
    val selectedDates = remember { mutableStateListOf(*dateArray) }


    //For Loading

    val loadingStatus = leaveViewModel.loadingStatus4

    var loading by remember { mutableStateOf(false) }

    if(loading)
    {
        linearProgression()
    }



    fun clearList()
    {
        leaveItems.clear()
        selectedDates.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateSelectedDates1(
        fromDate: LocalDate,
        toDate: LocalDate
    ) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // Adjusted date format pattern

        Log.d("updateSelectedDates1....", "Leave Date List : $leaveDateListDetails")

        leaveViewModel.getLeaveDateDetails(navController, context, empID, fromDate, toDate, selectedLeaveCode){ leaveDates ->

            if (leaveDates.isNullOrEmpty())
            {
                Constant.showToast(context, "Please try again after sometime...!")
            }
            else
            {
                try{

                    clearList()

                    for (leaveData in leaveDates) {
                        val date = LocalDateTime.parse(leaveData.date, formatter).toLocalDate()

                        //Hide this if  for display all the dates given from the API ,
                        //this condition add the dates from the from date only
                        /*                        if (date in fromDate..toDate)

                                                {*/
                        selectedDates.add(date)
                        val enableStatus = leaveData.type // Get the enable status from LeaveDateListData
                        val isWeeklyOff = leaveData.enableStatus.toInt()
                        val day = leaveData.weekName
                        val previousCount = leaveData.preCount
                        Log.d("updateSelectedDates1....", "updateSelectedDates1 Inside for loop")
                        Log.d("EnableStatus....", "Date/enableStatus/isWeeklyOff : $date/$enableStatus/$isWeeklyOff")
                        leaveItems.add(LeaveItem1(date = date, day = day, isWeeklyOff = isWeeklyOff, status = enableStatus.toInt(), previous = previousCount.toInt()))
//                        }
                    }

                }
                catch(e : Exception)
                {
                    Log.d("updateSelectedDates1....", "catch : ${e.stackTrace}")
                    Log.d("updateSelectedDates1....", "catch : ${e.message}")

                }

                // Display selected dates in log
                if(selectedLeaveType == "D")
                {
                    for (date in selectedDates)
                    {
                        Log.d("SelectedDates1...............", date.toString())
                    }
                }
                else
                {
                    Log.d("SelectedDates1...............", selectedDates.toString())
                }
            }
        }


    }




    fun validateTime(startTime: MutableState<String>, endTime: MutableState<String>, previousStartTime: MutableState<String>, previousEndTime: MutableState<String>) {
        val startTimeString = startTime.value
        val endTimeString = endTime.value

        if (startTimeString.isNotBlank() && endTimeString.isNotBlank()) {
            val startTimeCalendar = parseTime1(startTimeString)
            val endTimeCalendar = parseTime1(endTimeString)

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
                    val totalMinutes = leaveBalanceHours1(selectedLeaveType, startTime = startTime.value, endTime = endTime.value)
                    val hoursFloat = totalMinutes / 60.0f
                    val hours = totalMinutes / 60
                    val remainingMinutes = totalMinutes % 60
                    appliedValue = String.format("%.2f", hoursFloat)
                    val hoursDisplayed = String.format("%02d", hours)
                    val minutesDisplayed = String.format("%02d", remainingMinutes)
                    appliedHours = "$hoursDisplayed : $minutesDisplayed"
                    remainingValue = String.format("%.2f",availableBalance.toFloat() - hoursFloat)
                    previousStartTime.value = startTime.value
                    previousEndTime.value = endTime.value
                }
            }
        }
    }

    // Declaring a Boolean value to store bottom sheet collapsed state
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    val leaveId = remember { mutableIntStateOf(0) }
    val leaveName = remember { mutableStateOf("--Select Leave Type--") }
    var selectedLeaveName by remember { mutableStateOf(leaveName.value) }


    var reasonForLeave by remember { mutableStateOf("") }
    var selectedOption1 by remember { mutableStateOf<String?>(null) }


    //For Calendar

    var fromDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1)) }
    var toDate by remember { mutableStateOf(currentDate) }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())


    // Define a list of Boolean values to track the expanded state of each dropdown
    var expandedStates by remember { mutableStateOf(List(selectedDates.size) { false }) }


    //For Time Picker

    // Value for storing time as a string
    val startTime = remember { mutableStateOf("00:00") }
    val endTime = remember { mutableStateOf("00:00") }
    val previousStartTime = remember { mutableStateOf(startTime.value) }
    val previousEndTime = remember { mutableStateOf(endTime.value) }


    // Declaring and initializing a calendar
    val startTimeCalendar = Calendar.getInstance()
    val startTimeHourForShown = startTimeCalendar[Calendar.HOUR_OF_DAY]
    val startTimeMinuteForShown = startTimeCalendar[Calendar.MINUTE]

    // Creating a TimePicker dialog
    val startTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, startTimeHour : Int, startTimeMinute: Int ->
            val formattedTime = formatTimeIn12HourFormat1(startTimeHour, startTimeMinute)
            startTime.value = formattedTime
            validateTime(startTime, endTime,previousStartTime,previousEndTime)
        }, startTimeHourForShown, startTimeMinuteForShown, false
    )

    // Declaring and initializing a calendar
    val endTimeCalendar = Calendar.getInstance()
    val endTimeHourForShown = endTimeCalendar[Calendar.HOUR_OF_DAY]
    val endTimeMinuteForShown = endTimeCalendar[Calendar.MINUTE]


    // Creating a TimePicker dialog
    val endTimePickerDialog = TimePickerDialog(
        ContextThemeWrapper(context, R.style.AppTheme),
        {_, endTimeHour : Int, endTimeMinute: Int ->
            val formattedTime = formatTimeIn12HourFormat1(endTimeHour, endTimeMinute)
            endTime.value = formattedTime
            validateTime(startTime, endTime,previousStartTime,previousEndTime)
        }, endTimeHourForShown, endTimeMinuteForShown, false

    )


    fun formatBalance(balance: Double): String {
        val df = DecimalFormat("#,##0.##")
        df.maximumFractionDigits = 2
        return df.format(balance)
    }



    val calendarFrom = Calendar.getInstance()
    calendarFrom.set(1900, 0, 0) // add year, month (Jan), date

    val calendarTo = Calendar.getInstance()
    calendarTo.set(1900, 0, 0) // add year, month (Jan), date

    var selectedFromDate by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    var selectedToDate by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    var fromDateToDisplay by remember {
        mutableStateOf("From Date") // or use mutableStateOf(calendar.timeInMillis)
    }

    var toDateToDisplay by remember {
        mutableStateOf("To Date") // or use mutableStateOf(calendar.timeInMillis)
    }

    var previousSelectedFromDate by remember {
        mutableStateOf(selectedFromDate)
    }

    var previousSelectedToDate by remember {
        mutableStateOf(selectedToDate)
    }


    //For new calendar from OD and WFH


    var showFromDatePicker by remember {
        mutableStateOf(false)
    }

    var showToDatePicker by remember {
        mutableStateOf(false)
    }



    val datePickerStateFrom = rememberDatePickerState(
        initialSelectedDateMillis = selectedFromDate,

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= calendarFrom.timeInMillis
            }

            // users cannot select the years from 2024
            override fun isSelectableYear(year: Int): Boolean {

                val startDate2Date = Date(calendarFrom.timeInMillis)
                val calendar1 = Calendar.getInstance()
                calendar1.time = startDate2Date
                val year1 = calendar1.get(Calendar.YEAR)

                return year >= year1
            }
        }
    )


    val datePickerStateTo = rememberDatePickerState(
        initialSelectedDateMillis = selectedToDate,

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= calendarTo.timeInMillis
            }

            // users cannot select the years from 2024
            override fun isSelectableYear(year: Int): Boolean {

                val startDate2Date = Date(calendarTo.timeInMillis)
                val calendar1 = Calendar.getInstance()
                calendar1.time = startDate2Date
                val year1 = calendar1.get(Calendar.YEAR)

                return year >= year1
            }
        }
    )




    fun clearValues()
    {
        startTime.value = "00:00"
        endTime.value = "00:00"
        appliedValue = "00"
        appliedHours = "00 : 00"

        remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())

    }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Constant.AppTheme {

        if (showFromDatePicker) {

            DatePickerDialog(
                onDismissRequest = {
/*                showFromDatePicker = false
                clearValues()
                selectedFromDate = previousSelectedFromDate // Restore previous selection*/
                },
                confirmButton = {
                    TextButton(onClick = {

                        showFromDatePicker = false

                        val dateInMillis = datePickerStateFrom.selectedDateMillis!!
                        val selectedDate = LocalDate.ofEpochDay(dateInMillis / (24 * 60 * 60 * 1000))


                        if (leaveTypeSelected) {
                            if (selectedLeaveType == "D" && selectedDate > toDate) {
                                clearValues()
                                showFromDatePicker = true

                                if (toDateToDisplay == "To Date")
                                {
                                    Constant.showToast(context, "Select To date first, for apply future leave...")
                                }
                                else
                                {
                                    Constant.showToast(context, "From date cannot be greater than $toDate")
                                }
                            } else if (selectedLeaveType == "H") {
                                fromDate = selectedDate
                                toDate = fromDate // Set the to date equal to the from date for hourly leave
                                clearValues()

                                // Update selectedFromDate and selectedToDate accordingly
                                selectedFromDate = datePickerStateFrom.selectedDateMillis!!
                                selectedToDate = selectedFromDate

                                // Update previous selection
                                previousSelectedFromDate = selectedFromDate
                                previousSelectedToDate = selectedFromDate
                                fromDateToDisplay = formatter.format(Date(selectedFromDate))
                                toDateToDisplay = fromDateToDisplay

                                // Display selected dates in log
                                Log.d("SelectedDate1......", "fromDate : $fromDate")
                                Log.d("SelectedDate2......", "toDate : $toDate")
                            } else {
                                fromDate = selectedDate

                                clearValues()

                                if (toDateToDisplay != "To Date") {

                                    clearList()
                                    filePathClearance()
                                    updateSelectedDates1(fromDate, toDate)

                                }

                                selectedFromDate = datePickerStateFrom.selectedDateMillis!!
                                previousSelectedFromDate =
                                    selectedFromDate // Update previous selection
                                fromDateToDisplay = formatter.format(Date(selectedFromDate))

                                // Display selected dates in log
                                for (date in selectedDates) {
                                    Log.d("SelectedDate1......", date.toString())
                                }

                            }
                        } else {

                            Constant.showToast(context, "Please select a leave type first")
                        }


                    }) {
                        Text(
                            text = "Confirm",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)
                            )
                        )
                    }
                },
                /*
                        dismissButton = {
                            TextButton(onClick = {
                                showFromDatePicker = false
                                clearValues()
                                selectedFromDate = previousSelectedFromDate // Restore previous selection
                            }) {
                                Text(text = "Cancel",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    ))
                            }
                        }
            */
            ) {
                Constant.AppTheme {
                    DatePicker(
                        state = datePickerStateFrom,
                        showModeToggle = false // Edit button for date entry
                    )
                }
            }
        }
    }

    Constant.AppTheme {

        if (showToDatePicker) {

            DatePickerDialog(
                onDismissRequest = {
/*                showToDatePicker = false
                clearValues()
                selectedToDate = previousSelectedToDate // Restore previous selection*/

                },
                confirmButton = {
                    TextButton(onClick = {

                        showToDatePicker = false

                        val dateInMillis = datePickerStateTo.selectedDateMillis!!
                        val selectedDate =
                            LocalDate.ofEpochDay(dateInMillis / (24 * 60 * 60 * 1000))


                        if (leaveTypeSelected) {
                            if (selectedLeaveType == "D" && selectedDate < fromDate) {
                                clearValues()
                                showToDatePicker = true
                                Constant.showToast(context, "To date cannot be less than $fromDate")

                            }
                            /*                        else if (selectedLeaveType == "H")
                                                {
                                                    toDate = selectedDate // Set the to date equal to the from date for hourly leave
                                                    fromDate = selectedDate
                                                    clearValues()

                                                    // Update selectedFromDate and selectedToDate accordingly
                                                    selectedToDate = datePickerStateFrom.selectedDateMillis!!
                                                    selectedFromDate = selectedToDate

                                                    // Update previous selection
                                                    previousSelectedFromDate = selectedToDate
                                                    previousSelectedToDate = selectedToDate

                                                    toDateToDisplay = formatter.format(Date(selectedToDate))
                                                    fromDateToDisplay = toDateToDisplay

                                                    // Display selected dates in log
                                                    Log.d("SelectedDate1......", fromDate.toString())
                                                    Log.d("SelectedDate2......", toDate.toString())
                                                }*/
                            else {
                                if (selectedLeaveType != "H") {
                                    toDate = selectedDate

                                    clearValues()

                                    if (fromDateToDisplay != "From Date") {
                                        clearList()
                                        filePathClearance()
                                        updateSelectedDates1(fromDate, toDate)

                                    }

                                    selectedToDate = datePickerStateTo.selectedDateMillis!!
                                    previousSelectedToDate =
                                        selectedToDate // Update previous selection
                                    toDateToDisplay = formatter.format(Date(selectedToDate))



                                    for (date in selectedDates) {
                                        Log.d("SelectedDate2......", date.toString())
                                    }
                                } else {
                                    Constant.showToast(
                                        context,
                                        "For ${leaveName.value}, Please select a From Date"
                                    )
                                }

                            }
                        } else {
                            Constant.showToast(context, "Please select a leave type first")
                        }


                    }) {
                        Text(
                            text = "Confirm",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.themeColor)
                            )
                        )
                    }
                },
                /*
                        dismissButton = {
                            TextButton(onClick = {
                                showToDatePicker = false
                                clearValues()
                                selectedToDate = previousSelectedToDate // Restore previous selection
                            }) {
                                Text(text = "Cancel",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    ))
                            }
                        }
            */
            ) {
                Constant.AppTheme {
                    DatePicker(
                        state = datePickerStateTo,
                        showModeToggle = false, // Edit button for date entry
                    )
                }
            }
        }

    }

    ModalBottomSheetLayout(
//        scaffoldState = bottomSheetScaffoldState,
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


                                                    if (modalBottomSheetState.isVisible) {
                                                        modalBottomSheetState.hide()
                                                    } else {
                                                        modalBottomSheetState.show()
                                                    }

                                                }

                                                filePathClearance()
                                                leaveName.value = data.name
                                                leaveId.intValue = data.id
                                                leaveDuration = data.durationAllow
                                                selectedLeaveType = data.leaveType
                                                selectedLeaveCode = data.id.toString()
                                                availableBalance = data.balance.toString()
                                                isFileNeeded = data.isFileEnabled
                                                fileUnits = data.fileUnits

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
//        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = Color.Black,
        sheetElevation = 0.dp,
        sheetShape = MaterialTheme.shapes.large,
        scrimColor = Color.Black.copy(alpha = 0.4f), // Semi-transparent black scrim
        sheetState = modalBottomSheetState // Use modalBottomSheetState here

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
                        Column(modifier = Modifier.clickable { showFromDatePicker = true }) {
                            Text(
                                text = fromDateToDisplay,
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
                        Column(modifier = Modifier.clickable { showToDatePicker = true }) {
                            Text(
                                text = toDateToDisplay,
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

                    clearList()

                    if(fromDateToDisplay != "From Date" && toDateToDisplay != "To Date")
                    {
                        updateSelectedDates1(fromDate,toDate)
                    }


                    selectedIndex1 = -1
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



                    if(toDateToDisplay != "To Date")
                    {
                        toDate = fromDate // Set the to date equal to the from date for hourly leave
                        clearValues()

                        // Update selectedFromDate and selectedToDate accordingly
                        selectedToDate = selectedFromDate

                        // Update previous selection
                        previousSelectedToDate = previousSelectedFromDate


                        toDateToDisplay = fromDateToDisplay
                    }




                    remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                }
                reasonForLeave = ""
            }


            @Composable
            fun uiUpdateLeaveDates()
            {
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

                        leaveDaysHeader1()

                        HorizontalDivider(color = colorResource(id = R.color.themeColor))

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

                            Log.d("InsideLazy Column......", "leaveDurationList : $leaveDurationList")
                            Log.d("InsideLazy Column......", "leaveItems : $leaveItems")


                            items(leaveItems) { leaveItem  ->
                                val index = leaveItems.indexOf(leaveItem) // Capture the index here
                                val date = leaveItem.date
                                val preCount = leaveItem.previous
                                val isExpanded = expandedStates[selectedDates.indexOf(date)]
                                Log.d("LeaveItem", "Date: ${leaveItem.date}, selectedIndex: ${leaveItem.selectedIndex}")


                                val displayedDate = dateFormat.format(Date.from( date.atStartOfDay(ZoneId.systemDefault()) .toInstant()))
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
                                            text ="$displayedDate\n${leaveItem.day}" ,
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

                                        val isSelectable = leaveItem.status != 1  //leaveItem.status = 0 //For enable select, leaveItem.isWeeklyOff = 0 // Is not Weekly off


                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = if(isSelectable && leaveItem.isWeeklyOff != 1){colorResource(id = R.color.white)} else {colorResource(id = R.color.divider)}
                                            ),
                                            shape = RoundedCornerShape(5.dp),
                                            modifier =  if (isSelectable && leaveItem.isWeeklyOff != 1) {
                                                Modifier.clickable {
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
                                            } else {
                                                Modifier
                                            }

                                        ) {
                                            Box(modifier = Modifier.padding(10.dp))
                                            {
                                                selectedIndex1 = leaveItem.selectedIndex
//                                                selectedOption = if (isSelectable) { leaveDurationList.getOrNull(selectedIndex1!!) } else { "--" }

                                                if(isSelectable && leaveItem.isWeeklyOff == 0)
                                                {
                                                    selectedOption = leaveDurationList.getOrNull(selectedIndex1!!)
                                                }
                                                else if(isSelectable && leaveItem.isWeeklyOff == 1)
                                                {
                                                    selectedOption = "Full Day"
                                                    leaveItem.leaveType = "Full Day"
                                                    selectedOption = leaveItem.leaveType // Update selected option

                                                    leaveItems[index] = leaveItem

                                                    val applied = leaveBalanceDays1(selectedLeaveType, selectedDates = selectedDates, leaveItems = leaveItems)
                                                    appliedValue = String.format("%.2f", applied )
                                                    remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                                                }
                                                else
                                                {
                                                    selectedOption = "--"
                                                }


//                                                selectedOption = leaveDurationList.getOrNull(selectedIndex1!!)
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

                                                        Log.d("LeaveType... LeaveDurationTypeListDetails", "JsonArray:$jsonArray")

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
                                                                    selectedIndex1 = optionIndex // Update the selected index for the DropdownMenuItem
                                                                    selectedOption1 = option
                                                                    leaveItem.leaveType = option
                                                                    leaveItems[index] = leaveItem
                                                                    val applied = leaveBalanceDays1(selectedLeaveType, selectedDates = selectedDates, leaveItems = leaveItems)
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


            if(fromDateToDisplay != "From Date" && toDateToDisplay != "To Date")
            {

                // LOGIC TO DISPLAY THE UI

                Column()
                {
                    when (selectedLeaveType) {

                        "D" -> {
                            if(flag == 0 || leaveItems.isEmpty()) {

                                Log.d("Inside Lazy column....","selectedLeaveType : $selectedLeaveType")

                                clearList()

                                updateSelectedDates1(fromDate, toDate)

                                flag = 1
                            }



                            if(loadingStatus)
                            {
                                loading = true
                            }
                            else
                            {
                                loading = false

                                if(leaveDateListDetails.value.isEmpty())
                                {
                                    when (dateFlag)
                                    {
                                        0 -> {
                                            loading = true
                                        }
                                        1 -> {
                                            clearList()
                                            updateSelectedDates1(fromDate,toDate)
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
                                    when (dateFlag)
                                    {
                                        0 -> {
                                            loading = true
                                        }
                                        1 -> {
                                            uiUpdateLeaveDates()
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

                        "H" -> {

                            leaveItems.clear() // Clear any previously added leave items
                            toDate = fromDate // Automatically update "To Date" when Hour leave is selected
//                        updateSelectedDates( fromDate, toDate, selectedDates) // Update selected dates accordingly

                            Log.d("Hour type selected....", "Leave Date List 4 : $leaveDateListDetails")

//                            updateSelectedDates1( fromDate, toDate, selectedDates1, leaveDateList) // Update selected dates accordingly

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

                                    leaveHoursHeader1()

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
                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                                contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                            ) {
                                                Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                                {

                                                    Column(modifier = Modifier.weight(1.5f)) {

                                                        BasicTextField(
                                                            readOnly = true,
                                                            value = startTime.value,
                                                            onValueChange = { },
                                                            textStyle = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 14.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.themeColor)
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
                                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
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
                                                                color = colorResource(id = R.color.themeColor)
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
                                            .weight(0.6f)
                                            .padding(5.dp)
                                        ) {


                                            Column(modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.CenterHorizontally))
                                            {

                                                Text(
                                                    text = appliedHours,
                                                    style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.themeColor)
                                                ),
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .padding(top = 20.dp, bottom = 4.dp))


                                                /*                                                BasicTextField(
                                                                                                    readOnly = true,
                                                                                                    value = appliedHours,
                                                                                                    onValueChange = { },
                                                                                                    textStyle = TextStyle(fontFamily = poppins_font,
                                                                                                        fontSize = 14.sp,
                                                                                                        fontWeight = FontWeight(500),
                                                                                                        color = colorResource(id = R.color.themeColor)
                                                                                                    ), singleLine = true,
                                                                                                    modifier = Modifier.align(Alignment.End) // Center the text horizontally
                                                                                                )*/
                                            }



                                            /*                                            Button(
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


                                                                                        }*/
                                        }

                                    }
                                }
                            }
                        }
                        else -> {
                            clearList()
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

            if(showLoad.value)
            {
                circularProgression1(showLoad.value)
            }

            // Function to validate the selected file size
            fun isFileSizeValid(context: Context, uri: Uri): Boolean {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.use { stream ->
                    val fileSize = stream.available().toLong()
                    return fileSize <= MAX_FILE_SIZE_BYTES
                }
                return false
            }

            val path1 = "D:\\websites\\pmcsoftlimited\\"
            val path2 = "Payroll\\Upload\\LeaveUpload\\"

            val contentResolver = LocalContext.current.contentResolver             // Obtain the ContentResolver using LocalContext


            val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {

                    val mimeType = contentResolver.getType(uri)
                    if (mimeType != null && (mimeType.startsWith("image/") || mimeType == "application/pdf"))
                    {

                        // Check if the selected file size is valid
                        if (isFileSizeValid(context, uri))
                        {

                            selectedUri.value = uri // Handle the returned URI
                            leaveFileName.value = getFileName(uri,context)
                            leaveFilePath.value = leaveFileName.value
//                            leaveFilePath.value = "${path1}${Constant.FILE_URL}\\${path2}${leaveFileName.value}"

                            Log.d("Apply Leave New","File size is valid. Proceed with processing.")

                        }
                        else
                        {
                            Constant.showToast(context, "File size exceeds the maximum allowed size.")
                            Log.d("Apply Leave New","File size exceeds the maximum allowed size.")

                        }

                    }
                    else
                    {
                        Constant.showToast(context,"Please select an image or PDF file")
                    }

                }

            }


            @RequiresApi(34)
            @RequiresPermission(anyOf = [Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE])
            @Composable
            fun MediaStoreQueryDialog( onDismiss: () -> Unit, onConfirm: (Uri, String) -> Unit) {

                val context = LocalContext.current
                val files by loadImages(context.contentResolver)

                val buttonText = if(files.isNotEmpty()){"Manage"}else{"Add Images"}

                    AlertDialog(
                        onDismissRequest = onDismiss,
                        title = { Text("Select File") },

                        text = {

                            Column {
                                // Display the number of images found
                                
                                Card(modifier = Modifier.fillMaxWidth(1f),
                                    shape = RoundedCornerShape(8.dp))
                                {
                                    Row(modifier = Modifier.fillMaxWidth())
                                    {
                                        Column(modifier = Modifier.weight(0.75f).padding(start = 5.dp))
                                        {
                                            Text(
                                                text = "you've given access to a select number of photos",
                                                modifier = Modifier.padding(10.dp).fillMaxWidth(),
                                                style = TextStyle(fontFamily = poppins_font, fontSize = 13.sp, fontWeight = FontWeight(400)),
                                                color = colorResource(id = R.color.black)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(0.25f).padding(start = 2.dp, end = 5.dp).align(Alignment.CenterVertically))
                                        {
                                            Card(modifier = Modifier.padding(1.dp).align(Alignment.CenterHorizontally).clickable {
                                                showDialog = false
                                                launcherMultiplePermissions.launch(arrayOf(READ_MEDIA_IMAGES,READ_MEDIA_VISUAL_USER_SELECTED)) },
                                                shape = RoundedCornerShape(8.dp))
                                            {
                                                TextFieldNameSizeValidation(value = "Manage", size = 13, color = colorResource(id = R.color.themeColor), weight = 500, maxLength = 10)
                                            }
                                        }

                                    }
                                }

                                if(files.isNotEmpty())
                                {
                                    Text(
                                        text = "${files.size} images found",
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .fillMaxWidth(),
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                }

                                // Display the images in a grid with 3 columns
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(3),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 300.dp) // Set a maximum height for the grid
                                ) {
                                    items(files.size) { index ->
                                        val file = files[index]
                                        Column(
                                            modifier = Modifier
                                                .padding(3.dp)
                                                .clickable {
                                                    val mimeType =
                                                        context.contentResolver.getType(file.uri)
                                                    if (mimeType != null && (mimeType.startsWith("image/") || mimeType == "application/pdf")) {
                                                        if (isFileSizeValid(context, file.uri)) {
                                                            onConfirm(
                                                                file.uri,
                                                                getFileName(file.uri, context)
                                                            )
                                                        } else {
                                                            Constant.showToast(
                                                                context,
                                                                "File size exceeds the maximum allowed size."
                                                            )
                                                            Log.d(
                                                                "Apply Leave New",
                                                                "File size exceeds the maximum allowed size."
                                                            )
                                                        }
                                                    } else {
                                                        Constant.showToast(
                                                            context,
                                                            "Please select an image or PDF file"
                                                        )
                                                    }
                                                }
                                        ) {
                                            AsyncImage(
                                                model = file.uri,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(128.dp)
                                                    .aspectRatio(1f)
                                            )
                                            Text(
                                                text = file.name,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
/*                                            Text(
                                                text = file.mimeType,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )*/
                                            Text(
                                                text = Formatter.formatShortFileSize(context, file.size),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                        },
                        confirmButton = {

                            Row()
                            {
                                TextButton(onClick = onDismiss) {
                                    Text("Close")
                                }
                            }

                        }
                    )

            }


            if(showDialog)
            {

                MediaStoreQueryDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { uri, fileName ->
                        selectedUri.value = uri // Handle the returned URI
                        leaveFileName.value = fileName
                        leaveFilePath.value = leaveFileName.value
                        showDialog = false
                    }
                )
            }


            if(isFileNeeded == "1" && appliedValue >= fileUnits)
            {
                Column {

                    Row{



                        Column(modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)){

                            Row{

                                Column( modifier = Modifier.padding(start = 10.dp))
                                {
                                    PlainTooltipBox(
                                        tooltip = {
                                            Text(
                                                text = "Please attach an image or PDF file with a size not exceeding 2MB",
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
                                            contentDescription = "Information",
                                            modifier = Modifier
                                                .padding(top = 10.dp, bottom = 10.dp)
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .tooltipTrigger(),
                                            contentScale = ContentScale.Crop
                                        )

                                    }
                                }

                                Column( modifier = Modifier.padding(start = 10.dp))
                                {

                                    Text(
                                        text = "Upload File",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                    )

                                }

                            }
                        }


                        Column(modifier = Modifier
                            .padding(10.dp)
                            .weight(1f)){
                            Button(onClick = {


                                filePathClearance()

                                val granted = checkAndRequestStoragePermissions(
                                    context = context,
                                    launcher = launcherMultiplePermissions
                                )

                                Log.d("granted value", "$granted")

                                if(granted)
                                {
//                                    getContent.launch("image/*,application/pdf")
//                                    getContent.launch("*/*")

                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE  )
                                    {
                                        if(ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PERMISSION_GRANTED)
                                        {
                                            getContent.launch("*/*")
                                        }
                                        else showDialog = ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED

                                    }
                                    else
                                    {
                                        getContent.launch("*/*")
                                    }

                                }





//                                mediaFlag = 1




                            },
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)))
                            {

                                if (leaveFileName.value.length > 13) {
                                    PlainTooltipBox(
                                        tooltip = {
                                            Text(
                                                text = leaveFileName.value,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight(500),
                                                ),
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = leaveFileName.value.take(13) + "..." ,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.white)
                                            ),
                                            modifier = Modifier.tooltipTrigger()
                                        )
                                    }
                                }
                                else
                                {
                                    Text(
                                        text = leaveFileName.value,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
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


            Column {
                Button(
                    onClick = {

                        val formattedStartTime = convertTo24HourFormat1(startTime.value)
                        val formattedEndTime = convertTo24HourFormat1(endTime.value)

                        val fileFlag = leaveFileName.value.isEmpty() || leaveFileName.value == "Select File"

                        if (selectedLeaveType == "D") {

                            // Update leaveItems to remove "--Select Type--" if status is 1
                            leaveItems.forEach { leaveItem ->
                                if (leaveItem.leaveType == "--Select Type--" && leaveItem.status == 1) {
                                    leaveItem.leaveType = "" // Or any default value other than "--Select Type--"
                                }
                            }

                            val hasInvalidDuration = leaveItems.any { it.leaveType == "--Select Type--" }


                            if (fromDateToDisplay == "From Date")
                            {
                                Constant.showToast(context, "Please select the From Date....")
                            }
                            else if (toDateToDisplay == "To Date")
                            {
                                Constant.showToast(context, "Please select the To Date....")
                            }
                            else if (hasInvalidDuration)
                            {
                                Constant.showToast(context, "Please select the Duration....")
                            }
                            else if (reasonForLeave.isEmpty() || reasonForLeave.isBlank())
                            {
                                reasonForLeave = ""
                                Constant.showToast(context, "Please enter the reason....")
                            }
                            else if (isFileNeeded == "1" && appliedValue >= fileUnits && fileFlag)
                            {
                                Constant.showToast(context, "Please upload the document....")
                            }
                            else if(appliedValue == "00")
                            {
                                Constant.showToast(context, "Please select the valid dates....")
                            }
                            else
                            {
                                showLoad.value = true

                                applyLeave1(
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
                                    remainingValue,isFileNeeded,fileUnits
                                )


                                /*
                                                                if (isFileNeeded == "1" && appliedValue >= fileUnits)
                                                                {

                                                                    Log.d("selectedUri..", selectedUri.value!!.toString())

                                                                    if (selectedUri.value != null)
                                                                    {
                                //                                        leaveViewModel.uploadFile(context, selectedUri.value!!, leaveViewModel,navController,empID,leaveId,leaveItems = leaveItems,fromDate,toDate, selectedLeaveType,  reasonForLeave, formattedStartTime, formattedEndTime, appliedValue, availableBalance, remainingValue)

                                                                        applyLeave1(
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
                                                                            remainingValue,isFileNeeded,fileUnits
                                                                        )

                                                                    }
                                                                    else
                                                                    {
                                                                        Constant.showToast(context, "Please upload the document....")
                                                                    }

                                                                }
                                                                else
                                                                {

                                                                    applyLeave1(
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
                                                                        remainingValue,
                                                                        isFileNeeded,
                                                                        fileUnits
                                                                    )
                                                                }


                                                              */

                            }
                        }
                        else if(selectedLeaveType == "H")
                        {
                            if (fromDateToDisplay == "From Date")
                            {
                                Constant.showToast(context, "Please select the From Date....")
                            }
                            else if (toDateToDisplay == "To Date")
                            {
                                Constant.showToast(context, "Please select the To Date....")
                            }
                            else if (startTime.value == "00:00")
                            {
                                Constant.showToast(context, "Please select the Start Time....")
                            }
                            else if (endTime.value == "00:00")
                            {
                                Constant.showToast(context, "Please select the End Time....")
                            }
                            else if (reasonForLeave.isEmpty() || reasonForLeave.isBlank())
                            {
                                reasonForLeave = ""
                                Constant.showToast(context, "Please enter the reason....")
                            }
                            else {
                                showLoad.value = true

                                applyLeave1(
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
                                    remainingValue,isFileNeeded,fileUnits
                                )

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
fun leaveHoursHeader1()
{

    Row {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp),
            )
        {
            Text(text = "Start Time",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.align(Alignment.Start))
        }
        Column(modifier = Modifier
            .weight(1f)
            .padding(top = 10.dp, bottom = 10.dp, start = 12.dp)) {
            Text(text = "End Time",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Column(modifier = Modifier
            .weight(0.6f)
            .padding(top = 10.dp, bottom = 10.dp)) {
            Text(text = "Total Hrs",
                style = TextStyle(fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}


@SuppressLint("ComposableNaming")
@Composable
fun leaveDaysHeader1()
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

fun leaveBalanceHours1(
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


fun leaveBalanceDays1(
    selectedLeaveType: String,
    selectedDates: SnapshotStateList<LocalDate>,
    leaveItems: MutableList<LeaveItem1>,
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



fun getFileName(uri: Uri, context: Context): String {

    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(displayNameIndex)
            }
        }
        "Unknown File"
    } catch (e: Exception) {
        "Unknown File"
    }
}

@SuppressLint("SuspiciousIndentation")
fun applyLeave1(context: Context, leaveViewModel: LeaveViewModel, navController: NavController, empID: String, leaveId: MutableState<Int>, leaveItems: MutableList<LeaveItem1>, fromDate: LocalDate, toDate: LocalDate, selectedLeaveType: String, reason: String, startTime: String, endTime: String, appliedDays: String, availableDays: String, balanceDays: String, isFileNeeded : String, fileUnits : String)
{

// Submit the leave request or call API here if validation passes

    leaveViewModel.leaveFormValidate1(navController, context, empID, leaveId, fromDate, toDate, appliedDays) { message ->
        Log.e("Apply Leave : message", "message : $message")
        val validate = message.isNullOrEmpty()

//        val validate = message == "0"
        Log.e("Apply Leave : Msg", "Msg : $message")
        Log.e("Apply Leave : Msg", "validate : $validate")

        if(validate)
        {
            Log.e("Apply Leave : Msg", "true : jsonObject1 : $message")

            if (isFileNeeded == "1" && appliedDays >= fileUnits) {

                Log.d("selectedUri..", selectedUri.value!!.toString())

                if (selectedUri.value != null)
                {
                    leaveViewModel.uploadFile(
                        context,
                        selectedUri.value!!,
                        navController,
                        empID,
                        leaveId,
                        leaveItems = leaveItems,
                        fromDate,
                        toDate,
                        selectedLeaveType,
                        reason,
                        startTime,
                        endTime,
                        appliedDays,
                        availableDays,
                        balanceDays
                    )
                }
                else
                {
                    Constant.showToast(context, "Please upload the document....")
                }

            }
            else
            {
                leaveViewModel.postLeaveForm1(context, navController,empID, leaveId, leaveItems = leaveItems, fromDate, toDate, startTime, endTime, selectedLeaveType, reason, appliedDays, availableDays, balanceDays)
            }
        }
        else
        {
            Log.e("Apply Leave : Msg", "false : jsonObject1 : $message")
            filePathClearance()
            Constant.showToast(context, message.toString())
            navController.navigate("applyLeave")
        }
    }
}


fun convertTo24HourFormat1(time12Hour: String): String {
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


fun formatLeaveData1(fromDate: String, selectedLeaveType: String, startTime: String, endTime: String, appliedDays: String, leaveItems: List<LeaveItem1>): Any {

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

                val dayTypeId =  when (leaveItem.leaveType) {
                    "Full Day" -> "1"
                    "1st Half" -> "2"
                    "2nd Half" -> "3"
                    else -> ""
                }

                val dateDetails = JSONObject()
                dateDetails.put("date", leaveItem.date)
                dateDetails.put("interval", interval.toString())
//                dateDetails.put("dayTypeId", (leaveItem.selectedIndex + 1).toString())
                dateDetails.put("dayTypeId", dayTypeId)
                dateDetails.put(
                    "dayType", when (leaveItem.leaveType) {
                        "Full Day" -> "Full Day"
                        "1st Half" -> "1st Half"
                        "2nd Half" -> "2nd Half"
                        else -> ""
                    }
                )
                dateDetails.put("enableStatus", leaveItem.status.toString())
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

fun filePathClearance()
{
    leaveFileName.value = "Select File"
    leaveFilePath.value = ""
    selectedUri = mutableStateOf(null)
}




@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLeaveApplyPreview() {

    val navController = rememberNavController()

    val leaveTypeList = generateLeaveTypeDataList()

    val leaveDateListDetails = generateLeaveDateDetailsList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave Request", "leave") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {


        Column(modifier = Modifier.fillMaxSize().background(color = colorResource(id = R.color.backgroundColor)).padding(top = 80.dp))
        {
            Box()
            {

                val coroutineScope = rememberCoroutineScope()

                var flag by remember { mutableIntStateOf(1) }
                val dateFlag by remember { mutableIntStateOf(1) }

                val leaveDurationList = mutableListOf<String>()
                var leaveDuration by remember { mutableStateOf("[{\\\"id\\\":\\\"1\\\",\\\"name\\\": \\\"Full Day\\\",\\\"des\\\": \\\"Full Day\\\"}]") }
                var selectedLeaveType by remember { mutableStateOf("D") }
                var selectedLeaveCode by remember { mutableStateOf("232") }
                var leaveTypeSelected by remember { mutableStateOf(false) }
                var selectedIndex1 by remember { mutableStateOf<Int?>(null) }
                var selectedOption by remember { mutableStateOf<String?>(null) }


                // Initialize a list of LeaveItem
                val leaveItems = mutableListOf<LeaveItem1>()

                // variables for calculated values
                var availableBalance: String by remember { mutableStateOf("03") }
                var appliedValue: String by remember { mutableStateOf("00") }
                var remainingValue: String by remember { mutableStateOf("00") }
                var appliedHours: String by remember { mutableStateOf("01 : 00") }
                var isFileNeeded: String by remember { mutableStateOf("0") }
                var fileUnits: String by remember { mutableStateOf("0") }


                var leaveListFlag by remember { mutableIntStateOf(0) }

                val showLoad = remember { mutableStateOf(false) }

                leaveListFlag = if (leaveTypeList.isEmpty()) { 1 } else { 2 }


                val currentDate = remember { LocalDate.now() }


                // Initialize the selected dates list with the current date when entering the page
                val dateArray: Array<LocalDate> = emptyArray()
                val selectedDates = remember { mutableStateListOf(*dateArray) }


                fun clearList() {
                    leaveItems.clear()
                    selectedDates.clear()
                }

                @RequiresApi(Build.VERSION_CODES.O)
                fun updateSelectedDates1(fromDate: LocalDate, toDate: LocalDate)
                {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // Adjusted date format pattern

                    if (!leaveDateListDetails.isNullOrEmpty()) {

                        try {

                            clearList()

                            for (leaveData in leaveDateListDetails) {
                                val date = LocalDateTime.parse(leaveData.date, formatter).toLocalDate()

                                selectedDates.add(date)
                                val enableStatus = leaveData.type // Get the enable status from LeaveDateListData
                                val isWeeklyOff = leaveData.enableStatus.toInt()
                                val day = leaveData.weekName
                                val previousCount = leaveData.preCount

                                leaveItems.add(LeaveItem1(date = date, day = day, isWeeklyOff = isWeeklyOff, status = enableStatus.toInt(), previous = previousCount.toInt()))

                            }

                        } catch (e: Exception) {
                            Log.d("updateSelectedDates1....", "catch : ${e.stackTrace}")
                            Log.d("updateSelectedDates1....", "catch : ${e.message}")
                        }

                    }
                }

                // Declaring a Boolean value to store bottom sheet collapsed state

                val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


                val leaveId = remember { mutableIntStateOf(232) }
                val leaveName = remember { mutableStateOf("Yearly") }
                var selectedLeaveName by remember { mutableStateOf(leaveName.value) }


                var reasonForLeave by remember { mutableStateOf("") }
                var selectedOption1 by remember { mutableStateOf<String?>(null) }


                //For Calendar

                var fromDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1)) }
                var toDate by remember { mutableStateOf(currentDate) }

                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())


                // Define a list of Boolean values to track the expanded state of each dropdown
                var expandedStates by remember { mutableStateOf(List(selectedDates.size) { false }) }


                //For Time Picker

                // Value for storing time as a string
                val startTime = remember { mutableStateOf("00:00") }
                val endTime = remember { mutableStateOf("00:00") }
                val previousStartTime = remember { mutableStateOf(startTime.value) }
                val previousEndTime = remember { mutableStateOf(endTime.value) }

                fun formatBalance(balance: Double): String {
                    val df = DecimalFormat("#,##0.##")
                    df.maximumFractionDigits = 2
                    return df.format(balance)
                }


                val calendarFrom = Calendar.getInstance()
                calendarFrom.set(1900, 0, 0) // add year, month (Jan), date

                val calendarTo = Calendar.getInstance()
                calendarTo.set(1900, 0, 0) // add year, month (Jan), date

                var selectedFromDate: Long = Calendar.getInstance().apply {
                    set(Calendar.YEAR, 2024)
                    set(Calendar.MONTH, Calendar.APRIL)
                    set(Calendar.DAY_OF_MONTH, 12)
                }.timeInMillis

                var selectedToDate: Long = Calendar.getInstance().apply {
                    set(Calendar.YEAR, 2024)
                    set(Calendar.MONTH, Calendar.APRIL)
                    set(Calendar.DAY_OF_MONTH, 14)
                }.timeInMillis

                var fromDateToDisplay by remember {
                    mutableStateOf("From Date") // or use mutableStateOf(calendar.timeInMillis)
                }

                var toDateToDisplay by remember {
                    mutableStateOf("To Date") // or use mutableStateOf(calendar.timeInMillis)
                }

                var previousSelectedFromDate by remember {
                    mutableStateOf(selectedFromDate)
                }

                var previousSelectedToDate by remember {
                    mutableStateOf(selectedToDate)
                }


                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())


                // Update previous selection
                previousSelectedFromDate = selectedFromDate
                previousSelectedToDate = selectedFromDate
                fromDateToDisplay = formatter.format(Date(selectedFromDate))
                toDateToDisplay = formatter.format(Date(selectedToDate))

                fromDate = LocalDate.ofEpochDay(selectedFromDate / (24 * 60 * 60 * 1000))
                toDate = LocalDate.ofEpochDay(selectedToDate / (24 * 60 * 60 * 1000))

                updateSelectedDates1(fromDate, toDate)


                fun clearValues() {
                    startTime.value = "00:00"
                    endTime.value = "00:00"
                    appliedValue = "00"
                    appliedHours = "00 : 00"

                    remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())

                }



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


                                                                if (modalBottomSheetState.isVisible) {
                                                                    modalBottomSheetState.hide()
                                                                } else {
                                                                    modalBottomSheetState.show()
                                                                }

                                                            }

                                                            filePathClearance()
                                                            leaveName.value = "Yearly"
                                                            leaveId.intValue = 232
                                                            leaveDuration = "[{\\\"id\\\":\\\"1\\\",\\\"name\\\": \\\"Full Day\\\",\\\"des\\\": \\\"Full Day\\\"}]"
                                                            selectedLeaveType = "D"
                                                            selectedLeaveCode = "232"
                                                            availableBalance = "3.0"
                                                            isFileNeeded = "0"
                                                            fileUnits = "0"

                                                            leaveTypeSelected = true // Set leaveTypeSelected to true


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
                                                                                fontWeight = FontWeight(
                                                                                    500
                                                                                ),
                                                                            ),
                                                                        )
                                                                    }
                                                                ) {
                                                                    Text(
                                                                        text = data.name.take(25) + "...",
                                                                        style = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 13.sp,
                                                                            fontWeight = FontWeight(
                                                                                500
                                                                            ),
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
                                                                            fontWeight = FontWeight(
                                                                                500
                                                                            ),
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
                                    Column(modifier = Modifier.clickable {  }) {
                                        Text(
                                            text = fromDateToDisplay,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
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
                                    Column(modifier = Modifier.clickable { }) {
                                        Text(
                                            text = toDateToDisplay,
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

                        if (selectedLeaveName != leaveName.value) {
                            selectedLeaveName = leaveName.value
                            if (selectedLeaveType == "D") {

                                clearList()

                                if (fromDateToDisplay != "From Date" && toDateToDisplay != "To Date") {
                                    updateSelectedDates1(fromDate, toDate)
                                }

                                selectedIndex1 = -1
                                flag = 0
                                appliedValue = "00"
                                remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                            }
                            if (selectedLeaveType == "H") {

                                startTime.value = "00:00"
                                endTime.value = "00:00"
                                appliedValue = "00"
                                appliedHours = "00 : 00"



                                if (toDateToDisplay != "To Date") {
                                    toDate = fromDate // Set the to date equal to the from date for hourly leave
                                    clearValues()
                                    // Update selectedFromDate and selectedToDate accordingly
                                    selectedToDate = selectedFromDate
                                    // Update previous selection
                                    previousSelectedToDate = previousSelectedFromDate

                                    toDateToDisplay = fromDateToDisplay
                                }




                                remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                            }
                            reasonForLeave = ""
                        }


                        @Composable
                        fun uiUpdateLeaveDates()
                        {
                            Box(modifier = Modifier.height(200.dp).background(color = colorResource(id = R.color.backgroundColor)))
                            {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = colorResource(id = R.color.backgroundColor))
                                        .padding(top = 5.dp, bottom = 5.dp)
                                ) {

                                    HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                    leaveDaysHeader1()

                                    HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                    LazyColumn {

                                        item {
                                            Spacer(modifier = Modifier.height(5.dp))
                                        }

                                        // Ensure that expandedStates has the same size as selectedDates
                                        if (expandedStates.size != selectedDates.size) {
                                            expandedStates = MutableList(selectedDates.size) { false }
                                        }


                                        items(leaveItems) { leaveItem ->
                                            val index = leaveItems.indexOf(leaveItem) // Capture the index here
                                            val date = leaveItem.date
                                            val preCount = leaveItem.previous
                                            val isExpanded = expandedStates[selectedDates.indexOf(date)]

                                            val displayedDate = dateFormat.format(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
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
                                                        text = "$displayedDate\n${leaveItem.day}",
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

                                                    val isSelectable = leaveItem.status != 1  //leaveItem.status = 0 //For enable select, leaveItem.isWeeklyOff = 0 // Is not Weekly off


                                                    Card(
                                                        colors = CardDefaults.cardColors(
                                                            containerColor = if (isSelectable && leaveItem.isWeeklyOff != 1) {
                                                                colorResource(id = R.color.white)
                                                            } else {
                                                                colorResource(id = R.color.divider)
                                                            }
                                                        ),
                                                        shape = RoundedCornerShape(5.dp),
                                                        modifier = if (isSelectable && leaveItem.isWeeklyOff != 1) {
                                                            Modifier.clickable {
                                                                expandedStates =
                                                                    expandedStates.toMutableList()
                                                                        .apply {
                                                                            set(index, !isExpanded)
                                                                            for (i in indices) {
                                                                                if (i != index) {
                                                                                    set(i, false)
                                                                                }
                                                                            }
                                                                        }
                                                            }
                                                        } else {
                                                            Modifier
                                                        }

                                                    ) {
                                                        Box(modifier = Modifier.padding(10.dp))
                                                        {
                                                            selectedIndex1 = leaveItem.selectedIndex
//                                                selectedOption = if (isSelectable) { leaveDurationList.getOrNull(selectedIndex1!!) } else { "--" }

                                                            if (isSelectable && leaveItem.isWeeklyOff == 0) {
                                                                selectedOption = leaveDurationList.getOrNull(selectedIndex1!!)
                                                            }
                                                            else if (isSelectable && leaveItem.isWeeklyOff == 1) {
                                                                selectedOption = "Full Day"
                                                                leaveItem.leaveType = "Full Day"
                                                                selectedOption = leaveItem.leaveType // Update selected option

                                                                leaveItems[index] = leaveItem

                                                                val applied = leaveBalanceDays1(
                                                                    selectedLeaveType,
                                                                    selectedDates = selectedDates,
                                                                    leaveItems = leaveItems
                                                                )
                                                                appliedValue = String.format("%.2f", applied)
                                                                remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                                                            }
                                                            else
                                                            {
                                                                selectedOption = "--"
                                                            }


//                                                selectedOption = leaveDurationList.getOrNull(selectedIndex1!!)
                                                            Row {
                                                                Column {
                                                                    Text(
                                                                        text = selectedOption ?: "Select Type",
                                                                        style = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 14.sp,
                                                                            fontWeight = FontWeight(
                                                                                500
                                                                            ),
                                                                            color = colorResource(id = R.color.black)
                                                                        )
                                                                    )
                                                                }
                                                                Column(
                                                                    modifier = Modifier.fillMaxWidth(
                                                                        1f
                                                                    ),
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
                                                                    expanded = true,
                                                                    onDismissRequest = {
                                                                        // Close the dropdown when dismissed
                                                                        expandedStates =
                                                                            expandedStates.toMutableList()
                                                                                .apply {
                                                                                    set(
                                                                                        index,
                                                                                        false
                                                                                    )
                                                                                }
                                                                    },
                                                                    modifier = Modifier.wrapContentSize(
                                                                        Alignment.Center
                                                                    ),
                                                                ) {

                                                                    val jsonArray =
                                                                        JSONArray(leaveDuration)

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
                                                                        leaveDurationList.add(
                                                                            leaveDurationType.des
                                                                        )
                                                                    }

                                                                    leaveDurationList.forEachIndexed { optionIndex, option ->
                                                                        DropdownMenuItem(
                                                                            onClick = {

                                                                                leaveItem.selectedIndex = optionIndex // Update the selected index for this item
                                                                                selectedIndex1 = optionIndex // Update the selected index for the DropdownMenuItem
                                                                                selectedOption1 = option
                                                                                leaveItem.leaveType = option
                                                                                leaveItems[index] = leaveItem
                                                                                val applied = leaveBalanceDays1(selectedLeaveType, selectedDates = selectedDates, leaveItems = leaveItems)
                                                                                appliedValue = String.format("%.2f", applied)
                                                                                remainingValue = String.format("%.2f", availableBalance.toFloat() - appliedValue.toFloat())
                                                                                expandedStates =
                                                                                    expandedStates.toMutableList().apply { set(index, false) }
                                                                            },
                                                                            modifier = Modifier.width(IntrinsicSize.Max).height(IntrinsicSize.Min)
                                                                        ) {
                                                                            Text(
                                                                                text = option,
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


                        if (fromDateToDisplay != "From Date" && toDateToDisplay != "To Date") {

                            // LOGIC TO DISPLAY THE UI

                            Column()
                            {
                                when (selectedLeaveType) {

                                    "D" -> {
                                        if (flag == 0 || leaveItems.isEmpty()) {

                                            clearList()

                                            updateSelectedDates1(fromDate, toDate)

                                            flag = 1
                                        }



                                        when (dateFlag) {

                                            1 -> {
                                                uiUpdateLeaveDates()
                                            }

                                            else -> {
                                                noDataView()
                                            }

                                        }


                                    }

                                    "H" -> {

                                        leaveItems.clear() // Clear any previously added leave items
                                        toDate = fromDate // Automatically update "To Date" when Hour leave is selected

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

                                                leaveHoursHeader1()

                                                HorizontalDivider(color = colorResource(id = R.color.themeColor))

                                                Row(modifier = Modifier.weight(1f).padding(top = 10.dp, bottom = 10.dp, start = 10.dp)) {


                                                    //Start Time
                                                    Column(
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(5.dp)
                                                    ) {

                                                        Button(
                                                            onClick = {  },
                                                            shape = RoundedCornerShape(10),
                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                                        ) {
                                                            Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                                            {

                                                                Column(modifier = Modifier.weight(1.5f))
                                                                {

                                                                    BasicTextField(
                                                                        readOnly = true,
                                                                        value = startTime.value,
                                                                        onValueChange = { },
                                                                        textStyle = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 14.sp,
                                                                            fontWeight = FontWeight(500),
                                                                            color = colorResource(id = R.color.themeColor)
                                                                        ), singleLine = true,
                                                                        modifier = Modifier
                                                                            .padding(start = 5.dp)
                                                                            .clickable {  }
                                                                    )
                                                                }

                                                                Card(
                                                                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                                                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                                                )
                                                                {
                                                                    Icon(
                                                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                                                        contentDescription = "clock",
                                                                        tint = colorResource(id = R.color.themeColor),
                                                                        modifier = Modifier
                                                                            .height(20.dp)
                                                                            .width(20.dp)
                                                                            .size(25.dp)
                                                                    )
                                                                }

                                                            }

                                                        }
                                                    }


                                                    //End Time
                                                    Column(modifier = Modifier.weight(1f).padding(5.dp))
                                                    {

                                                        Button(
                                                            onClick = {  },
                                                            shape = RoundedCornerShape(10),
                                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                                                            contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp) //start = 15.dp, end = 15.dp
                                                        ) {
                                                            Row(Modifier.padding(top = 4.dp, bottom = 4.dp))
                                                            {

                                                                Column(modifier = Modifier.weight(1.5f))
                                                                {
                                                                    BasicTextField(
                                                                        readOnly = true,
                                                                        value = endTime.value,
                                                                        onValueChange = { },
                                                                        textStyle = TextStyle(
                                                                            fontFamily = poppins_font,
                                                                            fontSize = 14.sp,
                                                                            fontWeight = FontWeight(
                                                                                500
                                                                            ),
                                                                            color = colorResource(id = R.color.themeColor)
                                                                        ), singleLine = true,
                                                                        modifier = Modifier
                                                                            .padding(start = 5.dp)
                                                                            .clickable { }
                                                                    )
                                                                }


                                                                Card(
                                                                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                                                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                                                )
                                                                {
                                                                    Icon(
                                                                        painterResource(id = com.google.android.material.R.drawable.ic_clock_black_24dp),
                                                                        contentDescription = "clock",
                                                                        tint = colorResource(id = R.color.themeColor),
                                                                        modifier = Modifier.height(20.dp).width(20.dp).size(25.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }


                                                    //Applied Value
                                                    Column(modifier = Modifier.weight(0.6f).padding(5.dp))
                                                    {


                                                        Column(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))
                                                        {

                                                            Text(
                                                                text = appliedHours,
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.themeColor)
                                                                ),
                                                                modifier = Modifier
                                                                    .align(Alignment.End)
                                                                    .padding(
                                                                        top = 20.dp,
                                                                        bottom = 4.dp
                                                                    )
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    else -> {
                                        clearList()
                                        Box(
                                            modifier = Modifier
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
                                            text = "Available", style = TextStyle(
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
                                            .weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
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
                                            .weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
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
                                        reasonForLeave = it
                                },

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .background(color = colorResource(id = R.color.white)),
                                maxLines = 5,
                                textStyle = MaterialTheme.typography.titleMedium,
                                placeholder = {
                                    Text(
                                        text = "Enter Reason...",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                        ),
                                    )
                                },
                                singleLine = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = colorResource(id = R.color.black),
                                    focusedBorderColor = colorResource(id = R.color.themeColor),
                                    unfocusedBorderColor = colorResource(R.color.white),
                                ),
                            )
                        }


                        if (isFileNeeded == "1" && appliedValue >= fileUnits) {
                            Column {

                                Row {


                                    Column(modifier = Modifier.padding(10.dp).weight(1f)) {

                                        Row {

                                            Column(modifier = Modifier.padding(start = 10.dp))
                                            {
                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = "Please attach an image or PDF file with a size not exceeding 2MB",
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
                                                        contentDescription = "Information",
                                                        modifier = Modifier
                                                            .padding(top = 10.dp, bottom = 10.dp)
                                                            .size(20.dp)
                                                            .clip(CircleShape)
                                                            .tooltipTrigger(),
                                                        contentScale = ContentScale.Crop
                                                    )

                                                }
                                            }

                                            Column(modifier = Modifier.padding(start = 10.dp))
                                            {

                                                Text(
                                                    text = "Upload File",
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    modifier = Modifier.padding(
                                                        top = 10.dp,
                                                        bottom = 10.dp
                                                    )
                                                )

                                            }

                                        }
                                    }


                                    Column(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .weight(1f)
                                    ) {
                                        Button(
                                            onClick = {},
                                            modifier = Modifier.fillMaxSize(),
                                            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor))
                                        )
                                        {

                                            if (leaveFileName.value.length > 13) {
                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = leaveFileName.value,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = leaveFileName.value.take(13) + "...",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.white)
                                                        ),
                                                        modifier = Modifier.tooltipTrigger()
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = leaveFileName.value,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp,
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


                        Column {
                            Button(
                                onClick = {  }, modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(
                                        id = R.color.themeColor
                                    )
                                ),
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
        }
    }
}




fun generateLeaveTypeDataList(): List<LeaveTypeDetailData>
{
    return listOf(
        LeaveTypeDetailData( id = 232, name = "Yearly", leaveName = "YR", balance = 3.0, coffType = 0, leaveType = "D", durationAllow = "[{\\\"id\\\":\\\"1\\\",\\\"name\\\": \\\"Full Day\\\",\\\"des\\\": \\\"Full Day\\\"}]", isFileEnabled = "0", fileUnits = "0"),
        LeaveTypeDetailData( id = 233, name = "Monthly", leaveName = "MON", balance = 10.0, coffType = 0, leaveType = "D", durationAllow = "[{\\\"id\\\": \\\"1\\\",\\\"name\\\": \\\"Full Day\\\",\\\"des\\\": \\\"Full Day\\\"},{\\\"id\\\": \\\"2\\\",\\\"name\\\": \\\"Half Day\\\",\\\"des\\\": \\\"1st Half\\\"},{\\\"id\\\": \\\"3\\\",\\\"name\\\": \\\"Half Day\\\",\\\"des\\\": \\\"2nd Half\\\"}]", isFileEnabled = "0", fileUnits = "0"),
        LeaveTypeDetailData( id = 350, name = "PERMISSION", leaveName = "PE", balance = 5.0, coffType = 0, leaveType = "H", durationAllow = "", isFileEnabled = "0", fileUnits = "0"),
    )
}



fun generateLeaveDateDetailsList(): List<LeaveDateListData>
{
    return listOf(
        LeaveDateListData(  leaveType = "1", weekName = "Friday", day  = "Fri 12-Apr-2024",date  = "2024-04-12T00:00:00", leaveName  = "null",preCount = "0",enableStatus  = "0",type  = "0",leaveCode  = "0",leaveBalance  = "0"),
        LeaveDateListData(  leaveType = "1", weekName = "Saturday", day  = "Sat 13-Apr-2024",date  = "2024-04-13T00:00:00", leaveName  = "null",preCount = "0",enableStatus  = "1",type  = "1",leaveCode  = "0",leaveBalance  = "0"),
        LeaveDateListData(  leaveType = "1", weekName = "Sunday", day  = "Sun 14-Apr-2024",date  = "2024-04-14T00:00:00", leaveName  = "null",preCount = "0",enableStatus  = "1",type  = "1",leaveCode  = "0",leaveBalance  = "0"),
    )
}
