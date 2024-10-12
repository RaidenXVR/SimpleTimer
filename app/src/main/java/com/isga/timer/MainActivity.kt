package com.isga.timer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.isga.timer.databinding.ActivityMainBinding
import org.w3c.dom.Text
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use DataBindingUtil to inflate the layout and bind the ViewModel
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Bind the ViewModel to the layout
        binding.timerViewModel = viewModel

        // Set the lifecycle owner to enable LiveData observation
        binding.lifecycleOwner = this

        val startButton = findViewById<Button>(R.id.start_button)
        val stopButton = findViewById<Button>(R.id.stop_button)

        startButton.setOnClickListener {
            val minuteInput = findViewById<EditText>(R.id.minute_input).text.toString()
            val secondInput = findViewById<EditText>(R.id.seconds_input).text.toString()

            var minutesInMillis: Long = if (minuteInput.isEmpty()) 0 else minuteInput.toLong() * 60 * 1000
            var secondsInMillis: Long = if (secondInput.isEmpty()) 3000 else secondInput.toLong() * 1000

            if (secondsInMillis >= 60 * 1000) {
                val extraMinutes = secondsInMillis / (60 * 1000)
                minutesInMillis += extraMinutes * 60 * 1000
                secondsInMillis -= extraMinutes * 60 * 1000
            }

            viewModel.startTimer(minutesInMillis + secondsInMillis)
        }

        stopButton.setOnClickListener {
            viewModel.resetTimer()
        }

        viewModel.timerFinished.observe(this, Observer { isFinishing ->
            if (isFinishing){
                val intent = Intent(this, TimeUp::class.java)
                startActivity(intent)
            }

        })
    }
}