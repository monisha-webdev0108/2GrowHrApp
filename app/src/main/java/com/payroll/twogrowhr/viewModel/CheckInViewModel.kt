package com.payroll.twogrowhr.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.LocationManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.Model.ResponseModel.EmpShiftData
import com.payroll.twogrowhr.Model.ResponseModel.GeoData
import com.payroll.twogrowhr.Model.ResponseModel.ShiftData
import com.payroll.twogrowhr.Model.View.addressText
import com.payroll.twogrowhr.Model.View.checkinflag
import com.payroll.twogrowhr.Model.View.imageFileName
import com.payroll.twogrowhr.Model.View.img
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.locationProvider
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.Model.View.selectedShift
import com.payroll.twogrowhr.Model.View.selectedShiftCutOff
import com.payroll.twogrowhr.Model.View.selectedShiftEndTime
import com.payroll.twogrowhr.Model.View.selectedShiftId
import com.payroll.twogrowhr.Model.View.selectedShiftStartTime
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.components.userViewModel
import com.payroll.twogrowhr.repository.Repository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import com.payroll.twogrowhr.Model.ResponseModel.TodayAttendanceLogData
import com.payroll.twogrowhr.components.Screen
import com.payroll.twogrowhr.redirectJob
import com.payroll.twogrowhr.util.Resource
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class CheckInViewModel(
    private var repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val empID = userViewModel.getSFCode()
    private val divisionCode = userViewModel.getDivisionCode()


//------------------------------------------GET EMPLOYEE SHIFT DETAILS--------------------------------------------------//


    val empShiftList = savedStateHandle.getStateFlow("empShiftList", emptyList<EmpShiftData>())

    fun getEmpShiftDetails(
        context: Context,
        navController: NavController,
        empId: String
    ) = viewModelScope.launch {

        when(val response = repository.getEmpShiftResponse(context = context, sfCode = empId))
        {
            is Resource.Loading->
            {

            }
            is Resource.Success->
            {
                response.let {
                    loading.value = false
                    redirectJob?.cancel()
                    val data = response.data
                    if(data?.success == true)
                    {
                        Log.d("CheckInViewModel", "qwerty : getEmpShiftDetails API call was successful/true : ${data.head}")
                        savedStateHandle["empShiftList"] = data.head
                    }
                    else
                    {
                        savedStateHandle["empShiftList"] = emptyList<EmpShiftData>()
                        Log.d("CheckInViewModel", "qwerty : getEmpShiftDetails API call was successful/false : ${data?.head}")
                    }

                }
            }
            is Resource.Error->
            {
                loading.value = false
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/HomeScreen")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        savedStateHandle["empShiftList"] = emptyList<EmpShiftData>()
                        Log.d("CheckInViewModel", "qwerty : getEmpShiftDetails API call was successful /Data Not Found")
                    }
                }

                Log.d("CheckInViewModel", "qwerty : getEmpShiftDetails API call was not successful")
                return@launch
            }
        }

    }


