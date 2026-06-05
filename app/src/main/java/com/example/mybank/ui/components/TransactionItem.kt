package com.example.mybank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mybank.R
import com.example.mybank.data.models.Transaction
import com.example.mybank.ui.theme.*

@Composable
fun TransactionItem(transaction: Transaction, modifier: Modifier = Modifier) {
    // 1. Tentukan Uang Masuk / Keluar berdasarkan "type" dari Backend
    // Asumsi: TOP_UP atau TRANSFER_IN adalah uang masuk. (Bisa kamu sesuaikan nanti)
    val isIncome = transaction.type == "TOP_UP" || transaction.type == "TRANSFER_IN"

    // 2. Format Nominal (Handle Null)
    val amount = transaction.amount ?: 0.0
    val amountFormatted = "Rp" + String.format("%,.0f", amount).replace(',', '.')

    // 3. Prioritas Judul Transaksi (Karena dari BE ada banyak field nama)
    val title = transaction.merchantName
        ?: transaction.destinationName
        ?: transaction.description
        ?: "Transaksi"

    // 4. Tanggal
    val date = transaction.transactedAt ?: transaction.createdAt ?: "-"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ikon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (isIncome) Color(0xFFE8F5E9) else RedMain.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    id = if (isIncome) R.drawable.ic_arrow_down else R.drawable.ic_transfer
                ),
                contentDescription = null,
                tint = if (isIncome) Color(0xFF2E7D32) else RedMain,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Detail
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = OnyxMain,
                maxLines = 1 // Mencegah teks terlalu panjang merusak layout
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = SubtleText
            )
        }

        // Nominal
        Text(
            text = (if (isIncome) "+" else "-") + amountFormatted,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (isIncome) Color(0xFF2E7D32) else OnyxMain
        )
    }
}