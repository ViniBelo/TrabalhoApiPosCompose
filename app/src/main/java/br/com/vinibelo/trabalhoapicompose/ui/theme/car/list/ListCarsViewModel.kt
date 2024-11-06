package br.com.vinibelo.trabalhoapicompose.ui.theme.car.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.service.Result
import br.com.vinibelo.trabalhoapicompose.service.RetrofitClient
import br.com.vinibelo.trabalhoapicompose.service.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListCarsViewModel: ViewModel() {
    var state: ListCarsState by mutableStateOf(ListCarsState())
        private set

    init {
        fetchCars()
    }

    fun fetchCars() {
        state = state.copy(
            loading = true,
            errorWhileLoading = false
        )
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getCars() }

            withContext(Dispatchers.Main) {
                state = when (result) {
                    is Result.Error -> {
                        state.copy(errorWhileLoading = true)
                    }

                    is Result.Success -> {
                        state.copy(cars = result.data)
                    }
                }
            }
        }
        state = state.copy(loading = false)
    }
}