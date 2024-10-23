package com.payroll.twogrowhr.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.payroll.twogrowhr.R


val poppins_font= FontFamily(
    Font(R.font.poppins_bold,FontWeight.Bold),
    Font(R.font.poppin_light, FontWeight.Light),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_extralight, FontWeight.ExtraLight),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_thin, FontWeight.Thin),
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = poppins_font,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = poppins_font,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = poppins_font,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    ),
    titleSmall  = TextStyle(
        fontFamily = poppins_font,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    ),

    labelSmall = TextStyle(
        fontFamily = poppins_font,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
    ),


    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)