package id.myeco.myeco.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.myeco.myeco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}