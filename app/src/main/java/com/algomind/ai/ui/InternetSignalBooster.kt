
package com.algomind.ai.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Locale

private data class NetworkStatus(
    val connection: String,
    val internet: String,
    val bandwidth: String,
    val quality: String
)

@Composable
fun InternetSignalBoosterCard() {
    val context = LocalContext.current

    var status by remember {
        mutableStateOf(
            NetworkStatus(
                "Memeriksa...",
                "Belum diperiksa",
                "--",
                "Menganalisis"
            )
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            status = withContext(Dispatchers.IO) {
                readNetworkStatus(context)
            }
            delay(15000)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF17283D)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "INTERNET SIGNAL BOOSTER AI",
                color = Color(0xFF35D5C5),
                fontWeight = FontWeight.Bold
            )

            Text("Jaringan: ${status.connection}", color = Color.White)
            Text("Internet: ${status.internet}", color = Color.White)
            Text("Bandwidth: ${status.bandwidth}", color = Color.White)
            Text("Analisis: ${status.quality}", color = Color.White)

            Text(
                "MODE OTOMATIS • PEMERIKSAAN 15 DETIK",
                color = Color(0xFF35D5C5)
            )

            Text(
                "Pemantauan berjalan selama dashboard terbuka. " +
                "Modul tidak dapat menciptakan kuota gratis atau " +
                "menambah kekuatan radio perangkat.",
                color = Color.LightGray
            )
        }
    }
}

private fun readNetworkStatus(
    context: Context
): NetworkStatus {

    val manager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val network = manager.activeNetwork
        ?: return NetworkStatus(
            "Tidak terhubung",
            "Tidak tersedia",
            "--",
            "Periksa Wi-Fi atau data seluler"
        )

    val caps = manager.getNetworkCapabilities(network)
        ?: return NetworkStatus(
            "Tidak diketahui",
            "Tidak diketahui",
            "--",
            "Periksa koneksi"
        )

    val connection = when {
        caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
            "Wi-Fi"

        caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
            "Data seluler"

        else -> "Jaringan lain"
    }

    val validated = caps.hasCapability(
        NetworkCapabilities.NET_CAPABILITY_VALIDATED
    )

    val internet = if (validated) {
        "Terhubung dan tervalidasi"
    } else {
        "Belum tervalidasi"
    }

    val kbps = caps.linkDownstreamBandwidthKbps

    val bandwidth = if (kbps > 0) {
        String.format(
            Locale.US,
            "%.1f Mbps (estimasi)",
            kbps / 1000.0
        )
    } else {
        "Belum tersedia"
    }

    val quality = when {
        !validated -> "Periksa koneksi atau kuota"
        kbps >= 10000 -> "Kapasitas jaringan baik"
        kbps >= 1000 -> "Kapasitas jaringan sedang"
        kbps > 0 -> "Kapasitas jaringan rendah"
        else -> "Internet aktif"
    }

    return NetworkStatus(
        connection,
        internet,
        bandwidth,
        quality
    )
}