//------------------------------------------GET TODAYS ATTENDANCE LOG DETAILS--------------------------------------------------//


    val todaysLogList = savedStateHandle.getStateFlow("todaysLogList", emptyList<TodayAttendanceLogData>())

    fun getTodaysLogDetails(
        context: Context,
        navController: NavController,
        empId: String
    ) = viewModelScope.launch {

        when(val response = repository.getTodaysLogResponse(context = context, sfCode = empId, divisionCode = userViewModel.getDivisionCode().toString()))
        {
            is Resource.Loading->
            {
                loading.value = true
            }
            is Resource.Success->
            {
                response.let {
                    loading.value = false
                    redirectJob?.cancel()
                    val data = response.data
                    if(data?.success == true)
                    {
                        Log.d("CheckInViewModel", "qwerty : getTodaysLogDetails API call was successful/true : ${data.data}")
                        savedStateHandle["todaysLogList"] = data.data
                    }
                    else
                    {
                        savedStateHandle["todaysLogList"] = emptyList<TodayAttendanceLogData>()
                        Log.d("CheckInViewModel", "qwerty : getTodaysLogDetails API call was successful/false : ${data?.data}")
                    }

                }
            }
            is Resource.Error->
            {
                loading.value = false
                if(response.message.toString().isNotEmpty())
                {
                    if(response.message.toString() != "Not Found")
                    {
                        if(response.message.toString() == "Please check your network connection")
                        {
                            navController.navigate("${Screen.Network.route}/HomeScreen")
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                        else
                        {
                            Constant.showToast(context, response.message ?: "Something went wrong")
                        }
                    }
                    else
                    {
                        savedStateHandle["todaysLogList"] = emptyList<TodayAttendanceLogData>()
                        Log.d("CheckInViewModel", "qwerty : getTodaysLogDetails API call was successful /Data Not Found")
                    }
                }

                Log.d("CheckInViewModel", "qwerty : getTodaysLogDetails API call was not successful")
                return@launch
            }
        }

    }



//------------------------------------------GET SHIFT DETAILS--------------------------------------------------//


    val shiftList = savedStateHandle.getStateFlow("shiftList", emptyList<ShiftData>())
    private val stateCode : Int = 0

    fun getShiftDetails1(
        context: Context,
        navController: NavController,
        empId: String
    ) = CoroutineScope(Dispatchers.IO).launch {

        Log.d("CheckInViewModel", "qwerty : Inside getShiftDetails1")


        when(val response = repository.getShiftResponse(context = context, sfCode = empId, divisionCode.toString(),stateCode.toString()))
        {
            is Resource.Loading->
            {

            }
            is Resource.Success->
            {

                withContext(Dispatchers.Main) {

                    val data = response.data
                    data?.let {
                        val success = data.success

                        if(success)
                        {
                            Log.d("CheckInViewModel", "qwerty : getShiftDetails1 API call was successful/true : ${data.data}")
                            savedStateHandle["shiftList"] = it.data
                        }
                        else
                        {
                            Log.d("CheckInViewModel", "qwerty : getShiftDetails1 API call was successful/false : ${data.data}")
                            savedStateHandle["shiftList"] = emptyList<ShiftData>()
                        }
                    }
                }



            }
            is Resource.Error->
            {

                withContext(Dispatchers.Main) {

                    if(response.message.toString().isNotEmpty())
                    {
                        if(response.message.toString() != "Not Found")
                        {
                            if(response.message.toString() == "Please check your network connection")
                            {
                                navController.navigate("${Screen.Network.route}/HomeScreen")
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                            else
                            {
                                Constant.showToast(context, response.message ?: "Something went wrong")
                            }
                        }
                        else
                        {
                            Log.d("CheckInViewModel", "qwerty : getShiftDetails1 API call was successful / Data Not Found")
                            savedStateHandle["shiftList"] = emptyList<ShiftData>()
                        }
                    }
                    Log.d("CheckInViewModel", "qwerty : getShiftDetails1 API call not successful")
                    return@withContext

                }

            }
        }


    }

//------------------------------------------GET GEO DETAILS--------------------------------------------------//


    fun getGeoDetails1(context : Context, navController: NavController, empId: String,callback: (List<GeoData>?) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        try {


            when(val response = repository.getGeoResponse(context = context, sfCode = empId)){
                is Resource.Loading->{

                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false
                            redirectJob?.cancel()
                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            val success = data?.success

                            if(success== true)
                            {
                                Log.d("CheckInViewModel", "qwerty : getGeoDetails1 API call was successful/true. : ${data.Head}")
                                callback(data.Head)
                                return@let
                            }
                            else
                            {
                                Log.d("CheckInViewModel", "qwerty : getGeoDetails1 API call was successful/false. : ${data?.Head}")
                                callback(null)
                                return@let
                            }
                        }

                    }

                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        if(response.message.toString().isNotEmpty())
                        {
                            if(response.message.toString() != "Not Found")
                            {
                                if(response.message.toString() == "Please check your network connection")
                                {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                            else
                            {
                                Log.d("CheckInViewModel", "qwerty : getGeoDetails1 API call was successful/Data Not Found")
                                callback(null)
                            }
                        }
                        Log.d("CheckInViewModel", "getGeoDetails1 API call was not successful")
                        return@withContext

                    }

                }
            }

        }
        catch (e: Exception)
        {
            loading.value = false
            Log.e("CheckInViewModel", "Error during API call: ${e.message}")
            callback(null)
        }

    }


//------------------------------------------TO CHECK PERMISSIONS--------------------------------------------------//


    fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean
    {
        return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    }

//------------------------------------------FOR CLEAR THE VALUES--------------------------------------------------//


    fun clearValues()
    {
        loading.value = false
        selectedShift = ""
        selectedShiftId = ""
        selectedShiftStartTime = ""
        selectedShiftEndTime = ""
        selectedShiftCutOff = ""
        imageFileName =""
    }

//------------------------------------------FUNCTION LAT AND LONG--------------------------------------------------//

    @Suppress("DEPRECATION")
    fun latLong(context: Context, navController: NavController)
    {
        Log.d("Current latLong...", "Inside latLong")

        CoroutineScope(Dispatchers.Main).launch{
            if (!Constant.isNetworkAvailable(context)) {
                clearValues()
                Constant.showToast(context, "Please check your network connection")
                navController.navigate("${Screen.Network.route}/HomeScreen")
            }
            else{
                locationProvider = LocationServices.getFusedLocationProviderClient(context)
                val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return@launch
                }
                locationProvider.requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                lats = location.latitude
                                longs = location.longitude
                            }
                        }
                    },
                    Looper.getMainLooper()
                )

                Log.d("Current latLong...", "latitude : $lats,   longitude : $longs")
            }


        }

    }


