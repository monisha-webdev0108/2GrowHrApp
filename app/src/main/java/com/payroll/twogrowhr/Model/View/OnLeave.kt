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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.WishesData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.viewModel.OnLeaveViewModel


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnLeave(navController: NavController, onLeaveViewModel: OnLeaveViewModel) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "On Leave",
            "HomeScreen"
        ) },
        bottomBarContent = { },//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    { OnLeave_Screen(onLeaveModel = onLeaveViewModel) }
}

@Composable
fun OnLeave_Screen(onLeaveModel: OnLeaveViewModel) {
    val onLeaveList = onLeaveModel.OnLeaveList.collectAsState()
    val thisWeekData = onLeaveList.value.filter { it.wish == "L"}


    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.backgroundColor))
        .padding(top = 60.dp),
    ) {

        Card(modifier = Modifier.padding(bottom = 50.dp)) {
            Column(modifier = Modifier
                .background(color = colorResource(R.color.backgroundColor))
                .fillMaxSize()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)//outer
                .background(
                    color = colorResource(
                        R.color.white
                    )
                )
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp))  {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween){
                    Column {
                        Text(text = "On Leave", color = colorResource(R.color.black), style = MaterialTheme.typography.titleMedium)
                    }
                    Column{
                        Image(
                            painterResource(id = R.drawable.noleavetoday),
                            contentDescription = "no leave",
                            modifier = Modifier
                                .offset(y = (-35).dp)
                                .size(80.dp)
                        )
                    }
                }
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (thisWeekData.isNotEmpty()) {

                        for (OnLeave in onLeaveList.value.filter { it.wish == "L"}) {
                            Column {
                                Row(modifier = Modifier.padding(top=10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)) {
                                    val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                                    val img = OnLeave.profile // Assuming `wedding.Profile` contains the relative image path
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
                                                            .transformations(CircleCropTransformation())
                                                            .allowHardware(true)
                                                            .build(),
                                                        contentDescription = "icon",
                                                        contentScale = ContentScale.Fit,
                                                        modifier = Modifier.size(35.dp)
                                                    )
                                                } else {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.nonimgicon),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(35.dp)
                                                    )
                                                }
                                            }

                                            Column(modifier = Modifier.padding(start = 10.dp)) {
                                                BasicTextField(
                                                    value ="${OnLeave.name}(${OnLeave.empCode})" ,
                                                    onValueChange = { /* Handle value change if needed */ },
                                                    textStyle = TextStyle(
                                                        fontSize = 14.sp,
                                                        fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                                        color = colorResource(id = R.color.black)
                                                    ),
                                                    singleLine = true,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(end = 16.dp) // Adjust padding as needed
                                                )
                                                Text(text = OnLeave.designation, color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall)
                                            }
                                        }
                                    }
                                    Column(modifier = Modifier.weight(0.5f)) {
                                        Text(text = "${OnLeave.dayOfWeek}, ${OnLeave.date} ${OnLeave.month}", color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                                    }
                                }
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = colorResource(R.color.divider)
                                )
                            }
                        }

                    }
                    else{
                        Column(modifier = Modifier.fillMaxWidth(1f)) {

                            Image(
                                painterResource(id = R.drawable.noleavetoday),
                                contentDescription = "ring",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 10.dp)
                                    .size(150.dp)
                                    .alpha(0.5f),

                            )
                            Text(text = "No one is on leave this week!",
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
fun uiOnLeave_ScreenPreview() {

    val navController = rememberNavController()
    //WishesData
    val onLeaveList = generateWishList("L")

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "On Leave", "HomeScreen") },
        bottomBarContent = { },//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    {
        val thisWeekData = onLeaveList.filter { it.wish == "L"}


        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.backgroundColor))
            .padding(top = 60.dp),
        ) {

            Card(modifier = Modifier.padding(bottom = 50.dp)) {
                Column(modifier = Modifier
                    .background(color = colorResource(R.color.backgroundColor))
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)//outer
                    .background(color = colorResource(R.color.white)
                    )
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp))  {
                    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween){
                        Column {
                            Text(text = "On Leave", color = colorResource(R.color.black), style = MaterialTheme.typography.titleMedium)
                        }
                        Column{
                            Image(
                                painterResource(id = R.drawable.noleavetoday),
                                contentDescription = "no leave",
                                modifier = Modifier
                                    .offset(y = (-35).dp)
                                    .size(80.dp)
                            )
                        }
                    }
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        if (thisWeekData.isNotEmpty()) {

                            for (OnLeave in onLeaveList.filter { it.wish == "L"}) {
                                Column {
                                    Row(modifier = Modifier.padding(top=10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)) {
                                        val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                                        val processedImgUrl = Constant.IMAGE_URL + "Images/EmpUpload/"
                                        Column(modifier = Modifier.weight(1.5f)) {
                                            Row {
                                                Column {
                                                    if (processedImgUrl != link) {
                                                        AsyncImage(
                                                            model = ImageRequest.Builder(LocalContext.current)
                                                                .data(processedImgUrl)
                                                                .crossfade(true)
                                                                .transformations(CircleCropTransformation())
                                                                .allowHardware(true)
                                                                .build(),
                                                            contentDescription = "icon",
                                                            contentScale = ContentScale.Fit,
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    } else {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.nonimgicon),
                                                            contentDescription = null,
                                                            modifier = Modifier.size(35.dp)
                                                        )
                                                    }
                                                }

                                                Column(modifier = Modifier.padding(start = 10.dp)) {
                                                    BasicTextField(
                                                        value ="${OnLeave.name}(${OnLeave.empCode})" ,
                                                        onValueChange = { /* Handle value change if needed */ },
                                                        textStyle = TextStyle(
                                                            fontSize = 14.sp,
                                                            fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                                                            color = colorResource(id = R.color.black)
                                                        ),
                                                        singleLine = true,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(end = 16.dp) // Adjust padding as needed
                                                    )
                                                    Text(text = OnLeave.designation, color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall)
                                                }
                                            }
                                        }
                                        Column(modifier = Modifier.weight(0.5f)) {
                                            Text(text = "${OnLeave.dayOfWeek}, ${OnLeave.date} ${OnLeave.month}", color = colorResource(R.color.paraColor), style = MaterialTheme.typography.labelSmall, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                                        }
                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = colorResource(R.color.divider)
                                    )
                                }
                            }

                        }
                        else{
                            Column(modifier = Modifier.fillMaxWidth(1f)) {

                                Image(
                                    painterResource(id = R.drawable.noleavetoday),
                                    contentDescription = "ring",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 10.dp)
                                        .size(150.dp)
                                        .alpha(0.5f),

                                    )
                                Text(text = "No one is on leave this week!",
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

