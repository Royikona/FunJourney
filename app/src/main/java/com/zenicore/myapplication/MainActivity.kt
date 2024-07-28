package com.zenicore.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.zenicore.myapplication.auth.SigninActivity

class MainActivity : AppCompatActivity() {

    private lateinit var bottomMenuView: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            startActivity(Intent(this@MainActivity, SigninActivity::class.java))
            finish()
            return
        }

        bottomMenuView = findViewById(R.id.navigation_bottom)

        bottomMenuView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_trks -> {
                    replaceFragment(GameFragment())
                    true
                }
                R.id.bottom_rewards -> {
                    replaceFragment(RewardFragment())
                    true
                }
                R.id.bottom_profil -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }


}
