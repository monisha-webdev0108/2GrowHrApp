package com.payroll.twogrowhr.Model.View

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.components.AppScaffold
import com.payroll.twogrowhr.components.BottomNav
import com.payroll.twogrowhr.components.DrawerContent
import com.payroll.twogrowhr.components.TopBarBackNavigation
import com.payroll.twogrowhr.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun Updates(navController: NavController, viewModel: MainViewModel) {

    val isLoggedIn = remember { mutableStateOf(true) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    AppScaffold(
        drawerState = drawerState,
        topBarContent = { TopBarBackNavigation(
            navController = navController,
            title = "Updates",
            "HomeScreen"
        ) },
        drawerContent = { DrawerContent(navController = navController, viewModel = viewModel) },
        bottomBarContent = {if(isLoggedIn.value) { BottomNav(navController) } })
    { Updates_Screen(navController = navController) }

}

@Composable
fun Updates_Screen(navController: NavController) {
    val colors = listOf(
        colorResource(id = R.color.green),
        colorResource(id = R.color.red),
        colorResource(id = R.color.yellow),
        colorResource(id = R.color.blue),
        colorResource(id = R.color.pink),

        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.backgroundColor))
            .padding(top = 80.dp, bottom = 55.dp, start = 20.dp, end = 20.dp)
    ) {
        LazyColumn {
            items(colors) { color ->
                Row() {
                    Column() {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .background(
                                    color = color,
                                    shape = CircleShape
                                )
                        )
                        Divider(
                            modifier = Modifier
                                .height(80.dp)
                                .padding(start = 6.dp, top = 4.dp, bottom = 4.dp)
                                .width(1.dp),
                            color = colorResource(id = R.color.paraColor)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = "Version 1.4",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.black)
                        )
                        Text(
                            text = "Attendance Regularization Added",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(id = R.color.paraColor)
                        )
                    }
                }
            }
        }
    }
}




