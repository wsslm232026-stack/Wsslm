
package com.algomind.ai.engine

import java.util.Locale

object AnalyticsEngine {

    fun analyze(input: String): Pair<Boolean, String> {

        val numbers = input
            .trim()
            .split(Regex("[,;\\s]+"))
            .filter { it.isNotBlank() }

        if (numbers.isEmpty()) {
            return false to "Data tidak ditemukan."
        }

        if (numbers.any { number ->
                number.length != 4 ||
                !number.all { it in '0'..'9' }
            }) {
            return false to
                "Semua data harus terdiri dari 4 digit angka."
        }

        // Frekuensi seluruh digit
        val frequency = (0..9).associateWith { 0 }
            .toMutableMap()

        numbers.forEach { number ->
            number.forEach { digit ->
                frequency[digit.digitToInt()] =
                    frequency[digit.digitToInt()]!! + 1
            }
        }

        val totalDigits = numbers.size * 4

        // Frekuensi berdasarkan posisi
        val positions = listOf(
            "Ribuan",
            "Ratusan",
            "Puluhan",
            "Satuan"
        )

        val positionReport = positions.mapIndexed { index, name ->

            val counts = (0..9).associateWith { digit ->
                numbers.count {
                    it[index].digitToInt() == digit
                }
            }

            val report = counts
                .filter { it.value > 0 }
                .entries
                .joinToString(", ") {
                    "${it.key}=${it.value}"
                }

            "$name: $report"
        }.joinToString("\n")

        // Persentase digit
        val frequencyReport = frequency.entries
            .joinToString("\n") { entry ->

                val percentage =
                    entry.value * 100.0 / totalDigits

                val formatted = String.format(
                    Locale.ROOT,
                    "%.2f",
                    percentage
                )

                "Digit ${entry.key}: ${entry.value} kali ($formatted%)"
            }

        // Statistik data
        val unique = numbers.distinct().size
        val duplicateCount = numbers.size - unique

        val duplicates = numbers
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }

        val duplicateReport = if (duplicates.isEmpty()) {
            "Tidak ditemukan"
        } else {
            duplicates.entries.joinToString("\n") {
                "${it.key}: ${it.value} kali"
            }
        }

        // Distribusi digit genap dan ganjil
        val even = numbers.sumOf { number ->
            number.count {
                it.digitToInt() % 2 == 0
            }
        }

        val odd = totalDigits - even

        return true to """
            ALGO MIND AI
            ANALYTICS ENGINE 2.0

            === RINGKASAN DATA ===

            Jumlah data: ${numbers.size}
            Data unik: $unique
            Jumlah duplikasi: $duplicateCount
            Total digit: $totalDigits

            === FREKUENSI DIGIT ===

            $frequencyReport

            === ANALISIS POSISI ===

            $positionReport

            === GENAP DAN GANJIL ===

            Genap: $even
            Ganjil: $odd

            === DATA DUPLIKAT ===

            $duplicateReport

            === CATATAN ANALISIS ===

            Analisis berdasarkan data input.
            Frekuensi historis bukan jaminan
            atau prediksi pasti hasil berikutnya.

        """.trimIndent()
    }
}
