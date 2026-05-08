package com.example.mybank.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybank.R
import com.example.mybank.ui.theme.OnyxMain
import com.example.mybank.ui.theme.PureWhite
import com.example.mybank.ui.theme.SubtleText

@Composable
fun MyBankPromoCard(
    title: String,
    description: String,
    hashtag: String,
    imageRes: Int,
    modifier: Modifier = Modifier // KUNCI: Modifier ini yang bikin card bisa beda ukuran
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Gunakan Box agar kita bisa menumpuk (stack) gambar, gradient, dan teks
        Box(modifier = Modifier.fillMaxSize()) {

            // --- 1. GAMBAR (Posisi di Kanan) ---
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Promo Image",
                contentScale = ContentScale.Crop, // Memastikan gambar memenuhi areanya
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Mengambil 50% ruang di sisi kanan
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            )

            // --- 2. EFEK FADE / GRADIENT (Menyambungkan Putih ke Gambar) ---
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Sedikit lebih lebar ke kiri dari gambar
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                PureWhite,                   // Kiri: Putih pekat
                                PureWhite.copy(alpha = 0.8f),// Tengah: Mulai transparan
                                Color.Transparent            // Kanan: Tembus pandang
                            )
                        )
                    )
            )

            // --- 3. KONTEN TEKS (Posisi di Kiri) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.65f) // Membatasi teks agar tidak menutupi gambar utama
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                // Logo MyBank
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_red_main),
                    contentDescription = "MyBank Logo",
                    modifier = Modifier.height(14.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp // Merapatkan jarak antar baris
                    ),
                    color = OnyxMain,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = SubtleText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = hashtag,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SubtleText
                )
            }
        }
    }
}