package com.algomind.ai.core

import com.algomind.ai.engine.AnalyticsEngine
import com.algomind.ai.data.AiResponse

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

        val normalized = text.lowercase()

        return when {

            normalized == "status" ||
            normalized.contains("status sistem") -> {

                response(
                    true,
                    "AI Core aktif. Cloud dan database " +
                    "belum dikonfigurasi.",
                    "SYSTEM"
                )
            }

            normalized.contains("bantuan") ||
            normalized == "help" -> {

                response(
                    true,
                    """
                    PERINTAH TERSEDIA:

                    1. status
                    2. bantuan
                    3. analisis angka: 1234,5678
                    4. info sistem

                    Fitur cloud memerlukan backend.
                    """.trimIndent(),
                    "AI CORE"
                )
            }

            normalized.contains("analisis angka:") -> {

                val input = text.substringAfter(
                    ":",
                    ""
                )

                val result =
                    AnalyticsEngine.analyze(input)

                response(
                    result.first,
                    result.second,
                    "ANALYTICS ENGINE"
                )
            }

            normalized.contains("info sistem") -> {

                response(
                    true,
                    "ALGO MIND AI Foundation 1.0.0\n" +
                    "Platform Android\n" +
                    "Performa Balanced\n" +
                    "Cloud: Belum dikonfigurasi\n" +
                    "Database: Belum terhubung",
                    "SYSTEM"
                )
            }

            else -> {

                response(
                    false,
                    "Perintah belum dikenali. " +
                    "Ketik bantuan untuk melihat perintah.",
                    "AI CORE"
                )
            }
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
