package ir.mahan.wikifoodia.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.HiltAndroidApp
import ir.mahan.wikifoodia.R
import ir.mahan.wikifoodia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // Binding object
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!

    //Other
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // NavHost initialization
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}