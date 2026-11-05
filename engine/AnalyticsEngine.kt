package com.algomind.ai.engine

object AnalyticsEngine {

    fun analyze(
        input: String
    ): Pair<Boolean, String> {

        val numbers = input
            .split(",", " ", "\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (numbers.isEmpty()) {
            return false to "Data tidak ditemukan."
        }

        if (numbers.any {
            it.length != 4 || !it.all(Char::isDigit)
        }) {
            return false to
                "Semua data harus terdiri dari 4 digit."
        }

        val frequency = mutableMapOf<Char, Int>()

        numbers.forEach { number ->
            number.forEach { digit ->
                frequency[digit] =
                    (frequency[digit] ?: 0) + 1
            }
        }

        val totalDigits = numbers.size * 4

        val distribution = frequency
            .toSortedMap()
            .entries
            .joinToString("\n") {
                "Digit ${it.key}: ${it.value} kali"
            }

        val unique = numbers.distinct().size

        return true to """
            ANALISIS DATA SELESAI

            Jumlah data: ${numbers.size}
            Data unik: $unique
            Data duplikat: ${numbers.size - unique}
            Total digit: $totalDigits

            FREKUENSI DIGIT

            $distribution

            Catatan:
            Frekuensi historis tidak menjamin
            hasil kejadian berikutnya.
        """.trimIndent()
    }
}
