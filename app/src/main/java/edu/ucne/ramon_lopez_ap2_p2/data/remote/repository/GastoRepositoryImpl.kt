package edu.ucne.ramon_lopez_ap2_p2.data.remote.repository

import edu.ucne.ramon_lopez_ap2_p2.data.remote.GastosRemoteDataSource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import edu.ucne.ramon_lopez_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    val remoteDataSource: GastosRemoteDataSource
): GastoRepository {
    override suspend fun getGastos(): Flow<List<GastoResponse>> = flow {
        try {
            when (val result = remoteDataSource.getGastos()) {
                is Resource.Success -> {
                    val list = result.data?.map { it } ?: emptyList()
                    emit(list)
                }
                is Resource.Error -> {
                    emit(emptyList())
                }
                else -> {
                    emit(emptyList())
                }
            }
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun postGasto(gastoRequest: GastoRequest): Resource<GastoResponse?> {
        val result = remoteDataSource.postGasto(gastoRequest)
        return when (result) {
            is Resource.Success -> {
                val gasto = result.data
                Resource.Success(gasto)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error")
            }
        }
    }

    override suspend fun putGasto(id: Int, gastoRequest: GastoRequest): Resource<Unit>{
        val result = remoteDataSource.putGasto(id, gastoRequest)
        return when(result){
            is Resource.Success -> {
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error")
            }
        }
    }

    override suspend fun getGastoById(id: Int): Resource<GastoResponse>{
        val result = remoteDataSource.getGastoById(id)
        return when(result){
            is Resource.Success -> {
                val gasto = result.data
                Resource.Success(gasto)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error")
            }
        }

    }
}
