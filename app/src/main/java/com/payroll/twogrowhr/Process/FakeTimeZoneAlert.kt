package com.payroll.twogrowhr.Process

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Model.View.imageFileName
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.Model.View.selectedShift
import com.payroll.twogrowhr.Model.View.selectedShiftCutOff
import com.payroll.twogrowhr.Model.View.selectedShiftEndTime
import com.payroll.twogrowhr.Model.View.selectedShiftId
import com.payroll.twogrowhr.Model.View.selectedShiftStartTime
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.BackPressHandler
import com.payroll.twogrowhr.components.isDeveloperEnabled
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.findActivity
import com.payroll.twogrowhr.ui.theme.poppins_font

@RequiresApi(Build.VERSION_CODES.S)
@Suppress("DEPRECATION")
@Composable
fun FakeTimeAlert(navController : NavController) {
    Log.d("app_nav", "Inside FakeTimeAlert")

    val context = LocalContext.current

//    isDeveloperEnabled.value = Settings.Secure.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1


    BackPressHandler(onBackPressed = {

        loading.value = false
        selectedShift = ""
        selectedShiftId = ""
        selectedShiftStartTime = ""
        selectedShiftEndTime = ""
        selectedShiftCutOff = ""
        imageFileName =""
        lats = 0.0
        longs = 0.0
        context.findActivity()?.finish()
    })

    val text1 = if(!isTimeZoneAutomaticEnabled.value) "Auto Time/Time Zone is disabled..!" else "Auto Time/Time Zone is enabled..!"
    val text2 = if(!isTimeZoneAutomaticEnabled.value) "Please enable Auto Time Zone to proceed..." else "You can proceed now..."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.light_black))
            .alpha(0.9f),
        contentAlignment = Alignment.Center
    ) {

        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(350.dp)
                .padding(16.dp)
                .background(color = Color.Transparent),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.white)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Image(
                    painter = painterResource(id = R.drawable.alert_image),
                    contentDescription = "imageDescription",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(127.dp)
                        .width(276.dp)
                )

//                    HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(5.dp))


                Text(
                    text = "Auto Time Zone is disabled..!",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                    ),
                    color = colorResource(id = R.color.black),
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                )

                Text(
                    text = "Please enable Auto Time Zone to proceed...",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 11.sp,
                        fontWeight = FontWeight(500),
                    ),
                    modifier = Modifier.padding(5.dp),
                )

                HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 5.dp, end = 5.dp))


                TextButton(onClick = {

                    val intent = Intent(Settings.ACTION_DATE_SETTINGS)
                    context.startActivity(intent)

                }, modifier = Modifier.padding(5.dp),
                ) {
                    Text(
                        text = "Open Settings",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 11.sp,
                            fontWeight = FontWeight(500),
                        ),
                        color = colorResource(id = R.color.blue)
                    )
                }

/*

                if(!isTimeZoneAutomaticEnabled.value)
                {

                    HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 5.dp, end = 5.dp))


                    TextButton(onClick = {

                        val intent = Intent(Settings.ACTION_DATE_SETTINGS)
                        context.startActivity(intent)

                    }, modifier = Modifier.padding(5.dp),
                    ) {
                        Text(
                            text = "Open Settings",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 11.sp,
                                fontWeight = FontWeight(500),
                            ),
                        )
                    }
                }
                else
                {
                    HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 5.dp, end = 5.dp))

                    TextButton(onClick = {

//                        context.findActivity()?.finish()

                        loading.value = false
                        selectedShift = ""
                        selectedShiftId = ""
                        selectedShiftStartTime = ""
                        selectedShiftEndTime = ""
                        selectedShiftCutOff = ""
                        imageFileName =""

                        isTimeZoneAutomaticEnabled.value = Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME_ZONE, 0) == 1
                        navController.navigate("HomeScreen")

                    }, modifier = Modifier.padding(5.dp),
                    ) {
                        Text(
                            text = "Close",
                            style = TextStyle(
                                fontFamily = poppins_font,
                                fontSize = 11.sp,
                                fontWeight = FontWeight(500),
                            ),
                        )
                    }
                }

*/

                HorizontalDivider(color = colorResource(id = R.color.divider), modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 5.dp, end = 5.dp))

                TextButton(onClick = {

                    loading.value = false
                    selectedShift = ""
                    selectedShiftId = ""
                    selectedShiftStartTime = ""
                    selectedShiftEndTime = ""
                    selectedShiftCutOff = ""
                    imageFileName =""
                    lats = 0.0
                    longs = 0.0

                    context.findActivity()?.finish()

                }, modifier = Modifier.padding(top = 3.dp, bottom = 5.dp, start = 5.dp, end = 5.dp),
                ) {
                    Text(
                        text = "Close",
                        style = TextStyle(
                            fontFamily = poppins_font,
                            fontSize = 11.sp,
                            fontWeight = FontWeight(500),
                        ),
                        color = colorResource(id = R.color.red)
                    )
                }

            }
        }
    }
}
