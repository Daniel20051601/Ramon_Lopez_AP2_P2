package edu.ucne.ramon_lopez_ap2_p2.tareas.local.presentation

import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse

data class GastoUiState(
    val isLoading: Boolean? = null,
    val Gastos: List<GastoResponse>? = null,
    val id: Int = 0,
    val fecha: String = "",
    val suplidor: String = "",
    val ncf: String = "",
    val itbis: Double = 0.00,
    val monto: Double = 0.00,
    val showSheet: Boolean? = null,
    val userMessage: String = ""
    )
