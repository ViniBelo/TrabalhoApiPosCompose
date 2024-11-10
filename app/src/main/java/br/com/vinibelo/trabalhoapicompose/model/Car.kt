package br.com.vinibelo.trabalhoapicompose.model

data class Car(
    val id: String = "000",
    var value: CarDetails
)

data class CarDetails(
    val id: String = "000",
    val name: String = "",
    val year: String = "",
    val licence: String = "",
    val imageUrl: String = "",
    val place: CarLocation? = CarLocation(
        lat = 0.toDouble(),
        long = 0.toDouble()
    )
)

data class CarLocation(
    val lat: Double,
    val long: Double
)