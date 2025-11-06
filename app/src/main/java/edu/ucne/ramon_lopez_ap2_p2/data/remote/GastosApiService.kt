package edu.ucne.ramon_lopez_ap2_p2.data.remote

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastosApiService {
    @GET("api/Gastos")
    suspend fun getGastos(): Response<List<GastoResponse>>

    @POST("api/Gastos")
    suspend fun postGasto(@Body gestoRequest: GastoRequest): Response<GastoResponse>

    @GET("api/Gastos/{id}")
    suspend fun getGastoById(@Path("id") id : Int): Response<GastoResponse>

    @PUT("api/Gastos/{id}")
    suspend fun putGasto(@Path("id") id: Int, @Body gastoRequest: GastoRequest): Response<Unit>
}