package edu.ucne.ramon_lopez_ap2_p2.presentation

interface GastoEvent {
    data class onSuplidorChanged(val suplidor: String): GastoEvent
    data class onFechaChanged(val fecha: String): GastoEvent
    data class onNcfChanged(val ncf: String): GastoEvent
    data class onItbisChanged(val itbis: Double ): GastoEvent
    data class onMontoChanged(val monto: Double): GastoEvent
    data object onSave: GastoEvent
    data object showSheet: GastoEvent
    data object hideSheet: GastoEvent
    data object UserMessageShown: GastoEvent
    data class LoadGasto(val id: Int): GastoEvent
}