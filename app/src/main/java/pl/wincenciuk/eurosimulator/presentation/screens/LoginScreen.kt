package pl.wincenciuk.eurosimulator.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.getViewModel
import pl.wincenciuk.eurosimulator.R
import pl.wincenciuk.eurosimulator.presentation.components.EmailInput
import pl.wincenciuk.eurosimulator.presentation.components.PasswordInput
import pl.wincenciuk.eurosimulator.presentation.components.background_color
import pl.wincenciuk.eurosimulator.presentation.navigation.AppScreens
import pl.wincenciuk.eurosimulator.presentation.viewmodel.EuroViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel = getViewModel<EuroViewModel>()
    val showLoginForm = remember { mutableStateOf(true) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background_color
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painterResource(id = R.drawable.euro_simulator_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .size(230.dp)
                    .padding(top = 20.dp)
            )

            if (showLoginForm.value) {
                UserForm(navController, loading = false, isCreatingAccount = false) { email, password ->
                    //Log in
                    viewModel.signInWithEmailAndPassword(email, password, context){
                        navController.navigate(AppScreens.GroupStageScreen.name)
                    }
                }
            } else {
                UserForm(navController, loading = false, isCreatingAccount = true) { email, password ->
                    //Create account
                   viewModel.createUserWithEmailAndPassword(email, password, context) {
                       navController.navigate(AppScreens.GroupStageScreen.name)
                   }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val firstText = if (showLoginForm.value) "New user?" else "Come back?"
                Text(text = firstText)
                val secondText = if (showLoginForm.value) "Sign up" else "Login"
                Text(
                    text = secondText,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(start = 10.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    navController: NavController,
    loading: Boolean = false,
    isCreatingAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, password -> }
) {
    val email = rememberSaveable() { mutableStateOf("") }
    val password = rememberSaveable() { mutableStateOf("") }
    val passwordVisibility = rememberSaveable() { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier.background(background_color)

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            modifier = Modifier
                .padding(20.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            shape = RoundedCornerShape(15.dp),
            color = Color.Gray,
            border = BorderStroke(3.dp, Color.White),
            elevation = 30.dp
        ) {
            Column {
                if (isCreatingAccount) Text(
                    text = "Please enter a valid email and password that is at least 6 characters",
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 19.sp
                )
                else Text(
                    text = "Welcome to our app! Please, log in to your existing account or create a new one",
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(top = 10.dp, bottom = 10.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 19.sp
                )

                Surface(modifier = Modifier.padding(top = 0.dp),
                    color = Color.White,
                ) {
                    Column() {
                        EmailInput(
                            emailState = email,
                            enabled = !loading,
                            onAction = KeyboardActions {
                                passwordFocusRequest.requestFocus()
                            })

                        PasswordInput(
                            modifier = Modifier.focusRequester(passwordFocusRequest),
                            passwordState = password,
                            labelId = "Password",
                            enabled = !loading,
                            passwordVisibility = passwordVisibility,
                            onAction = KeyboardActions {
                                if (!valid) return@KeyboardActions
                                onDone(email.value.trim(), password.value.trim())
                            })
                    }

                }

            }
        }

        SubmitButton(
            textId = if (isCreatingAccount) "Register" else "Login",
            loading = loading,
            validInputs = valid,
        ) {
            onDone(email.value.trim(), password.value.trim())
//            navController.navigate(AppScreens.GroupStageScreen.name)
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .size(width = 150.dp, height = 60.dp),
        enabled = !loading && validInputs,
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.White),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Gray,
            contentColor = Color.White,
            disabledBackgroundColor = Color.White,
            disabledContentColor = Color.LightGray
        )
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(
            text = textId,
            modifier = Modifier.padding(5.dp), fontSize = 21.sp
        )
    }
}