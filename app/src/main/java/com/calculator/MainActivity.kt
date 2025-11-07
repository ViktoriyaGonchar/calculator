package com.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operation by remember { mutableStateOf<String?>(null) }
    var waitingForNewOperand by remember { mutableStateOf(false) }

    fun performCalculation(op1: Double, op2: Double, op: String): Double {
        return when (op) {
            "+" -> op1 + op2
            "-" -> op1 - op2
            "*" -> op1 * op2
            "/" -> if (op2 != 0.0) op1 / op2 else Double.NaN
            else -> op2
        }
    }

    fun formatResult(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }

    fun inputNumber(number: String) {
        if (waitingForNewOperand) {
            display = number
            waitingForNewOperand = false
        } else {
            display = if (display == "0") number else display + number
        }
    }

    fun inputOperation(op: String) {
        val inputValue = display.toDoubleOrNull()
        if (inputValue != null) {
            if (operand1 == null) {
                operand1 = inputValue
            } else if (operation != null) {
                operand1 = performCalculation(operand1!!, inputValue, operation!!)
                display = formatResult(operand1!!)
            }
            operation = op
            waitingForNewOperand = true
        }
    }

    fun calculate() {
        val inputValue = display.toDoubleOrNull()
        if (operand1 != null && operation != null && inputValue != null) {
            val result = performCalculation(operand1!!, inputValue, operation!!)
            display = if (result.isNaN()) "Ошибка" else formatResult(result)
            operand1 = null
            operation = null
            waitingForNewOperand = true
        }
    }

    fun clear() {
        display = "0"
        operand1 = null
        operation = null
        waitingForNewOperand = false
    }

    fun inputDecimal() {
        if (waitingForNewOperand) {
            display = "0."
            waitingForNewOperand = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Дисплей
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = display,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }

        // Кнопки
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Первая строка: C, /, *, -
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "C",
                    modifier = Modifier.weight(1f),
                    onClick = { clear() },
                    backgroundColor = MaterialTheme.colorScheme.errorContainer
                )
                CalculatorButton(
                    text = "/",
                    modifier = Modifier.weight(1f),
                    onClick = { inputOperation("/") },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
                CalculatorButton(
                    text = "*",
                    modifier = Modifier.weight(1f),
                    onClick = { inputOperation("*") },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
                CalculatorButton(
                    text = "-",
                    modifier = Modifier.weight(1f),
                    onClick = { inputOperation("-") },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            // Вторая строка: 7, 8, 9, +
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "7",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("7") }
                )
                CalculatorButton(
                    text = "8",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("8") }
                )
                CalculatorButton(
                    text = "9",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("9") }
                )
                CalculatorButton(
                    text = "+",
                    modifier = Modifier.weight(1f),
                    onClick = { inputOperation("+") },
                    backgroundColor = MaterialTheme.colorScheme.primaryContainer
                )
            }

            // Третья строка: 4, 5, 6, =
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "4",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("4") }
                )
                CalculatorButton(
                    text = "5",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("5") }
                )
                CalculatorButton(
                    text = "6",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("6") }
                )
                CalculatorButton(
                    text = "=",
                    modifier = Modifier.weight(1f),
                    onClick = { calculate() },
                    backgroundColor = MaterialTheme.colorScheme.primary
                )
            }

            // Четвертая строка: 1, 2, 3, .
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "1",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("1") }
                )
                CalculatorButton(
                    text = "2",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("2") }
                )
                CalculatorButton(
                    text = "3",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("3") }
                )
                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f),
                    onClick = { inputDecimal() }
                )
            }

            // Пятая строка: 0 (широкая кнопка)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(1f),
                    onClick = { inputNumber("0") }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
    Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (backgroundColor == MaterialTheme.colorScheme.primary ||
                backgroundColor == MaterialTheme.colorScheme.errorContainer ||
                backgroundColor == MaterialTheme.colorScheme.primaryContainer
            ) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorTheme {
        CalculatorScreen()
    }
}