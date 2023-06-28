package dev.rustybite.rustysosho.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.rustybite.rustysosho.R

// Set of Material typography styles to start with

private val MontserratBold = FontFamily(Font(R.font.montserrat_bold))
private val MontserratMedium = FontFamily(Font(R.font.montserrat_medium))
private val MontserratRegular = FontFamily(Font(R.font.montserrat_regular))
private val MontserratLight = FontFamily(Font(R.font.montserrat_light))


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MontserratLight,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
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