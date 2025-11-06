package edu.ucne.ramon_lopez_ap2_p2.domain.repository

import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import kotlinx.coroutines.flow.Flow

interface GastoRepository {
    suspend fun getGastos(): Flow<List<GastoResponse>>
    suspend fun postGasto(gastoRequest: GastoRequest): Resource<GastoResponse?>
    suspend fun putGasto(id: Int, gastoRequest: GastoRequest): Resource<Unit>
    suspend fun getGastoById(id: Int): Resource<GastoResponse>
}