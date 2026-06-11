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
import com.example.mybank.ui.screens.PromoDetailScreen
import com.example.mybank.ui.screens.PromoScreen
import com.example.mybank.ui.screens.RegisterScreen
import com.example.mybank.ui.theme.MyBankTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mybank.data.models.Transaction
import com.example.mybank.ui.screens.EditProfileScreen
import com.example.mybank.ui.screens.HistoryScreen
import com.example.mybank.ui.screens.NotificationScreen
import com.example.mybank.ui.screens.ProfileScreen
import com.example.mybank.ui.screens.TransferScreen
import com.example.mybank.ui.viewmodels.AuthViewModel
import com.example.mybank.ui.viewmodels.HomeViewModel
import com.example.mybank.ui.viewmodels.PersonalizationViewModel
import com.example.mybank.ui.viewmodels.RecommendationViewModel
import com.example.mybank.ui.viewmodels.TransactionViewModel
import com.example.mybank.ui.viewmodels.UserViewModel

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
                    val homeViewModel: HomeViewModel = viewModel()
                    val personalizationViewModel: PersonalizationViewModel = viewModel()
                    val recommendationViewModel: RecommendationViewModel = viewModel()
                    val transactionViewModel: TransactionViewModel = viewModel()
                    val userViewModel: UserViewModel = viewModel()

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
                                navController = navController,
                                homeViewModel = homeViewModel,
                                personalizationViewModel = personalizationViewModel,
                                recommendationViewModel = recommendationViewModel,
                                onNavigateToPromo = {
                                    navController.navigate("promo") {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable("promo") {
                            PromoScreen(
                                navController = navController,
                                recommendationViewModel = recommendationViewModel,
                                personalizationViewModel = personalizationViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = "promo_detail/{promoId}",
                            arguments = listOf(navArgument("promoId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val promoId = backStackEntry.arguments?.getInt("promoId") ?: 0
                            PromoDetailScreen(
                                navController = navController,
                                promoId = promoId,
                                recommendationViewModel = recommendationViewModel
                            )
                        }

                        composable("mutasi") {
                            HistoryScreen(
                                navController = navController,
                                transactionViewModel = transactionViewModel
                            )
                        }

                        composable("notifikasi") {
                            NotificationScreen(navController = navController)
                        }

                        composable("profile") {
                            ProfileScreen(
                                navController = navController,
                                personalizationViewModel = personalizationViewModel,
                                userViewModel = userViewModel
                            )
                        }

                        composable("edit_profile") {
                            EditProfileScreen(
                                navController = navController,
                                userViewModel = userViewModel
                            )
                        }

                        composable(
                            route = "transfer/{transactionType}", // Tambahkan penangkap variabel
                            arguments = listOf(navArgument("transactionType") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Tangkap nilainya (default ke "Transfer" jika kosong)
                            val type = backStackEntry.arguments?.getString("transactionType") ?: "Transfer"

                            // Lempar nilainya ke layar TransferScreen
                            TransferScreen(
                                navController = navController,
                                initialType = type
                            )
                        }
                    }
                }
            }
        }
    }
}