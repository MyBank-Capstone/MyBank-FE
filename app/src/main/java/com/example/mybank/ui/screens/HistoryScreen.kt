package com.example.mybank.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mybank.R
import com.example.mybank.data.models.Transaction
import com.example.mybank.ui.components.MyBankNavbar
import com.example.mybank.ui.components.TransactionItem
import androidx.compose.runtime.getValue
import com.example.mybank.ui.theme.Maroon
import com.example.mybank.ui.theme.OnyxMain
import com.example.mybank.ui.theme.PureWhite
import com.example.mybank.ui.theme.RedMain
import com.example.mybank.ui.theme.SubtleBackground
import com.example.mybank.ui.theme.SubtleText
import com.example.mybank.ui.viewmodels.HistoryUiState
import com.example.mybank.ui.viewmodels.TransactionViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    // KUNCI POIN 3: Cegah lompat ke login, paksa kembali ke Home
    BackHandler {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    val transferState by transactionViewModel.transferState.collectAsState()
    val uiState by transactionViewModel.historyState.collectAsState()

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
                currentScreen = "mutasi", // Sesuaikan dengan route
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Riwayat Transaksi",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = OnyxMain
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pantau semua aktivitas keuanganmu di sini",
                style = MaterialTheme.typography.bodyMedium,
                color = SubtleText
            )
            Spacer(modifier = Modifier.height(24.dp))

            // KUNCI: Menangani State dari API
            Box(modifier = Modifier.fillMaxSize()) {
                when (val state = uiState) {
                    is HistoryUiState.Loading -> {
                        CircularProgressIndicator(
                            color = RedMain,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is HistoryUiState.Error -> {
                        Text(
                            text = state.message,
                            color = RedMain,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is HistoryUiState.Empty -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_notes), // Pakai icon yg ada
                                contentDescription = null,
                                tint = SubtleText,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Belum ada transaksi bulan ini.", color = SubtleText)
                        }
                    }
                    is HistoryUiState.Success -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.transactions) { transaction ->
                                TransactionItem(transaction = transaction)
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    thickness = 1.dp,
                                    color = SubtleBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}