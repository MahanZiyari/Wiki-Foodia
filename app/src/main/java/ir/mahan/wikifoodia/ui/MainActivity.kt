package ir.mahan.wikifoodia.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Binding object
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    //Other
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        // NavHost initialization
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        // Bottom nav initialization
        binding.mainBottomNav.run {
            background = null
            setupWithNavController(navHost.navController)
        }
        // App Bar Visibility Handling
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> visibilityBottomMenu(false)
                R.id.registerFragment -> visibilityBottomMenu(false)
                R.id.detailFragment -> visibilityBottomMenu(false)
                R.id.stepsFragment -> visibilityBottomMenu(false)
                R.id.webViewFragment -> visibilityBottomMenu(false)
                else -> visibilityBottomMenu(true)
            }
        }
        // Opening Menu Fragment
        binding.mainFabMenu.setOnClickListener {
            navHost.navController.navigate(R.id.action_to_menuFragment)
        }
    }



    private fun visibilityBottomMenu(isVisibility: Boolean) {
        binding.apply {
            if (isVisibility) {
                mainAppBar.isVisible = true
                mainFabMenu.isVisible = true
            } else {
                mainAppBar.isVisible = false
                mainFabMenu.isVisible = false
            }
        }
    }

    //Calligraphy
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}