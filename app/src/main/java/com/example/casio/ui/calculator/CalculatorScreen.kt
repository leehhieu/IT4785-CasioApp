package com.example.casio.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val buttons = listOf(
    listOf("CE", "C", "BS", "/"),
    listOf("7", "8", "9", "x"),
    listOf("4", "5", "6", "-"),
    listOf("1", "2", "3", "+"),
    listOf("+/-", "0", ".", "=")
)

private val DarkKey = Color(0xFF7A7A7A)
private val FunctionKey = Color(0xFF8A8A8A)
private val DigitKey = Color(0xFFCCCCCC)

@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = viewModel()
) {
    val state = viewModel.state
    Column(modifier = modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(text = state.expression, fontSize = 24.sp, color = Color.Gray)
            Text(
                text = state.display,
                fontSize = when {
                    state.display.length > 12 -> 32.sp
                    state.display.length > 8 -> 48.sp
                    else -> 80.sp
                },
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(2f).padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row.forEach { label ->
                        CalculatorButton(
                            label = label,
                            color = keyColor(label),
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.onButton(label) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculatorButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxHeight().background(color).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 36.sp)
    }
}

private fun keyColor(label: String): Color = when {
    label == "CE" -> DarkKey
    label.all { it.isDigit() } -> DigitKey
    else -> FunctionKey
}

@Preview(showBackground = true)
@Composable
fun Screen1Preview() {
    CalculatorScreen()
}
