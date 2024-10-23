package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.PayslipMonthData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.downloadImageWithProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.viewModel.PaySlipViewModel
import java.net.URI
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Payslip(navController: NavController, paySlipViewModel: PaySlipViewModel) {

    Constant.AppTheme {
        AppScaffold1(
            topBarContent = { TopBarBackNavigation(navController = navController, title = "Payslip", "Reports") },
            bottomBarContent = { },
            onBack = { navController.navigateUp() }
        )
        { Payslip_Screen(navController = navController, paySlipViewModel = paySlipViewModel) }
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Payslip_Screen(navController: NavController, paySlipViewModel: PaySlipViewModel)
{


    val context = LocalContext.current

    var expanded by remember {mutableStateOf(false)}

    var expandedDownload by remember {mutableStateOf(false)}

    var selectedIndex by remember { mutableStateOf(0) }

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    var selectedYear by remember { mutableStateOf(currentYear.toString()) }

    val paidYears = listOf(
        "$currentYear-${currentYear + 1}",
        "${currentYear - 1}-$currentYear"
    )


// Fetch and update the payslip list when entering the page

    var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

    val loadingStatus = paySlipViewModel.loadingStatus

    flag = paySlipViewModel.flag

    var loading by remember { mutableStateOf(true) }

    val monthList = paySlipViewModel.paidMonthList.collectAsState()

    Log.d("Payslip... ", "MonthListDetails : ${monthList.value}")


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
    ){

        Text(text = "Select Financial Year", color = colorResource(id = R.color.paraColor), style = MaterialTheme.typography.titleSmall)

        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .animateContentSize(
                animationSpec = tween(
                    delayMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(top = 10.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
            shape = RoundedCornerShape(5.dp),
            onClick = {
                expanded = !expanded
            }
        ) {
            Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(text = paidYears[selectedIndex], color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium)
                    }
                    Column {
                        Icon(
                            painterResource(id = R.drawable.baseline_keyboard_arrow_down_24) ,
                            contentDescription ="",
                            tint = colorResource(id = R.color.black)
                        )
                    }
                }
            }
            if(expanded)
            {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .clickable { expanded = true }
                ) {

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        paidYears.forEachIndexed { index, year ->
                            DropdownMenuItem(onClick = {
                                selectedIndex = index
                                selectedYear = paidYears[selectedIndex]

                                val parts = selectedYear.split("-")
                                val year1 = parts.firstOrNull()
                                paySlipViewModel.getPaidMonthList(navController,context,year1.toString())
                                expanded = false
                            }) {
                                Text(text = year)
                            }
                        }
                    }
                }
            }
        }


//FOR RECEIVED LIST RESPONSE

        val loadingCircular = paySlipViewModel.loadingStatus

        if(loadingCircular)
        {
            circularProgression()
        }


        val showLoading = remember { mutableStateOf(false) }

        if (showLoading.value)
        {
            circularProgression()
        }

