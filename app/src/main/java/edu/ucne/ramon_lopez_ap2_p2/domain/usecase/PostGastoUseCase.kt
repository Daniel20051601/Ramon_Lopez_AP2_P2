package edu.ucne.ramon_lopez_ap2_p2.domain.usecase

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.domain.repository.GastoRepository
import javax.inject.Inject

class PostGastoUseCase @Inject constructor(
    val repository: GastoRepository
) {
    suspend operator fun invoke(gasto: GastoRequest) = repository.postGasto(gasto)
}