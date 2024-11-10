package br.com.vinibelo.trabalhoapicompose.service.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.vinibelo.trabalhoapicompose.BuildConfig
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class AccountServiceImpl : AccountService {
    override fun sendVerificationCode(phoneNumber: String, onResult: (Throwable?, String?) -> Unit) {
        val options = PhoneAuthOptions.newBuilder(Firebase.auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                override fun onVerificationFailed(e: FirebaseException) {
                    onResult(e, null)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onResult(null, verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyAuthenticationCode(
        verificationId: String,
        verificationCode: String,
        onResult: (Throwable?) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, verificationCode)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(null)
                } else {
                    onResult(task.exception)
                }
            }
    }

    private fun getGoogleSignInIntent(activity: Activity): Intent {
        val googleClientId = BuildConfig.GOOGLE_CLIENT_ID

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleClientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        return googleSignInClient.signInIntent
    }

    override fun loginWithGoogleAccount(
        launcher: ActivityResultLauncher<Intent>,
        activity: Activity
    ) {
        val signInIntent = getGoogleSignInIntent(activity)
        launcher.launch(signInIntent)
    }

    override fun handleGoogleSignInResult(
        task: Task<GoogleSignInAccount>,
        onResult: (Throwable?, String?) -> Unit
    ) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            val userUid = FirebaseAuth.getInstance().currentUser?.uid
                            onResult(null, userUid)
                        } else {
                            onResult(authTask.exception, null)
                        }
                    }
            } else {
                onResult(Exception("Google SignIn failed"), null)
            }
        } catch (e: ApiException) {
            onResult(e, null)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
