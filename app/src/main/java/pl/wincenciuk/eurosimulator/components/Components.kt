package pl.wincenciuk.eurosimulator.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val background_color = Color(0xFF104977)
val background_color2 = Color(0xFF00C099)
val green_check = Color(0xFF0F7012)
val little_green_check = Color(0xFFAEFFB0)
val txtfld_backg = Color(0xFFA09D9D)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}

@Composable
fun ScoreInputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
    onScoreChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it
            onScoreChanged(it)
                        },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        textStyle = TextStyle(
            fontSize = 22.sp,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier
            .size(width = 60.dp, height = 70.dp),
//            .padding(bottom = 3.dp, start = 3.dp, end = 3.dp),
//            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = txtfld_backg)
    )
}

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {

    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = { PassswordVisibility(passwordVisibility = passwordVisibility) },
        keyboardActions = onAction
    )
}

@Composable
fun PassswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        Icons.Default.Close
    }
}

@Composable
fun ScoreInput(
    modifier: Modifier = Modifier,
    scoreState: MutableState<String>,
    labelId: String = "",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    onScoreChanged: (String) -> Unit
) {
    ScoreInputField(
        modifier = modifier,
        valueState = scoreState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Number,
        imeAction = imeAction,
        onAction = onAction,
        onScoreChanged = onScoreChanged
    )
}

@Composable
fun PoInputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        singleLine = isSingleLine,
        textStyle = TextStyle(
        color = MaterialTheme.colors.onBackground, fontSize = 17.sp, textAlign = TextAlign.Center
        ),
        modifier = modifier,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        decorationBox = { innerTextField ->  
            Row(modifier = Modifier
                .border(width = 1.dp, color = background_color)
                .size(width = 23.dp, height = 26.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                innerTextField()
            }
        }
    )
}

@Composable
fun EuroLoader() {
    CircularProgressIndicator(
        modifier = Modifier.width(65.dp).padding(top = 200.dp),
        color = Color.Gray,
        strokeWidth = 6.dp)
    Text(text = "Loading...", modifier = Modifier.padding(top = 60.dp), color = Color.White, fontSize = 22.sp)
    Text(text = "Select your new European champion!", modifier = Modifier.padding(top = 15.dp), color = Color.LightGray, fontSize = 17.sp)
}