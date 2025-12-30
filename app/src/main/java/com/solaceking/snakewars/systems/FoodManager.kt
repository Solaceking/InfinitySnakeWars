package com.solaceking.snakewars.systems

import com.solaceking.snakewars.entities.Food
import com.solaceking.snakewars.entities.FoodType
import com.solaceking.snakewars.entities.Snake
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D
import kotlin.random.Random

/**
 * Food Management System
 * Handles food spawning, density management, and death explosion food
 */
class FoodManager(
    private val arenaWidth: Float = Constants.ARENA_WIDTH,
    private val arenaHeight: Float = Constants.ARENA_HEIGHT
) {
    private val foods = mutableListOf<Food>()
    private val spawnCheckInterval = 2f  // Check density every 2 seconds
    private var spawnTimer = 0f
    
    /**
     * Get all active foods
     */
    fun getFoods(): List<Food> = foods.filter { it.alive }
    
    /**
     * Update food system
     */
    fun update(deltaTime: Float) {
        // Update all foods (for animations)
        foods.forEach { it.update(deltaTime) }
        
        // Remove consumed foods
        foods.removeAll { !it.alive }
        
        // Check if we need to spawn more food
        spawnTimer += deltaTime
        if (spawnTimer >= spawnCheckInterval) {
            spawnTimer = 0f
            maintainFoodDensity()
        }
    }
    
    /**
     * Maintain target food density in arena
     */
    private fun maintainFoodDensity() {
        val totalTiles = (arenaWidth / Constants.ARENA_TILE_SIZE) * 
                        (arenaHeight / Constants.ARENA_TILE_SIZE)
        val targetFoodCount = (totalTiles * Constants.FOOD_DENSITY_TARGET).toInt()
        val currentFoodCount = foods.count { it.alive }
        
        if (currentFoodCount < targetFoodCount) {
            val toSpawn = targetFoodCount - currentFoodCount
            spawnFoodBatch(toSpawn.coerceAtMost(50))  // Max 50 at once
        }
    }
    
    /**
     * Spawn a batch of random food
     */
    private fun spawnFoodBatch(count: Int) {
        repeat(count) {
            spawnRandomFood()
        }
    }
    
    /**
     * Spawn a single random food item
     */
    fun spawnRandomFood(): Food {
        val position = findSafeSpawnPosition()
        val type = FoodType.random()
        val food = Food(position, type)
        foods.add(food)
        return food
    }
    
    /**
     * Find a safe position to spawn food (not inside snakes, not too close to other food)
     */
    private fun findSafeSpawnPosition(): Vector2D {
        var attempts = 0
        val maxAttempts = 20
        
        while (attempts < maxAttempts) {
            val x = Random.nextFloat() * arenaWidth
            val y = Random.nextFloat() * arenaHeight
            val position = Vector2D(x, y)
            
            // Check if too close to existing food (anti-clustering)
            var tooClose = false
            for (food in foods) {
                if (food.alive && position.distanceTo(food.position) < Constants.ARENA_TILE_SIZE * 5) {
                    tooClose = true
                    break
                }
            }
            
            if (!tooClose) {
                return position
            }
            
            attempts++
        }
        
        // If can't find safe spot, just spawn randomly
        return Vector2D(
            Random.nextFloat() * arenaWidth,
            Random.nextFloat() * arenaHeight
        )
    }
    
    /**
     * Spawn food from dead snake (death explosion)
     */
    fun spawnFoodFromDeath(snake: Snake) {
        val segments = snake.segments
        val conversionRate = Constants.DEATH_FOOD_CONVERSION_RATE
        val foodCount = (segments.size * conversionRate).toInt()
        
        // Create food at segment positions
        for (i in 0 until foodCount.coerceAtMost(segments.size)) {
            val segment = segments[i]
            val food = Food(
                position = segment.position.copy(),
                type = FoodType.APPLE,  // Death food is basic tier
                fromDeath = true
            )
            foods.add(food)
        }
    }
    
    /**
     * Remove a food item
     */
    fun removeFood(food: Food) {
        food.consume()
    }
    
    /**
     * Clear all food
     */
    fun clear() {
        foods.clear()
    }
    
    /**
     * Get food count
     */
    fun getFoodCount(): Int = foods.count { it.alive }
}
