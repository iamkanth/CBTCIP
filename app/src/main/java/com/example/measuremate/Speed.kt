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
import com.example.measuremate.databinding.ActivitySpeedBinding

class Speed : AppCompatActivity(), KeyListener {
    private lateinit var binding: ActivitySpeedBinding
    private lateinit var spinner: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var textViewPad: TextViewPad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySpeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = findViewById(R.id.speed_spinner)
        val spinnerList = listOf("m/s", "km/h", "m/min", "km/min")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        spinner2 = findViewById(R.id.speed_spinner2)
        val spinnerList2 = listOf("m/s", "km/h", "m/min", "km/min")
        val arrayAdapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList2)
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = arrayAdapter2

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.speed_main)) { v, insets ->
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
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    private fun replaceEditTextFrameWithFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.speed_text_frameLayout, fragment)
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
            binding.speedResult.text = result.toString()
        } else {
            binding.speedResult.text = ""
        }
    }

    private fun convertUnits(value: Double, fromUnit: String, toUnit: String): Double {
        // Conversion rates to m/s
        val conversionRates = mapOf(
            "m/s" to 1.0,
            "km/h" to 1000.0 / 3600.0,
            "m/min" to 1.0 / 60.0,
            "km/min" to 1000.0 / 60.0
        )

        val valueInMetersPerSecond = value * (conversionRates[fromUnit] ?: 1.0)
        return valueInMetersPerSecond / (conversionRates[toUnit] ?: 1.0)
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
