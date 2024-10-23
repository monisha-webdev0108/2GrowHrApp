package com.payroll.twogrowhr.Model.View

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.MandatoryTextField
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel
import com.payroll.twogrowhr.viewModel.LoanApprovalDataDetails
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditReporteeLoan(navController: NavController, loanViewModel: LoanDetailViewModel, approvalListViewModel: ApprovalListViewModel, loanData: LoanApprovalDataDetails)
{

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var reloadFlag by rememberSaveable { mutableIntStateOf(0) }


    //Employee Detail

    var empCode by remember { mutableStateOf("") }
    var divId by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var employeeDOJ by remember { mutableStateOf("") }
    var grossPay by remember { mutableStateOf("") }
    var profilePath by remember { mutableStateOf("") }


    val employeeProfileDetail = approvalListViewModel.employeeProfileDetailList.collectAsState()

    val profileDetail = employeeProfileDetail.value.firstOrNull()

    if(profileDetail != null)
    {
        empCode = profileDetail.empCode
        divId = profileDetail.divId.toString()
        designation = profileDetail.designation
        department = profileDetail.department
        employeeDOJ = profileDetail.doj.ifEmpty { "-" }
        grossPay = profileDetail.grossPay.toString()
        profilePath = profileDetail.filePath
    }

    //Loan Detail

    val loanName by remember { mutableStateOf(loanData.loanRuleName) }
    val loanTypeSlNo by remember { mutableStateOf(loanData.loanTypeSlNo) }
    val loanApplySlNo by remember { mutableStateOf(loanData.loanApplySlNo) }
    val nameInPayslip by remember { mutableStateOf(loanData.nameInPayslip) }
    val interestRateType by remember { mutableStateOf(loanData.interestRateType) }
    val interestRate by remember { mutableStateOf(loanData.interestRate) }
    var numberOfInstalments by rememberSaveable { mutableStateOf(loanData.numberOfMonths) }
    val loanRequestedAmount by remember { mutableStateOf(loanData.employeeRequestAmount) }
    var loanApprovedAmount by rememberSaveable { mutableStateOf(loanData.loanRequestAmount) }
    var loanSanctionDate by rememberSaveable { mutableStateOf(loanData.loanSanctionDate) }
    var loanDeductionType by rememberSaveable { mutableStateOf(loanData.deductionType) }

    var expanded by remember { mutableStateOf(false) }
    var expanded1 by remember { mutableStateOf(false) }

    var selectedDeductionType by rememberSaveable { mutableStateOf("") }

    selectedDeductionType = when(loanDeductionType){
        "1" -> "Monthly"
        "2" ->  "Quarterly"
        "3" -> "Half-Yearly"
        "4" -> "Yearly"
        else -> ""
    }


    var month by rememberSaveable { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var year by rememberSaveable { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.of(year, month, 1)) }

    var deductionMonth by rememberSaveable { mutableStateOf(loanData.deductionStartMonth) }
    var deductionStartDate by rememberSaveable { mutableStateOf(loanData.loanStartDate) }


    var selectedSanctionDate by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }

    var sanctionDateToDisplay by rememberSaveable { mutableStateOf("") }

