package edu.ucne.ramon_lopez_ap2_p2.tareas.local.presentation

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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.ramon_lopez_ap2_p2.data.remote.dto.GastoResponse
import edu.ucne.ramon_lopez_ap2_p2.domain.model.Gasto

@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()
   GastoListBody(
        state,
        viewModel::OnEvent
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

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(GastoEvent.UserMessageShown)
        }
    }
        Scaffold (
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Gastos") }
                )
            },
            snackbarHost = {SnackbarHost(snackbarHostState)},
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {onEvent(GastoEvent.showSheet)},

                    ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar Gastos"
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ){
                if(state.isLoading != null){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align (Alignment.Center)
                    )
                }else {
                    if(state.Gastos.isEmpty()){
                        Text(
                            text = "No hay Gastos",
                            modifier = Modifier.align(Alignment.Center),
                            style= MaterialTheme.typography.bodyLarge
                        )
                    }else{
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            items(
                                items = state.Gastos,
                                key = { gasto -> gasto.id }
                            ){ gasto ->
                                GastoItem(
                                    Gasto = gasto,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
            if(state.showSheet != null){
                ModalBottomSheet(
                    onDismissRequest = {onEvent(GastoEvent.hideSheet)},
                    sheetState= sheetState
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .navigationBarsPadding()
                            .imePadding()
                    ) {
                        Text(
                            text =  "Agregar Gastos",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        OutlinedTextField(
                            value = state.fecha,
                            onValueChange = { onEvent(GastoEvent.onFechaChanged(it)) },
                            label = { Text("Fecha") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.padding(8.dp))

                        OutlinedTextField(
                            value = state.suplidor,
                            onValueChange = { onEvent(GastoEvent.onSuplidorChanged(it)) },
                            label = { Text("Suplidor") },
                            modifier = Modifier
                                .fillMaxWidth(),
                            singleLine = true
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
        Gasto: GastoResponse,
        onClick: () -> Unit
    ){
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text(
                    text = Gasto.fecha,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Cédula: ${Gasto.suplidor}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }

