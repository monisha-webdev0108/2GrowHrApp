package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.payroll.twogrowhr.Model.ResponseModel.CTC
import com.payroll.twogrowhr.Model.ResponseModel.Deduction
import com.payroll.twogrowhr.Model.ResponseModel.Earning
import com.payroll.twogrowhr.Model.ResponseModel.Tot
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.SalaryDetailViewModel


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Salary(navController: NavController,salaryDetailViewModel: SalaryDetailViewModel)
{


    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Salary Details", "Reports") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    { SalaryScreen(salaryDetailViewModel = salaryDetailViewModel) }

}
@Composable
fun SalaryScreen(salaryDetailViewModel: SalaryDetailViewModel)
{

    val earningList = salaryDetailViewModel.earningList.collectAsState()
    val deductionList = salaryDetailViewModel.deductionList.collectAsState()
    val ctcList = salaryDetailViewModel.ctcList.collectAsState()
    val totList = salaryDetailViewModel.totList.collectAsState()

    val loadingStatus = salaryDetailViewModel.loadingStatus
    val flag1 = salaryDetailViewModel.flag

    var loading by remember { mutableStateOf(true) }


    if(loading && !salaryDetailViewModel.loadingStatus)
    {
        circularProgression()
    }



    if(loadingStatus)
    {
        loading = true
    }
    else
    {
        loading = false

        when (flag1) {

            0 ->
            {
                loading = true
            }

            1 -> {

                Log.d("SALARY DETAILS...", "EarningList: ${earningList.value}")
                Log.d("SALARY DETAILS...", "DeductionList: ${deductionList.value}")
                Log.d("SALARY DETAILS...", "ctcList: ${ctcList.value}")
                Log.d("SALARY DETAILS...", "totList: ${totList.value}")

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)

                ){
                    LazyColumn {
                        item {
                            tableEarningHeader()
                        }
                        items(earningList.value) { rowData ->
                            tableEarning(rowData)
                        }
                        item {
                            tableEarningFooter(totList.value)
                        }
                        item {
                            tableDeductionHeader()
                        }
                        items(deductionList.value) { rowData ->
                            tableRowDeduction(rowData)
                        }
                        item {
                            tableDeductionFooter(totList.value)
                        }
                        item {
                            tableFooterNetpay(totList.value)
                        }
                        item {
                            tableAddonHeader()
                        }
                        items(ctcList.value) { rowData ->
                            tableRowAddon(rowData)
                        }
                        item {
                            tableFooterAddon(totList.value)
                        }
                        item {
                            notify()
                        }
                    }
                }
            }
            2 ->
            {
                noDataView()
            }
            else ->
            {
                exceptionScreen()
            }
        }
    }
}
@Composable
fun tableEarningHeader() {
    Divider(color = colorResource(id = R.color.lightthemecolor))

    Column(
        modifier = Modifier.fillMaxWidth(),
    ){
        Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column( modifier = Modifier.weight(1f)){
                Text(
                    text = "Earnings",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = "Month",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f)){
                Text(
                    text = "Annual",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

        }
    }


    Divider(color = colorResource(id = R.color.lightthemecolor))
}

@Composable
fun tableEarning(data: Earning)
{

    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Sc_Name,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Monthly,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Annual,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

        }
    }

    Divider(color = colorResource(id = R.color.divider))
}

