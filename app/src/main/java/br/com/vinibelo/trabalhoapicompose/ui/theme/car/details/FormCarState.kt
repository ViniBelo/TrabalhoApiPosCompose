package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import br.com.vinibelo.trabalhoapicompose.model.Car

data class FormField<T>(
    val value: T,
    val errorMessageCode: Int = 0
) {
    val hasError get(): Boolean = errorMessageCode > 0
    val isValid get(): Boolean = !hasError
}

data class FormCarState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val persistedOrDeletedCar: Boolean = false,
    val errorWhileLoading: Boolean = false,
    val errorWhileSaving: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val car: Car = Car(),
    val name: FormField<String> = FormField(value = ""),
    val year: FormField<String> = FormField(value = ""),
    val license: FormField<String> = FormField(value = ""),
    val imageUrl: FormField<String> = FormField(value = "")
)