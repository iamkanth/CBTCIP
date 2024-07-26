package com.example.measuremate

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.measuremate.databinding.ActivityLengthBinding

class Length : AppCompatActivity(), KeyListener {
    private lateinit var binding: ActivityLengthBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var textViewPad: TextViewPad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLengthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = findViewById(R.id.length_spinner)
        val spinnerList = listOf("Kilometre", "Meter", "Centimeter", "Millimeter", "Inch", "Foot", "Yard", "Mile")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner2 = findViewById(R.id.length_spinner2)
        val spinnerList2 = listOf("Kilometre", "Meter", "Centimeter", "Millimeter", "Inch", "Foot", "Yard", "Mile")
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList2)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = arrayAdapter2

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.length_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            replaceFrameWithFragment(KeyFragment())
            replaceEditTextFrameWithFragment(TextViewPad().also { textViewPad = it })
        }

        spinner.onItemSelectedListener = ConversionListener()
        spinner2.onItemSelectedListener = ConversionListener()
    }

    private fun replaceFrameWithFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.length_frameLayout, fragment)
            .commit()
    }

    private fun replaceEditTextFrameWithFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.length_text_frameLayout, fragment)
            .commit()
    }

    private inner class ConversionListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            performConversion()
        }

        override fun onNothingSelected(parent: AdapterView<*>) {
            // Do nothing
        }
    }

    private fun performConversion() {
        val fromUnit = spinner.selectedItem.toString()
        val toUnit = spinner2.selectedItem.toString()
        val inputValue = textViewPad.currentText.toString().toDoubleOrNull()

        if (inputValue != null) {
            val result = convertUnits(inputValue, fromUnit, toUnit)
            binding.lengthResult.text = result.toString()
        } else {
            binding.lengthResult.text = ""
        }
    }

    private fun convertUnits(value: Double, fromUnit: String, toUnit: String): Double {
        // Conversion rates to meters
        val conversionRates = mapOf(
            "Kilometre" to 1000.0,
            "Meter" to 1.0,
            "Centimeter" to 0.01,
            "Millimeter" to 0.001,
            "Inch" to 0.0254,
            "Foot" to 0.3048,
            "Yard" to 0.9144,
            "Mile" to 1609.34
        )

        val valueInMeters = value * (conversionRates[fromUnit] ?: 1.0)
        return valueInMeters / (conversionRates[toUnit] ?: 1.0)
    }

    override fun onKeyPressed(text: String) {
        if (::textViewPad.isInitialized) {
            textViewPad.updateText(
                when (text) {
                    getString(R.string.ac) -> "AC"
                    getString(R.string.delete_sign) -> "DEL"
                    else -> text
                }
            )
            performConversion() // Perform conversion whenever text changes
        }
    }
}