//------------------------------------------CHECK LAT AND LONG DISTANCE--------------------------------------------------//

    fun checkDistance(geoDetailsList: List<GeoData>): Int {
        var checkLat: Double
        var checkLong: Double
        var checkRadius: Int
        var fenced =0


// Iterate through the list using a for loop
        for (data in geoDetailsList)
        {
            checkLat = data.latitudes.toDouble()
            checkLong = data.longitudes.toDouble()
            checkRadius = data.radius.toInt()

            val theta: Double = checkLong - longs
            var dist: Double = sin(lats * Math.PI / 180.0) * sin(checkLat * Math.PI / 180.0) + cos(
                lats * Math.PI / 180.0) * cos(checkLat * Math.PI / 180.0) * cos(theta * Math.PI / 180.0)
            dist = acos(dist)
            dist = dist*180.0/Math.PI
            dist *= 60 * 1.1515 * 2 * 1000

//            Log.d("distance", "venueLat : $checkLat ---> venueLong : $checkLong ---> userLat : $lats ---> userLong ---> $longs ---> distance ---> $dist")
            if(checkRadius>dist)
            {
                fenced += 1
            }
        }

//        Log.d("distance", "--$fenced")
        return fenced
    }

//------------------------------------------FOR UPLOAD IMAGE--------------------------------------------------//


    fun upload(context: Context, navController: NavController, imageUri: Uri)
    {
        uploadImage(context,navController,empID,imageUri)

        Log.d("uploadDetails... ", "inside upload : $empID/$imageUri")

    }

