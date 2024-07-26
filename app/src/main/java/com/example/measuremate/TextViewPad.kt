package com.example.measuremate

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class TextViewPad : Fragment(R.layout.keypad_background) {
    private lateinit var textViewPad: TextView
    val currentText = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewPad = view.findViewById(R.id.textViewPad)
    }

    fun updateText(text: String) {
        when (text) {
            "AC" -> currentText.clear()
            "DEL" -> {
                if (currentText.isNotEmpty()) {
                    currentText.deleteCharAt(currentText.length - 1)
                }
            }
            else -> currentText.append(text)
        }
        textViewPad.text = currentText.toString()
    }

    fun getText(): String {
        return currentText.toString()
    }
}
