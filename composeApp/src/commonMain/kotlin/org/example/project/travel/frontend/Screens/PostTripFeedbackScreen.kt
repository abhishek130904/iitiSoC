package org.example.project.travel.frontEnd.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.viewmodel.viewModel
import org.example.project.travel.frontEnd.viewModel.CitySearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTripFeedbackScreen(viewModel: CitySearchViewModel<*> = viewModel(
    modelClass = TODO(),
    keys = TODO(),
    creator = TODO()
)) {
    var cities by remember { mutableStateOf("") }
    var activities by remember { mutableStateOf("") }
    var hotels by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0f) }
    var budget by remember { mutableStateOf("") }
    var travelStyle by remember { mutableStateOf(listOf<String>()) }
    var feedback by remember { mutableStateOf("") }
    var consent by remember { mutableStateOf(false) }

    val styles = listOf("solo", "couple", "group", "adventure", "luxury")
    val budgets = listOf("$0-$500", "$501-$1000", "$1001-$2000", "$2000+")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Thanks for traveling with us! Share your experience.", modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = cities,
            onValueChange = { cities = it },
            label = { Text("Cities Visited") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = activities,
            onValueChange = { activities = it },
            label = { Text("Activities Done") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = hotels,
            onValueChange = { hotels = it },
            label = { Text("Hotels Stayed In") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = rating.toString(),
            onValueChange = { rating = it.toFloatOrNull() ?: 0f },
            label = { Text("Rating (1-5)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Budget Range") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            // Add dropdown items for budgets here (simplified for now)
        }

        Column {
            styles.forEach { style ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = travelStyle.contains(style),
                        onCheckedChange = { checked ->
                            travelStyle = if (checked) travelStyle + style else travelStyle - style
                        }
                    )
                    Text(style, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            label = { Text("Additional Feedback") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consent, onCheckedChange = { consent = it })
            Text("I consent to use this data for recommendations.", modifier = Modifier.padding(start = 8.dp))
        }

        Button(
            onClick = {
                if (consent && rating in 1f..5f && cities.isNotEmpty()) {
                    viewModel.submitFeedback(cities, activities, hotels, rating, budget, travelStyle, feedback)
                }
            },
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
            enabled = consent && rating in 1f..5f && cities.isNotEmpty()
        ) {
            Text("Submit")
        }

        // Display error or success message
        viewModel.error.value?.let { error ->
            Text(error, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}