//------------------------------------------FOR UPLOAD IMAGE THROUGH API CALL--------------------------------------------------//


    fun uploadImage(context: Context, navController: NavController, empID: String, imageuri: Uri) = CoroutineScope(Dispatchers.IO).launch {

        try {
            Log.d("uploadDetails... ", "inside uploadImage : $imageuri")

            val img: String = if(imageuri.toString().contains("/content")) {
                val pattern = Regex("/content:/com.payroll.twogrowhr.fileprovider/external_files/")
                pattern.replace(imageuri.toString(), "/storage/")
            } else {
                val pattern1 = Regex("content://com.payroll.twogrowhr.fileprovider/external_files/")
                pattern1.replace(imageuri.toString(), "/storage/")
            }
            Log.d("test", img)

            val file = File(img)
            val image = file.toString().replaceFirst(Regex("^file:"), "")
            Log.d("test1", image)

            val imagePath = File(image)
            Log.d("test2", image)

            val compressedImageFile = Compressor(context).compressToFile(imagePath)
            Log.d("test3", "$compressedImageFile")

            when (val response = repository.postCapturedImageResponse(context, image = compressedImageFile.toString())) {

               is Resource.Loading -> {

               }

                is Resource.Success -> {
                    withContext(Dispatchers.Main) {

                        val data = response.data
                        val message = data?.message

                        if(message == "File uploaded successfully")
                        {
                            loading.value = false
                            Log.d("CheckInViewModel", "catch : uploadImage API call was successful/true.")

                            if(userViewModel.hasLocationCheckNeed())
                            {

                                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                val isLocationEnabled = locationManager.isProviderEnabled(
                                    LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                                    LocationManager.NETWORK_PROVIDER)

                                try
                                {
                                    if(!isLocationEnabled)
                                    {
                                        Constant.showToast(context,"Please turn on the location")
                                        Log.d("CheckInViewModel...", "Location services not enabled. Launching settings activity.")

                                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                        try {
                                            context.startActivity(intent)
                                            Log.d("CheckInViewModel...", "Settings activity launched successfully.")
                                        } catch (e: Exception) {
                                            Log.e("CheckInViewModel...", "Error launching settings activity: ${e.message}")
                                        }
                                        latLong(context, navController)
                                    }
                                    else
                                    {
                                        Log.d("CheckInViewModel... ", "Current Location... Inside Upload Image : Location enabled")
                                        latLong(context, navController)//for getting current lat long..
                                        loading.value = false
                                        Constant.showToast(context = context ,"Fetching Location")
                                        navController.navigate("Currentlocation") { popUpTo("Currentlocation") }
                                    }
                                }
                                catch (e: Exception)
                                {
                                    Log.e("CheckInViewModel...", "Error during location check: ${e.message}")
                                }


                                // showload = false
                            }
                            else
                            {
                                if(userViewModel.hasGeoFencing())
                                {
                                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                    val isLocationEnabled = locationManager.isProviderEnabled(
                                        LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                                        LocationManager.NETWORK_PROVIDER)

                                    if(!isLocationEnabled)
                                    {
                                        Constant.showToast(context,"Please turn on the location")

                                        Log.d("CheckInViewModel... ", "Location... Location services not enabled. Launching settings activity.")

                                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

                                        try
                                        {
                                            context.startActivity(intent)
                                            Log.d("CheckInViewModel...", "Location... Settings activity launched successfully.")
                                        }
                                        catch (e: Exception)
                                        {
                                            Log.e("CheckInViewModel...", "Location... Error launching settings activity: ${e.message}")
                                        }
                                        latLong(context, navController)
                                    }
                                    else
                                    {
                                        latLong(context, navController)//for getting current lat long..

                                        getGeoDetails1(context,navController, empID){geoDataList ->

                                            Log.d("UserDataViewModel","from geoDetailsList-----$geoDataList")

                                            if (geoDataList.isNullOrEmpty())
                                            {
                                                loading.value = false
                                                Constant.showToast(context, "Fencing enabled but no bounds found...")
                                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }
                                            }
                                            else
                                            {
                                                val fenceDistance = checkDistance(geoDataList)


                                                if (fenceDistance > 0)
                                                {

                                                    if (checkinflag == "ckin")
                                                    {
                                                        submitCheckin2(context, navController)
                                                    }
                                                    else
                                                    {
                                                        submitCheckout2(context, navController)
                                                    }


                                                }
                                                else if (lats == 0.0)
                                                {
                                                    Constant.showToast(context, "Location not Fetched,Please Try Again after 10 seconds")
                                                    navController.navigate("HomeScreen")
                                                    {
                                                        popUpTo("HomeScreen")
                                                    }
                                                }
                                                else
                                                {
                                                    Constant.showToast( context, "You are out of bounds from the headquarters or Working place")
                                                    navController.navigate("HomeScreen") { popUpTo("HomeScreen")  }
                                                }

                                            }

                                        }

                                    }



                                }
                                else
                                {
                                    if (checkinflag == "ckin")
                                    {
                                        Log.d("CheckInViewModel...","inside else part for geofencing disabled selfie")
                                        submitCheckin2(context, navController)
                                    }
                                    else
                                    {
                                        Log.d("CheckInViewModel....","inside else part for geofencing disabled selfie")
                                        submitCheckout2(context, navController)
                                    }
                                }
                            }


                        }
                        else
                        {
                            if (message != null)
                            {
                                Constant.showToast(context, message)
                            }
                            loading.value = false
                            Log.d("CheckInViewModel", "qwerty : uploadImage API call was successful/false.")
                            navController.navigate("HomeScreen")
                            Constant.showToast(context, "Please try again later...")
                        }
                    }
                }
                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : uploadImage API call was not successful.")
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : uploadImage API call was not successful.")
                Log.d("CheckInViewModel... ", "UploadImage1... : $e")
                loading.value = false
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")
            }
        }
    }


//------------------------------------------SUBMIT CHECKIN TEST--------------------------------------------------//


    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckin(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckinTest")

        try
        {
            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            val selectedshiftid1 = if(selectedShiftId.isEmpty()) {
                0
            } else {
                selectedShiftId.toInt()
            }

            postObject.addProperty("Mode", "CIN")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("Shift_Selected_Id", selectedshiftid1)
            postObject.addProperty("Shift_Name", selectedShift)
            postObject.addProperty("ShiftStart", selectedShiftStartTime)
            postObject.addProperty("ShiftEnd", selectedShiftEndTime)
            postObject.addProperty("ShiftCutOff", selectedShiftCutOff)
            postObject.addProperty("App_Version", "Ver 1.0")
            postObject.addProperty("WrkType", "0")
            postObject.addProperty("CheckDutyFlag", "0")
            postObject.addProperty("On_Duty_Flag", "0")
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc","")
            postObject.addProperty("slfy", "")
            postObject.addProperty("Rmks", "")

            val checkInData = postObject.toString()

            Log.d("CheckInViewModel", "submitcheckin, checkIn Data : $checkInData")


            when(val response = repository.postCheckInResponse(context = context,checkInData = checkInData) ) {
                is Resource.Loading -> {
                    loading.value = true
                }

                is Resource.Success -> {

                    withContext(Dispatchers.Main) {
                        response.let {
                            loading.value = false
                            redirectJob?.cancel()
                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if (data?.success == true) {
                                checkinflag = "ckout"
                                val sharedPref =
                                    context.applicationContext?.getSharedPreferences("pref", 0)
                                        ?: return@let
                                with(sharedPref.edit()) {
                                    putString("checkinflag", checkinflag)
                                    apply()
                                }
                                /*                            selectedShift = ""
                                                            selectedShiftId = ""
                                                            selectedShiftStartTime = ""
                                                            selectedShiftEndTime = ""
                                                            selectedShiftCutOff = ""
                                                            imageFileName = ""
                                                            loading.value = false*/

                                clearValues()


                                Constant.showToast(context, "Checkin Successfully..!")
                                Log.d("CheckInViewModel", "catch : submitCheckin API call was successful/true : ${data.success}")

                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }
                            } else {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "catch : submitCheckin API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }


                        }
                    }

                }

                is Resource.Error -> {

                    withContext(Dispatchers.Main) {
                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckin API call was not successful")
                        return@withContext
                    }
                }
            }

        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                loading.value = false
                redirectJob?.cancel()
                Log.d("CheckInViewModel", "catch : submitCheckin API call was not successful")
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }

        }
    }




