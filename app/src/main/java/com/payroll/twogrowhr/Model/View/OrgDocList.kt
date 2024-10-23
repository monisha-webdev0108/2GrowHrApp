package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocListData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.viewModel.OrgDocListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrgDocList(navController: NavController,  orgDocListViewModel: OrgDocListViewModel) {

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Organization Documents", "HomeScreen") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        OrgDocList_Screen(orgDocListViewModel = orgDocListViewModel, navController = navController)
    }

}
@Composable
fun OrgDocList_Screen(orgDocListViewModel: OrgDocListViewModel,navController: NavController) {
    val orgDocList = orgDocListViewModel.orgDocList.collectAsState()
    val scrollState = rememberScrollState()
    var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

    val loadingStatus = orgDocListViewModel.loadingStatus

    flag = orgDocListViewModel.flag

    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    if(loading)
    {
        circularProgression()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(scrollState)
    ) {
        @Composable
        fun uiUpdate(){
            if(orgDocList.value.isNotEmpty()){
                for(data in orgDocList.value){

                    Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(3f)) {
                                BasicTextField(
                                    readOnly = true,
                                    value = data.folderName,
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

                            }

                            Row(modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .weight(1f)
                                .clickable {

                                    navController.navigate("${Screen.OrgDoc.route}?res=${data.folderId}")

                                }, horizontalArrangement = Arrangement.End,) {
                                Column(modifier = Modifier.padding(top = 1.dp)) {
                                    Row {
                                        Box(
                                            modifier = Modifier
                                                .width(30.dp)
                                                .height(25.dp)
                                                .background(
                                                    color = colorResource(id = R.color.flight_green),
                                                    shape = RoundedCornerShape(5.dp)
                                                ),
                                            contentAlignment = Alignment.Center,
                                        ){
                                            Text(text =data.folderCount , color = colorResource(id = R.color.green), style = MaterialTheme.typography.titleSmall)
                                        }
                                    }
                                }
                                Column {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Menu",
                                        modifier = Modifier.size(28.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else{
                noDataView()
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

            if(orgDocList.value.isEmpty())
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiORGDocumentScreenPreview() {

    val navController = rememberNavController()
    val orgDocList = generateOrgDocList()
    val scrollState = rememberScrollState()
    val flag by remember { mutableIntStateOf(1) }

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Organization Documents", "HomeScreen") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, start = 10.dp, end = 10.dp)
                .verticalScroll(scrollState)
        ) {
            @Composable
            fun uiUpdate(){
                if(orgDocList.isNotEmpty()){
                    for(data in orgDocList){

                        Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(bottom = 10.dp)) {
                            Row(modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(3f)) {
                                    BasicTextField(
                                        readOnly = true,
                                        value = data.folderName,
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

                                }

                                Row(modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .weight(1f)
                                    .clickable {

                                        navController.navigate("${Screen.OrgDoc.route}?res=${data.folderId}")

                                    }, horizontalArrangement = Arrangement.End,) {
                                    Column(modifier = Modifier.padding(top = 1.dp)) {
                                        Row {
                                            Box(
                                                modifier = Modifier
                                                    .width(30.dp)
                                                    .height(25.dp)
                                                    .background(
                                                        color = colorResource(id = R.color.flight_green),
                                                        shape = RoundedCornerShape(5.dp)
                                                    ),
                                                contentAlignment = Alignment.Center,
                                            ){
                                                Text(text =data.folderCount , color = colorResource(id = R.color.green), style = MaterialTheme.typography.titleSmall)
                                            }
                                        }
                                    }
                                    Column {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = "Menu",
                                            modifier = Modifier.size(28.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    noDataView()
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


fun generateOrgDocList(): List<OrgDocListData>
{
    return listOf(
        OrgDocListData(folderCount = "3",folderId = "100", folderName = "Organization Policies"),
        OrgDocListData(folderCount = "1",folderId = "101", folderName = "Rules and Regulations"),
        OrgDocListData(folderCount = "1",folderId = "102", folderName = "Company Vision"),
    )
}

