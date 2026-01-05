package com.ijakaa.elly.afroteso

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ijakaa.elly.afroteso.audio.AudioEngine

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // links to activity_main.xml

        // Initialize audio engine
        AudioEngine.init(this)

        // Button references
        val btnStart = findViewById<Button>(R.id.btnDrumStart)
        val btnStop = findViewById<Button>(R.id.btnDrumStop)

        // Button actions
        btnStart.setOnClickListener {
            AudioEngine.playDrum("djembe") // example, can add more drums
        }

        btnStop.setOnClickListener {
            AudioEngine.stopAll()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AudioEngine.release()
    }
}
