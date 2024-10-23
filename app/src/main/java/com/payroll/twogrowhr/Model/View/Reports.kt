package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.DrawerContent
import com.payroll.twogrowhr.components.TopBar
import com.payroll.twogrowhr.components.TopBarPreview
import com.payroll.twogrowhr.getLoginDetails
import com.payroll.twogrowhr.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Report(navController: NavController, viewModel : MainViewModel) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val jsonObject = getLoginDetails()
    var divisionName = ""

    if (jsonObject != null)
    {
        divisionName = jsonObject.getString("Division_Name")
    }

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBar(navController = navController, title = divisionName,drawerState = drawerState) },
        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { Report_Screen(navController = navController) }

}
@SuppressLint("PrivateResource")
@Composable
fun Report_Screen(navController: NavController) {

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)

    ){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

        //PAYSLIP

            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(
                            colorResource(id = R.color.light_bright_green),
                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center)
                    {
                        Icon(painterResource(id =R.drawable.payslip_icon )  , contentDescription ="payslip_icon", tint = colorResource(
                            id = R.color.green
                        ) , modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text(text = "Payslip", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                        Button(onClick = { navController.navigate("payslip")}, modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_green)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                            Text(text = "View", color = colorResource(id = R.color.green), fontSize = 14.sp )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp)) // Added space between cards


        //SALARY DETAILS

            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(
                            colorResource(id = R.color.light_bright_blue),
                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center)
                    {
                        Icon(painterResource(id =R.drawable.salary )  , contentDescription ="payslip_icon", tint = colorResource(
                            id = R.color.blue
                        ), modifier = Modifier.size(22.dp))
                    }

                    Column {
                        Text(text = "Salary details", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                        Button(onClick = { navController.navigate("salary")}, modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_blue)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                            Text(text = "View", color = colorResource(id = R.color.blue), fontSize = 14.sp )
                        }
                    }

                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {

            //PAYSLIP

            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(
                            colorResource(id = R.color.light_bright_pink),
                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center)
                    {
                        Icon(painterResource(id =R.drawable.tds )  , contentDescription ="payslip_icon", tint = colorResource(id = R.color.pink) , modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text(text = "TDS", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                        Button(onClick = { navController.navigate("formtds")}, modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_pink)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                            Text(text = "View", color = colorResource(id = R.color.pink), fontSize = 14.sp )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiReportsPreview() {

    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarPreview() },
        drawerContent = {},
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)

        ){
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                //PAYSLIP

                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(colorResource(id = R.color.light_bright_green), shape = CircleShape),
                            contentAlignment = Alignment.Center)
                        {
                            Icon(painterResource(id =R.drawable.payslip_icon )  , contentDescription ="payslip_icon", tint = colorResource(
                                id = R.color.green
                            ) , modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(text = "Payslip", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                            Button(onClick = { navController.navigate("payslip")}, modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_green)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                Text(text = "View", color = colorResource(id = R.color.green), fontSize = 14.sp )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp)) // Added space between cards


                //SALARY DETAILS

                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(
                                colorResource(id = R.color.light_bright_blue),
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center)
                        {
                            Icon(painterResource(id =R.drawable.salary )  , contentDescription ="payslip_icon", tint = colorResource(id = R.color.blue), modifier = Modifier.size(22.dp))
                        }

                        Column {
                            Text(text = "Salary details", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                            Button(onClick = { }, modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_blue)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                Text(text = "View", color = colorResource(id = R.color.blue), fontSize = 14.sp )
                            }
                        }

                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(top = 15.dp), horizontalArrangement = Arrangement.SpaceBetween) {

                //PAYSLIP

                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = R.color.white))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(
                                colorResource(id = R.color.light_bright_pink),
                                shape = CircleShape
                            ),
                            contentAlignment = Alignment.Center)
                        {
                            Icon(painterResource(id =R.drawable.tds )  , contentDescription ="payslip_icon", tint = colorResource(id = R.color.pink) , modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(text = "TDS", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))
                            Button(onClick = { navController.navigate("formtds")}, modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(1f), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_bright_pink)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                Text(text = "View", color = colorResource(id = R.color.pink), fontSize = 14.sp )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
            }
        }
    }

}