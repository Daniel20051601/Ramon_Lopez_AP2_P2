package edu.ucne.ramon_lopez_ap2_p2.domain.model

data class Gasto(
    val fecha: String,
    val suplidor: String?,
    val ncf: String?,
    val itbis: Double,
    val monto: Double
)
