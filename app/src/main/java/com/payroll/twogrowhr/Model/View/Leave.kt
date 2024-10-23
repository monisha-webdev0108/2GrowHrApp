package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.LeaveHistoryData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import java.text.SimpleDateFormat
import java.util.Locale

var leaveHistoryDataList1 = mutableStateOf<List<LeaveHistoryData>>(emptyList())

data class LeaveItemBalance(
    val totalBalance: Int,
    val availableBalance: Int,
    val leaveName: String
)





@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Leave(navController: NavController, leaveViewModel: LeaveViewModel) {

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave", "Attendance") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    { Leave_Screen(navController = navController, leaveViewModel = leaveViewModel) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("DEPRECATION")
@SuppressLint("NewApi")
@Composable
fun Leave_Screen(navController: NavController, leaveViewModel: LeaveViewModel) {

    val context = LocalContext.current

// Create a list to store LeaveItemBalance instances
    val leaveItemBalanceList = mutableListOf<LeaveItemBalance>()

// Add instances to the list
    leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 12, availableBalance = 5, leaveName = "casual"))
    leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 15, availableBalance = 8, leaveName = "sick"))
    leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 20, availableBalance = 12, leaveName = "paid"))
    leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 30, availableBalance = 5, leaveName = "unpaid"))
    leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 30, availableBalance = 0, leaveName = "Bal Zero Check"))



    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, start = 10.dp, end = 10.dp)
    ){

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = leaveViewModel.loadingStatus2

        flag = leaveViewModel.flag2

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        leaveViewModel.leaveHistoryList.collectAsState().also {
            leaveHistoryDataList1 = it as MutableState<List<LeaveHistoryData>>
        }


        @Composable
        fun noData()
        {
            Column(modifier = Modifier.fillMaxWidth(1f))
            {
                Image(
                    painter = painterResource(id = R.drawable.noleavesvg),
                    contentDescription = null, // Set to null if the image is decorative
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(with(LocalDensity.current) { 150.dp }),
                    contentScale = ContentScale.Fit
                )

                Text(text = "No leave history",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 10.dp),
                    style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                )
            }
        }

        @Composable
        fun uiUpdateLeave()
        {
            Column(modifier = Modifier.fillMaxWidth(1f))
            {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth(1f))
                {

                    LazyColumn {

                        // Sort the list by descending order using the Created_Date
                        val sortedList = leaveHistoryDataList1.value.sortedByDescending {
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(it.fromDate)
                        }


                        items(sortedList.take(3))
                        {data ->


                            val leaveTypeName = data.leaveTypeName
                            val fromDate = data.fromDate
                            val toDate = data.toDate
                            val noOfDays = data.noOfDays
                            val leaveStatus = data.leaveStatus
                            val leaveUnit = data.leaveUnit


                            val leaveType = if(leaveUnit == "Day") "Days" else "Hours"



                            val bgColor = when (data.leaveStatus) {
                                "Approved" -> R.color.toolight_green
                                "Reject" -> R.color.toolight_red
                                "Pending" -> R.color.toolight_themecolor
                                "Partially Approved" -> R.color.light_bright_yellow
                                "Cancelled" -> R.color.light_pink
                                else -> R.color.white
                            }

                            val statusColor1 = when (data.leaveStatus) {
                                "Approved" -> R.color.green
                                "Reject" -> R.color.red
                                "Pending" -> R.color.themeColor
                                "Partially Approved" -> R.color.yellow
                                "Cancelled" -> R.color.pink
                                else -> R.color.white
                            }


                            val inputFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                            val fromDateFormat = inputFormatter.parse(fromDate)
                            val fromDateFormatted = outputFormatter.format(fromDateFormat!!)


                            val toDateFormat = inputFormatter.parse(toDate)
                            val toDateFormatted = outputFormatter.format(toDateFormat!!)



                            Column(modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp))
                            {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),horizontalArrangement = Arrangement.SpaceBetween)
                                {

                                    //Leave Date and Type

                                    Column(modifier = Modifier.padding(top = 2.dp, start = 5.dp)){

                                        //For Date
                                        Row(modifier = Modifier.align(Alignment.Start)){
                                            Column {
                                                Text(text = "$fromDateFormatted - $toDateFormatted",
                                                    style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                                                )
                                            }
                                        }



                                        //For Leave Name
                                        Row(modifier = Modifier
                                            .padding(top = 3.dp)
                                            .align(Alignment.Start)){
                                            Column {
                                                if (leaveTypeName.length > 15)
                                                {

                                                    PlainTooltipBox(
                                                        tooltip = {
                                                            Text(
                                                                text = leaveTypeName,
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 13.sp,
                                                                    fontWeight = FontWeight(500),
                                                                ),
                                                            )
                                                        }
                                                    ) {
                                                        Text(
                                                            text = leaveTypeName.take(15) + "..." + " $noOfDays $leaveType",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.paraColor)
                                                            ),
                                                            modifier = Modifier.tooltipTrigger()
                                                        )
                                                    }
                                                }
                                                else
                                                {
                                                    Text(
                                                        text = "$leaveTypeName $noOfDays $leaveType",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    //Leave Status

                                    Column(modifier = Modifier.padding(top = 2.dp, end = 5.dp))
                                    {

                                        Button(onClick = { },
                                            colors = ButtonDefaults.buttonColors(colorResource(id = bgColor)),
                                            shape = RoundedCornerShape(20),
                                            modifier = Modifier.align(Alignment.End)
                                        ) {


                                            if (leaveStatus.length > 9) {

                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = leaveStatus,
                                                            style = TextStyle(fontFamily = poppins_font, fontSize = 12.sp, fontWeight = FontWeight(500)),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = leaveStatus.take(10) + "..." ,
                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                        modifier = Modifier
                                                            .padding(
                                                                top = 2.dp,
                                                                bottom = 2.dp,
                                                                start = 4.dp,
                                                                end = 4.dp
                                                            )
                                                            .tooltipTrigger())
                                                }
                                            } else {
                                                Text(
                                                    text = leaveStatus,
                                                    modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
                                                    style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                )
                                            }
                                        }

                                    }

                                }

                                //Divider

                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 5.dp),
                                    color = colorResource(id = R.color.divider)
                                )

                            }

                        }
                    }
                }

                //View All Card


                Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .clickable { navController.navigate("leaveHistory") })
                {
                    Column(modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 5.dp, bottom = 10.dp)
                        , horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "View all", style = MaterialTheme.typography.titleMedium, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                }
            }
        }


        //Leave History Detail

        Card(colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(1f))
        {

            val employeeID = userViewModel.getSFCode()

            // LOGIC TO DISPLAY THE UI

            if(loadingStatus)
            {
                loading = true
            }
            else
            {
                loading = false

                if(leaveHistoryDataList1.value.isEmpty())
                {
                    when (flag)
                    {
                        0 -> {
                            loading = true
                        }
                        1 -> {
                            leaveViewModel.getLeaveHistory(navController, context, empId = employeeID)
                        }
                        2 -> {
                            noData()
                        }
                        3 -> {
//                            exceptionScreen()
                            noData()

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
                            uiUpdateLeave()
                        }
                        2 -> {
                            noData()
                        }

                        3 -> {
//                            exceptionScreen()
                            noData()
                        }
                        else -> {
                            Constant.showToast(context,"Please try again later...!")
                        }
                    }
                }
            }


        }



        //Button For Request Leave 1

