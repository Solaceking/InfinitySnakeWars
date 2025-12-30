package com.solaceking.snakewars.entities

import com.badlogic.gdx.graphics.Color
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D

/**
 * Food types with different values
 */
enum class FoodType(
    val displayName: String,
    val segments: Int,
    val points: Int,
    val color: Color,
    val tier: Int
) {
    // Tier 1 - Basic (70% spawn rate)
    APPLE("Apple", 1, 10, Color(0.8f, 0.2f, 0.2f, 1f), 1),
    BANANA("Banana", 1, 15, Color(0.9f, 0.9f, 0.2f, 1f), 1),
    GRAPES("Grapes", 2, 25, Color(0.6f, 0.2f, 0.8f, 1f), 1),
    
    // Tier 2 - Premium (20% spawn rate)
    PIZZA("Pizza", 3, 50, Color(0.9f, 0.6f, 0.2f, 1f), 2),
    BURGER("Burger", 2, 40, Color(0.7f, 0.4f, 0.2f, 1f), 2),
    CAKE("Cake", 4, 75, Color(1f, 0.7f, 0.8f, 1f), 2),
    
    // Tier 3 - Special (10% spawn rate)
    DIAMOND("Diamond", 1, 200, Color(0.4f, 0.8f, 1f, 1f), 3),
    STAR("Star", 0, 100, Color(1f, 1f, 0.4f, 1f), 3),
    FIRE("Fire Fruit", 5, 150, Color(1f, 0.3f, 0.1f, 1f), 3);
    
    companion object {
        /**
         * Get random food type based on tier spawn rates
         */
        fun random(): FoodType {
            val roll = Math.random()
            
            return when {
                roll < Constants.FOOD_TIER1_SPAWN_RATE -> {
                    // Tier 1
                    listOf(APPLE, BANANA, GRAPES).random()
                }
                roll < Constants.FOOD_TIER1_SPAWN_RATE + Constants.FOOD_TIER2_SPAWN_RATE -> {
                    // Tier 2
                    listOf(PIZZA, BURGER, CAKE).random()
                }
                else -> {
                    // Tier 3
                    listOf(DIAMOND, STAR, FIRE).random()
                }
            }
        }
    }
}

/**
 * Food entity that snakes can eat to grow
 */
class Food(
    val position: Vector2D,
    val type: FoodType = FoodType.APPLE,
    var fromDeath: Boolean = false  // Spawned from dead snake?
) {
    var alive = true
    var pulseTimer = 0f  // For pulsing animation
    
    /**
     * Update food (for animations)
     */
    fun update(deltaTime: Float) {
        pulseTimer += deltaTime * 3f  // Pulse 3 times per second
    }
    
    /**
     * Get pulse scale (for animation)
     */
    fun getPulseScale(): Float {
        val pulse = Math.sin(pulseTimer.toDouble()).toFloat()
        return 1f + pulse * 0.2f  // Scale between 0.8 and 1.2
    }
    
    /**
     * Consume this food
     */
    fun consume() {
        alive = false
    }
}
