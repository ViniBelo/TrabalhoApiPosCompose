package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.service.Result
import br.com.vinibelo.trabalhoapicompose.service.RetrofitClient
import br.com.vinibelo.trabalhoapicompose.service.safeApiCall
import br.com.vinibelo.trabalhoapicompose.ui.theme.car.Arguments
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

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
                            car = result.data,
                            name = state.name.copy(value = result.data.name),
                            year = state.year.copy(value = result.data.year),
                            license = state.license.copy(value = result.data.license),
                            imageUrl = state.imageUrl.copy(value = result.data.imageUrl)
                        )
                    }
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    fun sendImage(uri: Uri, context: Context) {
        state = state.copy(isUploadingImage = true)
        CoroutineScope(Dispatchers.IO).launch {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val uniqueImageName = UUID.randomUUID()
            val imageRef = storageRef.child("images/$uniqueImageName.jpg")

            val bytesArray: ByteArray? = context.contentResolver
                .openInputStream(uri)
                ?.use { it.readBytes() }

            CoroutineScope(Dispatchers.IO).launch {
                bytesArray?.let {
                    val uploadTask = imageRef.putBytes(bytesArray)
                    uploadTask.addOnFailureListener {
                        Toast.makeText(
                            context,
                            context.getString(R.string.image_upload_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            state = state.copy(
                                imageUrl = state.imageUrl.copy(value = uri.toString())
                            )
                        }
                    }
                }
            }
            state = state.copy(isUploadingImage = false)
        }
    }

    fun onNameChanged(newName: String) {
        if (state.name.value != newName) {
            state = state.copy(
                name = state.name.copy(
                    value = newName
                )
            )
        }
    }

    fun onYearChanged(newYear: String) {
        if (state.year.value != newYear) {
            state = state.copy(
                year = state.year.copy(
                    value = newYear
                )
            )
        }
    }

    fun onLicenseChanged(newLicense: String) {
        if (state.license.value != newLicense) {
            state = state.copy(
                license = state.license.copy(
                    value = newLicense
                )
            )
        }
    }

    fun onImageUrlChanged(newImageUrl: String) {
        if (state.imageUrl.value != newImageUrl) {
            state = state.copy(
                imageUrl = state.imageUrl.copy(
                    value = newImageUrl
                )
            )
        }
    }

    fun saveCar() {
        state = state.copy(isSaving = true)
        val car = state.car.copy(
            name = state.name.value,
            year = state.year.value,
            license = state.license.value,
            imageUrl = state.imageUrl.value
        )
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall {
                RetrofitClient.apiService.updateCar(
                    id = carId,
                    car = car
                )
            }
            withContext(Dispatchers.Main) {
                state = when (result) {
                    is Result.Error -> {
                        state.copy(
                            errorWhileSaving = true
                        )
                    }
                    is Result.Success -> {
                        state.copy(
                            persistedOrDeletedCar = true
                        )
                    }
                }
            }
        }
        state = state.copy(isSaving = false)
    }

    fun deleteCar() {
        state = state.copy(isDeleting = true)
        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.deleteCar(carId) }

            withContext(Dispatchers.Main) {
                state = when (result) {
                    is Result.Error -> {
                        state.copy(
                            errorWhileLoading = true
                        )
                    }

                    is Result.Success -> {
                        state.copy(
                            persistedOrDeletedCar = true
                        )
                    }
                }
            }
            hideConfirmationDialog()
        }
        state = state.copy(isDeleting = false)
    }

    fun hideConfirmationDialog() {
        state = state.copy(showConfirmationDialog = false)
    }

    fun showConfirmationDialog() {
        state = state.copy(showConfirmationDialog = true)
    }
}