package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import br.com.vinibelo.trabalhoapicompose.model.Car

data class FormCarState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val persistedOrDeletedCar: Boolean = false,
    val errorWhileLoading: Boolean = false,
    val showConfirmationDialog: Boolean = false,
    val car: Car = Car()
)