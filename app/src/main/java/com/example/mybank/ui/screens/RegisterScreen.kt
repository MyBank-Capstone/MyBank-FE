package com.example.mybank.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import com.example.mybank.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybank.ui.components.*
import com.example.mybank.ui.theme.*
import com.example.mybank.ui.viewmodels.AuthState
import com.example.mybank.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalAnimationApi::class) // Dibutuhkan untuk animasi togetherWith
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    navController: NavController,
    viewModel: AuthViewModel,

) {
    // STATE UNTUK MULTI-STEP FORM
    var currentStep by remember { mutableStateOf(1) }
1
    // STATE DATA REGISTRASI
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isTermsChecked by remember { mutableStateOf(false) }
    var showTnCDialog by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val authState by viewModel.authState.collectAsState()

    // Reaksi UI - API Response
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                android.widget.Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", android.widget.Toast.LENGTH_LONG).show()
                viewModel.resetState()
                onRegisterSuccess() // Pindah ke halaman login
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                android.widget.Toast.makeText(context, errorMessage, android.widget.Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> { }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    if (currentStep > 1) {
                        currentStep-- // Kalau di step 2/3, mundur 1 langkah
                    } else {
                        onNavigateToLogin() // Kalau di step 1, kembali ke halaman Login
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
//                Icon(
//                    imageVector = Icon.Default.ArrowBack,
//                    contentDescription = "Kembali",
//                    tint = RedMain
//                )
            }

            Text(
                text = "Daftar",
                style = MaterialTheme.typography.titleLarge,
                color = RedMain,
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = { /* Aksi klik CS */ },
                modifier = Modifier.align(Alignment.CenterEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = RedMain
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cs),
                    contentDescription = "Pusat Bantuan",
                    tint = PureWhite
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_logo_red_main),
            contentDescription = "Logo Mybank",
            modifier = Modifier
                .fillMaxWidth()
        )

//        Spacer(modifier = Modifier.weight(1.2f))
        Spacer(modifier = Modifier.height(24.dp))

        // Indikator Langkah (Opsional tapi bagus untuk UX)
        Text(
            text = "Langkah $currentStep dari 3",
            style = MaterialTheme.typography.labelMedium,
            color = SubtleText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (currentStep) {
            1 -> {
                // Register langkah 1
                Text(
                    text = "Masukan Data Diri" ,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RedMain, // Label merah
                )

                MyBankTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nama Lengkap",
                    isRedMode = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                MyBankTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it},
                    label = "Tanggal Lahir",
                    isRedMode = false
                )
            }

            2 -> {
                // Register langkah 2
                Text(
                    text = "Informasi Kontak" ,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RedMain, // Label merah
                )

                MyBankTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nomor HP",
                    isRedMode = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                MyBankTextField(
                    value = dateOfBirth,
                    onValueChange = { dateOfBirth = it},
                    label = "Email",
                    isRedMode = false
                )
            }

            3 -> {
                // Register langkah 3
                Text(
                    text = "Set Keamanan" ,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RedMain, // Label merah
                )

                MyBankTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Buat Password",
                    isRedMode = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                MyBankTextField(
                    value = password,
                    onValueChange = { password = it},
                    label = "Konfirmasi Password",
                    isRedMode = false
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Checkbox(
                        checked = isTermsChecked,
                        onCheckedChange = { isTermsChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = RedMain,
                            uncheckedColor = SubtleText
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saya setuju dengan ",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnyxMain // Teks biasa gelap
                    )
                    Text(
                        text = "syarat dan ketentuan",
                        style = MaterialTheme.typography.labelSmall, // Teks bold
                        color = RedMain, // Teks merah
                        modifier = Modifier.clickable {
                            showTnCDialog = true
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saya sudah punya ID",
                style = MaterialTheme.typography.bodySmall,
                color = OnyxMain // Karena background putih, teks ini harus gelap
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.labelSmall,
                color = RedMain,
                modifier = Modifier.clickable {
                    onNavigateToLogin()
                }
            )
        }

        // --- 6. TOMBOL DAFTAR ---
        Button(
            onClick = {
                if(currentStep < 3) {
                    currentStep++
                } else {
                    if (password == confirmPassword) {
                        // TODO: panggil viewModel.register(name, dateOfBirth, ...)
                    }
                }
            },
            enabled = if (currentStep == 3) isTermsChecked else true, // TnC hanya di step 3

            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(bottom = 8.dp), // Jarak aman bawah
            colors = ButtonDefaults.buttonColors(
                containerColor = RedMain, // Background merah
                contentColor = PureWhite // Teks putih
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(if (currentStep < 3) "LANJUT" else "DAFTAR",
                style = MaterialTheme.typography.labelLarge,
                color = PureWhite
            )
        }
    }

    MyBankDialog(
        showDialog = showTnCDialog,
        onDismiss = { showTnCDialog = false },
        title = "Syarat dan Ketentuan"
    ) {
        // Konten yang bisa di-scroll
        Column(
            modifier = Modifier
                .weight(1f, fill = false) // fill=false agar tidak memaksa full screen jika teksnya sedikit
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.terms_and_conditions),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol di luar area scroll agar selalu terlihat (sticky di bawah)
        Button(
            onClick = { showTnCDialog = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Saya Mengerti")
        }
    }
}

//@Preview
//@Composable
//fun RegisterPreview() {
//    val navController = rememberNavController()
//    MyBankTheme {
//        RegisterScreen
//    }
//}