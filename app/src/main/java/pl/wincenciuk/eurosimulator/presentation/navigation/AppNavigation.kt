package pl.wincenciuk.eurosimulator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pl.wincenciuk.eurosimulator.presentation.screens.GroupStageScreen
import pl.wincenciuk.eurosimulator.presentation.screens.LoginScreen
import pl.wincenciuk.eurosimulator.presentation.screens.PlayoffScreen
import pl.wincenciuk.eurosimulator.presentation.screens.SplashScreen
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: EuroViewModel = viewModel()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name ){
        composable(AppScreens.SplashScreen.name){
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.name){
            LoginScreen(navController, viewModel)
        }
        composable(AppScreens.GroupStageScreen.name){
            GroupStageScreen(viewModel = viewModel, navController)
        }
        composable(AppScreens.PlayoffScreen.name){
            PlayoffScreen(viewModel = viewModel)
        }
    }
}