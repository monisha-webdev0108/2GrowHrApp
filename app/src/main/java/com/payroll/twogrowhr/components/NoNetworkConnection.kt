package com.payroll.twogrowhr.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.View.imageFileName
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.Model.View.selectedShift
import com.payroll.twogrowhr.Model.View.selectedShiftCutOff
import com.payroll.twogrowhr.Model.View.selectedShiftEndTime
import com.payroll.twogrowhr.Model.View.selectedShiftId
import com.payroll.twogrowhr.Model.View.selectedShiftStartTime
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.findActivity
import com.payroll.twogrowhr.redirectJob
import com.payroll.twogrowhr.ui.theme.poppins_font
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@Suppress("RedundantWith")
@Composable
fun noNetworkConnection(context : Context, navController: NavController, url : String)
{

//    var isClicked by remember { mutableStateOf(false) }


/*
    val buttonColor = if (isClicked) {
        colorResource(id = R.color.themeColor)
    } else {
        colorResource(id = R.color.lightshade)
    }

    val textColor = if (isClicked) {
        colorResource(id = R.color.lightshade)
    } else {
        colorResource(id = R.color.themeColor)
    }
*/


    Log.d("url","$url")


    BackPressHandler(onBackPressed = {
        Constant.clearVariables()
        context.findActivity()?.finish()
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.lightshade)),
        contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier.background(color = colorResource(id = R.color.lightshade))) {

            Image(
                painter = painterResource(id = R.drawable.no_internet),
                contentDescription = null, // Set to null if the image is decorative
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .size(with(LocalDensity.current) { 150.dp }),
//                    .alpha(0.5f), // Set the alpha value to adjust opacity (0.0f - fully transparent, 1.0f - fully opaque)
                contentScale = ContentScale.Fit
            )

            Text(
                text = "OOPS!",
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 19.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp)
            )

            Text(
                text = "NO INTERNET",
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 19.sp,
                    fontWeight = FontWeight(600),
                    color = colorResource(id = R.color.themeColor)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 0.dp)
            )

            Text(
                text = "Please check your internet connection",
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.black)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 11.dp)
            )

            Button(
                onClick = {
                    if (Constant.isNetworkAvailable(context)) {
                        redirectJob?.cancel()
                        CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate(url)
                            delay(TimeUnit.SECONDS.toMillis(2))  // Delay for 2 seconds
                        }
                    }
/*                    else
                    {
                        Constant.showToast(context, "Please check your network connection")
                    }*/
                },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightshade)),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp ),
                modifier = Modifier
                    .padding(top = 25.dp, start = 20.dp, end = 20.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp) // Set the height of the button
                    .fillMaxWidth()
//                    .width(100.dp) // Set the width of the button
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.themeColor),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = "TRY AGAIN",
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = R.color.themeColor)
                    ),
                )
            }
        }
    }
}