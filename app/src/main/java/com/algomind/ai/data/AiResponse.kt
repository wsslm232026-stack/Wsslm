
package com.algomind.ai.data

data class AiResponse(
    val success: Boolean,
    val message: String,
    val source: String,
    val timestamp: Long = System.currentTimeMillis()
)
