package edu.ucne.ramon_lopez_ap2_p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.ramon_lopez_ap2_p2.presentation.GastoScreen
import edu.ucne.ramon_lopez_ap2_p2.ui.theme.Ramon_Lopez_AP2_P2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ramon_Lopez_AP2_P2Theme {
                GastoScreen()
            }
        }
    }
}