/*        Button(onClick = { navController.navigate("LeaveApply") }, //LeaveApply
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp)
        ) {
            Text(text = "Request Leave",
                style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.white)
            ),)
        }*/

        //Button For Request Leave New

        Button(onClick = { navController.navigate("applyLeave") }, //LeaveApply
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp)
        ) {
            Text(text = "Request Leave",
                style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.white)
            ),)
        }
/*
// Correct Code For Leave Balance

        //Circle For Leave Balance


        Column(modifier = Modifier.padding(top = 10.dp))
        {
            Text(text = "Leave Balance",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.black),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            LazyColumn {


                items(leaveItemBalanceList.windowed(size = 2, step = 2, partialWindows = true)) { pairOfBalances ->

                    val countOfRounds = pairOfBalances.size

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
                    ) {
                        pairOfBalances.forEach { balance ->
                            // Calculate consumed and availed percentages
                            val consumedPercentage = balance.availableBalance.toFloat() / balance.totalBalance.toFloat()
                            val availedPercentage = 1 - consumedPercentage

                            Log.d("Balance Detail", "consumedPercentage/availedPercentage - $consumedPercentage/$availedPercentage")

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    DoughnutChart(
                                        modifier = Modifier
                                            .size(150.dp)
                                            .padding(16.dp),
                                        values = listOf(availedPercentage, consumedPercentage),
                                        colors = listOf(
                                            Color.Gray,
                                            Color.Magenta
                                        )
                                    )

                                    Text(
                                        text = balance.availableBalance.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black)
                                    )
                                }
                                Text(
                                    text = balance.leaveName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.paraColor),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth(if (countOfRounds == 2) 1f else 0.5f)
                                        .padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
//Hide 1

            }

// Data  remove mentioned below

        }

        */

    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DoughnutChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    colors: List<Color>
) {

    var startAngle by remember { mutableStateOf(0f) }
    var endAngle by remember { mutableStateOf(0f) }

    LaunchedEffect(values) {
        startAngle = 0f
        endAngle = 0f
        values.forEachIndexed { _, value ->
            endAngle += 360 * value
            animate(
                initialValue = startAngle,
                targetValue = endAngle,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Restart
                )
            ) { value1, _ ->
                startAngle = value1
            }
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            val canvasSize = size.minDimension
            val strokeWidth = canvasSize / 6
            val radius = (canvasSize - strokeWidth) / 2
            val centerOffset = Offset(size.width / 2, size.height / 2)

            values.forEachIndexed { index, value ->
                val sweepAngle = 360 * value

                    drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    topLeft = centerOffset - Offset(radius, radius),
                    size = Size(2 * radius, 2 * radius),
                    useCenter = false,
                    style = Stroke(strokeWidth)
                )
                startAngle += sweepAngle
            }
        }
    }
}



