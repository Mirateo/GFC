package gfc.frontend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.children
import com.google.android.material.navigation.NavigationView
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.FamilyController
import gfc.frontend.controllers.RewardsController
import gfc.frontend.controllers.TasksController
import gfc.frontend.ui.main.SectionsPagerAdapter
import gfc.frontend.databinding.ActivityMainBinding
import android.widget.LinearLayout


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navPoints: TextView
    lateinit var coins: TextView

    val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            refreshLists()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FamilyController.init(applicationContext)
        TasksController.init(applicationContext)
        RewardsController.init(applicationContext)
        // Initialize lists
        refreshLists()

        setSupportActionBar(binding.activityMainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val coin = binding.coin.layoutParams

        coin.width = (coin.width * 0.75).toInt()
        coin.height = (coin.height * 0.75).toInt()

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        refreshUserInfo()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> binding.drawerLayout.closeDrawer(GravityCompat.START)
                R.id.nav_profile -> {
                    if(AuthorizationController.userIsParent) {
                        val intent = Intent(this, SettingsActivity::class.java)
                        intent.putExtra("family", false)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    } else {
                        Toast.makeText(applicationContext, "Poproś rodzica o zmianę ustawień.",
                            android.widget.Toast.LENGTH_LONG
                        ).show()
                    }
                }
                R.id.nav_family -> {
                    if(AuthorizationController.userIsParent){
                        val intent = Intent(this, SettingsActivity::class.java)
                        intent.putExtra("family", true)
                        startActivity(intent)
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    else {
                        Toast.makeText(applicationContext, "Poproś rodzica o zmianę ustawień.", LENGTH_LONG).show()
                    }
                }
                R.id.nav_logout -> {
                    getSharedPreferences("userInfo", MODE_PRIVATE).edit().clear().apply()
                    getSharedPreferences("credentials", MODE_PRIVATE).edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(applicationContext, "Wylogowano poprawnie", LENGTH_SHORT).show()
                    finish()
                }
            }
            true
        }

        // Adder listener
        binding.fab.setOnClickListener {
            if(binding.viewPager.currentItem == 2) {
                val intent = Intent(this, NewTaskActivity::class.java)
                intent.putExtra("rewards", true)
                startActivityForResult(intent, 0)
            }
            else {
                val intent = Intent(this, NewTaskActivity::class.java)
                intent.putExtra("rewards", false)
                startActivityForResult(Intent(this, NewTaskActivity::class.java), 0)
            }
            binding.fabRefresh.performClick()
        }

        // Refresher listener
        binding.fabRefresh.setOnClickListener { view ->
            refreshLists()
            FamilyController.init(this)
            val username =
                getSharedPreferences("userInfo", MODE_PRIVATE).getString("username", "nope:(")
                    .toString()
            notifyPointsUpdated(FamilyController.getMemberById(FamilyController.getChildrenId(username))?.points!!)
            Snackbar.make(view, "Jesteś na bieżąco!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun refreshUserInfo() {
        val username =
            getSharedPreferences("userInfo", MODE_PRIVATE).getString("username", "nope:(")
                .toString()
        val email =
            getSharedPreferences("userInfo", MODE_PRIVATE).getString("email", "nope:(").toString()
        val role =
            getSharedPreferences("userInfo", MODE_PRIVATE).getString("role", "nope:(").toString()
        val friendlyName =
            getSharedPreferences("userInfo", MODE_PRIVATE).getString("friendlyName", "nope:(")
                .toString()
        val points =
            getSharedPreferences("userInfo", MODE_PRIVATE).getLong("points", 0)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)

        val navWelcome: TextView = headerView.findViewById(R.id.nav_welcome_text)
        val navUsername: TextView = headerView.findViewById(R.id.nav_username)
        val navUserEmail: TextView = headerView.findViewById(R.id.nav_email)
        navPoints = headerView.findViewById(R.id.nav_points)
        coins = binding.coins

        navWelcome.text = "Cześć, $friendlyName!"
        navUsername.text = "Nazwa użytkownika: $username"
        navUserEmail.text = "Adres e-mail: $email"

        notifyPointsUpdated(points)
    }

    fun refreshLists() {
        val currentPage = binding.viewPager.currentItem
        binding.viewPager.adapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.tabs.setupWithViewPager(binding.viewPager)
        binding.viewPager.currentItem = currentPage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                refreshLists()
            }
        }
    }

    fun notifyPointsUpdated(points: Long) {
        navPoints.text = points.toString()
        coins.text = points.toString()
    }


    private fun openDrawer() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            openDrawer()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
