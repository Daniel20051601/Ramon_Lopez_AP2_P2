package edu.ucne.ramon_lopez_ap2_p2.domain.repository

import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastoRepository {
    suspend fun getGastos(): Flow<List<Gasto>>
    suspend fun postGasto(gasto: Gasto): Resource<Gasto?>
    suspend fun putGasto(id: Int, gasto: Gasto): Resource<Unit>
    suspend fun getGastoById(id: Int): Resource<Gasto?>
}