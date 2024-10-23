package com.payroll.twogrowhr.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScaffold(
    drawerState: DrawerState,
    topBarContent: @Composable () -> Unit,
    drawerContent: @Composable () -> Unit,
    bottomBarContent: @Composable () -> Unit,
    onBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {


    Log.d("Drawer App scaffold","Drawer : $drawerContent")

    BackPressHandler(onBackPressed = onBack, drawerState = drawerState)
    rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerContent = drawerContent,
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = { topBarContent() },
                bottomBar = { bottomBarContent() },
            ) { paddingValues ->
                content(paddingValues)
            }
        }

}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppScaffold1(
    topBarContent: @Composable () -> Unit,
    bottomBarContent: @Composable () -> Unit,
    onBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    BackPressHandler(onBackPressed = onBack, drawerState = drawerState)

        Scaffold(
            topBar = { topBarContent() },
            bottomBar = { bottomBarContent() },
        ) { paddingValues ->
            content(paddingValues)
        }

}
