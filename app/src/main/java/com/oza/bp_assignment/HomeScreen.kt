package com.oza.bp_assignment

import android.service.autofill.OnClickAction
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oza.bp_assignment.model.Station

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: StationViewModel = viewModel()) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = { TopAppBar(title = { Text("BP Stations Finder") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Radius Selector
            RadiusSelector(
                selectedRadius = uiState.radiusMiles,
                onRadiusSelected = viewModel::setRadius
            )

            // Filters
            FilterOptions(
                filters = uiState.filters,
                onFilterChanged = viewModel::updateFilter
            )

            // Station List
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            } else if (uiState.stations.isEmpty()) {
                Text(
                    "No stations found",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            } else {
                StationList(stations = uiState.stations)
            }
        }
    }
}

@Composable
fun RadiusSelector(selectedRadius: Double, onRadiusSelected: (Double) -> Unit) {
    val radii = listOf(0.5, 1.0, 5.0)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        radii.forEach { radius ->
            FilterChip(
                selected = (selectedRadius == radius),
                onClick = { onRadiusSelected(radius) },
                label = { Text("${radius} mi") }
            )
        }
    }
}

@Composable
fun ExpandableFilter(modifier: Modifier = Modifier) {

}

@Composable
fun FilterOptions(
    filters: StationFilters,
    onFilterChanged: (String, Boolean) -> Unit
) {
    var expandableFilterMenu by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandableFilterMenu) 180f else 0f
    )

    Card (
        modifier = Modifier.padding(12.dp).animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            ),
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(6f).clickable(true, onClick = {
                    expandableFilterMenu = !expandableFilterMenu
                }),text = "Filters", style = MaterialTheme.typography.titleMedium)

                IconButton(
                    modifier = Modifier.weight(1f).rotate(rotationState),
                    onClick = {
                    expandableFilterMenu = !expandableFilterMenu
                }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Drop-down arrow")
                }
            }

            if (expandableFilterMenu) {
                FilterItem(
                    label = "Open 24 Hours",
                    checked = filters.open24h,
                    onCheckedChange = { onFilterChanged("24h", it) }
                )
                FilterItem(
                    label = "Convenience Store",
                    checked = filters.convenienceStore,
                    onCheckedChange = { onFilterChanged("store", it) }
                )
                FilterItem(
                    label = "Serves Hot Food",
                    checked = filters.hotFood,
                    onCheckedChange = { onFilterChanged("food", it) }
                )
                FilterItem(
                    label = "Accepts BP Card",
                    checked = filters.bpCard,
                    onCheckedChange = { onFilterChanged("card", it) }
                )
            }
        }
    }
}

@Composable
fun FilterItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null, // We handle the change through the row click
            modifier = Modifier
        )
        Text(label, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
fun StationList(stations: List<Station>) {
    LazyColumn {
        items(stations) { station ->
            StationItem(station = station)
        }
    }
}

@Composable
fun StationItem(station: Station) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(station.name, style = MaterialTheme.typography.titleMedium)
            Text(station.address, style = MaterialTheme.typography.bodyMedium)

            Row(modifier = Modifier.padding(top = 8.dp)) {
                if (station.isOpen24h) Badge("24h")
                if (station.hasConvenienceStore) Badge("Store")
                if (station.servesHotFood) Badge("Hot Food")
                if (station.acceptsBpCard) Badge("BP Card")
            }
        }
    }
}

@Composable
fun Badge(text: String) {
    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}