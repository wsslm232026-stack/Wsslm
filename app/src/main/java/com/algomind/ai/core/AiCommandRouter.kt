
package com.algomind.ai.core

import com.algomind.ai.data.AiResponse
import com.algomind.ai.engine.AnalyticsEngine
import java.util.Locale

object AiCommandRouter {

    fun execute(command: String): AiResponse {
        val text = command.trim()

        if (text.isEmpty()) {
            return response(
                false,
                "Perintah tidak boleh kosong.",
                "VALIDATOR"
            )
        }

        val normalized = text.lowercase(Locale.ROOT)

        return try {
            when {
                normalized == "status" ||
                normalized == "status sistem" -> {
                    response(
                        true,
                        """
                        ALGO MIND AI SYSTEM STATUS

                        AI Router: ACTIVE
                        Analytics Engine: TERPASANG
                        Cloud AI: BELUM DIKONFIGURASI
                        Database: BELUM TERHUBUNG
                        Mode: Balanced
                        """.trimIndent(),
                        "SYSTEM"
                    )
                }

                normalized == "bantuan" ||
                normalized == "help" -> {
                    response(
                        true,
                        """
                        DAFTAR PERINTAH

                        1. status
                        2. bantuan
                        3. info sistem
                        4. analisis angka: 1234,5678

                        Catatan:
                        Cloud AI memerlukan konfigurasi API.
                        """.trimIndent(),
                        "AI CORE"
                    )
                }

                normalized.startsWith("analisis angka:") ||
                normalized.startsWith("analisa angka:") -> {
                    val input = text.substringAfter(":")
                    val result = AnalyticsEngine.analyze(input)

                    response(
                        result.first,
                        result.second,
                        "ANALYTICS ENGINE"
                    )
                }

                normalized == "info sistem" -> {
                    response(
                        true,
                        """
                        ALGO MIND AI
                        Platform: Android
                        Architecture: Modular
                        Performance: Balanced
                        Router: Active
                        Cloud: Not Configured
                        Database: Not Connected
                        """.trimIndent(),
                        "SYSTEM INFORMATION"
                    )
                }

                else -> {
                    response(
                        false,
                        "Perintah belum dikenali. Ketik bantuan.",
                        "COMMAND ROUTER"
                    )
                }
            }
        } catch (e: Exception) {
            response(
                false,
                "Terjadi kesalahan: ${e.localizedMessage ?: "Unknown error"}",
                "EXECUTION ERROR"
            )
        }
    }

    private fun response(
        success: Boolean,
        message: String,
        source: String
    ) = AiResponse(
        success = success,
        message = message,
        source = source,
        timestamp = System.currentTimeMillis()
    )
}
