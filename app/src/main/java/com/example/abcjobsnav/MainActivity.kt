package com.example.abcjobsnav

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.abcjobsnav.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.abcjobsnav.R

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

        //binding.myToolbar.setTitleTextColor(Color.WHITE) //Color.parseColor("#00FF00") "#00FF0000"
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        Log.d("act", navController.toString())

        val navView: BottomNavigationView = binding.navView

        val navController2 = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
            )
        )
        setupActionBarWithNavController(navController2, appBarConfiguration)
        navView.setupWithNavController(navController2)

        //finishAffinity()
        val width = this.resources.configuration.screenWidthDp
        val height = this.resources.configuration.screenHeightDp
        val widthPixels = this.resources.displayMetrics.widthPixels
        val heightPixels = this.resources.displayMetrics.heightPixels
        val wtb = binding.myToolbar.width
        val htb = binding.myToolbar.height
        val bwtb = binding.navView.width
        val bhtb = binding.navView.height
        val widthScroll=binding.myScroll.width
        Log.d("Testing width scroll", widthScroll.toString())
        val myHeightScroll=height-112-10
        Log.d("Testing myHeightScroll", myHeightScroll.toString())
        val myHeightScrollPixels=((myHeightScroll*heightPixels)/height)
        Log.d("Testing Scroll widthPixels ", myHeightScrollPixels.toString())

        //PercentRelativeLayout
        val paramScroll=binding.myScroll.layoutParams
        paramScroll.height=myHeightScrollPixels
        binding.myScroll.layoutParams = paramScroll

        //binding.myScroll.setLayoutParams(ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, myHeightScrollPixels-100))
        Log.d("Testing width", width.toString())
        Log.d("Testing height", height.toString())
        Log.d("Testing widthPixels", widthPixels.toString())
        Log.d("Testing heightPixels", heightPixels.toString())
        Log.d("Testing width toolbar", wtb.toString())
        Log.d("Testing height toolbar", htb.toString())
        Log.d("Testing width bottom", bwtb.toString())
        Log.d("Testing height bottom", bhtb.toString())

        //setSupportActionBar(binding.my_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        supportActionBar!!.title = getString(R.string.abc_jobs)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.language_change -> {
                Toast.makeText(this, "Language Change", Toast.LENGTH_SHORT).show()
            }
            R.id.action_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            R.id.action_exit -> {
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
            R.id.action_switch_layout -> {
                Toast.makeText(this, "Change", Toast.LENGTH_SHORT).show()
                // Create an intent with a destination of the other Activity
                //val intent = Intent(this, RetrofitActivity::class.java)
                //startActivity(intent)
                //return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}