//LOGIC TO DISPLAY THE LIST

        @SuppressLint("NewApi")
        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun uiUpdate()
        {

            val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

            LazyColumn {
                itemsIndexed(monthList.value) { index, month ->
                    val isExpanded = expandedStates[index] ?: false

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            colorResource(id = R.color.light_bright_red),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pdf_icon),
                                        contentDescription = "pdf icon",
                                        tint = colorResource(id = R.color.red),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = "${month.monthName} ${month.year}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black)
                                    )
                                }

                                Icon(
                                    painter = painterResource(id = R.drawable.three_dot),
                                    contentDescription = "more",
                                    tint = colorResource(id = R.color.black),
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable {
                                            expandedStates[index] = !isExpanded
                                        }
                                )
                            }
                        }


                        // Extract the first URL, month, and year from the list (assuming there's at least one item)
                        val urlPdf = month.urlPdf
                        val fileName = "${month.month}-${month.year} payslip"

                        if (isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.End)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Card(
                                        modifier = Modifier.clickable {
                                            expandedStates[index] = false
                                        },
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                    ) {
                                        DropdownMenu(
                                            expanded = isExpanded,
                                            onDismissRequest = {
                                                expandedStates[index] = false
                                            },
                                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                        ) {
                                            DropdownMenuItem(onClick = {
                                                expandedStates[index] = false
                                                navController.navigate(
                                                    "${Screen.ViewPayslip.route}?month=${month.month}&year=${month.year}"
                                                )

                                            }) {
                                                Text(
                                                    text = "View",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.black)
                                                )
                                            }
                                            Divider(
                                                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp),
                                                color = colorResource(id = R.color.lightshade)
                                            )
                                            DropdownMenuItem(onClick = {
                                                expandedStates[index] = false
                                                Log.d("Payslip", "URL: ${month.urlPdf}")
/*                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(month.url))
                                                context.startActivity(intent)*/



                                                if (urlPdf.isNotEmpty())
                                                {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                        downloadImageWithProgression(navController, urlPdf, "$fileName.pdf", "ViewPayslip", showLoading, context)
                                                    }
                                                }
                                                else
                                                {
                                                    Log.e("DownloadButton", "URL is empty or invalid")
                                                }

                                            }) {
                                                Text(
                                                    text = "Download",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = colorResource(id = R.color.black)
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



// LOGIC TO DISPLAY THE UI

        if(loadingStatus && !paySlipViewModel.loadingStatus)
        {
            loading = true
        }
        else
        {
            loading = false

            if(monthList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        paySlipViewModel.getPaidMonthList(navController,context,currentYear.toString())
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
                when (flag)
                {
                    0 -> {
                        loading = true
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
fun uiPayslipListPreview() {

    val navController = rememberNavController()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Payslip", "Reports") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {
        var selectedIndex by remember { mutableStateOf(0) }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        var selectedYear by remember { mutableStateOf(currentYear.toString()) }

        val paidYears = listOf(
            "$currentYear-${currentYear + 1}",
            "${currentYear - 1}-$currentYear"
        )

        var expanded = false


// Fetch and update the payslip list when entering the page

        val flag by remember { mutableIntStateOf(1) }

        val monthList = generatePayslipList()

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
        ){

            Text(text = "Select Financial Year", color = colorResource(id = R.color.paraColor), style = MaterialTheme.typography.titleSmall)

            Card(modifier = Modifier
                .fillMaxWidth(1f)
                .animateContentSize(
                    animationSpec = tween(
                        delayMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .padding(top = 10.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                shape = RoundedCornerShape(5.dp),
                onClick = {}
            ) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = paidYears[selectedIndex], color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium)
                        }
                        Column {
                            Icon(
                                painterResource(id = R.drawable.baseline_keyboard_arrow_down_24) ,
                                contentDescription ="",
                                tint = colorResource(id = R.color.black)
                            )
                        }
                    }
                }
                if(expanded)
                {
                    Card(modifier = Modifier.fillMaxWidth(1f)) {

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {},
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            paidYears.forEachIndexed { index, year ->
                                DropdownMenuItem(onClick = {
                                    selectedIndex = index
                                    selectedYear = paidYears[selectedIndex]
                                    expanded = false
                                }) {
                                    Text(text = year)
                                }
                            }
                        }
                    }
                }
            }



//LOGIC TO DISPLAY THE LIST

            @Composable
            fun uiUpdate()
            {

                val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

                LazyColumn {
                    itemsIndexed(monthList) { index, month ->
                        val isExpanded = expandedStates[index] ?: false

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                colorResource(id = R.color.light_bright_red),
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.pdf_icon),
                                            contentDescription = "pdf icon",
                                            tint = colorResource(id = R.color.red),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .weight(1f),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "${month.monthName} ${month.year}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.three_dot),
                                        contentDescription = "more",
                                        tint = colorResource(id = R.color.black),
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clickable {
                                                expandedStates[index] = !isExpanded
                                            }
                                    )
                                }
                            }

                            if (isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.End)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Card(
                                            modifier = Modifier.clickable {
                                                expandedStates[index] = false
                                            },
                                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                                        ) {
                                            DropdownMenu(
                                                expanded = isExpanded,
                                                onDismissRequest = {
                                                    expandedStates[index] = false
                                                },
                                                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                            ) {
                                                DropdownMenuItem(onClick = {
                                                    expandedStates[index] = false
                                                    navController.navigate(
                                                        "${Screen.ViewPayslip.route}?month=${month.month}&year=${month.year}"
                                                    )

                                                }) {
                                                    Text(
                                                        text = "View",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = colorResource(id = R.color.black)
                                                    )
                                                }
                                                Divider(
                                                    modifier = Modifier.padding(top = 3.dp, bottom = 3.dp),
                                                    color = colorResource(id = R.color.lightshade)
                                                )
                                                DropdownMenuItem(onClick = {
                                                    expandedStates[index] = false
                                                }) {
                                                    Text(
                                                        text = "Download",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = colorResource(id = R.color.black)
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

            when (flag)
            {

                1 -> {
                    uiUpdate()
                }
                else -> {
                    noDataView()
                }

            }

        }
    }

}


fun generatePayslipList(): List<PayslipMonthData>
{
    return listOf(
        PayslipMonthData(month = 8, monthName  = "August", year  = 2024, url = "http://testing.2growhr.io/SalaryTemplate/ViewPayslip?vBirds=RU1QMTI3NDk=&month=8&year_need=2023&st=1&tid=1&org=113&subdiv=530&div=249", Sub_Div_Id = 530, urlPdf = "http://testing.2growhr.io/SalaryTemplate/Conpdf?emp=RU1QMTI3NDk=&mon=8&yr=2023&Empcode=&st=1&tid=1&org=113&subdiv=530 &div=249"),
        PayslipMonthData(month = 9, monthName  = "September", year  = 2024, url = "http://testing.2growhr.io/SalaryTemplate/ViewPayslip?vBirds=RU1QMTI3NDk=&month=9&year_need=2023&st=1&tid=1&org=113&subdiv=530&div=249", Sub_Div_Id = 530, urlPdf = "http://testing.2growhr.io/SalaryTemplate/Conpdf?emp=RU1QMTI3NDk=&mon=9&yr=2023&Empcode=&st=1&tid=1&org=113&subdiv=530 &div=249"),
        PayslipMonthData(month = 11, monthName  = "November", year  = 2024, url = "http://testing.2growhr.io/SalaryTemplate/ViewPayslip?vBirds=RU1QMTI3NDk=&month=11&year_need=2023&st=1&tid=1&org=113&subdiv=530&div=249", Sub_Div_Id = 530, urlPdf = "http://testing.2growhr.io/SalaryTemplate/Conpdf?emp=RU1QMTI3NDk=&mon=11&yr=2023&Empcode=&st=1&tid=1&org=113&subdiv=530 &div=249"),
    )
}


