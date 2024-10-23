package com.payroll.twogrowhr.Model.View

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.LocalContext
import com.payroll.twogrowhr.Model.ResponseModel.ApprovalList
import com.payroll.twogrowhr.Model.ResponseModel.DocumentDetailsData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.viewModel.ApprovalListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun approvalList(
    navController: NavController,
    approvalViewModel: ApprovalListViewModel
) {
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Approvals",
            "HomeScreen"
        ) },
        bottomBarContent = {},//if(isLoggedIn.value) { BottomNav(navController) }
        onBack = { navController.navigateUp() }
    )
    { ApprovalList_Screen(navController = navController,approvalViewModel = approvalViewModel) }
}

@Composable
fun ApprovalList_Screen(navController: NavController,approvalViewModel: ApprovalListViewModel) {

    val textArray = listOf("Leave", "Regularization", "Remote Work", "On Duty", "Over Time", "Comp Off", "Loan")

    val context = LocalContext.current

    val approvalList = approvalViewModel.approvalList.collectAsState()

// Fetch and update the payslip list when entering the page

    var flag by remember { mutableIntStateOf(0) }

//FOR GETTING RESULT

    val loadingStatus = approvalViewModel.loadingStatus3

    flag = approvalViewModel.flag3

    var loading by remember { mutableStateOf(true) }


    BackPressHandler(onBackPressed = { navController.navigate("HomeScreen") })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
    ) {


        //FOR RECEIVED LIST RESPONSE

        if(loading && !approvalViewModel.loadingStatus)
        {
            circularProgression()
        }




        @Composable
        fun uiUpdate() {

            LazyColumn {
                items(textArray) { title ->

                    val approvalListItem = approvalList.value.firstOrNull()

                    val countText = when (title) {
                        "Leave" -> "${approvalListItem?.leaveCount ?: 0}"
                        "Regularization" -> "${approvalListItem?.regularizeCount ?: 0}"
                        "Remote Work" -> "${approvalListItem?.wfhCount ?: 0}"
                        "On Duty" -> "${approvalListItem?.odCount ?: 0}"
                        "Over Time" -> "${approvalListItem?.shiftCount ?: 0}"
                        "Comp Off" -> "${approvalListItem?.compOffCount ?: 0}"

                        "Loan" -> "${approvalListItem?.loanCount ?: 0}"

                        else -> ""
                    }

                    val approvalCount = countText.toInt() > 0


                    val destination = when (title) {
                        "Leave" -> "leaveListApproval"
                        "Regularization" -> "regularizedApproval"
                        "Remote Work" -> "wfhApproval"
                        "On Duty" -> "odApproval"
                        "Over Time" -> "otApproval"
                        "Comp Off" -> "CompoOffApproval"
                        "Loan" -> "LoanApprovalList"

                        else -> ""
                    }



                    Card(
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(bottom = 10.dp).clickable {
                                if (approvalCount && destination.isNotEmpty()) {
                                    navController.navigate(destination)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colorResource(id = R.color.black)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                if (approvalCount) {
                                    Box(
                                        modifier = Modifier.width(80.dp).height(30.dp).background(
                                                color = colorResource(id = R.color.toolight_themecolor),
                                                shape = RoundedCornerShape(2.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Pending",
                                            color = colorResource(id = R.color.lightthemecolor),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.width(30.dp).height(30.dp).background(
                                                color = colorResource(id = R.color.dark_theme_color),
                                                shape = RoundedCornerShape(2.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Text(
                                            text = countText,
                                            color = colorResource(id = R.color.lightthemecolor),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Column(modifier = Modifier.padding(start = 10.dp))
                                    {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = "Menu",
                                            tint = colorResource(id = R.color.black),
                                            modifier = Modifier.size(25.dp)
                                        )
                                    }
                                }
                                else {
                                    Box(modifier = Modifier.width(110.dp).height(30.dp).background(
                                            color = colorResource(id = R.color.light_gray),
                                            shape = RoundedCornerShape(2.dp)
                                        ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No Request",
                                            color = colorResource(id = R.color.gray),
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                    Column(modifier = Modifier.padding(start = 10.dp))
                                    {
                                        Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "Menu",
                                                tint = colorResource(id = R.color.black),
                                                modifier = Modifier.size(25.dp).alpha(0.3f),
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

            if(approvalList.value.isEmpty())
            {
                when (flag)
                {
                    0 -> {
                        loading = true
                    }
                    1 -> {
                        approvalViewModel.getApprovalDetails(navController, context = context, userViewModel.getSFCode(), userViewModel.getOrg() )
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
fun uiApprovalListPreview()
{

    val navController = rememberNavController()
    val flag = 1

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Approvals", "HomeScreen") },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {


        val textArray = listOf("Leave", "Regularization", "Remote Work", "On Duty", "Over Time", "Comp Off", "Loan")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
        ) {


            val approvalList = generateApprovalList()


            @Composable
            fun uiUpdate() {

                LazyColumn {
                    items(textArray) { title ->

                        val approvalListItem = approvalList.firstOrNull()

                        val countText = when (title) {
                            "Leave" -> "${approvalListItem?.leaveCount ?: 0}"
                            "Regularization" -> "${approvalListItem?.regularizeCount ?: 0}"
                            "Remote Work" -> "${approvalListItem?.wfhCount ?: 0}"
                            "On Duty" -> "${approvalListItem?.odCount ?: 0}"
                            "Over Time" -> "${approvalListItem?.shiftCount ?: 0}"
                            "Comp Off" -> "${approvalListItem?.compOffCount ?: 0}"

                            "Loan" -> "${approvalListItem?.loanCount ?: 0}"

                            else -> ""
                        }

                        val approvalCount = countText.toInt() > 0


                        val destination = when (title) {
                            "Leave" -> "leaveListApproval"
                            "Regularization" -> "regularizedApproval"
                            "Remote Work" -> "wfhApproval"
                            "On Duty" -> "odApproval"
                            "Over Time" -> "otApproval"
                            "Comp Off" -> "CompoOffApproval"
                            "Loan" -> "LoanApprovalList"

                            else -> ""
                        }



                        Card(
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .clickable {
                                    if (approvalCount && destination.isNotEmpty()) {
                                        navController.navigate(destination)
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .padding(top = 10.dp, bottom = 10.dp, start = 14.dp, end = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = colorResource(id = R.color.black)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(1f),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                    if (approvalCount) {
                                        Box(
                                            modifier = Modifier
                                                .width(80.dp)
                                                .height(30.dp)
                                                .background(
                                                    color = colorResource(id = R.color.toolight_themecolor),
                                                    shape = RoundedCornerShape(2.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Pending",
                                                color = colorResource(id = R.color.lightthemecolor),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                        Box(
                                            modifier = Modifier
                                                .width(30.dp)
                                                .height(30.dp)
                                                .background(
                                                    color = colorResource(id = R.color.dark_theme_color),
                                                    shape = RoundedCornerShape(2.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {

                                            Text(
                                                text = countText,
                                                color = colorResource(id = R.color.lightthemecolor),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                        Column(modifier = Modifier.padding(start = 10.dp))
                                        {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "Menu",
                                                tint = colorResource(id = R.color.black),
                                                modifier = Modifier.size(25.dp)
                                            )
                                        }
                                    }
                                    else {
                                        Box(modifier = Modifier
                                            .width(110.dp)
                                            .height(30.dp)
                                            .background(
                                                color = colorResource(id = R.color.light_gray),
                                                shape = RoundedCornerShape(2.dp)
                                            ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "No Request",
                                                color = colorResource(id = R.color.gray),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                        }
                                        Column(modifier = Modifier.padding(start = 10.dp))
                                        {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                                contentDescription = "Menu",
                                                tint = colorResource(id = R.color.black),
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .alpha(0.3f),
                                            )
                                        }
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


fun generateApprovalList(): List<ApprovalList>
{
    return listOf(ApprovalList(leaveCount = 5, wfhCount = 0, odCount = 3, regularizeCount = 6, shiftCount = 0, compOffCount = 0, loanCount = 0))
}




