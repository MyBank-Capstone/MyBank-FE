package com.example.mybank.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import com.example.mybank.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mybank.data.models.MenuFeature
import com.example.mybank.data.models.PromoList
import com.example.mybank.ui.components.*
import com.example.mybank.ui.theme.*
import com.example.mybank.ui.viewmodels.HomeViewModel
import com.example.mybank.ui.viewmodels.PersonalizationViewModel

// 1. Simulasi Data dari Backend / AI Recommendation Engine
// (Nanti ini diambil dari ViewModel)
val dynamicMenus = listOf(
    MenuFeature("1", "Top Up", R.drawable.ic_topup), // Sesuaikan id drawable-nya
    MenuFeature("2", "Transfer", R.drawable.ic_transfer),
    MenuFeature("3", "Tagihan", R.drawable.ic_bill),
    MenuFeature("4", "Investasi", R.drawable.ic_invest)
)
// Coba ganti jadi: val dynamicMenus = emptyList<MenuFeature>() untuk melihat tampilan "Pin Menu"

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
    MenuFeature("10", "Exchange", R.drawable.ic_exchange),
    MenuFeature("11", "Catatan", R.drawable.ic_notes),
    MenuFeature("12", "Donasi", R.drawable.ic_donation)
)

// Di dalam HomeScreen, tentukan mana yang masuk favorit (Misal hasil rekomendasi AI)
val favoriteIds = listOf("1", "2", "3", "4") // ID fitur yang tampil di atas
val favoriteMenus = allMyBankFeatures.filter { it.id in favoriteIds }
// Sisanya otomatis masuk ke "Menu Lainnya"
val otherMenus = allMyBankFeatures.filterNot { it.id in favoriteIds }

fun getPromoList(userName: String) = listOf(
    PromoList(
        id = "p1",
        title = "Terbangkan Penatmu, $userName!",
        description = "Tiket SAT Airways diskon up to 50%",
        hashtag = "#OnlyForYou",
        imageRes = R.drawable.img_flight_promo
    ),
    PromoList(
        id = "p2",
        title = "Waktunya Kopi Sore, $userName!",
        description = "Diskon spesial 30% di seluruh outlet",
        hashtag = "#FlashSale",
        imageRes = R.drawable.img_promo_coffee
    ),
    PromoList(
        id = "p3",
        title = "Burger Streak Spesial",
        description = "Beli 1 Gratis 1 khusus pengguna MyBank",
        hashtag = "#WeekendPromo",
        imageRes = R.drawable.img_flight_promo
    )
)

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    personalizationViewModel: PersonalizationViewModel,
    onNavigateToPromo: () -> Unit = {},
