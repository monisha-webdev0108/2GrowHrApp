package com.payroll.twogrowhr.Model.View

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.payroll.twogrowhr.Constant
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.checkInViewModel
import com.payroll.twogrowhr.components.isMockLocation
import com.payroll.twogrowhr.components.isTimeZoneAutomaticEnabled
import com.payroll.twogrowhr.components.loading
import com.payroll.twogrowhr.components.userViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executors



@Composable
fun CameraScreen(navController: NavController) {

    val lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_FRONT) }

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showCameraPreview by remember { mutableStateOf(true) } // To control visibility
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var overlayVisible by remember { mutableStateOf(false) }


    TransparentOverlay(overlayVisible) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.backgroundColor))
                .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
        ) {


            fun checkInCamera()
            {

                overlayVisible = true
                coroutineScope.launch {
                    kotlinx.coroutines.delay(2000)
                    overlayVisible = false
                }
                loading.value = true
                checkInViewModel.upload(
                    context,
                    navController,
                    capturedImageUri!!
                )

            }

            if (showCameraPreview) {

                Row {
                    CameraView(
                        navController,
                        outputDirectory = filess,
                        executor = cameraExecutor,
                        onImageCaptured = { uri ->
                            capturedImageUri = uri
                            showCameraPreview = false // Switch to captured image view
                        },
                        lensFacing,
                        onError = { Log.e("CameraScreen kilo", "View error:", it) }
                    )
                }
            } else {


                Constant.AppTheme {
                    Row(
                        modifier = Modifier.fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Column {
                            Button(
                                onClick = {
                                    showCameraPreview = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh, // Use any icon you want
                                    contentDescription = "Retake" // Provide a content description for accessibility
                                )
                            }

                        }
                        Column {

                            Button(
                                onClick = {

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
                                            checkInCamera()
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
                                            checkInCamera()
                                        }
                                    }

                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = "Save Image"
                                )
                            }
                        }
                    }
                }




                // Display the captured image if available
                Row {
                    capturedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(model = uri),
                            contentDescription = "Captured Image",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            cameraExecutor = Executors.newSingleThreadExecutor()
        }

    }

}


@Composable
fun TransparentOverlay(
    visible: Boolean,
    color: Color = Color.Black.copy(alpha = 0.5f),
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        content()

        if (visible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 1f), // Use 0f for fully transparent
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color)
                )
            }
        }
    }
}