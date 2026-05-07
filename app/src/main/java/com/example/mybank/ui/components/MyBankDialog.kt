package com.example.mybank.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mybank.ui.screens.RegisterScreen
import com.example.mybank.ui.theme.MyBankTheme
import com.example.mybank.ui.theme.OnyxMain
import com.example.mybank.ui.theme.PureWhite

@Composable
fun MyBankDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    // Slot API: Membiarkan konten diisi dari luar
    content:
    @Composable ColumnScope.() -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            // Mematikan lebar default bawaan Android agar kita bisa atur margin sendiri
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), // Jarak aman dari tepi layar HP
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Header opsional (hanya muncul jika title diisi)
                    if (title != null) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = OnyxMain
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    content()
                }
            }
        }
    }
}