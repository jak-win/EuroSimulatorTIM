package pl.wincenciuk.eurosimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pl.wincenciuk.eurosimulator.presentation.screens.GroupStageScreen
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel
import pl.wincenciuk.eurosimulator.ui.theme.EuroSimulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EuroSimulatorTheme {
                // A surface container using the 'background' color from the theme
                GroupStageScreen(viewModel = EuroViewModel())
            }
        }
    }
}
