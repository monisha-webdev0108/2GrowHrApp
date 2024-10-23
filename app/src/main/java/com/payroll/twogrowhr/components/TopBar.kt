package com.payroll.twogrowhr.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.View.generateLoanData
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.ui.theme.poppins_font
import com.payroll.twogrowhr.viewModel.parseLoanDetailsFromString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, title :String,drawerState: DrawerState) {

    val coroutineScope = rememberCoroutineScope()

    val imgUrl = userViewModel.getProfile()
    Log.d("ImgUrl", imgUrl)

    val context = LocalContext.current


    val appUpdateManager = AppUpdateManagerFactory.create(context)

    val appUpdateInfoTask = appUpdateManager.appUpdateInfo
    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->


        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
        {
            isUpdateAvailable.value = true

        }
/*
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) {
            // An update is available. Show a message to the user.
            // You can prompt the user to start the update flow.
            isUpdateAvailable.value = true
        }
*/

        Log.d("App_Navigation", "appUpdateInfoTask : $appUpdateInfo")
        Log.d("App_Navigation", "appUpdateInfoTask / Boolean: ${appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE}")

    }

    Log.d("App_Navigation", "appUpdateInfoTask : $appUpdateInfoTask")

    Log.d("App_Navigation", "isUpdateAvailable : ${isUpdateAvailable.value}")





    TopAppBar(
//      modifier = Modifier.background(colorResource(id = R.color.red)),
        colors = TopAppBarDefaults.largeTopAppBarColors(Color.White),
        title = {
            BasicTextField(
                readOnly = true,
                value = title,
                onValueChange = { /* Handle value change if needed */ },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    color = colorResource(id = R.color.themeColor),
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp) // Adjust padding as needed
            )
//                    Text(text = title, style = MaterialTheme.typography.titleMedium, color = Color(0xFF405189))
                },
        navigationIcon = {
            IconButton(onClick = {
                if (drawerState.isClosed) {
                    coroutineScope.launch { drawerState.open() }
                }
            }) {
                Icon(painterResource(id = R.drawable.menubar)  , contentDescription = "menu", tint = Color(0xFF405189))
            }
        },
        actions ={
            Row {

                val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                val profile = link + imgUrl
                val processedImgUrl = profile.ifEmpty { link }

                Log.d("Drawer Profile/processedImgUrl", "$profile/$processedImgUrl")

                Column(modifier = Modifier.padding(end = 10.dp))
                {

                    if (processedImgUrl != link) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(processedImgUrl).crossfade(true)
                                .transformations(CircleCropTransformation()).allowHardware(true)
                                .build(),
                            placeholder = painterResource(R.drawable.capa),
                            contentDescription = "icon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clickable { navController.navigate("Profile") }
                                .size(35.dp)
                        )
                    } else {
                        // If profile is empty or null, display a default image
                        Image(
                            painter = painterResource(id = R.drawable.nonimgicon),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }

                }
/*

                Column(modifier = Modifier.padding(start = 3.dp, end = 10.dp))
                {
                    IconButton(onClick = { Constant.showToast(context, "Update Available") })
                    {
                        Icon(painterResource(id = R.drawable.notify)  , contentDescription = "notification", tint = Color.Unspecified, modifier = Modifier.size(25.dp)) //
                    }
                }


*/


            }
        },
    )
}


@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBackNavigation(navController: NavController, title: String, url: String) {

    TopAppBar(
        title = {
            Icon(painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "back",
                tint = colorResource(id = R.color.themeColor),
                modifier = Modifier.clickable { navController.navigate(url) }

            )
            BasicTextField(
                readOnly = true,
                value = title,
                onValueChange = { },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    color = colorResource(id = R.color.themeColor),
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp) // Adjust padding as needed

            )

        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
    )
}





