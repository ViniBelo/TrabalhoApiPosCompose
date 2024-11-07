package br.com.vinibelo.trabalhoapicompose.model

data class Car(
    val id: String = "000",
    val name: String = "",
    val year: String = "",
    val license: String = "",
    val imageUrl: String = "",
    val place: CarLocation? = CarLocation(
        latitude = 0.toDouble(),
        longitude = 0.toDouble()
    )
)

data class CarLocation(
    val latitude: Double,
    val longitude: Double
)