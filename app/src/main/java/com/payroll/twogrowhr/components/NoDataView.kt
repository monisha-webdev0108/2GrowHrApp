package com.payroll.twogrowhr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payroll.twogrowhr.R
import com.payroll.twogrowhr.ui.theme.poppins_font

@Composable
fun noDataView()
{
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

/*        Column {
            Image(
                painter = painterResource(id = R.drawable.noleavesvg),
                contentDescription = null, // Set to null if the image is decorative
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(with(LocalDensity.current) { 150.dp }),
                contentScale = ContentScale.Fit
            )
        }*/

        Column {
            Image(
                painter = painterResource(id = R.drawable.nodata),
                contentDescription = null, // Set to null if the image is decorative
                modifier = Modifier
                    .size(with(LocalDensity.current) { 250.dp }),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "No data to show",
                style = TextStyle(
                    fontFamily = poppins_font,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(500),
                    color = colorResource(id = R.color.paraColor)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

    }
}