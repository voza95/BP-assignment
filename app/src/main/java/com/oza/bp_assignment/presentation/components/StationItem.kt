package com.oza.bp_assignment.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oza.bp_assignment.R
import com.oza.bp_assignment.domain.model.Station
import com.oza.bp_assignment.presentation.screen.Badge

@Composable
fun StationItem(station: Station) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.bp_logo),
                contentDescription = "BP Logo",
                modifier = Modifier.size(60.dp).padding(end = 10.dp),
                alignment = Alignment.Center)
            Column {
                Text(station.name, style = MaterialTheme.typography.titleMedium)
                Text(station.address, style = MaterialTheme.typography.bodyMedium)

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    if (station.isOpen24h) Badge("24h")
                    if (station.hasConvenienceStore) Badge("Store")
                    if (station.acceptsBpCard) Badge("BP Card")
                }
            }
        }
    }
}