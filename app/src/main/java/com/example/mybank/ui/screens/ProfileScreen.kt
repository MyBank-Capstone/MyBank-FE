package com.example.mybank.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.mybank.R
import com.example.mybank.ui.components.MyBankNavbar
import com.example.mybank.ui.theme.*
import com.example.mybank.ui.viewmodels.HomeViewModel
import com.example.mybank.ui.viewmodels.PersonalizationViewModel

@Composable
fun ProfileScreen(
    // Parameter navigasi bisa ditambahkan nanti di sini
    navController: NavController,
    personalizationViewModel: PersonalizationViewModel,

) {
    // 1. KONTROL STATUS BAR: Memastikan teks status bar berwarna gelap di atas background putih
    val view = LocalView.current
    val isAiActive by personalizationViewModel.isAiActive.collectAsState()

    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    // KUNCI POIN 3: Cegah lompat ke login, paksa kembali ke Home
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Buka QRIS */ },
                shape = CircleShape,
                containerColor = Maroon,
                contentColor = PureWhite,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp), // 1. Tambah Shadow
                modifier = Modifier
                    .size(64.dp)
                    .offset(y = 64.dp)
                    .border(width = 4.dp, color = PureWhite, shape = CircleShape) // 2. Tambah Border Putih
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qris), // Pastikan icon ada
                    contentDescription = "QRIS",
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            MyBankNavbar(
                currentScreen = "profile",
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PureWhite)
                .padding(innerPadding)
                // Menambahkan verticalScroll agar aman jika layar HP pendek
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // --- HEADER JUDUL ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Profil",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Maroon
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BAGIAN 1: KARTU IDENTITAS USER ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Foto Profil (Gunakan placeholder gambar profilmu di drawable)
                    Image(
                        painter = painterResource(id = R.drawable.img_placeholder_profile),
                        contentDescription = "Foto Profil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "ANDI SAPUTRA",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnyxMain
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "02120 01020 012001", // Nomor rekening statis
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnyxMain
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BAGIAN 2: KELOMPOK PENGATURAN ---
            Text(
                text = "Pengaturan",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Maroon
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Menu Profile
                    SettingsMenuItem(
                        title = "Profile",
                        subtitle = "Kelola nomor HP, email, dan alamat domisili Anda.",
                        onClick = { /* Implementasi klik nanti */ }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    // Menu Kelola Rekening
                    SettingsMenuItem(
                        title = "Kelola Rekening",
                        subtitle = "Atur rekening utama dan limit transaksi harian.",
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    // Menu Notifikasi
                    SettingsMenuItem(
                        title = "Notifikasi",
                        subtitle = "Atur pemberitahuan untuk transaksi dan promo terbaru.",
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    // Menu Bahasa
                    SettingsMenuItem(
                        title = "Bahasa",
                        subtitle = "Bahasa Indonesia (ID)",
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    // Menu Khusus: Personalisasi AI (Menggunakan Switch)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                personalizationViewModel.updatePersonalizationStatus(!isAiActive)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Personalisasi AI",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnyxMain
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Izinkan AI mempelajari kebiasaan transaksi untuk rekomendasi promo dan menu yang relevan.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = SubtleText,
                                lineHeight = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // SWITCH ON/OFF PERSONALISASI
                        Switch(
                            checked = isAiActive,
                            onCheckedChange = { newValue ->
                                personalizationViewModel.updatePersonalizationStatus(newValue)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PureWhite,
                                checkedTrackColor = RedMain,
                                uncheckedThumbColor = SubtleText,
                                uncheckedTrackColor = SubtleBackground
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BAGIAN 3: KELOMPOK KEAMANAN ---
            Text(
                text = "Keamanan",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Maroon
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsMenuItem(
                        title = "Ubah PIN Transaksi",
                        subtitle = "Perbarui PIN 6 digit Anda secara berkala.",
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    SettingsMenuItem(
                        title = "Ubah Kata Sandi",
                        subtitle = "Ganti kata sandi login aplikasi Anda.",
                        onClick = {}
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = DividerDefaults.Thickness,
                        color = SubtleBackground
                    )

                    SettingsMenuItem(
                        title = "Perangkat Terhubung",
                        subtitle = "Kelola HP/perangkat yang memiliki akses ke akun ini.",
                        onClick = {}
                    )
                }
            }

            // Ekstra ruang di bawah agar item terbawah tidak tertutup bayangan Navbar/QRIS
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// --- KOMPONEN REUSABLE: Item Baris Menu Pengaturan ---
@Composable
fun SettingsMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = OnyxMain
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = SubtleText,
            lineHeight = 14.sp
        )
    }
}

//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    MyBankTheme {
////        ProfileScreen()
//    }
//}