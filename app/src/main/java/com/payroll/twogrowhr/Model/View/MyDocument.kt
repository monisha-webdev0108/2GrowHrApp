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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.Model.ResponseModel.DocumentListData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.DocumentViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyDocument(navController: NavController, documentViewModel: DocumentViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "My Documents",
            "HomeScreen"
        ) },
        drawerContent = {  },
        bottomBarContent = { },//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    { MyDocument_Screen(navController = navController, documentViewModel = documentViewModel)}
}
@Composable
fun MyDocument_Screen(navController: NavController, documentViewModel: DocumentViewModel) {

    filePathClearance1()

    val context = LocalContext.current

    val documentList = documentViewModel.documentList.collectAsState()

// Fetch and update the payslip list when entering the page

    var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

    val loadingStatus = documentViewModel.loadingStatus

    flag = documentViewModel.flag

    var loading by remember { mutableStateOf(true) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
    ) {


        //FOR RECEIVED LIST RESPONSE

        if(loading && !documentViewModel.loadingStatus)
        {
            circularProgression()
        }

//LOGIC TO DISPLAY THE LIST

        @Composable
        fun uiUpdate()
        {
            LazyColumn{
                items(documentList.value){documentDetails->


                    val textUrl = when(documentDetails.documentType)
                    {
                        "A" -> "${Screen.AadhaarCard.route}/${documentDetails.documentType}"
                        "DE" -> "${Screen.DrivingLicenseDoc.route}/${documentDetails.documentType}"
                        "EDU" -> "${Screen.EducationDocList.route}/${documentDetails.documentType}"
                        "EXP" -> "${Screen.ExperienceDocumentList.route}/${documentDetails.documentType}"
                        "OD" -> "${Screen.OtherDocList.route}/${documentDetails.documentType}"
                        "PC" -> "${Screen.PanCard.route}/${documentDetails.documentType}"
                        "PP" -> "${Screen.PassportCard.route}/${documentDetails.documentType}"
                        "VI" -> "${Screen.VoterIDDoc.route}/${documentDetails.documentType}"
                        else -> ""
                    }

                    val displayUploaded = when (documentDetails.documentType) {
                        "A", "DE", "PC", "PP", "VI" -> true
                        else -> false
                    }


                    Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), shape = RoundedCornerShape(8.dp), modifier = Modifier
                        .padding(bottom = 10.dp)
                        .clickable { navController.navigate(textUrl) }
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(modifier = Modifier.weight(0.60f)) {
                                Text(
                                    text = documentDetails.documentName,
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)
                                    ),
                                )
                            }

                            Column(modifier = Modifier.weight(0.40f)){
                                Row(modifier = Modifier
                                    .fillMaxWidth(1f),
                                    horizontalArrangement = Arrangement.End,)
                                {
                                    Column(modifier = Modifier
                                        .padding(top = 1.dp)
                                        .weight(0.80f)) {

                                        if(documentDetails.count > 0)
                                        {
                                            Row {

                                                Box(
                                                    modifier = Modifier
                                                        .width(80.dp)
                                                        .height(25.dp)
                                                        .weight(if (!displayUploaded) 1f else 0.70f)
                                                        .background(
                                                            color = colorResource(id = R.color.third_light_green),
                                                            shape = RoundedCornerShape(3.dp)
                                                        )
                                                        .clickable { navController.navigate(textUrl) },
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Text(
                                                        text = "Uploaded",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.green)
                                                        )
                                                    )
                                                }


                                                if(!displayUploaded)
                                                {
                                                    Box(
                                                        modifier = Modifier
                                                            .width(30.dp)
                                                            .height(25.dp)
                                                            .weight(0.30f)
                                                            .background(
                                                                color = colorResource(id = R.color.flight_green),
                                                                shape = RoundedCornerShape(3.dp)
                                                            )
                                                            .clickable {
                                                                navController.navigate(
                                                                    textUrl
                                                                )
                                                            },
                                                        contentAlignment = Alignment.Center,
                                                    ) {
                                                        Text(
                                                            text = "${documentDetails.count}",
                                                            color = colorResource(id = R.color.green),
                                                            style = MaterialTheme.typography.titleSmall
                                                        )
                                                    }
                                                }


                                            }
                                        }

                                    }

                                    Column(modifier = Modifier.weight(0.20f)) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = "Menu",
                                            modifier = Modifier
                                                .size(25.dp)
                                                .padding(start = 5.dp),
                                            tint = colorResource(id = R.color.black)
                                            )

                                    }
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

            if(documentList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        documentViewModel.getDocumentList(navController, context = context, userViewModel.getSFCode() )
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
fun uiDocumentPreview()
{


    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "My Documents", "HomeScreen") },
        drawerContent = {  },
        bottomBarContent = { },//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    {


        val flag = 1

        val documentList = generateDocumentList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
        ) {


//LOGIC TO DISPLAY THE LIST

            @Composable
            fun uiUpdate()
            {
                LazyColumn{
                    items(documentList){documentDetails->


                        val textUrl = when(documentDetails.documentType)
                        {
                            "A" -> "${Screen.AadhaarCard.route}/${documentDetails.documentType}"
                            "DE" -> "${Screen.DrivingLicenseDoc.route}/${documentDetails.documentType}"
                            "EDU" -> "${Screen.EducationDocList.route}/${documentDetails.documentType}"
                            "EXP" -> "${Screen.ExperienceDocumentList.route}/${documentDetails.documentType}"
                            "OD" -> "${Screen.OtherDocList.route}/${documentDetails.documentType}"
                            "PC" -> "${Screen.PanCard.route}/${documentDetails.documentType}"
                            "PP" -> "${Screen.PassportCard.route}/${documentDetails.documentType}"
                            "VI" -> "${Screen.VoterIDDoc.route}/${documentDetails.documentType}"
                            else -> ""
                        }

                        val displayUploaded = when (documentDetails.documentType) {
                            "A", "DE", "PC", "PP", "VI" -> true
                            else -> false
                        }


                        Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), shape = RoundedCornerShape(8.dp), modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clickable { navController.navigate(textUrl) }
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(modifier = Modifier.weight(0.60f)) {
                                    Text(
                                        text = documentDetails.documentName,
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)
                                        ),
                                    )
                                }

                                Column(modifier = Modifier.weight(0.40f)){
                                    Row(modifier = Modifier
                                        .fillMaxWidth(1f),
                                        horizontalArrangement = Arrangement.End,)
                                    {
                                        Column(modifier = Modifier
                                            .padding(top = 1.dp)
                                            .weight(0.80f)) {

                                            if(documentDetails.count > 0)
                                            {
                                                Row {

                                                    Box(
                                                        modifier = Modifier
                                                            .width(80.dp)
                                                            .height(25.dp)
                                                            .weight(if (!displayUploaded) 1f else 0.70f)
                                                            .background(
                                                                color = colorResource(id = R.color.third_light_green),
                                                                shape = RoundedCornerShape(3.dp)
                                                            )
                                                            .clickable { navController.navigate(textUrl) },
                                                        contentAlignment = Alignment.Center,
                                                    ) {
                                                        Text(
                                                            text = "Uploaded",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.green)
                                                            )
                                                        )
                                                    }


                                                    if(!displayUploaded)
                                                    {
                                                        Box(
                                                            modifier = Modifier
                                                                .width(30.dp)
                                                                .height(25.dp)
                                                                .weight(0.30f)
                                                                .background(
                                                                    color = colorResource(id = R.color.flight_green),
                                                                    shape = RoundedCornerShape(3.dp)
                                                                )
                                                                .clickable {
                                                                    navController.navigate(
                                                                        textUrl
                                                                    )
                                                                },
                                                            contentAlignment = Alignment.Center,
                                                        ) {
                                                            Text(
                                                                text = "${documentDetails.count}",
                                                                color = colorResource(id = R.color.green),
                                                                style = MaterialTheme.typography.titleSmall
                                                            )
                                                        }
                                                    }


                                                }
                                            }

                                        }

                                        Column(modifier = Modifier.weight(0.20f)) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "Menu",
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .padding(start = 5.dp),
                                                tint = colorResource(id = R.color.black)
                                            )

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

// LOGIC TO DISPLAY THE UI

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


fun generateDocumentList(): List<DocumentListData>
{
    return listOf(DocumentListData(documentName = "Aadhaar Card", documentType = "A", count = 1),
        DocumentListData(documentName = "Driving License", documentType = "DE", count = 1),
        DocumentListData(documentName = "Education Details", documentType = "EDU", count = 1),
        DocumentListData(documentName = "Experience Details", documentType = "EXP", count = 3),
        DocumentListData(documentName = "Other Documents", documentType = "OD", count = 0),
        DocumentListData(documentName = "PAN Card", documentType = "PC", count = 1),
        DocumentListData(documentName = "Passport", documentType = "PP", count = 0),
        DocumentListData(documentName = "Voter Id", documentType = "VI", count = 1),
    )
}