//------------------------------------------SUBMIT CHECKIN 1--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckin1(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

        Log.d("CheckInViewModel... ", " Inside submitCheckinTest1")


        try
        {
            val postObject = JsonObject()


            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            val selectedshiftid1 = if(selectedShiftId.isEmpty()) {
                0
            } else {
                selectedShiftId.toInt()
            }

            postObject.addProperty("Mode", "CIN")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("Shift_Selected_Id", selectedshiftid1)
            postObject.addProperty("Shift_Name", selectedShift)
            postObject.addProperty("ShiftStart", selectedShiftStartTime)
            postObject.addProperty("ShiftEnd", selectedShiftEndTime)
            postObject.addProperty("ShiftCutOff", selectedShiftCutOff)
            postObject.addProperty("App_Version", "Ver 1.0")
            postObject.addProperty("WrkType", "0")
            postObject.addProperty("CheckDutyFlag", "0")
            postObject.addProperty("On_Duty_Flag", "0")
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", img)
            postObject.addProperty("slfy", imageFileName)
            postObject.addProperty("Rmks", "")

            val checkInData = postObject.toString()

            Log.d("CheckInViewModel", "submitcheckin1, checkIn Data : $checkInData")


            when(val response = repository.postCheckInResponse(context = context,checkInData = checkInData)) {
                is Resource.Loading -> {
                    loading.value = true
                }

                is Resource.Success -> {


                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false
                            redirectJob?.cancel()
                            val data = response.data
                            savedStateHandle["success"] = data?.success
                            if (data?.success == true) {
                                checkinflag = "ckout"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)

                                /*                            selectedShift = ""
                                                            selectedShiftId = ""
                                                            selectedShiftStartTime = ""
                                                            selectedShiftEndTime = ""
                                                            selectedShiftCutOff = ""
                                                            imageFileName = ""
                                                            loading.value = false*/
                                clearValues()

                                Constant.showToast(context, "Checkin Successfully..!")
                                Log.d(
                                    "CheckInViewModel",
                                    "qwerty : submitCheckin1 API call was successful/true : ${data.success}"
                                )
                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }

                            } else {
                                loading.value = false
                                clearValues()
                                redirectJob?.cancel()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckin1 API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }


                    }


                }

                is Resource.Error -> {

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckin1 API call was not successful")

                    }


                }
            }

        }
        catch (e: Exception)
        {

            withContext(Dispatchers.Main) {

                e.printStackTrace()
                Log.d("CheckInViewModel", "catch : submitCheckin1 API call was not successful")
                clearValues()
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }


        }
    }

