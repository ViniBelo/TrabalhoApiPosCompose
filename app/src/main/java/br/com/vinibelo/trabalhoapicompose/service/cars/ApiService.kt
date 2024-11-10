package br.com.vinibelo.trabalhoapicompose.service.cars

import br.com.vinibelo.trabalhoapicompose.model.Car
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("car")
    suspend fun createCar(@Body car: Car): Car

    @PATCH("car/{id}")
    suspend fun updateCar(@Path("id") id: String, @Body car: Car): Car

    @GET("car")
    suspend fun getCars(): List<Car>

    @GET("car/{id}")
    suspend fun getCar(@Path("id") id: String): Car

    @DELETE("car/{id}")
    suspend fun deleteCar(@Path("id") id: String)
}