package com.oza.bp_assignment.data.repository

import com.oza.bp_assignment.domain.model.Station
import com.oza.bp_assignment.domain.model.StationFilters
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class StationRepository {
    private val mockStations = listOf(
        Station(
            id = "1",
            name = "BP Central",
            address = "123 Main St, London",
            latitude = 51.5074,
            longitude = -0.1278,
            isOpen24h = true,
            hasConvenienceStore = true,
            acceptsBpCard = true
        ),
        Station(
            id = "2",
            name = "BP Central 2",
            address = "1412 St Road, London",
            latitude = 51.5074,
            longitude = -0.1278,
            isOpen24h = false,
            hasConvenienceStore = true,
            acceptsBpCard = true
        ),
        Station(
            id = "1",
            name = "BP Central 3",
            address = "Random, London",
            latitude = 51.5074,
            longitude = -0.1278,
            isOpen24h = false,
            hasConvenienceStore = true,
            acceptsBpCard = true
        ),
        Station(
            id = "1",
            name = "BP Central 4",
            address = "123 Main St, London",
            latitude = 51.5074,
            longitude = -0.1278,
            isOpen24h = true,
            hasConvenienceStore = true,
            acceptsBpCard = true
        )
        // Add 9-14 more mock stations here with varied properties
    )

    fun getStations(
        filters: StationFilters,
        userLat: Double,
        userLon: Double,
        radiusMiles: Double
    ): List<Station> {
        return mockStations.filter { station ->
            // Apply distance filter
            val distance = calculateDistance(
                userLat, userLon,
                station.latitude, station.longitude
            ) <= radiusMiles

            // Apply boolean filters
            val openFilter = !filters.open24h || station.isOpen24h
            val storeFilter = !filters.convenienceStore || station.hasConvenienceStore
            val cardFilter = !filters.bpCard || station.acceptsBpCard

            distance && openFilter && storeFilter && cardFilter
        }
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 3958.8 // miles
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}