//------------------------------------------SUBMIT CHECKIN 2--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckin2(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckinTest2")


        try {

            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            val selectedshiftid1 = if(selectedShiftId.isEmpty()) {
                0
            } else {
                selectedShiftId.toInt()
            }

            postObject.addProperty("Mode", "CIN")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("Shift_Selected_Id", selectedshiftid1)
            postObject.addProperty("Shift_Name", selectedShift)
            postObject.addProperty("ShiftStart", selectedShiftStartTime)
            postObject.addProperty("ShiftEnd", selectedShiftEndTime)
            postObject.addProperty("ShiftCutOff", selectedShiftCutOff)
            postObject.addProperty("App_Version", "Ver 1.0")
            postObject.addProperty("WrkType", "0")
            postObject.addProperty("CheckDutyFlag", "0")
            postObject.addProperty("On_Duty_Flag", "0")
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", img)
            postObject.addProperty("slfy", imageFileName)
            postObject.addProperty("Rmks", "")

            val checkInData = postObject.toString()

            Log.d("CheckInViewModel", "submitcheckin2, checkIn Data : $checkInData")



            when(val response = repository.postCheckInResponse(context = context,checkInData = checkInData)){
                is Resource.Loading->{
                    loading.value = true
                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false
                            redirectJob?.cancel()
                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {

                                checkinflag = "ckout"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)

                                clearValues()

                                loading.value = false
                                Constant.showToast(context,"Checkin Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckin2 API call was successful/true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}

                            }
                            else
                            {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckin2 API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }

                    }


                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckin2 API call was not successful")
                        return@withContext

                    }

                }
            }

        }
        catch (e: Exception)
        {
            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : submitCheckin2 API call was not successful")
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }

        }

    }

//------------------------------------------SUBMIT CHECKIN 3--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckin3(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckinTest3")


        try
        {

            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            val selectedshiftid1 = if(selectedShiftId.isEmpty()) {
                0
            } else {
                selectedShiftId.toInt()
            }

            postObject.addProperty("Mode", "CIN")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("Shift_Selected_Id", selectedshiftid1)
            postObject.addProperty("Shift_Name", selectedShift)
            postObject.addProperty("ShiftStart", selectedShiftStartTime)
            postObject.addProperty("ShiftEnd", selectedShiftEndTime)
            postObject.addProperty("ShiftCutOff", selectedShiftCutOff)
            postObject.addProperty("App_Version", "Ver 1.0")
            postObject.addProperty("WrkType", "0")
            postObject.addProperty("CheckDutyFlag", "0")
            postObject.addProperty("On_Duty_Flag", "0")
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", "")
            postObject.addProperty("lat1", "")
            postObject.addProperty("long", "")
            postObject.addProperty("Lattitude", "")
            postObject.addProperty("Langitude", "")
            postObject.addProperty("iimgSrc", "")
            postObject.addProperty("slfy", "")
            postObject.addProperty("Rmks", "")

            val checkInData = postObject.toString()

            Log.d("CheckInViewModel", "submitcheckin3, checkIn Data : $checkInData")





            when(val response = repository.postCheckInResponse(context = context,checkInData = checkInData)){
                is Resource.Loading->{
                    loading.value = true
                }
                is Resource.Success->{


                    withContext(Dispatchers.Main) {

                        response.let {
                            redirectJob?.cancel()
                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {

                                checkinflag = "ckout"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)

                                clearValues()

                                Constant.showToast(context,"Checkin Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckin3 API call was successful/ true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}

                            }
                            else
                            {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckin3 API call was not successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }


                    }


                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckin3 API call was not successful")
                        return@withContext

                    }


                }
            }

        }
        catch (e: Exception)
        {


            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : submitCheckin3 API call was not successful")
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }


        }
    }

//------------------------------------------SUBMIT CHECKIN REMOTE--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckinRemote(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckinTest3")


        try
        {

            Log.d("CheckInViewModel... ", "Check In Remote/ Address Text --> $addressText")

            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            val selectedShiftId1 = if(selectedShiftId.isEmpty()) {
                0
            } else {
                selectedShiftId.toInt()
            }

            postObject.addProperty("Mode", "CIN")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("Shift_Selected_Id", selectedShiftId1)
            postObject.addProperty("Shift_Name", selectedShift)
            postObject.addProperty("ShiftStart", selectedShiftStartTime)
            postObject.addProperty("ShiftEnd", selectedShiftEndTime)
            postObject.addProperty("ShiftCutOff", selectedShiftCutOff)
            postObject.addProperty("App_Version", "Ver 1.0")
            postObject.addProperty("WrkType", "0")
            postObject.addProperty("CheckDutyFlag", "0")
            postObject.addProperty("On_Duty_Flag", "0")
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", "")
            postObject.addProperty("slfy", "")
            postObject.addProperty("Rmks", "")

            val checkInData = postObject.toString()

            Log.d("CheckInViewModel", "submitcheckin3, checkIn Data : $checkInData")




            when(val response = repository.postCheckInResponse(context = context,checkInData = checkInData) ) {
                is Resource.Loading -> {
                    loading.value = true
                }

                is Resource.Success -> {


                    withContext(Dispatchers.Main) {

                        response.let {

                            val data = response.data

                            savedStateHandle["success"] = data?.success


                            if(data?.success == true)
                            {

                                checkinflag = "ckout"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                selectedShift = ""
                                selectedShiftId = ""
                                selectedShiftStartTime = ""
                                selectedShiftEndTime = ""
                                selectedShiftCutOff = ""
                                imageFileName =""
                                loading.value = false

                                Constant.showToast(context,"Checkin Successfully..!")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}

                            }
                            else
                            {
                                loading.value = false
                                Constant.showToast(context, "Please try again later....")
                                navController.navigate("HomeScreen")
                            }





                            if (data?.success == true) {
                                checkinflag = "ckout"
                                val sharedPref =
                                    context.applicationContext?.getSharedPreferences("pref", 0)
                                        ?: return@let
                                with(sharedPref.edit()) {
                                    putString("checkinflag", checkinflag)
                                    apply()
                                }
                                selectedShift = ""
                                selectedShiftId = ""
                                selectedShiftStartTime = ""
                                selectedShiftEndTime = ""
                                selectedShiftCutOff = ""
                                imageFileName = ""
                                loading.value = false

                                Constant.showToast(context, "Checkin Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckinRemote API call was successful/true")
                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") }
                            } else {
                                loading.value = false
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckinRemote API call was successful/false")
                                navController.navigate("HomeScreen")
                            }


                        }

                    }


                }

                is Resource.Error -> {

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckinRemote API call was not successful")
                        return@withContext

                    }


                }


            }

        }
        catch (e: Exception)
        {


            withContext(Dispatchers.Main) {

                e.printStackTrace()
                Log.d("CheckInViewModel", "catch : submitCheckinRemote API call was not successful")
                loading.value = false
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }


        }
    }



