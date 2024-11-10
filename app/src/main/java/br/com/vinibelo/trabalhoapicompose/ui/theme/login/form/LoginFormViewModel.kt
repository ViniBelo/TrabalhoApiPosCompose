package br.com.vinibelo.trabalhoapicompose.ui.theme.login.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.BuildConfig
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.service.auth.AccountService
import br.com.vinibelo.trabalhoapicompose.service.auth.AccountServiceImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFormViewModel(): ViewModel() {
    private val accountService: AccountService = AccountServiceImpl()
    private var verificationId: String = ""
    var state: LoginFormState by mutableStateOf(LoginFormState())
        private set

    init {
        checkIsAuthenticated()
    }

    fun checkIsAuthenticated() {
        state = state.copy(isAuthenticated = Firebase.auth.currentUser != null)
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

    fun initiateGoogleLogin(activity: Activity, launcher: (Intent) -> Unit) {
        val googleClientId = BuildConfig.GOOGLE_CLIENT_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleClientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        launcher(googleSignInClient.signInIntent)
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, function: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            accountService.handleGoogleSignInResult(task) { error, userUid ->
                if (error == null && userUid != null) {
                    state = state.copy(isAuthenticated = true)
                }
            }
        }
    }
}