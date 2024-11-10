package br.com.vinibelo.trabalhoapicompose.service.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface AccountService {
    fun sendVerificationCode(phoneNumber: String, onResult: (Throwable?, String?) -> Unit)
    fun verifyAuthenticationCode(verificationId: String, verificationCode: String, onResult: (Throwable?) -> Unit)
    fun loginWithGoogleAccount(launcher: ActivityResultLauncher<Intent>, activity: Activity)
    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, onResult: (Throwable?, String?) -> Unit)
}