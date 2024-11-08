package br.com.vinibelo.trabalhoapicompose.service.auth

import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
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
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Verificação automática pode ser tratada aqui, se necessário
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onResult(e, null) // Retorna o erro
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onResult(null, verificationId) // Retorna o verificationId com sucesso
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
}
