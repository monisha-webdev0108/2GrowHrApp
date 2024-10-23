package com.payroll.twogrowhr


import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.payroll.twogrowhr.Model.View.lats
import com.payroll.twogrowhr.Model.View.longs
import com.payroll.twogrowhr.components.App_Nav
import com.payroll.twogrowhr.components.isDeveloperEnabled
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.ui.theme.Sample_appTheme
import com.payroll.twogrowhr.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job


val LocalContext = compositionLocalOf<Context> { error("No Context provided") }

var redirectJob: Job? = null

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            Sample_appTheme {
                CompositionLocalProvider(LocalContext provides this)
                {
                    val isTopBarVisible = remember { mutableStateOf(true) }
                    App_Nav(viewModel)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {

        super.onResume() // Call super.onResume()

        Log.d("MainActivity", "Inside isFakeGPSDetected")

//        isDeveloperEnabled.value = Settings.Secure.getInt(this.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED) == 1

        Log.d("MainActivity", "isTimeZoneAutomatic_0: ${isTimeZoneAutomaticEnabled.value}")

        isTimeZoneAutomaticEnabled.value = timeZoneEnabled()

        Log.d("MainActivity", "isTimeZoneAutomatic_1: ${isTimeZoneAutomaticEnabled.value}")


        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        Log.d("MainActivity", "isMockLocation_0: ${isMockLocation.value}")

        if (!isLocationEnabled) {

            Constant.showToast(this, "Please turn on the location")

            Log.d("MainActivity", "Location services not enabled. Launching settings activity.")

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

            try
            {
                this.startActivity(intent)
                Log.d("MainActivity", "Settings activity launched successfully.")
            }
            catch (e: Exception)
            {
                Log.e("MainActivity", "Error launching settings activity: ${e.message}")
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            checkIsMockLocation()
        }, 500)

        Log.d("MainActivity" ,"isDeveloperEnabled : ${isDeveloperEnabled.value}")
        Log.d("MainActivity", "isMockLocation_2: ${isMockLocation.value}")


/*        if(!isDeveloperEnabled.value)
        {

            isMockLocation.value = false

*/
    /*            try
            {
                val value = if(isMockLocation.value) "0" else "1"
                Settings.Secure.putString(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION, value)
            }
            catch (e: SecurityException) {
                Log.d("MainActivity", "isMockLocation Exception: ${isMockLocation.value}")
            }*/
    /*

            Log.d("MainActivity", "isMockLocation_3: ${isMockLocation.value}")
        }*/
    }

    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @Suppress("DEPRECATION")
    private fun checkIsMockLocation() {
        if (!Constant.isNetworkAvailable(this)) {
            Constant.showToast(this, "Please check your network connection")
//                navController.navigate("${Screen.Network.route}/HomeScreen")
        } else {
            locationProvider = LocationServices.getFusedLocationProviderClient(this)
            val locationRequest = LocationRequest().setInterval(2000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

                locationCallback =  object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                           val lat = location.latitude
                           val long = location.longitude
                            isMockLocation.value = location.isFromMockProvider

                            Log.d("MainActivity", "isMockLocation_1: ${isMockLocation.value}")
                            Log.d("MainActivity...", "latitude : $lat,   longitude : $long")

                            if(isMockLocation.value)
                            {
                                stopLocationUpdates()
                            }
                        }
                    }
                }


            locationProvider.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        }
    }

    private fun timeZoneEnabled(): Boolean
    {
        val timeZoneEnabled = Settings.Global.getInt(this.contentResolver, Settings.Global.AUTO_TIME_ZONE, 0) == 1
        val timeEnabled = Settings.Global.getInt(this.contentResolver, Settings.Global.AUTO_TIME, 0) == 1

        return timeZoneEnabled && timeEnabled
    }

    private fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the redirect job when the activity is destroyed to avoid memory leaks
        redirectJob?.cancel()
    }





/*    @Suppress("OVERRIDE_DEPRECATION")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Toast.makeText(this, "There is no back option", Toast.LENGTH_SHORT).show()
    }*/
}

//https://stackoverflow.com/questions/75633491/on-back-pressed-finish-the-activity-in-jetpack-compose
fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewApp() {
//    MainApp()
}