package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.AssetListData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.downloadImageWithProgression
import com.payroll.twogrowhr.components.exceptionScreen
import com.payroll.twogrowhr.components.linearProgression
import com.payroll.twogrowhr.components.noDataView
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.AssetsListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var assetList1 = mutableStateOf<List<AssetListData>>(emptyList())
@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun Asset(navController: NavController, assetViewModel: AssetsListViewModel) {

    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(navController = navController, title = "My Asset", "HomeScreen") },
        drawerContent = {  },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } })
    { Asset_Screen(navController = navController,assetViewModel = assetViewModel) }

}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SuspiciousIndentation")
@Composable
fun Asset_Screen(navController: NavController,assetViewModel: AssetsListViewModel) {
    val assetList = assetViewModel.AssetList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 75.dp, bottom = 55.dp, start = 10.dp, end = 10.dp)
    ) {

        var flag by remember { mutableIntStateOf(0) }

        //FOR GETTING RESULT

        val loadingStatus = assetViewModel.loadingStatus

        flag = assetViewModel.flag

        var loading by remember { mutableStateOf(false) }

        if(loading)
        {
            linearProgression()
        }

        val showLoading = remember { mutableStateOf(false) }// For DownLoading

        if (showLoading.value) {
            circularProgression()
        }

        assetViewModel.AssetList.collectAsState().also {
            assetList1 = it as MutableState<List<AssetListData>>
        }

        val context = LocalContext.current
        val empID = userViewModel.getSFCode()

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }

        fun refresh() = refreshScope.launch {
            refreshing = true
            assetViewModel.getAssetDetails(navController=navController, context = context, empId = empID,)
            delay(1500)
            refreshing = false
        }

        val state = rememberPullRefreshState(refreshing, ::refresh)

        @SuppressLint("NewApi")
        @Composable
        fun uiUpdateAsset() {
            Box(Modifier.pullRefresh(state)) {

                LazyColumn{
                    items(assetList.value){ asset ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(bottom = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                        ){
                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Asset Category",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                    BasicTextField(
                                        readOnly = true,
                                        value =asset.assetsCategoryName ,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        singleLine = true,
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Asset Name",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                    BasicTextField(
                                        readOnly = true,
                                        value =asset.assetsName ,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        singleLine = true,
                                    )
                                }

                            }
                            //Divider
                            Divider(modifier = Modifier.padding(top = 10.dp),color = colorResource(id = R.color.stroke))

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Received date",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                    BasicTextField(
                                        readOnly = true,
                                        value =asset.givenDate ,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        singleLine = true,
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Returned date",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                    BasicTextField(
                                        readOnly = true,
                                        value =asset.returnDate ,
                                        onValueChange = { /* Handle value change if needed */ },
                                        textStyle = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.poppins_medium)),
                                            color = colorResource(id = R.color.black)
                                        ),
                                        singleLine = true,
                                    )
                                }

                            }
                            //Divider
                            Divider(modifier = Modifier.padding( top = 10.dp),color = colorResource(id = R.color.stroke))

                            Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp,top =10.dp, bottom = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Status",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 11.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )
                                    if(asset.status =="Returned"){

                                        Button(onClick = {}, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_red)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) {
                                            Text(text = asset.status, color = colorResource(id = R.color.red), fontSize = 12.sp )
                                        }
                                    }
                                    else{
                                        Button(onClick = {}, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.third_light_green)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)) {
                                            Text(text = asset.status, color = colorResource(id = R.color.green), fontSize = 14.sp )
                                        }
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "",
                                        style = TextStyle(
                                            fontFamily = poppins_font,
                                            fontSize = 12.sp, fontWeight = FontWeight(500),
                                            color = colorResource(id = R.color.paraColor)
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 4.dp)
                                    )

                                    val urlLink =asset.assetsPDFURL
                                    val fileName=asset.assetsName + asset.invoiceId

                                    val coroutineScope = rememberCoroutineScope()
                                    Button(onClick = {
//                                        downloadImage(navController, urlLink, "$fileName.pdf", "my_assets", context)

                                        downloadImageWithProgression(navController, urlLink, "$fileName.pdf", "my_assets", showLoading, context)

                                        coroutineScope.launch {
                                            assetViewModel.getAssetDetails(empId = empID, context=context, navController = navController,)
                                        }
                                    }, modifier = Modifier.padding(bottom = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)), shape = RoundedCornerShape(5.dp),contentPadding = PaddingValues(start = 40.dp, end = 40.dp, top = 5.dp, bottom = 5.dp)) {
                                        Icon(painterResource(id =R.drawable.download1 )  , contentDescription ="loan", tint = colorResource(id = R.color.white) , modifier = Modifier.size(14.dp))
                                        Text(text = "Download", color = colorResource(id = R.color.white), fontSize = 12.sp , modifier = Modifier.padding(start = 3.dp))
                                    }
                                }

                            }
                        }
                    }
                }

                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))

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

            if(assetList.value.isEmpty())
            {

                when (flag)
                {
                    0 -> {

                        loading = true
                    }
                    1 -> {

                        assetViewModel.getAssetDetails(navController, context, userViewModel.getSFCode())
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

                        uiUpdateAsset()
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






