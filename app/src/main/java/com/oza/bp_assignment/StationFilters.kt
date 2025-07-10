package com.oza.bp_assignment

data class StationFilters(
    val open24h: Boolean = false,
    val convenienceStore: Boolean = false,
    val hotFood: Boolean = false,
    val bpCard: Boolean = false
)