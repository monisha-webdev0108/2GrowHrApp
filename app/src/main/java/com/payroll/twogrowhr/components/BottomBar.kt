package com.payroll.twogrowhr.components



import android.util.Log
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.ui.theme.poppins_font

@Composable
fun BottomNav(navController: NavController) {
    val items = listOf(
        Screen.HomeScreen,
        Screen.Attendance,
        Screen.Finance,
        Screen.Reports
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    BottomNavigation( backgroundColor = Color.White,modifier = Modifier
        .graphicsLayer { clip = true }
        .shadow(10.dp)) {
        items.forEach { screen ->

            val actualRoute = currentRoute?.substringBefore("?") // Extract actual route

            val selected = actualRoute == screen.route

            val color = if(selected) R.color.themeColor else R.color.paraColor
            BottomNavigationItem(
                selected = currentRoute == screen.route,
/*                unselectedContentColor = colorResource(id = R.color.paraColor),
                selectedContentColor = colorResource(id = R.color.themeColor),*/
                onClick = { navController.navigate(screen.route) },
                label = { Text(text = (screen.label),
                    style = TextStyle(
                        fontFamily = poppins_font,
                        fontSize = 11.sp,
                        fontWeight = FontWeight(500),
                        color = colorResource(id = color)
                    )
                ) },
                icon ={
                    Icon(
                        painterResource(id = screen.icon),
                        contentDescription = "Navigation Icon",
                        modifier = Modifier.padding(bottom = 8.dp),
                        tint = colorResource(id = color)
                    )
                }
            )
        }
    }
}

