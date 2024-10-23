package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.components.checkInViewModel
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.getLoginDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

var selectedShift = ""
var selectedShiftId = ""
var selectedShiftStartTime = ""
var selectedShiftEndTime = ""
var selectedShiftCutOff = ""
var imageFileName = ""
const val img = ""
lateinit var filess: File
private lateinit var cameraExecutor1: ExecutorService
private var shouldShowCamera1: MutableState<Boolean> = mutableStateOf(false)

class ComposeFileProvider : FileProvider(
    R.xml.provider_paths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val tsLong = System.currentTimeMillis() / 1000
            imageFileName = "$tsLong.jpg"
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString() + "/" + imageFileName

            filess = File(directory)
            val fos: FileOutputStream?
            try {
                fos = FileOutputStream(filess)
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return getUriForFile(context, context.packageName + ".fileprovider", filess)

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShiftSelection(navController: NavController, context: Context) {
    rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = {
            TopBarBackNavigation(
                navController = navController,
                title = "Shift Selection",
                "HomeScreen"
            )
        },
        bottomBarContent = {},
        onBack = { navController.navigateUp() }
    )
    {
        shiftSelect(
            navController = navController,
            context = context
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun shiftSelect(navController: NavController, context: Context) {

    val coroutineScope = CoroutineScope(Dispatchers.Main) // or Dispatchers.IO, depending on your needs

    val openDialog = remember { mutableStateOf(false) }


    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val jsonObject = getLoginDetails()
    val empID = jsonObject?.getString("Sf_code") ?: ""
    Log.d("S_empID...", "-- $empID")

//    val geoDetailsList = checkInViewModel.geoList.collectAsState()
//    Log.d("Shift Selection", "Geo Data List : $geoDetailsList")

    val shiftListDetails = checkInViewModel.shiftList.collectAsState()
    Log.d("Shift Selection", "Shift Data List : $shiftListDetails")


    fun checkInShift()
    {
        coroutineScope.launch {


                if (userViewModel.isImageCaptureNeed())
                {
                    //--- new
                    Log.d("shift... ", "selfie is enabled no geofencing")
                    shouldShowCamera1.value = true

                }
                else if (userViewModel.hasLocationCheckNeed())
                {


                    coroutineScope.launch {

                        val isLocationEnabled = withContext(Dispatchers.IO) {
                            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        }

//                                                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//                                                    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                        Log.d("shift... ", "inside location is enabled,before enabled or disabled no geofencing")

                        if (!isLocationEnabled)
                        {
                            Constant.showToast( context, "Please turn on the location")

                            Log.d( "Location", "Location services not enabled. Launching settings activity."  )

                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                            try
                            {
                                context.startActivity(intent)
                                Log.d("Location","Settings activity launched successfully." )
                            }
                            catch (e: Exception)
                            {
                                Log.e("Location","Error launching settings activity: ${e.message}")
                            }
                        }
                        else
                        {
                            checkInViewModel.latLong(context, navController)//for getting current lat long..
                            Constant.showToast( context = context,"Fetching Location" )
                            Log.d("shift... ","location is enabled no geofencing" )
                            navController.navigate("Currentlocation")
                            {
                                popUpTo(  "Currentlocation" )
                            }
                        }

                    }


                }
                else
                {
                    loading.value = true

                    val isLocationEnabled = withContext(Dispatchers.IO) {
                        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    }


                    // Check if location services are enabled
                    //                                                        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    //                                                        val isLocationEnabled  = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


                    if (userViewModel.hasGeoFencing())
                    {


                        if (!isLocationEnabled)
                        {
                            Constant.showToast(  context, "Please turn on the location" )
                            Log.d(  "Location","Location services not enabled. Launching settings activity.")

                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                            try
                            {
                                context.startActivity(intent)
                                Log.d("Location", "Settings activity launched successfully."  )
                            }
                            catch (e: Exception)
                            {
                                Log.e("Location", "Error launching settings activity: ${e.message}")
                            }
                            checkInViewModel.latLong(context, navController)
                            loading.value = false
                        }
                        else
                        {

                            checkInViewModel.latLong(context, navController) //for getting current lat long..

                            checkInViewModel.getGeoDetails1(context, navController, empID) { geoDataList ->


                                //                                                            Log.d("ShiftSelection","from geoDetailsList-----${geoDetailsList}")
                                Log.d("ShiftSelection","from geoDataList-----${geoDataList}")

                                if (geoDataList.isNullOrEmpty())
                                {
                                    Constant.showToast( context,  "Fencing enabled but no bounds found..." )
                                    navController.navigate("HomeScreen")
                                    {
                                        popUpTo("HomeScreen")
                                    }
                                }
                                else
                                {
                                    val fenceDistance = checkInViewModel.checkDistance(
                                        geoDataList
                                    )
                                    if (fenceDistance > 0)
                                    {
                                        checkInViewModel.submitCheckin(context,navController)
                                        //                                                                    checkInViewModel.submitCheckin(context, lifecycleOwner,viewModel, navController )
                                    }
                                    else if (lats == 0.0)
                                    {
                                        Constant.showToast( context,"Location not Fetched,Please Try Again after 10 seconds" )
                                        navController.navigate("HomeScreen")
                                        {
                                            popUpTo("HomeScreen")
                                        }
                                    } else {
                                        Constant.showToast( context, "You are out of bounds from the headquarters or Working place" )
                                        navController.navigate("HomeScreen")
                                        {
                                            popUpTo("HomeScreen" )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        checkInViewModel.submitCheckin(context, navController)
                        //                                                    checkInViewModel.submitCheckin( context, lifecycleOwner, viewModel, navController )
                    }


                }



            }
    }

    if (openDialog.value) {

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Check In")
            },
            text = {
                Text("Do you want to Confirm this Shift time : $selectedShift")
            },
            confirmButton = {
                Button(
                    onClick = {


                        openDialog.value = false


                        if(userViewModel.hasGeoFencing() || userViewModel.hasLocationCheckNeed())
                        {
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
                                checkInShift()
                            }
                        }
                        else
                        {
                            if(!isTimeZoneAutomaticEnabled.value)
                            {
                                navController.navigate("fakeTime")
                            }
                            else
                            {
                                checkInShift()
                            }
                        }





                    }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        selectedShift = ""
                        selectedShiftId = ""
                        selectedShiftStartTime = ""
                        selectedShiftEndTime = ""
                        selectedShiftCutOff = ""
                        openDialog.value = false
                    }) {
                    Text("Cancel")
                }

            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
    ) {


        if (shouldShowCamera1.value)
        {
            val uri = ComposeFileProvider.getImageUri(context)
            imageUri = uri
            navController.navigate("CameraScreen")
            shouldShowCamera1.value = false

        }
        cameraExecutor1 = Executors.newSingleThreadExecutor()


        LazyColumn {
            items(shiftListDetails.value) { shift ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 10.dp)
                        .clickable {
                            openDialog.value = true
                            selectedShift = shift.name
                            selectedShiftId = shift.id
                            selectedShiftStartTime = shift.shiftStartTime
                            selectedShiftEndTime = shift.shiftEndTime
                            selectedShiftCutOff = shift.aCutOff
                        }, colors = CardDefaults.cardColors(
                        containerColor = colorResource(
                            id = R.color.white
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = shift.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = colorResource(
                                    id = R.color.black
                                )
                            )


                            Log.d("shift times....", "$selectedShiftStartTime - $selectedShiftEndTime")


                            val startTimeParts = shift.shiftStartTime.split(" ")[1].split(":")
                            val hours = startTimeParts[0]
                            val minutes = startTimeParts[1]

                            val startTimeString = "$hours:$minutes"

                            val endTimeParts = shift.shiftEndTime.split(" ")[1].split(":")
                            val hours1 = endTimeParts[0]
                            val minutes1 = endTimeParts[1]

                            val endTimeString = "$hours1:$minutes1"

                            Log.d("shift times....", "$startTimeString - $endTimeString")

                            Text(
                                text = "$startTimeString - $endTimeString",
                                style = MaterialTheme.typography.titleSmall,
                                color = colorResource(
                                    id = R.color.paraColor
                                )
                            )
                        }

                    }
                }

            }

        }

    }

}











