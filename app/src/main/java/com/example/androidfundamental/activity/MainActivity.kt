package com.example.androidfundamental.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androidfundamental.R
import com.example.androidfundamental.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Mengatur Bottom Navigation
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> { // Mengarahkan ke HomeFragment
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.navigation_active_events -> { // Mengarahkan ke ActiveEventsFragment
                    navController.navigate(R.id.activeEventsFragment)
                    true
                }
                R.id.navigation_past_events -> { // Mengarahkan ke PastEventsFragment
                    navController.navigate(R.id.finishedEventsFragment)
                    true
                }
                else -> false
            }
        }

        // Set fragment default saat aplikasi dibuka
        if (savedInstanceState == null) {
            navController.navigate(R.id.homeFragment) // Ganti dengan fragment yang ingin ditampilkan pertama kali
        }
    }
}
