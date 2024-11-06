package br.com.vinibelo.trabalhoapicompose.ui.theme.car.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedCarsViewModel: ViewModel() {
    var changesOnCar by mutableStateOf(false)
        private set

    fun setCarChanged(changed: Boolean) {
        changesOnCar = changed
    }
}