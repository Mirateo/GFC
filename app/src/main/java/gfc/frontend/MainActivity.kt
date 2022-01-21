package gfc.frontend

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import gfc.frontend.controllers.AuthorizationController
import gfc.frontend.controllers.TasksController
import gfc.frontend.dataclasses.ObjectBox
import gfc.frontend.ui.main.SectionsPagerAdapter
import gfc.frontend.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var authController: AuthorizationController
    private lateinit var tasksController: TasksController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.init(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authController = AuthorizationController(applicationContext)
        tasksController = TasksController(applicationContext)

        // Initialize lists
        refreshLists()

        // Adder listener
        binding.fab.setOnClickListener {
            startActivity(Intent(this, NewTaskActivity::class.java))
        }

        // Refresher listener
        binding.fabRefresh.setOnClickListener { view ->
            refreshLists()
            Snackbar.make(view, "Tasks Refreshed!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //Slide menu Listener
        setSupportActionBar(binding.activityMainToolbar)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var username = getSharedPreferences("userInfo", MODE_PRIVATE).getString("username", "nope:(").toString()
        var email = getSharedPreferences("userInfo", MODE_PRIVATE).getString("email", "nope:(").toString()
        var role = getSharedPreferences("userInfo", MODE_PRIVATE).getString("role", "nope:(").toString()
        var friendlyName = getSharedPreferences("userInfo", MODE_PRIVATE).getString("friendlyName", "nope:(").toString()

        val navigationView : NavigationView = findViewById(R.id.nav_view)
        val headerView : View = navigationView.getHeaderView(0)

        val navWelcome : TextView = headerView.findViewById(R.id.nav_welcome_text)
        val navUsername : TextView = headerView.findViewById(R.id.nav_username)
        val navUserEmail : TextView = headerView.findViewById(R.id.nav_email)

        navWelcome.text = "Cześć, $friendlyName!"
        navUsername.text = "Nazwa użytkownika: $username"
        navUserEmail.text = "Adres e-mail: $email"

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> binding.drawerLayout.closeDrawer(GravityCompat.START)
                R.id.nav_profile -> Toast.makeText(applicationContext, "Clicked profile", LENGTH_SHORT).show()
                R.id.nav_logout -> {
                    tasksController.deleteData()
                    getSharedPreferences("userInfo", MODE_PRIVATE).edit().clear().apply()
                    getSharedPreferences("credentials", MODE_PRIVATE).edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(applicationContext, "Poprawnie wylogowano", LENGTH_SHORT).show()
                    finish()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshLists(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this, tasksController, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if(intent.getBooleanExtra("finished", false)){
                    refreshLists()
                }
            }
        }
    }
}
