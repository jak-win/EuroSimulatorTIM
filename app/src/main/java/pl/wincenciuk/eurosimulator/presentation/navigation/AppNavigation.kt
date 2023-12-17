package pl.wincenciuk.eurosimulator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.getViewModel
import pl.wincenciuk.eurosimulator.presentation.screens.GroupStageScreen
import pl.wincenciuk.eurosimulator.presentation.screens.LoginScreen
import pl.wincenciuk.eurosimulator.presentation.screens.PlayoffScreen
import pl.wincenciuk.eurosimulator.presentation.screens.SplashScreen
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel = getViewModel<EuroViewModel>()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.name ){
        composable(AppScreens.SplashScreen.name){
            SplashScreen(navController)
        }
        composable(AppScreens.LoginScreen.name){
            LoginScreen(navController)
        }
        composable(AppScreens.GroupStageScreen.name){
            GroupStageScreen(viewModel, navController)
        }
        composable(AppScreens.PlayoffScreen.name){
            PlayoffScreen(viewModel)
        }
    }
}