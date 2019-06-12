package com.springer.patryk.korkidajmi.model.extensions

import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView

fun TextInputLayout.hideErrorsOnEditTextChanged(editText: EditText) {
	editText.addTextChangedListener(object : TextWatcher {
		override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
		}

		override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
		}

		override fun afterTextChanged(s: Editable?) {
			error = null
		}
	})
}

fun EditText.getStringValue(): String = this.text.toString()

fun EditText.getFloatValue(): Float = this.getStringValue().toFloatOrNull() ?: 0f
fun EditText.getIntValue(): Int = this.getStringValue().toIntOrNull() ?: 0
fun View.setVisibility(isVisible: Boolean) {
	visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun TextView.setUnderLineText(text: String) {
	val content = SpannableString(text)
	content.setSpan(UnderlineSpan(), 0, text.length, 0)
	setText(content)
}