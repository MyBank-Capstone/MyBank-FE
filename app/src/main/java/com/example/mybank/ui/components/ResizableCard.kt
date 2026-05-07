package com.example.mybank.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.mybank.R // Penting agar R.drawable terbaca
import com.example.mybank.ui.theme.* // Agar Maroon, PureWhite, OnyxMain terbaca

@Composable
fun ResizableCard(
    modifier: Modifier = Modifier,
    containerColor: Color = PureWhite, // Warna default
    cornerRadius: Dp = 24.dp,         // Radius default sesuai desain
    content: @Composable () -> Unit    // "Slot" untuk isi kartu
) {
    Card(
        modifier = modifier.fillMaxWidth(), // Lebar selalu penuh
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Preview
@Composable
fun ResizableCardPrev() {
    ResizableCard() { }
}