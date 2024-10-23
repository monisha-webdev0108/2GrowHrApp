package com.payroll.twogrowhr.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.ui.theme.poppins_font

@Composable
fun exceptionScreen()
{
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Something went wrong.....!",
            style = TextStyle(
                fontFamily = poppins_font,
                fontSize = 15.sp,
                fontWeight = FontWeight(500),
                color = colorResource(id = R.color.paraColor)
            )
        )
    }
}