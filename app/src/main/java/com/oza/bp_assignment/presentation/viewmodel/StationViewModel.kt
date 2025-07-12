package com.oza.bp_assignment.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oza.bp_assignment.data.repository.StationRepository
import com.oza.bp_assignment.domain.model.Station
import com.oza.bp_assignment.domain.model.StationFilters
import kotlinx.coroutines.launch

class StationViewModel : ViewModel() {
    private val repository = StationRepository()

    var uiState by mutableStateOf(StationUiState())
        private set

    init {
        // Mock current location (London coordinates)
        updateLocation(51.5074, -0.1278)
    }

    fun updateLocation(lat: Double, lon: Double) {
        uiState = uiState.copy(
            userLat = lat,
            userLon = lon
        )
        loadStations()
    }

    fun setRadius(radius: Double) {
        uiState = uiState.copy(radiusMiles = radius)
        loadStations()
    }

    fun updateFilter(filterType: String, value: Boolean) {
        uiState = uiState.copy(filters = when (filterType) {
            "24h" -> uiState.filters.copy(open24h = value)
            "store" -> uiState.filters.copy(convenienceStore = value)
            "card" -> uiState.filters.copy(bpCard = value)
            else -> uiState.filters
        })
        loadStations()
    }

    private fun loadStations() {
        val currentState = uiState
        uiState = currentState.copy(isLoading = true)

        viewModelScope.launch {
            val stations = repository.getStations(
                filters = currentState.filters,
                userLat = currentState.userLat,
                userLon = currentState.userLon,
                radiusMiles = currentState.radiusMiles
            )

            uiState = currentState.copy(
                stations = stations,
                isLoading = false
            )
        }
    }
}

data class StationUiState(
    val stations: List<Station> = emptyList(),
    val userLat: Double = 0.0,
    val userLon: Double = 0.0,
    val radiusMiles: Double = 1.0,
    val filters: StationFilters = StationFilters(),
    val isLoading: Boolean = false
)
