package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.viewModel.BirthdayDetailViewModel
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun Birthday(
    navController: NavController,
    birthdayViewModel: BirthdayDetailViewModel
){
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Birthdays",
            "HomeScreen"
        ) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { Birthday_Screen(birthdayViewModel = birthdayViewModel) }
}

@Composable
fun Birthday_Screen(birthdayViewModel: BirthdayDetailViewModel) {
    val birthdayList = birthdayViewModel.birthdayList.collectAsState()
    /*val thisWeekData = birthdayList.value.filter { it.wish == "B" && it.status == "1" && it.dayOfWeek !="Today" }
    val today = birthdayList.value.filter { it.wish == "B" && it.status == "1" && it.dayOfWeek =="Today" }*/
    val upcomingWeekData = birthdayList.value.filter { it.wish == "B"}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.backgroundColor))
        .padding(top = 60.dp),
    ) {
        Card(modifier = Modifier.padding(bottom = 50.dp)) {
            Column(modifier = Modifier
                .background(color = colorResource(R.color.backgroundColor))
                .fillMaxSize()
                .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)//outer
                .background(color = colorResource(R.color.white))
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp) )  {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween){
                    Column {Text(text = "Birthday", color = colorResource(R.color.black), style = MaterialTheme.typography.titleMedium) }
                    Column{
                        Image(painterResource(id = R.drawable.birthcake)  , contentDescription = "birthday",modifier = Modifier
                            .offset(y = (-45).dp)
                            .size(100.dp))
                    }
                }
                Column( modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (upcomingWeekData.isNotEmpty()){
                        for (birthday in birthdayList.value.filter { it.wish == "B"  }) {
                            Column(modifier = Modifier.offset(y = (-10).dp)) {
                                Row(modifier = Modifier.padding(top=10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)) {
                                    val link ="http://2growhr.saneforce.info/Images/EmpUpload/"
                                    val img = birthday.profile // Assuming `wedding.profile` contains the relative image path
                                    val imgUrl = link + img
                                    val processedImgUrl = imgUrl.ifEmpty { link }
                                    Column(modifier = Modifier.weight(1.5f)) {
                                        Row {
                                            Column {
                                                if (processedImgUrl != link) {
                                                    AsyncImage(
                                                        model = ImageRequest.Builder(LocalContext.current)
                                                            .data(processedImgUrl)
                                                            .crossfade(true)
                                                            .transformations(
                                                                CircleCropTransformation()
                                                            )
                                                            .allowHardware(true)
                                                            .build(),
                                                        contentDescription = "icon",
                                                        contentScale = ContentScale.Fit,
                                                        modifier = Modifier.size(35.dp)
                                                    )
                                                } else {
                                                    // If imgUrl is empty or null, display a default image
                                                    Image(
                                                        painter = painterResource(id = R.drawable.nonimgicon),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(35.dp)
                                                    )
                                                }
                                            }
                                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                                BasicTextField(
                                                    readOnly = true,
                                                    value = "${birthday.name}(${birthday.empCode})",
                                                    onValueChange = { /* Handle value change if needed */ },
                                                    textStyle = TextStyle(
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                                        color = colorResource(id = R.color.black),
                                                    ),
                                                    singleLine = true,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(end = 16.dp) // Adjust padding as needed
                                                )
                                                Text(text = birthday.designation, color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall)
                                            }
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.5f)) {
                                        Text(text = "${birthday.dayOfWeek}, ${birthday.date} ${birthday.month}", color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                                    }
                                }
                                Divider(color = colorResource(R.color.divider), thickness = 1.dp)
                            }
                        }
                    }
                    else{
                        Column(modifier = Modifier.fillMaxWidth(1f)) {
                            Image(
                                painterResource(id = R.drawable.birthdaycake_opa)  ,
                                contentDescription = "birthday",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(80.dp)
                                    .height(80.dp),
                            )
                            Text(text = "No Birthdays",
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black),
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
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
fun uiBirthdayPreview() {

    val navController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(true) }

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Birthdays", "HomeScreen") },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    {
        val birthdayList = generateWishList("B")

        val upcomingWeekData = birthdayList.filter { it.wish == "B"}

        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.backgroundColor))
            .padding(top = 60.dp),
        ) {
            Card(modifier = Modifier.padding(bottom = 50.dp)) {
                Column(modifier = Modifier
                    .background(color = colorResource(R.color.backgroundColor))
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 30.dp)//outer
                    .background(color = colorResource(R.color.white))
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp) )  {
                    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween){
                        Column {Text(text = "Birthday", color = colorResource(R.color.black), style = MaterialTheme.typography.titleMedium) }
                        Column{
                            Image(painterResource(id = R.drawable.birthcake)  , contentDescription = "birthday",modifier = Modifier
                                .offset(y = (-45).dp)
                                .size(100.dp))
                        }
                    }
                    Column( modifier = Modifier.verticalScroll(rememberScrollState())) {
                        if (upcomingWeekData.isNotEmpty()){
                            for (birthday in birthdayList.filter { it.wish == "B"  }) {
                                Column(modifier = Modifier.offset(y = (-10).dp)) {
                                    Row(modifier = Modifier.padding(top=10.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)) {
                                        val link ="http://2growhr.saneforce.info/Images/EmpUpload/"
                                        val processedImgUrl = "http://2growhr.saneforce.info/Images/EmpUpload/"
                                        Column(modifier = Modifier.weight(1.5f)) {
                                            Row {
                                                Column {
                                                    if (processedImgUrl != link) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(processedImgUrl)
                                                                .crossfade(true)
                                                                .transformations(
                                                                    CircleCropTransformation()
                                                                )
                                                                .allowHardware(true)
                                                                .build(),
                                                            contentDescription = "icon",
                                                            contentScale = ContentScale.Fit,
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    } else {
                                                        // If imgUrl is empty or null, display a default image
                                                        Image(
                                                            painter = painterResource(id = R.drawable.nonimgicon),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    }
                                                }
                                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                                    BasicTextField(
                                                        readOnly = true,
                                                        value = "${birthday.name}(${birthday.empCode})",
                                                        onValueChange = { /* Handle value change if needed */ },
                                                        textStyle = TextStyle(
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                                            color = colorResource(id = R.color.black),
                                                        ),
                                                        singleLine = true,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(end = 16.dp) // Adjust padding as needed
                                                    )
                                                    Text(text = birthday.designation, color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall)
                                                }
                                            }
                                        }
                                        Column(modifier = Modifier.weight(0.5f)) {
                                            Text(text = "${birthday.dayOfWeek}, ${birthday.date} ${birthday.month}", color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                                        }
                                    }
                                    Divider(color = colorResource(R.color.divider), thickness = 1.dp)
                                }
                            }
                        }
                        else{
                            Column(modifier = Modifier.fillMaxWidth(1f)) {
                                Image(
                                    painterResource(id = R.drawable.birthdaycake_opa)  ,
                                    contentDescription = "birthday",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .width(80.dp)
                                        .height(80.dp),
                                )
                                Text(text = "No Birthdays",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.black),
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}