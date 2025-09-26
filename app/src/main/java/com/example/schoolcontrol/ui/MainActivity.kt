package com.example.schoolcontrol.ui
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolcontrol.R
import com.example.schoolcontrol.ui.fragments.CorreosFragment
import com.example.schoolcontrol.ui.fragments.NotasFragment
import com.example.schoolcontrol.ui.fragments.RankingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        openFragment(NotasFragment())
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_correos -> openFragment(CorreosFragment())
                R.id.nav_notas -> openFragment(NotasFragment())
                R.id.nav_ranking -> openFragment(RankingFragment())
                R.id.nav_perfil -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }
    }
    private fun openFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