@Composable
fun tableEarningFooter(totList: List<Tot>)
{

    if(totList.isNotEmpty())
    {
        var monthly = ""
        var annual = ""

        val scNameToFind = "TotEar"



        val deductionValue = totList.find { it.Sc_Name == scNameToFind }

        if (deductionValue != null)
        {
            monthly = deductionValue.Monthly
            annual = deductionValue.Annual
            Log.d("Found deductionValue Item", "Sc_Name: $scNameToFind, Monthly: $monthly, Annual: $annual")
        }
        else
        {
            Log.d("Item Not Found", "Sc_Name: $scNameToFind not found in the list")
        }

        Card(
            modifier = Modifier.padding(top = 10.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.white))
        ) {

            Column(modifier = Modifier.fillMaxWidth())
            {
                Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {
                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = "Total Earnings",
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = monthly,
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = annual,
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun tableDeductionHeader()
{
    Divider(color = colorResource(id = R.color.lightthemecolor), modifier = Modifier.padding(top=10.dp))

    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Deduction",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Month",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Annual",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
    Divider(color = colorResource(id = R.color.lightthemecolor))
}

@Composable
fun tableRowDeduction(data: Deduction)
{
    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween)
        {

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Sc_Name,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Monthly,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Annual,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

    Divider(color = colorResource(id = R.color.divider))
}

@Composable
fun tableDeductionFooter(totList: List<Tot>) {


    if(totList.isNotEmpty())
    {

        var monthly = ""
        var annual = ""

        val scNameToFind = "TotDed"
        val deductionValue = totList.find { it.Sc_Name == scNameToFind }

        if (deductionValue != null)
        {
            monthly = deductionValue.Monthly
            annual = deductionValue.Annual
            Log.d("Found deductionValue Item", "Sc_Name: $scNameToFind, Monthly: $monthly, Annual: $annual")
        }
        else {
            Log.d("Item Not Found", "Sc_Name: $scNameToFind not found in the list")
        }
        Card(
            modifier = Modifier.padding(top = 10.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.white))
        ) {

            Column(modifier = Modifier.fillMaxWidth())
            {
                Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = "Total Deductions",
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = monthly,
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = annual,
                            color = colorResource(id = R.color.themeColor),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }


    }



}
@Composable
fun tableFooterNetpay(totList: List<Tot>)
{


    if(totList.isNotEmpty())
    {
        var monthly = ""
        var annual = ""

        val scNameToFind = "Net" // Replace with the Sc_Name you're looking for

        val deductionValue = totList.find { it.Sc_Name == scNameToFind }

        if (deductionValue != null) {
            monthly = deductionValue.Monthly
            annual = deductionValue.Annual
            Log.d("Found deductionValue Item", "Sc_Name: $scNameToFind, Monthly: $monthly, Annual: $annual")
        }
        else
        {
            Log.d("Item Not Found", "Sc_Name: $scNameToFind not found in the list")
        }

        Card(
            modifier = Modifier.padding(top = 10.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.lightthemecolor))
        ) {

            Column(modifier = Modifier.fillMaxWidth())
            {
                Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = "Net pay*",
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = monthly,
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = annual,
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun tableAddonHeader()
{
    Divider(color = colorResource(id = R.color.lightthemecolor), modifier = Modifier.padding(top=10.dp))

    Column(modifier = Modifier.fillMaxWidth())
    {
        Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween )
        {

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Addons",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Month",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = "Annual",
                    color = colorResource(id = R.color.themeColor),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

    Divider(color = colorResource(id = R.color.lightthemecolor))
}
@Composable
fun tableRowAddon(data: CTC)
{
    Column(modifier = Modifier.fillMaxWidth())
    {
        Row( modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween )
        {
            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Sc_Name,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Monthly,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            Column(modifier = Modifier.weight(1f))
            {
                Text(
                    text = data.Annual,
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }

    Divider(color = colorResource(id = R.color.divider))
}

@Composable
fun tableFooterAddon(totList: List<Tot>)
{

    if(totList.isNotEmpty())
    {
        var monthly = ""
        var annual = ""

        val scNameToFind = "ctc" // Replace with the Sc_Name you're looking for

        val deductionValue = totList.find { it.Sc_Name == scNameToFind }

        if (deductionValue != null) {
            monthly = deductionValue.Monthly
            annual = deductionValue.Annual
            Log.d("Found deductionValue Item", "Sc_Name: $scNameToFind, Monthly: $monthly, Annual: $annual")
        } else {
            Log.d("Item Not Found", "Sc_Name: $scNameToFind not found in the list")
        }

        Card(
            modifier = Modifier.padding(top = 10.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.themeColor))
        ) {
            Column(modifier = Modifier.fillMaxWidth())
            {
                Row(modifier = Modifier.padding( 10.dp), horizontalArrangement = Arrangement.SpaceBetween)
                {

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = "Cost To Company",
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = monthly,
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                    Column(modifier = Modifier.weight(1f))
                    {
                        Text(
                            text = annual,
                            color = colorResource(id = R.color.white),
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun notify()
{
    Column(modifier = Modifier.padding( 10.dp)) {
        Text(text = "*The displayed Net pay does not include any Income tax,Professional tax or other deductions",
            style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 12.sp,
                fontWeight = FontWeight(500),
            ),
            color = colorResource(id = R.color.paraColor)
        )
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun uiSalaryPreview() {

    val navController = rememberNavController()

    val earningList = generateEarningDataList()
    val deductionList = generateDeductionDataList()
    val ctcList = generateCTCDataList()
    val totList = generateTotDataList()

    val flag1 = 1

    AppScaffold1(
        topBarContent = { TopBarBackNavigation(navController = navController, title = "Salary Details", "Reports") },
        bottomBarContent = { },
        onBack = { navController.navigateUp() }
    )
    {


        when (flag1) {

            1 -> {

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 80.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)

                ){
                    LazyColumn {
                        item {
                            tableEarningHeader()
                        }
                        items(earningList) { rowData ->
                            tableEarning(rowData)
                        }
                        item {
                            tableEarningFooter(totList)
                        }
                        item {
                            tableDeductionHeader()
                        }
                        items(deductionList) { rowData ->
                            tableRowDeduction(rowData)
                        }
                        item {
                            tableDeductionFooter(totList)
                        }
                        item {
                            tableFooterNetpay(totList)
                        }
                        item {
                            tableAddonHeader()
                        }
                        items(ctcList) { rowData ->
                            tableRowAddon(rowData)
                        }
                        item {
                            tableFooterAddon(totList)
                        }
                        item {
                            notify()
                        }
                    }
                }
            }
             else ->
            {
                noDataView()
            }
        }

    }
}


fun generateEarningDataList(): List<Earning>
{
    return listOf(
        Earning(Sc_Name  = "Basic", Monthly = "25,000.00", Annual  = "300,000.00"),
        Earning(Sc_Name  = "Dearness Allowance", Monthly = "10,000.00", Annual  = "120,000.00"),
        Earning(Sc_Name  = "House Rent Allowance", Monthly = "15,000.00", Annual  = "180,000.00")
    )
}

fun generateDeductionDataList(): List<Deduction>
{
    return listOf(
        Deduction(Sc_Name  = "EPF Employee", Monthly = "6,000.00", Annual  = "72,000.00"),
        Deduction(Sc_Name  = "ESI Employee", Monthly = "0.00", Annual  = "0.00"),
 )
}

fun generateCTCDataList(): List<CTC>
{
    return listOf(
        CTC(Sc_Name  = "PRE TAX DEDUCTION 1", Monthly = "500.00", Annual  = "6,000.00"),
        CTC(Sc_Name  = "EPF Employeer", Monthly = "6,000.00", Annual  = "72,000.00"),
        CTC(Sc_Name  = "ESI Employeer", Monthly = "0.00", Annual  = "0.00")
    )
}

fun generateTotDataList(): List<Tot>
{
    return listOf(
        Tot(Sc_Name  = "ctc", Monthly = "56,500.00", Annual  = "678,000.00"),
        Tot(Sc_Name  = "Gross", Monthly = "50,000.00", Annual  = "600,000.00"),
        Tot(Sc_Name  = "Net", Monthly = "44,000.00", Annual  = "528,000.00"),
        Tot(Sc_Name  = "Totctc", Monthly = "6,500.00", Annual  = "78,000.00"),
        Tot(Sc_Name  = "TotDed", Monthly = "6,000.00", Annual  = "72,000.00"),
        Tot(Sc_Name  = "TotEar", Monthly = "50,000.00", Annual  = "600,000.00")
    )
}

