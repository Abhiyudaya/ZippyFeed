package com.example.zippyfeed.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.zippyfeed.R

val poppinsFontFamily = FontFamily(
    Font(R.font.poppinsregular,FontWeight.Normal),
    Font(R.font.poppinsmedium,FontWeight.Medium),
    Font(R.font.poppinssemibold,FontWeight.SemiBold),
    Font(R.font.poppinsbold,FontWeight.Bold),
    Font(R.font.poppinsthin,FontWeight.Thin),
    Font(R.font.poppinslight,FontWeight.Light),
    Font(R.font.poppinsextralight,FontWeight.ExtraLight),
    Font(R.font.poppinsblack,FontWeight.Black)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
)