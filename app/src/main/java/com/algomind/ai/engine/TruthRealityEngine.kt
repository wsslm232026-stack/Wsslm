
package com.algomind.ai.engine

import java.net.URI
import java.util.Locale

object TruthRealityEngine {

    enum class Status {
        REJECTED,
        UNVERIFIED,
        CORROBORATED,
        CONFLICTING
    }

    data class Evidence(
        val sourceName: String,
        val sourceUrl: String,
        val publisher: String,
        val claim: String,
        val collectedAtMillis: Long,
        val contentExcerpt: String = ""
    )

    data class Assessment(
        val status: Status,
        val acceptedEvidenceCount: Int,
        val rejectedEvidenceCount: Int,
        val independentSourceCount: Int,
        val distinctClaimCount: Int,
        val summary: String
    )

    fun assess(
        evidence: List<Evidence>,
        nowMillis: Long = System.currentTimeMillis()
    ): Assessment {

        if (evidence.isEmpty()) {
            return result(
                Status.UNVERIFIED, 0, 0, 0, 0,
                "Belum ada bukti sumber."
            )
        }

        val accepted = evidence.filter { item ->
            val host = hostOf(item.sourceUrl)

            item.sourceName.isNotBlank() &&
            item.publisher.isNotBlank() &&
            item.claim.isNotBlank() &&
            item.contentExcerpt.isNotBlank() &&
            host != null &&
            item.collectedAtMillis > 0L &&
            item.collectedAtMillis <= nowMillis
        }

        val rejected = evidence.size - accepted.size

        if (accepted.isEmpty()) {
            return result(
                Status.REJECTED, 0, rejected, 0, 0,
                "Semua bukti gagal pemeriksaan dasar."
            )
        }

        val hosts = accepted
            .mapNotNull { hostOf(it.sourceUrl) }
            .distinct()

        val claims = accepted
            .map { normalize(it.claim) }
            .distinct()

        return when {
            claims.size > 1 -> result(
                Status.CONFLICTING,
                accepted.size,
                rejected,
                hosts.size,
                claims.size,
                "Sumber memberikan informasi berbeda."
            )

            hosts.size >= 2 -> result(
                Status.CORROBORATED,
                accepted.size,
                rejected,
                hosts.size,
                claims.size,
                "Klaim sama ditemukan pada beberapa host."
            )

            else -> result(
                Status.UNVERIFIED,
                accepted.size,
                rejected,
                hosts.size,
                claims.size,
                "Bukti hanya berasal dari satu host."
            )
        }
    }

    private fun hostOf(url: String): String? {
        return try {
            val uri = URI(url.trim())

            if (uri.scheme.equals(
                    "https",
                    ignoreCase = true
                )
            ) {
                uri.host
                    ?.lowercase(Locale.ROOT)
                    ?.removePrefix("www.")
            } else null

        } catch (_: Exception) {
            null
        }
    }

    private fun normalize(value: String): String =
        value.trim()
            .lowercase(Locale.ROOT)
            .replace(Regex("\\s+"), " ")

    private fun result(
        status: Status,
        accepted: Int,
        rejected: Int,
        sources: Int,
        claims: Int,
        summary: String
    ) = Assessment(
        status,
        accepted,
        rejected,
        sources,
        claims,
        summary
    )
}