//    onNavigateToProfil: () -> Unit = {}
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        LaunchedEffect(Unit) {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
// NOTE [DEKLARASI STATE]
    val context = LocalContext.current
    val activity = (context as? Activity)
    var isBalanceVisible by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val showConsentDialog by personalizationViewModel.showConsentDialog.collectAsState()
    val isAiActive by personalizationViewModel.isAiActive.collectAsState()
    val userName by homeViewModel.userName.collectAsState()

    // 1. TENTUKAN MENU FAVORIT SECARA DINAMIS
    // Jika AI nyala, pakai dynamicMenus. Jika mati, kosongkan.
    val currentFavorites = if (isAiActive && dynamicMenus.isNotEmpty()) {
        dynamicMenus
    } else {
        emptyList()
    }

    // 2. TENTUKAN MENU LAINNYA SECARA DINAMIS
    // Saring semua fitur yang tidak ada di currentFavorites
    val currentOtherMenus = allMyBankFeatures.filterNot { feature ->
        currentFavorites.any { fav -> fav.id == feature.id }
    }

    var isMenuExpanded by remember { mutableStateOf(false) }
    // KUNCI: Gunakan currentOtherMenus, BUKAN otherMenus
    val displayedOtherMenus = if (isMenuExpanded) currentOtherMenus else currentOtherMenus.take(8)

    // Ambil nama terbaru dari SharedPreferences saat masuk ke Home
    LaunchedEffect(Unit) {
        homeViewModel.refreshName()
    }

    BackHandler(enabled = true) {
        showLogoutDialog = true
    }
//NOTE [NAVBAR]
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
                currentScreen = "home",
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    ) { innerPadding ->

//NOTE [CONTENT UTAMA HOMEPAGE]
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
                //1. SEARCH BAR & CS BUTTON
                var searchQuery by remember { mutableStateOf("") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

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
                            .padding(horizontal = 8.dp),
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

                // GREETING AREA
                Text("Selamat pagi,", style = MaterialTheme.typography.bodySmall, color = PureWhite)
                Text(userName, style = MaterialTheme.typography.titleMedium, color = PureWhite)

                Spacer(modifier = Modifier.height(24.dp))

                ResizableCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            shadowElevation = 16.dp.toPx()
                            shape = RoundedCornerShape(24.dp)
                            clip = false
                        },
                    containerColor = Maroon
                ) {
                    Column(
                        modifier = Modifier.padding(start = 0.dp, end = 16.dp, top = 0.dp, bottom = 8.dp)
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
                            // 1. Teks Saldo Dinamis
                            Text(
                                text = if (isBalanceVisible) "Rp12.500.000,00" else "Rp************",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                painter = painterResource(
                                    id = if (isBalanceVisible) R.drawable.ic_eye_on20dp else R.drawable.ic_eye_off20dp
                                ),
                                contentDescription = "Toggle Saldo",
                                tint = PureWhite,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable { isBalanceVisible = !isBalanceVisible }                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ResizableCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = PureWhite
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isAiActive && dynamicMenus.isNotEmpty()) "Sering Digunakan" else "Pin Menu",
                                style = MaterialTheme.typography.titleMedium,
                                color = OnyxMain
                            )

                            Surface(
                                color = RedMain.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.clickable {  }
                            ) {
                                Row (
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
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Maroon
                                    )
                                }
                            }
                        }

                        // KUNCI: Logika Tampilan Isi Card
                        if (isAiActive && dynamicMenus.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                dynamicMenus.forEach { menu ->
                                    FeatureItem(label = menu.title, iconRes = menu.iconRes)
                                }
                            }
                        } else {
                            // Tampilkan Teks Default (Personalisasi Off)
                            Text(
                                text = "Anda belum menambahkan menu favorit",
                                style = MaterialTheme.typography.bodySmall,
                                color = SubtleText,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

//NOTE [WHITE ZONE]: (Other menu, promos, etc.)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = PureWhite
            ) {
                // 1. KONTAINER LUAR: Hanya pakai padding vertikal atas-bawah.
                Column(modifier = Modifier.padding(vertical = 24.dp)) {
// TODO: Expanding Menu
                    // 2. KONTAINER DALAM: Membungkus Menu Lainnya & Header Promo.
                    // 2. KONTAINER DALAM: Membungkus Menu Lainnya & Header Promo.
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text("Menu Lainnya", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid Menu Lainnya
                        displayedOtherMenus.chunked(4).forEach { rowItems ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                rowItems.forEach { menu ->
                                    FeatureItem(label = menu.title, iconRes = menu.iconRes)
                                }
                                // Menjaga grid tetap rata kiri-kanan jika baris terakhir kurang dari 4 item
                                repeat(4 - rowItems.size) { Spacer(modifier = Modifier.width(72.dp)) }
                            }
                        }

                        // KUNCI PERBAIKAN: Gunakan currentOtherMenus.size
                        if (currentOtherMenus.size > 8) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isMenuExpanded = !isMenuExpanded }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isMenuExpanded) "Tampilkan Lebih Sedikit" else "Lihat Semua Menu",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = RedMain
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    painter = painterResource(
                                        id = if (isMenuExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                                    ),
                                    contentDescription = "Toggle Menu",
                                    tint = RedMain,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Header Promo
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Promo untuk Anda", style = MaterialTheme.typography.titleMedium)
                            Text("Lihat semua",
                                color = RedMain,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.clickable { onNavigateToPromo() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. HORIZONTAL SCROLL
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        items(getPromoList(userName)) { promo ->
                            MyBankPromoCard(
                                title = promo.title,
                                description = promo.description,
                                hashtag = promo.hashtag,
                                imageRes = promo.imageRes,
                                modifier = Modifier.width(280.dp).height(140.dp)
                            )
                        }
                    }

                    // Ruang kosong tambahan di dasar layar agar card promo tidak
                    // tertutup oleh bayangan FloatingActionButton (QRIS) atau Bottom Bar
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

//NOTE [LOGOUT POP UP DIALOG]
        MyBankDialog(
            showDialog = showLogoutDialog,
            onDismiss = { showLogoutDialog = false },
            title = "Logout"
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Apakah Anda yakin untuk keluar dari MyBank?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnyxMain
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Batal", color = SubtleText)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            showLogoutDialog = false
                            // KUNCI: Pindah ke login dan hancurkan riwayat navigasi (Backstack)
                            // Agar setelah di layar Login, pencet back tidak bisa masuk Home lagi
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RedMain)
                    ) {
                        Text("Keluar", color = PureWhite)
                    }
                }
            }
        }

        PersonalizationConsentDialog(
            showDialog = showConsentDialog,
            onAllow = {
                personalizationViewModel.updatePersonalizationStatus(true)
            },
            onDecline = {
                personalizationViewModel.updatePersonalizationStatus(false)
            }
        )
    }
}

//@Preview
//@Composable
//fun HomePreview() {
//    MyBankTheme {
//        HomeScreen()
//    }
//}