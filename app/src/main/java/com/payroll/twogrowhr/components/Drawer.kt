package com.payroll.twogrowhr.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import com.payroll.twogrowhr.R
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.viewModel.MainViewModel



@Composable
fun DrawerContent(navController: NavController, viewModel: MainViewModel) {

    val context = LocalContext.current


    val empName: String = userViewModel.getSFName()
    val imgUrl: String = userViewModel.getProfile()
    Log.d("Drawer Emp_Name/ImgUrl", "$empName/$imgUrl")

    var showDialog by remember { mutableStateOf(false) }
    ModalDrawerSheet(drawerContainerColor = Color.White)
    {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(1f)
                .fillMaxWidth(0.8f)
        )
        {
            if (showDialog) {
                ShowDialog(
                    context = context,
                    viewModel = viewModel,
                    navController = navController
                ) { showDialog = false }
            }
            LazyColumn {
                item {
                    Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
                    {
                        Column {

                            val link = Constant.IMAGE_URL + "Images/EmpUpload/"
                            val profile = link + imgUrl
                            val processedImgUrl = profile.ifEmpty { link }

                            Log.d("Drawer Profile/processedImgUrl", "$profile/$$processedImgUrl")

                            if (processedImgUrl != link) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(processedImgUrl).crossfade(true).transformations(
                                            CircleCropTransformation()
                                        ).allowHardware(true).build(),
                                    placeholder = painterResource(R.drawable.capa),
                                    contentDescription = "icon",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(35.dp)
                                )
                            } else {
                                // If imgurl is empty or null, display a default image
                                Image(
                                    painter = painterResource(id = R.drawable.nonimgicon),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp)
                                )
                            }


                        }
                        Column(modifier = Modifier.padding(start = 10.dp)) {

                            // Custom TextStyle
                            val customTitleLargeTextStyle = TextStyle(
                                fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                fontSize = 18.sp, // Set the desired font size
                                fontWeight = FontWeight(500) // Set the desired font style
                            )
                            Text(
                                text = empName,
                                style = customTitleLargeTextStyle,
                                color = Color(0xFF405189),
                                modifier = Modifier.clickable { navController.navigate("Profile") }
                            )
                        }
                    }
                    Divider(color = Color(0xFFDFDFDF), thickness = 1.dp)
                }

                val itemsList = listOf(
                    DrawerMenuItem(
                        icon = R.drawable.orgdoc,
                        text = "Organization documents",
                        action = { navController.navigate("OrgDocList") }),
/*                    DrawerMenuItem(
                        icon = R.drawable.mydocuments,
                        text = "My documents",
                        action = { navController.navigate("My_document") }),*/
                    DrawerMenuItem(
                        icon = R.drawable.assets,
                        text = "My Asset",
                        action = { navController.navigate("my_assets") }),
                    DrawerMenuItem(
                        icon = R.drawable.approval,
                        text = "Approvals",
                        action = { navController.navigate("approvallist") }),
                    DrawerMenuItem(
                        icon = R.drawable.lock,
                        text = "Change password",
                        action = { navController.navigate("Change_password") }),
                    DrawerMenuItem(
                        icon = R.drawable.logout,
                        text = "Logout",
                        action = { showDialog = true })
                )

                items(count = itemsList.size) { index ->
                    DrawerItem(itemsList[index])
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(
    menuItem: DrawerMenuItem
) {

    Column(modifier = Modifier.fillMaxSize().clickable ( onClick = menuItem.action ))
    {
        Row(
            modifier = Modifier
                .padding(top = 17.dp, bottom = 17.dp)
//                .clickable(onClick = menuItem.action)
        ) {
            Column {
                Icon(
                    painterResource(id = menuItem.icon),
                    tint = colorResource(id = R.color.black),
                    contentDescription = "Menu",
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 10.dp))
            {
                Text(text = menuItem.text, style = MaterialTheme.typography.titleMedium)
            }
            Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Menu",
                    modifier = Modifier.size(23.dp)
                )
            }
        }

        Divider(color = Color(0xFFDFDFDF), thickness = 1.dp)
    }
}

@Composable
private fun ShowDialog(
    context: Context,
    viewModel: MainViewModel,
    navController: NavController,
    action: () -> Unit
) {
    AlertDialog(
        onDismissRequest = action,
        backgroundColor = colorResource(id = R.color.backgroundColor),
        shape = RoundedCornerShape(10),
        title = {
            Text(
                "Logout",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                "Are you sure you want to logout?",
                style = MaterialTheme.typography.titleLarge
            )
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                modifier = Modifier.padding(bottom = 10.dp),
                onClick = {
                    // Perform logout actions here
                    SharedPreferenceManager.setLoggedIn(context, false)
                    SharedPreferenceManager.setCheckInOut(context, "")
                    Constant.clearSQLiteData(context)
                    Constant.clearVariables()
                    viewModel.clearUiState1()
                    viewModel.clearCheckInState()
                    navController.navigate("Login")
                    action()
                }
            ) {
                Text(
                    "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.themeColor)),
                onClick = action    // Dismiss the dialog when cancel is clicked
            ) {
                Text(
                    "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    )
}

data class DrawerMenuItem(val icon: Int, val text: String, val action: () -> Unit)
