package com.example.mybank.ui.screens

import android.R.attr.text
import androidx.compose.foundation.Image
import com.example.mybank.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.mybank.ui.components.*
import com.example.mybank.ui.theme.*

@Composable
fun RegisterScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isTermsChecked by remember { mutableStateOf(false) } // State untuk Checkbox

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
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

        Spacer(modifier = Modifier.weight(1.2f))

        Text(
            text = "Masukkan email",
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium,
            color = RedMain, // Label merah
        )

        MyBankTextField(
            value = email,
            onValueChange = { email = it },
            label = "Masukkan email",
            isRedMode = false // Warna otomatis garis abu & teks gelap
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Buat password",
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium,
            color = RedMain,
        )

        MyBankTextField(
            value = password,
            onValueChange = { password = it },
            label = "Buat password",
            isPassword = true,
            isRedMode = false // Warna otomatis garis abu & teks gelap
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Konfirmasi Password",
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium,
            color = RedMain,
        )

        MyBankTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Konfirmasi password",
            isPassword = true,
            isRedMode = false
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Posisi di tengah sesuai desain
        ) {
            Checkbox(
                checked = isTermsChecked,
                onCheckedChange = { isTermsChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = RedMain,
                    uncheckedColor = SubtleText
                ),
                modifier = Modifier.size(20.dp) // Ukuran checkbox disesuaikan
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
                    // TODO: Tampilkan Pop-up Syarat dan Ketentuan
                }
            )
        }

        // --- SPACER DINAMIS 3 ---
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
                color = RedMain, // Aksen merah untuk tautan
                modifier = Modifier.clickable {
                    // TODO: Aksi navigasi kembali ke LoginScreen
                }
            )
        }

        // --- 6. TOMBOL DAFTAR ---
        Button(
            onClick = { /* Aksi Daftar */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 8.dp), // Jarak aman bawah
            colors = ButtonDefaults.buttonColors(
                containerColor = RedMain, // Background merah
                contentColor = PureWhite // Teks putih
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = "DAFTAR",
                style = MaterialTheme.typography.labelLarge,
                color = PureWhite
            )
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    MyBankTheme {
        RegisterScreen()
    }
}