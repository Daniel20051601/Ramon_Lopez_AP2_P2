package edu.ucne.ramon_lopez_ap2_p2.data.remote.dto

data class GastoResponse (
    val id: Int,
    val fecha: String,
    val suplidor: String?,
    val ncf: String?,
    val itbis: Double,
    val monto: Double
)