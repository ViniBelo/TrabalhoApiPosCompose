package br.com.vinibelo.trabalhoapicompose.ui.theme.car.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SharedCarsViewModel: ViewModel() {
    var isAuthenticated by mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
        private set

    var changesOnCar by mutableStateOf(false)
        private set

    fun setCarChanged(changed: Boolean) {
        changesOnCar = changed
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        isAuthenticated = false
    }
}