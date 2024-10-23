package com.payroll.twogrowhr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.redirectJob

@Composable
fun circularProgression()
{

    // Dispose of resources when the Composable is removed from the composition
    DisposableEffect(Unit) {
        onDispose {
            // Cancel redirectJob if it exists
            redirectJob?.cancel()
        }
    }

    Dialog(
        onDismissRequest = { loading.value = false },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(
                        Alignment.Center
                    ),
                color = colorResource(id = R.color.themeColor)
            )
        }
    }
}

@Composable
fun circularProgression1(loader : Boolean)
{
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    var loading = loader
    Dialog(
        onDismissRequest = {loading = false },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment= Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(
                        Alignment.Center
                    ),
                color = colorResource(id = R.color.themeColor)
            )
        }
    }
}

@Composable
fun linearProgression()
{
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .padding(bottom = 8.dp)
    )
}