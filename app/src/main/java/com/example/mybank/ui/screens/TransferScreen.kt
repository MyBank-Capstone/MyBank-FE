package com.example.mybank.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mybank.R
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.ui.theme.*
import com.example.mybank.ui.viewmodels.TransactionViewModel
import com.example.mybank.ui.viewmodels.TransferUiState
import com.example.mybank.data.models.TransactionRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    navController: NavController,
    initialType: String = "Transfer", // Default value, bisa dioper dari Home
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefsManager = remember { UserPreferencesManager(context) }
    val uiState by transactionViewModel.uiState.collectAsState()

    // State untuk form
    var selectedType by remember { mutableStateOf(initialType) }
    var expanded by remember { mutableStateOf(false) }
    val transactionTypes = listOf("Transfer Antar Bank", "Top Up E-Wallet", "Virtual Account")

    var destinationNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is TransferUiState.Success -> {
                Toast.makeText(context, "Transaksi Berhasil!", Toast.LENGTH_SHORT).show()
                transactionViewModel.resetState()

                // Hancurkan form dan kembali ke Home
                navController.popBackStack("home", inclusive = false)
            }
            is TransferUiState.Error -> {
                val errorMessage = (uiState as TransferUiState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                transactionViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kirim Uang", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(R.drawable.ic_back), contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite,
                    titleContentColor = OnyxMain
                )
            )
        },
        containerColor = PureWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Jenis Transaksi
            OutlinedTextField(
                value = initialType,
                onValueChange = {}, // Kosongkan karena tidak boleh diedit manual
                readOnly = true, // KUNCI: Membuatnya tidak bisa diketik
                label = { Text("Jenis Transaksi") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = OnyxMain,
                    disabledBorderColor = SubtleText
                ),
                enabled = false, // Membuatnya terlihat seperti elemen yang dikunci
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Input Nomor Tujuan
            OutlinedTextField(
                value = destinationNumber,
                onValueChange = { destinationNumber = it },
                label = { Text("Nomor Rekening / HP / VA") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Input Nominal
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Nominal (Rp)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Input Catatan (Opsional)
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Catatan (Opsional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Tombol Lanjut
            Button(
                onClick = {
                    // Konversi tipe transaksi dari bahasa manusia ke kode BE
                    val backendType = when {
                        initialType.contains("Top Up", ignoreCase = true) -> "TOPUP"
                        initialType.contains("Tagihan", ignoreCase = true) -> "PAYMENT"
                        initialType.contains("Tarik Tunai", ignoreCase = true) -> "WITHDRAW"
                        else -> "TRANSFER" // Default jika tidak ada yang cocok
                    }

                    val formattedDestination = if (destinationNumber.startsWith("0")) {
                        "+62" + destinationNumber.substring(1)
                    } else {
                        destinationNumber
                    }

                    // Rakit JSON Request
                    val request = TransactionRequest(
                        accountId = prefsManager.userId,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        type = backendType,
                        channel = "mobile",
                        destinationAccountNumber = destinationNumber,
                        destinationBankCode = "BCA", // Hardcode sementara
                        destinationName = "User $destinationNumber", // Hardcode sementara
                        description = initialType,
                        note = note,
                        merchantCategory = "-",
                        merchantLocation = "-",
                        merchantName = "-",
                        pin = "123456" // Hardcode sementara
                    )

                    transactionViewModel.submitTransaction(request)
                },
                // Matikan tombol saat loading agar tidak terklik 2x
                enabled = uiState !is TransferUiState.Loading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RedMain)
            ) {
                if (uiState is TransferUiState.Loading) {
                    CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(24.dp))
                } else {
                    Text("Lanjutkan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}