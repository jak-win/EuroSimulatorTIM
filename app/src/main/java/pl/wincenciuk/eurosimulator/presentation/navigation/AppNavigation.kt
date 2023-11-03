package pl.wincenciuk.eurosimulator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.wincenciuk.eurosimulator.presentation.screens.GroupStageScreen
import pl.wincenciuk.eurosimulator.presentation.screens.LoginScreen
import pl.wincenciuk.eurosimulator.presentation.screens.PlayoffScreen
import pl.wincenciuk.eurosimulator.presentation.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name ){
        composable(AppScreens.SplashScreen.name){
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.name){
            LoginScreen(navController)
        }
        composable(AppScreens.GroupStageScreen.name){
            GroupStageScreen(navController)
        }
        composable(AppScreens.PlayoffScreen.name){
            PlayoffScreen()
        }
    }
}