//Hide 1

/*
                items(leaveItemBalanceList){ balance ->


                    // Calculate consumed and availed percentages
                    val consumedPercentage = balance.availableBalance.toFloat() / balance.totalBalance.toFloat()
                    val availedPercentage = 1 - consumedPercentage

                    Log.d("Balance Detail", "consumedPercentage/availedPercentage - $consumedPercentage/$availedPercentage")

                    Row {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(contentAlignment = Alignment.Center){
                                DoughnutChart(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .padding(16.dp),
                                    values = listOf(availedPercentage, consumedPercentage),
                                    colors = listOf(
                                        Color.Gray,
                                        Color.Magenta
                                    )
                                )

                                Text(text = balance.availableBalance.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.black))
                            }
                            Text(text = balance.leaveName,
                                style =MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.paraColor),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp)
                            )
                        }
                    }
                }
*/


//Removed Data Here

/*
            Row {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center){



                        DoughnutChart(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp),
                            values = listOf(0.9f, 0.1f),
                            colors = listOf(
                                Color.Magenta,
                                Color.Gray
                            )
                        )


*/
/*
                        Canvas(modifier = Modifier.size(80.dp)) {

//                                       val canvasSize = size.minDimension
//                                    val center = Offset(size.width / 2f, size.height / 2f)
//                                    val radius = (canvasSize - 4.dp.toPx()) / 2f

                            // Draw the progress tracker
                            drawArc(
                                color = Color(0xffD9D9D9),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx())
                            )

                            // Draw the progress indicator
                            drawArc(
                                color =  Color(0xff0AB39C),
                                startAngle = -90f,
                                sweepAngle = 0.6f * 360f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx())
                            )

                        }
                 */
