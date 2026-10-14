package com.example.casio.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.objecthunter.exp4j.ExpressionBuilder

data class CalculatorState(
    val display: String = "0",
    val expression: String = ""
)

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    private var left: Long? = null
    private var operator: String? = null
    private var startNewOperand = true

    fun onButton(label: String) {
        when (label) {
            "+", "-", "x", "/" -> onOperator(label)
            "=" -> onEquals()
            "CE" -> onClearEntry()
            "C" -> clear()
            "BS" -> onBackspace()
            "+/-" -> onNegate()
            else -> if (label.all { it.isDigit() }) onDigit(label)
        }
    }

    private fun onClearEntry() {
        if (state.display == ERROR) return clear()
        state = state.copy(display = "0")
        startNewOperand = true
    }

    private fun onDigit(digit: String) {
        if (state.display == ERROR) clear()
        val current = if (startNewOperand || state.display == "0") "" else state.display
        if (current.trimStart('-').length >= MAX_DIGITS) return
        state = state.copy(display = current + digit)
        startNewOperand = false
    }

    private fun onOperator(op: String) {
        if (state.display == ERROR) return
        val pendingOp = operator
        val pendingLeft = left
        if (pendingOp != null && pendingLeft != null && !startNewOperand) {
            val result = calculate(pendingLeft, pendingOp, state.display.toLong()) ?: return
            state = state.copy(display = result.toString())
        }
        left = state.display.toLong()
        operator = op
        startNewOperand = true
        state = state.copy(expression = "${state.display} $op")
    }

    private fun onEquals() {
        val pendingOp = operator ?: return
        val pendingLeft = left ?: return
        val right = state.display.toLong()
        val result = calculate(pendingLeft, pendingOp, right) ?: return
        state = CalculatorState(display = result.toString(), expression = "$pendingLeft $pendingOp $right =")
        left = null
        operator = null
        startNewOperand = true
    }

    private fun onBackspace() {
        if (startNewOperand || state.display == ERROR) return
        val trimmed = state.display.dropLast(1)
        state = state.copy(display = if (trimmed.isEmpty() || trimmed == "-") "0" else trimmed)
    }

    private fun onNegate() {
        if (state.display == "0" || state.display == ERROR) return
        val display = state.display
        state = state.copy(display = if (display.startsWith("-")) display.drop(1) else "-$display")
    }

    private fun calculate(a: Long, op: String, b: Long): Long? {
        val symbol = if (op == "x") "*" else op
        return try {
            ExpressionBuilder("($a) $symbol ($b)").build().evaluate().toLong()
        } catch (e: ArithmeticException) {
            clear()
            state = CalculatorState(display = ERROR)
            null
        }
    }

    private fun clear() {
        state = CalculatorState()
        left = null
        operator = null
        startNewOperand = true
    }

    companion object {
        const val ERROR = "Không thể chia cho 0"
        private const val MAX_DIGITS = 15
    }
}
