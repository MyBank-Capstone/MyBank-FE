package com.example.mybank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mybank.ui.screens.HomeScreen
import com.example.mybank.ui.screens.LoginScreen
import com.example.mybank.ui.screens.PromoScreen
import com.example.mybank.ui.screens.RegisterScreen
import com.example.mybank.ui.theme.MyBankTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            MyBankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        // --- KONFIGURASI TRANSISI GLOBAL ---
                        // 1. Saat halaman BARU muncul (Maju)
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400) // Durasi 400ms agar smooth
                            )
                        },
                        // 2. Saat halaman LAMA menghilang (Maju)
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                        },
                        // 3. Saat halaman LAMA muncul kembali (Balik/Back)
                        popEnterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        },
                        // 4. Saat halaman BARU menghilang (Balik/Back)
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                        }
                    ) {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("home") {
                            HomeScreen(
                                onNavigateToPromo = {
                                    navController.navigate("promo") {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable("promo") {
                            PromoScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}