/*



                Text(text = "05", style = MaterialTheme.typography.titleMedium, color = colorResource(
                    id = R.color.black
                ))
            }
            Text(text = "Casual",
                style =MaterialTheme.typography.titleMedium,
                color = colorResource(id = R.color.paraColor),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp)
            )
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center){



*/
/*
                        Canvas(modifier = Modifier.size(80.dp)) {
//                                    val canvasSize = size.minDimension
//                                    val center = Offset(size.width / 2f, size.height / 2f)
//                                    val radius = (canvasSize - 4.dp.toPx()) / 2f

                            // Draw the progress tracker
                            drawArc(
                                color = Color(0xffD9D9D9),
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx())
                            )

                            // Draw the progress indicator
                            drawArc(
                                color =  Color(0xff299CDB),
                                startAngle = -90f,
                                sweepAngle = 0.6f * 360f,
                                useCenter = false,
                                style = Stroke(width = 4.dp.toPx())
                            )

                        }
*/
/*

                        DoughnutChart(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp),
                            values = listOf(0.2f, 0.8f),
                            colors = listOf(
                                Color.Green,
                                Color.Gray
                            )
                        )


                        Text(text = "05", style = MaterialTheme.typography.titleMedium, color = colorResource(
                            id = R.color.black
                        ))
                    }
                    Text(text = "Casual",
                        style =MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center){


                        DoughnutChart(
                            modifier = Modifier
                                .size(200.dp)
                                .padding(16.dp),
                            values = listOf(0.6f, 0.4f),
                            colors = listOf(
                                Color.Blue,
                                Color.Gray
                            )
                        )

*/
/*
                Canvas(modifier = Modifier.size(80.dp)) {
//                                    val canvasSize = size.minDimension
//                                    val center = Offset(size.width / 2f, size.height / 2f)
//                                    val radius = (canvasSize - 4.dp.toPx()) / 2f

                    // Draw the progress tracker
                    drawArc(
                        color = Color(0xffD9D9D9),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Draw the progress indicator
                    drawArc(
                        color =  Color(0xffF672A7),
                        startAngle = -90f,
                        sweepAngle = 0.6f * 360f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx())
                    )

                }
                */
