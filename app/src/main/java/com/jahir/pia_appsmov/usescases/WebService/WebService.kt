package com.jahir.pia_appsmov.usescases.registro

import com.google.gson.GsonBuilder
import com.jahir.pia_appsmov.usescases.iniciosesion.NomU
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object AppConstantes{
    const val Base_Url = "https://pasatareasappservicepia.azurewebsites.net/"
}

interface WebService {
    @POST("/usuarios/add")
    suspend fun registrarUser(
        @Body user: Usuario
    ): Response<String>

    @POST("/usuarios/verificacion")
    suspend fun INUser(
        @Body consulta: NomU
    ): Response<String>
}

object RetrofitClient{
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.Base_Url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}