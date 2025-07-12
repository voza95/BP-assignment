package com.oza.bp_assignment.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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