package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold1
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.TopBarBackNavigation
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Notify(navController: NavController) {
    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold1(
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Notification",
            "HomeScreen"
        ) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } },
        onBack = { navController.navigateUp() }
    )
    { Notify_Screen(navController = navController) }


/*
    Scaffold(
        topBar ={ NavigateTopbar(navController = navController,"Notification","Home") },
        content = { Notify_Screen(navController = navController) },
        bottomBar = { BottomNav(navController) }
    )

 */

}

@Composable
fun Notify_Screen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.backgroundColor))
        .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)

    ){
        Card(modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)
            .padding(bottom = 20.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)), shape = RoundedCornerShape(0.dp) ) {
            Column {
                LazyColumn {
                    items(2){
                        Row(modifier = Modifier
                            .background(color = colorResource(id = R.color.light_backgroundColor))
                            .padding(10.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Leave Request received", style = MaterialTheme.typography.titleMedium, color = colorResource(
                                    id = R.color.black
                                ))
                            }
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                                Button(onClick = { /*TODO*/ },
                                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.themeColor)),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text(text = "Review")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}