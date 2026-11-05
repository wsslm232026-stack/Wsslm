package com.algomind.ai.engine

import java.util.Locale

object AnalyticsEngine {

    fun analyze(input: String): Pair<Boolean, String> {
        val numbers = input.trim()
            .split(Regex("[,;\\s]+"))
            .filter { it.isNotBlank() }

        if (numbers.isEmpty()) {
            return false to "Data tidak ditemukan."
        }

        if (numbers.any {
                it.length != 4 || !it.all { digit -> digit in '0'..'9' }
            }) {
            return false to "Semua data harus terdiri dari 4 digit angka."
        }

        val totalDigits = numbers.size * 4
        val frequency = (0..9).associateWith { 0 }.toMutableMap()

        numbers.forEach { number ->
            number.forEach { digit ->
                val value = digit.digitToInt()
                frequency[value] = frequency.getValue(value) + 1
            }
        }

        val frequencyReport = frequency.entries.joinToString("\n") { entry ->
            val percentage = entry.value * 100.0 / totalDigits
            val formatted = String.format(Locale.ROOT, "%.2f", percentage)
            "Digit ${entry.key}: ${entry.value} kali ($formatted%)"
        }

        val positionNames = listOf("Ribuan", "Ratusan", "Puluhan", "Satuan")
        val positionReport = positionNames.mapIndexed { index, name ->
            val counts = (0..9).mapNotNull { digit ->
                val count = numbers.count { it[index].digitToInt() == digit }
                if (count > 0) "$digit=$count" else null
            }
            "$name: ${counts.joinToString(", ")}"
        }.joinToString("\n")

        val uniqueCount = numbers.distinct().size
        val duplicateCount = numbers.size - uniqueCount
        val duplicates = numbers.groupingBy { it }.eachCount()
            .filter { it.value > 1 }

        val duplicateReport = if (duplicates.isEmpty()) {
            "Tidak ditemukan"
        } else {
            duplicates.entries.joinToString("\n") {
                "${it.key}: ${it.value} kali"
            }
        }

        val even = numbers.sumOf { number ->
            number.count { it.digitToInt() % 2 == 0 }
        }
        val odd = totalDigits - even

        return true to """
            ALGO MIND AI
            ANALYTICS ENGINE 2.0

            === RINGKASAN DATA ===
            Jumlah data: ${numbers.size}
            Data unik: $uniqueCount
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

            === CATATAN ===
            Analisis berdasarkan data input.
            Frekuensi historis bukan jaminan prediksi hasil berikutnya.
        """.trimIndent()
    }
}
