package edu.ucne.ramon_lopez_ap2_p2.domain.usecase

import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto
import edu.ucne.ramon_lopez_ap2_p2.domain.repository.GastoRepository
import javax.inject.Inject

class SaveGastoUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    suspend operator fun invoke(id: Int = 0, gasto: Gasto): Resource<Gasto?> {
        return if (id == 0) {
            repository.postGasto(gasto)
        } else {
            when (repository.putGasto(id, gasto)) {
                is Resource.Success -> Resource.Success(null)
                is Resource.Error -> Resource.Error("Error al actualizar el gasto")
                else -> Resource.Error("Error desconocido")
            }
        }
    }
}