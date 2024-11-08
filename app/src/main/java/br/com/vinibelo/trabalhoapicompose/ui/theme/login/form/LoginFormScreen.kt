package br.com.vinibelo.trabalhoapicompose.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.ui.theme.TrabalhoApiComposeTheme
import br.com.vinibelo.trabalhoapicompose.ui.theme.login.form.LoginFormViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginFormScreen(
    modifier: Modifier = Modifier,
    loginFormViewModel: LoginFormViewModel = viewModel(),
    onUserAuthenticated: () -> Unit
) {
    LaunchedEffect(loginFormViewModel.state.isAuthenticated) {
        if (loginFormViewModel.state.isAuthenticated) {
            onUserAuthenticated()
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) { paddingValues ->
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 36.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Login",
                    fontSize = 40.sp
                )
            }
            val loginByPhoneModifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp
            )
            PhoneNumberForm(
                modifier = loginByPhoneModifier,
                phoneNumber = loginFormViewModel.state.phoneNumber,
                onPhoneNumberChange = loginFormViewModel::onPhoneNumberChange,
                onSendPressed = { loginFormViewModel.sendVerificationCode(context) }
            )
            if (loginFormViewModel.state.sentValidationCode) {
                CodeForm(
                    modifier = loginByPhoneModifier,
                    verificationCode = loginFormViewModel.state.verificationCode,
                    onVerificationCodeChanged = loginFormViewModel::onVerificationCodeChange,
                    onValidatePressed = { loginFormViewModel.validateCode(context) }
                )
            }
        }
    }
}

@Composable
fun PhoneNumberForm(
    modifier: Modifier = Modifier,
    onPhoneNumberChange: (String) -> Unit,
    phoneNumber: String,
    onSendPressed: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        TextFormField(
            modifier = Modifier,
            title = stringResource(R.string.phone_number),
            value = phoneNumber,
            onValueChanged = onPhoneNumberChange
        )
        TextButton(
            modifier = Modifier,
            text = stringResource(R.string.send),
            onButtonClick = onSendPressed
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhoneNumberFormPreview() {
    TrabalhoApiComposeTheme {
        PhoneNumberForm(
            modifier = Modifier,
            phoneNumber = "+55 11 912345678",
            onPhoneNumberChange = {},
            onSendPressed = {}
        )
    }
}


@Composable
fun CodeForm(
    modifier: Modifier = Modifier,
    verificationCode: String,
    onVerificationCodeChanged: (String) -> Unit,
    onValidatePressed: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        TextFormField(
            modifier = Modifier,
            title = stringResource(R.string.code),
            value = verificationCode,
            onValueChanged = onVerificationCodeChanged
        )
        TextButton(
            modifier = Modifier,
            text = stringResource(R.string.validate),
            onButtonClick = onValidatePressed
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CodeFormPreview() {
    TrabalhoApiComposeTheme {
        CodeForm(
            modifier = Modifier,
            verificationCode = "101010",
            onVerificationCodeChanged = {},
            onValidatePressed = {}
        )
    }
}

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(50.dp),
        onClick = onButtonClick
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun TextButtonSendSmsPreview() {
    TrabalhoApiComposeTheme {
        TextButton(
            modifier = Modifier,
            "Send",
            onButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextButtonValidateSmsCodePreview() {
    TrabalhoApiComposeTheme {
        TextButton(
            modifier = Modifier,
            "Validate",
            onButtonClick = {}
        )
    }
}

@Composable
fun TextFormField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChanged: (String) -> Unit
) {
    TextField(
        label = { Text(text = title) },
        modifier = modifier
            .padding(8.dp)
            .width(250.dp),
        value = value,
        onValueChange = onValueChanged
    )
}

@Preview(showBackground = true)
@Composable
fun TextFormFieldPhoneNumberPreview() {
    TrabalhoApiComposeTheme {
        TextFormField(
            modifier = Modifier,
            title = "Phone Number",
            value = "+55 (42) 9 8765-4321",
            onValueChanged = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextFormFieldCodePreview() {
    TrabalhoApiComposeTheme {
        TextFormField(
            modifier = Modifier,
            title = "Code",
            value = "123456",
            onValueChanged = { }
        )
    }
}