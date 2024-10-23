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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.UnApproveDetailLeaveData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.FileNameSizeValidation
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.fileNameFormatCheck
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.LeaveViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


var leaveApprovalDetailDataList1 = mutableStateOf<List<UnApproveDetailLeaveData>>(emptyList())

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Leave_approvals(navController: NavController, leaveViewModel: LeaveViewModel, slNo: String, empId: String, empName: String)
{

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = empName,
            "leaveListApproval"
        ) },
        bottomBarContent = {},//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    { Leave_approvals_Screen(navController = navController, leaveViewModel = leaveViewModel, slNo = slNo, empId = empId) }
}


data class LeaveDetail(
    val slNo: String,
    val app: String,
    val rejectReason: String,
    val dt: String,
    val empId: String,
    val leaveType: String,
    val count: String,
    val toDate: String
)

data class LeaveApprovalItem(
    val approveChecked : Boolean,
    val rejectChecked : Boolean
)


@Suppress("BooleanLiteralArgument")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Leave_approvals_Screen(navController: NavController, leaveViewModel: LeaveViewModel, slNo: String, empId: String) {

    val context = LocalContext.current

    Column(modifier = Modifier
        .background(color = colorResource(id = R.color.backgroundColor))
        .fillMaxSize()
        .padding(top = 70.dp, bottom = 0.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        var reasonForReject by remember { mutableStateOf("") }


//FOR GETTING RESULT

//        var remarks = ""

        val loadingStatus = leaveViewModel.loadingStatus1

        flag = leaveViewModel.flag1

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        leaveViewModel.leaveApprovalDetailList.collectAsState().also {
            leaveApprovalDetailDataList1 = it as MutableState<List<UnApproveDetailLeaveData>>
        }

        // Accessing the 'reason' property from each item in the list
        val reasonList: List<String> = leaveApprovalDetailDataList1.value.map { leaveData ->
            leaveData.reasonForLeave
        }

        // Accessing the 'filePath' property from each item in the list
        val filePathList: List<String> = leaveApprovalDetailDataList1.value.map { leaveData ->
            leaveData.filePath
        }


        val remarks : String? = reasonList.getOrNull(0)
        val filePath : String? = filePathList.getOrNull(0)

        Log.d("QWERTY leaveApprovalDetailDataList1", "${leaveApprovalDetailDataList1.value}")


//FOR RECEIVED APPROVAL RESPONSE

        val loadingCircular = leaveViewModel.loading1

        if(loadingCircular.value)
        {
            circularProgression()
        }

        val leaveDetailsList = remember { mutableStateListOf<LeaveDetail>() }
        val leaveApprovalDetail = JSONArray()

        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateLeaveDetail()
        {

            var customDialog by remember { mutableStateOf(false) }
            var isAnyRejectChecked by remember { mutableStateOf(false) }


            fun updateLeaveDetails()
            {
                for (leaveDetail in leaveDetailsList)
                {
                    val jsonObject = JSONObject()

                    // Parse the input date string
                    val formatterFromDate = DateTimeFormatter.ofPattern("MM/dd/yyyy") // From Date
                    val formatterToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // To Date
                    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    //From Date format for API
                    val fromDateFormat = LocalDate.parse(leaveDetail.dt, formatterFromDate)
                    val fromDateApi =  fromDateFormat.format(formatterOutput)

                    //To Date format for API
                    val toDateFormat = LocalDate.parse(leaveDetail.toDate, formatterToDate)
                    val toDateApi =  toDateFormat.format(formatterOutput)

                    Log.d("Leave Approval", "For date conversion :fromDateApi : $fromDateApi, toDateApi:$toDateApi ")

                    val reason = if(leaveDetail.app == "0") reasonForReject else ""

                    jsonObject.put("Sl_No", leaveDetail.slNo)
                    jsonObject.put("Emp_Id", leaveDetail.empId)
                    jsonObject.put("App", leaveDetail.app)
                    jsonObject.put("dt", fromDateApi)
                    jsonObject.put("Reject_reason", reason)
                    jsonObject.put("Leave_Type", leaveDetail.leaveType)
                    jsonObject.put("Count", leaveDetail.count)
                    jsonObject.put("To_Date", toDateApi)

                    leaveApprovalDetail.put(jsonObject)

                }
            }

            val checkboxStateList = remember { mutableStateListOf<LeaveApprovalItem>() }


            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth())
            {

                Column(modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(0.80f)
                    .fillMaxWidth())
                {

                    val isFileAvailable = !filePath.isNullOrEmpty()


                    var isExpanded by remember { mutableStateOf(false) }

                    val maxLineLength = 60 // Maximum number of characters to display before truncating

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(bottom = 15.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                    ) {

                        //File

                        if(isFileAvailable)
                        {


//                            val baseUrl = "http://testing.2growhr.io/Upload/LeaveUpload\\"

                            val baseUrl = "${Constant.IMAGE_URL}Upload/LeaveUpload\\"

                            val fileName = filePath?.removePrefix(baseUrl)

                            val fileNameNew = fileName?.let { fileNameFormatCheck(it) }

                            Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp))
                            {
                                Column(modifier = Modifier.weight(0.3f).padding(top = 5.dp) )
                                {
                                    Text(text = "Attachment",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)),
                                        modifier = Modifier
                                            .align(Alignment.Start))
                                }

                                Column(modifier = Modifier.weight(0.7f).padding(top = 5.dp) )
                                {

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End)
                                    {


                                        Icon(
                                            painterResource(id = R.drawable.attachment_icon),
                                            contentDescription = "Attachment",
                                            tint = colorResource(id = R.color.themeColor),
                                            modifier = Modifier
                                                .size(22.dp).padding(end = 3.dp)

                                        )

                                        FileNameSizeValidation(fileName = fileNameNew!!, fileUrl = filePath)

                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp), color = colorResource(id = R.color.divider))

                        }


                        Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 8.dp, bottom = 5.dp))
                        {
                            Column(modifier = Modifier.weight(0.5f) )
                            {
                                Text(text = "Remarks",
                                    style = TextStyle(
                                        fontFamily = poppins_font,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(500),
                                        color = colorResource(id = R.color.black)),
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .align(Alignment.Start))
                            }
                        }


                        //For Remarks

                        Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                        {
                            Column(modifier = Modifier.fillMaxWidth())
                            {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                    shape = RoundedCornerShape(2.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.Start)
                                        .clickable {
                                            // Toggle the expanded state and tooltip visibility
                                            isExpanded = !isExpanded
                                        },
                                ) {
                                    Box( modifier = Modifier.fillMaxWidth())
                                    {
                                        (if (isExpanded) remarks else remarks?.take(
                                            maxLineLength
                                        ) + if (remarks?.length!! > maxLineLength) "..." else "")?.let {
                                            Text(
                                                text = it,
                                                style = TextStyle(
                                                    fontFamily = poppins_font,
                                                    fontSize = 13.sp, fontWeight = FontWeight(500),
                                                    color = colorResource(id = R.color.paraColor)
                                                ),
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }


                    Column(modifier = Modifier
                        .padding(bottom = 7.dp)
//                        .weight(if(leaveApprovalDetailDataList1.value.size > 1) 0.60f else 0.20f)
                        .background(color = colorResource(id = R.color.backgroundColor)) )
                    {

                        LazyColumn (modifier = Modifier.fillMaxSize())
                        {

                            if(leaveApprovalDetailDataList1.value.isEmpty())
                            {
                                loading = true
                            }
                            else
                            {
                                items(leaveApprovalDetailDataList1.value.size) {index->

                                    // Use the state list to determine the initial state of the checkboxes
                                    val initialCheckboxState = checkboxStateList.getOrNull(index) ?: LeaveApprovalItem(true, false)


                                    var approveChecked by remember { mutableStateOf(initialCheckboxState.approveChecked) }
                                    var rejectChecked by remember { mutableStateOf(initialCheckboxState.rejectChecked) }

                                    val data = leaveApprovalDetailDataList1.value[index]

                                    val employeeName = data.empName
                                    val slNo1 = data.slNo
                                    val fromDate = data.fromDate
                                    val dayType = if(data.dayType == "Hour") "${data.count} ${data.dayType}" else data.dayType


                                    // Parse the input date string
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                                    val date = LocalDateTime.parse(fromDate, formatter)
                                    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String

                                    val updatedLeaveDetail = LeaveDetail(
                                        slNo = slNo1,
                                        app = if (approveChecked) "1" else "0" , // Set app based on checkbox selection
                                        rejectReason = reasonForReject, // Set the default value for rejectReason as an empty string
                                        dt = formattedApiDate, // Assuming dt should be set to formattedApiDate
                                        empId = data.empId,
                                        leaveType = data.leaveType,
                                        count = data.count, // Assuming count should be an empty string, update it accordingly
                                        toDate = data.toDate
                                    )



                                    // Update the corresponding element in leaveDetailsList based on fromDate
                                    val leaveDetailIndex = leaveDetailsList.indexOfFirst { it.dt == formattedApiDate }

                                    if (leaveDetailIndex != -1)
                                    {
                                        leaveDetailsList[leaveDetailIndex] = updatedLeaveDetail
                                    }
                                    else
                                    {
                                        leaveDetailsList.add(updatedLeaveDetail)
                                    }


                                    if(checkboxStateList.size <= index)
                                    {
                                        checkboxStateList.add(LeaveApprovalItem(approveChecked,rejectChecked))
                                    }
                                    else
                                    {
                                        checkboxStateList[index] = LeaveApprovalItem(approveChecked,rejectChecked)
                                    }


                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 5.dp, end = 5.dp)
                                    ) {


                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row {
                                                Column {

                                                    Text(text = "For",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)))

                                                    Text(text = formattedDate,
                                                        style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp,
                                                        fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.black)))

                                                }


                                                Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                                    Button(
                                                        onClick = { },
                                                        shape = RoundedCornerShape(10),
                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade)),
                                                        contentPadding = PaddingValues(10.dp)

                                                    ) {
                                                        Text(
                                                            text = dayType,
                                                            style = MaterialTheme.typography.titleSmall,
                                                            color = colorResource(id = R.color.lightthemecolor)
                                                        )
                                                    }
                                                }
                                            }
                                            HorizontalDivider(
                                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                                color = colorResource(id = R.color.divider)
                                            )

                                            Row {
                                                Column {
                                                }
                                                Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {

                                                    Row {
                                                        Checkbox(
                                                            checked = approveChecked,
                                                            onCheckedChange = { checked ->
                                                                if (checked || rejectChecked)
                                                                {
                                                                    approveChecked = checked
                                                                    rejectChecked = false
                                                                    checkboxStateList[index] = LeaveApprovalItem(approveChecked, false)
                                                                    Log.d("Leave Approval", " approveChecked checkboxStateList : ${checkboxStateList.toList()} ")

                                                                }
                                                            },
                                                            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.green)),)
                                                        Text(
                                                            text = "Approve",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.green)),
                                                            modifier = Modifier.padding(start = 0.dp, top = 13.dp),
                                                        )
                                                        Checkbox(
                                                            checked = rejectChecked,
                                                            onCheckedChange = { checked ->
                                                                if (checked || approveChecked )
                                                                {
                                                                    rejectChecked  = checked
                                                                    approveChecked = false
                                                                    checkboxStateList[index] = LeaveApprovalItem(false, rejectChecked)

//                                                                rejectCheckboxStateList[index] = checked
                                                                    Log.d("Leave Approval", "rejectChecked checkboxStateList : ${checkboxStateList.toList()} ")

                                                                }
                                                            },
                                                            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.red)),
                                                        )
                                                        Text(
                                                            text = "Reject",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.red)),
                                                            modifier = Modifier.padding(start = 0.dp, top = 13.dp),
                                                        )
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



                Column(modifier = Modifier.weight(0.13f))
                {
                    Box(modifier = Modifier
                        .padding(top = 5.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(0),
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                                Button(
                                    onClick = {

                                        isAnyRejectChecked = checkboxStateList.any { it.rejectChecked }

                                        if (isAnyRejectChecked)
                                        {
                                            customDialog = true
                                        }
                                        else
                                        {
                                            updateLeaveDetails()
                                            Log.d("Approval", "rejectReasonFlag/leaveApprovalDetail : $leaveApprovalDetail")
                                            leaveViewModel.postLeaveStatusUpdate(navController,empId,slNo,leaveApprovalDetail.toString(), context)
                                            leaveViewModel.getLeaveApprovalList(navController,context, userViewModel.getSFCode())
                                            navController.navigate("leaveListApproval")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                    shape = RoundedCornerShape(5.dp),
                                    contentPadding = PaddingValues(vertical = 15.dp)
                                ) {
                                    Text(
                                        text = "Save",
                                        color = colorResource(id = R.color.white),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            @Composable
            fun RejectDialogBox()
            {
                val maxLength = 250
                Dialog(onDismissRequest = {
                    customDialog = false
                    reasonForReject = ""
                })
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // text and buttons
                        Column(modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .background(
                                color = colorResource(id = R.color.backgroundColor),
                                shape = RoundedCornerShape(percent = 10)
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(height = 36.dp))

                            Text(text = "Reject Reason", color = colorResource(id = R.color.black), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 10.dp))

                            Row(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth())
                            {
                                Text(
                                    text = "Reason", style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.paraColor),
                                )
                            }

                            TextField(
                                value = reasonForReject,
                                onValueChange = {if (it.length <= maxLength)
                                    reasonForReject = it },
                                modifier = Modifier
                                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                                    .fillMaxWidth()
                                    .heightIn(min = 70.dp)
                                    .background(color = colorResource(id = R.color.white)),
                                maxLines = 5,
                                textStyle = MaterialTheme.typography.titleMedium,
                                placeholder = { Text(text = "Enter Reason...") },
                                singleLine = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = colorResource(id = R.color.black),
                                    focusedBorderColor = colorResource(id = R.color.themeColor),
                                    unfocusedBorderColor = colorResource(R.color.white),
                                )
                            )


                            // buttons
                            Button(onClick = {

                                if(reasonForReject == "" || reasonForReject.isBlank())
                                {
                                    Constant.showToast(context, "Enter Reason")
                                    reasonForReject = ""
                                }
                                else
                                {
                                    updateLeaveDetails()
                                    customDialog = false
                                    Log.d("Approval", "Inside reject reason not empty $leaveApprovalDetail")
                                    leaveViewModel.postLeaveStatusUpdate(navController,empId,slNo,leaveApprovalDetail.toString(), context)
                                    leaveViewModel.getLeaveApprovalList(navController,context, userViewModel.getSFCode())
                                    navController.navigate("leaveListApproval")
                                }

                            }, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp)) {
                                Text(text = "Reject", color = colorResource(id = R.color.white), fontSize = 14.sp )
                            }
                        }

                    }

                }

            }


            if(customDialog)
            {
                RejectDialogBox()
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

            if(leaveApprovalDetailDataList1.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        leaveViewModel.fetchAndUpdateLeaveData(navController,context,slNo)
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
                        uiUpdateLeaveDetail()
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
fun uiLeaveApprovalDetailPreview() {

    val navController = rememberNavController()

    val leaveApprovalDetailDataList = generateLeaveDetailApprovalDataList()

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Prabhu R", "leaveListApproval") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        Column(modifier = Modifier
            .background(color = colorResource(id = R.color.backgroundColor))
            .fillMaxSize()
            .padding(top = 70.dp, bottom = 0.dp)
        ) {

            val flag by remember { mutableIntStateOf(1) }


            // Accessing the 'reason' property from each item in the list
            val reasonList: List<String> = leaveApprovalDetailDataList.map { leaveData ->
                leaveData.reasonForLeave
            }

            // Accessing the 'filePath' property from each item in the list
            val filePathList: List<String> = leaveApprovalDetailDataList.map { leaveData ->
                leaveData.filePath
            }


            val remarks : String? = reasonList.getOrNull(0)
            val filePath : String? = filePathList.getOrNull(0)

            val leaveDetailsList = remember { mutableStateListOf<LeaveDetail>() }
            val leaveApprovalDetail = JSONArray()

            @SuppressLint("NewApi")
            @Composable
            fun uiUpdateLeaveDetail()
            {

                var customDialog by remember { mutableStateOf(false) }
                var isAnyRejectChecked by remember { mutableStateOf(false) }


                fun updateLeaveDetails()
                {
                    for (leaveDetail in leaveDetailsList)
                    {
                        val jsonObject = JSONObject()

                        // Parse the input date string
                        val formatterFromDate = DateTimeFormatter.ofPattern("MM/dd/yyyy") // From Date
                        val formatterToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // To Date
                        val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                        //From Date format for API
                        val fromDateFormat = LocalDate.parse(leaveDetail.dt, formatterFromDate)
                        val fromDateApi =  fromDateFormat.format(formatterOutput)

                        //To Date format for API
                        val toDateFormat = LocalDate.parse(leaveDetail.toDate, formatterToDate)
                        val toDateApi =  toDateFormat.format(formatterOutput)

                        Log.d("Leave Approval", "For date conversion :fromDateApi : $fromDateApi, toDateApi:$toDateApi ")

                        val reason = if(leaveDetail.app == "0") "reasonForReject" else ""

                        jsonObject.put("Sl_No", leaveDetail.slNo)
                        jsonObject.put("Emp_Id", leaveDetail.empId)
                        jsonObject.put("App", leaveDetail.app)
                        jsonObject.put("dt", fromDateApi)
                        jsonObject.put("Reject_reason", reason)
                        jsonObject.put("Leave_Type", leaveDetail.leaveType)
                        jsonObject.put("Count", leaveDetail.count)
                        jsonObject.put("To_Date", toDateApi)

                        leaveApprovalDetail.put(jsonObject)

                    }
                }

                val checkboxStateList = remember { mutableStateListOf<LeaveApprovalItem>() }


                Column(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth())
                {

                    Column(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .weight(0.80f)
                        .fillMaxWidth())
                    {

                        val isFileAvailable = !filePath.isNullOrEmpty()


                        var isExpanded by remember { mutableStateOf(false) }

                        val maxLineLength = 60 // Maximum number of characters to display before truncating

                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 15.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ) {

                            //File

                            if(isFileAvailable)
                            {


                                val baseUrl = "http://testing.2growhr.io/Upload/LeaveUpload\\"

                                val fileName = filePath?.removePrefix(baseUrl)

                                val fileNameNew = fileName?.let { fileNameFormatCheck(it) }

                                Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp))
                                {
                                    Column(modifier = Modifier.weight(0.3f).padding(top = 5.dp) )
                                    {
                                        Text(text = "Attachment",
                                            style = TextStyle(
                                                fontFamily = poppins_font,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight(500),
                                                color = colorResource(id = R.color.black)),
                                            modifier = Modifier
                                                .align(Alignment.Start))
                                    }

                                    Column(modifier = Modifier.weight(0.7f).padding(top = 5.dp) )
                                    {

                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End)
                                        {


                                            Icon(
                                                painterResource(id = R.drawable.attachment_icon),
                                                contentDescription = "Attachment",
                                                tint = colorResource(id = R.color.themeColor),
                                                modifier = Modifier
                                                    .size(22.dp).padding(end = 3.dp)

                                            )

                                            FileNameSizeValidation(fileName = fileNameNew!!, fileUrl = filePath)

                                        }
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp), color = colorResource(id = R.color.divider))

                            }


                            Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top = 8.dp, bottom = 5.dp))
                            {
                                Column(modifier = Modifier.weight(0.5f) )
                                {
                                    Text(text = "Remarks",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.black)),
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .align(Alignment.Start))
                                }
                            }


                            //For Remarks

                            Row( modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =5.dp, bottom = 5.dp))
                            {
                                Column(modifier = Modifier.fillMaxWidth())
                                {
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                        shape = RoundedCornerShape(2.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                            .clickable {
                                                // Toggle the expanded state and tooltip visibility
                                                isExpanded = !isExpanded
                                            },
                                    ) {
                                        Box( modifier = Modifier.fillMaxWidth())
                                        {
                                            (if (isExpanded) remarks else remarks?.take(
                                                maxLineLength
                                            ) + if (remarks?.length!! > maxLineLength) "..." else "")?.let {
                                                Text(
                                                    text = it,
                                                    style = TextStyle(
                                                        fontFamily = poppins_font,
                                                        fontSize = 13.sp, fontWeight = FontWeight(500),
                                                        color = colorResource(id = R.color.paraColor)
                                                    ),
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        }


                        Column(modifier = Modifier
                            .padding(bottom = 7.dp)
                            .background(color = colorResource(id = R.color.backgroundColor)) )
                        {

                            LazyColumn (modifier = Modifier.fillMaxSize())
                            {

                                items(leaveApprovalDetailDataList.size) {index->

                                    // Use the state list to determine the initial state of the checkboxes
                                    val initialCheckboxState = checkboxStateList.getOrNull(index) ?: LeaveApprovalItem(true, false)


                                    var approveChecked by remember { mutableStateOf(initialCheckboxState.approveChecked) }
                                    var rejectChecked by remember { mutableStateOf(initialCheckboxState.rejectChecked) }

                                    val data = leaveApprovalDetailDataList[index]

                                    val slNo1 = data.slNo
                                    val fromDate = data.fromDate
                                    val dayType = if(data.dayType == "Hour") "${data.count} ${data.dayType}" else data.dayType


                                    // Parse the input date string
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                                    val date = LocalDateTime.parse(fromDate, formatter)
                                    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    val formattedApiDate = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) as String

                                    val updatedLeaveDetail = LeaveDetail(
                                        slNo = slNo1,
                                        app = if (approveChecked) "1" else "0" , // Set app based on checkbox selection
                                        rejectReason = "reasonForReject", // Set the default value for rejectReason as an empty string
                                        dt = formattedApiDate, // Assuming dt should be set to formattedApiDate
                                        empId = data.empId,
                                        leaveType = data.leaveType,
                                        count = data.count, // Assuming count should be an empty string, update it accordingly
                                        toDate = data.toDate
                                    )



                                    // Update the corresponding element in leaveDetailsList based on fromDate
                                    val leaveDetailIndex = leaveDetailsList.indexOfFirst { it.dt == formattedApiDate }

                                    if (leaveDetailIndex != -1)
                                    {
                                        leaveDetailsList[leaveDetailIndex] = updatedLeaveDetail
                                    }
                                    else
                                    {
                                        leaveDetailsList.add(updatedLeaveDetail)
                                    }


                                    if(checkboxStateList.size <= index)
                                    {
                                        checkboxStateList.add(LeaveApprovalItem(approveChecked,rejectChecked))
                                    }
                                    else
                                    {
                                        checkboxStateList[index] = LeaveApprovalItem(approveChecked,rejectChecked)
                                    }


                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                        modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 5.dp, end = 5.dp)
                                    ) {


                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row {
                                                Column {

                                                    Text(text = "For",
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.paraColor)))

                                                    Text(text = formattedDate,
                                                        style = TextStyle(
                                                            fontFamily = poppins_font,
                                                            fontSize = 13.sp,
                                                            fontWeight = FontWeight(500),
                                                            color = colorResource(id = R.color.black)))

                                                }


                                                Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                                                    Button(
                                                        onClick = { },
                                                        shape = RoundedCornerShape(10),
                                                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade)),
                                                        contentPadding = PaddingValues(10.dp)

                                                    ) {
                                                        Text(
                                                            text = dayType,
                                                            style = MaterialTheme.typography.titleSmall,
                                                            color = colorResource(id = R.color.lightthemecolor)
                                                        )
                                                    }
                                                }
                                            }
                                            HorizontalDivider(
                                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                                color = colorResource(id = R.color.divider)
                                            )

                                            Row {
                                                Column {
                                                }
                                                Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {

                                                    Row {
                                                        Checkbox(
                                                            checked = approveChecked,
                                                            onCheckedChange = { checked ->
                                                                if (checked || rejectChecked)
                                                                {
                                                                    approveChecked = checked
                                                                    rejectChecked = false
                                                                    checkboxStateList[index] = LeaveApprovalItem(approveChecked, false)
                                                                    Log.d("Leave Approval", " approveChecked checkboxStateList : ${checkboxStateList.toList()} ")

                                                                }
                                                            },
                                                            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.green)),)
                                                        Text(
                                                            text = "Approve",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.green)),
                                                            modifier = Modifier.padding(start = 0.dp, top = 13.dp),
                                                        )
                                                        Checkbox(
                                                            checked = rejectChecked,
                                                            onCheckedChange = { checked ->
                                                                if (checked || approveChecked )
                                                                {
                                                                    rejectChecked  = checked
                                                                    approveChecked = false
                                                                    checkboxStateList[index] = LeaveApprovalItem(false, rejectChecked)

//                                                                rejectCheckboxStateList[index] = checked
                                                                    Log.d("Leave Approval", "rejectChecked checkboxStateList : ${checkboxStateList.toList()} ")

                                                                }
                                                            },
                                                            colors = CheckboxDefaults.colors(checkedColor = colorResource(id = R.color.red)),
                                                        )
                                                        Text(
                                                            text = "Reject",
                                                            style = TextStyle(
                                                                fontFamily = poppins_font,
                                                                fontSize = 13.sp,
                                                                fontWeight = FontWeight(500),
                                                                color = colorResource(id = R.color.red)),
                                                            modifier = Modifier.padding(start = 0.dp, top = 13.dp),
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }

                            }

                        }

                    }



                    Column(modifier = Modifier.weight(0.13f))
                    {
                        Box(modifier = Modifier
                            .padding(top = 5.dp)
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                shape = RoundedCornerShape(0),
                                modifier = Modifier.align(Alignment.BottomEnd)
                            ) {
                                Column(modifier = Modifier.padding(vertical = 15.dp, horizontal = 22.dp)) {
                                    Button(
                                        onClick = {

                                            isAnyRejectChecked = checkboxStateList.any { it.rejectChecked }

                                            if (isAnyRejectChecked)
                                            {
                                                customDialog = true
                                            }
                                            else
                                            {
                                                updateLeaveDetails()
                                                navController.navigate("leaveListApproval")
                                            }

                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                        shape = RoundedCornerShape(5.dp),
                                        contentPadding = PaddingValues(vertical = 15.dp)
                                    ) {
                                        Text(
                                            text = "Save",
                                            color = colorResource(id = R.color.white),
                                            style = MaterialTheme.typography.titleMedium
                                        )
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
                    uiUpdateLeaveDetail()
                }
                else -> {
                    noDataView()
                }
            }


        }
    }


}

fun generateLeaveDetailApprovalDataList(): List<UnApproveDetailLeaveData>
{
    return listOf(
        UnApproveDetailLeaveData(slNo = "1314", empId = "EMP10769", empName = "Prabhu R", flag = "0", reasonForLeave = "Leave", fromDate = "2023-04-12T00:00:00", toDate = "2023-04-12T00:00:00", leaveTypeName = "CASUAL LEAVE", leaveType = "112", dayType = "Full Day", count = "1", filePath = ""),
        UnApproveDetailLeaveData(slNo = "1314", empId = "EMP10769", empName = "Prabhu R", flag = "0", reasonForLeave = "Leave", fromDate = "2023-04-13T00:00:00", toDate = "2023-04-13T00:00:00", leaveTypeName = "CASUAL LEAVE", leaveType = "112", dayType = "Full Day", count = "1", filePath = ""),
        UnApproveDetailLeaveData(slNo = "1314", empId = "EMP10769", empName = "Prabhu R", flag = "0", reasonForLeave = "Leave", fromDate = "2023-04-14T00:00:00", toDate = "2023-04-14T00:00:00", leaveTypeName = "CASUAL LEAVE", leaveType = "112", dayType = "Full Day", count = "1", filePath = ""),
    )
}

