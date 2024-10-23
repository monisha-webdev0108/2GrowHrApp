package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.OrgDocDetailData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.downloadImage
import com.payroll.twogrowhr.components.downloadImageWithProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.orgFileNameFormatCheck
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.viewModel.OrgDocDetailViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrgDoc(navController: NavController, orgDocDetailViewModel: OrgDocDetailViewModel) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val orgDocDetailList = orgDocDetailViewModel.orgDocDetailList.collectAsState()
    val folderNameList: List<String> = orgDocDetailList.value.map { it.folderName }
    val folderName = folderNameList.firstOrNull()
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = folderName.toString(), "OrgDocList") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        OrgDoc_Screen(navController = navController,orgDocDetailViewModel=orgDocDetailViewModel)
    }

}
@SuppressLint("SuspiciousIndentation")
@Composable
fun OrgDoc_Screen(navController: NavController,orgDocDetailViewModel: OrgDocDetailViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp,  start = 10.dp, end = 10.dp)
            .verticalScroll(scrollState)
    ) {
        val orgDocDetailList = orgDocDetailViewModel.orgDocDetailList.collectAsState()
        var flag by remember { mutableIntStateOf(0) }
        var folderId by remember { mutableStateOf(0) }

//FOR GETTING RESULT

        val loadingStatus = orgDocDetailViewModel.loadingStatus

        flag = orgDocDetailViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            circularProgression()
        }

        val context = LocalContext.current
        val empID = userViewModel.getSFCode()
        val org = userViewModel.getOrg()

        val showLoading = remember { mutableStateOf(false) }

        if (showLoading.value) {
            circularProgression()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @Composable
        fun uiUpdate(){
            if(!orgDocDetailList.value.isNullOrEmpty()){
                for(data in orgDocDetailList.value){
                    Card(modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                            Row {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Column {

                                            Box(modifier = Modifier
                                                .size(48.dp)
                                                .background(
                                                    colorResource(id = R.color.light_bright_red),
                                                    shape = CircleShape
                                                ),
                                                contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painterResource(id = R.drawable.orgdoc )  , contentDescription ="pdf icon", tint = colorResource(
                                                        id = R.color.red
                                                    ), modifier = Modifier.size(22.dp) )
                                            }
                                        }
                                        Column(modifier = Modifier.padding(start = 10.dp), // Occupy remaining space
                                            verticalArrangement = Arrangement.Center,

                                            ) {
                                            Column {
                                                BasicTextField(
                                                    readOnly = true,
                                                    value = data.documentName,
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
                                            if(data.documentDescription.isNotEmpty()){
                                                Column {
                                                    BasicTextField(
                                                        readOnly = true,
                                                        value = data.documentDescription,
                                                        onValueChange = { /* Handle value change if needed */ },
                                                        textStyle = TextStyle(
                                                            fontSize = 14.sp,
                                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                            color = colorResource(id = R.color.paraColor)
                                                        ),
                                                        singleLine = true,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(end = 16.dp) // Adjust padding as needed
                                                    )

                                                }
                                            }
                                        }
                                    }
                                }

                                Column(modifier = Modifier
                                    .weight(0.5f)
                                    .padding(end = 10.dp, top = 10.dp), horizontalAlignment = Alignment.End) {
                                    val urlLink =data.documentPath
                                    val fileName=data.documentName

                                    fun getDocumentNameWithExtension(documentPath: String): String {
                                        val lastIndexOfSlash = documentPath.lastIndexOf("\\")
                                        return documentPath.substring(lastIndexOfSlash + 1)
                                    }


                                    val documentName = getDocumentNameWithExtension(urlLink)//Get the file name with extension
                                    val fileNameNew = orgFileNameFormatCheck(documentName) // remove the date Format in front of FileName

                                    Log.d("org Doc", "documentName : $documentName")

                                    Row(modifier = Modifier.clickable {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            downloadImage(navController, urlLink, fileNameNew, "OrgDoc", context)
                                            downloadImageWithProgression(navController, urlLink, fileNameNew, "OrgDoc", showLoading, context)
                                        }
                                        coroutineScope.launch {
                                            orgDocDetailViewModel.getOrgDocShowCountDetails(
                                                empId = empID,
                                                org = org,
                                                documentId=data.documentId,
                                                context=context,
                                                navController = navController,
                                            )
                                        }

                                    }) {
                                        Icon( painterResource(id = R.drawable.eye) ,
                                            contentDescription = "eye",
                                            tint = colorResource(id = R.color.blue),
                                            modifier = Modifier
                                                .size(22.dp)
                                                .padding(end = 3.dp)
                                        )
                                        Text(text = "View",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = colorResource(id = R.color.blue),
                                        )
                                    }
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

            if(orgDocDetailList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        orgDocDetailViewModel.fetchAndUpdateOrgDocDetailData(navController,context,empID,org,folderId)
//                        uiUpdate()
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
fun uiORGdOCScreenPreview() {

    val navController = rememberNavController()
    val orgDocDetailList = generateOrgDocDetailList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "HR Document", "OrgDocList") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp,  start = 10.dp, end = 10.dp)
                .verticalScroll(scrollState)
        ) {

            var flag by remember { mutableIntStateOf(1) }
            var folderId by remember { mutableStateOf(0) }


            @RequiresApi(Build.VERSION_CODES.O)
            @Composable
            fun uiUpdate(){
                if(!orgDocDetailList.isEmpty()){
                    for(data in orgDocDetailList){
                        Card(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {
                            Column(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Column {

                                                Box(modifier = Modifier
                                                    .size(48.dp)
                                                    .background(
                                                        colorResource(id = R.color.light_bright_red),
                                                        shape = CircleShape
                                                    ),
                                                    contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        painterResource(id = R.drawable.orgdoc )  , contentDescription ="pdf icon", tint = colorResource(
                                                            id = R.color.red
                                                        ), modifier = Modifier.size(22.dp) )
                                                }
                                            }
                                            Column(modifier = Modifier.padding(start = 10.dp), // Occupy remaining space
                                                verticalArrangement = Arrangement.Center,

                                                ) {
                                                Column {
                                                    BasicTextField(
                                                        readOnly = true,
                                                        value = data.documentName,
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
                                                if(data.documentDescription.isNotEmpty()){
                                                    Column {
                                                        BasicTextField(
                                                            readOnly = true,
                                                            value = data.documentDescription,
                                                            onValueChange = { /* Handle value change if needed */ },
                                                            textStyle = TextStyle(
                                                                fontSize = 14.sp,
                                                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                                                color = colorResource(id = R.color.paraColor)
                                                            ),
                                                            singleLine = true,
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(end = 16.dp) // Adjust padding as needed
                                                        )

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Column(modifier = Modifier
                                        .weight(0.5f)
                                        .padding(end = 10.dp, top = 10.dp), horizontalAlignment = Alignment.End) {
                                        val urlLink =data.documentPath
                                        val fileName=data.documentName

                                        Row(modifier = Modifier.clickable { }) {
                                            Icon( painterResource(id = R.drawable.eye) ,
                                                contentDescription = "eye",
                                                tint = colorResource(id = R.color.blue),
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .padding(end = 3.dp)
                                            )
                                            Text(text = "View",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = colorResource(id = R.color.blue),
                                            )
                                        }
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


fun generateOrgDocDetailList(): List<OrgDocDetailData>
{
    return listOf(
        OrgDocDetailData(documentId = 230, documentName = "HR Document", documentDescription = "", documentPath  = "", modifiedDate = "", folderName = ""),
        OrgDocDetailData(documentId = 230, documentName = "Leave Policy", documentDescription = "About Leave", documentPath  = "", modifiedDate = "", folderName = ""),
        OrgDocDetailData(documentId = 230, documentName = "Attendance Policy", documentDescription = "About Attendance", documentPath  = "", modifiedDate = "", folderName = ""),
)
}



