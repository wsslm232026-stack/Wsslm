package com.algomind.ai.core

data class AiModule(
    val id: String,
    val name: String,
    val description: String,
    val status: String
)

object ModuleRegistry {

    const val VERSION = "2.0.0"

    val modules = listOf(
        AiModule("core", "AI CORE", "Pusat kendali dan pengelolaan perintah AI.", "ACTIVE"),
        AiModule("cloud", "CLOUD ENGINE", "Integrasi model AI cloud melalui backend API.", "NOT CONFIGURED"),
        AiModule("data", "DATA ENGINE", "Pengumpulan, penyimpanan, dan validasi data.", "FOUNDATION"),
        AiModule("analytics", "ANALYTICS ENGINE", "Statistik, distribusi angka, dan analisis historis.", "ACTIVE"),
        AiModule("learning", "LEARNING ENGINE", "Evaluasi model dan pembelajaran berbasis data.", "FOUNDATION"),
        AiModule("automation", "AUTOMATION ENGINE", "Pengelolaan proses dan pekerjaan otomatis.", "FOUNDATION"),
        AiModule("security", "SECURITY ENGINE", "Pengamanan akses, konfigurasi, dan data.", "FOUNDATION"),
        AiModule("connector", "API CONNECTOR", "Koneksi API dan layanan eksternal.", "NOT CONFIGURED"),
        AiModule("report", "REPORT ENGINE", "Pembuatan laporan dan ringkasan analisis.", "FOUNDATION")
    )

    fun getModule(id: String): AiModule? =
        modules.find { it.id == id }

    fun getActiveModules(): List<AiModule> =
        modules.filter { it.status == "ACTIVE" }

    fun getModuleCount(): Int = modules.size
}
