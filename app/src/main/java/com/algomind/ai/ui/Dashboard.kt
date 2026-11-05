
package com.algomind.ai.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.algomind.ai.core.AiCommandRouter
import com.algomind.ai.core.ModuleRegistry

@Composable
fun Dashboard() {

    var command by remember { mutableStateOf("") }

    var result by remember {
        mutableStateOf("ALGO MIND AI siap menerima perintah.")
    }

    var source by remember { mutableStateOf("SYSTEM") }

    var executing by remember { mutableStateOf(false) }

    val accent = Color(0xFF35D5C5)
    val panel = Color(0xFF17283D)
    val background = Color(0xFF091321)

    fun executeCommand(input: String) {

        if (input.isBlank()) {
            result = "Masukkan perintah terlebih dahulu."
            source = "VALIDATION"
            return
        }

        if (executing) return

        executing = true

        try {
            val response = AiCommandRouter.execute(input)

            result = response.message
            source = response.source

        } catch (e: Exception) {
            result = "Terjadi kesalahan: ${
                e.localizedMessage ?: "Unknown error"
            }"
            source = "ERROR"
        } finally {
            executing = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // HEADER
            item {
                Text(
                    text = "ALGO MIND AI",
                    color = accent,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "UNIFIED INTELLIGENCE PLATFORM",
                    color = Color.White,
                    fontSize = 12.sp
                )

                Text(
                    text = "AI COMMAND • ANALYTICS • CLOUD SYSTEM",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }

            // SYSTEM MONITOR
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "SYSTEM MONITOR",
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "AI Router: Terpasang",
                            color = Color.White
                        )

                        Text(
                            "Analytics: Perlu pengujian",
                            color = Color.White
                        )

                        Text(
                            "Cloud: Belum dikonfigurasi",
                            color = Color.White
                        )

                        Text(
                            "Database: Belum terhubung",
                            color = Color.White
                        )
                    }
                }
            }

            // COMMAND CENTER
            item {
                Text(
                    text = "AI COMMAND CENTER",
                    color = accent,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = command,
                    onValueChange = { command = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Masukkan perintah AI")
                    },
                    placeholder = {
                        Text("Contoh: Analisis data historis")
                    },
                    minLines = 2,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            executeCommand(command)
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accent,
                        focusedLabelColor = accent,
                        cursorColor = accent
                    )
                )

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        executeCommand(command)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !executing
                ) {
                    Text(
                        if (executing) {
                            "PROCESSING..."
                        } else {
                            "EXECUTE COMMAND"
                        }
                    )
                }

                OutlinedButton(
                    onClick = {
                        command = ""
                        result = "ALGO MIND AI siap menerima perintah."
                        source = "SYSTEM"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLEAR COMMAND")
                }
            }

            // AI RESPONSE
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "AI RESPONSE | $source",
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = result,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // MODULES
            item {
                Text(
                    text = "INTELLIGENCE MODULES",
                    color = accent,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(ModuleRegistry.modules) { module ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = panel
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {

                        Text(
                            text = module.name,
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = module.description,
                            color = Color.White,
                            fontSize = 13.sp
                        )

                        Text(
                            text = "STATUS: ${module.status}",
                            color = Color.LightGray,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // FOOTER
            item {
                Spacer(Modifier.height(10.dp))

                Text(
                    text = "ALGO MIND AI • SYSTEM DASHBOARD",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
        }
    }
}
