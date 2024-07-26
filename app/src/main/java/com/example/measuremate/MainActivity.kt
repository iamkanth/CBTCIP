package com.example.measuremate

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.measuremate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.speed_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.speedView1.setOnClickListener {
            val intent = Intent(this, Speed::class.java)
            startActivity(intent)
        }
        binding.areaView.setOnClickListener {
            val intent = Intent(this, Area::class.java)
            startActivity(intent)
        }
        binding.volumeView.setOnClickListener {
            val intent = Intent(this, Volume::class.java)
            startActivity(intent)
        }
        binding.weightView.setOnClickListener {
            val intent = Intent(this, Weight::class.java)
            startActivity(intent)
        }
        binding.lengthView.setOnClickListener {
            val intent = Intent(this, Length::class.java)
            startActivity(intent)
        }
        binding.tempView.setOnClickListener {
            val intent = Intent(this, Temperature::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFrameWithFragment(keyFragment: Fragment) {
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout , keyFragment)
        fragTransaction.commit()
    }
}