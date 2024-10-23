package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.HolidayListData
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryDetailData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.viewModel.HolidayViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Holiday(navController: NavController, holidayViewModel: HolidayViewModel) {

    AppScaffold1(
        topBarContent = {
            TopBarBackNavigation(
                navController = navController,
                title = "Holiday List",
                "HomeScreen"
            )
        },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { Holiday_Screen(navController = navController, holidayViewModel = holidayViewModel) }
}

@Composable
fun Holiday_Screen(navController: NavController, holidayViewModel: HolidayViewModel)
{
    val context = LocalContext.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var year by remember { mutableStateOf(currentYear) }

    // Fetch empID using ViewModel or other method
    val empID = userViewModel.getSFCode()

    // Fetch and update the holiday list when entering the page
    val holidaysList = holidayViewModel.holidayList.collectAsState()

    Log.d("Holiday... HolidayListDetails", "${holidaysList.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
    ) {
        Row {
            Column {
                Image(
                    painterResource(id = R.drawable.umb),
                    contentDescription = "umbrella",
                    modifier = Modifier
                        .size(100.dp)
                        .offset(y = (-25).dp)
                )
            }
            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                Row {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "back",
                        tint = if (year == currentYear) { colorResource(id = R.color.themeColor) } else { colorResource(id = R.color.paraColor) },
                        modifier = Modifier.clickable {
                            try
                            {
                                if (year == currentYear)
                                {
                                    year--
                                    holidayViewModel.getHoliday(navController,context,empID,year.toString())
                                }
                            }
                            catch (e :Exception)
                            {
                                Log.d("Holiday... HolidayListDetails", "exception : ${e.message}")
                                Constant.showToast(context,"Something went wrong...!")
                            }
                        }
                    )
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(id = R.color.black)
                    )
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        contentDescription = "back",
                        tint = if (year == currentYear) { colorResource(id = R.color.paraColor) } else { colorResource(id = R.color.themeColor) },
                        modifier = Modifier.clickable
                        {
                            try
                            {
                                if (year != currentYear)
                                {
                                    year++
                                    holidayViewModel.getHoliday(navController,context, empID,year.toString())
                                }
                            }
                            catch (e :Exception)
                            {
                                Log.d("Holiday... HolidayListDetails", "exception : ${e.message}")
                                Constant.showToast(context,"Something went wrong...!")
                            }
                        }
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
                .padding(top = 10.dp)
                .offset(y = (-60).dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
        )
        {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Holidays",
                    style = MaterialTheme.typography.titleLarge,
                    color = colorResource(id = R.color.black)
                )
                LazyColumn(modifier = Modifier.padding(top = 8.dp))
                {
                    items(holidaysList.value) { holiday ->
                        Row {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(15.dp)
                                        .background(color = Color(android.graphics.Color.parseColor(holiday.color2)), shape = CircleShape)
                                )
                                HorizontalDivider(
                                    modifier = Modifier
                                        .height(60.dp)
                                        .padding(start = 6.dp, top = 4.dp, bottom = 4.dp)
                                        .width(1.dp),
                                    color = colorResource(id = R.color.paraColor)
                                )
                            }
                            Row {
                                Column(modifier = Modifier
                                    .padding(start = 10.dp)
                                    .weight(1f)) {
                                    BasicTextField(
                                        readOnly = true,
                                        value = holiday.holidayName,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black),
                                        ),
                                        singleLine = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 16.dp) // Adjust padding as needed
                                    )

                                    Text(
                                        text = "${holiday.monthName} ${holiday.date}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colorResource(id = R.color.paraColor)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .weight(0.5f),
                                    horizontalAlignment = Alignment.End
                                )
                                {
                                    Text(
                                        text = holiday.weekDay,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black)
                                    )
                                }
                            }
                        }
                    }

                    if (holidaysList.value.isEmpty())
                    {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center // Center the content vertically and horizontally
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.umb),
                                        contentDescription = "umbrella",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .offset(y = 20.dp)
                                            .alpha(0.5f)
                                    )
                                    Text(
                                        text = "No Holidays",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black),
                                        modifier = Modifier.padding(top = 8.dp) // Optional: Add top padding to the text
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiHolidayListPreview() {

    val navController = rememberNavController()

    val holidayDataList = generateHolidayDataList()



    AppScaffold1(
        topBarContent = {
            TopBarBackNavigation(navController = navController, title = "Holiday List", "HomeScreen") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var year by remember { mutableStateOf(currentYear) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
        ) {
            Row {
                Column {
                    Image(
                        painterResource(id = R.drawable.umb),
                        contentDescription = "umbrella",
                        modifier = Modifier.size(100.dp).offset(y = (-25).dp)
                    )
                }
                Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                    Row {
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "back",
                            tint = if (year == currentYear) { colorResource(id = R.color.themeColor) } else { colorResource(id = R.color.paraColor) },
                            modifier = Modifier
                        )
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(id = R.color.black)
                        )
                        Icon(
                            painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = "back",
                            tint = if (year == currentYear) { colorResource(id = R.color.paraColor) } else { colorResource(id = R.color.themeColor) },
                            modifier = Modifier
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .padding(top = 10.dp)
                    .offset(y = (-60).dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
            )
            {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Holidays",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(id = R.color.black)
                    )
                    LazyColumn(modifier = Modifier.padding(top = 8.dp))
                    {
                        items(holidayDataList) { holiday ->
                            Row {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .background(color = Color(android.graphics.Color.parseColor(holiday.color2)), shape = CircleShape)
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .height(60.dp)
                                            .padding(start = 6.dp, top = 4.dp, bottom = 4.dp)
                                            .width(1.dp),
                                        color = colorResource(id = R.color.paraColor)
                                    )
                                }
                                Row {
                                    Column(modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f)) {
                                        BasicTextField(
                                            readOnly = true,
                                            value = holiday.holidayName,
                                            onValueChange = { /* Handle value change if needed */ },
                                            textStyle = TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(500),
                                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                color = colorResource(id = R.color.black),
                                            ),
                                            singleLine = true,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 16.dp) // Adjust padding as needed
                                        )

                                        Text(
                                            text = "${holiday.monthName} ${holiday.date}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = colorResource(id = R.color.paraColor)
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(1f)
                                            .weight(0.5f),
                                        horizontalAlignment = Alignment.End
                                    )
                                    {
                                        Text(
                                            text = holiday.weekDay,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                }
                            }
                        }

                        if (holidayDataList.isEmpty())
                        {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center // Center the content vertically and horizontally
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Image(
                                            painterResource(id = R.drawable.umb),
                                            contentDescription = "umbrella",
                                            modifier = Modifier
                                                .size(100.dp)
                                                .offset(y = 20.dp)
                                                .alpha(0.5f)
                                        )
                                        Text(
                                            text = "No Holidays",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black),
                                            modifier = Modifier.padding(top = 8.dp) // Optional: Add top padding to the text
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

fun generateHolidayDataList(): List<HolidayListData>
{
    return listOf(
        HolidayListData(monthName = "Jan", date = "15", weekDay = "Sunday", holidayName = "PONGAL", color1 = "#009900", color2 = "#ccffcc", holidayDate = "2024-01-15T00:00:00"),
        HolidayListData(monthName = "Oct", date = "4", weekDay = "Wednesday", holidayName = "Saraswathi Pooja", color1 = "#003399", color2 = "#ccddff", holidayDate = "2024-10-04T00:00:00"),
        HolidayListData(monthName = "Oct", date = "23", weekDay = "Monday", holidayName = "Diwali", color1 = "#e63900", color2 = "#ffd9cc", holidayDate = "2024-10-23T00:00:00"),
    )
}


