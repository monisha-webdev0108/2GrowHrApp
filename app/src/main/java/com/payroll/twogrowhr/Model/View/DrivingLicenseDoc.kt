package com.payroll.twogrowhr.Model.View

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
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
import com.payroll.twogrowhr.Model.ResponseModel.DocumentDetailsData
import com.payroll.twogrowhr.Model.data.loadImages
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.MandatoryTextField
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.containsOnlySpecialCharacters
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.getFileName1
import com.payroll.twogrowhr.components.isFileSizeValid1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.DocumentViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DrivingLicenseDoc(navController: NavController, documentViewModel: DocumentViewModel, type: String) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Driving License",
            "My_document"
        ) },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { DrivingLicense_Screen(navController = navController, documentViewModel = documentViewModel, type = type)}
}
@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrivingLicense_Screen(navController: NavController, documentViewModel: DocumentViewModel, type: String) {

    var drivingLicenseNumber by remember { mutableStateOf("") }
    var drivingLicenseName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var dob by remember { mutableStateOf("") }
    var expiresOn by remember { mutableStateOf("") }
    var dateOfIssue by remember { mutableStateOf("") }
    var placeOfIssue by remember { mutableStateOf("") }
    var placeOfBirth by remember { mutableStateOf("") }
    var session by remember { mutableStateOf("") }

    var org by remember { mutableStateOf("") }
    var type1 by remember { mutableStateOf("") }
    var empId by remember { mutableStateOf("") }

    var uploadFileName by remember { mutableStateOf("") }

    var reloadFlag by remember { mutableIntStateOf(0) }

    var uploadFlag by remember { mutableIntStateOf(1) }

    var showDialog by remember { mutableStateOf(false) }


    val context = LocalContext.current
    LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

/*    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapResults ->
        mapResults.forEach {
            Log.d("Permissions", "Permission: ${it.key} Status: ${it.value}")
        }
        // check if any of the requested permissions is granted or not
        if (mapResults.values.all { it })
        {
            Constant.showToast(context, "Permission Granted")
        }
        else
        {
            Constant.showToast(context, "Permission Denied, Please give the permission to access..!")
        }
    }*/

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { mapResults ->
        mapResults.forEach {
            Log.d("Permissions", "Permission: ${it.key} Status: ${it.value}")
        }

        Log.d("Permissions", "Permissionssss: ${mapResults.values}")

        when
        {
            mapResults.values.all { it } -> { Constant.showToast(context, "Permission Granted") }
            mapResults.values.none { it } -> { Constant.showToast(context, "All Permissions Denied, Please give the permission to access..!") }
            else -> { Constant.showToast(context, "Permission Granted") }

        }

        showDialog = false

    }

    val documentDetailsList = documentViewModel.documentDetails.collectAsState().value

    val drivingLicenseData = documentDetailsList.firstOrNull()


    // Fetch and update the data list when entering the page

    var flag by remember { mutableIntStateOf(0) }

    //FOR GETTING RESULT

    val loadingStatus = documentViewModel.loadingStatus1

    flag = documentViewModel.flag1

    var loading by remember { mutableStateOf(true) }

    var selectedDate by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    //For Calendar

    val currentDate = remember { LocalDate.now() }

    var dobDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1)) }
    var expiryDate by remember { mutableStateOf(currentDate) }

    var selectedExpiryDate by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    var dateToDisplay by remember {
        mutableStateOf("") // or use mutableStateOf(calendar.timeInMillis)
    }

    var expiresOnToDisplay by remember {
        mutableStateOf("") // or use mutableStateOf(calendar.timeInMillis)
    }

    val calendar = Calendar.getInstance()
    calendar.set(1900, 0, 0) // add year, month (Jan), date



    var buttonText by remember { mutableStateOf("Save") }


    val dateAPIFormatter = SimpleDateFormat("yyyy-MM-dd")

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Assuming drivingLicenseData.dob is in "yyyy-MM-dd'T'HH:mm:ss" format for convert milli seconds
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()) //1999-01-21T00:00:00

    //For Expiry
    val dateFormatterExpiry = SimpleDateFormat("M/d/yyyy hh:mm:ss a", Locale.getDefault()) //5/22/2024 12:00:00 AM

    if (reloadFlag == 0 && drivingLicenseData != null)
    {

        drivingLicenseNumber = drivingLicenseData.accountNumber
        drivingLicenseName = drivingLicenseData.name
        address = drivingLicenseData.address
        fatherName = drivingLicenseData.fatherName
        filePath.value = drivingLicenseData.filePath
        fileName.value = drivingLicenseData.fileName

        dob = drivingLicenseData.dob
        expiresOn = drivingLicenseData.expiresDate
        dateOfIssue = drivingLicenseData.issuedDate
        placeOfIssue = drivingLicenseData.issuedPlace
        placeOfBirth = drivingLicenseData.birthPlace
        session = drivingLicenseData.session

        org = drivingLicenseData.org
        type1 = drivingLicenseData.type
        empId = drivingLicenseData.empID

        uploadFlag = if(drivingLicenseData.fileName.isEmpty() || drivingLicenseData.fileName.isBlank()) 1 else 0



        //Date of Birth

        val dob1 = dateFormatter.parse(drivingLicenseData.dob)

        val calendarDOB = Calendar.getInstance()

        dob1?.let {
            calendarDOB.time = it
            calendarDOB.add(Calendar.DAY_OF_YEAR, 1) // Add one day to the parsed date
        }

        selectedDate = calendarDOB.timeInMillis

        dateToDisplay = formatter.format(Date(dob1?.time!!))

        dobDate = LocalDate.ofEpochDay(dob1.time / (24 * 60 * 60 * 1000))

        //Expiry Date

        val doe = dateFormatterExpiry.parse(drivingLicenseData.expiresDate)

        val calendarDOE = Calendar.getInstance() // Date highlighted one day less than the selected day so add one more day for selectedExpiryDate

        doe?.let {
            calendarDOE.time = it
            calendarDOE.add(Calendar.DAY_OF_YEAR, 1) // Add one day to the parsed date
        }

        selectedExpiryDate = calendarDOE.timeInMillis

        expiresOnToDisplay = formatter.format(Date(doe?.time!!))

        expiryDate = LocalDate.ofEpochDay(doe.time / (24 * 60 * 60 * 1000))

        reloadFlag = 1

    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
    {

        var showDatePicker by remember {
            mutableStateOf(false)
        }

        var showExpiryDatePicker by remember {
            mutableStateOf(false)
        }


        //FOR RECEIVED LIST RESPONSE

        if(loading && !documentViewModel.loadingStatus1)
        {
            circularProgression()
        }

        if(statusLoading.value)
        {
            circularProgression1(statusLoading.value)
        }


        buttonText = if(documentDetailsList.isEmpty()) "Save" else "Update"

//LOGIC TO DISPLAY THE LIST

        @Suppress("DEPRECATION")
        @Composable
        fun uiUpdate()
        {


            //DOB

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate,

                selectableDates = object : SelectableDates {


                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        // Allow selecting dates from January 1, 1900, to the current day
                        val calendar1 = Calendar.getInstance()
                        calendar1.set(1900, Calendar.JANUARY, 0)
                        val minDate = calendar1.timeInMillis
                        val currentDate1 = System.currentTimeMillis()
                        return utcTimeMillis in minDate..currentDate1
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        // Allow selecting years from the current year onwards
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        return year <= currentYear
                    }
                }
            )

            // EXPIRY DATE

            val datePickerExpiryState = rememberDatePickerState(
                initialSelectedDateMillis = selectedExpiryDate,

                selectableDates = object : SelectableDates {


                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                        // Set the minimum date as the day after the dobDate
                        val minDate = dobDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

                        // Set the maximum date as December 31, 2100
                        val maxDate = LocalDate.of(2100, Month.DECEMBER, 31).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

                        // Check if the given utcTimeMillis falls within the range of minDate to maxDate
                        return utcTimeMillis in minDate..maxDate

                    }

                    override fun isSelectableYear(year: Int): Boolean {

                        // Get the maximum year supported by the calendar
                        val maxYear = LocalDate.of(2100, Month.DECEMBER, 31).year

                        // Allow selecting years from the year of the Date of Birth to the maximum year
                        return year in dobDate.year..maxYear
                    }
                }
            )








            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(0.85f)
            ) {

//DOB
                if  (showDatePicker) {

                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showDatePicker = false

                                Log.d("Inside if showDatePiker", "$selectedDate")

                                val selectedDateForValidate = LocalDate.ofEpochDay(datePickerState.selectedDateMillis!! / (24 * 60 * 60 * 1000))


                                if(expiresOnToDisplay.isEmpty())
                                {
                                    selectedDate = datePickerState.selectedDateMillis!!
                                    dateToDisplay = formatter.format(Date(selectedDate))
                                    dobDate = selectedDateForValidate
                                }
                                else
                                {
                                    if(selectedDateForValidate < expiryDate)
                                    {
                                        selectedDate = datePickerState.selectedDateMillis!!
                                        dateToDisplay = formatter.format(Date(selectedDate))
                                        dobDate = selectedDateForValidate
                                    }
                                    else
                                    {
                                        Constant.showToast(context, "Date of Birth should be less than the Expiry Date")
                                    }
                                }



                            }) {
                                Text(text = "Confirm",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    )
                                )
                            }
                        }
                    ) {
                        Constant.AppTheme {
                            DatePicker(state = datePickerState,
                                showModeToggle = false // Edit button for date entry
                            )
                        }
                    }
                }


                //EXPIRY DATE

                if  (showExpiryDatePicker) {

                    DatePickerDialog(
                        onDismissRequest = { showExpiryDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showExpiryDatePicker = false

                                Log.d("Inside if showExpiryDatePicker", "$selectedExpiryDate")

                                val expiryDateForValidate = LocalDate.ofEpochDay(datePickerExpiryState.selectedDateMillis!! / (24 * 60 * 60 * 1000))


                                if(dateToDisplay.isEmpty())
                                {
                                    selectedExpiryDate = datePickerExpiryState.selectedDateMillis!!
                                    expiresOnToDisplay = formatter.format(Date(selectedExpiryDate))
                                    expiryDate = expiryDateForValidate
                                }
                                else
                                {
                                    if(expiryDateForValidate > dobDate)
                                    {
                                        selectedExpiryDate = datePickerExpiryState.selectedDateMillis!!
                                        expiresOnToDisplay = formatter.format(Date(selectedExpiryDate))
                                        expiryDate = expiryDateForValidate
                                    }
                                    else
                                    {
                                        Constant.showToast(context, "Expiry Date should be greater than the Date of Birth")
                                    }
                                }



                            }) {
                                Text(text = "Confirm",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    )
                                )
                            }
                        }
                    ) {
                        Constant.AppTheme {
                            DatePicker(state = datePickerExpiryState,
                                showModeToggle = false // Edit button for date entry
                            )
                        }
                    }
                }


                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)) {


                    /*Account Number*/

                    MandatoryTextField(label = "License Number")

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = drivingLicenseNumber,
                            onValueChange = { newValue ->
                                val filteredValue = newValue.take(155).replace("\\s+".toRegex(), " ")
                                drivingLicenseNumber = filteredValue
                            },
                            placeholder = {
                                Text(
                                    text = "",
                                    color = colorResource(id = R.color.black),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            textStyle = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black) ), // Color of the entered text

                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.divider), // Change this to your desired border color
                                    shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                )
                        )

                    }

                    /*Account Holder Name*/

                    MandatoryTextField(label = "Name")


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(
                            value = drivingLicenseName,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.take(155).replace("\\s+".toRegex(), " ")
                                drivingLicenseName = filteredValue
                            },

                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),

                            textStyle = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black) ), // Color of the entered text

                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.divider), // Change this to your desired border color
                                    shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                )
                        )

                    }

                    /*Father Name*/

                    MandatoryTextField(label = "Father's Name")


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = fatherName,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.take(155).replace("\\s+".toRegex(), " ")
                                fatherName = filteredValue
                            },

                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = containerColor,
                                unfocusedContainerColor = containerColor,
                                disabledContainerColor = containerColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),

                            textStyle = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 13.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.black) ), // Color of the entered text

                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .border(
                                    width = 1.dp,
                                    color = colorResource(id = R.color.divider), // Change this to your desired border color
                                    shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                )
                        )

                    }

                    /*Date of Birth*/

                    MandatoryTextField(label = "Date of Birth")

                    Column()
                    {

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                readOnly = true,
                                value = dateToDisplay,
                                onValueChange = { dateToDisplay = it },

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),

                                textStyle = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black) ), // Color of the entered text

                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.divider), // Change this to your desired border color
                                        shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                    )
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = colorResource(id = R.color.themeColor),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .size(56.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.attendance),
                                        contentDescription = "calender",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable { showDatePicker = true  }
                                    )
                                }
                            }
                        }

                    }


                    /*Expires On*/

                    MandatoryTextField(label = "Expires On")

                    Column()
                    {

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                readOnly = true,
                                value = expiresOnToDisplay,
                                onValueChange = { expiresOnToDisplay = it },

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),

                                textStyle = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black) ), // Color of the entered text

                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.divider), // Change this to your desired border color
                                        shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                    )
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = colorResource(id = R.color.themeColor),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .size(56.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.attendance),
                                        contentDescription = "calender",
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable { showExpiryDatePicker = true }
                                    )
                                }
                            }
                        }

                    }


                    val contentResolver = LocalContext.current.contentResolver  // Obtain the ContentResolver using LocalContext


                    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                        if (uri != null) {

                            val mimeType = contentResolver.getType(uri)
                            if (mimeType != null && ((mimeType.startsWith("image/jpeg") || mimeType == "image/jpeg") ||  mimeType == "image/jpg" || mimeType == "application/pdf") || mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || mimeType == "application/msword")
                            {

                                // Check if the selected file size is valid
                                if (isFileSizeValid1(context, uri))
                                {

                                    fileUri.value = uri // Handle the returned URI
                                    fileName.value = getFileName1(uri,context)
                                    filePath.value = fileName.value
                                    uploadFileName = getFileName1(uri,context)

                                    Log.d("Driving License","File size is valid. Proceed with processing.")
                                }
                                else
                                {
                                    Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                    Log.d("Driving License","File size exceeds the maximum allowed size.")
                                }

                            }
                            else
                            {
                                Constant.showToast(context,"Please select an jpeg, jpg, pdf or word file")
                            }

                        }

                    }




                    @RequiresApi(34)
                    @RequiresPermission(anyOf = [READ_MEDIA_IMAGES, READ_EXTERNAL_STORAGE])
                    @Composable
                    fun MediaStoreQueryDialog( onDismiss: () -> Unit, onConfirm: (Uri, String) -> Unit) {

                        val files by loadImages(context.contentResolver)

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
                                                        val mimeType = contentResolver.getType(file.uri)
                                                        if (mimeType != null && ((mimeType.startsWith("image/jpeg") || mimeType == "image/jpeg") ||  mimeType == "image/jpg" || mimeType == "application/pdf") || mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" || mimeType == "application/msword")
                                                        {


                                                            if (isFileSizeValid1(context, file.uri))
                                                            {
                                                                onConfirm(file.uri, getFileName(file.uri, context))
                                                                Log.d("Driving License","File size is valid. Proceed with processing.")
                                                            }
                                                            else
                                                            {
                                                                Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                                                Log.d("Driving License","File size exceeds the maximum allowed size.")
                                                            }

                                                        }
                                                        else
                                                        {
                                                            Constant.showToast(context,"Please select an jpeg, jpg, pdf or word file")
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
                            onConfirm = { uri, fileName1 ->

                                fileUri.value = uri // Handle the returned URI
                                fileName.value = fileName1
                                filePath.value = fileName.value
                                uploadFileName = fileName1

                                showDialog = false
                            }
                        )
                    }

















                    //UPLOAD BUTTON
                    @Composable
                    fun uploadButton()
                    {


                        Button(
                            onClick = {

                                filePathClearance1()

                                val granted = checkAndRequestStoragePermissions(
                                    context = context,
                                    launcher = launcherMultiplePermissions
                                )

                                Log.d("granted value", "$granted")

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE  )
                                {
                                    if(ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
                                    {
                                        getContent.launch("*/*")
                                    }
                                    else showDialog = ContextCompat.checkSelfPermission(context, READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED

                                }
                                else
                                {
                                    getContent.launch("*/*")
                                }

                            },
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_green)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                            border = BorderStroke(1.dp, colorResource(id = R.color.green))
                        )
                        {
                            Row {
                                Icon(
                                    painterResource(id = R.drawable.singleplus),
                                    contentDescription = "calender",
                                    tint = colorResource(id = R.color.green),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 5.dp, end = 5.dp)
                                )
                                Text(
                                    text = "Upload document",
                                    color = colorResource(id = R.color.green),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                        }


                        Row(modifier = Modifier.padding(top = 10.dp))
                        {

                            Column()
                            {
                                PlainTooltipBox(
                                    tooltip = {
                                        Text(
                                            text = "Max File size not exceeding 2MB",
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
                                    text = "File Should be in jpeg, jpg, pdf, word Format.",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black) ),
                                )
                            }

                        }
                    }




                    if(documentDetailsList.isEmpty())
                    {
                        if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                        {
                            uploadFile(context = context, uploadFileName = uploadFileName)
                        }
                        else
                        {
                            uploadButton()
                        }
                    }
                    else
                    {
                        if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                        {
                            if(uploadFlag == 1)
                            {
                                uploadFile(context = context, uploadFileName = uploadFileName)
                            }
                            else
                            {
                                uploadedFile(context = context, navController = navController)
                            }
                        }
                        else
                        {
                            uploadButton()
                        }
                    }


                }
            }

            Box(modifier = Modifier
                .padding(top = 5.dp)
                .weight(0.13f)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(0),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                        Button(
                            onClick = {

                                Log.d("Driving License details", "Driving License_Number/Name/Address/FatherName : $drivingLicenseNumber / $drivingLicenseName / $address / $fatherName")

                                if(drivingLicenseNumber.isEmpty() || drivingLicenseNumber.isBlank())
                                {
                                    Constant.showToast(context, "Please enter the License number")
                                }
                                else if(containsOnlySpecialCharacters(drivingLicenseNumber))
                                {
                                    drivingLicenseNumber = ""
                                    Constant.showToast(context, "Please enter the valid License number")
                                }
                                else if(drivingLicenseName.isEmpty() || drivingLicenseName.isBlank())
                                {
                                    Constant.showToast(context, "Please enter the Name")
                                }
                                else if(!drivingLicenseName.all { it.isLetter() || it.isWhitespace() } || containsOnlySpecialCharacters(drivingLicenseName))
                                {
                                    drivingLicenseName = ""
                                    Constant.showToast(context, "Please enter the valid Name")
                                }
                                else if(fatherName.isEmpty() || fatherName.isBlank())
                                {
                                    Constant.showToast(context, "Please enter the Father Name")
                                }
                                else if(!fatherName.all { it.isLetter() || it.isWhitespace() } || containsOnlySpecialCharacters(fatherName))
                                {
                                    fatherName = ""
                                    Constant.showToast(context, "Please enter the valid Father name")
                                }
                                else if(dateToDisplay.isEmpty() || dateToDisplay.isBlank())
                                {
                                    Constant.showToast(context, "Please select the Date of Birth")
                                }
                                else if(expiresOnToDisplay.isEmpty() || expiresOnToDisplay.isBlank())
                                {
                                    Constant.showToast(context, "Please select the Expiry Date")
                                }
                                else if(fileName.value.isEmpty())
                                {
                                    Constant.showToast(context, "Please select the document to upload")
                                }
                                else {
                                    Log.d("Driving License details", "Driving License details submitted : Driving License _Number/Name/Address/FatherName/FileName : $drivingLicenseNumber / $drivingLicenseName / $address / $fatherName / ${fileName.value}")

                                    dob = dateAPIFormatter.format(Date(selectedDate)).toString()
                                    expiresOn = dateAPIFormatter.format(Date(selectedExpiryDate)).toString()

                                    statusLoading.value = true

                                    if(uploadFlag == 0)
                                    {
                                        coroutineScope.launch {
                                            documentViewModel.postDocumentDetails(navController, context, drivingLicenseNumber, drivingLicenseName, fatherName, dob, address, expiresOn, dateOfIssue, placeOfIssue, placeOfBirth, session, org, fileName.value, type1, empId).let { result ->

                                                when (result) {
                                                    is DocumentViewModel.PostDocumentResult.Success -> {
                                                        Constant.showToast(context, "Submitted Successfully")
                                                        navController.navigate("My_document")
                                                    }

                                                    is DocumentViewModel.PostDocumentResult.Failure -> {
                                                        Constant.showToast(context, "Please try again later.....")
                                                        navController.navigate("My_document")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        empId = userViewModel.getSFCode()
                                        type1 = type
                                        org = userViewModel.getOrg().toString()
                                        session = userViewModel.getOrg().toString()
                                        documentViewModel.uploadFile(context, fileUri.value!!, navController, drivingLicenseNumber, drivingLicenseName, fatherName, dob, address, expiresOn, dateOfIssue, placeOfIssue, placeOfBirth, session, org, filePath.value, type1, empId)
                                    }

                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = buttonText,
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.titleMedium
                            )
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

            if(documentDetailsList.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        documentViewModel.getDocumentDataDetails(navController, context, userViewModel.getSFCode(), type1)
                    }
                    2 -> {
                        uiUpdate()
                    }
                    3 -> {
                        uiUpdate()
                        Constant.showToast(context,"Something went wrong...!")

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
                        uiUpdate()
                    }

                    3 -> {
                        uiUpdate()
                        Constant.showToast(context,"Something went wrong...!")
                    }
                    else -> {
                        Constant.showToast(context,"Please try again later...!")
                    }
                }
            }


        }
    }

}








@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiUpdateDrivingEntryPreview()
{

    val context = LocalContext.current
    val navController = rememberNavController()
    val documentDetailsList= generateDrivingLicenseListData()
    val uploadFlag = 1
    val flag = 1
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    val drivingLicenseNumber = documentDetailsList.firstOrNull()?.accountNumber
    val drivingLicenseName = documentDetailsList.firstOrNull()?.name
    val fatherName = documentDetailsList.firstOrNull()?.fatherName
    val dateToDisplay = documentDetailsList.firstOrNull()?.dob
    val expiresOnToDisplay = documentDetailsList.firstOrNull()?.expiresDate
    val fileNamePreview = documentDetailsList.firstOrNull()?.fileName
    val uploadFileName = documentDetailsList.firstOrNull()?.fileName

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Driving License", "My_document") },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
        {


            buttonText = if(documentDetailsList.isEmpty()) "Save" else "Update"

//LOGIC TO DISPLAY THE LIST

            @Suppress("DEPRECATION")
            @Composable
            fun uiUpdate()
            {

                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .weight(0.85f)
                ) {

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp)) {


                        /*Account Number*/

                        MandatoryTextField(label = "License Number")

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(

                                value = drivingLicenseNumber!!,
                                onValueChange = { },
                                placeholder = {
                                    Text(
                                        text = "",
                                        color = colorResource(id = R.color.black),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                textStyle = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black) ), // Color of the entered text

                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.divider), // Change this to your desired border color
                                        shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                    )
                            )

                        }

                        /*Account Holder Name*/

                        MandatoryTextField(label = "Name")


                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                value = drivingLicenseName!!,
                                onValueChange = {},

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),

                                textStyle = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black) ), // Color of the entered text

                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.divider), // Change this to your desired border color
                                        shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                    )
                            )

                        }

                        /*Father Name*/

                        MandatoryTextField(label = "Father's Name")


                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(

                                value = fatherName!!,
                                onValueChange = {},

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),

                                textStyle = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.black) ), // Color of the entered text

                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .border(
                                        width = 1.dp,
                                        color = colorResource(id = R.color.divider), // Change this to your desired border color
                                        shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                    )
                            )

                        }

                        /*Date of Birth*/

                        MandatoryTextField(label = "Date of Birth")

                        Column()
                        {

                            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = dateToDisplay!!,
                                    onValueChange = {  },

                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = containerColor,
                                        unfocusedContainerColor = containerColor,
                                        disabledContainerColor = containerColor,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),

                                    textStyle = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black) ), // Color of the entered text

                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .border(
                                            width = 1.dp,
                                            color = colorResource(id = R.color.divider), // Change this to your desired border color
                                            shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                        )
                                )

                                Column(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier.background(color = colorResource(id = R.color.themeColor), shape = RoundedCornerShape(5.dp)).size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.attendance),
                                            contentDescription = "calender",
                                            tint = colorResource(id = R.color.white),
                                            modifier = Modifier.size(22.dp).clickable {   }
                                        )
                                    }
                                }
                            }

                        }


                        /*Expires On*/

                        MandatoryTextField(label = "Expires On")

                        Column()
                        {

                            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = expiresOnToDisplay!!,
                                    onValueChange = { },

                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = containerColor,
                                        unfocusedContainerColor = containerColor,
                                        disabledContainerColor = containerColor,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                    ),

                                    textStyle = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black) ), // Color of the entered text

                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .border(
                                            width = 1.dp,
                                            color = colorResource(id = R.color.divider), // Change this to your desired border color
                                            shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                        )
                                )

                                Column(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = colorResource(id = R.color.themeColor),
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.attendance),
                                            contentDescription = "calender",
                                            tint = colorResource(id = R.color.white),
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clickable {  }
                                        )
                                    }
                                }
                            }

                        }

                        //UPLOAD BUTTON
                        @Composable
                        fun uploadButton()
                        {


                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_green)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(top = 15.dp, bottom = 15.dp),
                                border = BorderStroke(1.dp, colorResource(id = R.color.green))
                            )
                            {
                                Row {
                                    Icon(
                                        painterResource(id = R.drawable.singleplus),
                                        contentDescription = "calender",
                                        tint = colorResource(id = R.color.green),
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(top = 5.dp, end = 5.dp)
                                    )
                                    Text(
                                        text = "Upload document",
                                        color = colorResource(id = R.color.green),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                            }


                            Row(modifier = Modifier.padding(top = 10.dp))
                            {

                                Column()
                                {
                                    PlainTooltipBox(
                                        tooltip = {
                                            Text(
                                                text = "Max File size not exceeding 2MB",
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
                                        text = "File Should be in jpeg, jpg, pdf, word Format.",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black) ),
                                    )
                                }

                            }
                        }




                        if(documentDetailsList.isEmpty())
                        {
                            if(fileNamePreview!!.isNotBlank() && fileNamePreview.isNotEmpty())
                            {
                                uploadFile(context = context, uploadFileName = uploadFileName!!)
                            }
                            else
                            {
                                uploadButton()
                            }
                        }
                        else
                        {
                            if(fileNamePreview!!.isNotBlank() && fileNamePreview!!.isNotEmpty())
                            {
                                if(uploadFlag == 1)
                                {
                                    uploadFile(context = context, uploadFileName = uploadFileName!!)
                                }
                                else
                                {
                                    uploadedFile(context = context, navController = navController)
                                }
                            }
                            else
                            {
                                uploadButton()
                            }
                        }


                    }
                }

                Box(modifier = Modifier
                    .padding(top = 5.dp)
                    .weight(0.13f)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(0),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                            Button(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(vertical = 15.dp)
                            ) {
                                Text(
                                    text = buttonText,
                                    color = colorResource(id = R.color.white),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

            }


// LOGIC TO DISPLAY THE UI


            when (flag)
            {
                0 -> {
                    uiUpdate()
                }
                1 -> {
                    uiUpdate()
                }
                2 -> {
                    uiUpdate()
                }

                3 -> {
                    uiUpdate()
                    Constant.showToast(context,"Something went wrong...!")
                }
                else -> {
                    Constant.showToast(context,"Please try again later...!")
                }
            }

        }

    }



}


