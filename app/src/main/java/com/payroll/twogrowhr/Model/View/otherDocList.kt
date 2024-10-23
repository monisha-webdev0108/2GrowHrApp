package com.payroll.twogrowhr.Model.View

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.Model.ResponseModel.OtherDocumentData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.FileNameSizeValidation
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtherDocList(navController: NavController, documentViewModel: DocumentViewModel, type : String) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Other Documents Details", "My_document") },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { OtherDocList_Screen(navController = navController, documentViewModel = documentViewModel, type = type)}
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun OtherDocList_Screen(navController: NavController, documentViewModel: DocumentViewModel, type : String) {

    filePathClearance1()

    Column(
        modifier = Modifier

            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 70.dp, start = 10.dp, end = 10.dp)
    )

    {
        Button(
            onClick = {navController.navigate("${Screen.OtherDoc.route}/${type}")},
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
        Column(modifier = Modifier.fillMaxSize())
        {


            val context = LocalContext.current

            val otherDocumentList = documentViewModel.otherDocumentDetails.collectAsState()

// Fetch and update the payslip list when entering the page

            var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

            val loadingStatus = documentViewModel.loadingStatus5

            flag = documentViewModel.flag5

            var loading by remember { mutableStateOf(true) }


            if(loading && !documentViewModel.loadingStatus5)
            {
                circularProgression()
            }


            val showLoading = remember { mutableStateOf(false) }

            if (showLoading.value) {
                circularProgression()
            }


            @Composable
            fun uiUpdate()
            {

                LazyColumn {
                    items(otherDocumentList.value) { documentDetails ->


                        val fileNameNew = fileNameFormatCheck(documentDetails.fileName)

                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Document Name",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        TextFieldNameSizeValidation(documentDetails.documentName, 14, colorResource(id = R.color.black), 500, 16)

                                    }


                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.End
                                    ) {

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = colorResource(
                                                    id = R.color.white
                                                )
                                            ),
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clickable {
                                                    downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)
//                                                  downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .clickable {
//                                                      downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                        downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)

                                                    })
                                            {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.download1),
                                                    contentDescription = "Download",
                                                    tint = Color(ContextCompat.getColor(context, R.color.green)),
                                                    modifier = Modifier
                                                        .size(15.dp)
                                                        .clickable {
//                                                          downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                            downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)

                                                        }
                                                )

                                                Column(modifier = Modifier.padding(start = 5.dp, end = 10.dp))
                                                {
                                                    Text(text = "Download",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.green)
                                                        ),
                                                        modifier = Modifier.clickable {
//                                                          downloadImage(navController, documentDetails.filePath, fileNameNew, "My_document", context)
                                                            downloadImageWithProgression(navController, documentDetails.filePath, fileNameNew, "My_document", showLoading, context)

                                                        }
                                                    )
                                                }


                                            }
                                        }
                                    }

                                    /*                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                                                    var expanded by remember { mutableStateOf(false) }
                                                                    Box(
                                                                        Modifier
                                                                            .padding(10.dp)
                                                                            .clickable { expanded = !expanded }) {
                                                                        Icon(
                                                                            painterResource(id = R.drawable.three_dot )  , contentDescription ="three dot", tint = colorResource(
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
                                                                                onClick = {},
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
                                                                                onClick = {},
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
                                Divider(
                                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                                    color = colorResource(id = R.color.lightshade)
                                )


                                /* UPLOADS*/
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Uploads",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.paraColor),
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )

                                        FileNameSizeValidation(fileNameNew, documentDetails.filePath)

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

                if(otherDocumentList.value.isEmpty())
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



@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiOtherDocumentListPreview() {

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Other Documents Details", "My_document") },
        drawerContent = { },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {

        var otherDocumentList = generateOtherDocumentList()
        val flag = 0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 70.dp, start = 10.dp, end = 10.dp)
        )
        {
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

            Column(modifier = Modifier.fillMaxSize())
            {

                @Composable
                fun uiUpdate()
                {

                    LazyColumn {
                        items(otherDocumentList) { documentDetails ->

                            val fileNameNew = fileNameFormatCheck(documentDetails.fileName)

                            Card(
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                modifier = Modifier.padding(top = 10.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Document Name",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            TextFieldNameSizeValidation(fileNameNew, 14, colorResource(id = R.color.black), 500, 16)

                                        }


                                        Column(
                                            modifier = Modifier.weight(1f),
                                            horizontalAlignment = Alignment.End
                                        ) {

                                            Card(
                                                colors = CardDefaults.cardColors(
                                                    containerColor = colorResource(
                                                        id = R.color.white
                                                    )
                                                ),
                                                modifier = Modifier.fillMaxSize()

                                            ) {
                                                Row(
                                                    modifier = Modifier.align(Alignment.End) )
                                                {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.download1),
                                                        contentDescription = "Download",
                                                        tint = colorResource(id = R.color.green),
                                                        modifier = Modifier
                                                            .size(15.dp)
                                                    )

                                                    Column(modifier = Modifier.padding(start = 5.dp, end = 10.dp))
                                                    {
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
                                    Divider(
                                        modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                                        color = colorResource(id = R.color.lightshade)
                                    )


                                    /* UPLOADS*/
                                    Row {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Uploads",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.paraColor),
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )

                                            FileNameSizeValidation(documentDetails.fileName, documentDetails.filePath)

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
}



fun generateOtherDocumentList(): List<OtherDocumentData>
{
    return listOf(
        OtherDocumentData(empId = "EMP12657", type = "OD", documentName = "document1", filePath = "http://testing.2growhr.io/UploadFiles/Documents/PaySlip_EMP5099_10_2022 (3).pdf", fileName = "document1.pdf"),
        OtherDocumentData(empId = "EMP12657", type = "OD", documentName = "document2", filePath = "http://testing.2growhr.io/UploadFiles/Documents/PaySlip_EMP5099_10_2022 (3).pdf", fileName = "document2.pdf"),
    )
}
