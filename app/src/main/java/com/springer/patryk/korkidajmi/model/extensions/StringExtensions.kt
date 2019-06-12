package com.springer.patryk.korkidajmi.model.extensions

import android.util.Patterns
import java.text.DecimalFormat

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun formatHourPrice(number: Float?): String = formatNumber(number, "zł/h")

fun formatNumber(number: Float?, unit: String): String {
	val decimalFormat = DecimalFormat("###,###,###")
	val formatSymbols = decimalFormat.decimalFormatSymbols
	formatSymbols.groupingSeparator = ' '
	decimalFormat.decimalFormatSymbols = formatSymbols
	number?.let {
		return "${decimalFormat.format(number)} $unit"
	}
	return ""
}

