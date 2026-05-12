package com.example.mybank.ui.screens

import android.app.Activity
import com.example.mybank.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mybank.models.PromoList
import com.example.mybank.ui.components.*
import com.example.mybank.ui.theme.*

@Composable
fun PromoScreen(
    onBackClick: () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        // Gunakan LaunchedEffect(Unit) agar dieksekusi sekali saja saat screen aktif
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // State untuk memunculkan pop-up XAI
    var showXaiDialog by remember { mutableStateOf(false) }

    // Simulasi daftar promo dari API (menggunakan model Promo yang sama dengan Beranda)
    val promoList = listOf(
        PromoList("p1", "Waktunya Kopi Sore, Andi!", "Diskon spesial 30%", "#OnlyForYou", R.drawable.img_promo_coffee),
        PromoList("p2", "Terbangkan Penatmu, Andi!", "Diskon spesial 30%", "#OnlyForYou", R.drawable.img_flight_promo),
        PromoList("p3", "Liburan lagi ke Lombok!", "Liburan hemat dengan MY BANK.", "#OnlyForYou", R.drawable.img_promo_vacation)
    )

    Scaffold(
        topBar = {
            // Menggunakan Column karena susunannya atas-bawah (Back di atas, Judul di bawah)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PureWhite)
                    .systemBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 8.dp)
            ) {
                // BARIS 1: Tombol Back (Touch area mencakup panah + teks)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onBackClick() }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = null,
                        tint = RedMain,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kembali",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = RedMain
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                //BARIS 2: Judul Kiri & Ikon Info Kanan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Promo untuk anda",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp // Dibuat cukup menonjol
                        ),
                        color = RedMain
                    )

                    //Tombol Info (Pemicu Pop-up XAI)
                    IconButton(
                        onClick = { showXaiDialog = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info), // Sesuaikan id drawable (i)
                            contentDescription = "Penjelasan Rekomendasi",
                            tint = RedMain
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        // KONTEN UTAMA: Pengganti RecyclerView
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PureWhite)
                .padding(innerPadding),
            // Memberi jarak aman di sisi kiri, kanan, dan bawah list
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
            // KUNCI: Memberikan jarak rapi 16.dp antar card promo secara otomatis
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(promoList, key = { it.id }) { promo ->
                MyBankPromoCard(
                    title = promo.title,
                    description = promo.description,
                    hashtag = promo.hashtag,
                    imageRes = promo.imageRes,
                    // KUNCI REUSABILITY: Cukup lemparkan fillMaxWidth() agar merentang penuh
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
            }
        }

        // POP-UP XAI (Menggunakan Wadah Reusable MyBankDialog)
        MyBankDialog(
            showDialog = showXaiDialog,
            onDismiss = { showXaiDialog = false }
        ) {
            // Header Pop-up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp)) // Penyeimbang agar judul di tengah

                Text(
                    text = "Mengapa Saya melihat ini?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Maroon
                )

                IconButton(
                    onClick = { showXaiDialog = false },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_dismiss),
                        contentDescription = "Tutup",
                        tint = Maroon
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Isi Penjelasan XAI
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                XaiPointItem(
                    title = "1. Riwayat Transaksi:",
                    description = "Kamu telah melakukan 3 kali pembelian tiket pesawat dalam 6 bulan terakhir."
                )
                XaiPointItem(
                    title = "2. Kategori Favorit:",
                    description = "40% pengeluaranmu bulan ini ada pada kategori Travel & Leisure."
                )
                XaiPointItem(
                    title = "3. Pola Waktu:",
                    description = "Kamu sering mencari inspirasi liburan di aplikasi MyBank pada akhir pekan."
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Sub-komponen estetik untuk menggabungkan teks tebal & tipis dalam 1 baris
@Composable
fun XaiPointItem(title: String, description: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = OnyxMain)) {
                append(title)
                append(" ")
            }
            withStyle(style = SpanStyle(color = OnyxMain)) {
                append(description)
            }
        },
        style = MaterialTheme.typography.bodySmall,
        lineHeight = 18.sp
    )
}

@Preview
@Composable
fun PromoPreview() {
    val navController = rememberNavController()
    MyBankTheme {
        PromoScreen(){
        }
    }
}