/*

                        Text(text = "05", style = MaterialTheme.typography.titleMedium, color = colorResource(
                            id = R.color.black
                        ))
                    }
                    Text(text = "Casual",
                        style =MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp)
                    )
                }
            }
            */

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiLeavePreview() {

    val navController = rememberNavController()

    val leaveHistoryDataList = generateLeaveHistoryDataList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Leave", "Attendance") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {


// Create a list to store LeaveItemBalance instances
        val leaveItemBalanceList = mutableListOf<LeaveItemBalance>()

// Add instances to the list
        leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 12, availableBalance = 5, leaveName = "casual"))
        leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 15, availableBalance = 8, leaveName = "sick"))
        leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 20, availableBalance = 12, leaveName = "paid"))
        leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 30, availableBalance = 5, leaveName = "unpaid"))
        leaveItemBalanceList.add(LeaveItemBalance(totalBalance = 30, availableBalance = 0, leaveName = "Bal Zero Check"))



        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, start = 10.dp, end = 10.dp)
        ){

            val flag by remember { mutableIntStateOf(1) }


            @Composable
            fun noData()
            {
                Column(modifier = Modifier.fillMaxWidth(1f))
                {
                    Image(
                        painter = painterResource(id = R.drawable.noleavesvg),
                        contentDescription = null, // Set to null if the image is decorative
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(with(LocalDensity.current) { 150.dp }),
                        contentScale = ContentScale.Fit
                    )

                    Text(text = "No leave history",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 10.dp),
                        style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                    )
                }
            }

            @Composable
            fun uiUpdateLeave()
            {
                Column(modifier = Modifier.fillMaxWidth(1f))
                {
                    Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth(1f))
                    {

                        LazyColumn {

                            // Sort the list by descending order using the Created_Date
                            val sortedList = leaveHistoryDataList.sortedByDescending {
                                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(it.fromDate)
                            }


                            items(sortedList.take(3))
                            {data ->


                                val leaveTypeName = data.leaveTypeName
                                val fromDate = data.fromDate
                                val toDate = data.toDate
                                val noOfDays = data.noOfDays
                                val leaveStatus = data.leaveStatus
                                val leaveUnit = data.leaveUnit


                                val leaveType = if(leaveUnit == "Day") "Days" else "Hours"



                                val bgColor = when (data.leaveStatus) {
                                    "Approved" -> R.color.toolight_green
                                    "Reject" -> R.color.toolight_red
                                    "Pending" -> R.color.toolight_themecolor
                                    "Partially Approved" -> R.color.light_bright_yellow
                                    "Cancelled" -> R.color.light_pink
                                    else -> R.color.white
                                }

                                val statusColor1 = when (data.leaveStatus) {
                                    "Approved" -> R.color.green
                                    "Reject" -> R.color.red
                                    "Pending" -> R.color.themeColor
                                    "Partially Approved" -> R.color.yellow
                                    "Cancelled" -> R.color.pink
                                    else -> R.color.white
                                }


                                val inputFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val outputFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                                val fromDateFormat = inputFormatter.parse(fromDate)
                                val fromDateFormatted = outputFormatter.format(fromDateFormat!!)


                                val toDateFormat = inputFormatter.parse(toDate)
                                val toDateFormatted = outputFormatter.format(toDateFormat!!)



                                Column(modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp))
                                {
                                    Row(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp),horizontalArrangement = Arrangement.SpaceBetween)
                                    {

                                        //Leave Date and Type

                                        Column(modifier = Modifier.padding(top = 2.dp, start = 5.dp)){

                                            //For Date
                                            Row(modifier = Modifier.align(Alignment.Start)){
                                                Column {
                                                    Text(text = "$fromDateFormatted - $toDateFormatted",
                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500),color =  colorResource(id = R.color.black)),
                                                    )
                                                }
                                            }



                                            //For Leave Name
                                            Row(modifier = Modifier
                                                .padding(top = 3.dp)
                                                .align(Alignment.Start)){
                                                Column {
                                                    if (leaveTypeName.length > 15)
                                                    {

                                                        PlainTooltipBox(
                                                            tooltip = {
                                                                Text(
                                                                    text = leaveTypeName,
                                                                    style = TextStyle(
                                                                        fontFamily = poppins_font,
                                                                        fontSize = 13.sp,
                                                                        fontWeight = FontWeight(500),
                                                                    ),
                                                                )
                                                            }
                                                        ) {
                                                            Text(
                                                                text = leaveTypeName.take(15) + "..." + " $noOfDays $leaveType",
                                                                style = TextStyle(
                                                                    fontFamily = poppins_font,
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight(500),
                                                                    color = colorResource(id = R.color.paraColor)
                                                                ),
                                                                modifier = Modifier.tooltipTrigger()
                                                            )
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Text(
                                                            text = "$leaveTypeName $noOfDays $leaveType",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 12.sp, fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.paraColor)
                                                            ),
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        //Leave Status

                                        Column(modifier = Modifier.padding(top = 2.dp, end = 5.dp))
                                        {

                                            Button(onClick = { },
                                                colors = ButtonDefaults.buttonColors(colorResource(id = bgColor)),
                                                shape = RoundedCornerShape(20),
                                                modifier = Modifier.align(Alignment.End)
                                            ) {


                                                if (leaveStatus.length > 9) {

                                                    PlainTooltipBox(
                                                        tooltip = {
                                                            Text(
                                                                text = leaveStatus,
                                                                style = TextStyle(fontFamily = poppins_font, fontSize = 12.sp, fontWeight = FontWeight(500)),
                                                            )
                                                        }
                                                    ) {
                                                        Text(
                                                            text = leaveStatus.take(10) + "..." ,
                                                            style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                            modifier = Modifier
                                                                .padding(
                                                                    top = 2.dp,
                                                                    bottom = 2.dp,
                                                                    start = 4.dp,
                                                                    end = 4.dp
                                                                )
                                                                .tooltipTrigger())
                                                    }
                                                } else {
                                                    Text(
                                                        text = leaveStatus,
                                                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, start = 4.dp, end = 4.dp),
                                                        style = TextStyle(fontFamily = poppins_font, fontSize = 11.sp, fontWeight = FontWeight(500),color =  colorResource(id = statusColor1)),
                                                    )
                                                }
                                            }

                                        }

                                    }

                                    //Divider

                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 5.dp),
                                        color = colorResource(id = R.color.divider)
                                    )

                                }

                            }
                        }
                    }

                    //View All Card


                    Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .clickable { navController.navigate("leaveHistory") })
                    {
                        Column(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 5.dp, bottom = 10.dp)
                            , horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "View all", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                id = R.color.themeColor
                            ))
                        }
                    }
                }
            }


            //Leave History Detail

            Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(1f))
            {

                when (flag)
                {

                    1 -> {
                        uiUpdateLeave()
                    }
                    else -> {
                        noData()
                    }

                }

            }

            //Button For Request Leave New

            Button(onClick = { navController.navigate("applyLeave") }, //LeaveApply
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(12.dp),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 16.dp)
            ) {
                Text(text = "Request Leave",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 13.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.white)
                    ),)
            }

            // Correct Code For Leave Balance
            //Circle For Leave Balance

            Column(modifier = Modifier.padding(top = 10.dp))
            {
                Text(text = "Leave Balance",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(id = R.color.black),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                LazyColumn {


                    items(leaveItemBalanceList.windowed(size = 2, step = 2, partialWindows = true)) { pairOfBalances ->

                        val countOfRounds = pairOfBalances.size

                        Row(modifier = Modifier.fillMaxWidth()) {
                            pairOfBalances.forEach { balance ->
                                // Calculate consumed and availed percentages
                                val consumedPercentage = balance.availableBalance.toFloat() / balance.totalBalance.toFloat()
                                val availedPercentage = 1 - consumedPercentage


                                Column(modifier = Modifier.weight(1f))
                                {
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth((if (countOfRounds == 2) 1f else 0.5f))) {

                                        DoughnutChart(
                                            modifier = Modifier.size(150.dp).padding(16.dp),
                                            values = listOf(availedPercentage, consumedPercentage),
                                            colors = listOf(
                                                Color.Gray,
                                                colorResource(id = R.color.light_bright_blue),
                                                colorResource(id = R.color.light_bright_green),
                                                colorResource(id = R.color.light_bright_pink),
                                                colorResource(id = R.color.light_bright_yellow),
                                                colorResource(id = R.color.light_bright_red)
                                            )
                                        )

                                        Text(
                                            text = balance.availableBalance.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                    Text(
                                        text = balance.leaveName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.paraColor),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth(if (countOfRounds == 2) 1f else 0.5f)
                                            .padding(top = 10.dp)
                                    )
                                }
                            }
                        }
                    }
    //Hide 1

                }

    // Data  remove mentioned below

            }
        }
    }

}



@Preview(showBackground = true)
@Composable
fun DoughnutChartPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        DoughnutChart(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            values = listOf(0.4f, 0.3f, 0.2f, 0.1f),
            colors = listOf(
                Color.Red,
                Color.Green,
                Color.Blue,
                Color.Gray
            )
        )
    }
}


