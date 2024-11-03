package br.com.vinibelo.trabalhoapicompose.service

import br.com.vinibelo.trabalhoapicompose.model.Car
import retrofit2.http.GET

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>
}