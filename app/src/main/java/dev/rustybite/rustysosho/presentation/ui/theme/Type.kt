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
private val MontserratLight = FontFamily(Font(R.font.montserrat_light, FontWeight.Light))


val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MontserratRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 32.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 28.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = MontserratMedium,
        fontSize = 16.sp
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