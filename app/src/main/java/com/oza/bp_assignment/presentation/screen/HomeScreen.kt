package com.oza.bp_assignment.presentation.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oza.bp_assignment.domain.model.StationFilters
import com.oza.bp_assignment.presentation.viewmodel.StationViewModel
import com.oza.bp_assignment.domain.model.Station
import com.oza.bp_assignment.R
import com.oza.bp_assignment.presentation.components.FilterItem
import com.oza.bp_assignment.presentation.components.RadiusSelector
import com.oza.bp_assignment.presentation.components.StationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: StationViewModel = viewModel()) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.bp_stations_finder)) }) }
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
                    stringResource(R.string.no_stations_found),
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
fun FilterOptions(
    filters: StationFilters,
    onFilterChanged: (String, Boolean) -> Unit
) {
    var expandableFilterMenu by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandableFilterMenu) 180f else 0f
    )

    Card (
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier
                    .weight(6f)
                    .clickable(true, onClick = {
                        expandableFilterMenu = !expandableFilterMenu
                    }),text = stringResource(R.string.filters), style = MaterialTheme.typography.titleMedium)

                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationState),
                    onClick = {
                    expandableFilterMenu = !expandableFilterMenu
                }) {
                    Icon(
                        painter = painterResource(R.drawable.user_filter),
                        contentDescription = "Filter",
                        modifier = Modifier.size(20.dp))
                }
            }

            if (expandableFilterMenu) {
                FilterItem(
                    label = stringResource(R.string.open_24_hours),
                    checked = filters.open24h,
                    onCheckedChange = { onFilterChanged("24h", it) }
                )
                FilterItem(
                    label = stringResource(R.string.convenience_store),
                    checked = filters.convenienceStore,
                    onCheckedChange = { onFilterChanged("store", it) }
                )
                FilterItem(
                    label = stringResource(R.string.accepts_bp_card),
                    checked = filters.bpCard,
                    onCheckedChange = { onFilterChanged("card", it) }
                )
            }
        }
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