package com.f1.info.features.drivers.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.f1.info.core.domain.model.Driver
import com.f1.info.core.ui.theme.F1InfoTheme

@Composable
fun DriverCard(driver: Driver, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp, 40.dp)
                    .background(color = Color(driver.teamColour.toColorInt()))
            )
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                model = driver.headshotUrl,
                contentDescription = driver.fullName,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = driver.fullName, style = MaterialTheme.typography.bodyLarge)
                Text(text = driver.teamName, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = driver.number.toString(), style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DriverCardPreview() {
    F1InfoTheme {
        DriverCard(
            driver = Driver(
                fullName = "Max VERSTAPPEN",
                number = 1,
                teamName = "Red Bull Racing",
                headshotUrl = "https://www.formula1.com/content/dam/fom-website/drivers/M/MAXVER01_Max_Verstappen/maxver01.png.transform/1col/image.png",
                teamColour = "#3671C6",
                broadcastName = "M VERSTAPPEN",
                firstName = "Max",
                lastName = "Verstappen"
            )
        )
    }
}
