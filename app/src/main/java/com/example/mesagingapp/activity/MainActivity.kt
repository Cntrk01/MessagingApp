package com.example.mesagingapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mesagingapp.R
import com.example.mesagingapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)

        val navController = navHostFragment?.findNavController()

        if (navController != null) {
            binding.bottomNav.setupWithNavController(navController)
        }
        navHostFragment?.findNavController()?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment, R.id.homeFragment, R.id.exitFragment ->
                    binding.bottomNav.visibility = View.VISIBLE

                else -> {
                    binding.bottomNav.visibility = View.INVISIBLE
                }

            }
        }
    }


}