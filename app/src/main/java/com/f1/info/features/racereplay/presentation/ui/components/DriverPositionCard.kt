package com.f1.info.features.racereplay.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.f1.info.core.presentation.ui.theme.F1InfoTheme
import com.f1.info.features.racereplay.presentation.model.DriverPosition

@Composable
fun DriverPositionCard(driver: DriverPosition) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = driver.position?.toString() ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(driver.headshotUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${driver.name} headshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = driver.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = driver.teamName,
                style = MaterialTheme.typography.bodySmall,
                color = Color(driver.teamColour.toColorInt())
            )
        }
    }
}

@Preview
@Composable
private fun DriverPositionCardPreview() {
    F1InfoTheme {
        val sampleDriver = DriverPosition(
            number = 44,
            name = "Lewis Hamilton",
            teamName = "Mercedes",
            headshotUrl = null,
            teamColour = "#6CD3BF",
            position = 1
        )
        DriverPositionCard(driver = sampleDriver)
    }
}
