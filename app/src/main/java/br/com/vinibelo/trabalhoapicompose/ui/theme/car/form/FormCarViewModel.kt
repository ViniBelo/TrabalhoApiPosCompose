package br.com.vinibelo.trabalhoapicompose.ui.theme.car.form

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.vinibelo.trabalhoapicompose.R
import br.com.vinibelo.trabalhoapicompose.service.cars.Result
import br.com.vinibelo.trabalhoapicompose.service.cars.RetrofitClient
import br.com.vinibelo.trabalhoapicompose.service.cars.safeApiCall
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
                            name = state.name.copy(value = result.data.value.name),
                            year = state.year.copy(value = result.data.value.year),
                            licence = state.licence.copy(value = result.data.value.licence),
                            imageUrl = state.imageUrl.copy(value = result.data.value.imageUrl)
                        )
                    }
                }
            }
        }
        state = state.copy(isLoading = false)
    }

    fun isEdit(): Boolean {
        return carId.isNotBlank()
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
                    value = newName,
                    errorMessageCode = validateName(newName)
                )
            )
        }
    }

    private fun validateName(name: String): Int =
        if (name.isBlank())
            R.string.name_required
        else 0

    fun onYearChanged(newYear: String) {
        if (state.year.value != newYear) {
            state = state.copy(
                year = state.year.copy(
                    value = newYear,
                    errorMessageCode = validateYear(newYear)
                )
            )
        }
    }

    private fun validateYear(year: String): Int =
        if (year.isBlank())
            R.string.year_required
        else 0

    fun onLicenseChanged(newLicense: String) {
        if (state.licence.value != newLicense) {
            state = state.copy(
                licence = state.licence.copy(
                    value = newLicense,
                    errorMessageCode = validateLicense(newLicense)
                )
            )
        }
    }

    private fun validateLicense(license: String): Int =
        if (license.isBlank())
            R.string.license_required
        else 0

    fun onImageUrlChanged(newImageUrl: String) {
        if (state.imageUrl.value != newImageUrl) {
            state = state.copy(
                imageUrl = state.imageUrl.copy(
                    value = newImageUrl,
                    errorMessageCode = validateImageUrl(newImageUrl)
                )
            )
        }
    }

    private fun validateImageUrl(imageUrl: String): Int =
        if (imageUrl.isBlank())
            R.string.image_url_required
        else 0

    fun saveCar() {
        if (isValidForm()) {
            state = state.copy(isSaving = true)
            val car = state.car.value.copy(
                id = if (carId != "") carId else UUID.randomUUID().toString(),
                name = state.name.value,
                year = state.year.value,
                licence = state.licence.value,
                imageUrl = state.imageUrl.value
            )
            CoroutineScope(Dispatchers.IO).launch {
                val result = if (carId != "") {
                    safeApiCall {
                        RetrofitClient.apiService.updateCar(
                            id = car.id,
                            car = car
                        )
                    }
                } else {
                    safeApiCall {
                        RetrofitClient.apiService.createCar(car = car)
                    }
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
    }

    private fun isValidForm(): Boolean {
        state = state.copy(
            name = state.name.copy(
                errorMessageCode = validateName(state.name.value)
            ),
            year = state.year.copy(
                errorMessageCode = validateYear(state.year.value)
            ),
            licence = state.licence.copy(
                errorMessageCode = validateLicense(state.licence.value)
            ),
            imageUrl = state.imageUrl.copy(
                errorMessageCode = validateImageUrl(state.imageUrl.value)
            )
        )
        return state.isValidForm
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