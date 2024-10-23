package com.payroll.twogrowhr.Model.View


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


var dateList = mutableListOf<LocalDate>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TeamAttendance(navController: NavController,  viewModel: MainViewModel) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Team Attendance",
            "Attendance"
        ) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { TeamAttendance_Screen(navController = navController,viewModel = viewModel) }

}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeamAttendance_Screen(navController: NavController,viewModel: MainViewModel) {
    val calendarState = rememberSheetState()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    var disabled by remember { mutableStateOf(false) }
    var disabled1 by remember { mutableStateOf(false) }

    var selectedCardIndex by remember { mutableStateOf(-1) }

    Constant.AppTheme{
        CalendarDialog(state = calendarState, selection = CalendarSelection.Date { date ->
            selectedDate.value = date
            calendarState.hide()
        })
    }

    var month by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH) + 1) }
    var year by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val dateList = remember { mutableStateOf(getDatesForMonthYear(currentYear, currentMonth)) }

    Log.d("Team attendance","Current Month : $currentMonth")
    Log.d("Team attendance","Month : $month")

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

    var monthDisplay = getMonth(month)

    var selectedDate1 = selectedDate.value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    Log.d("selectedDate1....","$selectedDate1")

    SideEffect {
        selectedCardIndex = -1 // Reset the selected card index whenever dateList changes
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
    ) {
        disabled = month == 1 && year == currentYear - 1
        disabled1 = year == currentYear && month == currentMonth
        MonthPicker(
            visible = visible,
            currentMonth = month,
            currentYear = year,
            confirmButtonCLicked = { month_, year_ ->
                month = month_
                year = year_

                monthDisplay = getMonth(month)

                dateList.value = getDatesForMonthYear(year, month)

                Log.d("MonthPicker","month/year : ${month}/${year} ")
                Log.d("MonthPicker","month displayed : $monthDisplay ")

                visible = false
            },
            cancelClicked = {
                dateList.value = getDatesForMonthYear(year, month)
                visible = false
            }
        )

        Button(
            onClick = { visible = true },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade))
        ) {
            Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),) {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            disabled = month == 1 && year == currentYear - 1

                            if(!disabled)
                            {
                                if (month == 1) {
                                    month = 12
                                    year--
                                }
                                else
                                {
                                    month--
                                }

                                monthDisplay = getMonth(month)
                                dateList.value = getDatesForMonthYear(year, month)
                            }
                        },
                        tint =  if (!disabled) colorResource(id = R.color.themeColor) else Color.Gray
                    )

                    Text(
                        text = "$monthDisplay $year",
                        style = TextStyle(fontFamily = poppins_font,
                            fontSize = 14.sp, fontWeight = FontWeight(500),
                            color = colorResource(id = R.color.black)
                        ),
                        modifier = Modifier.clickable { visible = true }
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.clickable {

                            disabled1 = year == currentYear && month == currentMonth

                            if(!disabled1)
                            {
                                if (month == 12) {
                                    month = 1
                                    year++
                                } else {
                                    month++
                                }

                                monthDisplay = getMonth(month)
                                dateList.value = getDatesForMonthYear(year, month)

                            }
                        },
                        tint =  if (!disabled1) colorResource(id = R.color.themeColor) else Color.Gray
                    )
                }
            }
        }

        LazyRow {

            items(dateList.value) { date ->

                val index = dateList.value.indexOf(date)

                val isSelected = selectedCardIndex == index


                Card(
                    modifier = Modifier
                        .padding(start = 3.dp, end = 3.dp, bottom = 3.dp, top = 10.dp)
                        .width(40.dp)
                        .height(40.dp)
                        .background(Color.White)
                        .animateContentSize(
                            animationSpec = tween(
                                delayMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),

                    ){


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = {
                                selectedCardIndex = index
                                Log.d("Team Attendance", "Selected Card Value: ${date.dayOfMonth}")
                            })
                            .background(color = if (isSelected) colorResource(id = R.color.themeColor) else colorResource(id = R.color.white) ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.black),
                        )

                    }

                }


            }

        }


//Need to add some content

    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDatesForMonthYear(year: Int, month: Int) : List<LocalDate> {

//    dateList.clear()

    val dateList = mutableListOf<LocalDate>()

    Log.d("getDatesForMonthYear", "month/year : $month $year")

    val startDate = LocalDate.of(year, month, 1)
    val endDate = startDate.plusMonths(1).minusDays(1)

    dateList.addAll(generateSequence(startDate) { it.plusDays(1) }
        .takeWhile { it <= endDate })

    dateList.forEach { println("${it.dayOfMonth}/${it.month}") }
    return dateList

}

@Composable
fun MonthPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    confirmButtonCLicked: (Int, Int) -> Unit,
    cancelClicked: () -> Unit
) {

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
                            tint = if (year == currentYearDialog) { colorResource(id = R.color.themeColor) } else { colorResource(id = R.color.paraColor) },
                            modifier = Modifier.clickable {
                                try
                                {
                                    if (year == currentYearDialog)
                                    {
                                        year--
                                    }
                                }
                                catch (e :Exception)
                                {
                                    Log.d("Team Attendance...", "Team Attendance : exception : ${e.message}")
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
                            tint = if (year == currentYearDialog) { colorResource(id = R.color.paraColor) } else { colorResource(id = R.color.themeColor) },
                            modifier = Modifier.clickable
                            {
                                try {
                                    if (year != currentYearDialog) {
                                        year++
                                        month = months[currentMonthDialog - 1]
                                    }
                                }
                                catch (e: Exception) {
                                    Log.d("Team Attendance...", "Team Attendance : exception : ${e.message}")
                                }
                            }

                        )

                    }

                    Card(
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
                            months.forEachIndexed { index, it ->

                                val selectable = year < currentYearDialog || (year == currentYearDialog && index < currentMonthDialog)

                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = {
                                                if (selectable) {
                                                    month = it
                                                }
                                            },
                                        )
                                        .background(
                                            color = if (month == it) colorResource(id = R.color.themeColor) else Color.Transparent,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val textColor = if (month == it) Color.White else if (selectable) Color.Black else Color.Gray
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

}
