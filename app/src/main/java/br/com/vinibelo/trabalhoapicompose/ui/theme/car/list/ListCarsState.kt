package br.com.vinibelo.trabalhoapicompose.ui.theme.car.list

import br.com.vinibelo.trabalhoapicompose.model.Car

data class ListCarsState(
    val loading: Boolean = false,
    val errorWhileLoading: Boolean = false,
    val cars: List<Car> = listOf()
)