package edu.ucne.ramon_lopez_ap2_p2.data.remote

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import javax.inject.Inject

class GastosRemoteDataSource @Inject constructor(
    val api: GastosApiService
) {

    suspend fun getGastos(): Resource<List<GastoResponse>> {
        return try {
            val response = api.getGastos()
            if (response.isSuccessful) {
                response.body().let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacia")
            } else {
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            return Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error"}")
        }
    }

    suspend fun postGasto(gastoRequest: GastoRequest): Resource<GastoResponse>{
        return try {
            val response = api.postGasto(gastoRequest)
            if (response.isSuccessful){
                response.body().let {
                    Resource.Success(it) ?: Resource.Error("")
                }
            }else {
                return Resource.Error("Error ${response.code()}: ${response.message()} ")
            }
        }catch (e: Exception){
            return Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error"}")
        }
    }

    suspend fun putGasto(id: Int, gastoRequest: GastoRequest): Resource<Unit>{
        return try{
            val response = api.putGasto(id,gastoRequest)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else {
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            return Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error"}")
        }
    }

    suspend fun getGastoById(id: Int): Resource<GastoResponse>{
        return try{
            val response = api.getGastoById(id)
            if(response.isSuccessful){
                response.body().let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacia")
            }else {
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            return Resource.Error("Error: ${e.localizedMessage ?: "Ocurrió un error"}")
        }
    }

}



