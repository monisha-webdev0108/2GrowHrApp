package com.payroll.twogrowhr.Model.View

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.Model.ResponseModel.ExperienceDocumentData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.components.TextFieldNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.downloadImage
import com.payroll.twogrowhr.components.downloadImageWithProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.fileNameFormatCheck
import com.payroll.twogrowhr.components.filePathClearance1
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.DocumentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExperienceDocumentList(navController: NavController, documentViewModel: DocumentViewModel, type : String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Experience Certificate Details",
            "My_document"
        ) },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { ExperienceDocumentList_Screen(navController = navController, documentViewModel = documentViewModel, type = type)}
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExperienceDocumentList_Screen(navController: NavController, documentViewModel: DocumentViewModel, type : String) {

    filePathClearance1()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
    )
    {


        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")


        Button(
            onClick = {navController.navigate("${Screen.ExperienceDoc.route}/${type}")},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
            shape = RoundedCornerShape(5.dp),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            Text(
                text = " + Add Certificate",
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
        ) {

            val context = LocalContext.current

            val experienceDocumentList = documentViewModel.experienceDocumentDetails.collectAsState()

// Fetch and update the payslip list when entering the page

            var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

            val loadingStatus = documentViewModel.loadingStatus4

            flag = documentViewModel.flag4

            var loading by remember { mutableStateOf(true) }


            if(loading && !documentViewModel.loadingStatus4)
            {
                circularProgression()
            }

            val showLoading = remember { mutableStateOf(false) }

            if (showLoading.value) {
                circularProgression()
            }

            @Suppress("DEPRECATION")
            @Composable
            fun uiUpdate()
            {

                LazyColumn {
                    items(experienceDocumentList.value) { documentDetails ->

                        val dateOfJoining = LocalDate.parse(documentDetails.doj, inputFormatter)
                        val dateOfJoiningDisplayed = outputFormatter.format(dateOfJoining)

                        val dateOfCompletion = LocalDate.parse(documentDetails.dor, inputFormatter)
                        val dateOfCompletionDisplayed = outputFormatter.format(dateOfCompletion)

                        val fileNameNew = fileNameFormatCheck(documentDetails.fileName)


                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            modifier = Modifier.padding( top = 10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row{
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Company Name", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        TextFieldNameSizeValidation(documentDetails.companyName,14, colorResource(id = R.color.black), 500, 16)

                                    }

                                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {


                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                            modifier = Modifier.fillMaxSize().clickable {
//                                                downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)
                                            }
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.End).clickable {
//                                                downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)
                                            })
                                            {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.download1),
                                                    contentDescription = "Download",
                                                    tint = colorResource(id = R.color.green),
                                                    modifier = Modifier
                                                        .size(15.dp)
                                                        .clickable {
//                                                            downloadImage(navController,documentDetails.filePath, fileNameNew, "My_document", context)
                                                            downloadImageWithProgression(navController,documentDetails.filePath, fileNameNew, "My_document", showLoading, context)
                                                        }
                                                )

                                                Column(modifier = Modifier.padding(start = 5.dp, end = 10.dp)) {
                                                    Text(text = "Download",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.green)
                                                        ),
                                                        modifier = Modifier.clickable {
//                                                            downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                            downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)
                                                        }
                                                    )
                                                }


                                            }
                                        }


                                    }



                                    /*
                                                                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                                                            var expanded by remember { mutableStateOf(false) }
                                                                            Box(
                                                                                Modifier
                                                                                    .padding(10.dp)
                                                                                    .clickable { expanded = !expanded }) {
                                                                                Icon(
                                                                                    painterResource(id =R.drawable.three_dot )  , contentDescription ="three dot", tint = colorResource(
                                                                                        id = R.color.black
                                                                                    ), modifier = Modifier.size(22.dp))
                                                                            }
                                                                            Column(horizontalAlignment = Alignment.End) {
                                                                                DropdownMenu(
                                                                                    expanded = expanded,
                                                                                    onDismissRequest = { expanded = false },
                                                                                ) {
                                                                                    // Add your dropdown menu items here
                                                                                    DropdownMenuItem(
                                                                                        onClick = {  },
                                                                                        modifier = Modifier
                                                                                            .padding(vertical = 0.dp, horizontal = 0.dp)
                                                                                            .fillMaxWidth(),
                                                                                    ) {
                                                                                        Text(
                                                                                            "Edit",
                                                                                            style = MaterialTheme.typography.titleMedium,
                                                                                            color = colorResource(id = R.color.black),
                                                                                            modifier = Modifier
                                                                                                .padding(vertical = 0.dp, horizontal = 0.dp)
                                                                                        )
                                                                                    }
                                                                                    Divider(modifier = Modifier.padding( bottom = 10.dp),color = colorResource(id = R.color.lightshade))
                                                                                    DropdownMenuItem(
                                                                                        onClick = {  },
                                                                                        modifier = Modifier
                                                                                            .padding(vertical = 0.dp, horizontal = 0.dp)
                                                                                            .fillMaxWidth(),
                                                                                    ) {
                                                                                        Text(
                                                                                            "Download",
                                                                                            style = MaterialTheme.typography.titleMedium,
                                                                                            color = colorResource(id = R.color.black),
                                                                                            modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp)
                                                                                        )
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                   */

                                }
                                Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))

                                /* Designation*/

                                Row{
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Designation", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        TextFieldNameSizeValidation(documentDetails.jobTitle, 14, colorResource(id = R.color.black), 500, 16)
                                    }


                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Location", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        val locationDisplay = if(documentDetails.location.isEmpty() || documentDetails.location.isBlank()) "-" else documentDetails.location

                                        TextFieldNameSizeValidation(locationDisplay, 14, colorResource(id = R.color.black), 500, 16)

                                    }

                                }

                                Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))

                                /* Joining, Completion*/

                                Row{
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Date of Joining", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        TextFieldNameSizeValidation(dateOfJoiningDisplayed, 14, colorResource(id = R.color.black), 500, 16)

                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Date of Relieving", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        TextFieldNameSizeValidation(dateOfCompletionDisplayed, 14, colorResource(id = R.color.black), 500, 16)

                                    }
                                }
                                Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))
                                /* Location, Uploads*/
                                Row{

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Uploads", style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        if (fileNameNew.length > 15) {
                                            PlainTooltipBox(
                                                tooltip = {
                                                    Text(
                                                        text = fileNameNew,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                        ),
                                                    )
                                                }
                                            ) {
                                                Text(
                                                    text = fileNameNew.take(15) + "..." ,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.themeColor)
                                                    ),
                                                    modifier = Modifier
                                                        .tooltipTrigger()
                                                        .clickable {
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(documentDetails.filePath))
                                                            context.startActivity(intent)
                                                        }
                                                )
                                            }
                                        }
                                        else
                                        {
                                            Text(
                                                text = fileNameNew,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.themeColor)
                                                ),
                                                modifier = Modifier
                                                    .clickable {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(documentDetails.filePath))
                                                        context.startActivity(intent)
                                                    }
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {}

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

                if(experienceDocumentList.value.isEmpty())
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
}




