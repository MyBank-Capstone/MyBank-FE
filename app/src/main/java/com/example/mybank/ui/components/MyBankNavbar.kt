package com.example.mybank.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mybank.R // Sesuaikan dengan package kamu
import com.example.mybank.ui.theme.Maroon
import com.example.mybank.ui.theme.PureWhite
import com.example.mybank.ui.theme.RedMain
import com.example.mybank.ui.theme.SubtleText

@Composable
fun MyBankNavbar(
    currentScreen: String,
    onTabSelected: (String) -> Unit // Fungsi untuk menangani klik pindah halaman
) {
    BottomAppBar(
        containerColor = PureWhite,
        contentPadding = PaddingValues(horizontal = 24.dp),
        // Menambahkan sedikit bayangan agar terpisah dari background putih
//        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- SISI KIRI ---
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                BottomNavIcon(
                    label = "Beranda",
                    iconRes = R.drawable.ic_home, // Siapkan icon ini
                    isActive = currentScreen == "Beranda",
                    onClick = { onTabSelected("Beranda") }
                )
                BottomNavIcon(
                    label = "Mutasi",
                    iconRes = R.drawable.ic_history, // Siapkan icon ini
                    isActive = currentScreen == "Mutasi",
                    onClick = { onTabSelected("Mutasi") }
                )
            }

            // --- AREA KOSONG DI TENGAH UNTUK QRIS ---
            Spacer(modifier = Modifier.width(48.dp))

            // --- SISI KANAN ---
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                BottomNavIcon(
                    label = "Notifikasi",
                    iconRes = R.drawable.ic_notification, // Siapkan icon ini
                    isActive = currentScreen == "Notifikasi",
                    onClick = { onTabSelected("Notifikasi") }
                )
                BottomNavIcon(
                    label = "Profil",
                    iconRes = R.drawable.ic_profile, // Siapkan icon ini
                    isActive = currentScreen == "Profil",
                    onClick = { onTabSelected("Profil") }
                )
            }
        }
    }
}

// Sub-komponen kecil untuk merapikan kode ikon dan teks
@Composable
fun BottomNavIcon(
    label: String,
    iconRes: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val color = if (isActive) Maroon else SubtleText

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}