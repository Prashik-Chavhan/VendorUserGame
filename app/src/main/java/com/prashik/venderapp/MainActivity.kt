package com.prashik.venderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.prashik.venderapp.ui.theme.VenderAppTheme

class MainActivity : ComponentActivity() {
    private val socketManager = SocketManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        socketManager.connect()
        enableEdgeToEdge()
        setContent {
            VenderAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val isConnected = remember { mutableStateOf(false) }

                    var showDialog by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        isConnected.value = socketManager.isConnected()
                    }

                    var newGeneratedNumber by remember { mutableIntStateOf(0) }

                    if (showDialog) {
                        GeneratedNumberDialog(
                            generatedNumber = newGeneratedNumber,
                            onDismiss = { showDialog = false }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                val generatedNumber = generateANumber()
                                socketManager.sendNumber(generatedNumber)
                                newGeneratedNumber = generatedNumber
                                showDialog = true
                            }
                        ) {
                            Text("Generate Number - ${isConnected.value}")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }
}

fun generateANumber(): Int {
    return (0..99).random()
}

@Composable
fun GeneratedNumberDialog(
    generatedNumber: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = modifier
                .width(300.dp)
                .height(250.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Generated number: $generatedNumber",
                fontSize = 25.sp
            )
        }
    }
}