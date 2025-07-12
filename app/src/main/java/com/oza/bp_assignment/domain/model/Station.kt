package com.oza.bp_assignment.domain.model

data class Station(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val isOpen24h: Boolean = false,
    val hasConvenienceStore: Boolean = false,
    val acceptsBpCard: Boolean = false
)