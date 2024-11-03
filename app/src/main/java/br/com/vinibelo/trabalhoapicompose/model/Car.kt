package br.com.vinibelo.trabalhoapicompose.model

data class Car(
    val id: String,
    val name: String,
    val year: String,
    val license: String,
    val imageUrl: String,
    val place: CarLocation? = null
)

data class CarLocation(
    val latitude: Double,
    val longitude: Double
)