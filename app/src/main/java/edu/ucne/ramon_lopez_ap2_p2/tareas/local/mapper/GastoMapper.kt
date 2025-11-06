package edu.ucne.ramon_lopez_ap2_p2.tareas.local.mapper

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto

fun Gasto.toDto() = GastoRequest(
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)

fun GastoRequest.toDomain() = Gasto(
    fecha = fecha,
    suplidor = suplidor,
    ncf = ncf,
    itbis = itbis,
    monto = monto
)