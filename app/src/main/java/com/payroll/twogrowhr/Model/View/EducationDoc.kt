@file:Suppress("DEPRECATION")

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries,UsingMaterialAndMaterial3Libraries
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.payroll.twogrowhr.Model.ResponseModel.DegreeDetailsData
import com.payroll.twogrowhr.Model.data.loadImages
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.MandatoryTextField
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.containsOnlySpecialCharacters
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.getFileName1
import com.payroll.twogrowhr.components.isFileSizeValid1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.components.viewSelectedFile
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.DocumentViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EducationDoc(navController: NavController, documentViewModel: DocumentViewModel, type : String) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Add Education Details",
            "${Screen.EducationDocList.route}/$type"
        ) },
        drawerContent = { },
        bottomBarContent = { },//BottomBar_edu()
        onBack = { navController.navigateUp() }
    )
    { Education_Screen(navController = navController, documentViewModel = documentViewModel, type = type)}
}
@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Education_Screen(navController: NavController, documentViewModel: DocumentViewModel, type : String) {

    var degreeName by remember { mutableStateOf("--Select Degree--") }
    var degreeType by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }


    var university by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    var uploadFileName by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current
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

    var showDialog by remember { mutableStateOf(false) }

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

        showDialog = false
    }


    val empId = userViewModel.getSFCode()


    var selectedDOJ by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    var selectedDOC by remember {
        mutableStateOf(System.currentTimeMillis()) // or use mutableStateOf(calendar.timeInMillis)
    }

    var dojToDisplay by remember {
        mutableStateOf("") // or use mutableStateOf(calendar.timeInMillis)
    }

    var docToDisplay by remember {
        mutableStateOf("") // or use mutableStateOf(calendar.timeInMillis)
    }

    val calendar = Calendar.getInstance()
    calendar.set(1900, 0, 0) // add year, month (Jan), date

    //For Calendar

    var dojDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1)) }
    var docDate by remember { mutableStateOf(dojDate.plusDays(1)) }

    //DATE OF JOINING

    val datePickerStateJoining = rememberDatePickerState(
        initialSelectedDateMillis = selectedDOJ,

        selectableDates = object : SelectableDates {


            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Allow selecting dates from January 1, 1900, to the current day
                val calendar1 = Calendar.getInstance()
                calendar1.set(1900, Calendar.JANUARY, 0)
                val minDate = calendar.timeInMillis
                val maxDate = if (docToDisplay.isEmpty())
                            {
                                LocalDate.of(2100, Month.DECEMBER, 31).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            }
                            else
                            {
                                docDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            }



                return utcTimeMillis in minDate..maxDate
            }

            override fun isSelectableYear(year: Int): Boolean {
                // Allow selecting years from the current year onwards

                val maxYear = if(docToDisplay.isEmpty())
                {
                    LocalDate.of(2100, Month.DECEMBER, 31).year
                }
                else
                {
                    Instant.ofEpochMilli(docDate.minusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .year
                }


                return year <= maxYear
            }
        }
    )

    // DATE OF COMPLETION

    val datePickerStateCompletion = rememberDatePickerState(
        initialSelectedDateMillis = selectedDOC,

        selectableDates = object : SelectableDates {


            override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                // Set the minimum date as the day after the dobDate
                val minDate = dojDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

                // Set the maximum date as December 31, 2100
                val maxDate = LocalDate.of(2100, Month.DECEMBER, 31).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

                // Check if the given utcTimeMillis falls within the range of minDate to maxDate
                return utcTimeMillis in minDate..maxDate

            }

            override fun isSelectableYear(year: Int): Boolean {

                // Get the maximum year supported by the calendar
                val maxYear = LocalDate.of(2100, Month.DECEMBER, 31).year

                // Allow selecting years from the year of the Date of Birth to the maximum year
                return year in dojDate.year..maxYear
            }
        }
    )


    val dateAPIFormatter = SimpleDateFormat("yyyy-MM-dd")

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val degreeDetails = documentViewModel.degreeDetails.collectAsState()

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
                            text = "Select Degree",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.themeColor),
                            modifier = Modifier.padding(10.dp)
                        )
                        HorizontalDivider(color = colorResource(id = R.color.divider))
                    }

                    LazyColumn {
                        items(degreeDetails.value) { data ->
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

                                        degreeName = data.qualification
                                        degreeType = data.degreeCode

                                    },
                            ) {
                                Row(modifier = Modifier.padding(15.dp)) {
                                    Column {
                                        Text(
                                            text = data.qualification,
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



        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
        {

            var showDOJDatePicker by remember {
                mutableStateOf(false)
            }

            var showDOCDatePicker by remember {
                mutableStateOf(false)
            }

            //FOR RECEIVED LIST RESPONSE

            if(statusLoading.value)
            {
                circularProgression1(statusLoading.value)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(0.85f)
            ) {


                if  (showDOJDatePicker) {

                    DatePickerDialog(
                        onDismissRequest = {
                            showDOJDatePicker = false
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDOJDatePicker = false

                                Log.d("Inside if showDatePiker", "$selectedDOJ")

                                val selectedDateForValidate = LocalDate.ofEpochDay(datePickerStateJoining.selectedDateMillis!! / (24 * 60 * 60 * 1000))


                                if(docToDisplay.isEmpty())
                                {
                                    selectedDOJ = datePickerStateJoining.selectedDateMillis!!
                                    dojToDisplay = formatter.format(Date(selectedDOJ))
                                    dojDate = selectedDateForValidate
                                }
                                else
                                {
                                    if(selectedDateForValidate < docDate)
                                    {
                                        selectedDOJ = datePickerStateJoining.selectedDateMillis!!
                                        dojToDisplay = formatter.format(Date(selectedDOJ))
                                        dojDate = selectedDateForValidate
                                    }
                                    else
                                    {
                                        Constant.showToast(context, "Date of Joining should be less than the Date of Completion")
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
                            DatePicker(state = datePickerStateJoining,
                                showModeToggle = false // Edit button for date entry
                            )
                        }
                    }
                }



                if  (showDOCDatePicker) {

                    DatePickerDialog(
                        onDismissRequest = {

                            showDOCDatePicker = false

                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDOCDatePicker = false


                                Log.d("Inside if showDateOfCompletionPicker", "$selectedDOC")

                                val expiryDateForValidate = LocalDate.ofEpochDay(datePickerStateCompletion.selectedDateMillis!! / (24 * 60 * 60 * 1000))


                                if(dojToDisplay.isEmpty())
                                {
                                    selectedDOC = datePickerStateCompletion.selectedDateMillis!!
                                    docToDisplay = formatter.format(Date(selectedDOC))
                                    docDate = expiryDateForValidate
                                }
                                else
                                {
                                    if(expiryDateForValidate > dojDate)
                                    {
                                        selectedDOC = datePickerStateCompletion.selectedDateMillis!!
                                        docToDisplay = formatter.format(Date(selectedDOC))
                                        docDate = expiryDateForValidate
                                    }
                                    else
                                    {
                                        Constant.showToast(context, "Date of Completion should be greater than the Date of Joining")
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
                            DatePicker(state = datePickerStateCompletion,
                                showModeToggle = false // Edit button for date entry
                            )
                        }
                    }
                }



                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)) {

                    //DEGREE

                    MandatoryTextField(label = "Degree")

                    // Creating a button that changes bottomSheetScaffoldState value upon click
                    // when bottomSheetScaffoldState is collapsed, it expands and vice-versa
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 2.dp, bottom = 5.dp)
                            .height(50.dp)
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
                        border = BorderStroke(1.dp, colorResource(id = R.color.divider)),
                        shape = RoundedCornerShape(5.dp))
                    {

                        Column(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                            Row {

                                Text(
                                    text = degreeName,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
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

                    //SPECIALIZATION


                    MandatoryTextField(label = "Specialization")

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = specialization,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                specialization = filteredValue
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


                    //UNIVERSITY

                    Text(
                        text = "University", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = university,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                university = filteredValue
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


                    //LOCATION

                    Text(
                        text = "Location", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)
                        TextField(

                            value = location,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                location = filteredValue
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


                    //Date of Joining

                    MandatoryTextField(label = "Date of Joining")


                    Column()
                    {

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                readOnly = true,
                                value = dojToDisplay,
                                onValueChange = { dojToDisplay = it },

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
                                            .clickable {

                                                Log.d("Education Details", "date : $dojToDisplay")
/*
                                                if(dojToDisplay.isEmpty() || dojToDisplay.isBlank())
                                                {
                                                    selectedDOJ = System.currentTimeMillis()
                                                    showDOJDatePicker = true
                                                }
                                                else
                                                {
                                                    val date = formatter.parse(dojToDisplay)

                                                    Log.d("Education Details", "Date Formatter : ${date?.time!!}")

                                                    selectedDOJ = date.time

                                                    Log.d("Education Details", "SelectedDate : $selectedDOJ")


                                                    showDOJDatePicker = true
                                                }*/

                                                showDOJDatePicker = true


                                            }
                                    )
                                }
                            }
                        }

                    }

                    //Date of Completion

                    MandatoryTextField(label = "Date of Completion")


                    Column()
                    {

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                readOnly = true,
                                value = docToDisplay,
                                onValueChange = { docToDisplay = it },

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
                                            .clickable {

                                                Log.d("Education Details", "date : $docToDisplay")
/*
                                                if(docToDisplay.isEmpty() || docToDisplay.isBlank())
                                                {
                                                    selectedDOC = System.currentTimeMillis()
                                                    showDOCDatePicker = true
                                                }
                                                else
                                                {
                                                    val date = formatter.parse(docToDisplay)

                                                    Log.d("Education Details", "Date Formatter : ${date?.time!!}")

                                                    selectedDOC = date.time

                                                    Log.d("Education Details", "SelectedDate  : $selectedDOC")


                                                    showDOCDatePicker = true
                                                }*/

                                                showDOCDatePicker = true

                                            }
                                    )
                                }
                            }
                        }

                    }


                    val contentResolver = androidx.compose.ui.platform.LocalContext.current.contentResolver             // Obtain the ContentResolver using LocalContext


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

                                    Log.d("Education Details","File size is valid. Proceed with processing.")
                                }
                                else
                                {
                                    Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                    Log.d("Education Details","File size exceeds the maximum allowed size.")
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
                                                Card(modifier = Modifier.padding(1.dp).align(Alignment.CenterHorizontally),
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
                                                                Log.d("Aadhaar","File size is valid. Proceed with processing.")
                                                            }
                                                            else
                                                            {
                                                                Constant.showToast(context, "File size exceeds the maximum allowed size.")
                                                                Log.d("Aadhaar","File size exceeds the maximum allowed size.")
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



                    /*Upload document*/

                    if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                    {
                        Divider(modifier = Modifier.padding( top = 10.dp,bottom = 10.dp),color = colorResource(id = R.color.lightshade))

                        Row(verticalAlignment = Alignment.CenterVertically){

                            if (uploadFileName.length > 20) {
                                PlainTooltipBox(
                                    tooltip = {
                                        Text(
                                            text = uploadFileName,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight(500),
                                            ),
                                        )
                                    }
                                ) {
                                    Text(
                                        text = uploadFileName.take(20) + "..." ,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)
                                        ),
                                        modifier = Modifier
                                            .tooltipTrigger()
                                            .clickable { viewSelectedFile(context, fileUri.value.toString())  }
                                    )
                                }
                            }
                            else
                            {
                                Text(
                                    text = uploadFileName,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.themeColor)
                                    ),
                                    modifier = Modifier.clickable { viewSelectedFile(context, fileUri.value.toString()) }
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))


                            Icon(painterResource(id = R.drawable.fi_rs_trash)  , contentDescription ="trash" ,tint = colorResource(id = R.color.red),
                                modifier = Modifier.clickable { filePathClearance1() }
                            )
                        }
                    }
                    else
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

                                Log.d("Education details", "Education details submitted : Degree Type / Degree Name / specialization / University / DOJ / DOC / FileName : $degreeType / $degreeName / $specialization / $university / $dojToDisplay / $docToDisplay / ${fileName.value}")

                                if(degreeName ==  "--Select Degree--")
                                {
                                    Constant.showToast(context, "Please select the Degree")
                                }
                                else if(specialization.isEmpty() || specialization.isBlank())
                                {
                                    Constant.showToast(context, "Please enter the specialization")
                                }
                                else if(!specialization.all { it.isLetter() || it.isWhitespace() } || containsOnlySpecialCharacters(specialization))
                                {
                                    specialization = ""
                                    Constant.showToast(context, "Please enter the valid specialization")
                                }
                                else if(dojToDisplay.isEmpty() || dojToDisplay.isBlank())
                                {
                                    Constant.showToast(context, "Please select the Date of Joining")
                                }
                                else if(docToDisplay.isEmpty() || docToDisplay.isBlank())
                                {
                                    Constant.showToast(context, "Please select the Date of Completion")
                                }
                                else if(fileName.value.isEmpty())
                                {
                                    Constant.showToast(context, "Please select the document to upload")
                                }
                                else {
                                    Log.d("Education details", "Education details submitted : Degree Type / Degree Name / specialization / University / DOJ / DOC / FileName : $degreeType / $degreeName / $specialization / $university / $dojToDisplay / $docToDisplay / ${fileName.value}")

                                    val formatForAPIDOJ = dateAPIFormatter.format(Date(selectedDOJ)).toString()
                                    val formatForAPIDOC = dateAPIFormatter.format(Date(selectedDOC)).toString()

                                    statusLoading.value = true

                                    documentViewModel.uploadFile1(context = context, fileUri = fileUri.value!!, navController = navController, degree_CompanyName = degreeType, experienceYears = "", qualification_DesignationName = specialization, location = location, doj = formatForAPIDOJ, doc_dor = formatForAPIDOC, universityName = university, documentName = "", filePath = fileName.value, type = type, empId = empId)

                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = "Save",
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




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiEducationDocumentDetailPreview()
{
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Add Education Details", "EducationDocList") },
        drawerContent = { },
        bottomBarContent = { },//BottomBar_edu()
        onBack = { navController.navigateUp() }
    )
    {

        var degreeName by remember { mutableStateOf("--Select Degree--") }
        var degreeType by remember { mutableStateOf("") }
        var specialization by remember { mutableStateOf("") }


        var university by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }

        val uploadFileName by remember { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

        var dojToDisplay by remember { mutableStateOf("") }

        var docToDisplay by remember { mutableStateOf("") }

        val calendar = Calendar.getInstance()
        calendar.set(1900, 0, 0) // add year, month (Jan), date

        //For Calendar

        val degreeDetails = generateEducationDetailsList()

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
                                text = "Select Degree",
                                style = MaterialTheme.typography.titleLarge,
                                color = colorResource(id = R.color.themeColor),
                                modifier = Modifier.padding(10.dp)
                            )
                            HorizontalDivider(color = colorResource(id = R.color.divider))
                        }

                        LazyColumn {
                            items(degreeDetails) { data ->
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

                                            degreeName = data.qualification
                                            degreeType = data.degreeCode

                                        },
                                ) {
                                    Row(modifier = Modifier.padding(15.dp)) {
                                        Column {
                                            Text(
                                                text = data.qualification,
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



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
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

                        //DEGREE

                        MandatoryTextField(label = "Degree")

                        // Creating a button that changes bottomSheetScaffoldState value upon click
                        // when bottomSheetScaffoldState is collapsed, it expands and vice-versa
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 2.dp, bottom = 5.dp)
                                .height(50.dp)
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
                            border = BorderStroke(1.dp, colorResource(id = R.color.divider)),
                            shape = RoundedCornerShape(5.dp))
                        {

                            Column(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                Row {

                                    Text(
                                        text = degreeName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
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

                        //SPECIALIZATION


                        MandatoryTextField(label = "Specialization")

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(

                                value = specialization,
                                onValueChange = {newValue ->
                                    val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                    specialization = filteredValue
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


                        //UNIVERSITY

                        Text(
                            text = "University", style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.paraColor),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(

                                value = university,
                                onValueChange = {newValue ->
                                    val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                    university = filteredValue
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


                        //LOCATION

                        Text(
                            text = "Location", style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.paraColor),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(

                                value = location,
                                onValueChange = {newValue ->
                                    val filteredValue = newValue.take(55).replace("\\s+".toRegex(), " ")
                                    location = filteredValue
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


                        //Date of Joining

                        MandatoryTextField(label = "Date of Joining")


                        Column()
                        {

                            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = dojToDisplay,
                                    onValueChange = { dojToDisplay = it },

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
                                                .clickable {   }
                                        )
                                    }
                                }
                            }

                        }

                        //Date of Completion

                        MandatoryTextField(label = "Date of Completion")


                        Column()
                        {

                            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = docToDisplay,
                                    onValueChange = { docToDisplay = it },

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
                                        )
                                    }
                                }
                            }

                        }

                        /*Upload document*/

                        if(fileName.value.isNotBlank() && fileName.value.isNotEmpty())
                        {
                            Divider(modifier = Modifier.padding( top = 10.dp,bottom = 10.dp),color = colorResource(id = R.color.lightshade))

                            Row(verticalAlignment = Alignment.CenterVertically){

                                if (uploadFileName.length > 20) {
                                    PlainTooltipBox(
                                        tooltip = {
                                            Text(
                                                text = uploadFileName,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight(500),
                                                ),
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = uploadFileName.take(20) + "..." ,
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.themeColor)
                                            ),
                                            modifier = Modifier.tooltipTrigger()
                                        )
                                    }
                                }
                                else
                                {
                                    Text(
                                        text = uploadFileName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)
                                        ),
                                        modifier = Modifier
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))


                                Icon(painterResource(id = R.drawable.fi_rs_trash)  , contentDescription ="trash" ,tint = colorResource(id = R.color.red),
                                    modifier = Modifier.clickable { filePathClearance1() }
                                )
                            }
                        }
                        else
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
                                onClick = {  },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(vertical = 15.dp)
                            ) {
                                Text(
                                    text = "Save",
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
}


fun generateEducationDetailsList(): List<DegreeDetailsData>
{
    return listOf(
        DegreeDetailsData( degreeCode = "1",  qualification = "BE / B.Tech"),
        DegreeDetailsData( degreeCode = "2",  qualification = "MBA / PGDM / MMS"),
        DegreeDetailsData( degreeCode = "3",  qualification = "BCA / BCM"),
        DegreeDetailsData( degreeCode = "4",  qualification = "B .Com"),
        DegreeDetailsData( degreeCode = "5",  qualification = "B.Sc"),
        DegreeDetailsData( degreeCode = "6",  qualification = "B.A."),
        DegreeDetailsData( degreeCode = "7",  qualification = "BBA / BBM / BBS"),
    )
}