//------------------------------------------SUBMIT CHECKOUT--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckOutRemote(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

        try
        {
            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            Log.d("submitCheckout....","submitcheckout")

            postObject.addProperty("Mode", "COUT")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", "")
            postObject.addProperty("slfy", "")
            postObject.addProperty("Rmks", "")


            val checkOutData = postObject.toString()

            Log.d("CheckInViewModel", "submitCheckout, checkOut Data : $checkOutData")


            when(val response = repository.postCheckOutResponse(context = context,empId = empID, checkOutData = checkOutData)){
                is Resource.Loading->{
                    loading.value = true
                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {

                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {
                                checkinflag = "ckin"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                loading.value = false
                                Constant.showToast(context,"Checkout Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckOutRemote API call was successful/true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}
                            }
                            else
                            {
                                loading.value = false
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckOutRemote API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }

                    }

                }
                is Resource.Error->{


                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        Log.d("CheckInViewModel", "qwerty : submitCheckOutRemote API call was not successful")
                        navController.navigate("HomeScreen")
                        return@withContext

                    }


                }
            }

        }
        catch (e: Exception)
        {

            withContext(Dispatchers.Main) {
                e.printStackTrace()
                Log.d("CheckInViewModel", "catch : submitCheckOutRemote API call was not successful")
                loading.value = false
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")
            }

        }
    }



//------------------------------------------SUBMIT CHECKOUT--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckout(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckoutTest")


        try
        {

            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            Log.d("submitCheckout....","submitcheckout")

            postObject.addProperty("Mode", "COUT")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", "")
            postObject.addProperty("lat1", 0.0)
            postObject.addProperty("long", 0.0)
            postObject.addProperty("Lattitude", 0.0)
            postObject.addProperty("Langitude", 0.0)
            postObject.addProperty("iimgSrc", "")
            postObject.addProperty("slfy", "")
            postObject.addProperty("Rmks", "")

            val checkOutData = postObject.toString()

            Log.d("CheckInViewModel", "submitCheckout, checkOut Data : $checkOutData")

            when(val response = repository.postCheckOutResponse(context = context,empId = empID, checkOutData = checkOutData)){
                is Resource.Loading->{
                    loading.value = true

                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false
                            redirectJob?.cancel()

                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {
                                checkinflag = "ckin"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                loading.value = false
                                clearValues()
                                Constant.showToast(context,"Checkout Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout API call was successful/true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}
                            }
                            else
                            {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }

                    }

                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        Log.d("CheckInViewModel", "qwerty : submitCheckout API call was not successful")
                        navController.navigate("HomeScreen")

                    }

                }
            }

        }
        catch (e: Exception)
        {

            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : submitCheckout API call was not successful")
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }

        }
    }


