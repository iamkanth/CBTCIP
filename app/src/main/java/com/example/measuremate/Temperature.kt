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
import com.example.measuremate.databinding.ActivityTemperatureBinding

class Temperature : AppCompatActivity(), KeyListener {
    private lateinit var binding: ActivityTemperatureBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var textViewPad: TextViewPad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTemperatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = findViewById(R.id.temp_spinner)
        val spinnerList = listOf("Celsius", "Fahrenheit", "Kelvin")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner2 = findViewById(R.id.temp_spinner2)
        val spinnerList2 = listOf("Celsius", "Fahrenheit", "Kelvin")
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList2)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = arrayAdapter2

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.temp_main)) { v, insets ->
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
            .replace(R.id.temp_frameLayout, fragment)
            .commit()
    }

    private fun replaceEditTextFrameWithFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.temp_text_frameLayout, fragment)
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
            binding.tempResult.text = result.toString()
        } else {
            binding.tempResult.text = ""
        }
    }

    private fun convertUnits(value: Double, fromUnit: String, toUnit: String): Double {
        // Conversion formulas
        return when (fromUnit) {
            "Celsius" -> when (toUnit) {
                "Fahrenheit" -> value * 9/5 + 32
                "Kelvin" -> value + 273.15
                else -> value
            }
            "Fahrenheit" -> when (toUnit) {
                "Celsius" -> (value - 32) * 5/9
                "Kelvin" -> (value - 32) * 5/9 + 273.15
                else -> value
            }
            "Kelvin" -> when (toUnit) {
                "Celsius" -> value - 273.15
                "Fahrenheit" -> (value - 273.15) * 9/5 + 32
                else -> value
            }
            else -> value
        }
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
