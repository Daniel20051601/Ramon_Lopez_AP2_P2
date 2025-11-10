package edu.ucne.ramon_lopez_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.ramon_lopez_ap2_p2.data.remote.Resource
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoRequest
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.GetGastoByIdUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.GetGastoUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.usecase.SaveGastoUseCase
import edu.ucne.ramon_lopez_ap2_p2.domain.validation.GastoValidator
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
    val saveGastoUseCase: SaveGastoUseCase,
    val validator: GastoValidator
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
                        Gastos = gastoList,
                        isLoading = false
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

            is GastoEvent.showSheet ->  showSheet()

            is GastoEvent.hideSheet -> {
                _uiState.update { state ->
                    state.copy(showSheet = false)
                }
            }

            is GastoEvent.onSave -> onSave()
            is GastoEvent.LoadGasto -> loadGastoById(event.id)
            else -> {}

        }
    }

    fun loadGastoById(id: Int) {
        viewModelScope.launch {
            when(val resultado = getGastoByIdUseCase(id)){
                is Resource.Success -> {
                    resultado.data?.let { gasto ->
                        _uiState.update { state ->
                            state.copy(
                                id = gasto.gastoId,
                                fecha = gasto.fecha,
                                suplidor = gasto.suplidor ?: "",
                                ncf = gasto.ncf ?: "",
                                itbis = gasto.itbis,
                                monto = gasto.monto,
                                isEditable = true,
                                showSheet = true,
                                userMessage = "Cargando Gasto para editar",
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update { state ->
                        state.copy(
                            userMessage = "Error al cargar el gasto",
                            showSheet = false
                        )
                    }
                }
                else -> {
                    _uiState.update { state ->
                        state.copy(
                            userMessage = "Error desconocido",
                            showSheet = false
                        )
                    }
                }
            }
        }
    }

    fun showSheet() {
        _uiState.update { state ->
            state.copy(
                showSheet = true,
                fecha = "",
                suplidor = "",
                ncf = "",
                itbis = 0.00,
                monto = 0.00,
                isEditable = false
                )
        }
    }

    fun onSave() {
        viewModelScope.launch {
            val fechaValidation = validator.validateFecha(_uiState.value.fecha)
            val suplidorValidation = validator.validateSuplidor(_uiState.value.suplidor)
            val ncfValidation = validator.validateNcf(_uiState.value.ncf)
            val itbisValidation = validator.validateItbis(_uiState.value.itbis)
            val montoValidation = validator.validateMonto(_uiState.value.monto)

            _uiState.update { state ->
                state.copy(
                    fechaError = if (!fechaValidation.isValid) fechaValidation.error else null,
                    suplidorError = if (!suplidorValidation.isValid) suplidorValidation.error else null,
                    ncfError = if (!ncfValidation.isValid) ncfValidation.error else null,
                    itbisError = if (!itbisValidation.isValid) itbisValidation.error else null,
                    montoError = if (!montoValidation.isValid) montoValidation.error else null
                )
            }

            if (fechaValidation.isValid && suplidorValidation.isValid &&
                ncfValidation.isValid && itbisValidation.isValid && montoValidation.isValid) {
                val gasto = GastoRequest(
                    fecha = _uiState.value.fecha,
                    suplidor = _uiState.value.suplidor,
                    ncf = _uiState.value.ncf,
                    itbis = _uiState.value.itbis,
                    monto = _uiState.value.monto
                )

                val id = _uiState.value.id

                when (val result = saveGastoUseCase(id, Gasto(
                    gastoId = id,
                    fecha = gasto.fecha,
                    suplidor = gasto.suplidor,
                    ncf = gasto.ncf,
                    itbis = gasto.itbis,
                    monto = gasto.monto
                ))) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                fecha = "",
                                suplidor = "",
                                ncf = "",
                                itbis = 0.00,
                                monto = 0.00,
                                userMessage = if(id == 0) "Gasto guardado correctamente"
                                else "Gasto actualizado correctamente",
                                showSheet = false,
                                isEditable = false,
                                id = 0
                            )
                        }
                        getGastos()
                    }
                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                userMessage = result.message ?: "Error al guardar el Gasto"
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