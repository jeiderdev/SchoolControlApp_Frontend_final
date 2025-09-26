package com.example.schoolcontrol.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.schoolcontrol.R
import com.example.schoolcontrol.ui.fragments.StudentSubjectsFragment
import com.example.schoolcontrol.ui.fragments.SubjectsFragment
import com.example.schoolcontrol.ui.fragments.TeacherSubjectsFragment
import com.example.schoolcontrol.ui.fragments.UsersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var userRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // 1️⃣ Obtener el rol del usuario autenticado
        val prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE)
        userRole = prefs.getString("USER_ROLE", null)
        Toast.makeText(this, "Rol: $userRole", Toast.LENGTH_SHORT).show()
        // 2️⃣ Inflar menú según rol
        when (userRole) {
            "ADMIN" -> {
                bottomNav.inflateMenu(R.menu.bottom_menu_admin)
                openFragment(UsersFragment()) // Default
            }
            "TEACHER" -> {
                bottomNav.inflateMenu(R.menu.bottom_menu_teacher)
                openFragment(TeacherSubjectsFragment()) // Default
            }
            "STUDENT" -> {
                bottomNav.inflateMenu(R.menu.bottom_menu_student)
                openFragment(StudentSubjectsFragment()) // Default
            }
        }

        // 3️⃣ Navegación según selección
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_users -> openFragment(UsersFragment())
                R.id.nav_subjects -> {
                    when (userRole) {
                        "ADMIN" -> openFragment(SubjectsFragment()) // Admin ve todas
                        "TEACHER" -> openFragment(TeacherSubjectsFragment()) // Solo las suyas
                        "STUDENT" -> openFragment(StudentSubjectsFragment()) // Solo matriculadas
                    }
                }
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
