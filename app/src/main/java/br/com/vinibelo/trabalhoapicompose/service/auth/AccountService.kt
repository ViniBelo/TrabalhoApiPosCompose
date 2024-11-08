package br.com.vinibelo.trabalhoapicompose.service.auth

interface AccountService {
    fun sendVerificationCode(phoneNumber: String, onResult: (Throwable?, String?) -> Unit)
    fun verifyAuthenticationCode(verificationId: String, verificationCode: String, onResult: (Throwable?) -> Unit)
}