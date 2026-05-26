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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybank.ui.viewmodels.AuthViewModel

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
                    val authViewModel: AuthViewModel = viewModel()

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
                        composable("login") {
                            LoginScreen(
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    // KUNCI: Pindah ke Home, lalu hancurkan rute Login dari memori
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onRegisterSuccess = {
                                    // Setelah register sukses, arahkan kembali ke login
                                    navController.popBackStack()
                                },
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }

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