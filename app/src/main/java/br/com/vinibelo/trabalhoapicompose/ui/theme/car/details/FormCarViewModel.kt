package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.service.Result
import br.com.vinibelo.trabalhoapicompose.service.RetrofitClient
import br.com.vinibelo.trabalhoapicompose.service.safeApiCall
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.Arguments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormCarViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val carId: String = savedStateHandle.get<String>(Arguments.CAR_ID) ?: ""
    var state: FormCarState by mutableStateOf(FormCarState())
        private set

    init {
        getCar()
    }

    private fun getCar() {
        state = state.copy(
            isLoading = true,
            errorWhileLoading = false,
        )
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getCar(carId) }

            withContext(Dispatchers.Main) {
                state = when(result) {
                    is Result.Error -> {
                        state.copy(
                            errorWhileLoading = true
                        )
                    }

                    is Result.Success -> {
                        state.copy(
                            car = result.data
                        )
                    }
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    fun deleteCar() {
        state = state.copy(isDeleting = true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.deleteCar(carId) }

            withContext(Dispatchers.Main) {
                state = when (result) {
                    is Result.Error -> {
                        state.copy(
                            isDeleting = false,
                            errorWhileLoading = true
                        )
                    }

                    is Result.Success -> {
                        state.copy(
                            isDeleting = false,
                            persistedOrDeletedCar = true
                        )
                    }
                }
            }
            hideConfirmationDialog()
        }
    }

    fun hideConfirmationDialog() {
        state = state.copy(showConfirmationDialog = false)
    }

    fun showConfirmationDialog() {
        state = state.copy(showConfirmationDialog = true)
    }
}