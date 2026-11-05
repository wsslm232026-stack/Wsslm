package com.algomind.ai.core

data class AiModule(
    val id: String,
    val name: String,
    val description: String,
    val status: String
)

object ModuleRegistry {

    val modules = listOf(

        AiModule(
            "core",
            "AI CORE",
            "Pusat kendali kecerdasan buatan",
            "ACTIVE"
        ),

        AiModule(
            "cloud",
            "CLOUD ENGINE",
            "Integrasi model AI cloud",
            "NOT CONFIGURED"
        ),

        AiModule(
            "data",
            "DATA ENGINE",
            "Pengumpulan dan validasi data",
            "FOUNDATION"
        ),

        AiModule(
            "analytics",
            "ANALYTICS ENGINE",
            "Statistik dan analisis pola",
            "ACTIVE"
        ),

        AiModule(
            "learning",
            "LEARNING ENGINE",
            "Evaluasi model dan pembelajaran",
            "FOUNDATION"
        ),

        AiModule(
            "automation",
            "AUTOMATION ENGINE",
            "Otomatisasi pekerjaan",
            "FOUNDATION"
        ),

        AiModule(
            "security",
            "SECURITY ENGINE",
            "Pengamanan akses dan data",
            "FOUNDATION"
        ),

        AiModule(
            "connector",
            "API CONNECTOR",
            "Koneksi layanan eksternal",
            "FOUNDATION"
        ),

        AiModule(
            "report",
            "REPORT ENGINE",
            "Pembuatan laporan",
            "FOUNDATION"
        )
    )
}
