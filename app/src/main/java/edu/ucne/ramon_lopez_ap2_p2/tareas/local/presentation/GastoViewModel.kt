package edu.ucne.ramon_lopez_ap2_p2.tareas.local.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.GetGastoByIdUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.GetGastoUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.PostGastoUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.PutGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    val getGastoUseCase: GetGastoUseCase,
    val getGastoByIdUseCase: GetGastoByIdUseCase,
    val postGastoUseCase: PostGastoUseCase,
    val putGastoUseCase: PutGastoUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(GastoUiState())
    val state: StateFlow<GastoUiState> = _uiState.asStateFlow()

    init {
        getGastos()
    }

    fun getGastos() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                )
            }
            getGastoUseCase().collect { gastoList ->
                _uiState.update {
                    it.copy(
                        Gastos = gastoList
                    )

                }
            }
        }
    }

    fun OnEvent(event: GastoEvent) {
        when (event) {
            is GastoEvent.onFechaChanged -> {
                _uiState.update { state ->
                    state.copy(fecha = event.fecha)

                }
            }

            is GastoEvent.onSuplidorChanged -> {
                _uiState.update { state ->
                    state.copy(suplidor = event.suplidor)
                }
            }

            is GastoEvent.onItbisChanged -> {
                _uiState.update { state ->
                    state.copy(itbis = event.itbis)
                }
            }

            is GastoEvent.onNcfChanged -> {
                _uiState.update { state ->
                    state.copy(ncf = event.ncf)
                }
            }

            is GastoEvent.onMontoChanged -> {
                _uiState.update { state ->
                    state.copy(monto = event.monto)
                }
            }

            is GastoEvent.showSheet -> {
                _uiState.update { state ->
                    state.copy(showSheet = true)
                }
            }

            is GastoEvent.hideSheet -> {
                _uiState.update { state ->
                    state.copy(showSheet = false)
                }
            }

            is GastoEvent.onSave -> onSave()
            else -> {}

        }
    }

    fun onSave() {
        viewModelScope.launch {
            val gasto = GastoRequest(
                fecha = _uiState.value.fecha,
                suplidor = _uiState.value.suplidor,
                ncf = _uiState.value.ncf,
                itbis = _uiState.value.itbis,
                monto = _uiState.value.monto
            )
            val id = _uiState.value.id
            viewModelScope.launch {
                if (id == null || id == 0) {
                    when (postGastoUseCase(gasto)) {
                        is Resource.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    fecha = "",
                                    suplidor = "",
                                    ncf = "",
                                    itbis = 0.00,
                                    monto = 0.00,
                                    userMessage = "Gasto guardado correctamente"
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    userMessage = "Error al guardar el Gasto"
                                )
                            }
                        }

                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    userMessage = "Error desconocido"
                                )
                            }
                        }
                    }
                } else {
                    when (putGastoUseCase(id, gasto)) {
                        is Resource.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    fecha = "",
                                    suplidor = "",
                                    ncf = "",
                                    itbis = 0.00,
                                    monto = 0.00,
                                    userMessage = "Gasto actualizado correctamente"
                                )
                            }
                        }

                        is Resource.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    userMessage = "Error al gactualizar el Gasto"
                                )
                            }
                        }

                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    userMessage = "Error desconocido"
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}


