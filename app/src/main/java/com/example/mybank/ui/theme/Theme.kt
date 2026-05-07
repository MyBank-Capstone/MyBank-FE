package com.example.mybank.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
/* private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)
*/

private val MyBankColorScheme = lightColorScheme(
    primary = RedMain,
    onPrimary = PureWhite,
    secondary = Maroon,
    background = PureWhite,
    surface = PureWhite,
    onSurface = OnyxMain,
    onSecondary = PureWhite
)

@Composable
fun MyBankTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        MyBankColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, // Menggunakan variabel colorScheme yang sudah dipilih
        typography = MyBankTypography,   // Mengambil konfigurasi Work Sans dari Type.kt
        content = content
    )
}