package com.example.mybank.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mybank.R
import com.example.mybank.ui.components.MyBankNavbar
import com.example.mybank.ui.theme.Maroon
import com.example.mybank.ui.theme.PureWhite

@Composable
fun NotificationScreen(navController: NavController) {
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
                currentScreen = "notifikasi", // Sesuaikan dengan route
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Layar Notifikasi",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}