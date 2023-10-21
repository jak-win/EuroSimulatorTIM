package pl.wincenciuk.eurosimulator.presentation.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.components.background_color
import pl.wincenciuk.eurosimulator.presentation.navigation.AppScreens

@Composable
fun SplashScreen(navController: NavController) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit){
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                OvershootInterpolator(8f).getInterpolation(it)
            }))
        delay(2000L)
        navController.navigate(AppScreens.LoginScreen.name)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background_color
    ) {
            Column(
                modifier = Modifier
                    .padding(1.dp)
                    .scale(scale.value),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painterResource(id = R.drawable.euro_simulator_logo),
                    contentDescription = "App logo"
                )
        }
    }
}