// Define the date format
    val sanctionDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val sanctionDateDisplayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val sanctionDateDisplayFormat1 = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    sanctionDateToDisplay = if (loanSanctionDate.isNotEmpty()) {
        val date = sanctionDateFormat.parse(loanSanctionDate)
        sanctionDateDisplayFormat.format(date)
    } else {
        ""
    }

    selectedSanctionDate = if (loanSanctionDate.isNotEmpty())
    {
        val date = sanctionDateFormat.parse(loanSanctionDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 12)         // Set the time to noon (12:00 PM)
        calendar.timeInMillis
    }
    else
    {
        System.currentTimeMillis()
    }


    val formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
    val yearMonth = YearMonth.parse(deductionMonth, formatter)

    month = yearMonth.monthValue
    year = yearMonth.year

    val formattedMonth = String.format("%02d", month)
    val startMonth = "$year-$formattedMonth-01"

    var visible by rememberSaveable { mutableStateOf(false) }

    fun getMonth(month : Int) : String
    {
        val monthDisplay1 =  when (month) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> throw IllegalArgumentException("Invalid month number: $month")
        }
        return  monthDisplay1
    }

    var monthDisplay by rememberSaveable { mutableStateOf("") }
    var yearDisplay by rememberSaveable { mutableStateOf("") }

    //Loan Eligibility API Data

    val formatterDOJ = DateTimeFormatter.ofPattern("MM-dd-yyyy")
    val formatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    var doj by remember { mutableStateOf("") }
    var dojDate by remember { mutableStateOf(LocalDate.of(1900, 1, 1)) }
    var salaryAmount by remember { mutableStateOf("") }
    var dojFlag by remember { mutableIntStateOf(0) }


    //FOR ORIGINAL DATA DISPLAY PURPOSE

    val loanApprovalData = LoanApprovalDataDetails(employeeID = loanData.employeeID, employeeCode = loanData.employeeCode, employeeName = loanData.employeeName,loanTypeSlNo = loanData.loanTypeSlNo,
        loanApplySlNo = loanData.loanApplySlNo, loanRuleName = loanData.loanRuleName, loanStartDate = loanData.loanStartDate
        , loanRequestAmount = loanData.loanRequestAmount, loanPeriod = loanData.loanPeriod, remarks = loanData.remarks, instalmentAmount = loanData.instalmentAmount, nameInPayslip = loanData.nameInPayslip, employeeRequestAmount = loanData.loanRequestAmount,
        deductionType = loanData.deductionType, deductionStartMonth = loanData.deductionStartMonth, numberOfMonths = loanData.numberOfMonths, loanSanctionDate = loanData.loanSanctionDate, interestRate = loanData.interestRate, interestRateType = loanData.interestRateType)

    val loanDataJson = Gson().toJson(loanApprovalData).toString()

    val backUrl = "${Screen.LoanApprovalDetail.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"


    //FOR DISPLAY INSTALMENT FOR EDITED DATA

    var flagInstallment by remember { mutableIntStateOf(0) }

    flagInstallment = loanViewModel.flag4

    var loading1 by remember { mutableStateOf(false) }

    val loadingStatus1 = loanViewModel.loadingStatus4

    val loanInstallmentDataList = loanViewModel.installmentDetails.collectAsState()

    Log.d("qwerty", "${loanInstallmentDataList.value}")

    var totalRepayableAmount = 0.00
    var instalmentValue = 0.00


    val loanInstalmentItemsList = mutableListOf<LoanInstalmentItems>()

    loanInstallmentDataList.value.forEach { installmentData ->

        val loanInstalmentItem = LoanInstalmentItems(
            mon = installmentData.month,
            yr = installmentData.year.toString(),
            PAmt = installmentData.installmentAmount
        )
        loanInstalmentItemsList.add(loanInstalmentItem)

    }




    val loanEligibilityDetail = loanViewModel.loanEligibilityDetailData.collectAsState()

    val loanEligibility = loanEligibilityDetail.value.firstOrNull()

    if(loanEligibility != null)
    {
        doj = loanEligibility.doj
        salaryAmount = loanEligibility.salary

        if (!doj.isNullOrEmpty()) {
            dojDate = LocalDate.parse(doj, formatterDOJ)
            dojFlag = 1
        }
    }

    val url = "${Screen.LoanApprovalDetail.route}?res=${loanData.loanApplySlNo}&loanDataJson=${loanDataJson}"

    BackPressHandler(onBackPressed = {
        if(reloadFlag== 1)
        {
            reloadFlag = 0
        }
        else
        {
            navController.navigate(url)
        }
    })

    var showDialog by remember { mutableStateOf(false) }

    val deductionType = when(selectedDeductionType){
        "Monthly" -> "1"
        "Quarterly" -> "2"
        "Half-Yearly" -> "3"
        "Yearly" -> "4"
        else -> ""
    }


    MonthPickerNormal(
        visible = visible,
        currentMonth = month,
        currentYear = year,
        confirmButtonCLicked = { month_, year_ ->

            month = month_
            year = year_

            selectedDate = LocalDate.of(year, month, 1)

            monthDisplay = getMonth(month)
            yearDisplay = year.toString()

            deductionMonth = "$monthDisplay $yearDisplay"

            // For change the loan Start Date Format

            val inputFormatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val parsedYearMonth = YearMonth.parse(deductionMonth, inputFormatter)// Parse the input string to a YearMonth

            val firstDayOfMonth = parsedYearMonth.atDay(1)// Convert the YearMonth to the first day of the month as a LocalDate

            deductionStartDate = firstDayOfMonth.format(outputFormatter).toString()

            Log.d("MonthPicker","month/year : ${month}/${year} dojDate/selectedDate : $dojDate/$selectedDate ")
            Log.d("MonthPicker","month displayed : $monthDisplay ")

            visible = false


        },
        cancelClicked = {
            visible = false
        }
    )

    var showDatePicker by remember { mutableStateOf(false) }

    if(reloadFlag == 0)
    {


        AppScaffold1(
            topBarContent = { TopBarBackNavigation(navController = navController, title = "Edit Loan", url = backUrl) },
            bottomBarContent = { },
            onBack = { navController.navigateUp() }
        ){
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


                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedSanctionDate,

                    selectableDates = object : SelectableDates {


                        override fun isSelectableDate(utcTimeMillis: Long): Boolean {

                            val calendar1 = Calendar.getInstance()
                            calendar1.set(1900, Calendar.JANUARY, 0)

                            val calendar2 = Calendar.getInstance()
                            calendar2.set(2100, Calendar.DECEMBER, 30)

                            val minDate = calendar1.timeInMillis
                            val maxDate = calendar2.timeInMillis

                            return utcTimeMillis in minDate..maxDate
                        }

                        override fun isSelectableYear(year: Int): Boolean {
                            val maxYear = 2100
                            return year <= maxYear
                        }
                    }
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .weight(0.85f)
                ) {

                    Constant.AppTheme {
                        if(showDatePicker) {

                            DatePickerDialog(
                                onDismissRequest = { showDatePicker = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showDatePicker = false

                                        Log.d("Inside if showDatePiker", "$selectedSanctionDate")

                                        selectedSanctionDate = datePickerState.selectedDateMillis!!
                                        loanSanctionDate = sanctionDateFormat.format(Date(selectedSanctionDate))
                                        sanctionDateToDisplay = sanctionDateDisplayFormat.format(Date(selectedSanctionDate))

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
                    }




                    Column(modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(10.dp))
                    {


                        //EMPLOYEE NAME

                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Employee Name", 13, colorResource(id = R.color.paraColor), 500, 14)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.backgroundColor)
                                TextField(

                                    value = loanData.employeeName,
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

                                    enabled = false // Make the TextField non-editable
                                )
                            }
                        }

                        //EMPLOYEE CODE

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Employee Code", 13, colorResource(id = R.color.paraColor), 500, 14)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.backgroundColor)
                                TextField(

                                    value = loanData.employeeCode,
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

                                    enabled = false // Make the TextField non-editable
                                )
                            }
                        }

                        //LOAN TYPE

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 14)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.backgroundColor)
                                TextField(

                                    value = loanName,
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
                        }




                        //NAME IN PAYSLIP

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Name in Payslip", 13, colorResource(id = R.color.paraColor), 500, 20)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.backgroundColor)
                                TextField(

                                    value = nameInPayslip,
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

                                    enabled = false // Make the TextField non-editable
                                )
                            }
                        }


                        //REQUESTED AMOUNT

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 20)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.backgroundColor)
                                TextField(

                                    value = loanRequestedAmount,
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

                                    enabled = false // Make the TextField non-editable
                                )
                            }
                        }


                        //APPROVED AMOUNT

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            MandatoryTextField(label = "Approved Amount")

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)

                                TextField(

                                    value = loanApprovedAmount,
                                    onValueChange = {newValue ->
                                        val filteredValue = newValue.filter { it.isDigit() }
                                        loanApprovedAmount = filteredValue
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

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            MandatoryTextField(label = "Deduction Type")

                            Card(
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(5.dp),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(start = 1.dp, end = 1.dp, top = 10.dp),
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
                                                    DropdownMenuItem(
                                                        text = { Text(text = selectionOption,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.black)
                                                            )
                                                        ) },

                                                        onClick = {

                                                            loanDeductionType = when(selectionOption){
                                                                "Monthly" -> "1"
                                                                "Quarterly" -> "2"
                                                                "Half-Yearly" -> "3"
                                                                "Yearly" -> "4"
                                                                else -> ""
                                                            }

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
                        }


                        //Start Deduction Month

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            MandatoryTextField(label = "Start Deduction Month")

                            Column()
                            {

                                Box(modifier = Modifier.padding(top = 10.dp)) {
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
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .clickable { visible = true },
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clickable { visible = true }
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
                                                        visible = true
                                                    }
                                            )
                                        }
                                    }
                                }

                            }
                        }


                        //No. Of Months

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            MandatoryTextField(label = "Number of Instalments")

                            Box(modifier = Modifier.padding(top = 10.dp)) {
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
                        }


                        //SANCTION DATE

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            MandatoryTextField(label = "Sanction Date")

                            Column()
                            {

                                Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                    val containerColor = colorResource(id = R.color.white)
                                    TextField(
                                        readOnly = true,
                                        value = sanctionDateToDisplay,
                                        onValueChange = { sanctionDateToDisplay = it },

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
                                            color = colorResource(id = R.color.black) ),
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .border(
                                                width = 1.dp,
                                                color = colorResource(id = R.color.divider), // Change this to your desired border color
                                                shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                            )
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .clickable { showDatePicker = true },
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clickable { showDatePicker = true }
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
                                                    .clickable { showDatePicker = true }
                                            )
                                        }
                                    }
                                }

                            }
                        }


                        //Interest Rate

                        Column(modifier = Modifier.padding(bottom = 10.dp))
                        {
                            TextFieldNameSizeValidation("Interest Rate", 13, colorResource(id = R.color.paraColor), 500, 20)

                            Box(modifier = Modifier.padding(top = 10.dp)) {
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

                        }

                    }
                }

                Box(modifier = Modifier
                    .padding(top = 5.dp)
                    .weight(0.13f))
                {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(0),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                            Button(
                                onClick = {

                                    Log.d("Loan details", "Loan Apply details check : Loan Type / Loan Name / NameInPayslip / amount / Deduction Month / Number of Months / Rate of Interest : $loanTypeSlNo / $loanName / $nameInPayslip / $loanRequestedAmount / $deductionMonth / $numberOfInstalments / $interestRate")


                                    val sanctionDateValidation = Instant.ofEpochMilli(selectedSanctionDate).atZone(ZoneId.systemDefault()).toLocalDate()

                                    if(dojFlag == 1 && selectedDate.isAfter(dojDate))
                                    {
                                        Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
                                        Log.d("MonthPicker","selectedDate isAfter dojDate")
                                    }
                                    else
                                    {
                                        Log.d("MonthPicker","dojDate/selectedDate : $dojDate/$selectedDate ")
                                    }

                                    if(loanApprovedAmount.isEmpty() || loanApprovedAmount.isBlank() || loanApprovedAmount.toInt() == 0 || loanApprovedAmount.toDouble() == 0.0)
                                    {
                                        loanApprovedAmount = ""
                                        Constant.showToast(context, "Please enter the Loan Approved Amount")
                                    }
                                    else if(numberOfInstalments.isEmpty() || numberOfInstalments.isBlank() || numberOfInstalments.toInt() == 0 || numberOfInstalments.toDouble() == 0.0)
                                    {
                                        numberOfInstalments = ""
                                        Constant.showToast(context, "Please enter the Number of Instalments")
                                    }
                                    else if(dojFlag == 1 && !selectedDate.isAfter(dojDate))
                                    {
                                        Constant.showToast(context, "Selected Instalment start date should be greater than DOJ..!")
                                    }
                                    else if(sanctionDateToDisplay.isEmpty() || sanctionDateToDisplay.isBlank())
                                    {
                                        Constant.showToast(context, "Please select the Sanction Date...!")
                                    }
                                    else if(dojFlag == 1 && dojDate.isAfter(sanctionDateValidation))
                                    {
                                        Constant.showToast(context, "Selected Sanction Date should be greater than DOJ..!")
                                    }
                                    else if(sanctionDateValidation.isAfter(selectedDate))
                                    {
                                        Constant.showToast(context, "Sanction date should not be greater than installment start month..!")
                                    }
                                    else if(numberOfInstalments.isEmpty() || numberOfInstalments.isBlank() || numberOfInstalments.toInt() == 0 )
                                    {
                                        numberOfInstalments = ""
                                        Constant.showToast(context, "Please enter the Number of Instalments")
                                    }
                                    else if(salaryAmount.isBlank() || salaryAmount.isEmpty())
                                    {
                                        Constant.showToast(context, "Contact Admin... Salary is not fixed for you..")
                                    }
                                    else
                                    {
                                        statusLoading.value = true
                                        approvalListViewModel.getEmployeeProfileDetails(navController = navController, context = context, empId = loanData.employeeID, orgId = userViewModel.getOrg().toString())
                                        loanViewModel.getInstallmentDetails(navController = navController, context = context, deductionType = deductionType, numberOfInstalments = numberOfInstalments, startMonth = startMonth, loanAmount = loanApprovedAmount, interestRate = interestRate, interestType = interestRateType)

                                        reloadFlag = 1

                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(vertical = 15.dp)
                            ) {
                                Text(
                                    text = "Next",
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
    else //reload flag == 1
    {



        if(!loadingStatus1 && !statusLoading.value)
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
            {


                for (element in loanInstallmentDataList.value)
                {
                    totalRepayableAmount += element.installmentAmount

                    if(instalmentValue < element.installmentAmount)
                    {
                        instalmentValue = element.installmentAmount
                    }
                }

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 0.dp, end = 0.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 15.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "back",
                            tint = colorResource(id = R.color.themeColor),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(25.dp)
                                .clickable { reloadFlag = 0 }
                        )
                        BasicTextField(
                            readOnly = true,
                            value = "Loan Instalment",
                            onValueChange = { },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                color = colorResource(id = R.color.themeColor),
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                @Composable
                fun LoanAppliedEmployeeDetail()
                {

/*                    val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                    val img = event.profile // Assuming `wedding.Profile` contains the relative image path
                    val imgUrl = link + img
                    val processedImgUrl = imgUrl.ifEmpty { link }*/
                    val proName = loanData.employeeName
                    val firstTwoLetters = proName.take(2).uppercase(Locale.ROOT)

                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp, start = 10.dp, end = 10.dp)
                        .clickable { expanded1 = !expanded1 }, colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                    {
                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(start = 5.dp, end = 5.dp, top = 7.dp, bottom = 7.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {
                            Column(modifier = Modifier
                                .weight(0.15f)
                                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp))
                            {

                                if (profilePath.isNotEmpty()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                            .data(profilePath)
                                            .crossfade(true)
                                            .transformations(CircleCropTransformation())
                                            .allowHardware(true)
                                            .build(),
                                        contentDescription = "icon",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.size(35.dp)
                                    )
                                } else {
                                    Box(contentAlignment = Alignment.BottomCenter){
                                        Box(
                                            modifier = Modifier
                                                .size(33.dp)
                                                .background(
                                                    color = colorResource(id = R.color.pink),
                                                    shape = CircleShape
                                                ),contentAlignment = Alignment.Center
                                        ){
                                            Text(text = firstTwoLetters, color = colorResource(id = R.color.white), style = MaterialTheme.typography.titleSmall)
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(1.dp)){
                                            repeat(3){
                                                Box(modifier = Modifier
                                                    .size(3.dp)
                                                    .offset(y = (-4).dp)
                                                    .background(
                                                        color = colorResource(id = R.color.white),
                                                        shape = CircleShape
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                            }

                            Column(modifier = Modifier
                                .weight(0.75f)
                                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp))
                            {

                                val employee = "${loanData.employeeName} (${loanData.employeeCode})"


                                TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.black), 500, 30)

                                TextFieldNameSizeValidation(designation, 13, colorResource(id = R.color.paraColor), 500, 25)

                            }

                            Column(modifier = Modifier
                                .weight(0.10f)
                                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp))
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                                    contentDescription = "Down arrow",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .clickable {
                                            expanded1 = !expanded1
                                        }
                                )
                            }
                        }


                        if(expanded1)
                        {
                            Column {

                                HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top= 5.dp, bottom = 10.dp), color = colorResource(id = R.color.divider))

                                Row(modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                                {
                                    Column(modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp)
                                        .weight(0.33f))
                                    {
                                        Column(modifier = Modifier
                                            .padding(top = 5.dp, bottom = 2.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation("DOJ", 13, colorResource(id = R.color.paraColor), 500, 10)
                                        }

                                        Column(modifier = Modifier
                                            .padding(top = 3.dp, bottom = 5.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation(employeeDOJ, 12, colorResource(id = R.color.black), 500, 10)
                                        }
                                    }

                                    Column(modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp)
                                        .weight(0.33f))
                                    {
                                        Column(modifier = Modifier
                                            .padding(top = 5.dp, bottom = 2.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation("Department", 12, colorResource(id = R.color.paraColor), 500, 11)
                                        }

                                        Column(modifier = Modifier
                                            .padding(top = 3.dp, bottom = 5.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation(department, 12, colorResource(id = R.color.black), 500, 11)
                                        }
                                    }

                                    Column(modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp)
                                        .weight(0.34f))
                                    {
                                        Column(modifier = Modifier
                                            .padding(top = 5.dp, bottom = 2.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation("Gross Salary", 13, colorResource(id = R.color.paraColor), 500, 12)
                                        }

                                        Column(modifier = Modifier
                                            .padding(top = 3.dp, bottom = 10.dp)
                                            .align(Alignment.Start))
                                        {
                                            TextFieldNameSizeValidation(grossPay, 13, colorResource(id = R.color.black), 500, 12)
                                        }
                                    }
                                }
                            }
                        }

                    }
                }


                @Composable
                fun LoanApplyDetailHeader()
                {
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
                    {


                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(loanName, 13, colorResource(id = R.color.black), 500, 18)
                                }

                            }

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Deduction Type", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
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
                                            text = selectedDeductionType,
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


                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 2.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Approved Amount", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(loanApprovedAmount, 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(loanRequestedAmount, 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }


                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 2.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            val date = sanctionDateFormat.parse(loanSanctionDate)
                            val sanctionDate = sanctionDateDisplayFormat1.format(date)

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Deduction Start From", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(deductionStartDate, 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Sanction Date", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(sanctionDate, 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }


                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 2.dp, bottom = 10.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                        {

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Number of Instalments", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation(numberOfInstalments, 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }

                            Column(modifier = Modifier.weight(0.5f))
                            {

                                Column(modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("Rate of Interest", 13, colorResource(id = R.color.paraColor), 500, 30)
                                }

                                Column(modifier = Modifier.align(Alignment.Start))
                                {
                                    TextFieldNameSizeValidation("$interestRate%", 13, colorResource(id = R.color.black), 500, 10)
                                }

                            }

                        }


                    }
                }


                @Composable
                fun LoanInstalmentDetail()
                {

                    Column {

                        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))


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


                        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))

                        LazyColumn (modifier = Modifier.padding(bottom = 2.dp)){
                            items(loanInstallmentDataList.value) { data ->

                                Column(modifier = Modifier.padding(top = 4.dp, bottom = 7.dp, start = 15.dp, end = 15.dp)) {

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

                                    HorizontalDivider(modifier = Modifier.padding(top = 5.dp, bottom = 3.dp), color = colorResource(id = R.color.divider))

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
                                    text = "Are you sure you want to approve this?",
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
                                        showDialog = false




                                        // Parsing the startMonth string into a LocalDate object
                                        val date = LocalDate.parse(startMonth)

                                        // Extracting year and month from the LocalDate object
                                        val year1 = date.year
                                        val month1 = date.monthValue


                                        // Extracting fromDate
                                        val firstInstallment = loanInstallmentDataList.value.first()
                                        val fromMonth = firstInstallment.month.toInt()
                                        val fromYear = firstInstallment.year
                                        val fromDate = LocalDate.of(fromYear, fromMonth, 1)
                                        val formattedFromDate = fromDate.format(formatter3) + " 00:00:00.000"


                                        // Extracting toDate
                                        val lastInstallment = loanInstallmentDataList.value.last()
                                        val toMonth = lastInstallment.month.toInt()
                                        val toYear = lastInstallment.year
                                        val toDate = LocalDate.of(toYear, toMonth, 1)
                                        val formattedToDate = toDate.format(formatter3) + " 00:00:00.000"

                                        // Step 1: Define the formatter to parse the sanctionDate
                                        val inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
                                        val sanctionDate = LocalDate.parse(loanSanctionDate, inputFormatter)
                                        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        val formattedSanctionDate = sanctionDate.format(outputFormatter) + " 00:00:00.000"


                                        val finalAmount = loanApprovedAmount.toDouble() / numberOfInstalments.toInt()
                                        val formattedFinalAmount = String.format("%.2f", finalAmount)// Formatting the final amount to display only two decimal points

                                        Log.d("Button clicking1", "formattedFinalAmount/formattedFromDate/formattedToDate : $formattedFinalAmount/$formattedFromDate/$formattedToDate")


                                        Log.d("Button clicking2", "sfCode = ${loanData.employeeID} /n deductionType = $deductionType /n loanAmount = $loanApprovedAmount /n month = $month1 /n year = $year1 /n numberOfMonth = $numberOfInstalments /n div =  $divId /n org = ${userViewModel.getOrg()} /n fromDate = $formattedFromDate /n toDate= $formattedToDate /n finalAmount = $formattedFinalAmount/n interestRate = ${loanData.interestRate} " +
                                                "/n interestRateType = ${loanData.interestRateType} /n nameInPayslip = ${loanData.nameInPayslip} /n loanIntrestAmount = $totalRepayableAmount /n remarks = ${loanData.remarks} /n loanTypeSlNo = ${loanData.loanTypeSlNo} /n loanApplySlNo = ${loanData.loanApplySlNo} /n approverEmpId = ${userViewModel.getSFCode()} /n loanSanctionDate = $formattedSanctionDate /n instalInterestAmount = $instalmentValue")


                                        Log.d("loan Instalment Details", "$loanInstalmentItemsList")


                                        approvalListViewModel.postLoanEditApproveDetails(context = context, navController = navController, loanItems = loanInstalmentItemsList, empID = loanData.employeeID, deductionType = deductionType.toInt(), loanAmount = loanApprovedAmount.toFloat(), month = month1, year = year1, numberOfInstalments = numberOfInstalments.toInt(), divId = divId.toInt(), orgId = userViewModel.getOrg(), fromDate = formattedFromDate, toDate = formattedToDate, finalAmount = finalAmount.toFloat(), interestRate = interestRate.toFloat(), interestRateType = interestRateType.toInt(), nameInPayslip = nameInPayslip, loanInterestAmount = totalRepayableAmount.toFloat(), remarks = loanData.remarks, loanTypeSlNo = loanTypeSlNo.toInt(), loanApplySlNo = loanApplySlNo.toInt(), approverEmpId = userViewModel.getSFCode(), loanSanctionDate = formattedSanctionDate, instalmentInterestType = instalmentValue.toFloat())

                                        statusLoading.value = false

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


                @Composable
                fun uiUpdate()
                {

                    if(statusLoading.value)
                    {
                        circularProgression()
                    }


                    Column(modifier = Modifier.fillMaxSize())
                    {

                        Column(modifier = Modifier.weight(0.83f))
                        {
                            Column(modifier = Modifier)
                            {
                                LoanAppliedEmployeeDetail()
                            }

                            Column(modifier = Modifier)
                            {
                                LoanApplyDetailHeader()
                            }

                            Column(modifier = Modifier)
                            {
                                LoanInstalmentDetail()
                            }
                        }


                        Column(modifier = Modifier.weight(0.05f))
                        {

                            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))

                            Row(modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                Column(modifier = Modifier.weight(0.5f))
                                {
                                    Text(
                                        text = "Repayable Amount",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                    )
                                }

                                Column(modifier = Modifier.weight(0.5f))
                                {
                                    Text(
                                        text = totalRepayableAmount.toString(),
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.themeColor)),
                                        modifier = Modifier
                                            .align(Alignment.End)
                                    )
                                }
                            }
                        }


                        Column(modifier = Modifier.weight(0.12f))
                        {
                            Box(modifier = Modifier) {
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
                                                    loanViewModel.getExistingLoanAmountDetails(navController = navController, context = context, empId = userViewModel.getSFCode()){ instalmentAmount ->

                                                        if((instalmentAmount.toDouble() + instalmentValue) < salaryAmount.toDouble())
                                                        {

                                                            Log.d("Button clicking1", "$salaryAmount/${(instalmentAmount.toDouble() + instalmentValue)} instalmentAmount.toDouble() + instalmentValue) < salaryAmount.toDouble()")

//                                                            statusLoading.value = true

                                                            showDialog = true



                                                        }
                                                        else
                                                        {
                                                            Log.d("Button clicking1", "Salary/ExistingInstalment : $salaryAmount/${(instalmentAmount.toDouble() + instalmentValue)}")
                                                            statusLoading.value = false
                                                            coroutineScope.launch { Constant.showToast(context,"Employee had an Outstanding Loan!!!") }
                                                        }

                                                    }
                                                }
                                                else
                                                {
                                                    statusLoading.value = false
                                                    coroutineScope.launch { Constant.showToast(context, "Instalment Amount is greater than employee salary!!!") }
                                                }

                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                            shape = RoundedCornerShape(5.dp),
                                            contentPadding = PaddingValues(vertical = 15.dp)
                                        ) {
                                            Text(
                                                text = "Save & Approve",
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


                if(loading1)
                {
                    circularProgression()
                }

                // LOGIC TO DISPLAY THE UI

                if(loadingStatus1)
                {
                    loading1 = true
                }
                else
                {
                    loading1 = false

                    if(loanInstallmentDataList.value.isEmpty())
                    {
                        when (flagInstallment)
                        {
                            0 -> {
                                loading1 = true
                            }
                            1 -> {
                                loanViewModel.getInstallmentDetails(navController = navController, context = context, deductionType = deductionType, numberOfInstalments = numberOfInstalments, startMonth = startMonth, loanAmount = loanApprovedAmount, interestRate = interestRate, interestType = interestRateType)
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
                        when (flagInstallment)
                        {
                            0 -> {
                                loading1 = true
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
        else
        {
            circularProgression()
        }
    }
}




@Composable
@Preview
fun LoanAppliedEmployeeDetailPreview()
{

    var expanded by remember { mutableStateOf(true) }

    val loanData = generateLoanData()
    val firstTwoLetters = loanData.employeeName.take(2).uppercase(Locale.ROOT)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
    {
        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 5.dp, end = 5.dp, top = 7.dp, bottom = 7.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier
                .weight(0.15f)
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp))
            {

                Box(contentAlignment = Alignment.BottomCenter){
                    Box(
                        modifier = Modifier
                            .size(33.dp)
                            .background(
                                color = colorResource(id = R.color.pink),
                                shape = CircleShape
                            ),contentAlignment = Alignment.Center
                    ){
                        Text(text = firstTwoLetters, color = colorResource(id = R.color.white), style = MaterialTheme.typography.titleSmall)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)){
                        repeat(3){
                            Box(modifier = Modifier
                                .size(3.dp)
                                .offset(y = (-4).dp)
                                .background(
                                    color = colorResource(id = R.color.white),
                                    shape = CircleShape
                                )
                            )
                        }
                    }
                }
//                                }

            }

            Column(modifier = Modifier
                .weight(0.75f)
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp))
            {

                val employee = "${loanData.employeeName} (${loanData.employeeCode})"

                TextFieldNameSizeValidation(employee, 13, colorResource(id = R.color.black), 500, 30)

                TextFieldNameSizeValidation("Software Developer", 13, colorResource(id = R.color.paraColor), 500, 25)

            }

            Column(modifier = Modifier
                .weight(0.10f)
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .clickable { expanded = !expanded })
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = "Down arrow",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            expanded = !expanded
                        }
                )
            }
        }


        if(expanded)
        {
            Column {

                HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top= 5.dp, bottom = 10.dp), color = colorResource(id = R.color.divider))

                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    {
                        Column(modifier = Modifier
                            .padding(top = 5.dp, bottom = 2.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("DOJ", 13, colorResource(id = R.color.paraColor), 500, 30)
                        }

                        Column(modifier = Modifier
                            .padding(top = 3.dp, bottom = 5.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("22/02/2022", 13, colorResource(id = R.color.black), 500, 30)
                        }
                    }

                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    {
                        Column(modifier = Modifier
                            .padding(top = 5.dp, bottom = 2.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("Department", 13, colorResource(id = R.color.paraColor), 500, 30)
                        }

                        Column(modifier = Modifier
                            .padding(top = 3.dp, bottom = 5.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("Sales", 13, colorResource(id = R.color.black), 500, 30)
                        }
                    }

                    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    {
                        Column(modifier = Modifier
                            .padding(top = 5.dp, bottom = 2.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("Gross Salary", 13, colorResource(id = R.color.paraColor), 500, 30)
                        }

                        Column(modifier = Modifier
                            .padding(top = 3.dp, bottom = 10.dp)
                            .align(Alignment.Start))
                        {
                            TextFieldNameSizeValidation("25000", 13, colorResource(id = R.color.black), 500, 30)
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun LoanApplyDetailHeaderPreview()
{

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 4.dp, bottom = 5.dp, start = 10.dp, end = 10.dp), colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)))
    {


        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 10.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Car Loan", 13, colorResource(id = R.color.black), 500, 18)
                }

            }

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Deduction Type", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
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
                            text = "Monthly",
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


        }

        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 2.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Approved Amount", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("5000", 13, colorResource(id = R.color.black), 500, 10)
                }

            }

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("10000", 13, colorResource(id = R.color.black), 500, 10)
                }

            }


        }

        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 2.dp, bottom = 4.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {


            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Deduction Start From", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("01/02/2023", 13, colorResource(id = R.color.black), 500, 10)
                }

            }

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Sanction Date", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("01/01/2023", 13, colorResource(id = R.color.black), 500, 10)
                }

            }


        }

        HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.divider))

        Row(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 2.dp, bottom = 10.dp, start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Number of Instalments", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("10", 13, colorResource(id = R.color.black), 500, 10)
                }

            }

            Column(modifier = Modifier.weight(0.5f))
            {

                Column(modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("Rate of Interest", 13, colorResource(id = R.color.paraColor), 500, 30)
                }

                Column(modifier = Modifier.align(Alignment.Start))
                {
                    TextFieldNameSizeValidation("10%", 13, colorResource(id = R.color.black), 500, 10)
                }

            }

        }
    }
}


