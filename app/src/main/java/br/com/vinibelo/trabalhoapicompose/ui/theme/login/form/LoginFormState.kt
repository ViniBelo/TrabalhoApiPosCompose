package br.com.vinibelo.trabalhoapicompose.ui.theme.login.form

data class LoginFormState(
    val sentValidationCode: Boolean = false,
    val isAuthenticated: Boolean = false,
    val phoneNumber: String = "+55",
    val verificationCode: String = ""
)
