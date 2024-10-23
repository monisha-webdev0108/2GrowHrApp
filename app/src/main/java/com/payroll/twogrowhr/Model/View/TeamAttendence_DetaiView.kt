package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun teamAttendanceDetailView(navController: NavController) {
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
    { teamAttendanceDetailViewScreen(navController = navController) }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun teamAttendanceDetailViewScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp,)
    ){
        Scaffold(
            content = { teamAttendanceDetail() },
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun teamAttendanceDetail() {

    val calendarState = rememberSheetState()
    val calendarStateEnd = rememberSheetState()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedEndDate = remember {mutableStateOf(LocalDate.now()) }
    var text by remember {mutableStateOf("") }
    var textCheckOut by remember {mutableStateOf("") }
    // Declaring a Boolean value to store bottom sheet collapsed state
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    // Declaring Coroutine scope
    val coroutineScope = rememberCoroutineScope()
    CalendarDialog(state = calendarState, selection = CalendarSelection.Date { date ->
        selectedDate.value = date
        calendarState.hide()
    })
    CalendarDialog(state = calendarStateEnd, selection = CalendarSelection.Date { date ->
        selectedEndDate.value = date
        calendarState.hide()
    })
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.white))
            ) {
                Column(
                    Modifier.fillMaxSize(),

                    ) {
                    Row(modifier = Modifier.fillMaxWidth(1f)) {
                        Column(modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                        ) {
                            Text(
                                text = "Regularize",
                                style= MaterialTheme.typography.titleLarge,
                                color = colorResource(id = R.color.themeColor), modifier = Modifier.padding(10.dp)
                            )
                        }
                        Column(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(1f),
                        ) {
                            Image(
                                painterResource(id =R.drawable.close )  ,
                                contentDescription ="close",
                                modifier = Modifier
                                    .size(35.dp)
                                    .align(Alignment.End)
                                    .padding(end = 10.dp,)
                                    .clickable {
                                        coroutineScope.launch {
                                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                bottomSheetScaffoldState.bottomSheetState.expand()
                                            } else {
                                                bottomSheetScaffoldState.bottomSheetState.collapse()
                                            }
                                        }

                                    },
                                )
                            }
                        }
                    Divider(color = colorResource(id = R.color.divider))
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        Text(text = "Check-IN",
                            style = MaterialTheme.typography.titleSmall,
                            color = colorResource(id = R.color.paraColor)
                        )
                        Box( modifier = Modifier.padding(bottom = 10.dp)){
                            val containerColor = colorResource(id = R.color.backgroundColor)
                            TextField(
                                value =text ,
                                onValueChange = { text = it },

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = containerColor,
                                    unfocusedContainerColor = containerColor,
                                    disabledContainerColor = containerColor,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                            )
                            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                Box(modifier = Modifier
                                    .background(
                                        color = colorResource(id = R.color.themeColor),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .size(58.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(painterResource(id = R.drawable.clock_1) ,
                                        contentDescription ="password" ,
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier
                                            .size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        Text(text = "Check-OUT",
                            style = MaterialTheme.typography.titleSmall,
                            color = colorResource(id = R.color.paraColor)
                        )
                        Box( modifier = Modifier.padding(bottom = 10.dp)){
                            TextField(
                                value =textCheckOut ,
                                onValueChange = { textCheckOut = it },

                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorResource(id = R.color.backgroundColor),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                            )
                            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                Box(modifier = Modifier
                                    .background(
                                        color = colorResource(id = R.color.themeColor),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .size(58.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(painterResource(id = R.drawable.clock_1) ,
                                        contentDescription ="password" ,
                                        tint = colorResource(id = R.color.white),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }



                        }
                    }
                    Column(modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    ) {
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp)
                                .background(color = colorResource(id = R.color.backgroundColor)),

                            maxLines = 5,
                            textStyle = MaterialTheme.typography.titleMedium,
                            placeholder = { Text(text = "Remarks") },
                            singleLine = false,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = colorResource(R.color.white),
                                focusedBorderColor = colorResource(id = R.color.themeColor),
                                cursorColor = colorResource(id = R.color.black)
                            )
                        )
                    }
                    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)) {
                        Button(onClick = { /*TODO*/ }, modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(text = "Regularize", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = Color.Black,
        sheetElevation = 0.dp,
        sheetShape = MaterialTheme.shapes.large
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.backgroundColor))
                .padding(start = 20.dp, end = 20.dp),

            ){
            Row() {
                Column() {
                    Image(
                        painterResource(id = R.drawable.capa)  , contentDescription = "profile img", modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                    )
                }
                Column() {
                    Text(text = "Divya",
                        style= MaterialTheme.typography.titleLarge,
                        color = colorResource(id = R.color.black),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(text = "Software developer",
                        style= MaterialTheme.typography.titleMedium,
                        color = colorResource(id = R.color.paraColor),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Button(
                    onClick = { calendarState.show() },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade))
                ) {
                    Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),) {
                        Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column() {
                                Icon(painterResource(id = R.drawable.attendance)  , contentDescription ="attendance",
                                    tint = colorResource(id = R.color.themeColor), modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.clickable { calendarState.show() }) {
                                Text(
                                    text = selectedDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    )
                                )
                            }
                            Column() {
                                Icon(painterResource(id = R.drawable.baseline_arrow_forward_ios_24)  ,
                                    contentDescription ="front",
                                    tint = colorResource(id = R.color.themeColor)
                                )
                            }
                            Column(modifier = Modifier.clickable { calendarStateEnd.show() }) {
                                Text(
                                    text = selectedEndDate.value.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Divider(color = colorResource(id = R.color.lightthemecolor), modifier = Modifier.padding(top = 10.dp))
            Column(Modifier.padding(10.dp)) {
                Row() {
                    Column(modifier = Modifier.weight(0.5f)) {
                        Text(text = "Date", style = MaterialTheme.typography.titleSmall, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Check IN", style = MaterialTheme.typography.titleSmall, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Check OUT", style = MaterialTheme.typography.titleSmall, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                    Column(modifier = Modifier.weight(0.5f)) {
                        Text(text = "Total", style = MaterialTheme.typography.titleSmall, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                    Column(modifier = Modifier.weight(0.1f)) {
                        Text(text = "", style = MaterialTheme.typography.titleSmall, color = colorResource(
                            id = R.color.themeColor
                        ))
                    }
                }
            }
            Divider(color = colorResource(id = R.color.lightthemecolor), modifier = Modifier.padding(bottom = 10.dp))
//            CHECK IN & CHECK OUT DETAILS CARD
            LazyColumn(){
                items(10){
                    var expanded by remember {mutableStateOf(false)}
                    Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp)
                        .clickable { expanded = !expanded }) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row() {
                                Column(modifier = Modifier.weight(0.8f)) {
                                    Text(text = "02", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                        id = R.color.black
                                    ))
                                    Text(text = "SAT", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                        id = R.color.black
                                    ))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "09:25", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                        id = R.color.green
                                    ))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Missing", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                        id = R.color.red
                                    ))
                                }
                                Column(modifier = Modifier.weight(0.5f)) {
                                    Text(text = "08:50", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                        id = R.color.yellow
                                    ))
                                }
                                Column(modifier = Modifier.weight(0.2f)) {
                                    Icon(painterResource(id = R.drawable.baseline_keyboard_arrow_down_24)  ,
                                        contentDescription ="down" ,
                                        tint = colorResource(id = R.color.black),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                        if (expanded){
                            Divider(color = colorResource(R.color.backgroundColor), thickness = 1.dp)
                            Column(Modifier.padding(10.dp)) {
                                Row {
                                    Row(modifier = Modifier
                                        .weight(1.2f)
                                        .padding(top = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Column() {
                                            Icon(painterResource(id = R.drawable.location)  ,
                                                contentDescription = "location",
                                                tint = colorResource(id = R.color.paraColor),
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .padding(end = 10.dp)
                                            )
                                        }
                                        Column() {
                                            Text(text = "No 4, Chaimers road, Nandanam,Chennai",
                                                style = MaterialTheme.typography.titleSmall,
                                                color = colorResource(id = R.color.paraColor),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.8f), horizontalAlignment = Alignment.End) {
                                        Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(colorResource(id = R.color.toolight_green)), shape = RoundedCornerShape(20),contentPadding = PaddingValues(top=5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)) {
                                            Icon(painterResource(id = R.drawable.clock_1)  , contentDescription = "clock", tint = colorResource(
                                                id = R.color.green
                                            ),modifier = Modifier
                                                .padding(end = 10.dp)
                                                .width(20.dp)
                                                .height(20.dp))
                                            Text(text = "09:25",style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(500), color = colorResource(
                                                id = R.color.green
                                            )))
                                        }
                                    }
                                }
                                Divider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(top = 10.dp))
                                Column(modifier = Modifier.padding(top = 10.dp)) {
                                    Button(onClick = { coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    } },
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                        shape = RoundedCornerShape(5.dp),
                                        contentPadding = PaddingValues(top=5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
                                    ) {
                                        Icon(painterResource(id = R.drawable.clock_1)  , contentDescription = "clock", tint = colorResource(
                                            id = R.color.white
                                        ),modifier = Modifier
                                            .padding(end = 10.dp)
                                            .width(20.dp)
                                            .height(20.dp))
                                        Text(text = "Regularize", style = MaterialTheme.typography.titleSmall, color = Color.White)
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