fun generateDrivingLicenseListData(): List<DocumentDetailsData>
{
    return listOf(
        DocumentDetailsData(empID = "EMP251", type = "DE", accountNumber = "1234ABED", name = "Matheesha", fatherName = "Gautam Gambir", dob = "12/10/2001", address = "ABC Road", expiresDate = "12/10/2028", issuedDate = "12/10/2023", issuedPlace = "Chennai", birthPlace = "Madurai", org = "113", session = "113", filePath = "String", fileName = "License.jpg"),
    )
}
/*
DocumentDetailsData(empID = "EMP251", type = "PC", accountNumber = "123456789123", name = "Matheesha", fatherName = "Gautam Gambir", dob = "12/10/2001", address = "ABC Road", expiresDate = "12/10/2028", issuedDate = "12/10/2023", issuedPlace = "Chennai", birthPlace = "Madurai", org = "113", session = "113", filePath = "String", fileName = "PanCard.jpg"),
DocumentDetailsData(empID = "EMP251", type = "VI", accountNumber = "123456789123", name = "Matheesha", fatherName = "Gautam Gambir", dob = "12/10/2001", address = "ABC Road", expiresDate = "12/10/2028", issuedDate = "12/10/2023", issuedPlace = "Chennai", birthPlace = "Madurai", org = "113", session = "113", filePath = "String", fileName = "VoterId.jpg"),
DocumentDetailsData(empID = "EMP251", type = "PP", accountNumber = "123456789123", name = "Matheesha", fatherName = "Gautam Gambir", dob = "12/10/2001", address = "ABC Road", expiresDate = "12/10/2028", issuedDate = "12/10/2023", issuedPlace = "Chennai", birthPlace = "Madurai", org = "113", session = "113", filePath = "String", fileName = "VoterId.jpg"),
*/
