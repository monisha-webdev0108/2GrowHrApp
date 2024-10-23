package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.DrawerContent
import com.payroll.twogrowhr.components.TopBar
import com.payroll.twogrowhr.components.TopBarPreview
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.circularProgression1
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.getLoginDetails
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LoanDetailViewModel
import com.payroll.twogrowhr.viewModel.MainViewModel
import com.payroll.twogrowhr.viewModel.statusLoading
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Finance(navController: NavController,viewModel : MainViewModel, loanViewModel: LoanDetailViewModel) {

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
    { Finance_Screen(navController = navController, loanViewModel = loanViewModel) }

}
@SuppressLint("PrivateResource")
@Composable
fun Finance_Screen(navController: NavController, loanViewModel: LoanDetailViewModel)
{

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }


    val empID = userViewModel.getSFCode()
    val org = userViewModel.getOrg()


    var loanApplyStatus by remember { mutableStateOf("") }
    var loanEnableStatus by remember { mutableStateOf("") }


    val loanEnableData= loanViewModel.loanEnableData.collectAsState().value
    val loanEnableDataStatus = loanEnableData.firstOrNull()
    loanEnableStatus = loanEnableDataStatus?.flag.toString()


    val loanMappedData= loanViewModel.loanMappedData.collectAsState().value
    val loanMappedDataStatus = loanMappedData.firstOrNull()

    loanApplyStatus = loanMappedDataStatus?.flag.toString()




    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)

    ){

        if(statusLoading.value)
        {
            circularProgression1(statusLoading.value)
        }

        Text(
            text = "Loan" ,
            style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                color = colorResource(id = R.color.themeColor)
            ),
            modifier = Modifier.padding(bottom = 15.dp))


        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Card(modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate("Loan") },
                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_theme_color)) // Set the border of the card
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Box(modifier = Modifier
                        .size(48.dp)
                        .background(
                            colorResource(id = R.color.light_bright_green),
                            shape = RoundedCornerShape(8.dp)
                        ), // Adjust the corner radius as needed
                        contentAlignment = Alignment.Center)
                    {
                        Icon(painterResource(id =R.drawable.loan )  , contentDescription ="loan", tint = colorResource(id = R.color.green) , modifier = Modifier.size(22.dp))
                    }
                    Column {
                        Text(text = "Loan details", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))


                        Text(text = "View all Loan related Details",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 11.sp,
                                fontWeight = FontWeight(500),
                                color = colorResource(id = R.color.paraColor) ),
                            modifier = Modifier.padding(top = 10.dp))


                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
        }






        if(loanEnableStatus == "1")
        {
            Text(
                text = "Request" ,
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.padding(bottom = 15.dp))


            if (showDialog) {


                Dialog(onDismissRequest = {
                    showDialog = false
                },)
                {

                    Box(
                        modifier = Modifier
                            .padding(40.dp)
                            .fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = colorResource(id = R.color.backgroundColor),
                                    shape = RoundedCornerShape(percent = 3)
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Row(modifier = Modifier
                                .padding(top = 25.dp, bottom = 12.dp, start = 20.dp, end = 20.dp)
                                .fillMaxWidth())
                            {
                                Text(
                                    text = "Contact Admin to configure Loan Request",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.black),
                                    textAlign = TextAlign.Center // Set the desired alignment (e.g., TextAlign.Center)
                                )

                            }

                            // buttons

                            Button(onClick = {
                                showDialog = false
                            },
                                modifier = Modifier.padding(bottom = 25.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                Text(text = "Close", color = colorResource(id = R.color.white), fontSize = 14.sp )
                            }
                        }

                    }

                }


            }

            var flag by remember { mutableIntStateOf(0) }

            val loadingStatus = loanViewModel.loadingStatus2

            flag = loanViewModel.flag2

            val loading by remember { mutableStateOf(false) }

            if(loading && !loadingStatus)
            {
                circularProgression()
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
            {

                Card(modifier = Modifier
                    .weight(1f)
                    .clickable {

                        statusLoading.value = true

                        loanViewModel.getLoanMappedDetails(
                            navController,
                            context,
                            empID,
                            org.toString()
                        ) { count ->

                            when (count.toInt() > 0) {
                                true -> {
                                    coroutineScope.launch { navController.navigate("ApplyLoan") }
                                }

                                false -> {
                                    showDialog = true
                                }

                            }

                        }

                    },
                    colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_theme_color)) // Set the border of the card
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(
                                colorResource(id = R.color.light_bright_pink),
                                shape = RoundedCornerShape(8.dp)
                            ), // Adjust the corner radius as needed
                            contentAlignment = Alignment.Center)
                        {
                            Icon(painterResource(id =R.drawable.loan_icon )  , contentDescription ="loan_icon", tint = colorResource(id = R.color.pink) , modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(text = "Apply Loan", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))

                            Text(text = "Apply & Manage all Loan related request",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor) ),
                                modifier = Modifier.padding(top = 10.dp))

                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
            }

        }



    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiFinancePreview() {

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


        val loanEnableStatus by remember { mutableStateOf("1") }


        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)

        ){


            Text(
                text = "Loan" ,
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.padding(bottom = 15.dp))


            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
            {

                Card(modifier = Modifier
                    .weight(1f)
                    .clickable { navController.navigate("Loan") },
                    colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                    border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_theme_color)) // Set the border of the card
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        Box(modifier = Modifier
                            .size(48.dp)
                            .background(
                                colorResource(id = R.color.light_bright_green),
                                shape = RoundedCornerShape(8.dp)
                            ), // Adjust the corner radius as needed
                            contentAlignment = Alignment.Center)
                        {
                            Icon(painterResource(id =R.drawable.loan )  , contentDescription ="loan", tint = colorResource(id = R.color.green) , modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text(text = "Loan details", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))


                            Text(text = "View all Loan related Details",
                                style = TextStyle(
                                    fontFamily = poppins_font,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight(500),
                                    color = colorResource(id = R.color.paraColor) ),
                                modifier = Modifier.padding(top = 10.dp))


                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
            }


            if(loanEnableStatus == "1")
            {
                Text(
                    text = "Request" ,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600),
                        color = colorResource(id = R.color.themeColor)
                    ),
                    modifier = Modifier.padding(bottom = 15.dp))

                Row(Modifier.fillMaxWidth().padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {

                    Card(modifier = Modifier
                        .weight(1f)
                        .clickable { },
                        colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                        border = BorderStroke(1.dp, color = colorResource(id = R.color.dark_theme_color)) // Set the border of the card
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Box(modifier = Modifier
                                .size(48.dp)
                                .background(
                                    colorResource(id = R.color.light_bright_pink),
                                    shape = RoundedCornerShape(8.dp)
                                ), // Adjust the corner radius as needed
                                contentAlignment = Alignment.Center)
                            {
                                Icon(painterResource(id =R.drawable.loan_icon )  , contentDescription ="loan_icon", tint = colorResource(id = R.color.pink) , modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text(text = "Apply Loan", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 10.dp))

                                Text(text = "Apply & Manage all Loan related request",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.paraColor) ),
                                    modifier = Modifier.padding(top = 10.dp))

                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(colorResource(id = com.google.android.material.R.color.mtrl_btn_transparent_bg_color))) {}
                }
            }
        }
    }
}