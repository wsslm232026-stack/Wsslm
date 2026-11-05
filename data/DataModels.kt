package com.algomind.ai.data

data class AiCommand(
    val id: String,
    val command: String,
    val timestamp: Long
)

data class AiResponse(
    val success: Boolean,
    val message: String,
    val source: String,
    val timestamp: Long
)

data class AnalysisRecord(
    val id: String,
    val source: String,
    val date: String,
    val value: String,
    val verified: Boolean
)

data class SystemStatus(
    val application: String,
    val aiCore: String,
    val cloud: String,
    val database: String
)
