package com.solaceking.snakewars

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/**
 * Android Activity that launches the Snake Wars game
 * Uses LibGDX AndroidApplication to run the game
 */
class MainActivity : AndroidApplication() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configure LibGDX
        val config = AndroidApplicationConfiguration().apply {
            // Use accelerometer and compass for future features
            useAccelerometer = false
            useCompass = false
            
            // Gyroscope not needed
            useGyroscope = false
            
            // Don't wake lock (save battery)
            useWakeLock = false
            
            // Number of samples for MSAA (anti-aliasing)
            // 0 = disabled, 2-4 = good quality
            numSamples = 2
        }
        
        // Initialize and launch the game
        initialize(SnakeWarsGame(), config)
    }
}