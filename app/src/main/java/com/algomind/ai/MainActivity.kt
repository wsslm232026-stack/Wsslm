
package com.algomind.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

data class AiResponse(
    val success: Boolean,
    val message: String,
    val source: String
)

data class AiModule(
    val name: String,
    val description: String,
    val status: String
)

object ModuleRegistry {
    val modules = listOf(
        AiModule("AI CORE", "Pusat kendali AI", "ACTIVE"),
        AiModule("CLOUD ENGINE", "Integrasi AI cloud", "NOT CONFIGURED"),
        AiModule("DATA ENGINE", "Pengumpulan data", "FOUNDATION"),
        AiModule("ANALYTICS ENGINE", "Analisis statistik", "ACTIVE"),
        AiModule("LEARNING ENGINE", "Evaluasi model", "FOUNDATION"),
        AiModule("AUTOMATION ENGINE", "Otomatisasi", "FOUNDATION"),
        AiModule("SECURITY ENGINE", "Keamanan sistem", "FOUNDATION"),
        AiModule("API CONNECTOR", "Integrasi API", "FOUNDATION")
    )
}

object AnalyticsEngine {
    fun analyze(input: String): Pair<Boolean, String> {
        val numbers = input.split(Regex("[,\\s]+"))
            .filter { it.isNotBlank() }

        if (numbers.isEmpty()) {
            return false to "Data tidak ditemukan."
        }

        if (numbers.any {
            it.length != 4 || !it.all(Char::isDigit)
        }) {
            return false to "Semua data harus 4 digit."
        }

        val frequency = numbers
            .flatMap { it.toList() }
            .groupingBy { it }
            .eachCount()
            .toSortedMap()

        return true to buildString {
            appendLine("ANALISIS SELESAI")
            appendLine("Jumlah data: ${numbers.size}")
            appendLine("Data unik: ${numbers.distinct().size}")
            appendLine("Frekuensi digit:")
            frequency.forEach { (digit, count) ->
                appendLine("$digit = $count kali")
            }
            appendLine("Frekuensi bukan prediksi pasti.")
        }
    }
}

object AiCommandRouter {
    fun execute(command: String): AiResponse {
        val text = command.trim()

        if (text.isEmpty()) {
            return AiResponse(false, "Masukkan perintah.", "VALIDATOR")
        }

        val normalized = text.lowercase()

        val result = when {
            normalized == "status" ->
                "AI Core aktif. Cloud belum dikonfigurasi."

            normalized == "bantuan" ->
                "Perintah: status, bantuan, analisis angka: 1234,5678"

            normalized.contains("analisis angka:") -> {
                val analysis = AnalyticsEngine.analyze(
                    text.substringAfter(":")
                )
                return AiResponse(
                    analysis.first,
                    analysis.second,
                    "ANALYTICS ENGINE"
                )
            }

            else -> "Perintah belum dikenali. Ketik bantuan."
        }

        return AiResponse(true, result, "AI CORE")
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFF35D5C5),
                    background = Color(0xFF091321),
                    surface = Color(0xFF17283D)
                )
            ) {
                com.algomind.ai.ui.Dashboard()
            }
        }
    }
}
