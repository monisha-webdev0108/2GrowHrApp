package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenu
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.viewModel.TdsFormViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormTds(navController: NavController, tdsFormViewModel: TdsFormViewModel) {


    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Constant.AppTheme {
        AppScaffold1(
            topBarContent = { TopBarBackNavigation(
                navController = navController,
                title = "TDS Form",
                "Reports"
            ) },
            bottomBarContent = {},
            onBack = { navController.navigateUp() }
        )
        { FormTds_Screen(navController = navController, tdsFormViewModel = tdsFormViewModel) }
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTds_Screen(navController: NavController,tdsFormViewModel: TdsFormViewModel)
{

    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

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

    val loadingStatus = tdsFormViewModel.loadingStatus

    flag = tdsFormViewModel.flag

    var loading by remember { mutableStateOf(false) }

    val monthList = tdsFormViewModel.tdsMonthList.collectAsState()

    Log.d("Payslip... ", "MonthListDetails : ${monthList.value}")


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
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
                        Icon(painterResource(id = R.drawable.baseline_keyboard_arrow_down_24) , contentDescription ="", tint = colorResource(id = R.color.black))
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
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        paidYears.forEachIndexed { index, year ->
                            DropdownMenuItem(onClick = {
                                selectedIndex = index
                                selectedYear = paidYears[selectedIndex]

                                val parts = selectedYear.split("-")
                                val year1 = parts.firstOrNull()
                                tdsFormViewModel.getTdsFormMonthList(navController,context,year1.toString())
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

        val loadingCircular = tdsFormViewModel.loadingStatus

        if(loadingCircular)
        {
            circularProgression()
        }

//LOGIC TO DISPLAY THE LIST

        @Composable
        fun uiUpdate()
        {

            LazyColumn {
                items(monthList.value){month ->
                    Card(modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {

                                    Box(modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            colorResource(id = R.color.light_bright_red),
                                            shape = CircleShape
                                        ),
                                        contentAlignment = Alignment.Center) {
                                        Icon(
                                            painterResource(id = R.drawable.pdf_icon )  , contentDescription ="pdf icon", tint = colorResource(
                                            id = R.color.red
                                        ), modifier = Modifier.size(22.dp) )
                                    }
                                }

                                Column(modifier = Modifier.padding(start = 10.dp), // Occupy remaining space
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {


                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                                    {
                                        Text(text = month.monthName + " " + month.year,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.black),
                                            modifier = Modifier.padding(top = 10.dp)
                                        )
                                    }

                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Button(onClick = {

                                        Log.d("Payslip", "URL : ${month.url} ")

                                        // Create an Intent with ACTION_VIEW and the URL
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(month.url))

                                        // Start the activity
                                        context.startActivity(intent)

                                    },
                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_red)),
                                        shape = RoundedCornerShape(20),
                                        contentPadding = PaddingValues(top=4.dp, bottom = 4.dp, start = 7.dp, end = 7.dp),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {

                                        Text(text = "View", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.red)))
                                    }
//                                Icon(painterResource(id =R.drawable.three_dot)  , contentDescription ="three-dot", tint = colorResource(
//                                    id = R.color.black
//                                ), modifier = Modifier
//                                    .size(22.dp)
//                                    .align(Alignment.End)
//                                    .clickable { mDisplayMenu = !mDisplayMenu }
//
//                                )
//                                DropdownMenu(
//                                    expanded = mDisplayMenu,
//                                    onDismissRequest = { mDisplayMenu = false }
//                                ) {
//
//                                    // Creating dropdown menu item, on click
//                                    // would create a Toast message
//                                    DropdownMenuItem(onClick = { Toast.makeText(mContext, "Download", Toast.LENGTH_SHORT).show() }) {
//                                        Text(text = "Download")
//                                    }
//
//
//                                }
                                }

                            }

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

            if(monthList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        Log.d("Payslip..","Payslip list is empty.... flag is 0")
                        loading = true
                    }
                    1 -> {
                        Log.d("Payslip..","Payslip list is empty.... but flag is 1")
                        tdsFormViewModel.getTdsFormMonthList(navController , context,currentYear.toString())
                    }
                    2 -> {
                        Log.d("Payslip..","Payslip list is empty.... flag is 2")
                        noDataView()
                    }
                    3 -> {
                        Log.d("Payslip..","Payslip list is empty.... flag is 3")
                        exceptionScreen()
                    }
                    else -> {
                        Log.d("Payslip..","Payslip list is empty.... flag is else")
                        Constant.showToast(context,"Please try again later...!")
                    }
                }

            }
            else
            {
                when (flag)
                {
                    0 -> {
                        Log.d("Payslip..","Payslip list is not empty else.... flag is 0")
                        loading = true
                    }
                    1 -> {
                        Log.d("Payslip..","Payslip list is not empty else.... flag is 1")
                        uiUpdate()
                    }
                    2 -> {
                        Log.d("Payslip..","Payslip list is not empty else.... flag is 2")
                        noDataView()
                    }

                    3 -> {
                        Log.d("Payslip..","Payslip list is not empty.... flag is 3")
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