@Composable
@Preview
fun LoanInstalmentDetailPreview()
{

    val loanInstallmentDataList = generateInstalmentData()

    Log.d("qwerty", "$loanInstallmentDataList")

    var totalRepayableAmount = 0.00
    var instalmentValue = 0.00

    for (element in loanInstallmentDataList)
    {
        totalRepayableAmount += element.installmentAmount

        if(instalmentValue < element.installmentAmount)
        {
            instalmentValue = element.installmentAmount
        }
    }

    Column {

        HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp, start = 10.dp, end = 10.dp), color = colorResource(id = R.color.lightshade))


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


        HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))

        LazyColumn (modifier = Modifier.padding(bottom = 2.dp)){
            items(loanInstallmentDataList) { data ->

                Column(modifier = Modifier.padding(top = 4.dp, bottom = 7.dp, start = 15.dp, end = 15.dp)) {

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

                    HorizontalDivider(modifier = Modifier.padding(top = 5.dp, bottom = 3.dp), color = colorResource(id = R.color.divider))

                }
            }
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLoanApprovalEntryPreview()
{
    val navController = rememberNavController()
    var expanded = false
    var visible = false

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Edit Loan", url = "backUrl") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)
        )
        {
            Card(
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(0.85f)
            ) {

                Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(10.dp))
                {


                    //EMPLOYEE NAME

                    Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Employee Name", 13, colorResource(id = R.color.paraColor), 500, 14)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(

                                value = "Matheesha Pathirana",
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

                                modifier = Modifier.fillMaxWidth(1f).border( width = 1.dp, color = colorResource(id = R.color.divider), shape = RoundedCornerShape(4.dp) ),

                                enabled = false // Make the TextField non-editable
                            )
                        }
                    }

                    //EMPLOYEE CODE

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Employee Code", 13, colorResource(id = R.color.paraColor), 500, 14)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(

                                value = "SAN123",
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

                                enabled = false // Make the TextField non-editable
                            )
                        }
                    }

                    //LOAN TYPE

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Loan Type", 13, colorResource(id = R.color.paraColor), 500, 14)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(

                                value = "Car Loan",
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
                    }




                    //NAME IN PAYSLIP

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Name in Payslip", 13, colorResource(id = R.color.paraColor), 500, 20)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(

                                value = "Car Loan",
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

                                enabled = false // Make the TextField non-editable
                            )
                        }
                    }


                    //REQUESTED AMOUNT

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Requested Amount", 13, colorResource(id = R.color.paraColor), 500, 20)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(

                                value = "10000",
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

                                enabled = false // Make the TextField non-editable
                            )
                        }
                    }


                    //APPROVED AMOUNT

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        MandatoryTextField(label = "Approved Amount")

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)

                            TextField(

                                value = "5000",
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

                        }
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

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        MandatoryTextField(label = "Deduction Type")

                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(start = 1.dp, end = 1.dp, top = 10.dp),
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
                                            value = "Monthly",
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
                                                DropdownMenuItem(
                                                    text = { Text(text = selectionOption,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)
                                                        )
                                                    ) },

                                                    onClick = {
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
                    }


                    //Start Deduction Month

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        MandatoryTextField(label = "Start Deduction Month")

                        Column()
                        {

                            Box(modifier = Modifier.padding(top = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = "Jan 2023",
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
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .clickable { visible = true },
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable { visible = true }
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
                                                    visible = true
                                                }
                                        )
                                    }
                                }
                            }

                        }
                    }


                    //No. Of Months

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        MandatoryTextField(label = "Number of Instalments")

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.white)

                            TextField(

                                value = "6",
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

                        }
                    }


                    //SANCTION DATE

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        MandatoryTextField(label = "Sanction Date")

                        Column()
                        {

                            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                                val containerColor = colorResource(id = R.color.white)
                                TextField(
                                    readOnly = true,
                                    value = "10/3/2022",
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
                                        color = colorResource(id = R.color.black) ),
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .border(
                                            width = 1.dp,
                                            color = colorResource(id = R.color.divider), // Change this to your desired border color
                                            shape = RoundedCornerShape(4.dp) // Adjust the shape as needed
                                        )
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .clickable { },
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable {  }
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
                    }


                    //Interest Rate

                    Column(modifier = Modifier.padding(bottom = 10.dp))
                    {
                        TextFieldNameSizeValidation("Interest Rate", 13, colorResource(id = R.color.paraColor), 500, 20)

                        Box(modifier = Modifier.padding(top = 10.dp)) {
                            val containerColor = colorResource(id = R.color.backgroundColor)

                            TextField(

                                value = "10",
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

                    }

                }
            }

            Box(modifier = Modifier
                .padding(top = 5.dp)
                .weight(0.13f))
            {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(0),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = "Next",
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


@Composable
@Preview
fun uiUpdatePreview()
{

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 0.dp, bottom = 0.dp, start = 0.dp, end = 0.dp))
    {

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 0.dp, end = 0.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable { }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "back",
                    tint = colorResource(id = R.color.themeColor),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(25.dp)
                        .clickable {  }
                )
                BasicTextField(
                    readOnly = true,
                    value = "Loan Instalment",
                    onValueChange = { },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                        color = colorResource(id = R.color.themeColor),
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        Column(modifier = Modifier.weight(0.85f))
        {
            Column(modifier = Modifier)
            {
                LoanAppliedEmployeeDetailPreview()
            }

            Column(modifier = Modifier)
            {
                LoanApplyDetailHeaderPreview()
            }

            Column(modifier = Modifier)
            {
                LoanInstalmentDetailPreview()
            }
        }

        Column(modifier = Modifier.weight(0.05f))
        {

            HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp, start = 5.dp, end = 5.dp), color = colorResource(id = R.color.lightshade))

            Row(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 15.dp, end = 15.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceBetween)
            {
                Column(modifier = Modifier.weight(0.5f))
                {
                    Text(
                        text = "Total Repayable Amount",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                }

                Column(modifier = Modifier.weight(0.5f))
                {
                    Text(
                        text = "5000",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.themeColor)),
                        modifier = Modifier
                            .align(Alignment.End)
                    )
                }
            }
        }


        Column(modifier = Modifier.weight(0.10f))
        {
            Box(modifier = Modifier)
            {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                    shape = RoundedCornerShape(0),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(vertical = 15.dp)
                        ) {
                            Text(
                                text = "Save & Approve",
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