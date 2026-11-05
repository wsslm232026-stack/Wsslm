
package com.algomind.ai.core

import com.algomind.ai.data.AiResponse
import com.algomind.ai.engine.AnalyticsEngine
import java.util.Locale

object AiCommandRouter {

    private val aliases = mapOf(
        "alfa" to "bantuan",
        "omega" to "status",
        "nova" to "analisis bantuan",
        "titan" to "keamanan",
        "atlas" to "perangkat",
        "orbit" to "jaringan",
        "vega" to "saham",
        "phoenix" to "diagnostik",
        "echo" to "laporan",
        "sleep" to "jeda"
    )

    fun execute(command: String): AiResponse {
        val text = command.trim()

        if (text.isEmpty()) {
            return response(false, "Perintah kosong.", "VALIDATOR")
        }

        val normalized = text.lowercase(Locale.ROOT)
        val routed = aliases[normalized] ?: normalized

        return try {
            when {
                routed == "bantuan" || routed == "help" ->
                    response(true, """
                        ALGO MIND AI MASTER COMMAND

                        ALFA    : Pusat kendali
                        OMEGA   : Status sistem
                        NOVA    : Analisis angka
                        TITAN   : Keamanan
                        ATLAS   : Pengelolaan perangkat
                        ORBIT   : Jaringan internet
                        VEGA    : Pemantauan saham
                        PHOENIX : Diagnostik aplikasi
                        ECHO    : Laporan
                        SLEEP   : Jeda otomatisasi

                        Analisis:
                        analisis angka: 1234,5678

                        Kode kata bukan PIN keamanan.
                        Tindakan sensitif perlu autentikasi.
                    """.trimIndent(), "MASTER COMMAND")

                routed == "status" ->
                    response(true, """
                        ALGO MIND AI STATUS
                        Router: ACTIVE
                        Master Command: ACTIVE
                        Analytics: TERPASANG
                        Cloud: BELUM DIKONFIGURASI
                        Database: BELUM TERHUBUNG
                        Voice: BELUM DIINTEGRASIKAN
                        PIN: BELUM DIINTEGRASIKAN
                    """.trimIndent(), "SYSTEM")

                routed == "analisis bantuan" ->
                    response(true,
                        "Gunakan: analisis angka: 1234,5678",
                        "ANALYTICS")

                routed.startsWith("analisis angka:") ||
                routed.startsWith("analisa angka:") -> {
                    val input = text.substringAfter(":")
                    val result = AnalyticsEngine.analyze(input)
                    response(result.first, result.second, "ANALYTICS")
                }

                routed == "keamanan" ->
                    response(true,
                        "TITAN: pemeriksaan keamanan untuk perangkat yang diizinkan.",
                        "SECURITY")

                routed == "perangkat" ->
                    response(true,
                        "ATLAS: pengelolaan perangkat dengan izin pemilik.",
                        "DEVICE MANAGER")

                routed == "jaringan" ->
                    response(true,
                        "ORBIT: periksa kartu Internet Signal Monitor.",
                        "NETWORK")

                routed == "saham" ->
                    response(true,
                        "VEGA: pemantauan pasar memerlukan API data resmi.",
                        "MARKET")

                routed == "diagnostik" ->
                    response(true,
                        "PHOENIX: periksa status aplikasi dan log build.",
                        "DIAGNOSTICS")

                routed == "laporan" ->
                    response(true,
                        "ECHO: Master Command dan router tersedia. Integrasi lain masih bertahap.",
                        "REPORT")

                routed == "jeda" ->
                    response(true,
                        "SLEEP: layanan otomatis belum tersedia untuk dihentikan.",
                        "AUTOMATION")

                else ->
                    response(false,
                        "Perintah tidak dikenali. Ketik ALFA.",
                        "COMMAND ROUTER")
            }
        } catch (e: Exception) {
            response(false,
                "Kesalahan: ${e.localizedMessage ?: "Unknown error"}",
                "EXECUTION ERROR")
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
