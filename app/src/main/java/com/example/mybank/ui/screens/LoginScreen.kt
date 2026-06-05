package com.example.mybank.ui.screens

import androidx.compose.foundation.Image
import com.example.mybank.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybank.ui.components.MyBankTextField
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mybank.ui.theme.*
import com.example.mybank.ui.viewmodels.AuthState
import com.example.mybank.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    navController: NavController,
    // Panggil ViewModel ke dalam layar
    viewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    // Pantau (observe) perubahan status dari ViewModel
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Reaksi UI berdasarkan status
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                viewModel.resetState()

                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RedMain)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge,
                color = PureWhite,
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = { /* Aksi klik CS */ },
                modifier = Modifier.align(Alignment.CenterEnd),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = PureWhite
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cs),
                    contentDescription = "Pusat Bantuan",
                    tint = RedMain
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_logo_white_main),
            contentDescription = "Logo Mybank",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1.2f))

        Text(
            text = "Email MyBank",
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp), // Tambah sedikit jarak ke textfield
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium,
            color = PureWhite,
        )

        MyBankTextField(
            value = email,
            onValueChange = { email = it },
            label = "Masukkan Email", // Label dalam bisa diubah karena sudah ada label luar
            isRedMode = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Password",
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium,
            color = PureWhite,
        )

        MyBankTextField(
            value = password,
            onValueChange = { password = it },
            label = "Masukkan Password",
            isPassword = true,
            isRedMode = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp), // Jarak dari input password ke teks ini
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Perlu bantuan dengan login?",
                style = MaterialTheme.typography.bodySmall,
                color = PureWhite
            )
            Text(
                text = "Lupa password",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                modifier = Modifier.clickable {
                    // TODO: Aksi navigasi ke halaman Lupa Password
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- TAMBAHAN 2: Baris Daftar Disini ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Jarak dari teks ini ke tombol Login di bawahnya
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saya belum punya ID MyBank",
                style = MaterialTheme.typography.bodySmall,
                color = PureWhite
            )
            Text(
                text = "Daftar disini",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = PureWhite,
                modifier = Modifier.clickable {
                    onNavigateToRegister()
                }
            )
        }

        // Button Login
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PureWhite,
                    contentColor = RedMain
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "LOGIN",
                    style = MaterialTheme.typography.labelLarge,
                    color = RedMain
                )
            }

            IconButton(
                onClick = { /* TODO Ini implement pake fingerprint, nanti lah */ },
                modifier = Modifier.size(56.dp).background(PureWhite, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fingerprint24dp),
                    contentDescription = "Login Sidik Jari",
                    tint = RedMain
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun LoginPreview() {
//    val navController = rememberNavController()
//    MyBankTheme {
//        LoginScreen(navController = navController, )
//    }
//}