@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiExperienceDocumentPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Experience Certificate Details", "My_document") },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {

        val experienceDocumentList = generateExperienceDocumentList()
        val flag = 1

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
        )
        {


            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")


            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                Text(
                    text = " + Add Certificate",
                    color = colorResource(id = R.color.white),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
            ) {

                @Suppress("DEPRECATION")
                @Composable
                fun uiUpdate()
                {

                    LazyColumn {
                        items(experienceDocumentList) { documentDetails ->

                            val dateOfJoining = LocalDate.parse(documentDetails.doj, inputFormatter)
                            val dateOfJoiningDisplayed = outputFormatter.format(dateOfJoining)

                            val dateOfCompletion = LocalDate.parse(documentDetails.dor, inputFormatter)
                            val dateOfCompletionDisplayed = outputFormatter.format(dateOfCompletion)

                            val fileNameNew = fileNameFormatCheck(documentDetails.fileName)


                            Card(
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                modifier = Modifier.padding( top = 10.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row{
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Company Name", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            TextFieldNameSizeValidation(documentDetails.companyName,14, colorResource(id = R.color.black), 500, 16)

                                        }

                                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {


                                            Card(
                                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                Row(modifier = Modifier.align(Alignment.End))
                                                {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.download1),
                                                        contentDescription = "Download",
                                                        tint = colorResource(id = R.color.green),
                                                        modifier = Modifier
                                                            .size(15.dp)
                                                                                                                )

                                                    Column(modifier = Modifier.padding(start = 5.dp, end = 10.dp)) {
                                                        Text(text = "Download",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.green)
                                                            ),
                                                            modifier = Modifier
                                                        )
                                                    }


                                                }
                                            }


                                        }



                                    }
                                    Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))

                                    /* Designation*/

                                    Row{
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Designation", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            TextFieldNameSizeValidation(documentDetails.jobTitle, 14, colorResource(id = R.color.black), 500, 16)
                                        }


                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Location", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            val locationDisplay = if(documentDetails.location.isEmpty() || documentDetails.location.isBlank()) "-" else documentDetails.location

                                            TextFieldNameSizeValidation(locationDisplay, 14, colorResource(id = R.color.black), 500, 16)

                                        }

                                    }

                                    Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))

                                    /* Joining, Completion*/

                                    Row{
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Date of Joining", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            TextFieldNameSizeValidation(dateOfJoiningDisplayed, 14, colorResource(id = R.color.black), 500, 16)

                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Date of Relieving", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            TextFieldNameSizeValidation(dateOfCompletionDisplayed, 14, colorResource(id = R.color.black), 500, 16)

                                        }
                                    }
                                    Divider(modifier = Modifier.padding( bottom = 10.dp, top = 10.dp),color = colorResource(id = R.color.lightshade))
                                    /* Location, Uploads*/
                                    Row{

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Uploads", style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            if (fileNameNew.length > 15) {
                                                PlainTooltipBox(
                                                    tooltip = {
                                                        Text(
                                                            text = fileNameNew,
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                            ),
                                                        )
                                                    }
                                                ) {
                                                    Text(
                                                        text = fileNameNew.take(15) + "..." ,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.themeColor)
                                                        ),
                                                        modifier = Modifier
                                                            .tooltipTrigger()
                                                    )
                                                }
                                            }
                                            else
                                            {
                                                Text(
                                                    text = fileNameNew,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.themeColor)
                                                    ),
                                                    modifier = Modifier
                                                )
                                            }
                                        }

                                        Column(modifier = Modifier.weight(1f)) {}

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


}



