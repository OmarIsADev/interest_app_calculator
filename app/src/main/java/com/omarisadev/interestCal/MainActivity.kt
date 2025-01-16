package com.omarisadev.interestCal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omarisadev.interestCal.ui.theme.InterestCalTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InterestCalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier
            .fillMaxSize()
            .padding(32.dp, 64.dp)
    ) {
        Title()

        Form()
    }

}

@Composable
fun Title() {
    Text(
        text = "Bank interest Calculator",
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
fun Form() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        val intialSalary = input("Intial Salary")
        val rate = input("Interest Rate")

        Text("Duration")
        val monthsDuration = input("Months")
        val yearsDuration = input("Years")

        val compound = optionList()

//        ExposedDropdownMenuBox(
//            expanded = false,
//            onExpandedChange = {}
//        ) {
//            OutlinedTextField(
//                label = { Text("Rate duration") },
//                value = duration,
//                onValueChange = { duration = it }
//            )
//        }
//
//        Button(
//            onClick = { },
//            modifier = Modifier.fillMaxWidth(),
//        ) {
//            Text(stringResource(R.string.calc), textAlign = TextAlign.Center, maxLines = 1)
//        }

        HorizontalDivider()

        val duration: Double = ((monthsDuration / 12.0) + yearsDuration)
        val salary: Double = calculateInterest(intialSalary, rate, compound, duration)

        Text(
            text = "${(salary * 100).toInt().toDouble() / 100}$"
        )

    }
}

@Composable
fun input(label: String): Double {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        value = value,
        onValueChange = { value = it },
        modifier = Modifier.fillMaxWidth()
    )

    return if (value == "") {
        0.0
    } else {
        value.toDouble()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun optionList(): String {
    val options = listOf("annually", "semi", "quarterly", "monthly", "daily")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column {
        Text(
            text = "Choose Compound:",
            style = MaterialTheme.typography.bodyLarge
        )


        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = selectedOption == option,
                    onClick = { selectedOption = option },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    )
                ) {
                    Text(text = option, maxLines = 1)
                }
            }

        }
    }

    return selectedOption
}

fun calculateInterest(
    salary: Double,
    rate: Double,
    compound: String,
    duration: Double
): Double {
    val i = when (compound) {
        "annually" -> 1
        "semi" -> 2
        "quarterly" -> 4
        "monthly" -> 12
        "daily" -> 365
        else -> 1
    }

    val dueRate: Double = i * duration

    return (salary * (1 + (rate / 100 / i)).pow(dueRate))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InterestCalTheme {
        App()
    }
}
