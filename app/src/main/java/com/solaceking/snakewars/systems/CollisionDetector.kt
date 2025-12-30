package com.solaceking.snakewars.systems

import com.solaceking.snakewars.entities.Food
import com.solaceking.snakewars.entities.Snake
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D
import kotlin.math.floor

/**
 * Spatial Grid for efficient collision detection
 * Divides arena into cells to avoid O(n²) checks
 */
class SpatialGrid(
    private val arenaWidth: Float,
    private val arenaHeight: Float,
    private val cellSize: Int = Constants.SPATIAL_GRID_CELL_SIZE
) {
    private val cells = mutableMapOf<Pair<Int, Int>, MutableList<Snake>>()
    private val cols = (arenaWidth / cellSize).toInt() + 1
    private val rows = (arenaHeight / cellSize).toInt() + 1
    
    /**
     * Clear all cells
     */
    fun clear() {
        cells.clear()
    }
    
    /**
     * Insert snake into grid
     */
    fun insert(snake: Snake) {
        val head = snake.getHeadPosition()
        val cellX = floor(head.x / cellSize).toInt()
        val cellY = floor(head.y / cellSize).toInt()
        val key = Pair(cellX, cellY)
        
        cells.getOrPut(key) { mutableListOf() }.add(snake)
    }
    
    /**
     * Get snakes near a position (checks 3×3 grid around position)
     */
    fun getNearby(position: Vector2D): List<Snake> {
        val cellX = floor(position.x / cellSize).toInt()
        val cellY = floor(position.y / cellSize).toInt()
        
        val nearby = mutableListOf<Snake>()
        
        // Check 3×3 grid of cells
        for (dx in -1..1) {
            for (dy in -1..1) {
                val key = Pair(cellX + dx, cellY + dy)
                cells[key]?.let { nearby.addAll(it) }
            }
        }
        
        return nearby
    }
}

/**
 * Collision Detection System
 * Handles all collision checks: snake vs wall, snake vs food, snake vs snake
 */
class CollisionDetector(
    private val arenaWidth: Float = Constants.ARENA_WIDTH,
    private val arenaHeight: Float = Constants.ARENA_HEIGHT
) {
    private val spatialGrid = SpatialGrid(arenaWidth, arenaHeight)
    
    /**
     * Check if circles overlap
     */
    private fun circlesOverlap(
        pos1: Vector2D,
        radius1: Float,
        pos2: Vector2D,
        radius2: Float
    ): Boolean {
        val distance = pos1.distanceTo(pos2)
        return distance < (radius1 + radius2)
    }
    
    /**
     * Check wall collision for a snake
     */
    fun checkWallCollision(snake: Snake): Boolean {
        if (!snake.alive || snake.invulnerable) return false
        
        val head = snake.getHeadPosition()
        val radius = Constants.SNAKE_HEAD_RADIUS
        
        return head.x - radius < 0 ||
               head.x + radius > arenaWidth ||
               head.y - radius < 0 ||
               head.y + radius > arenaHeight
    }
    
    /**
     * Check food collision for a snake
     */
    fun checkFoodCollision(snake: Snake, foods: List<Food>): Food? {
        if (!snake.alive) return null
        
        val head = snake.getHeadPosition()
        
        for (food in foods) {
            if (!food.alive) continue
            
            if (circlesOverlap(
                head, Constants.SNAKE_HEAD_RADIUS,
                food.position, Constants.FOOD_RADIUS
            )) {
                return food
            }
        }
        
        return null
    }
    
    /**
     * Check head-to-head collision between two snakes
     */
    fun checkHeadToHeadCollision(snake1: Snake, snake2: Snake): Boolean {
        if (!snake1.alive || !snake2.alive) return false
        if (snake1.invulnerable || snake2.invulnerable) return false
        
        val head1 = snake1.getHeadPosition()
        val head2 = snake2.getHeadPosition()
        
        return circlesOverlap(
            head1, Constants.SNAKE_HEAD_RADIUS,
            head2, Constants.SNAKE_HEAD_RADIUS
        )
    }
    
    /**
     * Check if snake head hits another snake's body
     */
    fun checkBodyCollision(snake: Snake, other: Snake): Boolean {
        if (!snake.alive || !other.alive) return false
        if (snake.invulnerable) return false
        
        val head = snake.getHeadPosition()
        
        // Check against all body segments (skip head)
        for (i in 1 until other.segments.size) {
            val segment = other.segments[i]
            
            if (circlesOverlap(
                head, Constants.SNAKE_HEAD_RADIUS,
                segment.position, Constants.SNAKE_BODY_RADIUS
            )) {
                return true
            }
        }
        
        return false
    }
    
    /**
     * Check all collisions for a snake against other snakes
     * Returns CollisionResult with details
     */
    fun checkSnakeCollisions(
        snake: Snake,
        allSnakes: List<Snake>
    ): CollisionResult {
        if (!snake.alive || snake.invulnerable) {
            return CollisionResult.None
        }
        
        // Update spatial grid
        spatialGrid.clear()
        for (s in allSnakes) {
            if (s.alive) {
                spatialGrid.insert(s)
            }
        }
        
        // Get nearby snakes
        val nearby = spatialGrid.getNearby(snake.getHeadPosition())
        
        // Check head-to-head collisions first
        for (other in nearby) {
            if (other === snake) continue
            
            if (checkHeadToHeadCollision(snake, other)) {
                return CollisionResult.HeadToHead(other)
            }
        }
        
        // Check body collisions
        for (other in nearby) {
            if (other === snake) continue
            
            if (checkBodyCollision(snake, other)) {
                return CollisionResult.HitBody(other)
            }
        }
        
        return CollisionResult.None
    }
    
    /**
     * Collision result sealed class
     */
    sealed class CollisionResult {
        object None : CollisionResult()
        data class HeadToHead(val opponent: Snake) : CollisionResult()
        data class HitBody(val bodyOwner: Snake) : CollisionResult()
    }
}
