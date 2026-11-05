package com.algomind.ai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algomind.ai.core.AiCommandRouter
import com.algomind.ai.core.ModuleRegistry

private val Background = Color(0xFF091321)
private val Panel = Color(0xFF17283D)
private val Accent = Color(0xFF35D5C5)
private val MainText = Color(0xFFEAF2FF)

@Composable
fun Dashboard() {

    var command by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf(
            "ALGO MIND AI siap menerima perintah."
        )
    }

    var source by remember {
        mutableStateOf("SYSTEM")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            item {

                Text(
                    "ALGO MIND AI",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Accent
                )

                Text(
                    "UNIFIED INTELLIGENCE PLATFORM",
                    color = MainText,
                    fontSize = 11.sp
                )
            }

            item {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Panel
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            "SYSTEM MONITOR",
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )

                        StatusRow("Application", "RUNNING")
                        StatusRow("AI Core", "ACTIVE")
                        StatusRow("Analytics", "ACTIVE")
                        StatusRow("Cloud AI", "NOT CONFIGURED")
                        StatusRow("Database", "NOT CONNECTED")
                    }
                }
            }

            item {

                Text(
                    "AI COMMAND CENTER",
                    color = Accent,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = command,
                    onValueChange = {
                        command = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Masukkan perintah AI")
                    },
                    minLines = 2
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {

                        val response =
                            AiCommandRouter.execute(command)

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
                        containerColor = Panel
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            "AI RESPONSE | $source",
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            result,
                            color = MainText
                        )
                    }
                }
            }

            item {

                Text(
                    "INTELLIGENCE MODULES",
                    color = Accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            items(ModuleRegistry.modules) { module ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Panel
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        Text(
                            module.name,
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            module.description,
                            color = MainText,
                            fontSize = 12.sp
                        )

                        Text(
                            "STATUS: ${module.status}",
                            color = Color.LightGray,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusRow(
    label: String,
    status: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(label, color = MainText)

        Text(
            status,
            color = Accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
