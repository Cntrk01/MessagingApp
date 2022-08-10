package com.example.mesagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mesagingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)

        val navController = navHostFragment?.findNavController()

        if (navController != null) {
            binding.bottomNav.setupWithNavController(navController)
        }
        navHostFragment?.findNavController()?.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment,R.id.homeFragment ->
                    binding.bottomNav.visibility = View.VISIBLE

                else -> {
                    binding.bottomNav.visibility = View.INVISIBLE
                }

            }
        }

    }
}