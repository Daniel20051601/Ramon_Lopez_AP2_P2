package edu.ucne.ramon_lopez_ap2_p2.tareas.local.presentation

interface GastoEvent {
    data class onSuplidorChanged(val suplidor: String): GastoEvent
    data class onFechaChanged(val fecha: String): GastoEvent
    data class onNcfChanged(val ncf: String): GastoEvent
    data class onItbisChanged(val itbis: Double ): GastoEvent
    data class onMontoChanged(val monto: Double): GastoEvent
    object onSave: GastoEvent
    object showSheet: GastoEvent
    object hideSheet: GastoEvent
    object UserMessageShown: GastoEvent
    object gastoSelected: GastoEvent
}