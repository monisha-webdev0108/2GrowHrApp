package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.TextClock
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarIcon
import com.payroll.twogrowhr.components.checkInViewModel
import com.payroll.twogrowhr.components.circularProgression
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.components.userViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.components.startRedirectTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckInLocation(navController: NavController, context: Context)
{
    rememberDrawerState(initialValue = DrawerValue.Closed)


    val checkInOutFlag = SharedPreferenceManager.getCheckInOut(context)
    buttonText = if (checkInOutFlag == "ckout") {
        "Check Out"
    } else {
        "Check In"
    }

    LaunchedEffect(Unit)
    {
        checkInViewModel.latLong(context, navController)
        if(lats==0.0) {
            withContext(Dispatchers.IO) {
                TimeUnit.SECONDS.sleep(1L)
            }
            Constant.showToast(context, "Fetching location, Please wait")
        }
    }
    AppScaffold1(
        topBarContent = {
            TopBarIcon(
                title = buttonText,
                R.drawable.locationicon
            )
        },
        bottomBarContent = {})
    {
        CheckInLocationScreen(
            navController = navController,
            context = context
        )
    }


}


@SuppressLint("ResourceAsColor")
@Composable
fun CheckInLocationScreen(navController: NavController, context: Context) {

    val coroutineScope = rememberCoroutineScope()
    var overlayVisible by remember { mutableStateOf(false) }


    val empID = userViewModel.getSFCode()
    Log.d("S_empID...", "-- $empID")

    Log.d("CheckInLocationScreen", "-- Inside CheckInLocationScreen")

    loading.value = false

    if(loading.value)
    {
        circularProgression()
        startRedirectTimer(navController, LocalContext.current,"HomeScreen")
    }

    TransparentOverlay(overlayVisible) {


        if (lats == 0.0)
        {
            TimeUnit.SECONDS.sleep(1L)
            val loc = LatLng(lats, longs)
            val marker = MarkerState(position = loc)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(loc, 16f)
            }


            // Move time-consuming operations to background threads
            LaunchedEffect(Unit) {
                // Background thread for geocoding
                withContext(Dispatchers.IO) {
                    try {

                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION") val addresses = geocoder.getFromLocation(lats, longs, 1)
                        if (addresses?.isNotEmpty() == true) {
                            val address = addresses[0]
                            addressText = "${address.getAddressLine(0)}, ${address.locality}"

                        }

                    }
                    catch (e: IOException)
                    {
                        Log.d("geolocation", e.message.toString())

                    }
                }
            }




            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 50.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.backgroundColor))
                        .padding(top = 1.dp, bottom = 20.dp, start = 5.dp, end = 20.dp)
                        .weight(6.5f, true),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true)

                ) {
                    Marker(
                        state = marker
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(2f, true), verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .weight(0.7f, true).padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painterResource(id = R.drawable.clock_1),
                            contentDescription = "",
                            tint = colorResource(id = R.color.purple_200),
                            modifier = Modifier
                                .size(size = 20.dp)
                                .padding(5.dp)
                        )
                        AndroidView(
                            // on below line we are initializing our text clock.
                            factory = { context ->
                                TextClock(context).apply {
                                    format12Hour?.let {
                                        this.format12Hour = "dd/mm/yyyy hh:mm:ss a"
                                    }
                                    timeZone?.let { this.timeZone = it }
                                    textSize.let { this.textSize = 13f }

                                }
                            },
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxWidth(1f)
                                .weight(1.2f, true),
                        )
                        val color = if (checkinflag == "ckout") colorResource(id = R.color.red) else colorResource( id = R.color.green)
                        Button(
                            onClick = {


                                if(isMockLocation.value)
                                {
                                    navController.navigate("fakeGPS")
                                }
                                else
                                {
                                    overlayVisible = true
                                    coroutineScope.launch {
                                        delay(2000)
                                        overlayVisible = false
                                    }

                                    coroutineScope.launch {
                                        val isLocationEnabled = withContext(Dispatchers.IO) {
                                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                        }


                                        if(!isLocationEnabled)
                                        {
                                            Constant.showToast(context,"Please turn on the location")
                                            Log.d("Location", "Location services not enabled. Launching settings activity.")

                                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                            try {
                                                context.startActivity(intent)
                                                Log.d("Location", "Settings activity launched successfully.")
                                            } catch (e: Exception) {
                                                Log.e("Location", "Error launching settings activity: ${e.message}")
                                            }
//                                        checkInViewModel.latLong(context)
                                        }
                                        else
                                        {
                                            checkInViewModel.latLong(context,navController)


                                            coroutineScope.launch {

                                                if(addressText == "")
                                                {
                                                    checkInViewModel.latLong(context,navController)
                                                }
                                                else
                                                {
                                                    if (checkinflag == "ckin")
                                                    {
                                                        Log.d("Current Location...", "--submitCheckinTest1 ---> 2")
                                                        checkInViewModel.submitCheckin1(context, navController)
                                                    }
                                                    else
                                                    {
                                                        Log.d("Current Location...", "--submitCheckOutTest1 ---> 2")
                                                        checkInViewModel.submitCheckout1(context, navController)
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }







                            },
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxWidth(1f)
                                .fillMaxHeight(1f)
                                .requiredHeight(40.dp) // Set the height of the button
                                .requiredWidth(110.dp) // Set the width of the button
                                .weight(0.8f, true),
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            shape = RoundedCornerShape(5.dp)

                        ) {

                            Text(
                                text = buttonText,
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.labelSmall
                            )

                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .weight(1.3f, true)
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.location),
                            contentDescription = "",
                            tint = colorResource(id = R.color.purple_200),
                            modifier = Modifier
                                .size(size = 20.dp)
                                .padding(10.dp)
                        )
                        Text(
                            text = addressText,
                            color = colorResource(R.color.paraColor),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {


                        Column(modifier = Modifier.weight(1f)) {

                            OutlinedButton(onClick = {
                                lats = 0.0
                                longs = 0.0
                                addressText = ""
                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }
                            },
                                modifier= Modifier
                                    .size(50.dp)
                                    .align(Alignment.Start),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, Color(R.color.divider)),
                                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = colorResource(id = R.color.divider)
                                )

                            )
                            {
                                androidx.compose.material.Icon(
                                    painter = painterResource(id = R.drawable.previous),
                                    contentDescription = "Fetch Location",
                                    tint = Color(ContextCompat.getColor(context, R.color.themeColor)),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }









                        Column(modifier = Modifier.weight(1f)) {

                            OutlinedButton(onClick = {
                                checkInViewModel.latLong(context, navController)
                                Constant.showToast(context = context, "Fetching Location")
                                navController.navigate("CheckInLocation") { popUpTo("CheckInLocation") }
                            },
                                modifier= Modifier
                                    .size(50.dp)
                                    .align(Alignment.End),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, Color(R.color.divider)),
                                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = colorResource(id = R.color.divider)
                                )

                            )
                            {
                                androidx.compose.material.Icon(
                                    painter = painterResource(id = R.drawable.sync),
                                    contentDescription = "Fetch Location",
                                    tint = Color(ContextCompat.getColor(context, R.color.themeColor)),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }

                }
            }

        }
        else {

            val loc = LatLng(lats, longs)
            val marker = MarkerState(position = loc)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(loc, 16f)
            }



            // Move time-consuming operations to background threads
            LaunchedEffect(Unit) {
                // Background thread for geocoding
                withContext(Dispatchers.IO) {
                    try
                    {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION") val addresses = geocoder.getFromLocation(lats, longs, 1)
                        if (addresses?.isNotEmpty() == true) {
                            val address = addresses[0]
                            addressText = "${address.getAddressLine(0)}, ${address.locality}"
                        }


                    }
                    catch (e: IOException)
                    {
                        Log.d("geolocation", e.message.toString())
                    }
                }
            }



            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.backgroundColor))
                    .padding(top = 50.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.backgroundColor))
                        .padding(top = 1.dp, bottom = 20.dp, start = 5.dp, end = 10.dp)
                        .weight(6.5f, true),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true)
                ) {
                    Marker(
                        state = marker
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .weight(2f, true), verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .weight(0.5f, true).padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painterResource(id = R.drawable.clock_1),
                            contentDescription = "",
                            tint = colorResource(id = R.color.purple_200),
                            modifier = Modifier
                                .size(size = 20.dp)
                                .padding(top = 5.dp)
                        )
                        AndroidView(
                            factory = { context ->
                                TextClock(context).apply {
                                    format12Hour?.let {
                                        this.format12Hour = "dd/MM/yyyy hh:mm:ss a"
                                    }
                                    timeZone?.let { this.timeZone = it }
                                    textSize.let { this.textSize = 13f }

                                }
                            },
                            modifier = Modifier
                                .padding(start = 3.dp)
                                .padding(top = 5.dp)
                                .fillMaxWidth(1f)
                                .weight(1.2f, true),
                        )
                        val color =
                            if (checkinflag == "ckout") colorResource(id = R.color.red) else colorResource(
                                id = R.color.green
                            )
                        Button(
                            onClick = {


                                if(isMockLocation.value)
                                {
                                    navController.navigate("fakeGPS")
                                }
                                else if(!isTimeZoneAutomaticEnabled.value)
                                {
                                    navController.navigate("fakeTime")
                                }
                                else
                                {
                                    overlayVisible = true

                                    coroutineScope.launch {
                                        delay(2000)
                                        overlayVisible = false
//                                }
//
//                                coroutineScope.launch {

                                        val isLocationEnabled = withContext(Dispatchers.IO) {
                                            val locationManager =
                                                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                        }

                                        if(!isLocationEnabled)
                                        {
                                            Constant.showToast(context,"Please turn on the location")
                                            Log.d("Location", "Location services not enabled. Launching settings activity.")

                                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                            try {
                                                context.startActivity(intent)
                                                Log.d("Location", "Settings activity launched successfully.")
                                            } catch (e: Exception) {
                                                Log.e("Location", "Error launching settings activity: ${e.message}")
                                            }
//                                        checkInViewModel.latLong(context)
                                        }
                                        else
                                        {

                                            checkInViewModel.latLong(context, navController)
                                            coroutineScope.launch {
                                                if(addressText == "")
                                                {
                                                    checkInViewModel.latLong(context, navController)
                                                }
                                                else
                                                {
                                                    if (checkinflag == "ckin")
                                                    {
                                                        Log.d("Current Location...", "--submitCheckinTest1 ---> 2")
                                                        try {
                                                            checkInViewModel.submitCheckin1(context, navController)
                                                        } catch (e: Exception) {
                                                            Log.e("Location", "Error in submitCheckin1: ${e.message}")
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Log.d("Current Location...", "--submitCheckOutTest1 ---> 2")

                                                        try {

                                                            checkInViewModel.submitCheckout1(context, navController)

                                                        } catch (e: Exception) {
                                                            Log.e("Location", "Error in submitCheckin1: ${e.message}")
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }

                                }



                            },
                            modifier = Modifier
                                .padding(0.dp)
                                .fillMaxWidth(1f)
                                .fillMaxHeight(1f)
                                .requiredHeight(40.dp) // Set the height of the button
                                .requiredWidth(110.dp) // Set the width of the button
                                .weight(0.8f, true),
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            shape = RoundedCornerShape(5.dp)
                        ) {

                            Text(
                                text = buttonText,
                                color = colorResource(id = R.color.white),
                                style = MaterialTheme.typography.labelSmall
                            )

                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .weight(1.5f, true)
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.location),
                            contentDescription = "",
                            tint = colorResource(id = R.color.purple_200),
                            modifier = Modifier.size(size = 20.dp)
                        )
                        Text(
                            text = addressText,
                            color = colorResource(R.color.paraColor),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        Column(modifier = Modifier.weight(1f)) {

                            OutlinedButton(onClick = {
                                lats = 0.0
                                longs = 0.0
                                addressText = ""
                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }
                            },
                                modifier= Modifier
                                    .size(50.dp)
                                    .align(Alignment.Start),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, Color(R.color.divider)),
                                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = colorResource(id = R.color.divider)
                                )

                            )
                            {
                                androidx.compose.material.Icon(
                                    painter = painterResource(id = R.drawable.previous),
                                    contentDescription = "Fetch Location",
                                    tint = Color(ContextCompat.getColor(context, R.color.themeColor)),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }



                        Column(modifier = Modifier.weight(1f)) {

                            OutlinedButton(onClick = {
                                checkInViewModel.latLong(context, navController)
                                Constant.showToast(context = context, "Fetching Location")
                                navController.navigate("CheckInLocation") { popUpTo("CheckInLocation") }
                            },
                                modifier= Modifier
                                    .size(50.dp)
                                    .align(Alignment.End),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, Color(R.color.divider)),
                                contentPadding = PaddingValues(0.dp),  //avoid the little icon
                                colors = androidx.compose.material.ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = colorResource(id = R.color.divider)
                                )

                            )
                            {
                                androidx.compose.material.Icon(
                                    painter = painterResource(id = R.drawable.sync),
                                    contentDescription = "Fetch Location",
                                    tint = Color(ContextCompat.getColor(context, R.color.themeColor)),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }


                }
            }
        }


    }


}
