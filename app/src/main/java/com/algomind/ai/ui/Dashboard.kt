
package com.algomind.ai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.algomind.ai.AiCommandRouter
import com.algomind.ai.ModuleRegistry

@Composable
fun Dashboard() {
    var command by remember { mutableStateOf("") }
    var result by remember {
        mutableStateOf("ALGO MIND AI siap menerima perintah.")
    }
    var source by remember { mutableStateOf("SYSTEM") }

    val accent = Color(0xFF35D5C5)
    val panel = Color(0xFF17283D)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF091321)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "ALGO MIND AI",
                    color = accent,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "UNIFIED INTELLIGENCE PLATFORM",
                    color = Color.White
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("SYSTEM MONITOR", color = accent)
                        Text("AI Core: ACTIVE", color = Color.White)
                        Text("Analytics: ACTIVE", color = Color.White)
                        Text("Cloud: NOT CONFIGURED", color = Color.White)
                        Text("Database: NOT CONNECTED", color = Color.White)
                    }
                }
            }

            item {
                Text(
                    "AI COMMAND CENTER",
                    color = accent,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = command,
                    onValueChange = { command = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Masukkan perintah AI") },
                    minLines = 2
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        val response = AiCommandRouter.execute(command)
                        result = response.message
                        source = response.source
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("EXECUTE COMMAND")
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("AI RESPONSE | $source", color = accent)
                        Spacer(Modifier.height(8.dp))
                        Text(result, color = Color.White)
                    }
                }
            }

            item {
                Text(
                    "INTELLIGENCE MODULES",
                    color = accent,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(ModuleRegistry.modules) { module ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(module.name, color = accent)
                        Text(module.description, color = Color.White)
                        Text(
                            "STATUS: ${module.status}",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
