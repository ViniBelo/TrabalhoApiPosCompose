package br.com.vinibelo.trabalhoapicompose.ui.theme.car.details

import br.com.vinibelo.trabalhoapicompose.model.Car

data class FormCarState(
    val loading: Boolean = false,
    val errorWhileLoading: Boolean = false,
    val car: Car = Car()
)