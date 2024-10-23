package com.example.twogrowhr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.payroll.twogrowhr.R

//import com.example.twogrowhr.Screen.Intro.route
@Composable
fun IntroScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize(), ){
        Box{
            Box(modifier = Modifier
                .fillMaxWidth(1f)
                .height(250.dp)
                .background(color = colorResource(id = R.color.backgroundColor))) {
            }
            Box(modifier = Modifier
                .fillMaxWidth(1f)
                .offset(y = 70.dp),) {
                Image(painterResource(id = R.drawable.login_img) , contentDescription ="intro_img", modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .align(
                        Alignment.Center
                    ))
            }
        }
        Column(modifier = Modifier.padding(top = 100.dp, start = 25.dp, end = 25.dp)) {
            Text(text = "Get Started", color = colorResource(id = R.color.paraColor),style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500))
            Text(text = "Millions of people use to", color = colorResource(id = R.color.themeColor), style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.W700), modifier = Modifier.padding(top=10.dp))
            Text(text = "made their work", color = colorResource(id = R.color.themeColor), style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.W700),modifier = Modifier.padding(top=10.dp))
            Text(text = "effortlessly.", color = colorResource(id = R.color.themeColor), style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.W700),modifier = Modifier.padding(top=10.dp))
            Button(
                onClick = {
                    navController.navigate("B")
                },
                shape = CutCornerShape(10),
                colors =  ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary), modifier = Modifier.fillMaxWidth(1f).padding(top=40.dp).height(55.dp)

            ) {
                Text(text = "Continous")
            }
        }


    }
}



