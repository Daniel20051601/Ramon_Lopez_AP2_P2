package edu.ucne.ramon_lopez_ap2_p2.data.remote.repository

import edu.ucne.ramon_lopez_ap2_p2.data.remote.GastosRemoteDataSource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import edu.ucne.ramon_lopez_ap2_p2.data.remote.mapper.toDomain
import edu.ucne.ramon_lopez_ap2_p2.data.remote.mapper.toRequest
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto
import edu.ucne.ramon_lopez_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    val remoteDataSource: GastosRemoteDataSource
): GastoRepository {
    override suspend fun getGastos(): Flow<List<Gasto>> = flow {
            when (val result = remoteDataSource.getGastos()) {
                is Resource.Success -> {
                    val list = result.data?.map { it.toDomain() } ?: emptyList()
                    emit(list)
                }
                is Resource.Error -> {
                    emit(emptyList())
                }
                else -> {
                    emit(emptyList())
                }
            }
    }

    override suspend fun postGasto(gasto: Gasto): Resource<Gasto?> {
        val result = remoteDataSource.postGasto(gasto.toRequest())
        return when (result) {
            is Resource.Success -> {
                val response = result.data
                Resource.Success(response?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error")
            }
        }
    }

    override suspend fun putGasto(id: Int, gasto: Gasto): Resource<Unit>{
        val result = remoteDataSource.putGasto(id, gasto.toRequest())
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

    override suspend fun getGastoById(id: Int): Resource<Gasto?>{
        val result = remoteDataSource.getGastoById(id)
        return when(result){
            is Resource.Success -> {
                val gasto = result.data
                Resource.Success(gasto?.toDomain())
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