fun generateExperienceDocumentList(): List<ExperienceDocumentData>
{
    return listOf(
        ExperienceDocumentData(empId = "EMP12657", type = "EXP", companyName = "ABC Pvt. Ltd", jobTitle = "Junior Developer", doj = "2018-09-10T00:00:00", dor = "2019-08-30T00:00:00", location = "Madurai", filePath = "http://testing.2growhr.io/UploadFiles/Documents/PaySlip_EMP5099_10_2022 (3).pdf", fileName = "ExpDetails1.pdf"),
        ExperienceDocumentData(empId = "EMP12657", type = "EXP", companyName = "XYZ Pvt. Ltd", jobTitle = "Senior Developer", doj = "2019-09-01T00:00:00", dor = "2020-12-31T00:00:00", location = "Madurai", filePath = "http://testing.2growhr.io/UploadFiles/Documents/PaySlip_EMP5099_10_2022 (3).pdf", fileName = "ExpDetails2.pdf"),
        ExperienceDocumentData(empId = "EMP12657", type = "EXP", companyName = "SaneForce", jobTitle = "Project Manager", doj = "2022-01-05T00:00:00", dor = "2024-05-31T00:00:00", location = "Madurai", filePath = "http://testing.2growhr.io/UploadFiles/Documents/PaySlip_EMP5099_10_2022 (3).pdf", fileName = "ExpDetails3.pdf"),
    )
}