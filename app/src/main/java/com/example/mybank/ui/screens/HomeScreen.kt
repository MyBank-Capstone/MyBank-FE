package com.example.mybank.ui.screens

import com.example.mybank.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.mybank.models.MenuFeature
import com.example.mybank.ui.components.FeatureItem
import com.example.mybank.ui.components.MyBankNavbar
import com.example.mybank.ui.components.ResizableCard
import com.example.mybank.ui.theme.Maroon
import com.example.mybank.ui.theme.MyBankTheme
import com.example.mybank.ui.theme.OnyxMain
import com.example.mybank.ui.theme.PureWhite
import com.example.mybank.ui.theme.RedMain
import com.example.mybank.ui.theme.SubtleText

// 1. Simulasi Data dari Backend / AI Recommendation Engine
// (Nanti ini diambil dari ViewModel)
val dynamicMenus = listOf(
    MenuFeature("1", "Top Up", R.drawable.ic_topup), // Sesuaikan id drawable-nya
    MenuFeature("2", "Transfer", R.drawable.ic_transfer),
    MenuFeature("3", "Tagihan", R.drawable.ic_bill),
    MenuFeature("4", "Investasi", R.drawable.ic_invest)
)
// Coba ganti jadi: val dynamicMenus = emptyList<MenuFeature>() untuk melihat tampilan "Pin Menu"

// 1. Definisikan semua fitur yang ada di aplikasi MyBank
val allMyBankFeatures = listOf(
    MenuFeature("1", "Top Up", R.drawable.ic_topup),
    MenuFeature("2", "Transfer", R.drawable.ic_transfer),
    MenuFeature("3", "Tagihan", R.drawable.ic_bill),
    MenuFeature("4", "Investasi", R.drawable.ic_invest),
    MenuFeature("5", "Kartu Kredit", R.drawable.ic_credit_card),
    MenuFeature("6", "Lifestyle", R.drawable.ic_lifestyle),
    MenuFeature("7", "Tiket", R.drawable.ic_ticket),
    MenuFeature("8", "Tarik Tunai", R.drawable.ic_cash_out),
    MenuFeature("9", "Voucher", R.drawable.ic_voucher),
    MenuFeature("10", "Exchange", R.drawable.ic_invest),
    MenuFeature("11", "Catatan", R.drawable.ic_notes),
    MenuFeature("12", "Donasi", R.drawable.ic_donation)
)

// 2. Di dalam HomeScreen, tentukan mana yang masuk favorit (Misal hasil rekomendasi AI)
val favoriteIds = listOf("1", "2", "3", "4") // ID fitur yang tampil di atas
val favoriteMenus = allMyBankFeatures.filter { it.id in favoriteIds }
// 3. Sisanya otomatis masuk ke "Menu Lainnya"
val otherMenus = allMyBankFeatures.filterNot { it.id in favoriteIds }

@Composable
fun HomeScreen() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Buka QRIS */ },
                shape = CircleShape,
                containerColor = Maroon,
                contentColor = PureWhite,
                // KUNCI: Gunakan offset Y untuk menurunkannya sedikit agar memotong bar
                modifier = Modifier.size(64.dp).offset(y = 50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qris),
                    contentDescription = "QRIS",
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            MyBankNavbar(currentScreen = "Beranda", onTabSelected = {})
        }
// Content Utama
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(RedMain)
//                .systemBarsPadding()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header, Saldo, masuk ke background merah
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // --- 1. SEARCH BAR & CS BUTTON ---
                var searchQuery by remember { mutableStateOf("") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search Bar Custom (Figma-Perfect)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp)
                            .background(PureWhite, CircleShape)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search), // Pastikan ada icon search merah
                            contentDescription = "Search",
                            tint = RedMain,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Cari fitur atau transaksi",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 12.sp,
                                    color = SubtleText
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                textStyle = MaterialTheme.typography.bodySmall.copy(color = OnyxMain),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Tombol Pusat Bantuan (CS)
                    Row(
                        modifier = Modifier
                            .height(32.dp)
                            .background(Maroon, CircleShape)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cs),
                            contentDescription = "CS",
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Pusat", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = PureWhite, lineHeight = 12.sp)
                            Text("Bantuan", style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp), color = PureWhite, lineHeight = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Greeting
                Text("Selamat pagi,", style = MaterialTheme.typography.bodySmall, color = PureWhite)
                Text("Andi Saputra", style = MaterialTheme.typography.titleMedium, color = PureWhite)

                Spacer(modifier = Modifier.height(24.dp))

                ResizableCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Maroon
                ) {
                    Column(
                        // Padding atas (top) dibuat lebih kecil dari padding bawah (bottom)
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
                    ) {
                        Text(
                            text = "Rekening Utama",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureWhite.copy(alpha = 0.8f)
                        )

                        // Jarak tipis agar terkesan mengelompok
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rp12.500.000,00",
                                style = MaterialTheme.typography.titleLarge,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_eye_on20dp), // Ganti dengan nama ikon matamu
                                contentDescription = "Toggle Saldo",
                                tint = PureWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ResizableCard(
                    modifier = Modifier
                        .height(136.dp)
                        .fillMaxWidth(),
                    containerColor = PureWhite
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (dynamicMenus.isNotEmpty()) "Sering digunakan" else "Pin Menu",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnyxMain
                        )

                        Surface(
                            color = RedMain.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.clickable {  }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = "Atur Menu",
                                    tint = Maroon,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Atur Menu",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Normal,
                                    color = OnyxMain
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = PureWhite
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Menu Lainnya", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    otherMenus.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { menu ->
                                FeatureItem(label = menu.title, iconRes = menu.iconRes)
                            }
                            repeat(4 - rowItems.size) { Spacer(modifier = Modifier.width(72.dp)) }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Promo untuk Anda", style = MaterialTheme.typography.titleMedium)
                        Text("Lihat semua", color = RedMain, style = MaterialTheme.typography.labelSmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Horizontal Scroll
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(end = 24.dp)
                    ) {
                        items(5) {

                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    MyBankTheme {
        HomeScreen()
    }
}