package com.example.mybank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle // Pastikan ini ter-import
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mybank.R

// 1. Perbaikan pada FontFamily (Hapus FontWeight yang dobel)
val WorkSans = FontFamily(
    Font(R.font.work_sans, FontWeight.Normal),
    Font(R.font.work_sans_medium, FontWeight.Medium),    // Cukup 2 parameter
    Font(R.font.work_sans_semibold, FontWeight.SemiBold),  // Cukup 2 parameter
    Font(R.font.work_sans_bold, FontWeight.Bold),      // Cukup 2 parameter
    Font(R.font.work_sans_italic, FontWeight.Normal, FontStyle.Italic)
)

// 2. Perbaikan Typography (Warna dihilangkan agar lebih fleksibel)
val MyBankTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    titleLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 19.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.sp
    ),
    labelSmall = TextStyle(
        fontFamily = WorkSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 19.sp
    )
)