@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarIcon(title: String, url: Int) {
    rememberCoroutineScope()
    Log.d("Icon Name", "Top Bar Icon name: ${R.drawable.location}")
    Log.d("Icon Name", "Top Bar Icon url : $url")

    TopAppBar(

        title = {
            Icon(painterResource(id = url),
                contentDescription = "Icon",
                tint = colorResource(id = R.color.themeColor),
                modifier = Modifier.size(size = 20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF405189),
                modifier = Modifier
                    .padding(start = 30.dp)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White),
    )
}


//For Loan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithEdit(navController: NavController, title :String, backUrl: String, editUrl: String, data : String, dataClassType : String) {

    Log.d("TopBarWithEdit", "TopBarWithEdit : $data")

    val parsedData = remember { mutableStateOf(parseLoanDetailsFromString(data)) }

    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(

        colors = TopAppBarDefaults.largeTopAppBarColors(Color.White),
        title = {

            Icon(painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "back",
                tint = colorResource(id = R.color.themeColor),
                modifier = Modifier.clickable { navController.navigate(backUrl) }
            )

            BasicTextField(
                readOnly = true,
                value = title,
                onValueChange = { },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    color = colorResource(id = R.color.themeColor),
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
        },
        actions = {

            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }

            DropdownMenu(expanded = isMenuExpanded,
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .height(IntrinsicSize.Max),
                onDismissRequest = { isMenuExpanded = false })
            {
                DropdownMenuItem(
                    text = {
                        Text(
                        text = "Edit" ,
                        style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.themeColor)),
                    )
                    },
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white))
                        .height(IntrinsicSize.Max),
                    onClick = { isMenuExpanded = false
                        navController.navigate(editUrl)
                    }
                )
            }
        }
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TopBarWithEditPreview() {

    val navController = rememberNavController()

    val parsedData = generateLoanData()

    val title = "Title"

    var isMenuExpanded by remember { mutableStateOf(false) }

    TopAppBar(

        colors = TopAppBarDefaults.largeTopAppBarColors(Color.White),
        title = {

            Icon(painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "back",
                tint = colorResource(id = R.color.themeColor),
                modifier = Modifier.clickable {  }
            )

            BasicTextField(
                readOnly = true,
                value = title,
                onValueChange = { },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    color = colorResource(id = R.color.themeColor),
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )
        },
        actions = {

            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }

            DropdownMenu(expanded = isMenuExpanded,
                modifier = Modifier
                    .background(color = colorResource(id = R.color.white))
                    .height(IntrinsicSize.Max),
                onDismissRequest = { isMenuExpanded = false })
            {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Edit" ,
                            style = TextStyle(fontFamily = poppins_font, fontSize = 14.sp, fontWeight = FontWeight(500), color = colorResource(id = R.color.themeColor)),
                        )
                    },
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white))
                        .height(IntrinsicSize.Max),
                    onClick = { isMenuExpanded = false
                    }
                )
            }
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBarPreview() {

    val title = "SAN MEDIA"

    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(Color.White),
        title = {
            BasicTextField(
                readOnly = true,
                value = title,
                onValueChange = { /* Handle value change if needed */ },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    fontFamily = FontFamily(Font(R.font.poppins_semibold)),
                    color = colorResource(id = R.color.themeColor),
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp) // Adjust padding as needed
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(painterResource(id = R.drawable.menubar)  , contentDescription = "menu", tint = Color(0xFF405189))
            }
        },
        actions ={
            Row {

                val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                val processedImgUrl = Constant.IMAGE_URL + "Images/EmpUpload/"

                Column(modifier = Modifier.padding(end = 10.dp))
                {

                    if (processedImgUrl != link)
                    {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(processedImgUrl).crossfade(true)
                                .transformations(CircleCropTransformation()).allowHardware(true)
                                .build(),
                            placeholder = painterResource(R.drawable.capa),
                            contentDescription = "icon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.clickable { }.size(35.dp)
                        )
                    }
                    else {
                        Image(
                            painter = painterResource(id = R.drawable.nonimgicon),
                            contentDescription = null,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }

            }
        },
    )
}



