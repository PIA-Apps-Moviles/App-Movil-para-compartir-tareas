package com.jahir.pia_appsmov.usescases.registro

import com.google.gson.GsonBuilder
import com.jahir.pia_appsmov.usescases.MenuFragments.Buscar.BuscarTareas
import com.jahir.pia_appsmov.usescases.MenuFragments.TodasTareas.TareasGlobales
import com.jahir.pia_appsmov.usescases.MenuFragments.misTareas.dt_MisTareas
import com.jahir.pia_appsmov.usescases.actualizarT.UpdateTarea
import com.jahir.pia_appsmov.usescases.compartirTarea.Tarea
import com.jahir.pia_appsmov.usescases.iniciosesion.NomU
import com.jahir.pia_appsmov.usescases.user.Capturar_Datos
import com.jahir.pia_appsmov.usescases.user.SubirFP
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

object AppConstantes{
    const val Base_Url = "https://pasatareasappservicepia.azurewebsites.net"
}

interface WebService {

    //Parte del USUARIO

    @GET("/usuarios/{user}") //Ver mis datos de la cuenta
    suspend fun misdatos(
        @Path ("user") user : String
    ): Response<Capturar_Datos>

    @POST("/usuarios/verificacion") //Iniciar sesion (ESTE SI FUCIONA) (falta pulir cosas) <------- 5.-
    suspend fun INUser(
        @Body consulta: NomU
    ): Response<String>

    @POST("/usuarios/add") //Registrar Usuario (ESTE SI FUNCIONA) (falta pulir cosas) <------- 6.-
    suspend fun registrarUser(
        @Body user: Usuario
    ): Response<String>

    @PUT("/usuarios/foto") //Agregar foto de perfil
    suspend fun miFotoP(
        @Body fp: SubirFP
    ): Response <String>

    @DELETE("/usuarios/{UserID}") //Eliminar Cuenta
    suspend fun eliminarcuenta(
        @Path ("UserID") UserID : String
    ): Response <String>

///////////////////////////////////////////////////////////////////////////////////////////////////////

    //Parte de las TAREAS

    @GET("/homework/ver/{materia}") //Buscar taarea mediante el searchview
    suspend fun buscarTareas(
        @Path ("materia") materia : String
    ): Response <BuscarTareas>


    @GET("/homework") //Todas las tareas FUNCIONA
    suspend fun tareasGlobales(): Response<TareasGlobales>


    @GET("/homework/{user}") //Todas mis Tareas FUNCIONA
    suspend fun misTareas(
        @Path ("user") user: String
    ): Response<dt_MisTareas>


    @POST("/homework/add") //Guardar Tarea  (ESTE FUNCIONA) (Pulir cosas menores)<--------------------------1.-
    suspend fun guardarTarea (
        @Body homework: Tarea
    ): Response<String>


    @PUT("/homework/update/{id}") //Actualizar Tarea FUNCIONA
    suspend fun actualizarTarea(
        @Path ("id") id: Int,
        @Body updateH: UpdateTarea
    ): Response<String>



    @DELETE("/homework/delete/{id}") //Eliminar Tarea FUNCIONA
    suspend fun eliminartarea(
        @Path ("id") id : Int
    ): Response <String>

}

object RetrofitClient{
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.Base_Url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}