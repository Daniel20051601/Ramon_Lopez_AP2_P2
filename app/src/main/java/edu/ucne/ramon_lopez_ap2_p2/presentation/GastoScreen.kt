package edu.ucne.ramon_lopez_ap2_p2.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.ramon_lopez_ap2_p2.domain.mapper.formatDate
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto
import edu.ucne.ramon_lopez_ap2_p2.ui.theme.Ramon_Lopez_AP2_P2Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    GastoListBody(
        state = state,
        onEvent = viewModel::OnEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoListBody(
    state: GastoUiState,
    onEvent: (GastoEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.userMessage) {
        if (state.userMessage.isNotBlank()) {
            snackbarHostState.showSnackbar(state.userMessage)
            onEvent(GastoEvent.UserMessageShown)
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val fecha = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss",
                            Locale.getDefault()
                        ).format(Date(millis))
                        onEvent(GastoEvent.onFechaChanged(fecha))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gastos") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(GastoEvent.showSheet) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Gasto"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val gastos = state.Gastos
                if (gastos.isEmpty()) {
                    Text(
                        text = "No hay Gastos",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = gastos,
                            key = { gasto -> gasto.gastoId }
                        ) { gasto ->
                            GastoItem(
                                Gasto = gasto,
                                onClick = { onEvent(GastoEvent.LoadGasto(gasto.gastoId)) }
                            )
                        }
                    }
                }
            }
        }

        if (state.showSheet) {
            ModalBottomSheet(
                onDismissRequest = { onEvent(GastoEvent.hideSheet) },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                        .imePadding()
                ) {
                    Text(
                        text = if(state.isEditable)"Editar Gastos" else "Agregar Gastos",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    OutlinedTextField(
                        value = state.fecha.formatDate(),
                        onValueChange = { },
                        label = { Text("Fecha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        enabled = false,
                        readOnly = true,
                        isError = state.fechaError != null,
                        supportingText = {
                            if (state.fechaError != null) {
                                Text(state.fechaError)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        value = state.suplidor,
                        onValueChange = { onEvent(GastoEvent.onSuplidorChanged(it)) },
                        label = { Text("Suplidor") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.suplidorError != null,
                        supportingText = {
                            if (state.suplidorError != null) {
                                Text(state.suplidorError)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.ncf,
                        onValueChange = { onEvent(GastoEvent.onNcfChanged(it)) },
                        label = { Text("NCF") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.ncfError != null,
                        supportingText = {
                            if (state.ncfError != null) {
                                Text(state.ncfError)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        value = state.itbis.toString(),
                        onValueChange = {
                            onEvent(GastoEvent.onItbisChanged(it.toDoubleOrNull() ?: 0.0))
                        },
                        label = { Text("ITBIS") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.itbisError != null,
                        supportingText = {
                            if (state.itbisError != null) {
                                Text(state.itbisError)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        value = state.monto.toString(),
                        onValueChange = {
                            onEvent(GastoEvent.onMontoChanged(it.toDoubleOrNull() ?: 0.0))
                        },
                        label = { Text("Monto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = state.montoError != null,
                        supportingText = {
                            if (state.montoError != null) {
                                Text(state.montoError)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onEvent(GastoEvent.hideSheet) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = { onEvent(GastoEvent.onSave) },
                            modifier = Modifier.weight(1f),
                            enabled = state.fecha.isNotBlank()
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GastoItem(
    Gasto: Gasto,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = Gasto.fecha.formatDate(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.End),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Suplidor: ${Gasto.suplidor}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "NCF: ${Gasto.ncf}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "ITBIS: $${Gasto.itbis}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Monto: $${Gasto.monto}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GastoScreenPreview() {
    val previewGastos = listOf(
        Gasto(
            gastoId = 1,
            fecha = "2024-03-20 08:10 p.m",
            suplidor = "Supermercado",
            ncf = "B0123456781",
            itbis = 180.00,
            monto = 1000.00
        ),
        Gasto(
            gastoId = 2,
            fecha = "2024-03-19 09:30 a.m",
            suplidor = "Ferretería",
            ncf = "B0123456790",
            itbis = 90.00,
            monto = 500.00
        )
    )

    val previewState = GastoUiState(
        isLoading = false,
        Gastos = previewGastos,
        showSheet = false
    )

    Ramon_Lopez_AP2_P2Theme {
        GastoListBody(
            state = previewState,
            onEvent = {}
        )
    }
}

