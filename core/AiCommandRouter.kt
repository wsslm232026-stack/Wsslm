
package com.algomind.ai.core

import com.algomind.ai.engine.AnalyticsEngine
import com.algomind.ai.data.AiResponse
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
                normalized.contains("status sistem") ||
                normalized.contains("status aplikasi") -> {

                    response(
                        true,
                        """
                        ALGO MIND AI SYSTEM STATUS

                        AI Router: ACTIVE
                        Analytics Engine: TERPASANG
                        Cloud AI: BELUM DIKONFIGURASI
                        Database: BELUM TERHUBUNG
                        Platform: Android
                        Mode: Balanced

                        Catatan: Status instalasi tidak
                        menjamin seluruh mesin telah diuji.
                        """.trimIndent(),
                        "SYSTEM"
                    )
                }

                normalized == "help" ||
                normalized == "bantuan" ||
                normalized.contains("daftar perintah") -> {

                    response(
                        true,
                        """
                        ALGO MIND AI COMMAND CENTER

                        PERINTAH:

                        1. status
                        2. bantuan
                        3. info sistem
                        4. analisis angka: 1234,5678

                        ANALYTICS:
                        Gunakan format:
                        analisis angka: 1234, 5678

                        CLOUD:
                        Memerlukan konfigurasi backend
                        dan API yang valid.

                        DATABASE:
                        Memerlukan koneksi penyimpanan.
                        """.trimIndent(),
                        "AI CORE"
                    )
                }

                normalized.startsWith("analisis angka:") ||
                normalized.startsWith("analisa angka:") -> {

                    val input = text.substringAfter(":")

                    if (input.isBlank()) {
                        response(
                            false,
                            "Masukkan angka setelah tanda titik dua.",
                            "ANALYTICS VALIDATOR"
                        )
                    } else {

                        val result =
                            AnalyticsEngine.analyze(input)

                        response(
                            result.first,
                            result.second,
                            "ANALYTICS ENGINE"
                        )
                    }
                }

                normalized == "info sistem" ||
                normalized == "informasi sistem" -> {

                    response(
                        true,
                        """
                        ALGO MIND AI
                        Foundation Version: 1.0.0
                        Platform: Android
                        Architecture: Modular
                        Performance: Balanced
                        Command Router: Active
                        Cloud: Not Configured
                        Database: Not Connected
                        """.trimIndent(),
                        "SYSTEM INFORMATION"
                    )
                }

                else -> {

                    response(
                        false,
                        """
                        Perintah belum dikenali.

                        Silakan ketik:
                        bantuan

                        Untuk melihat daftar perintah
                        yang tersedia.
                        """.trimIndent(),
                        "COMMAND ROUTER"
                    )
                }
            }

        } catch (e: Exception) {

            response(
                false,
                "Terjadi kesalahan saat menjalankan perintah: ${
                    e.localizedMessage ?: "Unknown error"
                }",
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
