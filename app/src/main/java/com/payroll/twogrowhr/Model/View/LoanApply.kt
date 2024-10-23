package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedButton
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.MandatoryTextField
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoanApply(navController: NavController, loanViewModel: LoanDetailViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Apply Loan", "Finance") },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { LoanApply_Screen(navController = navController, loanViewModel = loanViewModel)}
}



@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun LoanApply_Screen(navController: NavController, loanViewModel: LoanDetailViewModel)
{

    //Loan Rule Details and Value to be save

    var loanName by remember { mutableStateOf("--Select Loan--") }
    var loanType by remember { mutableStateOf("") }
    var nameInPayslip by remember { mutableStateOf("") }
    var interestRateType by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }

    var numberOfInstalments by remember { mutableStateOf("") }

    var loanAmount by remember { mutableStateOf("") }
    val repayableAmount by remember { mutableStateOf("") }

    //Loan Eligibility API Data

    val formatterDOJ = DateTimeFormatter.ofPattern("MM-dd-yyyy")

    var doj by remember { mutableStateOf("") }
    var dojDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1))}
    var salaryAmount by remember { mutableStateOf("") }


    val empID = userViewModel.getSFCode()
    val org = userViewModel.getOrg()

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var deductionMonth by remember {
        mutableStateOf("") // or use mutableStateOf(calendar.timeInMillis)
    }

    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val loanTypeList = loanViewModel.loanTypeList.collectAsState()

    val loanEligibilityDetail = loanViewModel.loanEligibilityDetailData.collectAsState()

    val loanEligibility = loanEligibilityDetail.value.firstOrNull()



    if(loanEligibility != null)
    {
        doj = loanEligibility.doj
        salaryAmount = loanEligibility.salary

        dojDate = LocalDate.parse(doj, formatterDOJ)
    }


    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var expanded by remember { mutableStateOf(false) }
    var selectedDeductionType by remember { mutableStateOf("--Select--") }

    var month by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    var selectedDate by remember { mutableStateOf(LocalDate.of(year, month, 1))}


    var visible by remember { mutableStateOf(false) }

    fun getMonth(month : Int) : String
    {
        val monthDisplay1 =  when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> throw IllegalArgumentException("Invalid month number: $month")
        }
        return  monthDisplay1
    }

    var monthDisplay by remember { mutableStateOf("") }
    var yearDisplay by remember { mutableStateOf("") }


    MonthPickerNormal(
        visible = visible,
        currentMonth = month,
        currentYear = year,
        confirmButtonCLicked = { month_, year_ ->

            month = month_
            year = year_

            Log.d("MonthPicker","selectedDate before set : $selectedDate ")

            Log.d("MonthPicker","month/year : ${month}/${year} ")

            selectedDate = LocalDate.of(year, month, 1)

            monthDisplay = getMonth(month)
            yearDisplay = year.toString()

            deductionMonth = "$monthDisplay $yearDisplay"


            Log.d("MonthPicker","selectedDate After set: $selectedDate ")

            if(selectedDate.isAfter(dojDate))
            {
                Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
                Log.d("MonthPicker","selectedDate isAfter dojDate")
            }
            else
            {
                Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
            }


            Log.d("MonthPicker","month/year : ${month}/${year} ")
            Log.d("MonthPicker","month displayed : $monthDisplay ")

            visible = false


        },
        cancelClicked = {
            visible = false
        }
    )



    ModalBottomSheetLayout(
        sheetContent = {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(colorResource(id = R.color.white))
            )
            {



                var flag by remember { mutableIntStateOf(0) }

                val loadingStatus = loanViewModel.loadingStatus1

                flag = loanViewModel.flag1

                var loading by remember { mutableStateOf(false) }

                if(loading && !loanViewModel.loadingStatus)
                {
                    linearProgression()
                }


                Column(Modifier.fillMaxSize())
                {
                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    {
                        Text(
                            text = "Select Loan Type",
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.themeColor),
                            modifier = Modifier.padding(10.dp)
                        )
                        HorizontalDivider(color = colorResource(id = R.color.divider))
                    }

                    @Composable
                    fun uiUpdate()
                    {
                        LazyColumn {
                            items(loanTypeList.value) { data ->
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp)
                                        .clickable {

                                            statusLoading.value = true

                                            loanViewModel.getLoanEligibilityDetails(
                                                navController = navController,
                                                context = context,
                                                empId = empID,
                                                ruleID = data.slNo.toString()
                                            ) { count ->

                                                if (count == "0") {
                                                    coroutineScope.launch {
                                                        if (modalBottomSheetState.isVisible) {
                                                            modalBottomSheetState.hide()
                                                        } else {
                                                            modalBottomSheetState.show()
                                                        }
                                                    }

                                                    loanName = data.ruleName
                                                    loanType = data.slNo.toString()

//                                                    reloadFlag = 0 //to set 0 again when change the rule to get the value of DOJ

                                                    interestRate = data.interestRate.toString()
                                                    interestRateType =
                                                        data.interestRateType.toString()

                                                    nameInPayslip = data.ruleName
                                                } else {
                                                    coroutineScope.launch {
                                                        // Display toast message on the main UI thread
                                                        Constant.showToast(context, "You are not eligible for this loan")
                                                    }
                                                }

                                            }

                                        },
                                ) {
                                    Row(modifier = Modifier.padding(15.dp)) {
                                        Column {
                                            Text(
                                                text = data.ruleName,
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


                    @Composable
                    fun noDataToShow()
                    {
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

                    @Composable
                    fun ExceptionToShow()
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = "Something went wrong..!",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor)
                                )
                            )
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

                        if(loanTypeList.value.isEmpty())
                        {
                            when (flag)
                            {
                                0 -> {
                                    loading = true
                                }
                                1 -> {
                                    loanViewModel.getLoanTypeDetails(navController, context, empID, org.toString())
                                }
                                2 -> {
                                    noDataToShow()
                                }
                                3 -> {
                                    ExceptionToShow()
                                }
                                else -> {
                                    coroutineScope.launch {
                                        Constant.showToast(context, "Please try again later...!")
                                    }
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
                                    noDataToShow()
                                }
                                3 -> {
                                    ExceptionToShow()
                                }
                                else -> {
                                    coroutineScope.launch {
                                        Constant.showToast(context, "Please try again later...!")
                                    }
                                }
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


                Column(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp)) {

                    //LOAN TYPE

                    MandatoryTextField(label = "Loan Type")

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
                        shape = RoundedCornerShape(5.dp)
                    )
                    {

                        Column(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(), verticalArrangement = Arrangement.Center) {
                            Row {

                                Text(
                                    text = loanName,
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

                    //NAME IN PAYSLIP

                    Text(
                        text = "Name in Payslip", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.backgroundColor)
                        TextField(

                            value = nameInPayslip,
                            onValueChange = { },

                            placeholder = {
                                Text(
                                    text = "Name shown in payslip",
                                    color = colorResource(id = R.color.paraColor),
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
                                ),

                            enabled = false // Make the TextField non-editable
                        )
                    }


                    //AMOUNT

                    MandatoryTextField(label = "Enter Amount")


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)

                        TextField(

                            value = loanAmount,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.filter { it.isDigit() }
                                loanAmount = filteredValue
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

                    val deductionTypeList: List<String> =
                        listOf(
                            "Monthly", //1
                            "Quarterly",//2
                            "Half-Yearly",//3
                            "Yearly"//4
                        )



                    val dropdownMenuColors = TextFieldDefaults.colors(
                        cursorColor = Color.White, // Change to your desired color
                        focusedIndicatorColor = Color.Transparent, // Change to your desired color
                        unfocusedIndicatorColor = Color.Transparent, // Change to your desired color
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                    //DEDUCTION TYPE

                    MandatoryTextField(label = "Deduction Type")

                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 1.dp, end = 1.dp, bottom = 10.dp),
                        border = BorderStroke(1.dp, colorResource(id = R.color.divider))
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
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth(), // Set the background color of the dropdown menu
                                ) {
                                    TextField(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .menuAnchor()
                                            .background(Color.White), // menuAnchor modifier must be passed to the text field for correctness.
                                        readOnly = true,
                                        value = selectedDeductionType,
                                        singleLine = true,
                                        onValueChange = {},
                                        textStyle = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black) ), // Color of the entered text
                                        trailingIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                                contentDescription = "Down arrow",
                                                modifier = Modifier.clickable {
                                                    expanded = !expanded
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
                                        deductionTypeList.forEach { selectionOption ->
                                            androidx.compose.material3.DropdownMenuItem(
                                                text = { Text(text = selectionOption,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)
                                                    )) },

                                                onClick = {
                                                    selectedDeductionType = selectionOption
                                                    expanded = false
                                                },
                                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                                modifier = Modifier
                                                    .background(Color.White)
                                                    .fillMaxWidth(), // Set the background color of the dropdown menu
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }




                    //Start Deduction Month

                    MandatoryTextField(label = "Start Deduction Month")

                    Column()
                    {

                        Box(modifier = Modifier.padding(bottom = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)
                            TextField(
                                readOnly = true,
                                value = deductionMonth,
                                onValueChange = { deductionMonth = it },

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
                                modifier = Modifier.fillMaxWidth(1f).clickable { visible = true },
                                horizontalAlignment = Alignment.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clickable { visible = true }
                                        .background(color = colorResource(id = R.color.themeColor), shape = RoundedCornerShape(5.dp))
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
                                                visible = true
                                            }
                                    )
                                }
                            }
                        }

                    }





                    //No. Of Months

                    MandatoryTextField(label = "Number of Installments")


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.white)

                        TextField(

                            value = numberOfInstalments,
                            onValueChange = {newValue ->
                                val filteredValue = newValue.filter { it.isDigit() }.take(3)
                                numberOfInstalments = filteredValue
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


                    //Interest Rate

                    Text(
                        text = "Interest Rate", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.backgroundColor)

                        TextField(

                            value = interestRate,
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
                                ),
                            enabled = false
                        )

                    }




/*
                    //TOTAL REPAYABLE AMOUNT

                    Text(
                        text = "Total Repayable Amount", style = MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )


                    Box(modifier = Modifier.padding(bottom = 10.dp)) {
                        val containerColor = colorResource(id = R.color.backgroundColor)
                        TextField(

                            value = repayableAmount,
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
                                ),
                            enabled = false
                        )

                    }

                    */

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

                                Log.d("Loan details", "Loan Apply details check : Loan Type / Loan Name / NameInPayslip / amount / Deduction Month / Number of Months / Rate of Interest : $loanType / $loanName / $nameInPayslip / $loanAmount / $deductionMonth / $numberOfInstalments / $interestRate")


                                if(selectedDate.isAfter(dojDate))
                                {
                                    Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
                                    Log.d("MonthPicker","selectedDate isAfter dojDate")
                                }
                                else
                                {
                                    Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
                                }

                                if(loanName ==  "--Select Loan--")
                                {
                                    Constant.showToast(context, "Please select the Loan Type")
                                }
                                else if(loanAmount.isEmpty() || loanAmount.isBlank() || loanAmount.toInt() == 0 || loanAmount.toDouble() == 0.0)
                                {
                                    loanAmount = ""
                                    Constant.showToast(context, "Please enter the Amount")
                                }
                                else if(selectedDeductionType == "--Select--")
                                {
                                    Constant.showToast(context, "Please enter the Deduction Type")
                                }
                                else if(deductionMonth.isEmpty() || deductionMonth.isBlank())
                                {
                                    Constant.showToast(context, "Please select the Deduction start month")
                                }
                                else if(!selectedDate.isAfter(dojDate))
                                {
                                    Constant.showToast(context, "Selected Date should be greater than DOJ..!")
                                }
                                else if(numberOfInstalments.isEmpty() || numberOfInstalments.isBlank() || numberOfInstalments.toInt() == 0 )
                                {
                                    numberOfInstalments = ""
                                    Constant.showToast(context, "Please enter the Number of Installments")
                                }
                                else if(salaryAmount.isBlank() || salaryAmount.isEmpty())
                                {
                                    Constant.showToast(context, "Contact Admin... Salary is not fixed for you..")
                                }
                                else
                                {
                                    statusLoading.value = true

                                    val deductionType = when(selectedDeductionType){
                                        "Monthly" -> "1"
                                        "Quarterly" -> "2"
                                        "Half-Yearly" -> "3"
                                        "Yearly" -> "4"
                                        else -> ""
                                    }

                                    val formattedMonth = String.format("%02d", month)
                                    val startMonth = "$year-$formattedMonth-01"

                                    Log.d("Loan details", "Loan Apply details check : Loan Type / Loan Name / NameInPayslip / amount / Deduction Month / Number of Months / Rate of Interest / Rate of Interest Type : $loanType / $loanName / $nameInPayslip / $loanAmount / $startMonth / $numberOfInstalments / $interestRate / $interestRateType")

                                    navController.navigate("${Screen.LoanInstalmentDetails.route}/${loanName}/${nameInPayslip}/${loanType}/${deductionType}/${numberOfInstalments}/${startMonth}/${loanAmount}/${interestRate}/${interestRateType}/${salaryAmount}")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = "Check to Installment Amount",
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


/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthPickerNormal(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    confirmButtonCLicked: (Int, Int) -> Unit,
    cancelClicked: () -> Unit
) {

    val context = LocalContext.current

    val months = listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")

    val currentYearDialog = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonthDialog = Calendar.getInstance().get(Calendar.MONTH) + 1

    Log.d("MonthPicker","currentMonthDialog/currentYearDialog : $currentMonthDialog/$currentYearDialog")

    var month by remember {
        mutableStateOf(months[currentMonth - 1])
    }

    var year by remember {
        mutableStateOf(currentYear)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    if (visible) {

        AlertDialog(
            backgroundColor = Color.White,
            shape = RoundedCornerShape(10),
            title = {

            },
            text = {

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(

                            painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "back",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier.clickable {

                                try
                                {
                                    year--
                                }
                                catch (e :Exception)
                                {
                                    Log.d("Loan Apply...", "Apply Loan : exception : ${e.message}")
                                }
                            }

                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = year.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(

                            painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = "Forward",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier.clickable
                            {
                                try
                                {
                                    year++
                                }
                                catch (e: Exception)
                                {
                                    Log.d("Loan Apply...", "Apply Loan : exception : ${e.message}")
                                }
                            }

                        )

                    }

                    androidx.compose.material.Card(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth(),
                        elevation = 0.dp
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            mainAxisSpacing = 16.dp,
                            crossAxisSpacing = 16.dp,
                            mainAxisAlignment = MainAxisAlignment.Center,
                            crossAxisAlignment = FlowCrossAxisAlignment.Center
                        ) {
                            months.forEachIndexed { _, it ->

                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                month = it
                                            },
                                        )
                                        .background(
                                            color = if (month == it) colorResource(id = R.color.themeColor) else Color.Transparent,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val textColor = if (month == it) Color.White else Color.Black
                                    androidx.compose.material.Text(
                                        text = it,
                                        color = textColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }


                }

            },
            buttons = {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, bottom = 30.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            cancelClicked()
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = Color.Transparent),
                        colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        androidx.compose.material.Text(
                            text = "Cancel",
                            color = colorResource(id = R.color.black),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    OutlinedButton(
                        modifier = Modifier.padding(end = 20.dp),
                        onClick = {
                            confirmButtonCLicked(
                                months.indexOf(month) + 1,
                                year
                            )
                        },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, color = colorResource(id = R.color.themeColor)),
                        colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        androidx.compose.material.Text(
                            text = "OK",
                            color = colorResource(id = R.color.themeColor),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            },
            onDismissRequest = {

            }
        )

    }

}*/
