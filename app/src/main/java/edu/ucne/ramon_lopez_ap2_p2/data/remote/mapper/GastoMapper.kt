package edu.ucne.ramon_lopez_ap2_p2.data.remote.mapper

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto

fun Gasto.toRequest(): GastoRequest = GastoRequest(
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)

fun GastoResponse.toDomain() = Gasto(
    gastoId = gastoId,
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)