package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.userViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



var flipNeeded = 0

@SuppressLint("ResourceAsColor")
@Composable
fun CameraView(
    navController: NavController,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    lensFacing : Int,
    onError: (ImageCaptureException) -> Unit
) {
    // 1
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lensFacing1 by remember { mutableIntStateOf(lensFacing) }
    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing1)
        .build()


    // 2
    LaunchedEffect(lensFacing1) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    val coroutineScope = rememberCoroutineScope()
    var overlayVisible by remember { mutableStateOf(false) }


    var buttonClicked by remember { mutableStateOf(false) }

    TransparentOverlay(overlayVisible) {

        Column(modifier = Modifier.fillMaxSize().fillMaxWidth())
        {

            // 3
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize().fillMaxWidth()) {


                AndroidView({ previewView }, modifier = Modifier.fillMaxSize().fillMaxWidth())

                //icons

                Row( modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp) , horizontalArrangement = Arrangement.SpaceBetween)// Set the background color here)
                {

                    //Back Button



                    OutlinedButton(onClick = {

                        if(!buttonClicked)
                        {
                            navController.popBackStack()

                            buttonClicked = true

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                buttonClicked = false
                            }
                        }

                         },
                        modifier= Modifier.size(50.dp),  //avoid the oval shape
                        shape = CircleShape,
                        border= BorderStroke(1.dp, Color(R.color.divider)),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Black.copy(alpha = 0.5f) // Set the background to transparent or your desired color
                        ))
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.previous),
                            contentDescription = "Back Button",
                            tint = Color(ContextCompat.getColor(context, R.color.white)),
                            modifier = Modifier.size(30.dp)
                        )
                    }


                    //Capture Button


                    OutlinedButton(onClick = {
                        overlayVisible = true
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(2000)
                            overlayVisible = false
                        }

                        Log.i("kilo", "ON CLICK")
                        takePhoto(
                            imageCapture = imageCapture,
                            outputDirectory = outputDirectory,
                            executor = executor,
                            onImageCaptured = onImageCaptured,
                            onError = onError
                        ) },
                        modifier= Modifier.size(60.dp).padding(bottom = 10.dp),  //avoid the oval shape
                        shape = CircleShape,
                        border= BorderStroke(1.dp, Color(R.color.divider)),
                        contentPadding = PaddingValues(0.dp),  //avoid the little icon
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color.Black.copy(alpha = 0.5f) // Set the background to transparent or your desired color
                        ))
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.take_photos),
                            contentDescription = "Take picture",
                            tint = Color(ContextCompat.getColor(context, R.color.white)),
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }


                    Log.d("camera Flip", "${userViewModel.isCameraFlipNeed()}")

                    //Flip Option

                    if(userViewModel.isCameraFlipNeed())
                    {
                        OutlinedButton(onClick = {
                            lensFacing1 = if (lensFacing1 == CameraSelector.LENS_FACING_FRONT) {
                                CameraSelector.LENS_FACING_BACK
                            } else {
                                CameraSelector.LENS_FACING_FRONT
                            }
                        },
                            modifier= Modifier.size(50.dp),  //avoid the oval shape
                            shape = CircleShape,
                            border= BorderStroke(1.dp, Color(R.color.divider)),
                            contentPadding = PaddingValues(0.dp),  //avoid the little icon
                            colors = ButtonDefaults.outlinedButtonColors(
                                backgroundColor = Color.Black.copy(alpha = 0.5f) // Set the background to transparent or your desired color
                            ))
                        {
                            Icon(
                                painter = painterResource(id = R.drawable.flip_circular_arows),
                                contentDescription = "Flip Button",
                                tint = Color(ContextCompat.getColor(context, R.color.white)),
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }




                }


            }


        }

    }

}



private fun takePhoto(
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputDirectory).build()

    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("kilo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(outputDirectory)
            onImageCaptured(savedUri)
        }
    })
}

@Suppress("BlockingMethodInNonBlockingContext")
private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}




