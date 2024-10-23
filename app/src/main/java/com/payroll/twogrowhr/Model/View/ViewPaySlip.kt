package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.downloadImage
import com.payroll.twogrowhr.components.downloadImageWithProgression
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.PaySlipHeadDetailsViewModel
import kotlinx.coroutines.launch
import java.net.URI


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun ViewPayslip(navController: NavController, paySlipHeadDetailsViewModel: PaySlipHeadDetailsViewModel) {


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "View Payslip",
            "payslip"
        ) },
        drawerContent = { },
        bottomBarContent = {downloadButton(navController = navController, paySlipHeadDetailsViewModel=paySlipHeadDetailsViewModel) })
    { ViewPayslip_Screen(navController = navController, paySlipHeadDetailsViewModel=paySlipHeadDetailsViewModel) }

}

@Composable
fun ViewPayslip_Screen(navController: NavController, paySlipHeadDetailsViewModel: PaySlipHeadDetailsViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val urlPdf = navBackStackEntry?.arguments?.getString("urlpdf")
    val urlPdfShown = if(urlPdf.isNullOrEmpty()) "" else urlPdf
    Log.d("urlPdfShown", urlPdfShown)

    val payHeadDetailList = paySlipHeadDetailsViewModel.paySlipHeadList.collectAsState()
    val payEarningDetailList = paySlipHeadDetailsViewModel.paySlipEarningList.collectAsState()
    val payDeductionDetailList = paySlipHeadDetailsViewModel.paySlipDeductionList.collectAsState()
    val payOtherDeductionDetailList = paySlipHeadDetailsViewModel.paySlipOtherDeductionList.collectAsState()
    var totalDeduction by remember { mutableStateOf(0) }
    var totalEarnings by remember { mutableStateOf(0) }
    var totalDeductionLop by remember { mutableStateOf(0) }
    val lopAmountList: List<Int> = payHeadDetailList.value.map { it.Lop_Amount }
    val totalLopAmount: Int = lopAmountList.sum()
    Log.d("totalLopAmount",totalLopAmount.toString())
    val showLoading = remember { mutableStateOf(false) }

    if (showLoading.value) {
            circularProgression()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 70.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
            .verticalScroll(rememberScrollState())
    ){
        for (event in payHeadDetailList.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Ensure the Column takes up the full width
                    .padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(), // Make the Box take up the full width
                    contentAlignment = Alignment.Center // Center content within the Box
                ) {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.dark_theme_color)),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Text(
                            text = " ${event.Month} - ${event.Year}",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W600, // Use W600 for bold
                                color = colorResource(id = R.color.themeColor)
                            )
                        )
                    }
                }
            }

            Column(Modifier.padding(bottom = 10.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), modifier = Modifier.fillMaxWidth(1f),shape = RoundedCornerShape(3.dp)) {
                    Row(modifier = Modifier.padding(10.dp)) {
                        Column(
                            modifier = Modifier
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    val x = size.width - strokeWidth / 2
                                    drawLine(
                                        color = Color.LightGray,
                                        start = Offset(x, 0f),
                                        end = Offset(x, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                }
                                .weight(1f)
                        ) {
                            Text(text = "Total days", style = MaterialTheme.typography.titleSmall,
                                color = colorResource(id = R.color.green)
                            )
                            Text(text = event.Day_Month, style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    val x = size.width - strokeWidth / 2
                                    drawLine(
                                        color = Color.LightGray,
                                        start = Offset(x, 0f),
                                        end = Offset(x, size.height),
                                        strokeWidth = strokeWidth
                                    )
                                }
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(text = "Lop days", style = MaterialTheme.typography.titleSmall,
                                color = colorResource(id = R.color.red)
                            )
                            Text(text = event.Loss_Of_Days, style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(text = "Salary days", style = MaterialTheme.typography.titleSmall,
                                color = colorResource(id = R.color.blue)
                            )
                            Text(text = event.Working_Day, style = MaterialTheme.typography.titleMedium,
                                color = colorResource(id = R.color.black)
                            )
                        }
                    }
                }
            }
        }

        /* ============================================Start Earning============================================*/
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(text = "Earnings", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium)
        }

            Column(modifier = Modifier.padding(bottom = 10.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), modifier = Modifier.fillMaxWidth(1f),shape = RoundedCornerShape(3.dp)) {
                    Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Components", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Amount", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                        }
                    }
                    Divider(color = colorResource(id = R.color.lightthemecolor))
                    totalEarnings = 0
                    for(earning in payEarningDetailList.value){
                        totalEarnings += earning.Amount
                        Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                BasicTextField(
                                    readOnly = true,
                                    value = earning.Salary_Component_Name,
                                    onValueChange = { /* Handle value change if needed */ },
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        color = colorResource(id = R.color.paraColor)
                                    ),
                                    singleLine = true,
                                    )
                            }
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                Text(text = "${earning.Amount}", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                            }

                        }
                        Divider(color = colorResource(id = R.color.divider))
                    }
                    Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Gross pay (A)", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "$totalEarnings", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                        }


                    }
                }
            }

        /* ============================================End Earning============================================*/

        /* ============================================Start Deduction============================================*/
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(text = "Deductions", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium)
        }

        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), modifier = Modifier.fillMaxWidth(1f),shape = RoundedCornerShape(3.dp)) {
                Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Component", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Amount", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                    }

                }
                Divider(color = colorResource(id = R.color.lightthemecolor))
                totalDeduction = 0
                for (deduction in payDeductionDetailList.value){
                        totalDeduction+= deduction.Amount

                        Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                BasicTextField(
                                    readOnly = true,
                                    value = deduction.Salary_Component_Name,
                                    onValueChange = { /* Handle value change if needed */ },
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        color = colorResource(id = R.color.paraColor)
                                    ),
                                    singleLine = true,)

                            }
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                Text(text = "${deduction.Amount}", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                            }

                        }
                        Divider(color = colorResource(id = R.color.divider))
                    }
                totalDeductionLop = totalLopAmount + totalDeduction
                Row(modifier = Modifier.background(color = colorResource(id = R.color.light_bright_red)),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "LOP", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium,modifier = Modifier.padding(10.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "$totalLopAmount", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End).padding(10.dp))
                    }
                }
                Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Total Deductions (B)", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium)
                    }
                    Column(modifier = Modifier.weight(1f)) {

                        Text(text = "$totalDeductionLop", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                    }
                }
            }
        }

        /* ============================================End Deduction============================================*/
        /* ============================================Start Other Deduction============================================*/
        val otherdd: List<Int> = payOtherDeductionDetailList.value.map { it.Total_Other_Earnings }
        if (otherdd.sum() !== 0) {
            Column(modifier = Modifier.padding(bottom = 10.dp)) {
                Text(text = "Other Deduction", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium)
            }

            Column(modifier = Modifier.padding(bottom = 10.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), modifier = Modifier.fillMaxWidth(1f),shape = RoundedCornerShape(3.dp)) {
                    Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Component", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Amount", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                        }

                    }
                    Divider(color = colorResource(id = R.color.lightthemecolor))
                    for (otherDeduction in payOtherDeductionDetailList.value){
                        Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                BasicTextField(
                                    readOnly = true,
                                    value = otherDeduction.Other_Component ?: "",
                                    onValueChange = { /* Handle value change if needed */ },
                                    textStyle = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight(500),
                                        fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                        color = colorResource(id = R.color.paraColor)
                                    ),
                                    singleLine = true,)

                            }
                            Column(modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, bottom = 5.dp)) {
                                Text(text = "${otherDeduction.Total_Other_Earnings}", color = colorResource(id = R.color.black), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                            }

                        }
                        Divider(color = colorResource(id = R.color.divider))
                    }

                }
            }
        }

        /* ============================================End Other Deduction===========================================*/

        /* ============================================NetPay Start============================================*/

        Column(modifier = Modifier.padding(bottom = 30.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.purpleBlue)),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.themeColor),
                        shape = RoundedCornerShape(5.dp)
                    ),
                shape = RoundedCornerShape(3.dp)
            ) {

                Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(top = 5.dp, bottom = 5.dp)) {
                        BasicTextField(
                            readOnly = true,
                            value = "Net Pay (A-B)",
                            onValueChange = { /* Handle value change if needed */ },
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(500),
                                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                color = colorResource(id = R.color.themeColor)
                            ),
                            singleLine = true,
                            )
                    }
                    var netPay = totalEarnings - totalDeductionLop
                    Log.d("netPay check","${netPay}")
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(top = 5.dp, bottom = 5.dp)) {
                        Text(text = "$netPay", color = colorResource(id = R.color.themeColor), style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.End))
                    }
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun downloadButton(navController: NavController, paySlipHeadDetailsViewModel: PaySlipHeadDetailsViewModel) {
    val payHeadDetailList by paySlipHeadDetailsViewModel.paySlipHeadList.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val empID = userViewModel.getSFCode()


    val showLoading = remember { mutableStateOf(false) }

    if (showLoading.value) {
        circularProgression()
    }

    // Extract the first URL, month, and year from the list (assuming there's at least one item)
    val urlPdf = payHeadDetailList.firstOrNull()?.Url_PDF?.let {
        try {
            URI.create(it).toString()
        } catch (e: IllegalArgumentException) {
            Log.e("DownloadButton", "Invalid URL: $it", e)
            null
        }
    } ?: ""
    val month = payHeadDetailList.firstOrNull()?.Month?.toString() ?: ""
    val year = payHeadDetailList.firstOrNull()?.Year ?: ""
    val fileName = "$month-$year payslip"

    // Log the URL for debugging
    Log.d("DownloadButton", "URL: $urlPdf")

    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        shape = RoundedCornerShape(0),
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            if (urlPdf.isNotEmpty())
                            {
                                downloadImageWithProgression(navController, urlPdf, "$fileName.pdf", "ViewPayslip", showLoading, context)
                                paySlipHeadDetailsViewModel.getPaySlipHeadList(navController, context, empID, month, year)
                            }
                            else
                            {
                                Log.e("DownloadButton", "URL is empty or invalid")
                            }
                        } catch (e: Exception) {
                            // Handle any exceptions, such as invalid URL
                            Log.e("DownloadButton", "Error downloading payslip", e)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                shape = RoundedCornerShape(5.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                Icon(painterResource(id = R.drawable.download1), contentDescription = "Download", tint = colorResource(id = R.color.white), modifier = Modifier.size(16.dp))
                Text(
                    text = "Download Payslip",
                    color = colorResource(id = R.color.white),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
        }
    }
}
