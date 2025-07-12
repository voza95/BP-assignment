package com.oza.bp_assignment.domain.model

data class StationFilters(
    val open24h: Boolean = false,
    val convenienceStore: Boolean = false,
    val bpCard: Boolean = false
)