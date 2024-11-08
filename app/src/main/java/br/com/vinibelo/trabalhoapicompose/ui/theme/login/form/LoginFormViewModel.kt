package br.com.vinibelo.trabalhoapicompose.ui.theme.login.form

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.service.auth.AccountService
import br.com.vinibelo.trabalhoapicompose.service.auth.AccountServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginFormViewModel(): ViewModel() {
    private val accountService: AccountService = AccountServiceImpl()
    private var verificationId: String = ""
    var state: LoginFormState by mutableStateOf(LoginFormState())
        private set

    init {
        if (Firebase.auth.currentUser != null) {
            state = state.copy(isAuthenticated = true)
        }
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        if (state.phoneNumber != newPhoneNumber) {
            state = state.copy(
                phoneNumber = newPhoneNumber
            )
        }
    }

    fun onVerificationCodeChange(newVerificationCode: String) {
        if (state.verificationCode != newVerificationCode) {
            state = state.copy(
                verificationCode = newVerificationCode
            )
        }
    }

    fun sendVerificationCode(context: Context) {
        accountService.sendVerificationCode(state.phoneNumber) { error, verificationId ->
            if (error != null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_sending_code),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (verificationId != null) {
                this.verificationId = verificationId
                state = state.copy(
                    sentValidationCode = true
                )
                Toast.makeText(
                    context,
                    context.getString(R.string.code_sent_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun validateCode(context: Context) {
        accountService.verifyAuthenticationCode(
            verificationCode = state.verificationCode,
            verificationId = verificationId,
        ) { error ->
            if (error == null) {
                state = state.copy(
                    isAuthenticated = true
                )
            }
        }
    }
}