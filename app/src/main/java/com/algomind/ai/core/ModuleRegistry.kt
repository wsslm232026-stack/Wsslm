
package com.algomind.ai.core

data class AiModule(
    val id: String,
    val name: String,
    val description: String,
    val status: String
)

object ModuleRegistry {

    const val VERSION = "2.1.0"

    val modules = listOf(
        AiModule("core", "MASTER AUTHORITY CENTER",
            "Pusat perintah utama dan pelaksanaan tugas yang aman.", "ACTIVE"),

        AiModule("cloud", "CLOUD AI ENGINE",
            "Koneksi model AI melalui backend dan API resmi.", "NOT CONFIGURED"),

        AiModule("data", "DATA ENGINE",
            "Pengumpulan, penyimpanan, dan validasi sumber data.", "FOUNDATION"),

        AiModule("analytics", "ANALYTICS ENGINE",
            "Statistik dan analisis historis.", "ACTIVE"),

        AiModule("learning", "LEARNING & BACKTEST ENGINE",
            "Evaluasi model menggunakan data historis dan metrik terukur.", "FOUNDATION"),

        AiModule("automation", "AUTOMATION ENGINE",
            "Tugas otomatis dengan log aktivitas dan kontrol jeda.", "FOUNDATION"),

        AiModule("security", "SECURITY ENGINE",
            "Perlindungan aplikasi, PIN, dan konfirmasi tindakan sensitif.", "FOUNDATION"),

        AiModule("connector", "INTERNET & API CONNECTOR",
            "Koneksi situs dan API yang dapat diakses secara sah.", "NOT CONFIGURED"),

        AiModule("network", "NETWORK & SIGNAL MONITOR",
            "Pemantauan Wi-Fi, jaringan seluler, dan kualitas koneksi.", "FOUNDATION"),

        AiModule("device", "DEVICE MANAGER",
            "Pengelolaan perangkat yang didaftarkan dengan izin pemilik.", "FOUNDATION"),

        AiModule("market", "STOCK MARKET MONITOR",
            "Pemantauan pasar dan integrasi broker resmi.", "FOUNDATION"),

        AiModule("cambodia", "CAMBODIA POOLS DATA",
            "Arsip Cambodia Pools terpisah dari operator lainnya.", "FOUNDATION"),

        AiModule("visual", "VISUAL COMMAND CENTER",
            "Tampilan progres, sumber, hasil, dan kesalahan proses.", "FOUNDATION"),

        AiModule("report", "REPORT ENGINE",
            "Laporan aktivitas berdasarkan data yang tersedia.", "FOUNDATION")
    )

    fun getModule(id: String): AiModule? =
        modules.find { it.id == id }

    fun getActiveModules(): List<AiModule> =
        modules.filter { it.status == "ACTIVE" }

    fun getModuleCount(): Int = modules.size
}