//------------------------------------------SUBMIT CHECKOUT 1--------------------------------------------------//

    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckout1(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckoutTest1")

        try
        {

            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            Log.d("CheckInViewModel....","submitcheckout1")


            postObject.addProperty("Mode", "COUT")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", img)
            postObject.addProperty("slfy", imageFileName)
            postObject.addProperty("Rmks", "")


            val checkOutData = postObject.toString()

            Log.d("CheckInViewModel", "submitCheckout, checkOut Data : $checkOutData")



            when(val response = repository.postCheckOutResponse(context = context,empId = empID, checkOutData = checkOutData)){
                is Resource.Loading->{
                    loading.value = true
                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false
                            redirectJob?.cancel()

                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {
                                checkinflag = "ckin"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                loading.value = false
                                clearValues()
                                Constant.showToast(context,"Checkout Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout1 API call was successful/ true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}
                            }
                            else
                            {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout1 API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }


                    }


                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        navController.navigate("HomeScreen")
                        Log.d("CheckInViewModel", "qwerty : submitCheckout1 API call was not successful")
                        return@withContext

                    }

                }
            }



        }
        catch (e: Exception)
        {

            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : submitCheckout1 API call was not successful")
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }

        }

    }



//------------------------------------------SUBMIT CHECKOUT TEST 2--------------------------------------------------//


    @SuppressLint("SuspiciousIndentation", "SimpleDateFormat")
    fun submitCheckout2(context: Context, navController: NavController) = CoroutineScope(Dispatchers.IO).launch {

//        Log.d("CheckInViewModel... ", "submitCheckoutTest2")

        try
        {
            val postObject = JsonObject()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val currentDateAndTime = sdf.format(Date())
            val time = currentDateAndTime.split("\\s".toRegex()).toTypedArray()

            Log.d("submitcheckout2....","submitcheckout2")

            postObject.addProperty("Mode", "COUT")
            postObject.addProperty("Divcode", divisionCode)
            postObject.addProperty("sfCode", empID)
            postObject.addProperty("eDate", currentDateAndTime)
            postObject.addProperty("eTime", time[1])
            postObject.addProperty("current_address", addressText)
            postObject.addProperty("lat1", lats)
            postObject.addProperty("long", longs)
            postObject.addProperty("Lattitude", lats)
            postObject.addProperty("Langitude", longs)
            postObject.addProperty("iimgSrc", img)
            postObject.addProperty("slfy", imageFileName)
            postObject.addProperty("Rmks", "")

            val checkOutData = postObject.toString()

            Log.d("CheckInViewModel", "submitCheckout, checkOut Data : $checkOutData")



            when(val response = repository.postCheckOutResponse(context = context,empId = empID, checkOutData = checkOutData)){
                is Resource.Loading->{
                    loading.value = true
                }
                is Resource.Success->{

                    withContext(Dispatchers.Main) {

                        response.let {
                            loading.value = false

                            redirectJob?.cancel()

                            val data = response.data

                            savedStateHandle["success"] = data?.success

                            if(data?.success == true)
                            {

                                checkinflag = "ckin"
                                SharedPreferenceManager.setCheckInOut(context, checkinflag)
                                clearValues()
                                loading.value = false
                                Constant.showToast(context,"Checkout Successfully..!")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout2 API call was successful/true : ${data.success}")
                                navController.navigate("HomeScreen"){popUpTo("HomeScreen")}

                            }
                            else
                            {
                                loading.value = false
                                clearValues()
                                Constant.showToast(context, "Please try again later....")
                                Log.d("CheckInViewModel", "qwerty : submitCheckout2 API call was successful/false : ${data?.success}")
                                navController.navigate("HomeScreen")
                            }
                        }

                    }


                }
                is Resource.Error->{

                    withContext(Dispatchers.Main) {

                        loading.value = false
                        clearValues()

                        if (response.message.toString().isNotEmpty())
                        {
                            if (response.message.toString() != "Not Found")
                            {
                                if (response.message.toString() == "Please check your network connection") {
                                    navController.navigate("${Screen.Network.route}/HomeScreen")
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                                else
                                {
                                    Constant.showToast(context, response.message ?: "Something went wrong")
                                }
                            }
                        }
                        Log.d("CheckInViewModel", "qwerty : submitCheckout2 API call was not successful")
                        navController.navigate("HomeScreen")
                        return@withContext
                    }


                }
            }
        }
        catch (e: Exception)
        {

            withContext(Dispatchers.Main) {

                e.printStackTrace()
                clearValues()
                Log.d("CheckInViewModel", "catch : submitCheckout2 API call was not successful")
                loading.value = false
                redirectJob?.cancel()
                Constant.showToast(context, "Something went wrong....")
                navController.navigate("HomeScreen")

            }

        }

    }



//------------------------------------------------------------------------------------------------------------//



}


//Geo Deleted


/*

val data = repository.getGeoResponse(sfCode = empId)

data?.let {
    val success = data.success

    if(success)
    {
        callback(it.Head)
        return@launch
    }
    else
    {
        callback(null)
        return@launch
    }
}

*/
