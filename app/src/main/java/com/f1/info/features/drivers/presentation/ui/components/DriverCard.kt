package com.f1.info.features.drivers.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.info.core.ui.theme.F1InfoTheme
import com.f1.info.features.drivers.domain.model.Driver

@Composable
fun DriverCard(driver: Driver, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = driver.number.toString(), modifier = Modifier.padding(end = 16.dp))
            Text(text = driver.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DriverCardPreview() {
    F1InfoTheme {
        DriverCard(
            driver = Driver(
                id = "max_verstappen",
                name = "Max Verstappen",
                number = 33,
                teamId = "red_bull",
                nationality = "Netherlands"
            )
        )
    }
}
