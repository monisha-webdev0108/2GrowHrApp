package com.payroll.twogrowhr.components

import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Checkbox
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.SharedPreferenceManager
import com.payroll.twogrowhr.viewModel.MainViewModel

@Composable
fun PrivacyPolicy(navController: NavController, viewModel: MainViewModel) {

    var accepted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val privacyPolicyUrl = "https://2growhr.io/privacy.html"
    val isLoggedIn = SharedPreferenceManager.isLoggedIn(LocalContext.current)



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // WebView to display the privacy policy content
        AndroidView(
            factory = { context ->

                WebView(context).apply {
                    loadUrl(privacyPolicyUrl)

                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        )
        // Acceptance checkbox
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = accepted,
                onCheckedChange = { newAccepted ->
                    accepted = newAccepted
                }
            )
            Text(
                text = "I accept the Privacy Policy",
                modifier = Modifier.padding(start = 8.dp)
            )
        }


        Button(onClick = {
            if(accepted)
            {
                SharedPreferenceManager.setPrivacyPolicy(context,true)

                if (isLoggedIn)
                {
                    navController.navigate(Screen.HomeScreen.route)
                }
                else
                {
                    navController.navigate("Login") { popUpTo("Login") }
                }
            }
        },
            shape = RoundedCornerShape(0),
            colors =  ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)), modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 20.dp)
                .height(55.dp)) {
            androidx.compose.material3.Text(text = "Proceed", style = MaterialTheme.typography.titleMedium, color = colorResource(id = R.color.white))
        }
    }
}
